package gay.paradox.render.font;

import com.google.gson.JsonObject;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FontMetrics {
    private int emSize;
    private float lineHeight;
    private float ascender;
    private float descender;
    private float underlineY;
    private float underlineThickness;

    public static FontMetrics parse(final JsonObject object) {
        final FontMetrics metrics = new FontMetrics();

        metrics.emSize = object.get("emSize").getAsInt();
        metrics.lineHeight = object.get("lineHeight").getAsFloat();
        metrics.ascender = object.get("ascender").getAsFloat();
        metrics.descender = object.get("descender").getAsFloat();
        metrics.underlineY = object.get("underlineY").getAsFloat();
        metrics.underlineThickness = object.get("underlineThickness").getAsFloat();

        return metrics;
    }
}