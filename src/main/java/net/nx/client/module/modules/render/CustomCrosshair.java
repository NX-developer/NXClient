package net.nx.client.module.modules.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.*;
import net.nx.client.ui.NXColors;
import net.nx.client.util.RenderUtil;

import java.awt.Color;

public class CustomCrosshair extends Module {

    private final ModeSetting style;
    private final SliderSetting size;
    private final SliderSetting gap;
    private final SliderSetting thickness;
    private final BooleanSetting dot;
    private final ColorSetting color;

    public CustomCrosshair() {
        super("Custom Crosshair", "Custom crosshair with various styles", Category.RENDER);
        this.style     = addSetting(new ModeSetting("Style", "Crosshair style", "Cross", "Cross", "T-Shape", "Circle", "Dot"));
        this.size      = addSetting(new SliderSetting("Size", "Crosshair arm length", 5, 1, 15, 1));
        this.gap       = addSetting(new SliderSetting("Gap", "Center gap size", 3, 0, 10, 1));
        this.thickness = addSetting(new SliderSetting("Thickness", "Line thickness", 1, 1, 4, 1));
        this.dot       = addSetting(new BooleanSetting("Dot", "Center dot", false));
        this.color     = addSetting(new ColorSetting("Color", "Crosshair color", new Color(255, 255, 255, 200)));
        setEnabled(false);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPost(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (mc.gameSettings.showDebugInfo || mc.thePlayer == null) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int cx = sr.getScaledWidth()  / 2;
        int cy = sr.getScaledHeight() / 2;

        int s  = size.getIntValue();
        int g  = gap.getIntValue();
        int t  = thickness.getIntValue();
        int c  = color.getColor();

        if (style.is("Cross") || style.is("T-Shape")) {
            RenderUtil.drawRect(cx - s - g, cy - t/2, cx - g, cy + t/2 + 1, c);
            if (!style.is("T-Shape")) {
                RenderUtil.drawRect(cx + g, cy - t/2, cx + s + g, cy + t/2 + 1, c);
            }
            RenderUtil.drawRect(cx - t/2, cy - s - g, cx + t/2 + 1, cy - g, c);
            RenderUtil.drawRect(cx - t/2, cy + g, cx + t/2 + 1, cy + s + g, c);
        }

        if (dot.isEnabled()) {
            RenderUtil.drawRect(cx - 1, cy - 1, cx + 1, cy + 1, c);
        }
    }
}
