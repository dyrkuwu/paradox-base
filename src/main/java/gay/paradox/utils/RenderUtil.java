package gay.paradox.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtil implements Imports {
    public static void drawLine(MatrixStack matrices, float x, float y, float x1, float y1, float width, Color color) {
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y, 0f).color(r, g, b, a);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x1, y1, 0f).color(r, g, b, a);

        RenderSystem.lineWidth(width);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
    }

    public static Pair<Vec3d, Boolean> project(Matrix4f modelView, Matrix4f projection, Vec3d vector) {
        Vec3d camPos = vector.subtract(mc.gameRenderer.getCamera().getPos());
        Vector4f vec = new Vector4f((float) camPos.x, (float) camPos.y, (float) camPos.z, 1F);

        vec.mul(modelView);
        vec.mul(projection);

        boolean isVisible = vec.w() > 0.0;

        if (vec.w() != 0) {
            vec.x /= vec.w();
            vec.y /= vec.w();
            vec.z /= vec.w();
        }

        double screenX = (vec.x() * 0.5 + 0.5) * mc.getWindow().getScaledWidth();
        double screenY = (0.5 - vec.y() * 0.5) * mc.getWindow().getScaledHeight();

        Vec3d position = new Vec3d(screenX, screenY, vec.z());

        return new Pair<>(position, isVisible);
    }

    public static void drawOutlineRect(MatrixStack matrices, float x, float y, float x1, float y1, Color color) {
        drawLine(matrices, x, y, x1, y, 1.0f, color);
        drawLine(matrices, x1, y, x1, y1, 1.0f, color);
        drawLine(matrices, x, y1, x1, y1, 1.0f, color);
        drawLine(matrices, x, y, x, y1, 1.0f, color);
    }

    public static void drawRect(MatrixStack matrices, float x, float y, float x1, float y1, Color color) {
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y1, 0f).color(r, g, b, a);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x1, y1, 0f).color(r, g, b, a);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x1, y, 0f).color(r, g, b, a);
        bufferBuilder.vertex(matrices.peek().getPositionMatrix(), x, y, 0f).color(r, g, b, a);

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
    }
} 