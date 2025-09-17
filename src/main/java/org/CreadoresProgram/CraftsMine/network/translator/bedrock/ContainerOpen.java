package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.protocol.WindowOpenPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.math.vector.Vector3i;
public class ContainerOpen implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.ContainerOpenPacket packet = (org.cloudburstmc.protocol.bedrock.packet.ContainerOpenPacket) pk;
    WindowOpenPacket npk = new WindowOpenPacket();
    npk.windowid = packet.getId();
    Vector3i pos = packet.getBlockPosition();
    npk.x = (int) pos.getX();
    npk.y = (int) pos.getY();
    npk.z = (int) pos.getZ();
    int typ = packet.getType().getId();
    npk.type = (byte) typ;
    player.sendDataCraftsman(npk);
  }
}
