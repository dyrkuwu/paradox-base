package gay.paradox.render;

import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import org.ladysnake.satin.api.managed.uniform.Uniform1f;

public class Shaders {
    public static final ManagedCoreShader MSDF = ShaderEffectManager.getInstance().manageCoreShader(Identifier.of("paradox", "msdf"), VertexFormats.POSITION_TEXTURE_COLOR);
    public static Uniform1f msdfPxrange;

    public static void init() {
        msdfPxrange = MSDF.findUniform1f("uPxRange");
    }
}