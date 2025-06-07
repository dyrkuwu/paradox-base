package gay.paradox.features.impl.combat;

import gay.paradox.features.Category;
import gay.paradox.features.FeatureInfo;
import gay.paradox.features.settings.impl.IntSetting;
import gay.paradox.utils.MathUtil;
import gay.paradox.event.Subscribe;


import gay.paradox.features.Feature;

@FeatureInfo(name = "JumpReset", description = "jump on hit", category = Category.COMBAT)
public class AutoJumpReset extends Feature {
    private final IntSetting chance = new IntSetting("Chance", 100, 0, 100, "%");

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.setSprinting(false);
        }
        super.onDisable();
    }

    @Subscribe
    public void onTick() {
        if (mc.currentScreen != null) return;
        if (mc.player.isUsingItem()) return;
        if (!mc.player.isOnGround()) return;
        if (mc.player.hurtTime != 9) return;
        
        if (MathUtil.randomInt(1, 100) <= chance.getValue()) {
            mc.player.jump();
        }
    }
}
