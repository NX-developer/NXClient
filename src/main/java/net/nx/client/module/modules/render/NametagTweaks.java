package net.nx.client.module.modules.render;

import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.SliderSetting;

public class NametagTweaks extends Module {

    public final SliderSetting  nametagScale;
    public final BooleanSetting hideBackground;
    public final BooleanSetting showOwnNametag;
    public final BooleanSetting showHealth;

    public NametagTweaks() {
        super("Nametag Tweaks", "Customizes player nametag rendering", Category.RENDER);
        this.nametagScale    = addSetting(new SliderSetting("Scale",        "Nametag text scale",         1.5, 0.5, 3.0, 0.1));
        this.hideBackground  = addSetting(new BooleanSetting("No BG",       "Hide nametag background",     false));
        this.showOwnNametag  = addSetting(new BooleanSetting("Own Nametag", "Show your own nametag",       false));
        this.showHealth      = addSetting(new BooleanSetting("Show Health",  "Show health under nametag",   true));
        setEnabled(false);
    }
}
