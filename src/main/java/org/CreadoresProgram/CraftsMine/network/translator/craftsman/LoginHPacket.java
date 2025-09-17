package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.cloudburstmc.protocol.bedrock.packet.RequestChunkRadiusPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.DisconnectPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.LoginStatusPacket;
public class LoginHPacket implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.LoginPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.LoginPacket) pk;
    if(packet.protocol > 90){
      LoginStatusPacket sta = new LoginStatusPacket();
      sta.status = LoginStatusPacket.LOGIN_SUCCESS;
      player.sendDataCraftsman(sta);
      DisconnectPacket disc = new DisconnectPacket();
      disc.message = "§cPlease enter\nIP: "+Server.getInstance().getConfig().getBedrockAddress()+" Port: "+Server.getInstance().getConfig().getBedrockPort();
      player.sendDataCraftsman(disc);
      return;
    }
    if(packet.protocol < 80){
      LoginStatusPacket discsub = new LoginStatusPacket();
      discsub.status = LoginStatusPacket.LOGIN_FAILED_CLIENT;
      player.sendDataCraftsman(discsub);
      return;
    }
    if(!(Server.getInstance().getConfig().isSynchServer())){
      if(Server.getInstance().getBedrockPlayers().size() > Server.getInstance().getConfig().getMaxplayers()){
        LoginStatusPacket stas = new LoginStatusPacket();
        stas.status = LoginStatusPacket.LOGIN_SUCCESS;
        player.sendDataCraftsman(stas);
        DisconnectPacket dicm = new DisconnectPacket();
        dicm.message = "disconnectionScreen.serverFull";
        player.sendDataCraftsman(dicm);
        return;
      }
    }
    try{
      player.onLogin(packet);
      player.getBedrockClientSession().getPeer().getCodecHelper().setBlockDefinitions(Server.getInstance().getBlockDefinitions());
      player.getBedrockClientSession().sendPacketImmediately(player.getLoginPacket());
    }catch(Exception e){
      org.CreadoresProgram.CraftsMine.network.protocol.LoginStatusPacket spk = new org.CreadoresProgram.CraftsMine.network.protocol.LoginStatusPacket();
      spk.status = org.CreadoresProgram.CraftsMine.network.protocol.LoginStatusPacket.LOGIN_SUCCESS;
      player.sendDataCraftsman(spk);
      player.disconnect("Failed to connect: "+ e+" or Server Offline");
      return;
    }
    Server.getInstance().getLogger().info(player.getCraftsmanUsername()+" Logged in");
  }
}
