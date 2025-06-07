package gay.paradox.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mouse.class)
public interface MouseHandlerAccessor {
    @Invoker(value="onMouseButton")
    void press(long var1, int var3, int var4, int var5);
}

