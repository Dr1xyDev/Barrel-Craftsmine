package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.AddItemEntityPacket;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import java.io.IOException;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufOutputStream;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.CreadoresProgram.CraftsMine.inventory.Item;
public class AddItemEntity implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.AddItemEntityPacket packet = (org.cloudburstmc.protocol.bedrock.packet.AddItemEntityPacket) pk;
      byte[] nbt = new byte[0];
      if(packet.getItemInHand().getTag() != null){
        ByteBuf buffer = Unpooled.buffer();
        try(NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))){
          stream.writeTag(packet.getItemInHand().getTag());
        }catch(IOException e){
          Server.getInstance().getLogger().error("Unable to save NBT data", e);
        }
        nbt = buffer.array();
      }
      Item item = Item.translateItem(new Item(packet.getItemInHand().getDefinition().getRuntimeId(), packet.getItemInHand().getDamage(), packet.getItemInHand().getCount(), nbt));
      AddItemEntityPacket npk = new AddItemEntityPacket();
      npk.item = item;
      npk.eid = packet.getRuntimeEntityId();
      player.getUniquesEntitysIds().put(packet.getUniqueEntityId(), packet.getRuntimeEntityId());
      npk.x = (float) packet.getPosition().getX();
      npk.y = (float) packet.getPosition().getY();
      npk.z = (float) packet.getPosition().getZ();
      npk.speedX = (float) packet.getMotion().getX();
      npk.speedY = (float) packet.getMotion().getY();
      npk.speedZ = (float) packet.getMotion().getZ();
        player.sendDataCraftsman(npk);
    }
}
