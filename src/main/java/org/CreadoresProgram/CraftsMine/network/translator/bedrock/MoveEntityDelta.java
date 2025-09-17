package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.MoveEntitiesPacket;
import org.CreadoresProgram.CraftsMine.network.translator.ReloadAuthP;
public class MoveEntityDelta implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket packet = (org.cloudburstmc.protocol.bedrock.packet.MoveEntityDeltaPacket) pk;
    MoveEntitiesPacket npka = new MoveEntitiesPacket();
    npka.eid = packet.getRuntimeEntityId();
    double x = (double) packet.getX();
    double y = (double) packet.getY();
    double z = (double) packet.getZ();
    npka.x = x;
    npka.y = y;
    npka.z = z;
    double yaw = (double) packet.getYaw();
    npka.yaw = yaw;
    double pitch = (double) packet.getPitch();
    npka.pitch = pitch;
    npka.headYaw = (double) packet.getHeadYaw();
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
