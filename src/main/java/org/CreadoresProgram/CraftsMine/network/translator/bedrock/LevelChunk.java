package org.CreadoresProgram.CraftsMine.network.translator.bedrock;

import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.FullChunkPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import io.netty.buffer.ByteBuf;

/**
 * Translates modern LevelChunkPacket to the ORDER_COLUMNS format of MCPE 0.15.10
 */
public class LevelChunk implements BedrockPacketTranslator {

    private static final int OUT_SIZE = 32768 + 16384 + 16384 + 16384 + 256 + 1024 + 4;

    @Override
    public boolean immediate() {
        return true;
    }

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket packet =
                (org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket) pk;

        ByteBuf data = packet.getData();
        if (data == null || !data.isReadable()) return;

        int len = data.readableBytes();
        byte[] raw = new byte[len];
        data.getBytes(data.readerIndex(), raw);

        byte[] payload = convertToColumns(raw);
        if (payload == null) return;

        FullChunkPacket npk = new FullChunkPacket();
        npk.chunkX = packet.getChunkX();
        npk.chunkZ = packet.getChunkZ();
        npk.order = FullChunkPacket.ORDER_COLUMNS;
        npk.cdata = payload;
        player.sendDataCraftsman(npk);
    }

    private static byte[] convertToColumns(byte[] raw) {
        // Section data is limited to 8 sections from Y 0 to 127
        byte[][] ids = new byte[8][4096];
        byte[][] data = new byte[8][2048];

        int pos = 0;
        int sec = 0;
        while (pos < raw.length && sec < 8) {
            if (pos >= raw.length) break;
            int ver = raw[pos++] & 0xFF;

            if (ver == 0x08 || ver == 0x09) {
                int storages = (ver == 0x09) ? (raw[pos++] & 0xFF) : 1;
                for (int s = 0; s < storages; s++) {
                    if (pos >= raw.length) break;
                    int bitsPerBlock = (raw[pos++] & 0xFF) >> 1;
                    int[] palette;
                    int[] indices = new int[4096];

                    if (bitsPerBlock == 0) {
                        int palSize = readVarInt(raw, pos);
                        pos += varIntSize(raw, pos);
                        palette = new int[palSize];
                        for (int i = 0; i < palSize; i++) {
                            palette[i] = readVarInt(raw, pos);
                            pos += varIntSize(raw, pos);
                        }
                    } else {
                        int blocksPerWord = 32 / bitsPerBlock;
                        int wordCount = (4096 + blocksPerWord - 1) / blocksPerWord;
                        int mask = (1 << bitsPerBlock) - 1;
                        int idx = 0;
                        for (int w = 0; w < wordCount && pos + 3 < raw.length; w++) {
                            int word = (raw[pos] & 0xFF)
                                    | ((raw[pos + 1] & 0xFF) << 8)
                                    | ((raw[pos + 2] & 0xFF) << 16)
                                    | ((raw[pos + 3] & 0xFF) << 24);
                            pos += 4;
                            for (int k = 0; k < blocksPerWord && idx < 4096; k++, idx++) {
                                indices[idx] = (word >> (k * bitsPerBlock)) & mask;
                            }
                        }
                        int palSize = readVarInt(raw, pos);
                        pos += varIntSize(raw, pos);
                        palette = new int[palSize];
                        for (int i = 0; i < palSize; i++) {
                            palette[i] = readVarInt(raw, pos);
                            pos += varIntSize(raw, pos);
                        }
                    }

                    if (s == 0) {
                        // Modern format is YZX while the destination section format is XZY
                        for (int i = 0; i < 4096; i++) {
                            int palIdx = (bitsPerBlock == 0) ? 0 : indices[i];
                            int legId = (palIdx < palette.length)
                                    ? toLegacyId(palette[palIdx]) : 0;
                            int yi = (i >> 8) & 15;
                            int zi = (i >> 4) & 15;
                            int xi = i & 15;
                            ids[sec][xi | (zi << 4) | (yi << 8)] = (byte) legId;
                        }
                    }
                }
            } else if (ver == 0x00) {
                // Legacy section contains 4096 IDs followed by 2048 data nibbles
                if (pos + 6143 >= raw.length) break;
                System.arraycopy(raw, pos, ids[sec], 0, 4096);
                pos += 4096;
                System.arraycopy(raw, pos, data[sec], 0, 2048);
                pos += 2048;
            } else {
                break;
            }
            sec++;
        }

        byte[] heightMap = new byte[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int maxY = 0;
                outer:
                for (int s = 7; s >= 0; s--) {
                    for (int ly = 15; ly >= 0; ly--) {
                        int y = s * 16 + ly;
                        if (y > 127) continue;
                        if (ids[s][x | (z << 4) | (ly << 8)] != 0) {
                            maxY = Math.min(y + 1, 127);
                            break outer;
                        }
                    }
                }
                heightMap[(x & 15) + (z & 15) * 16] = (byte) maxY;
            }
        }

        byte[] out = new byte[OUT_SIZE];
        int off = 0;

        // Block IDs use the index formula x<<11|z<<7|y
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y++) {
                    int s = y >> 4, ly = y & 15;
                    out[off + ((x << 11) | (z << 7) | y)] =
                            ids[s][x | (z << 4) | (ly << 8)];
                }
            }
        }
        off += 32768;

        // Block data is stored as nibbles
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y += 2) {
                    int s = y >> 4, ly = y & 15;
                    int s2 = (y + 1) >> 4, ly2 = (y + 1) & 15;
                    int ni = x | (z << 4) | (ly << 7);
                    int ni2 = x | (z << 4) | (ly2 << 7);
                    byte lo = (byte) ((data[s][ni >> 1] >> ((ni & 1) << 2)) & 0x0F);
                    byte hi = (byte) ((data[s2][ni2 >> 1] >> ((ni2 & 1) << 2)) & 0x0F);
                    int ci = (x << 11) | (z << 7) | y;
                    out[off + (ci >> 1)] = (byte) ((hi << 4) | (lo & 0x0F));
                }
            }
        }
        off += 16384;

        // Sky light is full bright
        java.util.Arrays.fill(out, off, off + 16384, (byte) 0xFF);
        off += 16384;

        // Block light remains zero
        off += 16384;

        // Heightmap
        System.arraycopy(heightMap, 0, out, off, 256);
        off += 256;

        // Biome colors use plains green for every column
        for (int i = 0; i < 256; i++) {
            out[off + 1] = (byte) 0x80;
            out[off + 2] = 0x40;
            off += 4;
        }

        // Extra data remains zero

        return out;
    }

    // Reads an unsigned VarInt value
    private static int readVarInt(byte[] buf, int pos) {
        int val = 0, shift = 0;
        while (pos < buf.length && shift < 35) {
            int b = buf[pos++] & 0xFF;
            val |= (b & 0x7F) << shift;
            if ((b & 0x80) == 0) break;
            shift += 7;
        }
        return val;
    }

    // Returns the number of bytes occupied by the VarInt at the given position
    private static int varIntSize(byte[] buf, int pos) {
        int size = 0;
        while (pos < buf.length && size < 5) {
            size++;
            if ((buf[pos++] & 0x80) == 0) break;
        }
        return size;
    }

    // Converts runtime IDs to legacy IDs for 0.15.x
    private static int toLegacyId(int runtimeId) {
        if (runtimeId >= 0 && runtimeId < 256) return runtimeId;
        return 0;
    }
}
