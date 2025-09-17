package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.UpdateAttributesPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.nukkitLib.entity.Attribute;
import org.cloudburstmc.protocol.bedrock.data.AttributeData;
import org.cloudburstmc.protocol.bedrock.data.attribute.AttributeModifierData;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
public class UpdateAttributes implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket packet = (org.cloudburstmc.protocol.bedrock.packet.UpdateAttributesPacket) pk;
        UpdateAttributesPacket npk = new UpdateAttributesPacket();
      npk.entityId = packet.getRuntimeEntityId();
      List<Attribute> entrys = new ObjectArrayList<>();
      for(AttributeData attr : packet.getAttributes()){
          Attribute subatr = Attribute.getAttributeByName(attr.getName());
        if(subatr != null){
        entrys.add(new Attribute(subatr.getId(), attr.getName(), attr.getMinimum(), attr.getMaximum(), attr.getDefaultValue(), true));
        }
          for(AttributeModifierData subattr : attr.getModifiers()){
              Attribute subatr2 = Attribute.getAttributeByName(subattr.getName());
              if(subatr2 != null){
                  subatr2 = new Attribute(subatr2.getId(), subatr2.getName(), attr.getMinimum(), attr.getMaximum(), attr.getDefaultValue(), true);
                  subatr2.setValue(subattr.getAmount());
                  entrys.add(subatr2);
              }
          }
      }
        if(entrys.size() == 0){
            return;
        }
      npk.entries = entrys.stream().toArray(Attribute[]::new);
      player.sendDataCraftsman(npk);
    }
}
