package org.CreadoresProgram.CraftsMine.network.translator.bedrock;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.BedrockPacketTranslator;
import org.CreadoresProgram.CraftsMine.network.protocol.StartGamePacket;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.CreadoresProgram.CraftsMine.network.translator.ReloadAuthP;
import org.cloudburstmc.protocol.bedrock.data.AuthoritativeMovementMode;
import java.util.concurrent.TimeUnit;
import java.util.List;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.protocol.common.SimpleDefinitionRegistry;
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition;
import org.cloudburstmc.protocol.bedrock.data.definitions.SimpleItemDefinition;
import org.CreadoresProgram.CraftsMine.utils.BlockMapper;
import org.CreadoresProgram.CraftsMine.server.Server;

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

    // Extract block palette from StartGamePacket and build runtime->legacy mapping
    List<NbtMap> blockPalette = null;
    try {
      blockPalette = packet.getBlockPalette();
    } catch (Throwable ignored) {
    }
    if (Server.getInstance().getConfig().isDebug()) {
      Server.getInstance().getLogger().debug(
        "[StartGame] getBlockPalette() returned: " +
        (blockPalette == null ? "NULL" : ("size=" + blockPalette.size())));
    }

    // If getBlockPalette() returned null, try reflection to find the field
    if (blockPalette == null || blockPalette.isEmpty()) {
      try {
        for (java.lang.reflect.Field f : packet.getClass().getDeclaredFields()) {
          if (f.getName().contains("blockPalette") || f.getName().contains("block_palette")) {
            f.setAccessible(true);
            Object val = f.get(packet);
            if (Server.getInstance().getConfig().isDebug()) {
              Server.getInstance().getLogger().debug(
                "[StartGame] Found field '" + f.getName() +
                "' type=" + f.getType().getName() +
                " value=" + (val == null ? "NULL" : val.getClass().getName()));
            }
            if (val instanceof List) {
              @SuppressWarnings("unchecked")
              List<NbtMap> casted = (List<NbtMap>) val;
              blockPalette = casted;
              break;
            } else if (val instanceof NbtMap) {
              // It's a compound containing a "blocks" list
              NbtMap compound = (NbtMap) val;
              List<NbtMap> blocks = compound.getList("blocks", NbtType.COMPOUND);
              if (blocks != null && !blocks.isEmpty()) {
                blockPalette = blocks;
                break;
              }
            }
          }
        }
      } catch (Throwable t) {
        if (Server.getInstance().getConfig().isDebug()) {
          Server.getInstance().getLogger().debug(
            "[StartGame] Reflection search for blockPalette failed: " + t);
        }
      }
    }

    if (blockPalette != null && !blockPalette.isEmpty()) {
      player.setBlockMapper(new BlockMapper(blockPalette));
      if (Server.getInstance().getConfig().isDebug()) {
        Server.getInstance().getLogger().debug(
          "[StartGame] BlockMapper created from StartGamePacket palette, size=" +
          player.getBlockMapper().size());
      }
    } else if (Server.getInstance().getBlockDefinitions() != null) {
      // Fallback: use bundled palette
      List<NbtMap> bundled = new java.util.ArrayList<>();
      for (int i = 0; ; i++) {
        try {
          org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition bd =
                  Server.getInstance().getBlockDefinitions().getDefinition(i);
          if (bd == null) break;
          java.lang.reflect.Field f = bd.getClass().getDeclaredField("definition");
          f.setAccessible(true);
          bundled.add((NbtMap) f.get(bd));
        } catch (Throwable t) {
          break;
        }
      }
      if (!bundled.isEmpty()) {
        player.setBlockMapper(new BlockMapper(bundled));
        if (Server.getInstance().getConfig().isDebug()) {
          Server.getInstance().getLogger().debug(
            "[StartGame] BlockMapper created from BUNDLED palette, size=" +
            player.getBlockMapper().size() + " (WARNING: may not match backend version)");
        }
      } else {
        Server.getInstance().getLogger().warn(
          "[StartGame] BlockMapper NOT created - no block palette available! Chunks will be empty.");
      }
    } else {
      Server.getInstance().getLogger().warn(
        "[StartGame] BlockMapper NOT created - no block definitions available! Chunks will be empty.");
    }

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
