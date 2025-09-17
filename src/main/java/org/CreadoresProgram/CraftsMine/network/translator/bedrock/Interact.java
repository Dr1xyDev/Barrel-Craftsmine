package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.InteractPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
public class Interact implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.InteractPacket packet = (org.cloudburstmc.protocol.bedrock.packet.InteractPacket) pk;
    InteractPacket npk = new InteractPacket();
    npk.target = npk.eid = packet.getRuntimeEntityId();
    switch(packet.getAction()){
      case NONE:
        npk.action = 0;
        break;
      case INTERACT:
        npk.action = InteractPacket.ACTION_RIGHT_CLICK;
        break;
      case DAMAGE:
        npk.action = InteractPacket.ACTION_LEFT_CLICK;
        break;
      case LEAVE_VEHICLE:
        npk.action = InteractPacket.ACTION_VEHICLE_EXIT;
        break;
      default:
        return;
    }
    player.sendDataCraftsman(npk);
  }
}
