package net.nx.client.module.modules.render;

import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.SliderSetting;

public class Fullbright extends Module {

    private final SliderSetting gamma;
    private float savedGamma = -1f;

    public Fullbright() {
        super("Fullbright", "Increases gamma for full visibility in darkness", Category.RENDER);
        this.gamma = addSetting(new SliderSetting("Gamma", "Brightness level", 10.0, 1.0, 15.0, 0.5));
        setEnabled(false);
    }

    @Override
    public void onEnable() {
        savedGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        if (savedGamma >= 0) {
            mc.gameSettings.gammaSetting = savedGamma;
            savedGamma = -1f;
        }
    }

    @Override
    public void onUpdate() {
        mc.gameSettings.gammaSetting = (float) gamma.getValue();
    }
}
