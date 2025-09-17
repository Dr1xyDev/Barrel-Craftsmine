package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.LevelEventPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.data.SoundEvent;
public class LevelSoundEvent implements BedrockPacketTranslator{
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet packet = (org.cloudburstmc.protocol.bedrock.packet.LevelSoundEvent1Packet) pk;
    LevelEventPacket npk = new LevelEventPacket();
    switch(packet.getSound()){
      case UNDEFINED:
        return;
      case BREAK:
      case PLACE:
      case BREAK_BLOCK:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_BLOCK_PLACE;
        break;
        case BOW:
      case SHOOT:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_SHOOT;
        break;
        case FIZZ:
      case ITEM_FIZZ:
      case SPARKLER_USE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_FIZZ;
        break;
      case EXPLODE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_TNT;
        break;
        case LEVELUP:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_EXPERIENCE_ORB;
        break;
      case TELEPORT:
      case IMITATE_ENDERMAN:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ENDERMAN_TELEPORT;
        break;
      case CAMERA_TAKE_PICTURE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_CAMERA_TAKE_PICTURE;
        break;
      case IMITATE_BLAZE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_BLAZE_SHOOT;
        break;
      case IMITATE_GHAST:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_GHAST;
        break;
      case RANDOM_ANVIL_USE:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ANVIL_USE;
        break;
      case THROW:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ITEM_THROWN;
        break;
      case DROP_SLOT:
      case POP:
        npk.eventID = LevelEventPacket.Events.EVENT_SOUND_ITEM_DROP;
        break;
      default:
        return;
    }
    npk.datae = packet.getExtraData();
    npk.x = packet.getPosition().getX();
    npk.y = packet.getPosition().getY();
    npk.z = packet.getPosition().getZ();
    player.sendDataCraftsman(npk);
  }
}
