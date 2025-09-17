package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.AddPaintingPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class AddPainting implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.AddPaintingPacket packet = (org.cloudburstmc.protocol.bedrock.packet.AddPaintingPacket) pk;
      AddPaintingPacket npk = new AddPaintingPacket();
      npk.eid = packet.getRuntimeEntityId();
      npk.direction = packet.getDirection();
      npk.title = packet.getMotive();
      npk.x = (int) packet.getPosition().getX();
      npk.y = (int) packet.getPosition().getY();
      npk.z = (int) packet.getPosition().getZ();
        player.sendDataCraftsman(npk);
    }
}
