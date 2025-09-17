package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.ResourcePackClientResponsePacket;

public class ResourcePacksInfo implements BedrockPacketTranslator{
  @Override
  public boolean immediate(){
    return true;
  }
  @Override
  public void translate(BedrockPacket pk, Player player){
    ResourcePackClientResponsePacket respon = new ResourcePackClientResponsePacket();
    respon.setStatus(ResourcePackClientResponsePacket.Status.HAVE_ALL_PACKS);
    player.getBedrockClientSession().sendPacketImmediately(respon);
  }
}
