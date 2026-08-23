package org.CreadoresProgram.CraftsMine.network.translator.bedrock;

import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.FullChunkPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.CreadoresProgram.CraftsMine.utils.BlockMapper;
import io.netty.buffer.ByteBuf;

/**
 * Translates modern LevelChunkPacket to the ORDER_COLUMNS format of MCPE 0.15.10.
 * Modern Bedrock sub-chunk block index is XZY: (x << 8) | (z << 4) | y.
 * Legacy MCPE section index is XZY: (x) | (z << 4) | (y << 8).
 * Legacy column index is: (x << 11) | (z << 7) | y, y=0..127.
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

        BlockMapper mapper = player.getBlockMapper();
        byte[] payload = convertToColumns(raw, mapper);
        if (payload == null) return;

        FullChunkPacket npk = new FullChunkPacket();
        npk.chunkX = packet.getChunkX();
        npk.chunkZ = packet.getChunkZ();
        npk.order = FullChunkPacket.ORDER_COLUMNS;
        npk.cdata = payload;
        player.sendDataCraftsman(npk);

        if (Server.getInstance().getConfig().isDebug()) {
            Server.getInstance().getLogger().debug(
                "[Chunk] TX x=" + npk.chunkX + " z=" + npk.chunkZ + " bytes=" + payload.length);
        }
    }

    private static byte[] convertToColumns(byte[] raw, BlockMapper mapper) {
        // 8 sections for 128-height world (y=0..127, section = y>>4)
        byte[][] ids = new byte[8][4096];
        byte[][] data = new byte[8][2048];

        int pos = 0;
        int sec = 0;
        while (pos < raw.length && sec < 8) {
            int ver = raw[pos++] & 0xFF;

            if (ver == 0x08 || ver == 0x09) {
                int storages = (ver == 0x09) ? (pos < raw.length ? (raw[pos++] & 0xFF) : 1) : 1;
                for (int s = 0; s < storages; s++) {
                    if (pos >= raw.length) break;
                    int bitsPerBlock = (raw[pos++] & 0xFF) >> 1;
                    int[] palette;
                    int[] indices = new int[4096];

                    if (bitsPerBlock == 0) {
                        // No block data words, just palette with 1 entry
                        int[] pal = readVarInt(raw, pos);
                        pos = pal[1];
                        int palSize = pal[0];
                        palette = new int[palSize];
                        for (int i = 0; i < palSize; i++) {
                            int[] pe = readVarInt(raw, pos);
                            pos = pe[1];
                            palette[i] = pe[0];
                        }
                        // All blocks use palette[0]
                        for (int i = 0; i < 4096; i++) indices[i] = 0;
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
                        int[] pal = readVarInt(raw, pos);
                        pos = pal[1];
                        int palSize = pal[0];
                        palette = new int[palSize];
                        for (int i = 0; i < palSize; i++) {
                            int[] pe = readVarInt(raw, pos);
                            pos = pe[1];
                            palette[i] = pe[0];
                        }
                    }

                    // Only use first storage for block IDs and data
                    if (s == 0) {
                        for (int i = 0; i < 4096; i++) {
                            int palIdx = indices[i];
                            int runtimeId = (palIdx < palette.length) ? palette[palIdx] : 0;
                            int legId, legMeta;
                            if (mapper != null) {
                                legId = mapper.getLegacyId(runtimeId);
                                legMeta = mapper.getLegacyMeta(runtimeId);
                            } else {
                                // Fallback: runtime IDs < 256 map directly (old behavior)
                                legId = (runtimeId >= 0 && runtimeId < 256) ? runtimeId : 0;
                                legMeta = 0;
                            }
                            // Modern Bedrock sub-chunk index: (x << 8) | (z << 4) | y
                            int xi = (i >> 8) & 15;
                            int zi = (i >> 4) & 15;
                            int yi = i & 15;
                            // Legacy section index: x | (z << 4) | (y << 8)
                            int secIdx = xi | (zi << 4) | (yi << 8);
                            ids[sec][secIdx] = (byte) legId;
                            setNibble(data[sec], secIdx, legMeta);
                        }
                    }
                }
            } else if (ver == 0x00) {
                // Legacy section: 4096 IDs + 2048 data nibbles
                if (pos + 6143 >= raw.length) break;
                System.arraycopy(raw, pos, ids[sec], 0, 4096);
                pos += 4096;
                System.arraycopy(raw, pos, data[sec], 0, 2048);
                pos += 2048;
            } else if (ver == 0x01) {
                // Persistence format: 4096 IDs + 2048 data nibbles (runtime IDs)
                if (pos + 6143 >= raw.length) break;
                System.arraycopy(raw, pos, ids[sec], 0, 4096);
                pos += 4096;
                System.arraycopy(raw, pos, data[sec], 0, 2048);
                pos += 2048;
                // Need to convert runtime IDs to legacy IDs
                if (mapper != null) {
                    for (int i = 0; i < 4096; i++) {
                        int rt = ids[sec][i] & 0xFF;
                        ids[sec][i] = (byte) mapper.getLegacyId(rt);
                        setNibble(data[sec], i, mapper.getLegacyMeta(rt));
                    }
                }
            } else {
                // Unknown version, stop processing
                break;
            }
            sec++;
        }

        // Build heightmap from block data
        byte[] heightMap = new byte[256];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int maxY = 0;
                for (int s = 7; s >= 0; s--) {
                    for (int ly = 15; ly >= 0; ly--) {
                        int y = s * 16 + ly;
                        if (y > 127) continue;
                        int secIdx = x | (z << 4) | (ly << 8);
                        if (ids[s][secIdx] != 0) {
                            maxY = Math.min(y + 1, 127);
                            break;
                        }
                    }
                    if (maxY > 0) break;
                }
                heightMap[(x & 15) + (z & 15) * 16] = (byte) maxY;
            }
        }

        byte[] out = new byte[OUT_SIZE];
        int off = 0;

        // Block IDs: column index = (x << 11) | (z << 7) | y, y=0..127
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y++) {
                    int s = y >> 4, ly = y & 15;
                    int secIdx = x | (z << 4) | (ly << 8);
                    out[off + ((x << 11) | (z << 7) | y)] = ids[s][secIdx];
                }
            }
        }
        off += 32768;

        // Block data nibbles: column index = (x << 11) | (z << 7) | y
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y += 2) {
                    int s1 = y >> 4, ly1 = y & 15;
                    int s2 = (y + 1) >> 4, ly2 = (y + 1) & 15;
                    int secIdx1 = x | (z << 4) | (ly1 << 8);
                    int secIdx2 = x | (z << 4) | (ly2 << 8);
                    int ni1 = secIdx1;
                    int ni2 = secIdx2;
                    byte lo = getNibble(data[s1], ni1);
                    byte hi = getNibble(data[s2], ni2);
                    int ci = (x << 11) | (z << 7) | y;
                    out[off + (ci >> 1)] = (byte) ((hi << 4) | (lo & 0x0F));
                }
            }
        }
        off += 16384;

        // Sky light: full bright
        java.util.Arrays.fill(out, off, off + 16384, (byte) 0xFF);
        off += 16384;

        // Block light: zero (already zero from allocation)
        off += 16384;

        // Heightmap: 256 bytes
        System.arraycopy(heightMap, 0, out, off, 256);
        off += 256;

        // Biome colors: 1024 bytes (256 * 4), plains green
        for (int i = 0; i < 256; i++) {
            out[off + 0] = 0x00; // R
            out[off + 1] = (byte) 0x80; // G
            out[off + 2] = 0x40; // B
            out[off + 3] = 0x00; // unused
            off += 4;
        }

        // Extra data count: LInt32 = 0
        out[off + 0] = 0;
        out[off + 1] = 0;
        out[off + 2] = 0;
        out[off + 3] = 0;

        return out;
    }

    private static byte getNibble(byte[] arr, int idx) {
        if (arr == null) return 0;
        int byteIdx = idx >> 1;
        if (byteIdx >= arr.length) return 0;
        byte b = arr[byteIdx];
        return (idx & 1) != 0 ? (byte) ((b >> 4) & 0x0F) : (byte) (b & 0x0F);
    }

    private static void setNibble(byte[] arr, int idx, int val) {
        if (arr == null) return;
        int byteIdx = idx >> 1;
        if (byteIdx >= arr.length) return;
        val &= 0x0F;
        if ((idx & 1) != 0) {
            arr[byteIdx] = (byte) ((arr[byteIdx] & 0x0F) | (val << 4));
        } else {
            arr[byteIdx] = (byte) ((arr[byteIdx] & 0xF0) | val);
        }
    }

    // Returns [value, newOffset]
    private static int[] readVarInt(byte[] buf, int pos) {
        int val = 0, shift = 0;
        while (pos < buf.length && shift < 35) {
            int b = buf[pos++] & 0xFF;
            val |= (b & 0x7F) << shift;
            if ((b & 0x80) == 0) break;
            shift += 7;
        }
        return new int[]{val, pos};
    }
}
