package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.ui.NXColors;
import net.nx.client.util.RenderUtil;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class PotionHUD extends HUDModule {

    private final BooleanSetting showIcons;
    private final BooleanSetting showAmplifier;
    private final ColorSetting bgColor;

    public PotionHUD() {
        super("PotionHUD", "Shows active potion effects with remaining duration", 2, 260);
        this.showIcons     = addSetting(new BooleanSetting("Icons",      "Show potion icons",       false));
        this.showAmplifier = addSetting(new BooleanSetting("Amplifier",  "Show amplifier (II, III)", true));
        this.bgColor       = addSetting(new ColorSetting("Background", "Background color", new Color(0x0A, 0x0A, 0x14, 160)));
        setEnabled(true);
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        if (mc.thePlayer == null) return;

        Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
        if (effects.isEmpty()) return;

        List<PotionEffect> sorted = new ArrayList<>(effects);
        sorted.sort(Comparator.comparingInt(e -> -e.getDuration()));

        int lineH = 11;
        int pad   = 3;
        int w     = 110;
        int h     = sorted.size() * lineH + pad * 2;

        RenderUtil.drawRoundedRect(posX - pad, posY - pad, posX + w, posY + h - pad, 3, bgColor.getColor());

        for (int i = 0; i < sorted.size(); i++) {
            PotionEffect effect = sorted.get(i);
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            if (potion == null) continue;

            String name = potion.getName();
            if (name.startsWith("potion.")) name = name.substring(7);
            name = capitalize(name);

            if (showAmplifier.isEnabled() && effect.getAmplifier() > 0) {
                name += " " + toRoman(effect.getAmplifier() + 1);
            }

            String duration = formatDuration(effect.getDuration());
            int color = potion.isBadEffect() ? 0xFFFF6666 : 0xFF88DDFF;
            mc.fontRendererObj.drawStringWithShadow(name + " " + duration, (float) posX, (float) posY + i * lineH, color);
        }
    }

    private String formatDuration(int ticks) {
        int secs = ticks / 20;
        return String.format("%d:%02d", secs / 60, secs % 60);
    }

    private String capitalize(String s) {
        if (s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private String toRoman(int n) {
        switch (n) { case 2: return "II"; case 3: return "III"; case 4: return "IV"; case 5: return "V"; default: return String.valueOf(n); }
    }

    @Override public int getWidth()  { return 110; }
    @Override public int getHeight() { return 80; }
}
