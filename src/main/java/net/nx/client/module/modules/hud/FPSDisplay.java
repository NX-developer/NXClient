package net.nx.client.module.modules.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.module.settings.ModeSetting;
import net.nx.client.ui.NXColors;

import java.awt.Color;

public class FPSDisplay extends HUDModule {

    private final ModeSetting style;
    private final ColorSetting color;

    public FPSDisplay() {
        super("FPS Display", "Shows current FPS", 2, 2);
        this.style  = addSetting(new ModeSetting("Style", "Display style", "Simple", "Simple", "Detailed", "Graph"));
        this.color  = addSetting(new ColorSetting("Color", "Text color", new Color(NXColors.ACCENT_PRIMARY, true)));
        setEnabled(true);
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        int fps = Minecraft.getDebugFPS();
        String text;
        if (style.is("Simple")) {
            text = fps + " FPS";
        } else {
            String quality = fps >= 120 ? "§a" : fps >= 60 ? "§e" : "§c";
            text = quality + fps + " §7FPS";
        }
        mc.fontRendererObj.drawStringWithShadow(text, (float) posX, (float) posY, color.getColor());
    }

    @Override public int getWidth()  { return mc.fontRendererObj.getStringWidth("120 FPS"); }
    @Override public int getHeight() { return 9; }
}
