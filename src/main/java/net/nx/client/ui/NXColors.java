package net.nx.client.ui;

import java.awt.Color;

public class NXColors {

    public static int BACKGROUND_DARK;
    public static int BACKGROUND_MID;
    public static int BACKGROUND_PANEL;
    public static int ACCENT_PRIMARY;
    public static int ACCENT_HOVER;
    public static int ACCENT_GLOW;
    public static int TEXT_PRIMARY;
    public static int TEXT_SECONDARY;
    public static int TEXT_DISABLED;
    public static int TOGGLE_ON;
    public static int TOGGLE_OFF;
    public static int SLIDER_TRACK;
    public static int SLIDER_FILL;
    public static int BORDER;
    public static int SCROLLBAR;

    public static void init() {
        BACKGROUND_DARK   = rgba(0x0A, 0x0A, 0x0F, 230);
        BACKGROUND_MID    = rgba(0x10, 0x10, 0x18, 220);
        BACKGROUND_PANEL  = rgba(0x12, 0x12, 0x1A, 210);
        ACCENT_PRIMARY    = rgba(0x2D, 0x7F, 0xF9, 255);
        ACCENT_HOVER      = rgba(0x1E, 0x90, 0xFF, 255);
        ACCENT_GLOW       = rgba(0x2D, 0x7F, 0xF9, 80);
        TEXT_PRIMARY      = rgba(0xFF, 0xFF, 0xFF, 255);
        TEXT_SECONDARY    = rgba(0xAA, 0xBB, 0xCC, 255);
        TEXT_DISABLED     = rgba(0x55, 0x66, 0x77, 255);
        TOGGLE_ON         = rgba(0x2D, 0x7F, 0xF9, 255);
        TOGGLE_OFF        = rgba(0x33, 0x33, 0x44, 255);
        SLIDER_TRACK      = rgba(0x22, 0x22, 0x30, 255);
        SLIDER_FILL       = rgba(0x2D, 0x7F, 0xF9, 255);
        BORDER            = rgba(0x20, 0x30, 0x50, 180);
        SCROLLBAR         = rgba(0x2D, 0x7F, 0xF9, 120);
    }

    private static int rgba(int r, int g, int b, int a) {
        return new Color(r, g, b, a).getRGB();
    }

    public static int withAlpha(int color, int alpha) {
        Color c = new Color(color, true);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB();
    }

    public static int chromaColor(float speed) {
        long time = System.currentTimeMillis();
        float hue = (time / (1000f / Math.max(0.1f, speed))) % 1f;
        return Color.getHSBColor(hue, 0.9f, 1f).getRGB();
    }

    public static int lerp(int colorA, int colorB, float t) {
        Color a = new Color(colorA, true);
        Color b = new Color(colorB, true);
        int r = (int) (a.getRed()   + (b.getRed()   - a.getRed())   * t);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t);
        int al = (int)(a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t);
        return new Color(r, g, bl, al).getRGB();
    }
}
