package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.UpdateBlockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition;
import org.CreadoresProgram.CraftsMine.utils.BlockMapper;
public class UpdateBlock implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket packet = (org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket) pk;
    Vector3i pos = packet.getBlockPosition();
    int x = (int) pos.getX();
    int y = (int) pos.getY();
    int z = (int) pos.getZ();
    // Only process main layer (layer 0); waterlogged layer is ignored for legacy protocol
    if (packet.getDataLayer() != 0) return;

    BlockDefinition subBlock = packet.getDefinition();
    int runtimeId = subBlock.getRuntimeId();
    int legId, legMeta;
    BlockMapper mapper = player.getBlockMapper();
    if (mapper != null) {
      legId = mapper.getLegacyId(runtimeId);
      legMeta = mapper.getLegacyMeta(runtimeId);
    } else {
      Item blockDef = Item.translateBlock(new Item(runtimeId, packet.getDataLayer(), 1, new byte[0]));
      legId = blockDef.realId;
      legMeta = blockDef.realData;
    }
    UpdateBlockPacket npk = new UpdateBlockPacket();
    UpdateBlockPacket.Entry block = new UpdateBlockPacket.Entry(x, z, y, legId, legMeta, UpdateBlockPacket.FLAG_ALL);
    UpdateBlockPacket.Entry[] entrys = new UpdateBlockPacket.Entry[1];
    entrys[0] = block;
    npk.records = entrys;
    player.sendDataCraftsman(npk);
  }
}
