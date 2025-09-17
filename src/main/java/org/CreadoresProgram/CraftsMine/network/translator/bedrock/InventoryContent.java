package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.WindowSetContentPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import java.util.List;
import org.CreadoresProgram.CraftsMine.server.Server;
import java.io.IOException;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufOutputStream;
import java.util.Arrays;
public class InventoryContent implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket packet = (org.cloudburstmc.protocol.bedrock.packet.InventoryContentPacket) pk;
        WindowSetContentPacket npk = new WindowSetContentPacket();
      List<Item> items = new ObjectArrayList<>();
      npk.windowid = packet.getContainerId();
      for(ItemData ite : packet.getContents()){
          byte[] nbt = new byte[0];
          if(ite.getTag() != null){
              ByteBuf buffer = Unpooled.buffer();
              try (NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))) {
                stream.writeTag(ite.getTag());
            } catch (IOException e) {
                // This shouldn't happen (as this is backed by a Netty ByteBuf), but okay...
                Server.getInstance().getLogger().error("Unable to save NBT data", e);
              }
              nbt = buffer.array();
          }
        Item subi = Item.translateItem(new Item(ite.getNetId(), ite.getDamage(), ite.getCount(), nbt));
        items.add(subi);
      }
        if(items.size() == 0){
            return;
        }
      npk.slots = items.stream().toArray(Item[]::new);
        if(npk.windowid == WindowSetContentPacket.SPECIAL_INVENTORY){
            player.setInventory(npk.slots);
            if(player.getHotbar() != null){
                List<Item> subinv = new ObjectArrayList<>();
                for(Item hotpla : player.getHotbar()){
                    subinv.add(hotpla);
                }
                for(Item itesub : npk.slots){
                    subinv.add(itesub);
                }
                npk.slots = subinv.stream().toArray(Item[]::new);
            List<Integer> hotb = new ObjectArrayList<>();
            for(int it = 0; it < player.getHotbar().length; ++it){
                if(it > npk.slots.length){
                    break;
                }
                hotb.add(it + 9);
            }
            Integer[] subsubhotbar = hotb.stream().toArray(Integer[]::new);
            int[] subhotbar = Arrays.stream(subsubhotbar).mapToInt(Integer::intValue).toArray();
            npk.hotbar =subhotbar;
            }
        }else if(npk.windowid == 0x7a){
            player.setHotbar(npk.slots);
            if(player.getInventory() != null){
                npk.windowid = WindowSetContentPacket.SPECIAL_INVENTORY;
                List<Item> subinv = new ObjectArrayList<>();
                for(Item hotpla : player.getHotbar()){
                    subinv.add(hotpla);
                }
                for(Item itesub : player.getInventory()){
                    subinv.add(itesub);
                }
                npk.slots = subinv.stream().toArray(Item[]::new);
            List<Integer> hotb = new ObjectArrayList<>();
            for(int it = 0; it < player.getHotbar().length; ++it){
                if(it > npk.slots.length){
                    break;
                }
                hotb.add(it + 9);
            }
            Integer[] subsubhotbar = hotb.stream().toArray(Integer[]::new);
            int[] subhotbar = Arrays.stream(subsubhotbar).mapToInt(Integer::intValue).toArray();
            npk.hotbar =subhotbar;
            }
        }
      player.sendDataCraftsman(npk);
    }
    public boolean isArmorInv(List<ItemData> contents){
        if(contents.size() > 5){
            return false;
        }
        return true;
    }
}
