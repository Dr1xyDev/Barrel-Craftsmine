package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.MoveEntitiesPacket;
import org.CreadoresProgram.CraftsMine.network.translator.ReloadAuthP;
public class MoveEntityAbsolute implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.MoveEntityAbsolutePacket packet = (org.cloudburstmc.protocol.bedrock.packet.MoveEntityAbsolutePacket) pk;
    Vector3f pos = packet.getPosition();
    Vector3f rot = packet.getRotation();
    MoveEntitiesPacket npka = new MoveEntitiesPacket();
    npka.eid = packet.getRuntimeEntityId();
    double x = (double) pos.getX();
    double y = (double) pos.getY();
    double z = (double) pos.getZ();
    npka.x = x;
    npka.y = y;
    npka.z = z;
    double yaw = (double) rot.getY();
    npka.yaw = yaw;
    double pitch = (double) rot.getX();
    npka.pitch = pitch;
    npka.headYaw = 1;
    player.sendDataCraftsman(npka);
    if(packet.getRuntimeEntityId() == player.getRuntimeEntityId()){
      player.setOldPosition(player.getVector3f());
      player.x = x;
      player.y = y;
      player.z = z;
      player.yaw = (float) yaw;
      player.pitch = (float) pitch;
    }
  }
}
