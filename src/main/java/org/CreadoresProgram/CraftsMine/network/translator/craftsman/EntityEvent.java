package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityEventType;
public class EntityEvent implements CraftsmanPacketTranslator {

    @Override
    public void translate(MCDPacket pk, Player player) {
        org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket) pk;
      EntityEventPacket npk = new EntityEventPacket();
      npk.setRuntimeEntityId(packet.eid);
      switch(packet.event){
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.HURT_ANIMATION:
          npk.setType(EntityEventType.HURT);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.DEATH_ANIMATION:
          npk.setType(EntityEventType.DEATH);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.TAME_FAIL:
          npk.setType(EntityEventType.TAME_FAILED);
            break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.TAME_SUCCESS:
          npk.setType(EntityEventType.TAME_SUCCEEDED);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.RESPAWN:
          npk.setType(EntityEventType.RESPAWN);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.USE_ITEM:
          npk.setType(EntityEventType.USE_ITEM);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.SHAKE_WET:
          npk.setType(EntityEventType.SHAKE_WETNESS);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.EAT_GRASS_ANIMATION:
          npk.setType(EntityEventType.EAT_GRASS);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.FISH_HOOK_BUBBLE:
          npk.setType(EntityEventType.FISH_HOOK_BUBBLE);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.FISH_HOOK_POSITION:
          npk.setType(EntityEventType.FISH_HOOK_POSITION);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.FISH_HOOK_HOOK:
          npk.setType(EntityEventType.FISH_HOOK_TIME);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.FISH_HOOK_TEASE:
          npk.setType(EntityEventType.FISH_HOOK_TEASE);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.SQUID_INK_CLOUD:
          npk.setType(EntityEventType.SQUID_FLEEING);
          break;
        case org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket.AMBIENT_SOUND:
          npk.setType(EntityEventType.PLAY_AMBIENT);
          break;
        default:
          return;
      }
        player.getBedrockClientSession().sendPacket(npk);
    }
}
