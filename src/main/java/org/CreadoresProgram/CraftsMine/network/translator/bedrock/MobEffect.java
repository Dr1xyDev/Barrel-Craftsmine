package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.MobEffectPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class MobEffect implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.MobEffectPacket packet = (org.cloudburstmc.protocol.bedrock.packet.MobEffectPacket) pk;
      if(packet.getEffectId() > 23){
        return;
      }
        MobEffectPacket npk = new MobEffectPacket();
      npk.eid = packet.getRuntimeEntityId();
      switch(packet.getEvent()){
        case ADD:
          npk.eventId = MobEffectPacket.EVENT_ADD;
          break;
        case MODIFY:
          npk.eventId = MobEffectPacket.EVENT_MODIFY;
          break;
        case REMOVE:
          npk.eventId = MobEffectPacket.EVENT_REMOVE;
          break;
        default:
          npk.eventId = 0;
          break;
      }
      npk.amplifier = packet.getAmplifier();
      npk.particles = packet.isParticles();
      npk.duration = packet.getDuration();
      npk.effectId = packet.getEffectId();
        player.sendDataCraftsman(npk);
    }
}
