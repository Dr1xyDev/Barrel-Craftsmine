package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import java.util.List;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
public class WindowSetContent implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.WindowSetContentPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.WindowSetContentPacket) pk;
      InventoryContentPacket subpacket = new InventoryContentPacket();
      subpacket.setContainerId(packet.windowid);
      List<ItemData> data = new ObjectArrayList<>();
      for(Item item : packet.slots){
        ItemData.Builder preitem = ItemData.builder();
        preitem.definition(new SimpleItemDefinition("", (int) item.itemIns[0], false));
        preitem.damage((int) item.itemIns[1]);
        preitem.count((int) item.itemIns[2]);
        data.add(preitem.build());
      }
      subpacket.setContents(data);
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
