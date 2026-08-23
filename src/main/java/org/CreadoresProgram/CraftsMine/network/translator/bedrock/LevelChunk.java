package org.CreadoresProgram.CraftsMine.network.translator.bedrock;

import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.FullChunkPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LevelChunk implements BedrockPacketTranslator {

    // Fixed ORDER_COLUMNS payload size for 0.15.10
    private static final int OUT_SIZE = 32768 + 16384 + 16384 + 16384 + 256 + 1024 + 4;

    @Override
    public boolean immediate() { return true; }

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket packet =
                (org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket) pk;

        byte[] payload = convertToColumns(packet.getData());
        if (payload == null) return;

        FullChunkPacket npk = new FullChunkPacket();
        npk.chunkX  = packet.getChunkX();
        npk.chunkZ  = packet.getChunkZ();
        npk.order   = FullChunkPacket.ORDER_COLUMNS;
        npk.cdata   = payload;
        player.sendDataCraftsman(npk);
    }

    // Converts modern paletted sub-chunks to the 128-block ORDER_COLUMNS format
    private static byte[] convertToColumns(ByteBuf src) {
        if (src == null || src.readableBytes() < 1) return null;

        // Block sections and auxiliary data
        byte[][] blockIds  = new byte[8][4096];
        byte[][] blockData = new byte[8][2048];
        byte[][] skyLight  = new byte[8][2048];
        for (int i = 0; i < 8; i++) java.util.Arrays.fill(skyLight[i], (byte)0xFF);
        byte[][] blkLight  = new byte[8][2048];

        ByteBuf buf = src.duplicate();

        // Read available sub-chunks from the payload
        int subChunkCount = buf.readableBytes() > 0 ? readSubChunks(buf, blockIds, blockData) : 0;

        // Calculate heightmap from the loaded blocks
        byte[] heightMap = new byte[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int maxY = 0;
                for (int sec = 7; sec >= 0; sec--) {
                    if (blockIds[sec] == null) continue;
                    for (int ly = 15; ly >= 0; ly--) {
                        int y = sec * 16 + ly;
                        if (y > 127) continue;
                        int idx = (x & 15) | ((z & 15) << 4) | ((ly & 15) << 8);
                        if (blockIds[sec][idx] != 0) { maxY = y + 1; break; }
                    }
                    if (maxY > 0) break;
                }
                if (maxY > 127) maxY = 127;
                heightMap[(x & 15) + (z & 15) * 16] = (byte) maxY;
            }
        }

        // Build the final ORDER_COLUMNS buffer
        byte[] out = new byte[OUT_SIZE];
        int off = 0;

        // Block IDs
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y++) {
                    int sec = y >> 4;
                    int ly  = y & 15;
                    byte id = (blockIds[sec] != null)
                            ? blockIds[sec][(x & 15) | ((z & 15) << 4) | (ly << 8)]
                            : 0;
                    out[off + ((x << 11) | (z << 7) | y)] = id;
                }
            }
        }
        off += 32768;

        // Block data nibbles
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y += 2) {
                    int sec = y >> 4;
                    int ly  = y & 15;
                    byte lo = 0, hi = 0;
                    if (blockData[sec] != null) {
                        int ni = (x & 15) | ((z & 15) << 4) | (ly << 7);
                        lo = (byte)((blockData[sec][ni >> 1] >> ((ni & 1) << 2)) & 0x0F);
                    }
                    int y2 = y + 1, sec2 = y2 >> 4, ly2 = y2 & 15;
                    if (blockData[sec2] != null) {
                        int ni = (x & 15) | ((z & 15) << 4) | (ly2 << 7);
                        hi = (byte)((blockData[sec2][ni >> 1] >> ((ni & 1) << 2)) & 0x0F);
                    }
                    int ci = (x << 11) | (z << 7) | y;
                    out[off + (ci >> 1)] = (byte)((hi << 4) | (lo & 0x0F));
                }
            }
        }
        off += 16384;

        // Sky light set to full brightness
        java.util.Arrays.fill(out, off, off + 16384, (byte)0xFF);
        off += 16384;

        // Block light set to zero
        off += 16384;

        // Heightmap
        System.arraycopy(heightMap, 0, out, off, 256);
        off += 256;

        // Biome colors
        for (int i = 0; i < 256; i++) {
            out[off]     = 0x00;
            out[off + 1] = (byte)0x80;
            out[off + 2] = 0x40;
            out[off + 3] = 0x00;
            off += 4;
        }

        // Extra data count set to zero
        return out;
    }

    // Reads legacy and paletted sub-chunks
    private static int readSubChunks(ByteBuf buf, byte[][] blockIds, byte[][] blockData) {
        int count = 0;
        while (buf.readableBytes() > 0 && count < 8) {
            if (!buf.isReadable()) break;
            int version = buf.readUnsignedByte();

            if (version == 0x08 || version == 0x09) {
                // Paletted format
                int storageCount = (version == 0x09) ? buf.readUnsignedByte() : 1;
                // Only the first storage contains the base blocks
                for (int s = 0; s < storageCount; s++) {
                    if (!buf.isReadable()) break;
                    int bitsPerBlock = buf.readUnsignedByte() >> 1;
                    if (bitsPerBlock == 0) {
                        // Single-entry palette
                        int paletteSize = readVarInt(buf);
                        int singleId = mapToLegacy(readVarInt(buf));
                        for (int i = 1; i < paletteSize; i++) readVarInt(buf);
                        if (s == 0) {
                            java.util.Arrays.fill(blockIds[count], (byte)(singleId & 0xFF));
                        }
                        continue;
                    }
                    int blocksPerWord = (bitsPerBlock > 0) ? (32 / bitsPerBlock) : 0;
                    if (blocksPerWord == 0) { skipStorage(buf); continue; }
                    int wordCount = (4096 + blocksPerWord - 1) / blocksPerWord;
                    int[] words = new int[wordCount];
                    for (int i = 0; i < wordCount; i++) {
                        if (buf.readableBytes() < 4) break;
                        words[i] = buf.readIntLE();
                    }
                    int paletteSize = readVarInt(buf);
                    int[] palette = new int[paletteSize];
                    for (int i = 0; i < paletteSize; i++) {
                        palette[i] = mapToLegacy(readVarInt(buf));
                    }
                    if (s == 0) {
                        // Unpack XZY blocks
                        int mask = (1 << bitsPerBlock) - 1;
                        for (int i = 0; i < 4096; i++) {
                            int word  = i / blocksPerWord;
                            int shift = (i % blocksPerWord) * bitsPerBlock;
                            int palIdx = (words[word] >> shift) & mask;
                            int legacyId = (palIdx < palette.length) ? palette[palIdx] : 0;
                            // Convert XZY index to section index
                            int yi = (i >> 8) & 15, zi = (i >> 4) & 15, xi = i & 15;
                            blockIds[count][(xi) | (zi << 4) | (yi << 8)] = (byte)(legacyId & 0xFF);
                        }
                    }
                }
            } else if (version == 0x00) {
                // Legacy format
                if (buf.readableBytes() < 6144) break;
                buf.readBytes(blockIds[count]);
                buf.readBytes(blockData[count]);
            } else {
                // Unknown version
                break;
            }
            count++;
        }
        return count;
    }

    // Skips a paletted storage without parsing it
    private static void skipStorage(ByteBuf buf) {
        if (buf.readableBytes() < 1) return;
        int bitsPerBlock = buf.readUnsignedByte() >> 1;
        if (bitsPerBlock == 0) {
            int ps = readVarInt(buf);
            for (int i = 0; i < ps; i++) readVarInt(buf);
            return;
        }
        int blocksPerWord = 32 / bitsPerBlock;
        int wordCount = (4096 + blocksPerWord - 1) / blocksPerWord;
        buf.skipBytes(Math.min(wordCount * 4, buf.readableBytes()));
        int ps = readVarInt(buf);
        for (int i = 0; i < ps; i++) readVarInt(buf);
    }

    // Reads an unsigned VarInt
    private static int readVarInt(ByteBuf buf) {
        int value = 0, shift = 0;
        while (buf.isReadable() && shift < 35) {
            int b = buf.readUnsignedByte();
            value |= (b & 0x7F) << shift;
            if ((b & 0x80) == 0) break;
            shift += 7;
        }
        return value;
    }

    // Maps runtime palette IDs to legacy 0.15.x IDs
    private static int mapToLegacy(int runtimeId) {
        if (runtimeId >= 0 && runtimeId < 256) return runtimeId;
        return 0;
    }
                        }
