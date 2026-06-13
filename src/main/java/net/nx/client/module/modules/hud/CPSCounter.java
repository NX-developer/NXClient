package net.nx.client.module.modules.hud;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.ui.NXColors;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CPSCounter extends HUDModule {

    private final List<Long> leftClicks  = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();
    private final BooleanSetting showBoth;
    private final ColorSetting color;

    public CPSCounter() {
        super("CPS Counter", "Shows clicks per second", 2, 13);
        this.showBoth = addSetting(new BooleanSetting("Show Both", "Show both left and right CPS", true));
        this.color    = addSetting(new ColorSetting("Color", "Text color", new Color(NXColors.ACCENT_PRIMARY, true)));
        setEnabled(true);
    }

    @Override
    public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override
    public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent
    public void onMouseInput(InputEvent.MouseInputEvent event) {
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

        String text;
        if (showBoth.isEnabled()) {
            text = leftClicks.size() + " | " + rightClicks.size() + " CPS";
        } else {
            text = leftClicks.size() + " CPS";
        }
        mc.fontRendererObj.drawStringWithShadow(text, (float) posX, (float) posY, color.getColor());
    }

    private void purge(List<Long> list, long now) {
        Iterator<Long> it = list.iterator();
        while (it.hasNext()) if (now - it.next() > 1000) it.remove();
    }

    @Override public int getWidth()  { return mc.fontRendererObj.getStringWidth("10 | 10 CPS"); }
    @Override public int getHeight() { return 9; }
}
