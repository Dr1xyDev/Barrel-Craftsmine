package org.CreadoresProgram.CraftsMine.network;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.common.PacketSignal;

public class BedrockBatchHandler implements BedrockPacketHandler {
  private final Player player;
  public BedrockBatchHandler(Player player) {
        this.player = player;
  }
  @Override
  public PacketSignal handlePacket(BedrockPacket packet) {
    try{
      if(Server.getInstance().getConfig().isDebug()){
        Server.getInstance().getLogger().debug("|MC Bedrock size| Recive Datapack "+packet.getClass().getSimpleName());
      }
      player.getPacketTranslatorManager().translate(packet);
    }catch(Exception ex){
      Server.getInstance().getLogger().error("Datapacket translate error: ", ex);
    }
    if(packet instanceof org.cloudburstmc.protocol.bedrock.packet.NetworkSettingsPacket || packet instanceof org.cloudburstmc.protocol.bedrock.packet.ToastRequestPacket || packet instanceof org.cloudburstmc.protocol.bedrock.packet.AvailableCommandsPacket){
        return PacketSignal.UNHANDLED;
    }else{
        return PacketSignal.HANDLED;
    }
  }
}
