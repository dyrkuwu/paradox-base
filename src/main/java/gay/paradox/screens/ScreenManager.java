package gay.paradox.screens;

import gay.paradox.screens.clickgui.ClickGuiScreen;
import gay.paradox.utils.Imports;

public class ScreenManager implements Imports {
    private ClickGuiScreen clickGuiScreen;
    
    public void initialize() {
        clickGuiScreen = new ClickGuiScreen();
    }
    
    public void displayClickGUI() {
        mc.setScreen(clickGuiScreen);
    }
    
    public boolean isClickGUIOpen() {
        return mc.currentScreen == clickGuiScreen;
    }
    
    public void closeAllScreens() {
        mc.setScreen(null);
    }
}
