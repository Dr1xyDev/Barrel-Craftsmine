package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.protocol.WindowDataPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
public class ContainerSetData implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.ContainerSetDataPacket packet = (org.cloudburstmc.protocol.bedrock.packet.ContainerSetDataPacket) pk;
    WindowDataPacket npk = new WindowDataPacket();
    npk.windowid = packet.getWindowId();
    npk.property = packet.getProperty();
    npk.value = packet.getValue();
    player.sendDataCraftsman(npk);
  }
}
