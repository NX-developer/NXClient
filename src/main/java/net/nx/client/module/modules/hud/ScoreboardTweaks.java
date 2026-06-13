package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.module.settings.SliderSetting;
import net.nx.client.ui.NXColors;

import java.awt.Color;

public class ScoreboardTweaks extends HUDModule {

    public final BooleanSetting hideNumbers;
    public final BooleanSetting cleanBackground;
    public final BooleanSetting chromaTitle;
    public final ColorSetting accentColor;
    public final SliderSetting backgroundAlpha;

    public ScoreboardTweaks() {
        super("Scoreboard Tweaks", "Customizes the scoreboard appearance", 0, 0);
        this.hideNumbers      = addSetting(new BooleanSetting("Hide Numbers",   "Hide score numbers next to entries", true));
        this.cleanBackground  = addSetting(new BooleanSetting("Clean BG",       "Clean scoreboard background",        true));
        this.chromaTitle      = addSetting(new BooleanSetting("Chroma Title",   "RGB chroma on scoreboard title",     false));
        this.accentColor      = addSetting(new ColorSetting("Accent Color",     "Scoreboard accent color", new Color(NXColors.ACCENT_PRIMARY, true)));
        this.backgroundAlpha  = addSetting(new SliderSetting("BG Alpha",        "Background transparency", 100, 0, 255, 1));
        setEnabled(true);
    }

    @Override
    public void renderHUD(ScaledResolution res) {}

    @Override public int getWidth()  { return 0; }
    @Override public int getHeight() { return 0; }
}
