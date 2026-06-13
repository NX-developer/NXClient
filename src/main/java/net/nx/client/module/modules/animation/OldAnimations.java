package net.nx.client.module.modules.animation;

import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.SliderSetting;

public class OldAnimations extends Module {

    public final BooleanSetting oldSwing;
    public final BooleanSetting oldSword;
    public final BooleanSetting oldBlocking;
    public final BooleanSetting oldBow;
    public final BooleanSetting oldRod;
    public final BooleanSetting oldEating;
    public final SliderSetting  swingSpeed;

    public OldAnimations() {
        super("Old Animations", "Restores classic 1.7 item animations", Category.ANIMATION);
        this.oldSwing   = addSetting(new BooleanSetting("1.7 Swing",    "Classic item swing animation",   true));
        this.oldSword   = addSetting(new BooleanSetting("1.7 Sword",    "Old sword idle position",        true));
        this.oldBlocking= addSetting(new BooleanSetting("1.7 Blocking", "Old sword blocking animation",   true));
        this.oldBow     = addSetting(new BooleanSetting("1.7 Bow",      "Old bow draw animation",         true));
        this.oldRod     = addSetting(new BooleanSetting("1.7 Rod",      "Old fishing rod animation",      true));
        this.oldEating  = addSetting(new BooleanSetting("1.7 Eating",   "Old eating animation",           true));
        this.swingSpeed = addSetting(new SliderSetting("Swing Speed", "Item swing speed multiplier", 1.0, 0.2, 2.0, 0.1));
        setEnabled(true);
    }
}
