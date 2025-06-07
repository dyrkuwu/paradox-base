package gay.paradox.screens.clickgui.component.impl.settings;

import gay.paradox.Paradox;
import gay.paradox.features.settings.impl.FloatSetting;
import gay.paradox.screens.clickgui.component.Component;
import gay.paradox.utils.Imports;
import gay.paradox.utils.RenderUtil;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FloatSliderComponent extends Component implements Imports {
    private final FloatSetting setting;
    private boolean dragging;
    
    public FloatSliderComponent(FloatSetting setting, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.setting = setting;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (dragging) {
            float percent = (mouseX - x) / width;
            percent = Math.min(1, Math.max(0, percent));
            float value = setting.getMinValue() + (setting.getMaxValue() - setting.getMinValue()) * percent;
            setting.setValue(value);
        }
        
        Color bgColor = isHovered(mouseX, mouseY) 
                ? new Color(45, 45, 45, 200) 
                : new Color(35, 35, 35, 200);
        RenderUtil.drawRect(context.getMatrices(), x, y, x + width, y + height, bgColor);
        
        float sliderWidth = ((setting.getValue() - setting.getMinValue()) / (setting.getMaxValue() - setting.getMinValue())) * width;
        RenderUtil.drawRect(context.getMatrices(), x, y, x + sliderWidth, y + height, new Color(173, 123, 255, 200));
        
        BigDecimal bd = BigDecimal.valueOf(setting.getValue()).setScale(2, RoundingMode.HALF_UP);
        String displayValue = setting.getName() + ": " + bd.floatValue() + setting.getSuffix();
        Paradox.fonts.getArial().renderWithShadow(context.getMatrices(), displayValue, x + 5, y + height / 2 - 5, 9, -1);
    }
    
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int) mouseX, (int) mouseY) && button == 0) {
            dragging = true;
            float percent = (float) ((mouseX - x) / width);
            percent = Math.min(1, Math.max(0, percent));
            float value = setting.getMinValue() + (setting.getMaxValue() - setting.getMinValue()) * percent;
            setting.setValue(value);
        }
    }
    
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
    }
} 