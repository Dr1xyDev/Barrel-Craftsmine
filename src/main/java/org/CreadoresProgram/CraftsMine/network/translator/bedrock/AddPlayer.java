package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.AddPlayerPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.SetEntityLinkPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.inventory.Item;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataType;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityFlag;
import java.util.HashMap;
import java.util.Map;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import org.cloudburstmc.nbt.NBTInputStream;
import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.protocol.common.util.stream.LittleEndianByteBufOutputStream;
import org.CreadoresProgram.CraftsMine.server.Server;
import java.io.IOException;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
public class AddPlayer implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket packet = (org.cloudburstmc.protocol.bedrock.packet.AddPlayerPacket) pk;
      AddPlayerPacket npk = new AddPlayerPacket();
      npk.uuid = packet.getUuid();
      npk.username = packet.getUsername();
      npk.eid = packet.getRuntimeEntityId();
      npk.x = (float) packet.getPosition().getX();
      npk.y = (float) packet.getPosition().getY();
      npk.z = (float) packet.getPosition().getZ();
      npk.speedX = (float) packet.getMotion().getX();
        npk.speedY = (float) packet.getMotion().getY();
        npk.speedZ = (float) packet.getMotion().getZ();
      npk.pitch = (float) packet.getRotation().getX();
      npk.yaw = (float) packet.getRotation().getY();
        byte[] nbt = new byte[0];
        if(packet.getHand().getTag() != null){
              ByteBuf buffer = Unpooled.buffer();
              try (NBTOutputStream stream = new NBTOutputStream(new LittleEndianByteBufOutputStream(buffer))) {
                stream.writeTag(packet.getHand().getTag());
            } catch (IOException e) {
                // This shouldn't happen (as this is backed by a Netty ByteBuf), but okay...
                Server.getInstance().getLogger().error("Unable to save NBT data", e);
              }
              nbt = buffer.array();
          }
      npk.item = Item.translateItem(new Item(packet.getHand().getDefinition().getRuntimeId(), packet.getHand().getDamage(), packet.getHand().getCount(), nbt));
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
        if(packet.getMetadata().getFlags().size() != 0){
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
            map.put(0, new Object[]{ 7, value });
        }
        npk.metadata = map;
        player.sendDataCraftsman(npk);
        player.getUniquesEntitysIds().put(packet.getUniqueEntityId(), packet.getRuntimeEntityId());
        if(packet.getEntityLinks().size() > 0){
            for(EntityLinkData link : packet.getEntityLinks()){
                SetEntityLinkPacket n2pk = new SetEntityLinkPacket();
                n2pk.rider = link.getFrom();
                n2pk.riding = link.getTo();
                switch(link.getType()){
                    case REMOVE:
                        n2pk.type = SetEntityLinkPacket.TYPE_REMOVE;
                        break;
                    case RIDER:
                        n2pk.type = SetEntityLinkPacket.TYPE_RIDE;
                        break;
                    case PASSENGER:
                        n2pk.type = SetEntityLinkPacket.TYPE_PASSENGER;
                        break;
                }
                player.sendDataCraftsman(n2pk);
            }
        }
    }
}
