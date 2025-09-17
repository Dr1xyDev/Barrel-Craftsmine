package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.data.GameType;
import org.CreadoresProgram.CraftsMine.network.protocol.SetPlayerGameTypePacket;
public class SetPlayerGameType implements BedrockPacketTranslator {
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.SetPlayerGameTypePacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetPlayerGameTypePacket) pk;
    player.setGamemode(packet.getGamemode());
    SetPlayerGameTypePacket npk = new SetPlayerGameTypePacket();
    npk.gamemode = packet.getGamemode();
    player.sendDataCraftsman(npk);
  }
}
