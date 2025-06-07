package gay.paradox;

import gay.paradox.event.EventBus;
import gay.paradox.features.FeatureManager;
import gay.paradox.handlers.HandlerManager;
import gay.paradox.render.Shaders;
import gay.paradox.render.font.Fonts;
import gay.paradox.screens.ScreenManager;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Paradox implements ModInitializer {
	public static final String MOD_ID = "paradox";
	public static EventBus EVENT_BUS = new EventBus();
	public static final FeatureManager featureManager = new FeatureManager();
	public static final ScreenManager screenManager = new ScreenManager();
	public static final HandlerManager handlerManager = new HandlerManager();
	public static Fonts fonts;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		Shaders.init();
		fonts = new Fonts();
		featureManager.initialize();
		screenManager.initialize();
		handlerManager.initialize();

		LOGGER.info("Initialized :3");
	}
}