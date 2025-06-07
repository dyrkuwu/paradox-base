package gay.paradox.event.impl;

import gay.paradox.event.Event;
import gay.paradox.event.EventPhase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class EventMove extends Event {
    private EventPhase phase;
    private double x, y, z;
    private boolean onGround;
    private double yaw, pitch;
}
