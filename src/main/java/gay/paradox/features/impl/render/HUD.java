package gay.paradox.features.impl.render;

import gay.paradox.Paradox;
import gay.paradox.event.Subscribe;
import gay.paradox.event.impl.EventRender2D;
import gay.paradox.features.Category;
import gay.paradox.features.Feature;
import gay.paradox.features.FeatureInfo;
import gay.paradox.features.settings.impl.BoolSetting;
import gay.paradox.features.settings.impl.IntSetting;
import gay.paradox.utils.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

@FeatureInfo(name = "HUD", description = "Stuffs on ur screen", category = Category.RENDER)
public class HUD extends Feature {
    private final BoolSetting watermark = new BoolSetting("Watermark", true);
    private final IntSetting watermarkX = new IntSetting("Watermark X", 5, 0, 500, o -> watermark.getValue());
    private final IntSetting watermarkY = new IntSetting("Watermark Y", 5, 0, 500, o -> watermark.getValue());
    private final BoolSetting arrayList = new BoolSetting("ArrayList", true);
    private final IntSetting arrayListOffset = new IntSetting("List Offset", 5, 0, 500, o -> arrayList.getValue());

    @Subscribe
    public void onRender2D(EventRender2D event) {
        if (mc.getDebugHud().shouldShowDebugHud()) return;

        renderWatermark(event);
        renderArrayList(event);
    }

    private void renderWatermark(EventRender2D event) {
        if (!watermark.getValue()) return;

        Paradox.fonts.getArial().renderWithShadow(
            event.getMatrixStack(),
            "P",
            watermarkX.getValue(),
            watermarkY.getValue(),
            9,
            new Color(173, 123, 255).getRGB()
        );

        Paradox.fonts.getArial().renderWithShadow(
            event.getMatrixStack(),
            "aradox",
            watermarkX.getValue() + mc.textRenderer.getWidth("P"),
            watermarkY.getValue(),
            9,
            Color.white.getRGB()
        );
    }

    private void renderArrayList(EventRender2D event) {
        if (!arrayList.getValue()) return;

        int y = arrayListOffset.getValue();
        ArrayList<Feature> enabledFeatures = getEnabledFeatures();

        if (enabledFeatures.isEmpty()) return;

        for (Feature feature : enabledFeatures) {
            renderFeature(event, feature, y);
            y += mc.textRenderer.fontHeight + 4;
        }
    }

    private ArrayList<Feature> getEnabledFeatures() {
        ArrayList<Feature> featuresSorted = new ArrayList<>(Paradox.featureManager.getFeatures());
        featuresSorted.sort(Comparator.comparingDouble((Feature feature) ->
            Paradox.fonts.getArial().getWidth(feature.getName()) +
            Paradox.fonts.getArial().getWidth(feature.getInfo())
        ).reversed());

        ArrayList<Feature> enabledFeatures = new ArrayList<>();
        for (Feature feature : featuresSorted) {
            if (feature.isEnabled()) {
                enabledFeatures.add(feature);
            }
        }
        return enabledFeatures;
    }

    private void renderFeature(EventRender2D event, Feature feature, int y) {
        String featureName = feature.getName();
        float textWidth = Paradox.fonts.getArial().getWidth(featureName);
        float textX = event.getScaledWidth() - textWidth - arrayListOffset.getValue() - 2 - 
            (feature.getInfo().isEmpty() ? 0 : Paradox.fonts.getArial().getWidth(feature.getInfo()) + 2);
        float bgStartX = textX - 3;

        renderBackground(event, bgStartX, y);
        renderFeatureName(event, featureName, textX, y);
        renderFeatureInfo(event, feature, y);
    }

    private void renderBackground(EventRender2D event, float bgStartX, int y) {
        RenderUtil.drawRect(
            event.getMatrixStack(),
            bgStartX,
            y - 2,
            event.getScaledWidth() - arrayListOffset.getValue() + 1,
            y + mc.textRenderer.fontHeight + 2,
            new Color(35, 35, 35, 200)
        );

        RenderUtil.drawRect(
            event.getMatrixStack(),
            event.getScaledWidth() - arrayListOffset.getValue(),
            y - 2,
            event.getScaledWidth() - arrayListOffset.getValue() + 1,
            y + mc.textRenderer.fontHeight + 2,
            new Color(173, 123, 255)
        );
    }

    private void renderFeatureName(EventRender2D event, String featureName, float textX, int y) {
        Paradox.fonts.getArial().render(
            event.getMatrixStack(),
            featureName,
            textX,
            y - 1,
            new Color(213, 163, 255).getRGB()
        );
    }

    private void renderFeatureInfo(EventRender2D event, Feature feature, int y) {
        if (feature.getInfo().isEmpty()) return;

        Paradox.fonts.getArial().render(
            event.getMatrixStack(),
            feature.getInfo(),
            event.getScaledWidth() - arrayListOffset.getValue() - 2 - Paradox.fonts.getArial().getWidth(feature.getInfo()),
            y - 1,
            Color.lightGray.getRGB()
        );
    }
}
