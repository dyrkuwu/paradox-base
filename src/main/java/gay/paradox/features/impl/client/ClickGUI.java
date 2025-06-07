package gay.paradox.features.impl.client;

import gay.paradox.Paradox;
import gay.paradox.features.Category;
import gay.paradox.features.Feature;
import gay.paradox.features.FeatureInfo;
import org.lwjgl.glfw.GLFW;

@FeatureInfo(name = "ClickGUI", description = "Crazy gui", category = Category.CLIENT, key = GLFW.GLFW_KEY_RIGHT_SHIFT)
public class ClickGUI extends Feature {
    @Override
    public void onEnable() {
        Paradox.screenManager.displayClickGUI();
        this.toggle();
    }

    @Override
    public boolean isEnabled() {
        return Paradox.screenManager.isClickGUIOpen();
    }
}
