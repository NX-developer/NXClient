package net.nx.client.module.modules.render;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.SliderSetting;

public class FPSBoost extends Module {

    public final BooleanSetting reducedParticles;
    public final BooleanSetting disableFog;
    public final BooleanSetting fastRender;
    public final BooleanSetting smoothFPS;
    public final SliderSetting  renderDistance;
    public final SliderSetting  entityDistance;

    private int savedRenderDist = -1;
    private int savedParticles  = -1;

    public FPSBoost() {
        super("FPS Boost", "Reduces render load for better FPS performance", Category.RENDER);
        this.reducedParticles = addSetting(new BooleanSetting("Reduced Particles", "Minimize particle effects",       true));
        this.disableFog       = addSetting(new BooleanSetting("Disable Fog",       "Remove fog for clarity & speed",  true));
        this.fastRender       = addSetting(new BooleanSetting("Fast Render",       "Optimized OpenGL rendering",       true));
        this.smoothFPS        = addSetting(new BooleanSetting("Smooth FPS",        "Stabilize frame rate",             false));
        this.renderDistance   = addSetting(new SliderSetting("Render Distance",    "Chunk render distance", 8, 2, 16, 1));
        this.entityDistance   = addSetting(new SliderSetting("Entity Distance",    "Entity render distance %", 100, 25, 500, 25));
        setEnabled(true);
    }

    @Override
    public void onEnable() {
        applySettings();
    }

    @Override
    public void onDisable() {
        if (savedRenderDist >= 0) mc.gameSettings.renderDistanceChunks = savedRenderDist;
        if (savedParticles >= 0)  mc.gameSettings.particleSetting = savedParticles;
    }

    @Override
    public void onUpdate() {
        applySettings();
    }

    private void applySettings() {
        if (savedRenderDist < 0) savedRenderDist = mc.gameSettings.renderDistanceChunks;
        if (savedParticles  < 0) savedParticles  = mc.gameSettings.particleSetting;

        mc.gameSettings.renderDistanceChunks = renderDistance.getIntValue();
        mc.gameSettings.particleSetting      = reducedParticles.isEnabled() ? 2 : savedParticles;
    }
}
