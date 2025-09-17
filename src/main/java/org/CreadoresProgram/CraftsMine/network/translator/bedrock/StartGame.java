package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.StartGamePacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.translator.ReloadAuthP;
import org.cloudburstmc.protocol.bedrock.data.AuthoritativeMovementMode;
import java.util.concurrent.TimeUnit;
import org.cloudburstmc.protocol.common.SimpleDefinitionRegistry;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;

public class StartGame implements BedrockPacketTranslator{
  @Override
  public boolean immediate() {
    return true;
  }
  @Override
  public void translate(BedrockPacket pk, Player player){
    org.cloudburstmc.protocol.bedrock.packet.StartGamePacket packet = (org.cloudburstmc.protocol.bedrock.packet.StartGamePacket) pk;
    StartGamePacket npk = new StartGamePacket();
    npk.seed = Math.toIntExact(packet.getSeed());
    npk.generator = packet.getGeneratorId();
    npk.eid = packet.getRuntimeEntityId();
    player.setRuntimeEntityId(packet.getRuntimeEntityId());
    int spawnX = (int) packet.getDefaultSpawn().getX();
    int spawnY = (int) packet.getDefaultSpawn().getY();
    int spawnZ = (int) packet.getDefaultSpawn().getZ();
    npk.spawnX = spawnX;
    npk.spawnY = spawnY;
    npk.spawnZ = spawnZ;
    npk.x = packet.getPlayerPosition().getX();
    npk.y = packet.getPlayerPosition().getY();
    npk.z = packet.getPlayerPosition().getZ();
    switch(packet.getPlayerGameType()){
      case SURVIVAL:
      case DEFAULT:
        npk.gamemode = 0 & 0b11 & 0x01;
        break;
      case CREATIVE:
        npk.gamemode = 1 & 0b11 & 0x01;
        break;
      case ADVENTURE:
        npk.gamemode = 2 & 0b11 & 0x01;
        break;
      case SURVIVAL_VIEWER:
        npk.gamemode = 3 & 0b11 & 0x01;
        break;
      case CREATIVE_VIEWER:
        npk.gamemode = 4 & 0b11 & 0x01;
        break;
    }
    npk.b1 = true;
    npk.b2 = true;
    npk.b3 = false;
    npk.unknownstr = "";
    if(packet.getDimensionId() == 2){
      npk.dimension = 1 & 0xFF;
    }else{
      byte dimention = (byte) packet.getDimensionId();
      byte subdimension = (byte) (dimention & 0xFF);
      npk.dimension = subdimension;
    }
    SimpleDefinitionRegistry<ItemDefinition> itemDefinitions = SimpleDefinitionRegistry.<ItemDefinition>builder()
                .addAll(packet.getItemDefinitions())
                .add(new SimpleItemDefinition("minecraft:empty", 0, false))
                .build();
    player.getBedrockClientSession().getPeer().getCodecHelper().setItemDefinitions(itemDefinitions);
    if(packet.getAuthoritativeMovementMode() == AuthoritativeMovementMode.SERVER){
      player.setPlayerInputAuth(true);
      player.setTikPia(packet.getCurrentTick());
      player.getPlayerInputExecutor().scheduleAtFixedRate(new Runnable(){
        @Override
        public void run(){
          player.setTikPia(player.getTikPia() + 1);
          ReloadAuthP auht = new ReloadAuthP(player);
        }
      }, 0, 50, TimeUnit.MILLISECONDS);
    }
    player.sendDataCraftsman(npk);
    player.getUniquesEntitysIds().put(packet.getUniqueEntityId(), packet.getRuntimeEntityId());
  }
}
