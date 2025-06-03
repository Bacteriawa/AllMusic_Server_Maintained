package com.coloryr.allmusic.server.side.fabric.event;

import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public interface MusicAddEvent {
    Event<MusicAddEvent> EVENT = EventFactory.createArrayBacked(MusicAddEvent.class,
            (listeners) -> (player, music) -> {
                for (MusicAddEvent listener : listeners) {
                    InteractionResult result = listener.interact(player, music);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult interact(Player player, MusicObj music);
}
