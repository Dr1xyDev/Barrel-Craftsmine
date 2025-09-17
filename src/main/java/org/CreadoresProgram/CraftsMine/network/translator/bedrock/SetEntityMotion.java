package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.protocol.SetEntityMotionPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class SetEntityMotion implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket) pk;
    SetEntityMotionPacket npk = new SetEntityMotionPacket();
    npk.entities = new SetEntityMotionPacket.Entry[]{
      new SetEntityMotionPacket.Entry(packet.getRuntimeEntityId(), ((double) packet.getMotion().getX()), ((double) packet.getMotion().getY()), ((double) packet.getMotion().getZ()))
    };
    player.sendDataCraftsman(npk);
  }
}
