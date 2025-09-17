package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.SetDifficultyPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class SetDifficulty implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.SetDifficultyPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.SetDifficultyPacket) pk;
      SetDifficultyPacket subpacket = new SetDifficultyPacket();
      subpacket.setDifficulty(packet.difficulty);
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
