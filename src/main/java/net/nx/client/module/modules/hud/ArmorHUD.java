package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ModeSetting;

public class ArmorHUD extends HUDModule {

    private final BooleanSetting showDurability;
    private final BooleanSetting showHeldItem;
    private final ModeSetting orientation;

    public ArmorHUD() {
        super("ArmorHUD", "Shows armor and held item with durability", 2, 150);
        this.showDurability = addSetting(new BooleanSetting("Durability", "Show item durability", true));
        this.showHeldItem   = addSetting(new BooleanSetting("Held Item",  "Show held item",       true));
        this.orientation    = addSetting(new ModeSetting("Layout", "Display layout", "Vertical", "Vertical", "Horizontal"));
        setEnabled(true);
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        if (mc.thePlayer == null) return;

        ItemStack[] armor = mc.thePlayer.inventory.armorInventory;
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableDepth();

        int slot = 0;
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = armor[i];
            if (stack == null) { slot++; continue; }

            int ix = (int) posX;
            int iy = (int) posY + slot * 18;

            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, ix, iy);

            if (showDurability.isEnabled() && stack.isItemStackDamageable()) {
                int maxDmg  = stack.getMaxDamage();
                int curDmg  = maxDmg - stack.getItemDamage();
                float pct   = (float) curDmg / maxDmg;
                int color   = pct > 0.5f ? 0xFF00FF00 : pct > 0.25f ? 0xFFFFFF00 : 0xFFFF4444;
                mc.fontRendererObj.drawStringWithShadow(curDmg + "", ix + 18, iy + 4, color);
            }
            slot++;
        }

        if (showHeldItem.isEnabled()) {
            ItemStack held = mc.thePlayer.getHeldItem();
            if (held != null) {
                int ix = (int) posX;
                int iy = (int) posY + slot * 18;
                mc.getRenderItem().renderItemAndEffectIntoGUI(held, ix, iy);
                mc.fontRendererObj.drawStringWithShadow(held.getDisplayName(), ix + 18, iy + 4, 0xFFFFFFFF);
            }
        }

        GlStateManager.disableDepth();
        RenderHelper.disableStandardItemLighting();
    }

    @Override public int getWidth()  { return 70; }
    @Override public int getHeight() { return 18 * 5; }
}
