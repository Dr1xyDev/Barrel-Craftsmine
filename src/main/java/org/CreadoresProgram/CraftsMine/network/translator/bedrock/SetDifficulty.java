package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.SetDifficultyPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class SetDifficulty implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player){
      org.cloudburstmc.protocol.bedrock.packet.SetDifficultyPacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetDifficultyPacket) pk;
      SetDifficultyPacket subpacket = new SetDifficultyPacket();
      subpacket.difficulty = packet.getDifficulty();
      player.sendDataCraftsman(subpacket);
    }
}
