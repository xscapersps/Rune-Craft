package com.elvarg.game.entity.impl.object.handle;
import com.elvarg.game.entity.impl.player.Player;
import com.elvarg.net.packet.Packet;

public class ClickEventHandle {

    private ClickEvent[] events;

    public ClickEventHandle(ClickEvent... events) {
        this.events = events;
    }

    public boolean handle(Packet packet, Player player) {
        for (ClickEvent event : events) {
            if(event.getPacket() == packet.getOpcode()) {
                if(event.handle(player))
                    return true;
            }
        }
        return false;
    }

}
