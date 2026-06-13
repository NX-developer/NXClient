package net.nx.client.module.modules.hud;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.ui.NXColors;

import java.awt.Color;

public class Coordinates extends HUDModule {

    private final BooleanSetting showDirection;
    private final ColorSetting labelColor;
    private final ColorSetting valueColor;

    public Coordinates() {
        super("Coordinates", "Shows XYZ coordinates", 2, 60);
        this.showDirection = addSetting(new BooleanSetting("Direction", "Show facing direction", true));
        this.labelColor    = addSetting(new ColorSetting("Label Color", "Color of XYZ labels", new Color(NXColors.TEXT_SECONDARY, true)));
        this.valueColor    = addSetting(new ColorSetting("Value Color", "Color of values", new Color(NXColors.TEXT_PRIMARY, true)));
        setEnabled(true);
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        EntityPlayerSP p = mc.thePlayer;
        if (p == null) return;

        int lc = labelColor.getColor();
        int vc = valueColor.getColor();

        float x = (float) posX;
        float y = (float) posY;

        drawLine("X: ", String.format("%.1f", p.posX), x, y, lc, vc);
        drawLine("Y: ", String.format("%.1f", p.posY), x, y + 10, lc, vc);
        drawLine("Z: ", String.format("%.1f", p.posZ), x, y + 20, lc, vc);

        if (showDirection.isEnabled()) {
            drawLine("Dir: ", getDirection(p), x, y + 30, lc, vc);
        }
    }

    private void drawLine(String label, String value, float x, float y, int lc, int vc) {
        mc.fontRendererObj.drawStringWithShadow(label, x, y, lc);
        mc.fontRendererObj.drawStringWithShadow(value, x + mc.fontRendererObj.getStringWidth(label), y, vc);
    }

    private String getDirection(EntityPlayerSP p) {
        float yaw = ((p.rotationYaw % 360) + 360) % 360;
        if (yaw < 22.5 || yaw >= 337.5) return "South (+Z)";
        if (yaw < 67.5)  return "Southwest";
        if (yaw < 112.5) return "West (-X)";
        if (yaw < 157.5) return "Northwest";
        if (yaw < 202.5) return "North (-Z)";
        if (yaw < 247.5) return "Northeast";
        if (yaw < 292.5) return "East (+X)";
        return "Southeast";
    }

    @Override public int getWidth()  { return 100; }
    @Override public int getHeight() { return showDirection.isEnabled() ? 38 : 28; }
}
