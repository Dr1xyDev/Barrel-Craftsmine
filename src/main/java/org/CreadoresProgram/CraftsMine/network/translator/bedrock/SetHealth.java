package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.SetHealthPacket;
public class SetHealth implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.SetHealthPacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetHealthPacket) pk;
    SetHealthPacket npk = new SetHealthPacket();
    npk.health = packet.getHealth();
    player.sendDataCraftsman(npk);
  }
}
