package net.nx.client.module.modules.render;

import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.SliderSetting;

public class ChromaUI extends Module {

    public final SliderSetting speed;
    public final SliderSetting saturation;

    public ChromaUI() {
        super("Chroma UI", "RGB chroma effect on all HUD elements globally", Category.RENDER);
        this.speed      = addSetting(new SliderSetting("Speed",      "Chroma cycle speed", 1.0, 0.1, 5.0, 0.1));
        this.saturation = addSetting(new SliderSetting("Saturation", "Color saturation",   0.9, 0.3, 1.0, 0.05));
        setEnabled(false);
    }

    public int getChromaColor(double xOffset) {
        float hue = (float)(((System.currentTimeMillis() / (1000.0 / speed.getValue())) + xOffset * 0.02) % 1.0);
        return java.awt.Color.getHSBColor(hue, saturation.getValue().floatValue(), 1f).getRGB() | 0xFF000000;
    }
}
