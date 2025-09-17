package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.CommandRequestPacket;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginType;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginData;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.server.Server;
import java.util.UUID;
public class ChatPacket implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.TextPacket chatPacket = (org.CreadoresProgram.CraftsMine.network.protocol.TextPacket) pk;
    if(!chatPacket.message.startsWith("/")){
      TextPacket textPacket = new TextPacket();
      textPacket.setType(TextPacket.Type.CHAT);
      textPacket.setNeedsTranslation(false);
      textPacket.setSourceName(chatPacket.source);
      textPacket.setMessage(chatPacket.message);
      textPacket.setXuid("");
      textPacket.setPlatformChatId("");
      player.getBedrockClientSession().sendPacket(textPacket);
    }else{
      CommandRequestPacket Crp = new CommandRequestPacket();
      Crp.setVersion(Server.getInstance().getBedrockPacketCodec().getProtocolVersion());
      Crp.setCommand("?"+chatPacket.message.substring(1));
      CommandOriginData Cod = new CommandOriginData(CommandOriginType.PLAYER, UUID.fromString(player.getUUID()), player.getUUID(), 0);
      Crp.setCommandOriginData(Cod);
      player.getBedrockClientSession().sendPacket(Crp);
    }
  }
}
