package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class MobEquipment implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.MobEquipmentPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.MobEquipmentPacket) pk;
      MobEquipmentPacket subpacket = new MobEquipmentPacket();
        subpacket.setRuntimeEntityId(packet.eid);
      subpacket.setContainerId(0);
      subpacket.setInventorySlot(packet.slot);
      subpacket.setHotbarSlot(packet.selectedSlot);
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
