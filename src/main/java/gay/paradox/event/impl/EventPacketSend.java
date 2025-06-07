package gay.paradox.event.impl;

import gay.paradox.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.packet.Packet;

@Getter
@AllArgsConstructor
public class EventPacketSend extends Event {
    private Packet<?> packet;
}
