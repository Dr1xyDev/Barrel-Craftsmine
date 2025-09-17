package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.network.translator.ReloadAuthP;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;
import org.cloudburstmc.math.vector.Vector3f;
public class MovePlayerC implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.MovePlayerPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.MovePlayerPacket) pk;
    MovePlayerPacket movePlayerPacket = new MovePlayerPacket();
    movePlayerPacket.setRuntimeEntityId(packet.eid);
    player.setOldPosition(player.getVector3f());
    player.x = (double) packet.x;
    player.y = (double) packet.y;
    player.z = (double) packet.z;
    player.yaw = (float) packet.yaw;
    player.pitch = (float) packet.pitch;
    movePlayerPacket.setPosition(Vector3f.from(packet.x, packet.y, packet.z));
    movePlayerPacket.setRotation(Vector3f.from(packet.pitch, packet.yaw, packet.yaw));
    if(packet.mode == org.CreadoresProgram.CraftsMine.network.protocol.MovePlayerPacket.MODE_NORMAL){
      movePlayerPacket.setMode(MovePlayerPacket.Mode.NORMAL);
    }else if(packet.mode == org.CreadoresProgram.CraftsMine.network.protocol.MovePlayerPacket.MODE_ROTATION){
      movePlayerPacket.setMode(MovePlayerPacket.Mode.HEAD_ROTATION);
    }else{
      movePlayerPacket.setMode(MovePlayerPacket.Mode.RESPAWN);
    }
    movePlayerPacket.setOnGround(packet.onGround);
    movePlayerPacket.setRidingRuntimeEntityId(0);
    movePlayerPacket.setTeleportationCause(MovePlayerPacket.TeleportationCause.UNKNOWN);
    movePlayerPacket.setEntityType(0);
    player.getBedrockClientSession().sendPacket(movePlayerPacket);
  }
}
