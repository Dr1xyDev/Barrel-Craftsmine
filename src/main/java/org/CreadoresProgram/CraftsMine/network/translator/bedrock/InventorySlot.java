package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.WindowSetSlotPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import java.util.List;
import org.CreadoresProgram.CraftsMine.server.Server;
import java.io.IOException;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufOutputStream;
public class InventorySlot implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket packet = (org.cloudburstmc.protocol.bedrock.packet.InventorySlotPacket) pk;
      WindowSetSlotPacket npk = new WindowSetSlotPacket();
      npk.windowid = packet.getContainerId();
      npk.slot = packet.getSlot();
      if(packet.getSlot() < 10){
          npk.hotbarSlot = packet.getSlot();
      }else{
          npk.hotbarSlot = -1;
      }
      ItemData ite = packet.getItem();
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
      npk.item = Item.translateItem(new Item(ite.getNetId(), ite.getDamage(), ite.getCount(), nbt));
      player.sendDataCraftsman(npk);
    }
}
