package gay.paradox.screens.clickgui.component.impl.settings;

import gay.paradox.Paradox;
import gay.paradox.features.settings.impl.ModeSetting;
import gay.paradox.screens.clickgui.component.Component;
import gay.paradox.utils.Imports;
import gay.paradox.utils.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.awt.*;

public class ModeComponent extends Component implements Imports {
    private final ModeSetting setting;
    
    public ModeComponent(ModeSetting setting, float x, float y, float width, float height) {
        super(x, y, width, height);
        this.setting = setting;
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Color bgColor = isHovered(mouseX, mouseY) 
                ? new Color(45, 45, 45, 200) 
                : new Color(35, 35, 35, 200);
        RenderUtil.drawRect(context.getMatrices(), x, y, x + width, y + height, bgColor);
        
        String displayValue = setting.getName() + ": " + setting.getValue();
        Paradox.fonts.getArial().renderWithShadow(context.getMatrices(), displayValue, x + 5, y + height / 2 - 5, 9, -1);
    }
    
    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int) mouseX, (int) mouseY) && button == 0) {
            setting.cycle();
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
} 