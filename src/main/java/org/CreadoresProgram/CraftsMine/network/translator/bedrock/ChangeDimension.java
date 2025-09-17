package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.ChangeDimensionPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class ChangeDimension implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.ChangeDimensionPacket packet = (org.cloudburstmc.protocol.bedrock.packet.ChangeDimensionPacket) pk;
        ChangeDimensionPacket npk = new ChangeDimensionPacket();
        if(packet.getDimension() == 2){
          npk.dimension = 1 & 0xFF;
        }else{
          byte dimention = (byte) packet.getDimension();
          byte subdimension = (byte) (dimention & 0xFF);
          npk.dimension = subdimension;
        }
      npk.x = packet.getPosition().getX();
      npk.y = packet.getPosition().getY();
      npk.z = packet.getPosition().getZ();
      player.sendDataCraftsman(npk);
    }
}
