package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.LoginStatusPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.TickSyncPacket;
import org.cloudburstmc.protocol.bedrock.packet.SetLocalPlayerAsInitializedPacket;

public class PlayStatus implements BedrockPacketTranslator{
  @Override
  public boolean immediate(){
    return true;
  }
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket packet = (org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket) pk;
    LoginStatusPacket loginPS = new LoginStatusPacket();
    if (packet.getStatus() == org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket.Status.PLAYER_SPAWN) {
      loginPS.status = LoginStatusPacket.PLAYER_SPAWN;
      TickSyncPacket tickSyncPacket = new TickSyncPacket();
      tickSyncPacket.setRequestTimestamp(0);
      tickSyncPacket.setResponseTimestamp(0);
      player.getBedrockClientSession().sendPacketImmediately(tickSyncPacket);
      SetLocalPlayerAsInitializedPacket setLocalPlayerAsInitializedPacket = new SetLocalPlayerAsInitializedPacket();
      setLocalPlayerAsInitializedPacket.setRuntimeEntityId(player.getRuntimeEntityId());
      player.getBedrockClientSession().sendPacket(setLocalPlayerAsInitializedPacket);
      //TP player pending...
    }else if(packet.getStatus() == org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket.Status.LOGIN_SUCCESS){
      loginPS.status = LoginStatusPacket.LOGIN_SUCCESS;
    }else if(packet.getStatus() == org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket.Status.LOGIN_FAILED_CLIENT_OLD){
      loginPS.status = LoginStatusPacket.LOGIN_FAILED_CLIENT;
    }else if(packet.getStatus() == org.cloudburstmc.protocol.bedrock.packet.PlayStatusPacket.Status.LOGIN_FAILED_SERVER_OLD){
      loginPS.status = LoginStatusPacket.LOGIN_FAILED_SERVER;
    }else{
      loginPS.status = LoginStatusPacket.LOGIN_FAILED_CLIENT;
    }
    player.sendDataCraftsman((MCDPacket) loginPS);
  }
}
