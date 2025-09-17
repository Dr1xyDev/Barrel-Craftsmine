package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class BossEvent implements BedrockPacketTranslator {

  private String bossHealt = "-";
    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.BossEventPacket packet = (org.cloudburstmc.protocol.bedrock.packet.BossEventPacket) pk;
        if(packet.getAction() == org.cloudburstmc.protocol.bedrock.packet.BossEventPacket.Action.REMOVE){
          if(player.getBossBars().get(packet.getBossUniqueEntityId()) != null){
            player.getBossBars().remove(packet.getBossUniqueEntityId());
          }
        }else{
          String preTitle = "";
          float preHeald = 0;
          if(player.getBossBars().get(packet.getBossUniqueEntityId()) != null){
            preTitle = (String) player.getBossBars().get(packet.getBossUniqueEntityId())[1];
            preHeald = (float) player.getBossBars().get(packet.getBossUniqueEntityId())[0];
          }
          if(packet.getTitle() != null && packet.getTitle() != "null"){
            preTitle = packet.getTitle().replaceAll("§r", "§f§r");
          }
          if(packet.getHealthPercentage() > 0){
            preHeald = packet.getHealthPercentage();
          }
          player.getBossBars().put(packet.getBossUniqueEntityId(), new Object[]{ preHeald, preTitle });
        }
          String bossbars = "";
          int bossCount = 0;
          for(Object[] idb : player.getBossBars().values()){
            if(bossCount > 2){
              break;
            }
            bossCount++;
            int healt = (int) ((float) idb[0]);
            String title = (String) idb[1];
            int porsent = (int) Math.floor((46 * healt) / 100);
            String bossHC = "§l§0|§5"+repeat(bossHealt, porsent)+"§0"+repeat(bossHealt, ((int) Math.floor(46 - porsent)))+"§0|";
            bossbars += bossHC + "\n"+title+"\n";
          }
          if(bossCount < 1){
            player.getScreenManager().sendBossBar("");
          }else{
            player.getScreenManager().sendBossBar(bossbars);
          }
    }
  private static String repeat(String str, int num){
    return new String(new char[num]).replace("\0", str);
  }
}
