package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.MobArmorEquipmentPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class MobArmorEquipment implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.MobArmorEquipmentPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.MobArmorEquipmentPacket) pk;
      MobArmorEquipmentPacket subpacket = new MobArmorEquipmentPacket();
      subpacket.setRuntimeEntityId(packet.eid);
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
