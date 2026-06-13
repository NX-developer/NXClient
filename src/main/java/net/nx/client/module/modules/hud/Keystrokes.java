package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.ui.NXColors;
import net.nx.client.util.RenderUtil;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Keystrokes extends HUDModule {

    private final BooleanSetting showCPS;
    private final ColorSetting activeColor;
    private final ColorSetting inactiveColor;

    private final List<Long> leftClicks  = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();

    private static final int KEY_W = 28, KEY_A = 30, KEY_S = 31, KEY_D = 32;
    private static final int BTN_W = 22, BTN_H = 14, GAP = 2;

    public Keystrokes() {
        super("Keystrokes", "Shows WASD + mouse button presses with CPS", 2, 100);
        this.showCPS      = addSetting(new BooleanSetting("Show CPS", "Show CPS on mouse buttons", true));
        this.activeColor  = addSetting(new ColorSetting("Active Color",   "Color when key pressed", new Color(NXColors.ACCENT_PRIMARY, true)));
        this.inactiveColor= addSetting(new ColorSetting("Inactive Color", "Color when key released", new Color(0x22, 0x22, 0x33, 180)));
        setEnabled(true);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent
    public void onMouse(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState()) {
            int btn = Mouse.getEventButton();
            if (btn == 0) leftClicks.add(System.currentTimeMillis());
            if (btn == 1) rightClicks.add(System.currentTimeMillis());
        }
    }

    @Override
    public void renderHUD(ScaledResolution res) {
        long now = System.currentTimeMillis();
        purge(leftClicks, now);
        purge(rightClicks, now);

        float x = (float) posX;
        float y = (float) posY;

        boolean w = isKeyDown(KEY_W), a = isKeyDown(KEY_A), s = isKeyDown(KEY_S), d = isKeyDown(KEY_D);
        boolean lmb = Mouse.isButtonDown(0), rmb = Mouse.isButtonDown(1);
        boolean space = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_SPACE);

        drawKey("W", x + BTN_W + GAP, y, w);
        drawKey("A", x, y + BTN_H + GAP, a);
        drawKey("S", x + BTN_W + GAP, y + BTN_H + GAP, s);
        drawKey("D", x + (BTN_W + GAP) * 2, y + BTN_H + GAP, d);

        String lText = showCPS.isEnabled() ? leftClicks.size() + " LMB" : "LMB";
        String rText = showCPS.isEnabled() ? rightClicks.size() + " RMB" : "RMB";
        drawKey(lText, x, y + (BTN_H + GAP) * 2, lmb);
        drawKey(rText, x + BTN_W + GAP, y + (BTN_H + GAP) * 2, rmb);

        drawWideKey("SPACE", x, y + (BTN_H + GAP) * 3, space);
    }

    private void drawKey(String label, float x, float y, boolean pressed) {
        int bg = pressed ? activeColor.getColor() : inactiveColor.getColor();
        RenderUtil.drawRoundedRect(x, y, x + BTN_W, y + BTN_H, 2, bg);
        int textColor = pressed ? 0xFFFFFFFF : 0xFF888899;
        int tw = mc.fontRendererObj.getStringWidth(label);
        mc.fontRendererObj.drawStringWithShadow(label, x + (BTN_W - tw) / 2f, y + (BTN_H - 8) / 2f, textColor);
    }

    private void drawWideKey(String label, float x, float y, boolean pressed) {
        int totalW = BTN_W * 3 + GAP * 2;
        int bg = pressed ? activeColor.getColor() : inactiveColor.getColor();
        RenderUtil.drawRoundedRect(x, y, x + totalW, y + BTN_H, 2, bg);
        int textColor = pressed ? 0xFFFFFFFF : 0xFF888899;
        int tw = mc.fontRendererObj.getStringWidth(label);
        mc.fontRendererObj.drawStringWithShadow(label, x + (totalW - tw) / 2f, y + (BTN_H - 8) / 2f, textColor);
    }

    private boolean isKeyDown(int keyCode) {
        return org.lwjgl.input.Keyboard.isKeyDown(keyCode);
    }

    private void purge(List<Long> list, long now) {
        Iterator<Long> it = list.iterator();
        while (it.hasNext()) if (now - it.next() > 1000) it.remove();
    }

    @Override public int getWidth()  { return BTN_W * 3 + GAP * 2; }
    @Override public int getHeight() { return BTN_H * 4 + GAP * 3; }
}
