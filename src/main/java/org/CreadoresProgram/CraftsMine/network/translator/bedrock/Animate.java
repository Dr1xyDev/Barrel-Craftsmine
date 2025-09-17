package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.AnimatePacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
public class Animate implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.AnimatePacket packet = (org.cloudburstmc.protocol.bedrock.packet.AnimatePacket) pk;
    AnimatePacket npk = new AnimatePacket();
    switch(packet.getAction()){
      case NO_ACTION:
        npk.action = 0;
        break;
      case SWING_ARM:
        npk.action = 1;
        break;
      case WAKE_UP:
        npk.action = 3;
        break;
      case CRITICAL_HIT:
        npk.action = 4;
        break;
      case MAGIC_CRITICAL_HIT:
        npk.action = 5;
        break;
      case ROW_RIGHT:
        npk.action = 128;
        break;
      case ROW_LEFT:
        npk.action = 129;
        break;
      default:
        return;
    }
    npk.eid = packet.getRuntimeEntityId();
    player.sendDataCraftsman(npk);
  }
}
