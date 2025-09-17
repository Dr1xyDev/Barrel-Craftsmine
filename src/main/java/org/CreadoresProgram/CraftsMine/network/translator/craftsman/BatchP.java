package org.CreadoresProgram.CraftsMine.network.translator.craftsman;
import org.CreadoresProgram.CraftsMine.network.protocol.base.MCDPacket;
import org.CreadoresProgram.CraftsMine.network.translator.interfaces.CraftsmanPacketTranslator;
import org.CreadoresProgram.CraftsMine.player.Player;
import org.CreadoresProgram.CraftsMine.nukkitLib.utils.Zlib;
import org.CreadoresProgram.CraftsMine.dragonetLib.proxy.utilities.BinaryStream;
import java.io.IOException;
import org.CreadoresProgram.CraftsMine.server.Server;
import org.CreadoresProgram.CraftsMine.network.protocol.Interface.Protocol;
import org.CreadoresProgram.CraftsMine.dragonetLib.proxy.utilities.Binary;

public class BatchP implements CraftsmanPacketTranslator{
  public void translate(MCDPacket pk, Player player){
    org.CreadoresProgram.CraftsMine.network.protocol.BatchPacket packet = (org.CreadoresProgram.CraftsMine.network.protocol.BatchPacket) pk;
    boolean isDebug = Server.getInstance().getConfig().isDebug();
    if(isDebug){
      Server.getInstance().getLogger().debug("decode BatchPacket Craftsman size...");
    }
    byte[] payload = packet.payload;
    byte[] decompresPayload = new byte[1024 * 1024 * 64];
    try{
      decompresPayload = Zlib.inflate(payload);
    }catch(IOException ex){
      return;
    }
    int Decomsize = decompresPayload.length;
    BinaryStream dataReader = new BinaryStream(decompresPayload);
    int offset = 0;
    while (offset < Decomsize) {
      int pklen = dataReader.getInt();
      offset += 4;
      byte[] pkData = dataReader.get(pklen);
      if(pkData == null || pkData.length == 0){
        return;
      }
      offset += pklen;
      MCDPacket pk2 = Protocol.decode(pkData);
      if(isDebug){
        Server.getInstance().getLogger().debug("Process "+pk2.getClass().getSimpleName()+" 0x"+Binary.bytesToHexString(pk2.getBuffer()));
      }
      player.getPacketTranslatorManager().translate(pk2);
    }
  }
}
