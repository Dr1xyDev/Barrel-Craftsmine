package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.ChunkRadiusUpdatedPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
public class ChunkRadiusUpdated implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.ChunkRadiusUpdatedPacket packet = (org.cloudburstmc.protocol.bedrock.packet.ChunkRadiusUpdatedPacket) pk;
    ChunkRadiusUpdatedPacket npk = new ChunkRadiusUpdatedPacket();
    npk.radius = packet.getRadius();
    player.sendDataCraftsman(npk);
  }
}
