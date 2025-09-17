package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.ContainerClosePacket;
public class WindowClose implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.WindowClosePacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.WindowClosePacket) pk;
    ContainerClosePacket npk = new ContainerClosePacket();
    byte idw = (byte) packet.windowid;
    npk.setId(idw);
    player.getBedrockClientSession().sendPacket(npk);
  }
}
