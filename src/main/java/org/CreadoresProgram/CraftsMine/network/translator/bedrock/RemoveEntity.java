package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.RemoveEntityPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class RemoveEntity implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.RemoveEntityPacket packet = (org.cloudburstmc.protocol.bedrock.packet.RemoveEntityPacket) pk;
        if(player.getUniquesEntitysIds().get(packet.getUniqueEntityId()) == null){
            return;
        }
      RemoveEntityPacket npk = new RemoveEntityPacket();
      npk.eid = player.getUniquesEntitysIds().get(packet.getUniqueEntityId());
        player.getUniquesEntitysIds().remove(packet.getUniqueEntityId());
        player.sendDataCraftsman(npk);
    }
}
