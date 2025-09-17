package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.SetEntityLinkPacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityLinkData;
public class SetEntityLink implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
        org.cloudburstmc.protocol.bedrock.packet.SetEntityLinkPacket packet = (org.cloudburstmc.protocol.bedrock.packet.SetEntityLinkPacket) pk;
      SetEntityLinkPacket npk = new SetEntityLinkPacket();
      EntityLinkData link = packet.getEntityLink();
                npk.rider = link.getFrom();
                npk.riding = link.getTo();
                switch(link.getType()){
                    case REMOVE:
                        npk.type = SetEntityLinkPacket.TYPE_REMOVE;
                        break;
                    case RIDER:
                        npk.type = SetEntityLinkPacket.TYPE_RIDE;
                        break;
                    case PASSENGER:
                        npk.type = SetEntityLinkPacket.TYPE_PASSENGER;
                        break;
                }
                player.sendDataCraftsman(npk);
    }
}
