package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3f;
import org.CreadoresProgram.CraftsMine.network.protocol.RespawnPacket;
public class Respawn implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player){
      org.cloudburstmc.protocol.bedrock.packet.RespawnPacket packet = (org.cloudburstmc.protocol.bedrock.packet.RespawnPacket) pk;
      RespawnPacket subpacket = new RespawnPacket();
      subpacket.x = packet.getPosition().getX();
      subpacket.y = packet.getPosition().getY();
      subpacket.z = packet.getPosition().getZ();
      player.sendDataCraftsman(subpacket);
    }
}
