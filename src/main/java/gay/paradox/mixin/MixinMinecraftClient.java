package gay.paradox.mixin;

import gay.paradox.Paradox;
import gay.paradox.event.impl.EventTick;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        EventTick event = new EventTick();
        Paradox.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}
