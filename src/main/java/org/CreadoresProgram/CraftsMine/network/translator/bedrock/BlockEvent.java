package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.BlockEventPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
public class BlockEvent implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.BlockEventPacket packet = (org.cloudburstmc.protocol.bedrock.packet.BlockEventPacket) pk;
    BlockEventPacket npk = new BlockEventPacket();
    npk.x = (int) packet.getBlockPosition().getX();
    npk.y = (int) packet.getBlockPosition().getY();
    npk.z = (int) packet.getBlockPosition().getZ();
    npk.case1 = packet.getEventType();
    npk.case2 = packet.getEventData();
    player.sendDataCraftsman(npk);
  }
}
