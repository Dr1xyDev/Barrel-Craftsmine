package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.RequestChunkRadiusPacket;
public class RequestChunkRadius implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.RequestChunkRadiusPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.RequestChunkRadiusPacket) pk;
    RequestChunkRadiusPacket chunkRadiusPacket = new RequestChunkRadiusPacket();
    chunkRadiusPacket.setRadius(packet.radius);
    player.getBedrockClientSession().sendPacketImmediately(chunkRadiusPacket);
  }
}
