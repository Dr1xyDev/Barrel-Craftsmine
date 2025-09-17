package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
public class SetTitle implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket) pk;
    if(packet.getType() == org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket.Type.CLEAR || packet.getType() == org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket.Type.RESET || packet.getType() == org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket.Type.TIMES){ 
      return; 
    }
    if(packet.getType() == org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket.Type.TITLE || packet.getType() == org.cloudburstmc.protocol.bedrock.packet.SetTitlePacket.Type.TITLE_JSON){
      player.getScreenManager().sendTitle("§l"+packet.getText().replaceAll("§r", "§f§r"));
    }else{
      player.getScreenManager().sendSubtitle(packet.getText().replaceAll("§r", "§f§r"));
    }
  }
}
