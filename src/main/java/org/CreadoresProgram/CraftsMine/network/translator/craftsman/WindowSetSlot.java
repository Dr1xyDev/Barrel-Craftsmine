package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
public class WindowSetSlot implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.WindowSetSlotPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.WindowSetSlotPacket) pk;
      InventorySlotPacket subpacket = new InventorySlotPacket();
      subpacket.setContainerId(packet.windowid);
      subpacket.setSlot(packet.slot);
      Item item = packet.item;
      ItemData.Builder preitem = ItemData.builder();
      preitem.definition(new SimpleItemDefinition("", (int) item.itemIns[0], false));
      preitem.damage((int) item.itemIns[1]);
      preitem.count((int) item.itemIns[2]);
      subpacket.setItem(preitem.build());
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
