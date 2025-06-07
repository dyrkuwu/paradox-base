package gay.paradox.event;

import lombok.Setter;
import lombok.Getter;

@Setter
@Getter
public class Event {
    private boolean cancelled = false;
}
