package gay.paradox.utils;

import gay.paradox.mixin.MinecraftClientAccessor;
import gay.paradox.mixin.MouseHandlerAccessor;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class MouseSimulation implements Imports {
    private static final HashMap<Integer, Boolean> mouseButtons = new HashMap<>();
    private static final ExecutorService clickExecutor = Executors.newFixedThreadPool(100);

    public static MouseHandlerAccessor getMouseHandler() {
        return (MouseHandlerAccessor)((MinecraftClientAccessor) mc).getMouse();
    }

    public static boolean isMouseButtonPressed(int keyCode) {
        return mouseButtons.getOrDefault(keyCode, false);
    }

    public static void mousePress(int keyCode) {
        mouseButtons.put(keyCode, true);
        getMouseHandler().press(mc.getWindow().getHandle(), keyCode, 1, 0);
    }

    public static void mouseRelease(int keyCode) {
        getMouseHandler().press(mc.getWindow().getHandle(), keyCode, 0, 0);
    }

    public static void mouseClick(int keyCode, int millis) {
        clickExecutor.submit(() -> {
            try {
                mousePress(keyCode);
                Thread.sleep(millis);
                mouseRelease(keyCode);
            } catch (InterruptedException ignored) {}
        });
    }

    public static void mouseClick(int keyCode) {
        mouseClick(keyCode, 35);
    }
}
