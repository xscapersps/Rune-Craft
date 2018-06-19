package com.elvarg.game.entity.impl.object.handle;

import com.elvarg.game.entity.impl.player.Player;

public abstract class ClickEvent {

    protected abstract int getPacket();
    protected abstract boolean handle(Player player);

}
