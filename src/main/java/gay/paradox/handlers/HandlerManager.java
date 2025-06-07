package gay.paradox.handlers;

import gay.paradox.Paradox;
import gay.paradox.handlers.impl.KeyHandler;
import lombok.Getter;

import java.util.ArrayList;

public class HandlerManager {
    @Getter
    private final ArrayList<Handler> handlers = new ArrayList<>();

    public void initialize() {
        handlers.add(new KeyHandler());

        for (Handler handler : handlers) {
            Paradox.EVENT_BUS.register(handler);
        }
    }
}