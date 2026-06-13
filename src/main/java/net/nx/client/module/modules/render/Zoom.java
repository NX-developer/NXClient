package net.nx.client.module.modules.render;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.SliderSetting;
import org.lwjgl.input.Keyboard;

public class Zoom extends Module {

    private final SliderSetting zoomLevel;
    private final BooleanSetting smoothZoom;
    private final BooleanSetting scaleSensitivity;

    private float currentFOV = 70f;
    private float targetFOV  = 70f;
    private float savedSens  = -1f;
    private boolean wasZoomed = false;

    public Zoom() {
        super("Zoom", "Smooth zoom while holding key (default: C)", Category.RENDER, Keyboard.KEY_C);
        this.zoomLevel        = addSetting(new SliderSetting("Zoom Level",   "Target FOV while zoomed", 10, 1, 45, 1));
        this.smoothZoom       = addSetting(new BooleanSetting("Smooth Zoom", "Smooth zoom transition",   true));
        this.scaleSensitivity = addSetting(new BooleanSetting("Scale Sens",  "Scale mouse sensitivity",  true));
        setEnabled(true);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); restoreState(); }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        boolean holding = Keyboard.isKeyDown(getKeybind());

        if (holding) {
            targetFOV = zoomLevel.getValue().floatValue();
            if (!wasZoomed && scaleSensitivity.isEnabled()) {
                savedSens = mc.gameSettings.mouseSensitivity;
                mc.gameSettings.mouseSensitivity = savedSens * (zoomLevel.getValue().floatValue() / 70f);
            }
            wasZoomed = true;
        } else {
            targetFOV = mc.gameSettings.fovSetting;
            if (wasZoomed) restoreState();
            wasZoomed = false;
        }

        if (smoothZoom.isEnabled()) {
            currentFOV += (targetFOV - currentFOV) * 0.2f;
        } else {
            currentFOV = targetFOV;
        }
        mc.gameSettings.fovSetting = (int) currentFOV;
    }

    private void restoreState() {
        if (savedSens >= 0) {
            mc.gameSettings.mouseSensitivity = savedSens;
            savedSens = -1f;
        }
        currentFOV = mc.gameSettings.fovSetting;
        targetFOV  = currentFOV;
    }
}
