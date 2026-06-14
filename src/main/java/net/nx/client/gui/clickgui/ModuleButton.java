package net.nx.client.gui.clickgui;

import net.minecraft.client.Minecraft;
import net.nx.client.module.Module;
import net.nx.client.module.settings.*;
import net.nx.client.ui.NXColors;
import net.nx.client.util.RenderUtil;
import org.lwjgl.input.Keyboard;

import java.awt.Color;

public class ModuleButton {

    static final int H         = 14;
    static final int SETTING_H = 14;
    private static final double RADIUS = 3;

    private final Module module;
    private final int width;
    private boolean expanded = false;
    private float hoverAnim = 0f;

    private SliderSetting draggingSlider = null;

    private final Minecraft mc = Minecraft.getMinecraft();

    public ModuleButton(Module module, int width) {
        this.module = module;
        this.width  = width;
    }

    public void draw(int mx, int my, int x, int y) {
        boolean hovered = mx >= x && mx <= x + width && my >= y && my < y + H;
        hoverAnim += (hovered ? 0.15f : -0.15f);
        hoverAnim = Math.max(0f, Math.min(1f, hoverAnim));

        int bg = module.isEnabled()
            ? NXColors.lerp(NXColors.BACKGROUND_MID, NXColors.withAlpha(NXColors.ACCENT_PRIMARY, 180), 0.25f)
            : NXColors.lerp(NXColors.BACKGROUND_MID, NXColors.BACKGROUND_PANEL, hoverAnim);
        RenderUtil.drawRoundedRect(x, y, x + width, y + H - 1, RADIUS, bg);

        if (module.isEnabled()) {
            RenderUtil.drawRect(x, y, x + 2, y + H - 1, NXColors.ACCENT_PRIMARY);
        }

        int textColor = module.isEnabled() ? 0xFFFFFFFF
            : NXColors.lerp(NXColors.TEXT_SECONDARY, 0xFFFFFFFF, hoverAnim);
        mc.fontRendererObj.drawStringWithShadow(module.getName(), x + 5, y + 3, textColor);

        drawToggle(x + width - 20, y + 3, module.isEnabled());

        if (expanded && !module.getSettings().isEmpty()) {
            drawSettings(mx, my, x, y + H);
        }
    }

    private void drawToggle(int x, int y, boolean on) {
        RenderUtil.drawRoundedRect(x, y, x + 18, y + 8, 4,
            on ? NXColors.TOGGLE_ON : NXColors.TOGGLE_OFF);
        int kx = on ? x + 10 : x + 2;
        RenderUtil.drawRoundedRect(kx, y + 1, kx + 6, y + 7, 3, 0xFFFFFFFF);
    }

    private void drawSettings(int mx, int my, int x, int baseY) {
        int iy = baseY;
        for (Setting<?> s : module.getSettings()) {
            RenderUtil.drawRect(x, iy, x + width, iy + SETTING_H,
                NXColors.withAlpha(NXColors.BACKGROUND_DARK, 200));

            if (s instanceof BooleanSetting)  drawBoolSetting((BooleanSetting) s, x, iy);
            else if (s instanceof SliderSetting)  drawSliderSetting((SliderSetting) s, mx, x, iy);
            else if (s instanceof ModeSetting)    drawModeSetting((ModeSetting) s, x, iy);
            else if (s instanceof ColorSetting)   drawColorSetting((ColorSetting) s, x, iy);
            else if (s instanceof KeybindSetting) drawKeybindSetting((KeybindSetting) s, x, iy);

            iy += SETTING_H;
        }
    }

    private void drawBoolSetting(BooleanSetting s, int x, int y) {
        mc.fontRendererObj.drawStringWithShadow(s.getName(), x + 5, y + 3, NXColors.TEXT_SECONDARY);
        drawToggle(x + width - 20, y + 3, s.isEnabled());
    }

    private void drawSliderSetting(SliderSetting s, int mx, int x, int y) {
        String label = s.getName() + ": §b" + String.format("%.2g", s.getValue());
        mc.fontRendererObj.drawStringWithShadow(label, x + 5, y + 1, NXColors.TEXT_SECONDARY);
        int tx = x + 4, tw = width - 8, ty = y + 9, th = 3;
        RenderUtil.drawRoundedRect(tx, ty, tx + tw, ty + th, 2, NXColors.SLIDER_TRACK);
        int fw = (int)(tw * s.getPercent());
        if (fw > 0) RenderUtil.drawRoundedRect(tx, ty, tx + fw, ty + th, 2, NXColors.SLIDER_FILL);
        int kx = tx + fw - 3;
        RenderUtil.drawRoundedRect(kx, ty - 2, kx + 6, ty + th + 2, 3, 0xFFFFFFFF);
    }

    private void drawModeSetting(ModeSetting s, int x, int y) {
        mc.fontRendererObj.drawStringWithShadow(s.getName(), x + 5, y + 3, NXColors.TEXT_SECONDARY);
        String val = "§b" + s.getValue();
        int vw = mc.fontRendererObj.getStringWidth(val);
        mc.fontRendererObj.drawStringWithShadow("◀ " + val + " ▶", x + width - vw - 20, y + 3, NXColors.TEXT_SECONDARY);
    }

    private void drawColorSetting(ColorSetting s, int x, int y) {
        mc.fontRendererObj.drawStringWithShadow(s.getName(), x + 5, y + 3, NXColors.TEXT_SECONDARY);
        RenderUtil.drawRect(x + width - 16, y + 3, x + width - 4, y + 11, s.getColor() | 0xFF000000);
    }

    private void drawKeybindSetting(KeybindSetting s, int x, int y) {
        mc.fontRendererObj.drawStringWithShadow(s.getName(), x + 5, y + 3, NXColors.TEXT_SECONDARY);
        String key = s.isBinding() ? "§cPress..." : "§b" + s.getKeyName();
        int kw = mc.fontRendererObj.getStringWidth(key);
        mc.fontRendererObj.drawStringWithShadow("[" + key + "§r]", x + width - kw - 16, y + 3, NXColors.TEXT_SECONDARY);
    }

    public void mouseClicked(int mx, int my, int button, int x, int y) {
        if (button == 0) {
            // Toggle button (right side)
            if (mx >= x + width - 22 && mx <= x + width && my >= y && my < y + H) {
                module.toggle();
                return;
            }
            // Expand/collapse (left area)
            if (mx >= x && mx < x + width - 22 && my >= y && my < y + H) {
                if (!module.getSettings().isEmpty()) expanded = !expanded;
                return;
            }
        }
        if (button == 1 && mx >= x && mx < x + width && my >= y && my < y + H) {
            if (!module.getSettings().isEmpty()) expanded = !expanded;
            return;
        }

        if (!expanded) return;
        int iy = y + H;
        for (Setting<?> s : module.getSettings()) {
            if (my >= iy && my < iy + SETTING_H) {
                if (s instanceof BooleanSetting && button == 0) {
                    ((BooleanSetting) s).toggle();
                } else if (s instanceof SliderSetting && button == 0) {
                    draggingSlider = (SliderSetting) s;
                    updateSlider(mx, x, (SliderSetting) s);
                } else if (s instanceof ModeSetting) {
                    if (button == 0) ((ModeSetting) s).cycle();
                    if (button == 1) ((ModeSetting) s).cycleBack();
                } else if (s instanceof KeybindSetting && button == 0) {
                    ((KeybindSetting) s).setBinding(true);
                }
            }
            iy += SETTING_H;
        }
    }

    public void mouseReleased() { draggingSlider = null; }

    public void keyTyped(char c, int keyCode) {
        for (Setting<?> s : module.getSettings()) {
            if (s instanceof KeybindSetting && ((KeybindSetting) s).isBinding()) {
                ((KeybindSetting) s).setValue(keyCode == Keyboard.KEY_ESCAPE ? Keyboard.KEY_NONE : keyCode);
                ((KeybindSetting) s).setBinding(false);
                return;
            }
        }
    }

    private void updateSlider(int mx, int bx, SliderSetting s) {
        int tx = bx + 4, tw = width - 8;
        double pct = Math.max(0, Math.min(1, (double)(mx - tx) / tw));
        s.setValue(s.getMin() + pct * (s.getMax() - s.getMin()));
    }

    /** Extra pixel height when this button is expanded with settings. */
    public int getExpandedHeight() {
        return expanded ? module.getSettings().size() * SETTING_H : 0;
    }

    public boolean isExpanded() { return expanded; }
    public Module getModule()   { return module; }
}
