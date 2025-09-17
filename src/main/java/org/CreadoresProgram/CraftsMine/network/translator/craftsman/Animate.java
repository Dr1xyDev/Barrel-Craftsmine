package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.cloudburstmc.protocol.bedrock.packet.AnimatePacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
public class Animate implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.AnimatePacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.AnimatePacket) pk;
    AnimatePacket npk = new AnimatePacket();
    npk.setRuntimeEntityId(packet.eid);
    switch(packet.action){
      case 0:
        npk.setAction(AnimatePacket.Action.NO_ACTION);
        break;
      case 1:
        npk.setAction(AnimatePacket.Action.SWING_ARM);
        break;
      case 3:
        npk.setAction(AnimatePacket.Action.WAKE_UP);
        break;
      case 4:
        npk.setAction(AnimatePacket.Action.CRITICAL_HIT);
        break;
      case 5:
        npk.setAction(AnimatePacket.Action.MAGIC_CRITICAL_HIT);
        break;
      case 128:
        npk.setAction(AnimatePacket.Action.ROW_RIGHT);
        break;
      case 129:
        npk.setAction(AnimatePacket.Action.ROW_LEFT);
        break;
    }
    player.getBedrockClientSession().sendPacket(npk);
  }
}
