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
 */
public class LevelChunk implements BedrockPacketTranslator {

    private static final int OUT_SIZE = 32768 + 16384 + 16384 + 16384 + 256 + 1024 + 4;
    private static boolean loggedHeader = false;

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
        boolean isDebug = Server.getInstance().getConfig().isDebug();

        // Log first chunk's raw header for diagnostics
        if (isDebug && !loggedHeader) {
            loggedHeader = true;
            StringBuilder hex = new StringBuilder();
            int dumpLen = Math.min(64, raw.length);
            for (int i = 0; i < dumpLen; i++) {
                hex.append(String.format("%02X ", raw[i] & 0xFF));
            }
            Server.getInstance().getLogger().debug(
                "[Chunk] First chunk raw header (" + dumpLen + " bytes): " + hex);
            Server.getInstance().getLogger().debug(
                "[Chunk] BlockMapper=" + (mapper != null ? ("size=" + mapper.size()) : "NULL"));
            try {
                Server.getInstance().getLogger().debug(
                    "[Chunk] subChunksLength=" + packet.getSubChunksLength());
            } catch (Throwable ignored) {
            }
        }

        byte[] payload = convertToColumns(raw, mapper);
        if (payload == null) return;

        FullChunkPacket npk = new FullChunkPacket();
        npk.chunkX = packet.getChunkX();
        npk.chunkZ = packet.getChunkZ();
        npk.order = FullChunkPacket.ORDER_COLUMNS;
        npk.cdata = payload;
        player.sendDataCraftsman(npk);

        if (isDebug) {
            Server.getInstance().getLogger().debug(
                "[Chunk] TX x=" + npk.chunkX + " z=" + npk.chunkZ + " bytes=" + payload.length);
        }
    }

    private static byte[] convertToColumns(byte[] raw, BlockMapper mapper) {
        byte[][] ids = new byte[8][4096];
        byte[][] data = new byte[8][2048];

        int pos = 0;
        int sec = 0;
        boolean loggedFirstSubChunk = false;
        while (pos < raw.length && sec < 8) {
            int ver = raw[pos++] & 0xFF;

            if (!loggedFirstSubChunk) {
                loggedFirstSubChunk = true;
                if (Server.getInstance().getConfig().isDebug()) {
                    Server.getInstance().getLogger().debug(
                        "[Chunk] SubChunk[0] version=0x" + Integer.toHexString(ver));
                }
            }

            if (ver == 0x08 || ver == 0x09) {
                int storages = (ver == 0x09) ? (pos < raw.length ? (raw[pos++] & 0xFF) : 1) : 1;
                for (int s = 0; s < storages; s++) {
                    if (pos >= raw.length) break;
                    int bitsPerBlock = (raw[pos++] & 0xFF) >> 1;
                    int[] palette;
                    int[] indices = new int[4096];

                    if (bitsPerBlock == 0) {
                        int[] pal = readVarInt(raw, pos);
                        pos = pal[1];
                        int palSize = pal[0];
                        palette = new int[palSize];
                        for (int i = 0; i < palSize; i++) {
                            int[] pe = readVarInt(raw, pos);
                            pos = pe[1];
                            palette[i] = pe[0];
                        }
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

                    // Log first sub-chunk's palette for diagnostics
                    if (s == 0 && sec == 0 && Server.getInstance().getConfig().isDebug()) {
                        StringBuilder palStr = new StringBuilder();
                        int showCnt = Math.min(palette.length, 10);
                        for (int i = 0; i < showCnt; i++) {
                            int rt = palette[i];
                            int lid = mapper != null ? mapper.getLegacyId(rt) : -1;
                            palStr.append("rt=").append(rt).append("->leg=").append(lid).append(" ");
                        }
                        Server.getInstance().getLogger().debug(
                            "[Chunk] Palette[" + sec + "] size=" + palette.length + " " + palStr);
                    }

                    if (s == 0) {
                        for (int i = 0; i < 4096; i++) {
                            int palIdx = indices[i];
                            int runtimeId = (palIdx < palette.length) ? palette[palIdx] : 0;
                            int legId, legMeta;
                            if (mapper != null) {
                                legId = mapper.getLegacyId(runtimeId);
                                legMeta = mapper.getLegacyMeta(runtimeId);
                            } else {
                                legId = (runtimeId >= 0 && runtimeId < 256) ? runtimeId : 0;
                                legMeta = 0;
                            }
                            int xi = (i >> 8) & 15;
                            int zi = (i >> 4) & 15;
                            int yi = i & 15;
                            int secIdx = xi | (zi << 4) | (yi << 8);
                            ids[sec][secIdx] = (byte) legId;
                            setNibble(data[sec], secIdx, legMeta);
                        }
                    }
                }
            } else if (ver == 0x00) {
                // Legacy format: 4096 raw block IDs + 2048 metadata nibbles
                if (pos + 6143 >= raw.length) break;
                System.arraycopy(raw, pos, ids[sec], 0, 4096);
                pos += 4096;
                System.arraycopy(raw, pos, data[sec], 0, 2048);
                pos += 2048;
            } else if (ver == 0x01) {
                // Persistence format: same layout as 0x00 but runtime IDs
                if (pos + 6143 >= raw.length) break;
                System.arraycopy(raw, pos, ids[sec], 0, 4096);
                pos += 4096;
                System.arraycopy(raw, pos, data[sec], 0, 2048);
                pos += 2048;
                if (mapper != null) {
                    for (int i = 0; i < 4096; i++) {
                        int rt = ids[sec][i] & 0xFF;
                        ids[sec][i] = (byte) mapper.getLegacyId(rt);
                        setNibble(data[sec], i, mapper.getLegacyMeta(rt));
                    }
                }
            } else {
                break;
            }
            sec++;
        }

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

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 128; y += 2) {
                    int s1 = y >> 4, ly1 = y & 15;
                    int s2 = (y + 1) >> 4, ly2 = (y + 1) & 15;
                    int secIdx1 = x | (z << 4) | (ly1 << 8);
                    int secIdx2 = x | (z << 4) | (ly2 << 8);
                    byte lo = getNibble(data[s1], secIdx1);
                    byte hi = getNibble(data[s2], secIdx2);
                    int ci = (x << 11) | (z << 7) | y;
                    out[off + (ci >> 1)] = (byte) ((hi << 4) | (lo & 0x0F));
                }
            }
        }
        off += 16384;

        java.util.Arrays.fill(out, off, off + 16384, (byte) 0xFF);
        off += 16384;
        off += 16384;

        System.arraycopy(heightMap, 0, out, off, 256);
        off += 256;

        for (int i = 0; i < 256; i++) {
            out[off + 0] = 0x00;
            out[off + 1] = (byte) 0x80;
            out[off + 2] = 0x40;
            out[off + 3] = 0x00;
            off += 4;
        }

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
