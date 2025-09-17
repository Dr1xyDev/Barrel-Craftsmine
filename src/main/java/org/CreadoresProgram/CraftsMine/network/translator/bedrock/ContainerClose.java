package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.protocol.WindowClosePacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
public class ContainerClose implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket packet = (org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket) pk;
    WindowClosePacket npk = new WindowClosePacket();
    npk.windowid = (int) packet.getId();
    player.sendDataCraftsman(npk);
  }
}
