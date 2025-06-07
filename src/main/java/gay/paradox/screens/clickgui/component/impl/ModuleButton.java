package gay.paradox.screens.clickgui.component.impl;

import gay.paradox.Paradox;
import gay.paradox.features.Feature;
import gay.paradox.features.settings.Setting;
import gay.paradox.screens.clickgui.component.Component;
import gay.paradox.screens.clickgui.component.impl.settings.*;
import gay.paradox.utils.Imports;
import gay.paradox.utils.RenderUtil;
import lombok.Getter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import gay.paradox.features.settings.impl.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleButton extends Component implements Imports {
    private final Feature feature;
    private final Map<Setting, Component> settingComponentMap = new HashMap<>();
    @Getter
    private boolean expanded;
    private final Runnable onStateChange;

    public ModuleButton(Feature feature, float x, float y, float width, float height, Runnable onStateChange) {
        super(x, y, width, height);
        this.feature = feature;
        this.onStateChange = onStateChange;

        initSettingComponents(x, y + height);
    }

    private void initSettingComponents(float x, float startY) {
        float settingY = startY;
        for (Setting setting : feature.getSettings()) {
            Component component = null;
            if (setting instanceof BoolSetting) {
                component = new BooleanComponent((BoolSetting) setting, x, settingY, width, 16);
            } else if (setting instanceof IntSetting) {
                component = new SliderComponent((IntSetting) setting, x, settingY, width, 16);
            } else if (setting instanceof FloatSetting) {
                component = new FloatSliderComponent((FloatSetting) setting, x, settingY, width, 16);
            } else if (setting instanceof ModeSetting) {
                component = new ModeComponent((ModeSetting) setting, x, settingY, width, 16);
            } else if (setting instanceof RangeSetting) {
                component = new RangeComponent((RangeSetting) setting, x, settingY, width, 16);
            }

            if (component != null) {
                settingComponentMap.put(setting, component);
                settingY += 16;
            }
        }
    }

    private List<Component> getVisibleSettingComponents() {
        List<Component> visibleComponents = new ArrayList<>();
        float settingY = y + height;

        for (Setting setting : feature.getSettings()) {
            if (!setting.isVisible()) continue;

            Component component = settingComponentMap.get(setting);
            if (component != null) {
                component.updatePosition(x, settingY);
                visibleComponents.add(component);
                settingY += component.getHeight();
            }
        }

        return visibleComponents;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Color buttonColor = new Color(35, 35, 35, 200);

        if (isHovered(mouseX, mouseY)) {
            buttonColor = new Color(buttonColor.getRed() + 15, buttonColor.getGreen() + 15, buttonColor.getBlue(), buttonColor.getAlpha());
        }

        RenderUtil.drawRect(context.getMatrices(), x, y, x + width, y + height, buttonColor);
        Paradox.fonts.getArial().renderWithShadow(context.getMatrices(), feature.getName(), x + 5, y + height / 2 - 5, 9, feature.isEnabled() ? new Color(173, 123, 255).getRGB() : new Color(255, 255, 255).getRGB());

        if (feature.hasSettings()) {
            String indicator = expanded ? "-" : "+";
            Paradox.fonts.getArial().renderWithShadow(context.getMatrices(), indicator, x + width - 10, y + height / 2 - 5, 9, -1);
        }

        if (expanded) {
            List<Component> visibleComponents = getVisibleSettingComponents();
            for (Component component : visibleComponents) {
                component.render(context, mouseX, mouseY, delta);
            }
        }
    }

    @Override
    public void updatePosition(float x, float y) {
        super.updatePosition(x, y);

        if (expanded) {
            List<Component> visibleComponents = getVisibleSettingComponents();
            float settingY = y + height;
            for (Component component : visibleComponents) {
                component.updatePosition(x, settingY);
                settingY += component.getHeight();
            }
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int) mouseX, (int) mouseY)) {
            if (button == 0) {
                feature.toggle();
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            } else if (button == 1 && feature.hasSettings()) {
                expanded = !expanded;
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                if (onStateChange != null) {
                    onStateChange.run();
                }
            }
        }

        if (expanded) {
            List<Component> visibleComponents = getVisibleSettingComponents();
            for (Component component : visibleComponents) {
                component.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (expanded) {
            List<Component> visibleComponents = getVisibleSettingComponents();
            for (Component component : visibleComponents) {
                component.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (expanded) {
            List<Component> visibleComponents = getVisibleSettingComponents();
            for (Component component : visibleComponents) {
                component.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    public float getSettingsHeight() {
        float height = 0;
        List<Component> visibleComponents = getVisibleSettingComponents();
        for (Component component : visibleComponents) {
            height += component.getHeight();
        }
        return height;
    }
}
