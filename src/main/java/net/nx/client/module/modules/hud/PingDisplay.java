package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.ui.NXColors;

import java.awt.Color;

public class PingDisplay extends HUDModule {

    private final ColorSetting color;

    public PingDisplay() {
        super("Ping Display", "Shows your current server ping", 2, 24);
        this.color = addSetting(new ColorSetting("Color", "Text color", new Color(NXColors.ACCENT_PRIMARY, true)));
        setEnabled(true);
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        int ping = getPing();
        String pingColor = ping < 80 ? "§a" : ping < 150 ? "§e" : "§c";
        String text = pingColor + ping + " §7ms";
        mc.fontRendererObj.drawStringWithShadow(text, (float) posX, (float) posY, 0xFFFFFFFF);
    }

    private int getPing() {
        if (mc.thePlayer == null || mc.getNetHandler() == null) return 0;
        NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
        return info != null ? info.getResponseTime() : 0;
    }

    @Override public int getWidth()  { return 60; }
    @Override public int getHeight() { return 9; }
}
