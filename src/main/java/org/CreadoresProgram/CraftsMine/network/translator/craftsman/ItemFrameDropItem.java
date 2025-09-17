package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.ItemFrameDropItemPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3i;
public class ItemFrameDropItem implements CraftsmanPacketTranslator{
  @Override
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.ItemFrameDropItemPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.ItemFrameDropItemPacket) pk;
    ItemFrameDropItemPacket npk = new ItemFrameDropItemPacket();
    npk.setBlockPosition(Vector3i.from(packet.x, packet.y, packet.z));
    player.getBedrockClientSession().sendPacket(npk);
  }
}
