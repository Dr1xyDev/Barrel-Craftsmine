package org.CreadoresProgram.CraftsMine.network.translator;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.PlayerAuthInputPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.*;
public class ReloadAuthP{
  public ReloadAuthP(Player player){
    try{
      if(!(player.getBedrockClientSession().isConnected())){
        return;
      }
        PlayerAuthInputPacket pk = new PlayerAuthInputPacket();
        pk.setPosition(player.getVector3f());
        pk.setRotation(Vector3f.from(player.getPitch(), player.getYaw(), player.getYaw()));
        pk.setMotion(Vector2f.ZERO);
        pk.setInputMode(InputMode.TOUCH);
        pk.setPlayMode(ClientPlayMode.SCREEN);
        pk.setDelta(Vector3f.from(player.getVector3f().getX() - player.getOldPosition().getX(), player.getVector3f().getY() - player.getOldPosition().getY(), player.getVector3f().getZ() - player.getOldPosition().getZ()));
        pk.setVrGazeDirection(null);
        pk.setTick(player.getTikPia());
        if (player.isSneaking()) {
                    pk.getInputData().add(PlayerAuthInputData.SNEAKING);
        }
        if (player.isSprinting()) {
                    pk.getInputData().add(PlayerAuthInputData.SPRINTING);
        }
        if (player.getDiggingStatus() == PlayerActionType.START_BREAK) {
                    pk.getInputData().add(PlayerAuthInputData.PERFORM_BLOCK_ACTIONS);
        }
        player.getBedrockClientSession().sendPacketImmediately(pk);
      }catch(Exception ex){
        Server.getInstance().getLogger().error("Error translating a datapacket: ", ex);
      }
  }
}
