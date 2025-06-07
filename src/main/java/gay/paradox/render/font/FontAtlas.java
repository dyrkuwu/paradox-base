package gay.paradox.render.font;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.systems.RenderSystem;
import gay.paradox.render.Shaders;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FontAtlas {

    private final static String FORMATTING_PALETTE = "0123456789abcdefklmnor";
    private final static int[][] FORMATTING_COLOR_PALETTE = new int[32][3];

    private final int[] textColor = new int[3];
    private volatile float textX;

    private final int distanceRange;
    private final int width;
    private final int height;

    private float size = 9;

    private final Glyph[] glyphs = new Glyph[2048 * 2048];
    private final FontMetrics fontMetrics;

    private final NativeImageBackedTexture tex;

    public FontAtlas(final ResourceManager manager, final String name) throws IOException {
        this(
                new InputStreamReader(manager.open(Identifier.of("paradox", "fonts/" + name + ".json"))),
                manager.open(Identifier.of("paradox", "fonts/" + name + ".png"))
        );
    }

    public FontAtlas(final Reader meta, final InputStream texture) throws IOException {
        this.tex = new NativeImageBackedTexture(NativeImage.read(texture));

        final JsonObject atlasJson = JsonParser.parseReader(meta).getAsJsonObject();

        if ("msdf".equals(atlasJson.getAsJsonObject("atlas").get("width").getAsString())) {
            throw new RuntimeException("Unsupported atlas-type");
        }

        this.width = atlasJson.getAsJsonObject("atlas").get("width").getAsInt();
        this.height = atlasJson.getAsJsonObject("atlas").get("height").getAsInt();
        this.distanceRange = atlasJson.getAsJsonObject("atlas").get("distanceRange").getAsInt();
        this.fontMetrics = FontMetrics.parse(atlasJson.getAsJsonObject("metrics"));

        for (final JsonElement glyphElement : atlasJson.getAsJsonArray("glyphs")) {
            final JsonObject glyphObject = glyphElement.getAsJsonObject();
            final Glyph glyph = Glyph.parse(glyphObject);

            this.glyphs[glyph.getUnicode()] = glyph;
        }
    }

    public String truncate(final String text, final float width, final float size) {
        if (getWidth(text, size) <= width) return text;

        StringBuilder truncated = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            if (getWidth(truncated.toString(), size) < width ) {
                truncated.append(text.charAt(i));
            } else {
                truncated.append("...");
                break;
            }
        }
        return truncated.toString();
    }

    public void render(final MatrixStack matrixStack, final String text, final float x, final float y, final int color) {
        this.render(matrixStack, text, x, y, size, color);
    }

    public void renderRightString(final MatrixStack matrixStack, final String text, final float x, final float y, final float size, final int color) {
        this.render(matrixStack, text, x - getWidth(text), y, size, color);
    }

    public void renderRightString(final MatrixStack matrixStack, final String text, final float x, final float y, final int color) {
        this.render(matrixStack, text, x - getWidth(text), y, size, color);
    }

    public void renderCenteredString(final MatrixStack matrixStack, final String text, final float x, final float y, final int color) {
        this.render(matrixStack, text, x - getWidth(text) / 2f, y, size, color);
    }

    public void renderCenteredString(final MatrixStack matrixStack, final String text, final float x, final float y, final float size, final int color) {
        this.render(matrixStack, text, x - getWidth(text) / 2f, y, size, color);
    }

    public void render(final MatrixStack matrices, final OrderedText text, final float x, final float y, final float size, final int color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, tex.getGlId());
        RenderSystem.setShader(Shaders.MSDF::getProgram);
        Shaders.msdfPxrange.set(distanceRange);

        this.textX = x;

        final Matrix4f model = matrices.peek().getPositionMatrix();
        final int alpha = ColorHelper.Argb.getAlpha(color);
        final int red = ColorHelper.Argb.getRed(color);
        final int green = ColorHelper.Argb.getGreen(color);
        final int blue = ColorHelper.Argb.getBlue(color);

        this.textColor[0] = red;
        this.textColor[1] = green;
        this.textColor[2] = blue;

        final BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        text.accept((index, style, codePoint) -> {
            final Glyph glyph = this.glyphs[codePoint];

            if (glyph == null)
                return true;
            if (style.getColor() == null) {
                this.textColor[0] = red;
                this.textColor[1] = green;
                this.textColor[2] = blue;
            } else {
                final int rgb = style.getColor().getRgb();
                this.textColor[0] = ColorHelper.Argb.getRed(rgb);
                this.textColor[1] = ColorHelper.Argb.getGreen(rgb);
                this.textColor[2] = ColorHelper.Argb.getBlue(rgb);
            }
            this.textX += this.visit(model, bufferBuilder, glyph, textX, y, size, alpha);
            return true;
        });

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public void render(MatrixStack matrices, String text, float x, float y, float size, int color) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, tex.getGlId());
        RenderSystem.setShader(Shaders.MSDF::getProgram);
        Shaders.msdfPxrange.set(distanceRange);

        final Matrix4f model = matrices.peek().getPositionMatrix();
        int alpha = ColorHelper.Argb.getAlpha(color);
        int red = ColorHelper.Argb.getRed(color);
        int green = ColorHelper.Argb.getGreen(color);
        int blue = ColorHelper.Argb.getBlue(color);

        this.textColor[0] = red;
        this.textColor[1] = green;
        this.textColor[2] = blue;

        if (text.length() <= 0) {
            return;
        }

        final BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i);

            if (unicode == '§' && i + 1 < text.length()) {
                final int colorIndex = FORMATTING_PALETTE.indexOf(Character.toLowerCase(text.charAt(i + 1)));
                if (colorIndex >= 0 && colorIndex < 16) {
                    System.arraycopy(FORMATTING_COLOR_PALETTE[colorIndex], 0, textColor, 0, 3);
                } else if (colorIndex == 21) {
                    textColor[0] = red;
                    textColor[1] = green;
                    textColor[2] = blue;
                }
                i++;
            } else {
                final Glyph glyph = this.glyphs[unicode];

                if (glyph == null)
                    continue;
                if (glyph.getPlaneRight() - glyph.getPlaneLeft() != 0) {
                    float x0 = x + glyph.getPlaneLeft() * size;
                    float x1 = x + glyph.getPlaneRight() * size;
                    float y0 = y + fontMetrics.getAscender() * size - glyph.getPlaneTop() * size;
                    float y1 = y + fontMetrics.getAscender() * size - glyph.getPlaneBottom() * size;
                    float u0 = glyph.getAtlasLeft() / width;
                    float u1 = glyph.getAtlasRight() / width;
                    float v0 = glyph.getAtlasTop() / height;
                    float v1 = glyph.getAtlasBottom() / height;

                    bufferBuilder.vertex(model, x0, y0, 0).texture(u0, 1 - v0).color(textColor[0], textColor[1], textColor[2], alpha);
                    bufferBuilder.vertex(model, x0, y1, 0).texture(u0, 1 - v1).color(textColor[0], textColor[1], textColor[2], alpha);
                    bufferBuilder.vertex(model, x1, y1, 0).texture(u1, 1 - v1).color(textColor[0], textColor[1], textColor[2], alpha);
                    bufferBuilder.vertex(model, x1, y0, 0).texture(u1, 1 - v0).color(textColor[0], textColor[1], textColor[2], alpha);
                }
                x += size * glyph.getAdvance();
            }
        }
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public void renderWithShadow(final MatrixStack matrices, final String text, final float x, final float y, final float size, final int color) {
        RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
        this.render(matrices, text, x + 0.75F, y + 0.75F,size, color);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        this.render(matrices, text, x, y,size, color);
    }

    private float visit(final Matrix4f model, final BufferBuilder bufferBuilder, final Glyph glyph, final float x, final float y, final float size, final int alpha) {
        if (glyph.getPlaneRight() - glyph.getPlaneLeft() != 0) {
            float x0 = x + glyph.getPlaneLeft() * size;
            float x1 = x + glyph.getPlaneRight() * size;
            float y0 = y + fontMetrics.getAscender() * size - glyph.getPlaneTop() * size;
            float y1 = y + fontMetrics.getAscender() * size - glyph.getPlaneBottom() * size;
            float u0 = glyph.getAtlasLeft() / width;
            float u1 = glyph.getAtlasRight() / width;
            float v0 = glyph.getAtlasTop() / height;
            float v1 = glyph.getAtlasBottom() / height;

            bufferBuilder.vertex(model, x0, y0, 0).texture(u0, 1 - v0).color(textColor[0], textColor[1], textColor[2], alpha);
            bufferBuilder.vertex(model, x0, y1, 0).texture(u0, 1 - v1).color(textColor[0], textColor[1], textColor[2], alpha);
            bufferBuilder.vertex(model, x1, y1, 0).texture(u1, 1 - v1).color(textColor[0], textColor[1], textColor[2], alpha);
            bufferBuilder.vertex(model, x1, y0, 0).texture(u1, 1 - v0).color(textColor[0], textColor[1], textColor[2], alpha);
        }
        return size * glyph.getAdvance();
    }

    public final float getSize() {
        return this.size;
    }

    public final float getWidth(final Text text) {
        return this.getWidth(text, size);
    }

    public final float getWidth(final Text text, final float size) {
        return this.getWidth(text.asOrderedText(), size);
    }

    public final float getWidth(final OrderedText text) {
        return this.getWidth(text, size);
    }

    public final float getWidth(final OrderedText text, final float size) {
        final float[] sum = new float[1];

        text.accept((index, style, codePoint) -> {
            final Glyph glyph = this.glyphs[codePoint];

            if (glyph == null)
                return true;
            if (glyph.getPlaneRight() - glyph.getPlaneLeft() != 0) {
                sum[0] += size * glyph.getAdvance();
            }
            return true;
        });
        return sum[0];
    }

    public final float getWidth(final String text) {
        return this.getWidth(text, size);
    }

    public float getWidth(String text, float size) {
        float sum = 0;
        for (int i = 0; i < text.length(); i++) {
            final int unicode = text.codePointAt(i);

            if (unicode == '§' && i + 1 < text.length()) {
                i++;
            } else {
                final Glyph glyph = glyphs[unicode];

                if (glyph == null)
                    continue;
                if (glyph.getPlaneRight() - glyph.getPlaneLeft() != 0) {
                    sum += size * glyph.getAdvance();
                }
            }
        }
        return sum;
    }

    public final float getLineHeight() {
        return this.getLineHeight(size);
    }

    public final float getLineHeight(final float size) {
        return this.fontMetrics.getLineHeight() * size;
    }

    static {
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }


            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            FORMATTING_COLOR_PALETTE[i][0] = k;
            FORMATTING_COLOR_PALETTE[i][1] = l;
            FORMATTING_COLOR_PALETTE[i][2] = i1;
        }
    }
}