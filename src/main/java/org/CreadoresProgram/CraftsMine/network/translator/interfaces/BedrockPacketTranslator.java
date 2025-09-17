package org.CreadoresProgram.CraftsMine.network.translator.interfaces;

import org.CreadoresProgram.CraftsMine.player.Player;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

public interface BedrockPacketTranslator {

    default boolean immediate() {
        return false;
    }

    void translate(BedrockPacket pk, Player player);
}
