package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.cloudburstmc.protocol.bedrock.packet.PlayerInputPacket;
import org.cloudburstmc.math.vector.Vector2f;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
public class PlayerInput implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.PlayerInputPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.PlayerInputPacket) pk;
    PlayerInputPacket npk = new PlayerInputPacket();
    npk.setJumping(packet.jumping);
    npk.setSneaking(packet.sneaking);
    npk.setInputMotion(Vector2f.from(packet.motionX, packet.motionY));
    player.getBedrockClientSession().sendPacket(npk);
  }
}
