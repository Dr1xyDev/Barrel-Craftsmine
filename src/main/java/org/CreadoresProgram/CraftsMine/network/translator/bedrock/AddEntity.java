package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.AddEntityPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.SetEntityLinkPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
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
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.CreadoresProgram.CraftsMine.nukkitLib.entity.Attribute;
import org.CreadoresProgram.CraftsMine.network.protocol.UpdateAttributesPacket;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.attribute.AttributeModifierData;
public class AddEntity implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket packet = (org.cloudburstmc.protocol.bedrock.packet.AddEntityPacket) pk;
      AddEntityPacket npk = new AddEntityPacket();
      npk.eid = packet.getRuntimeEntityId();
      npk.x = (float) packet.getPosition().getX();
      npk.y = (float) packet.getPosition().getY();
      npk.z = (float) packet.getPosition().getZ();
      npk.speedX = (float) packet.getMotion().getX();
        npk.speedY = (float) packet.getMotion().getY();
        npk.speedZ = (float) packet.getMotion().getZ();
      npk.pitch = (float) packet.getRotation().getX();
      npk.yaw = (float) packet.getRotation().getY();
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
      npk.type = packet.getEntityType();
        npk.modifiers = 0;
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
                    default:
                        continue;
                }
                player.sendDataCraftsman(n2pk);
            }
        }
        if(packet.getAttributes().size() > 0){
            UpdateAttributesPacket n3pk = new UpdateAttributesPacket();
            n3pk.entityId = packet.getRuntimeEntityId();
            List<Attribute> entryss = new ObjectArrayList<>();
            for(AttributeData attr : packet.getAttributes()){
                Attribute subatr = Attribute.getAttributeByName(attr.getName());
                if(subatr != null){
                    entryss.add(new Attribute(subatr.getId(), attr.getName(), attr.getMinimum(), attr.getMaximum(), attr.getDefaultValue(), true));
                }
                for(AttributeModifierData subattr : attr.getModifiers()){
                    Attribute subatr2 = Attribute.getAttributeByName(subattr.getName());
                    if(subatr2 != null){
                        subatr2 = new Attribute(subatr2.getId(), subatr2.getName(), attr.getMinimum(), attr.getMaximum(), attr.getDefaultValue(), true);
                        subatr2.setValue(subattr.getAmount());
                        entryss.add(subatr2);
                    }
                }
            }
            if(entryss.size() < 1){
                return;
            }
            n3pk.entries = entryss.stream().toArray(Attribute[]::new);
            player.sendDataCraftsman(n3pk);
        }
    }
}
