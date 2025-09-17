package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.EntityEventPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
public class EntityEvent implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket packet = (org.cloudburstmc.protocol.bedrock.packet.EntityEventPacket) pk;
      EntityEventPacket npk = new EntityEventPacket();
      npk.eid = packet.getRuntimeEntityId();
      switch(packet.getType()){
        case NONE:
          return;
        case HURT:
          npk.event = EntityEventPacket.HURT_ANIMATION;
          break;
        case DEATH:
          npk.event = EntityEventPacket.DEATH_ANIMATION;
          break;
        case TAME_FAILED:
          npk.event = EntityEventPacket.TAME_FAIL;
            break;
        case TAME_SUCCEEDED:
          npk.event = EntityEventPacket.TAME_SUCCESS;
          break;
        case RESPAWN:
          npk.event = EntityEventPacket.RESPAWN;
          break;
        case USE_ITEM:
          npk.event = EntityEventPacket.USE_ITEM;
          break;
        case SHAKE_WETNESS:
          npk.event = EntityEventPacket.SHAKE_WET;
          break;
        case EAT_GRASS:
          npk.event = EntityEventPacket.EAT_GRASS_ANIMATION;
          break;
        case FISH_HOOK_BUBBLE:
          npk.event = EntityEventPacket.FISH_HOOK_BUBBLE;
          break;
        case FISH_HOOK_POSITION:
          npk.event = EntityEventPacket.FISH_HOOK_POSITION;
          break;
        case FISH_HOOK_TIME:
          npk.event = EntityEventPacket.FISH_HOOK_HOOK;
          break;
        case FISH_HOOK_TEASE:
          npk.event = EntityEventPacket.FISH_HOOK_TEASE;
          break;
        case SQUID_FLEEING:
          npk.event = EntityEventPacket.SQUID_INK_CLOUD;
          break;
        case PLAY_AMBIENT:
          npk.event = EntityEventPacket.AMBIENT_SOUND;
          break;
        default:
          return;
      }
        player.sendDataCraftsman(npk);
    }
}
