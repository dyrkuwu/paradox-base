package gay.paradox.features.impl.movement;

import gay.paradox.event.Subscribe;
import gay.paradox.event.impl.EventMove;
import gay.paradox.features.Category;
import gay.paradox.features.Feature;
import gay.paradox.features.FeatureInfo;

@FeatureInfo(name = "Sprint", description = "Sprints for u", category = Category.MOVEMENT)
public class Sprint extends Feature {
    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.setSprinting(false);
        }
        super.onDisable();
    }

    @Subscribe
    public void onMove(EventMove event) {
        if (mc.player == null) return;

        mc.options.sprintKey.setPressed(true);
    }
}
