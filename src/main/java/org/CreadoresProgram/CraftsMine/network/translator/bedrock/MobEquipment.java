package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.MobEquipmentPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import java.io.IOException;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufOutputStream;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class MobEquipment implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket packet = (org.cloudburstmc.protocol.bedrock.packet.MobEquipmentPacket) pk;
      MobEquipmentPacket npk = new MobEquipmentPacket();
      npk.eid = packet.getRuntimeEntityId();
      byte[] nbt = new byte[0];
      if(packet.getItem().getTag() != null){
        ByteBuf buffer = Unpooled.buffer();
        try(NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))){
          stream.writeTag(packet.getItem().getTag());
        }catch(IOException e){
          Server.getInstance().getLogger().error("Unable to save NBT data", e);
        }
        nbt = buffer.array();
      }
      npk.item = Item.translateItem(new Item(packet.getItem().getNetId(), packet.getItem().getDamage(), packet.getItem().getCount(), nbt));
      npk.slot = packet.getInventorySlot();
      npk.selectedSlot = packet.getHotbarSlot();
        player.sendDataCraftsman(npk);
    }
}
