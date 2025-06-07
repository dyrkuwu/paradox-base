package gay.paradox.handlers.impl;

import gay.paradox.Paradox;
import gay.paradox.event.Subscribe;
import gay.paradox.event.impl.EventKey;
import gay.paradox.features.Feature;
import gay.paradox.handlers.Handler;
import org.lwjgl.glfw.GLFW;

public class KeyHandler extends Handler {
    @Subscribe
    public void onKey(EventKey event) {
        if (mc.currentScreen != null)
            return;

        if (event.getAction() != GLFW.GLFW_PRESS)
            return;

        for (Feature feature : Paradox.featureManager.getFeatures()) {
            if (feature.getKey() == event.getKey()) {
                feature.toggle();
            }
        }
    }
}
