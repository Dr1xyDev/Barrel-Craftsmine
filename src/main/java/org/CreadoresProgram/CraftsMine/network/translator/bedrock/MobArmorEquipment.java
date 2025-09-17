package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.MobArmorEquipmentPacket;
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
public class MobArmorEquipment implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.MobArmorEquipmentPacket packet = (org.cloudburstmc.protocol.bedrock.packet.MobArmorEquipmentPacket) pk;
      MobArmorEquipmentPacket npk = new MobArmorEquipmentPacket();
      npk.eid = packet.getRuntimeEntityId();
      byte[] nbt0 = new byte[0];
      if(packet.getHelmet().getTag() != null){
        ByteBuf buffer = Unpooled.buffer();
        try(NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))){
          stream.writeTag(packet.getHelmet().getTag());
        }catch(IOException e){
          Server.getInstance().getLogger().error("Unable to save NBT data", e);
        }
        nbt0 = buffer.array();
      }
      npk.slots[0] = Item.translateItem(new Item(packet.getHelmet().getNetId(), packet.getHelmet().getDamage(), packet.getHelmet().getCount(), nbt0));
      byte[] nbt1 = new byte[0];
      if(packet.getChestplate().getTag() != null){
        ByteBuf buffer = Unpooled.buffer();
        try(NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))){
          stream.writeTag(packet.getChestplate().getTag());
        }catch(IOException e){
          Server.getInstance().getLogger().error("Unable to save NBT data", e);
        }
        nbt1 = buffer.array();
      }
      npk.slots[1] = Item.translateItem(new Item(packet.getChestplate().getNetId(), packet.getChestplate().getDamage(), packet.getChestplate().getCount(), nbt1));
      byte[] nbt2 = new byte[0];
      if(packet.getLeggings().getTag() != null){
        ByteBuf buffer = Unpooled.buffer();
        try(NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))){
          stream.writeTag(packet.getLeggings().getTag());
        }catch(IOException e){
          Server.getInstance().getLogger().error("Unable to save NBT data", e);
        }
        nbt2 = buffer.array();
      }
      npk.slots[2] = Item.translateItem(new Item(packet.getLeggings().getNetId(), packet.getLeggings().getDamage(), packet.getLeggings().getCount(), nbt2));
      byte[] nbt3 = new byte[0];
      if(packet.getBoots().getTag() != null){
        ByteBuf buffer = Unpooled.buffer();
        try(NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))){
          stream.writeTag(packet.getBoots().getTag());
        }catch(IOException e){
          Server.getInstance().getLogger().error("Unable to save NBT data", e);
        }
        nbt3 = buffer.array();
      }
      npk.slots[3] = Item.translateItem(new Item(packet.getBoots().getNetId(), packet.getBoots().getDamage(), packet.getBoots().getCount(), nbt3));
        player.sendDataCraftsman(npk);
    }
}
