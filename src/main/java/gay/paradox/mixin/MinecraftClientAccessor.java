package gay.paradox.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {
    @Accessor
    Mouse getMouse();

    @Invoker
    void invokeDoItemUse();

    @Invoker
    boolean invokeDoAttack();

    @Accessor(value="itemUseCooldown")
    void setItemUseCooldown(int var1);
}
