package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.*;
import org.cloudburstmc.protocol.bedrock.packet.PlayerActionPacket;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.ItemUseTransaction;
public class PlayerAction implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket) pk;
    Vector3i blockPos = Vector3i.from(packet.x, packet.y, packet.z);
    PlayerActionPacket actionpack = new PlayerActionPacket();
    actionpack.setRuntimeEntityId(packet.entityId);
    actionpack.setBlockPosition(blockPos);
    actionpack.setResultPosition(blockPos);
    actionpack.setFace(packet.face);
    PlayerActionType action = null;
    switch((byte)packet.action){
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_START_BREAK:
        action = PlayerActionType.START_BREAK;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_CANCEL_BREAK:
        action = PlayerActionType.ABORT_BREAK;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_FINISH_BREAK:
        action = PlayerActionType.STOP_BREAK;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_RELEASE_ITEM:
        action = PlayerActionType.DROP_ITEM;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_STOP_SLEEPING:
        action = PlayerActionType.STOP_SLEEP;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_RESPAWN:
        action = PlayerActionType.RESPAWN;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_JUMP:
        action = PlayerActionType.JUMP;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_START_SPRINT:
        action = PlayerActionType.START_SPRINT;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_STOP_SPRINT:
        action = PlayerActionType.STOP_SPRINT;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_START_SNEAK:
        action = PlayerActionType.START_SNEAK;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_STOP_SNEAK:
        action = PlayerActionType.STOP_SNEAK;
        break;
      case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_DIMENSION_CHANGE:
        action = PlayerActionType.DIMENSION_CHANGE_REQUEST_OR_CREATIVE_DESTROY_BLOCK;
        break;
        case org.CreadoresProgram.CraftsMine.network.protocol.PlayerActionPacket.ACTION_NETHER_UNKNOWN:
        action = PlayerActionType.DIMENSION_CHANGE_SUCCESS;
        break;
    }
    actionpack.setAction(action);
    player.getBedrockClientSession().sendPacket(actionpack);
  }
}
