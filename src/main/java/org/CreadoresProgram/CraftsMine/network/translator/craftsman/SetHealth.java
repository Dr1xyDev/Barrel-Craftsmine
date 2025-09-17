package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.cloudburstmc.protocol.bedrock.packet.SetHealthPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
public class SetHealth implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.SetHealthPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.SetHealthPacket) pk;
    SetHealthPacket npk = new SetHealthPacket();
    npk.setHealth(packet.health);
    player.getBedrockClientSession().sendPacket(npk);
  }
}
