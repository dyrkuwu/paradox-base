package gay.paradox.features;

import gay.paradox.features.impl.client.ClickGUI;
import gay.paradox.features.impl.combat.AutoJumpReset;
import gay.paradox.features.impl.movement.Sprint;
import gay.paradox.features.impl.render.HUD;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class FeatureManager {
    private final ArrayList<Feature> features = new ArrayList<>();

    public void initialize() {
        features.add(new Sprint());
        features.add(new HUD());
        features.add(new ClickGUI());
        features.add(new AutoJumpReset());

        features.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    }

    public <T extends Feature> T getFeatureByClass(Class<T> featureClass) {
        return featureClass.cast(features.stream()
                .filter(feature -> feature.getClass().equals(featureClass))
                .findFirst()
                .orElse(null));
    }
}
