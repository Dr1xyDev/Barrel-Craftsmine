package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.RespawnPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3f;
public class Respawn implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.RespawnPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.RespawnPacket) pk;
      RespawnPacket subpacket = new RespawnPacket();
      subpacket.setRuntimeEntityId(player.getRuntimeEntityId());
      subpacket.setState(RespawnPacket.State.CLIENT_READY);
      subpacket.setPosition(Vector3f.from(packet.x, packet.y, packet.z));
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
