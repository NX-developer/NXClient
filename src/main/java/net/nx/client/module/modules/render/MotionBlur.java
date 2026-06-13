package net.nx.client.module.modules.render;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.SliderSetting;
import org.lwjgl.opengl.GL11;

public class MotionBlur extends Module {

    private final SliderSetting strength;

    public MotionBlur() {
        super("Motion Blur", "Adds motion blur effect to the world", Category.RENDER);
        this.strength = addSetting(new SliderSetting("Strength", "Blur strength (higher = more blur)", 0.5, 0.1, 0.9, 0.05));
        setEnabled(false);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        float s = (float) strength.getValue();
        GL11.glAccum(GL11.GL_MULT, s);
        GL11.glAccum(GL11.GL_ACCUM, 1f - s);
        GL11.glAccum(GL11.GL_RETURN, 1f);
    }
}
