package com.coloryr.allmusic.server.side.fabric;

import com.coloryr.allmusic.server.AllMusicFabric;
import com.coloryr.allmusic.server.PackPayload;
import com.coloryr.allmusic.server.TaskItem;
import com.coloryr.allmusic.server.Tasks;
import com.coloryr.allmusic.server.core.AllMusic;
import com.coloryr.allmusic.server.core.objs.enums.ComType;
import com.coloryr.allmusic.server.core.objs.music.MusicObj;
import com.coloryr.allmusic.server.core.objs.music.SongInfoObj;
import com.coloryr.allmusic.server.core.side.BaseSide;
import com.coloryr.allmusic.server.side.fabric.event.MusicAddEvent;
import com.coloryr.allmusic.server.side.fabric.event.MusicPlayEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;

import java.io.File;
import java.util.Collection;

public class SideFabric extends BaseSide {
    @Override
    public void runTask(Runnable run) {
        AllMusicFabric.server.execute(run);
    }

    @Override
    public void runTask(Runnable run1, int delay) {
        Tasks.add(new TaskItem() {{
            tick = delay;
            run = run1;
        }});
    }

    @Override
    public boolean checkPermission(Object player) {
        CommandSourceStack source = (CommandSourceStack) player;
        return source.hasPermission(2);
    }

    @Override
    public boolean isPlayer(Object player) {
        CommandSourceStack source = (CommandSourceStack) player;
        return source.isPlayer();
    }

    @Override
    public boolean checkPermission(Object player, String permission) {
        return checkPermission(player);
    }

    @Override
    public boolean needPlay(boolean islist) {
        for (var player : AllMusicFabric.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false, islist)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<?> getPlayers() {
        return AllMusicFabric.server.getPlayerList().getPlayers();
    }

    @Override
    public String getPlayerName(Object player) {
        if (player instanceof ServerPlayer player1) {
            return player1.getName().getString();
        }

        return null;
    }

    @Override
    public String getPlayerServer(Object player) {
        return null;
    }

    @Override
    public void send(Object player, ComType type, String data, int data1) {
        if (player instanceof ServerPlayer player1) {
            send(player1, new PackPayload(type, data, data1));
        }
    }

    @Override
    public Object getPlayer(String player) {
        return AllMusicFabric.server.getPlayerList().getPlayerByName(player);
    }

    @Override
    public void sendBar(Object player, String data) {
        if (player instanceof ServerPlayer player1) {
            FabricApi.sendBar(player1, data);
        }
    }

    @Override
    public File getFolder() {
        return new File("allmusic/");
    }

    @Override
    public void broadcast(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        for (var player : AllMusicFabric.server.getPlayerList().getPlayers()) {
            if (!AllMusic.isSkip(player.getName().getString(), null, false)) {
                player.displayClientMessage(Component.nullToEmpty(message), false);
            }
        }
    }

    @Override
    public void broadcastWithRun(String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        FabricApi.sendMessageBqRun(message, end, command);
    }

    @Override
    public void sendMessage(Object obj, String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        CommandSourceStack source = (CommandSourceStack) obj;
        source.sendSystemMessage(Component.nullToEmpty(message));
    }

    @Override
    public void sendMessageRun(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        FabricApi.sendMessageRun(obj, message, end, command);
    }

    @Override
    public void sendMessageSuggest(Object obj, String message, String end, String command) {
        if (message == null || message.isEmpty()) {
            return;
        }
        FabricApi.sendMessageSuggest(obj, message, end, command);
    }

    @Override
    public boolean onMusicPlay(SongInfoObj obj) {
        return MusicPlayEvent.EVENT.invoker().interact(obj) != InteractionResult.PASS;
    }

    @Override
    public boolean onMusicAdd(Object obj, MusicObj music) {
        CommandSourceStack source = (CommandSourceStack) obj;
        return MusicAddEvent.EVENT.invoker().interact(source.getPlayer(), music) != InteractionResult.PASS;
    }

    private void send(ServerPlayer players, PackPayload data) {
        if (players == null)
            return;
        try {
            runTask(() -> ServerPlayNetworking.send(players, data));
        } catch (Exception e) {
            AllMusic.log.warning("§c数据发送发生错误");
            e.printStackTrace();
        }
    }
}
