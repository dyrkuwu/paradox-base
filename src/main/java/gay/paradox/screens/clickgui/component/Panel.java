package gay.paradox.screens.clickgui.component;

import gay.paradox.Paradox;
import gay.paradox.features.Category;
import gay.paradox.features.Feature;
import gay.paradox.screens.clickgui.component.impl.ModuleButton;
import gay.paradox.utils.Imports;
import gay.paradox.utils.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel extends Component implements Imports {
    private final Category category;
    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    private boolean dragging;
    private float dragX, dragY;
    private boolean expanded = true;

    public Panel(Category category, float x, float y, float width, float height, List<Feature> features) {
        super(x, y, width, height);
        this.category = category;

        float moduleY = y + height;
        for (Feature feature : features) {
            if (feature.getCategory() == category) {
                ModuleButton moduleButton = new ModuleButton(feature, x, moduleY, width, 18, this::updatePositions);
                moduleButtons.add(moduleButton);
                moduleY += 18;
            }
        }
    }

    private void updatePositions() {
        float moduleY = y + height;
        for (ModuleButton button : moduleButtons) {
            button.updatePosition(x, moduleY);
            moduleY += button.getHeight();
            if (button.isExpanded()) {
                moduleY += button.getSettingsHeight();
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        updatePositions();

        RenderUtil.drawRect(context.getMatrices(), x, y, x + width, y + height, new Color(173, 123, 255, 120));
        Paradox.fonts.getArial().renderWithShadow(context.getMatrices(), category.name, x + 5, y + height / 2 - 5, 9, -1);

        String indicator = expanded ? "-" : "+";
        Paradox.fonts.getArial().renderWithShadow(context.getMatrices(), indicator, x + width - 10, y + height / 2 - 5, 9, -1);

        if (expanded) {
            for (ModuleButton button : moduleButtons) {
                button.render(context, mouseX, mouseY, delta);
            }
        }
        
        float totalHeight = expanded ? getExpandedHeight() : height;
        RenderUtil.drawOutlineRect(context.getMatrices(), x, y, x + width, y + totalHeight, new Color(173, 123, 255, 200));
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int) mouseX, (int) mouseY)) {
            if (button == 0) {
                dragging = true;
                dragX = (float) mouseX - x;
                dragY = (float) mouseY - y;
            } else if (button == 1) {
                expanded = !expanded;
                mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }

        if (expanded) {
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            dragging = false;
        }

        if (expanded) {
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if (expanded) {
            for (ModuleButton moduleButton : moduleButtons) {
                moduleButton.keyPressed(keyCode, scanCode, modifiers);
            }
        }
    }

    public float getExpandedHeight() {
        if (!expanded) return height;

        float totalHeight = height;
        for (ModuleButton button : moduleButtons) {
            totalHeight += button.getHeight();
            if (button.isExpanded()) {
                totalHeight += button.getSettingsHeight();
            }
        }

        return totalHeight;
    }
}
