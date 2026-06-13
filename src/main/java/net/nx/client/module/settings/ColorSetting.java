package net.nx.client.module.settings;

import java.awt.Color;

public class ColorSetting extends Setting<Color> {

    private boolean chromaEnabled;
    private float chromaSpeed;

    public ColorSetting(String name, String description, Color defaultValue) {
        super(name, description, defaultValue);
        this.chromaEnabled = false;
        this.chromaSpeed = 1.0f;
    }

    public int getColor() {
        if (chromaEnabled) {
            long time = System.currentTimeMillis();
            float hue = (time / (1000f / chromaSpeed) % 1f);
            Color chroma = Color.getHSBColor(hue, 1f, 1f);
            return new Color(chroma.getRed(), chroma.getGreen(), chroma.getBlue(), value.getAlpha()).getRGB();
        }
        return value.getRGB();
    }

    public int getColorWithAlpha(int alpha) {
        Color c = new Color(getColor(), true);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB();
    }

    public boolean isChromaEnabled() { return chromaEnabled; }
    public void setChromaEnabled(boolean chromaEnabled) { this.chromaEnabled = chromaEnabled; }
    public float getChromaSpeed() { return chromaSpeed; }
    public void setChromaSpeed(float chromaSpeed) { this.chromaSpeed = chromaSpeed; }
}
