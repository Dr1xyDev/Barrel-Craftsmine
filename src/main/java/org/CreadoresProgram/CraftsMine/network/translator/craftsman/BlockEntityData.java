package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.BlockEntityDataPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import java.io.IOException;
import org.CreadoresProgram.CraftsMine.server.Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NbtUtils;
import io.netty.buffer.ByteBufInputStream;
public class BlockEntityData implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.BlockEntityDataPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.BlockEntityDataPacket) pk;
      BlockEntityDataPacket subpacket = new BlockEntityDataPacket();
        subpacket.setBlockPosition(Vector3i.from(packet.x, packet.y, packet.z));
      ByteBuf byteBuf = Unpooled.buffer();
      byteBuf.writeBytes(packet.namedTag);
      Object nbt = null;
      try(NBTInputStream reader = NbtUtils.createNetworkReader(new ByteBufInputStream(byteBuf), 1024 * 1024 * 512)){
        nbt = reader.readTag();
      }catch(IOException e){
        Server.getInstance().getLogger().error("No nbt: ", e);
      }
      subpacket.setData((NbtMap) nbt);
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
