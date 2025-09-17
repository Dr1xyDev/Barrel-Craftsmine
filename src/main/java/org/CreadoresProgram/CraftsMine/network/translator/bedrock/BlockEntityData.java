package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.BlockEntityDataPacket;
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
public class BlockEntityData implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.BlockEntityDataPacket packet = (org.cloudburstmc.protocol.bedrock.packet.BlockEntityDataPacket) pk;
      byte[] nbt = new byte[0];
        ByteBuf buffer = Unpooled.buffer();
        try(NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))){
          stream.writeTag(packet.getData());
        }catch(IOException e){
          Server.getInstance().getLogger().error("Unable to save NBT data", e);
        }
        nbt = buffer.array();
      BlockEntityDataPacket npk = new BlockEntityDataPacket();
      npk.x = (int) packet.getBlockPosition().getX();
      npk.y = (int) packet.getBlockPosition().getY();
      npk.z = (int) packet.getBlockPosition().getZ();
      npk.namedTag = nbt;
        player.sendDataCraftsman(npk);
    }
}
