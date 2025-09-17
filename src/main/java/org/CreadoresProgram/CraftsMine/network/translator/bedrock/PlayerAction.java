package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
public class PlayerAction implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket packet = (org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket) pk;
    PlayerActionPacket npk = new PlayerActionPacket();
    npk.face = packet.getFace();
    npk.entityId = packet.getRuntimeEntityId();
    npk.x = (int) packet.getBlockPosition().getX();
    npk.y = (int) packet.getBlockPosition().getY();
    npk.z = (int) packet.getBlockPosition().getZ();
    switch(packet.getAction()){
      case START_BREAK:
        npk.action = PlayerActionPacket.ACTION_START_BREAK;
        break;
      case ABORT_BREAK:
        npk.action = PlayerActionPacket.ACTION_CANCEL_BREAK;
        break;
      case STOP_BREAK:
        npk.action = PlayerActionPacket.ACTION_FINISH_BREAK;
        break;
      case DROP_ITEM:
        npk.action = PlayerActionPacket.ACTION_RELEASE_ITEM;
        break;
      case STOP_SLEEP:
        npk.action = PlayerActionPacket.ACTION_STOP_SLEEPING;
        break;
      case RESPAWN:
        npk.action = PlayerActionPacket.ACTION_RESPAWN;
        break;
      case JUMP:
        npk.action = PlayerActionPacket.ACTION_JUMP;
        break;
      case START_SPRINT:
        npk.action = PlayerActionPacket.ACTION_START_SPRINT;
        break;
      case STOP_SPRINT:
        npk.action = PlayerActionPacket.ACTION_STOP_SPRINT;
        break;
      case START_SNEAK:
        npk.action = PlayerActionPacket.ACTION_START_SNEAK;
        break;
      case STOP_SNEAK:
        npk.action = PlayerActionPacket.ACTION_STOP_SNEAK;
        break;
      case DIMENSION_CHANGE_REQUEST_OR_CREATIVE_DESTROY_BLOCK:
        npk.action = PlayerActionPacket.ACTION_DIMENSION_CHANGE;
        break;
      case DIMENSION_CHANGE_SUCCESS:
        npk.action = PlayerActionPacket.ACTION_NETHER_UNKNOWN;
        break;
      default:
        return;
    }
    player.sendDataCraftsman(npk);
  }
}
