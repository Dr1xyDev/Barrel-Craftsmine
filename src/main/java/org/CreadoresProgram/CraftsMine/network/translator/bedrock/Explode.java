package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.protocol.ExplodePacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Vector3;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import java.util.List;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
public class Explode implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.ExplodePacket packet = (org.cloudburstmc.protocol.bedrock.packet.ExplodePacket) pk;
    ExplodePacket npk = new ExplodePacket();
    npk.x = packet.getPosition().getX();
    npk.y = packet.getPosition().getY();
    npk.z = packet.getPosition().getZ();
    npk.radius = packet.getRadius();
    List<Vector3> recor = new ObjectArrayList<>();
    for(Vector3i reco : packet.getRecords()){
      Vector3 pos = new Vector3();
      pos.x = reco.getX();
      pos.y = reco.getY();
      pos.z = reco.getZ();
      recor.add(pos);
    }
    npk.records = recor.stream().toArray(Vector3[]::new);
    player.sendDataCraftsman(npk);
  }
}
