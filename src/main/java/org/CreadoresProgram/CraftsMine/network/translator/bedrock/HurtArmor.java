package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.protocol.HurtArmorPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
public class HurtArmor implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.HurtArmorPacket packet = (org.cloudburstmc.protocol.bedrock.packet.HurtArmorPacket) pk;
    HurtArmorPacket npk = new HurtArmorPacket();
    byte healt = (byte) packet.getDamage();
    npk.health = healt;
    player.sendDataCraftsman(npk);
  }
}
