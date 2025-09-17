package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.PlayerListPacket;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.nukkitLib.entity.data.Skin;
import java.util.ArrayList;
import java.util.List;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.EnumSet;
import java.util.UUID;
import org.CreadoresProgram.CraftsMine.server.Server;
public class PlayerList implements BedrockPacketTranslator {

    @Override
    public void translate(BedrockPacket pk, Player player) {
      org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket packet = (org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket) pk;
      PlayerListPacket npack = new PlayerListPacket();
      if(packet.getAction() == org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket.Action.ADD){
        npack.type = PlayerListPacket.TYPE_ADD;
      }else{
        npack.type = PlayerListPacket.TYPE_REMOVE;
      }
      List<PlayerListPacket.Entry> entrys = new ObjectArrayList<>();
      for(org.cloudburstmc.protocol.bedrock.packet.PlayerListPacket.Entry entry : packet.getEntries()){
          try{
          entrys.add(new PlayerListPacket.Entry(entry.getUuid(), entry.getEntityId(), entry.getName().replaceAll("§r", "§f§r"), new Skin(entry.getSkin().getSkinData().getImage(), entry.getSkin().getGeometryData())));
          }catch(Exception ex){
              
          }
      }
      npack.entries = entrys.stream().toArray(PlayerListPacket.Entry[]::new);
      player.sendDataCraftsman(npack);
    }
}
