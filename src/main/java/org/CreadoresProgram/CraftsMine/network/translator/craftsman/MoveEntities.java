package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.network.translator.ReloadAuthP;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;
import org.cloudburstmc.math.vector.Vector3f;
public class MoveEntities implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.MoveEntitiesPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.MoveEntitiesPacket) pk;
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
    movePlayerPacket.setRidingRuntimeEntityId(0);
    movePlayerPacket.setTeleportationCause(MovePlayerPacket.TeleportationCause.UNKNOWN);
    movePlayerPacket.setEntityType(0);
    player.getBedrockClientSession().sendPacket(movePlayerPacket);
  }
}
