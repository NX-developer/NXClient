package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.ui.NXColors;

import java.awt.Color;

public class ReachDisplay extends HUDModule {

    private final ColorSetting color;
    private double lastReach = 0;
    private long lastHitTime = 0;
    private static final long DISPLAY_DURATION = 3000;

    public ReachDisplay() {
        super("Reach Display", "Shows your measured reach on last hit (display only, no reach modification)", 2, 35);
        this.color = addSetting(new ColorSetting("Color", "Text color", new Color(NXColors.ACCENT_PRIMARY, true)));
        setEnabled(false);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!(event.target instanceof EntityLivingBase)) return;
        if (mc.thePlayer == null) return;
        lastReach = mc.thePlayer.getDistanceToEntity(event.target);
        lastHitTime = System.currentTimeMillis();
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        long elapsed = System.currentTimeMillis() - lastHitTime;
        if (elapsed > DISPLAY_DURATION) return;

        String text = String.format("Reach: %.2fb", lastReach);
        mc.fontRendererObj.drawStringWithShadow(text, (float) posX, (float) posY, color.getColor());
    }

    @Override public int getWidth()  { return 80; }
    @Override public int getHeight() { return 9; }
}
