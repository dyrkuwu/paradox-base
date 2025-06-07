package gay.paradox.event.impl;

import gay.paradox.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventKey extends Event {
    private int key;
    private int action;
}
