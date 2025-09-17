package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.SetEntityDataPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import java.util.HashMap;
import java.util.Map;
import org.CreadoresProgram.CraftsMine.server.Server;
import java.io.IOException;
public class SetEntityData implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetEntityDataPacket) pk;
      SetEntityDataPacket npk = new SetEntityDataPacket();
      npk.eid = packet.getRuntimeEntityId();
      Map<Integer, Object[]> map = new HashMap<>();
        for(EntityDataType arg : packet.getMetadata().keySet()){
            if(arg == EntityDataTypes.NAME){
                map.put(2, new Object[]{ 4, ((String) packet.getMetadata().get(arg)) });
            }else if(arg == EntityDataTypes.PLAYER_FLAGS){
                map.put(16, new Object[]{ 0, ((byte) packet.getMetadata().get(arg)) });
            }else if(arg == EntityDataTypes.EFFECT_COLOR){
                map.put(7, new Object[]{ 2, ((int) packet.getMetadata().get(arg)) });
            }else if(arg == EntityDataTypes.EFFECT_AMBIENCE){
                map.put(8, new Object[]{ 0, ((byte) packet.getMetadata().get(arg)) });
            }else if(arg == EntityDataTypes.NAMETAG_ALWAYS_SHOW){
                map.put(3, new Object[]{ 0, ((byte) packet.getMetadata().get(arg)) });
            }else if(arg == EntityDataTypes.LEASH_HOLDER){
                map.put(23, new Object[]{ 7, ((long) packet.getMetadata().get(arg)) });
            }else if(arg == EntityDataTypes.AIR_SUPPLY){
                map.put(1, new Object[]{ 1, ((short) packet.getMetadata().get(arg)) });
            }
        }
        if(packet.getMetadata().getFlags() != null && packet.getMetadata().getFlags().size() > 0){
            long value = 0;
                int lower = 0 * 64;
                int upper = lower + 64;
            int flagIndex = 0;
            for(EntityFlag flag : packet.getMetadata().getFlags()){
                switch(flag){
                    case ON_FIRE:
                        flagIndex = 0;
                        break;
                    case SNEAKING:
                        flagIndex = 1;
                        break;
                    case RIDING:
                        flagIndex = 2;
                        break;
                    case SPRINTING:
                        flagIndex = 3;
                        break;
                    case INVISIBLE:
                        flagIndex = 5;
                        break;
                    case SLEEPING:
                        flagIndex = 1;
                        break;
                    case PLAYING_DEAD:
                        flagIndex = 2;
                        break;
                    case BABY:
                        flagIndex = 0;
                        break;
                    default:
                        continue;
                }
                if(flagIndex >= lower && flagIndex < upper){
                    value |= 1L << (flagIndex & 0x3f);
                }
            }
            map.put(0, new Object[]{ 7, ((long) value) });
        }
        npk.metadata = map;
        player.sendDataCraftsman(npk);
    }
}
