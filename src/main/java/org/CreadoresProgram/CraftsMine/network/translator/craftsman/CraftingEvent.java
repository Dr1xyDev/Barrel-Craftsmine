package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.CraftingEventPacket;
import org.cloudburstmc.protocol.bedrock.data.inventory.CraftingType;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import java.io.IOException;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufOutputStream;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import org.cloudburstmc.protocol.bedrock.data.inventory.ItemData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
public class CraftingEvent implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.CraftingEventPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.CraftingEventPacket) pk;
    CraftingEventPacket npk = new CraftingEventPacket();
    npk.setContainerId((byte) packet.windowId);
    npk.setType(CraftingType.values()[packet.type]);
    npk.setUuid(packet.id);
    player.getBedrockClientSession().sendPacket(npk);
  }
}
