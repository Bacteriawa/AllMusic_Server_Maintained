package com.coloryr.allmusic.server;

import com.coloryr.allmusic.server.core.objs.enums.ComType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record PackPayload(ComType comType, String data, int data1) implements CustomPacketPayload {
    public static final Type<PackPayload> ID = new CustomPacketPayload.Type<>(AllMusicFabric.ID);
    public static final StreamCodec<FriendlyByteBuf, PackPayload> CODEC =
            StreamCodec.ofMember((value, buf) -> com.coloryr.allmusic.server.codec.PacketCodec.pack(buf, value.comType, value.data, value.data1),
                    buffer -> new PackPayload(ComType.CLEAR, null, 0));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
