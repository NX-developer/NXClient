package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.ui.NXColors;

import java.awt.Color;

public class ComboCounter extends HUDModule {

    private int combo = 0;
    private long lastHitTime = 0;
    private static final long COMBO_TIMEOUT = 5000;
    private final ColorSetting color;

    public ComboCounter() {
        super("Combo Counter", "Shows your current hit combo", 2, 46);
        this.color = addSetting(new ColorSetting("Color", "Text color", new Color(NXColors.ACCENT_PRIMARY, true)));
        setEnabled(false);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); combo = 0; }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!(event.target instanceof EntityLivingBase)) return;
        long now = System.currentTimeMillis();
        if (now - lastHitTime > COMBO_TIMEOUT) combo = 0;
        combo++;
        lastHitTime = now;
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        if (mc.thePlayer == null) return;
        if (event.entityLiving == mc.thePlayer) { combo = 0; }
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        if (combo <= 0) return;
        if (System.currentTimeMillis() - lastHitTime > COMBO_TIMEOUT) { combo = 0; return; }
        String text = combo + " Combo";
        mc.fontRendererObj.drawStringWithShadow(text, (float) posX, (float) posY, color.getColor());
    }

    @Override public int getWidth()  { return 70; }
    @Override public int getHeight() { return 9; }
}
