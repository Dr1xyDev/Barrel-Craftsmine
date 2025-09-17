package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.AdventureSettingsPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.data.AdventureSetting;
public class AdventureSettings implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.AdventureSettingsPacket packet = (org.cloudburstmc.protocol.bedrock.packet.AdventureSettingsPacket) pk;
    AdventureSettingsPacket npk = new AdventureSettingsPacket();
    npk.userPermission = 0x2;
    npk.globalPermission = 0x2;
    int flags = 0;
    for(AdventureSetting flag : packet.getSettings()){
      switch(flag){
        case WORLD_IMMUTABLE:
          flags |= 0x01;
          break;
        case NO_PVM:
          flags |= 0x04;
          break;
        case SHOW_NAME_TAGS:
          flags |= 0x20;
          break;
        case AUTO_JUMP:
          flags |= 0x40;
          break;
        case MAY_FLY:
          flags |= 0x80;
          break;
        case NO_CLIP:
          flags |= 0x100;
          break;
        default:
          continue;
      }
    }
    npk.flags = flags;
    player.sendDataCraftsman(npk);
  }
}
