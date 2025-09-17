package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.data.ScoreInfo;
import java.util.Map;
import java.util.TreeMap;
public class SetScore implements BedrockPacketTranslator{
  private static String space = "                                                             ";
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.SetScorePacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetScorePacket) pk;
    if(packet.getAction() == org.cloudburstmc.protocol.bedrock.packet.SetScorePacket.Action.SET){
      for(ScoreInfo scoreInfo : packet.getInfos()){
        player.getScoreBoard().put(scoreInfo.getScore(), space+scoreInfo.getName().replaceAll("§r", "§f§r"));
      }
    }else{
      for(ScoreInfo scoreInfo : packet.getInfos()){
        if(player.getScoreBoard().get(scoreInfo.getScore()) != null){
          player.getScoreBoard().remove(scoreInfo.getScore());
        }
      }
    }
    TreeMap<Integer, String> scoreB = (TreeMap) player.getScoreBoard();
    StringBuilder sb = new StringBuilder();
    for(Map.Entry<Integer, String> entry : scoreB.entrySet()){
      sb.append(entry.getValue()).append("\n");
    }
    player.getScreenManager().sendScoreBoard(sb.toString());
  }
}
