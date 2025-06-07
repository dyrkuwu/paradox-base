package gay.paradox.screens.clickgui.component;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawContext;

public abstract class Component {
    @Getter
    @Setter
    protected float x, y, width, height;

    public Component(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);
    
    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
    
    public void mouseClicked(double mouseX, double mouseY, int button) {}
    
    public void mouseReleased(double mouseX, double mouseY, int button) {}
    
    public void keyPressed(int keyCode, int scanCode, int modifiers) {}
} 