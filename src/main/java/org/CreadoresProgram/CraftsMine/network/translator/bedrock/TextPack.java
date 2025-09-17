package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.TextPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class TextPack implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.TextPacket packet = (org.cloudburstmc.protocol.bedrock.packet.TextPacket) pk;
    switch(packet.getType()){
      case POPUP: 
      case JUKEBOX_POPUP: {
        player.getScreenManager().sendPopup(packet.getMessage().replaceAll("§r", "§f§r"));
        break;
      }
      case TIP: {
        player.sendTip(packet.getMessage().replaceAll("§r", "§f§r"));
        break;
      }
      case SYSTEM: {
        TextPacket pack = new TextPacket();
        pack.type = TextPacket.TYPE_SYSTEM;
        if(packet.getSourceName() != null){
          pack.source = packet.getSourceName().replaceAll("§r", "§f§r");
        }
        pack.message = packet.getMessage().replaceAll("§r", "§f§r");
        pack.parameters = packet.getParameters().stream().toArray(String[]::new);
        player.sendDataCraftsman((MCDPacket) pack);
        break;
      }
      case TRANSLATION: {
        TextPacket pack = new TextPacket();
        pack.type = TextPacket.TYPE_TRANSLATION;
        if(packet.getSourceName() != null){
          pack.source = packet.getSourceName().replaceAll("§r", "§f§r");
        }
        pack.message = packet.getMessage().replaceAll("§r", "§f§r");
        pack.parameters = packet.getParameters().stream().toArray(String[]::new);
        player.sendDataCraftsman((MCDPacket) pack);
        break;
      }
      case CHAT: {
        TextPacket pack = new TextPacket();
        pack.type = TextPacket.TYPE_CHAT;
        if(packet.getSourceName() != null){
          pack.source = packet.getSourceName().replaceAll("§r", "§f§r");
        }
        pack.message = packet.getMessage().replaceAll("§r", "§f§r");
        pack.parameters = packet.getParameters().stream().toArray(String[]::new);
        player.sendDataCraftsman((MCDPacket) pack);
        break;
      }
      default:{
        player.sendMessage(packet.getMessage().replaceAll("§r", "§f§r"));
        break;
      }
    }
  }
}
