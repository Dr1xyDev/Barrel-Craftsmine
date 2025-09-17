package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.SetTimePacket;
public class SetTime implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.SetTimePacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetTimePacket) pk;
    SetTimePacket npk = new SetTimePacket();
    npk.time = packet.getTime();
    player.sendDataCraftsman(npk);
  }
}
