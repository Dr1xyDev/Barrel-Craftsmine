package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.FullChunkPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
public class LevelChunk implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket packet = (org.cloudburstmc.protocol.bedrock.packet.LevelChunkPacket) pk;
      FullChunkPacket npk = new FullChunkPacket();
      npk.chunkX = packet.getChunkX();
      npk.chunkZ = packet.getChunkZ();
        npk.cdata = packet.getData().array();
        player.sendDataCraftsman(npk);
    }
  @Override
    public boolean immediate() {
        return true;
    }
}
