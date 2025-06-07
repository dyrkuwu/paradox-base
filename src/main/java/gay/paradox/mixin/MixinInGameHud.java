package gay.paradox.mixin;

import gay.paradox.Paradox;
import gay.paradox.event.impl.EventRender2D;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        EventRender2D event = new EventRender2D(context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getMatrices(), tickCounter.getTickDelta(true), context);
        Paradox.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
