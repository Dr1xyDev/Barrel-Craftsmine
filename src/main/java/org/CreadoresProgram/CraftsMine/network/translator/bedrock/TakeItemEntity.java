package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.PickUpItemPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class TakeItemEntity implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.TakeItemEntityPacket packet = (org.cloudburstmc.protocol.bedrock.packet.TakeItemEntityPacket) pk;
      PickUpItemPacket npk = new PickUpItemPacket();
      npk.entityId = packet.getRuntimeEntityId();
        npk.target = packet.getItemRuntimeEntityId();
        player.sendDataCraftsman(npk);
    }
}
