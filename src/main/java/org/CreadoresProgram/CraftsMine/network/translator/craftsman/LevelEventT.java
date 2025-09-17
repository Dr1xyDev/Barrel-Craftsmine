package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
public class LevelEventT implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player){
      org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket) pk;
      LevelEventPacket subpacket = new LevelEventPacket();
        subpacket.setData(packet.datae);
      subpacket.setPosition(Vector3f.from(packet.x, packet.y, packet.z));
      switch(packet.eventID){
        case org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket.Events.EVENT_SOUND_CLICK:
          subpacket.setType(LevelEvent.SOUND_CLICK);
          break;
          case org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket.Events.EVENT_SOUND_CLICK_FAIL:
          subpacket.setType(LevelEvent.SOUND_CLICK_FAIL);
          break;
          case org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket.Events.EVENT_SOUND_DOOR_OPEN:
          subpacket.setType(LevelEvent.SOUND_DOOR_OPEN);
          break;
          case org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket.Events.EVENT_SOUND_FIZZ:
          subpacket.setType(LevelEvent.SOUND_FIZZ);
          break;
          case org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket.Events.EVENT_PLAYERS_SLEEPING:
          subpacket.setType(LevelEvent.SLEEPING_PLAYERS);
          break;
          case org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket.Events.EVENT_SET_DATA:
          subpacket.setType(LevelEvent.SET_DATA);
          break;
          default:
              return;
      }
      player.getBedrockClientSession().sendPacket(subpacket);
    }
}
