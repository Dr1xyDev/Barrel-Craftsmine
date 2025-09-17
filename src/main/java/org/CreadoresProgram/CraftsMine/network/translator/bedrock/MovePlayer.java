package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.MovePlayerPacket;
import org.CreadoresProgram.CraftsMine.network.translator.ReloadAuthP;
public class MovePlayer implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket packet = (org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket) pk;
    Vector3f pos = packet.getPosition();
    Vector3f rot = packet.getRotation();
    MovePlayerPacket npka = new MovePlayerPacket();
    npka.eid = packet.getRuntimeEntityId();
    float x = (float) pos.getX();
    float y = (float) pos.getY();
    float z = (float) pos.getZ();
    npka.x = x;
    npka.y = y;
    npka.z = z;
    float yaw = (float) rot.getY();
    npka.yaw = yaw;
    float pitch = (float) rot.getX();
    npka.pitch = pitch;
    npka.headYaw = 1;
    switch(packet.getMode()){
      case NORMAL:
        npka.mode = MovePlayerPacket.MODE_NORMAL;
        break;
      case RESPAWN:
      case TELEPORT:
        npka.mode = MovePlayerPacket.MODE_RESET;
        break;
      case HEAD_ROTATION:
        npka.mode = MovePlayerPacket.MODE_ROTATION;
        break;
      default:
        return;
    }
    npka.onGround = packet.isOnGround();
    player.sendDataCraftsman(npka);
    if(packet.getRuntimeEntityId() == player.getRuntimeEntityId()){
      player.setOldPosition(player.getVector3f());
      player.x = x;
      player.y = y;
      player.z = z;
      player.yaw = yaw;
      player.pitch = pitch;
    }
  }
}
