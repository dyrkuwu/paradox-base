package gay.paradox.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.paradox.Paradox;
import gay.paradox.event.impl.EventRender3D;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z", opcode = Opcodes.GETFIELD, ordinal = 0), method = "renderWorld")
    private void onWorldRender(RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        Camera camera = mc.gameRenderer.getCamera();
        MatrixStack matrixStack = new MatrixStack();
        RenderSystem.getModelViewStack().pushMatrix().mul(matrixStack.peek().getPositionMatrix());
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0f));

        Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();

        EventRender3D event = new EventRender3D(
                matrixStack,
                tickCounter.getTickDelta(false),
                camera,
                (GameRenderer)(Object)this,
                projectionMatrix,
                mc.worldRenderer
        );

        Paradox.EVENT_BUS.post(event);

        RenderSystem.getModelViewStack().popMatrix();
    }
}
