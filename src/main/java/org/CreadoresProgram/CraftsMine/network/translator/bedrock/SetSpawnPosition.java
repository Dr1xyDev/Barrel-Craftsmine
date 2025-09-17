package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.protocol.SetSpawnPositionPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
public class SetSpawnPosition implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.SetSpawnPositionPacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetSpawnPositionPacket) pk;
    Vector3i pos = packet.getSpawnPosition();
    SetSpawnPositionPacket npk = new SetSpawnPositionPacket();
    npk.x = pos.getX();
    npk.y = pos.getY();
    npk.z = pos.getZ();
    player.sendDataCraftsman(npk);
  }
}
