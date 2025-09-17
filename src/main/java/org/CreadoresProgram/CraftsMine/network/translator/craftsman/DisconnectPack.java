package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.DisconnectPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class DisconnectPack implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.DisconnectPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.DisconnectPacket) pk;
      DisconnectPacket subpacket = new DisconnectPacket();
        subpacket.setKickMessage(packet.message);
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
