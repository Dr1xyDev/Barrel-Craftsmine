package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.data.LevelEvent;
import org.cloudburstmc.protocol.bedrock.data.ParticleType;
public class LevelEventT implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket packet = (org.cloudburstmc.protocol.bedrock.packet.LevelEventPacket) pk;
    LevelEventPacket npk = new LevelEventPacket();
    if(packet.getType().getClass().isAssignableFrom(LevelEvent.class)){
    switch((LevelEvent) packet.getType()){
      case UNDEFINED:
        return;
      case SOUND_CLICK:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_CLICK;
        break;
        case SOUND_CLICK_FAIL:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_CLICK_FAIL;
        break;
        case SOUND_LAUNCH:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_SHOOT;
        break;
        case SOUND_DOOR_OPEN:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_DOOR_OPEN;
        break;
        case SOUND_FIZZ:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_FIZZ;
        break;
        case SOUND_FUSE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_TNT;
        break;
        case SOUND_GHAST_WARNING:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_GHAST;
        break;
      case SOUND_GHAST_FIREBALL:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_GHAST_SHOOT;
        break;
        case SOUND_BLAZE_FIREBALL:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_BLAZE_SHOOT;
        break;
        case SOUND_ZOMBIE_DOOR_BUMP:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_DOOR_BUMP;
        break;
        case SOUND_ZOMBIE_DOOR_CRASH:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_BREAK_WOODEN_DOOR;
        break;
        case SOUND_ZOMBIE_INFECTED:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ZOMBIE_INFECT;
        break;
        case SOUND_ZOMBIE_CONVERTED:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ZOMBIE_HEAL;
        break;
        case SOUND_ENDERMAN_TELEPORT:
      case SOUND_TELEPORT_ENDERPEARL:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ENDERMAN_TELEPORT;
        break;
        case SOUND_ANVIL_BROKEN:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ANVIL_BREAK;
        break;
        case SOUND_ANVIL_USED:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ANVIL_USE;
        break;
        case SOUND_ANVIL_LAND:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ANVIL_LAND;
        break;
        case SOUND_ITEMFRAME_ITEM_ADD:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ITEM_FRAME_ITEM_ADDED;
        break;
        case SOUND_ITEMFRAME_BREAK:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ITEM_FRAME_REMOVED;
        break;
        case SOUND_ITEMFRAME_PLACE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ITEM_FRAME_PLACED;
        break;
        case SOUND_ITEMFRAME_ITEM_REMOVE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ITEM_FRAME_ITEM_REMOVED;
        break;
        case SOUND_ITEMFRAME_ITEM_ROTATE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ITEM_FRAME_ITEM_ROTATED;
        break;
        case SOUND_CAMERA:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_CAMERA_TAKE_PICTURE;
        break;
        case SOUND_EXPERIENCE_ORB_PICKUP:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_EXPERIENCE_ORB;
        break;
        case SOUND_POINTED_DRIPSTONE_LAND:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_BLOCK_PLACE;
        break;
        case SOUND_DYE_USED:
      case CAULDRON_DYE_ARMOR:
        case CAULDRON_ADD_DYE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_CAULDRON_DYE_ARMOR;
        break;
        case PARTICLE_SHOOT:
        npk.eventID = LevelEventPacket.Events.EVENT_PARTICLE_SHOOT;
        break;
      case PARTICLE_DESTROY_BLOCK:
        npk.eventID = LevelEventPacket.Events.EVENT_PARTICLE_DESTROY;
        break;
        case PARTICLE_POTION_SPLASH:
        npk.eventID = LevelEventPacket.Events.EVENT_PARTICLE_SPLASH;
        break;
        case PARTICLE_EYE_OF_ENDER_DEATH:
        npk.eventID = LevelEventPacket.Events.EVENT_PARTICLE_EYE_DESPAWN;
        break;
        case PARTICLE_MOB_BLOCK_SPAWN:
      case PARTICLE_GENERIC_SPAWN:
        npk.eventID = LevelEventPacket.Events.EVENT_PARTICLE_SPAWN;
        break;
        case CAULDRON_FILL_POTION:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_CAULDRON_FILL_POTION;
        break;
        case CAULDRON_FILL_WATER:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_CAULDRON_FILL_WATER;
        break;
        case SLEEPING_PLAYERS:
        npk.eventID = LevelEventPacket.Events.EVENT_PLAYERS_SLEEPING;
        break;
        case START_RAINING:
        npk.eventID = LevelEventPacket.Events.EVENT_START_RAIN;
        break;
      case START_THUNDERSTORM:
        npk.eventID = LevelEventPacket.Events.EVENT_START_THUNDER;
        break;
        case STOP_RAINING:
        npk.eventID = LevelEventPacket.Events.EVENT_STOP_RAIN;
        break;
        case STOP_THUNDERSTORM:
        npk.eventID = LevelEventPacket.Events.EVENT_STOP_THUNDER;
        break;
      case SET_DATA:
        npk.eventID = LevelEventPacket.Events.EVENT_SET_DATA;
        break;
      default:
        return;
    }
    }else if(packet.getType().getClass().isAssignableFrom(ParticleType.class)){
      switch((ParticleType) packet.getType()){
        case UNDEFINED:
          return;
        case BUBBLE:
          npk.eventID = 1;
          break;
        case CRIT:
          npk.eventID = 2;
          break;
        case SMOKE:
          npk.eventID = 3;
          break;
        case EXPLODE:
          npk.eventID = 4;
          break;
        case EVAPORATION:
          npk.eventID = 5;
          break;
        case FLAME:
          npk.eventID = 6;
          break;
        case LAVA:
          npk.eventID = 7;
          break;
        case LARGE_SMOKE:
          npk.eventID = 8;
          break;
        case RED_DUST:
          npk.eventID = 9;
          break;
        case ICON_CRACK:
          npk.eventID = 10;
          break;
        case SNOWBALL_POOF:
          npk.eventID = 11;
          break;
        case LARGE_EXPLODE:
          npk.eventID = 12;
          break;
        case HUGE_EXPLOSION:
          npk.eventID = 13;
          break;
        case MOB_FLAME:
          npk.eventID = 14;
          break;
        case HEART:
          npk.eventID = 15;
          break;
        case TERRAIN:
          npk.eventID = 16;
          break;
        case TOWN_AURA:
          npk.eventID = 17;
          break;
        case PORTAL:
          npk.eventID = 18;
          break;
        case WATER_SPLASH:
          npk.eventID = 19;
          break;
        case WATER_WAKE:
          npk.eventID = 20;
          break;
        case DRIP_WATER:
          npk.eventID = 21;
          break;
        case DRIP_LAVA:
          npk.eventID = 22;
          break;
        case FALLING_DUST:
          npk.eventID = 23;
          break;
        case MOB_SPELL:
          npk.eventID = 24;
          break;
        case MOB_SPELL_AMBIENT:
          npk.eventID = 25;
          break;
        case MOB_SPELL_INSTANTANEOUS:
          npk.eventID = 26;
          break;
        case INK:
          npk.eventID = 27;
          break;
        case SLIME:
          npk.eventID = 28;
          break;
        case RAIN_SPLASH:
          npk.eventID = 29;
          break;
        case VILLAGER_ANGRY:
          npk.eventID = 30;
          break;
        case VILLAGER_HAPPY:
          npk.eventID = 31;
          break;
        case ENCHANTING_TABLE:
          npk.eventID = 32;
          break;
        default:
          npk.eventID = LevelEventPacket.Events.EVENT_ADD_PARTICLE_MASK;
          break;
      }
    }
    npk.datae = packet.getData();
    npk.x = packet.getPosition().getX();
    npk.y = packet.getPosition().getY();
    npk.z = packet.getPosition().getZ();
    player.sendDataCraftsman(npk);
  }
}
