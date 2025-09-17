package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.InteractPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class Interact implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.InteractPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.InteractPacket) pk;
    InteractPacket npk = new InteractPacket();
    npk.setRuntimeEntityId(packet.target);
    switch(packet.action){
      case org.CreadoresProgram.CraftsMine.network.protocol.InteractPacket.ACTION_RIGHT_CLICK:
        npk.setAction(InteractPacket.Action.INTERACT);
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.InteractPacket.ACTION_LEFT_CLICK:
        npk.setAction(InteractPacket.Action.DAMAGE);
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.InteractPacket.ACTION_VEHICLE_EXIT:
        npk.setAction(InteractPacket.Action.LEAVE_VEHICLE);
        break;
    }
    player.getBedrockClientSession().sendPacket(npk);
  }
}
