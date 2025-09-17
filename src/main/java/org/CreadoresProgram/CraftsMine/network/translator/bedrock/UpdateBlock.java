package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.UpdateBlockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
public class UpdateBlock implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket packet = (org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket) pk;
    UpdateBlockPacket npk = new UpdateBlockPacket();
    Vector3i pos = packet.getBlockPosition();
    int x = (int) pos.getX();
    int y = (int) pos.getY();
    int z = (int) pos.getZ();
    BlockDefinition subBlock = packet.getDefinition();
    Item blockDef = Item.translateBlock(new Item(subBlock.getRuntimeId(), packet.getDataLayer(), 1, new byte[0]));
    UpdateBlockPacket.Entry block = new UpdateBlockPacket.Entry(x, z, y, blockDef.realId, blockDef.realData, UpdateBlockPacket.FLAG_ALL);
    UpdateBlockPacket.Entry[] entrys = new UpdateBlockPacket.Entry[1];
    entrys[0] = block;
    npk.records = entrys;
    player.sendDataCraftsman(npk);
  }
}
