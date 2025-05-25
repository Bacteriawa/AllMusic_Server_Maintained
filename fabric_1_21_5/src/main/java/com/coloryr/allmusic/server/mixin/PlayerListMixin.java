package com.coloryr.allmusic.server.mixin;

import com.coloryr.allmusic.server.core.AllMusic;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(at = @At("HEAD"), method = "remove")
    private void remove(ServerPlayer serverPlayer, CallbackInfo ci) {
        AllMusic.removeNowPlayPlayer(serverPlayer.getName().getString());
    }

    @Inject(at = @At("TAIL"), method = "placeNewPlayer")
    private void add(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
        AllMusic.joinPlay(serverPlayer.getName().getString());
    }
}
