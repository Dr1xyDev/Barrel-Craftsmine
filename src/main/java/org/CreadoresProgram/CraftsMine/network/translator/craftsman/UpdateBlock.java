package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.UpdateBlockPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleBlockDefinition;
public class UpdateBlock implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.UpdateBlockPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.UpdateBlockPacket) pk;
      for(org.CreadoresProgram.CraftsMine.network.protocol.UpdateBlockPacket.Entry record : packet.records){
        UpdateBlockPacket subpacket = new UpdateBlockPacket();
        subpacket.setDataLayer(record.blockData);
        subpacket.setBlockPosition(Vector3i.from(record.x, record.y, record.z));
        player.getBedrockClientSession().sendPacket(subpacket);
      }
    }
}
