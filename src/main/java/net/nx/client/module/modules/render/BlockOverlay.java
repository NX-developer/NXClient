package net.nx.client.module.modules.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ColorSetting;
import net.nx.client.module.settings.ModeSetting;
import net.nx.client.ui.NXColors;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class BlockOverlay extends Module {

    private final ModeSetting mode;
    private final ColorSetting outlineColor;
    private final ColorSetting fillColor;
    private final BooleanSetting fill;

    public BlockOverlay() {
        super("Block Overlay", "Custom outline/fill on the block you're looking at", Category.RENDER);
        this.mode         = addSetting(new ModeSetting("Mode", "Rendering mode", "Outline", "Outline", "Fill", "Both"));
        this.outlineColor = addSetting(new ColorSetting("Outline Color", "Outline color", new Color(NXColors.ACCENT_PRIMARY, true)));
        this.fillColor    = addSetting(new ColorSetting("Fill Color", "Fill color", new Color(0x2D, 0x7F, 0xF9, 40)));
        this.fill         = addSetting(new BooleanSetting("Antialias", "Smooth edges", true));
        setEnabled(true);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDrawHighlight(DrawBlockHighlightEvent event) {
        event.setCanceled(true);
        MovingObjectPosition mop = event.target;
        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;

        BlockPos pos = mop.getBlockPos();
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if (block == null) return;

        Entity rv = mc.getRenderViewEntity();
        float pt = event.partialTicks;
        double rpX = rv.lastTickPosX + (rv.posX - rv.lastTickPosX) * pt;
        double rpY = rv.lastTickPosY + (rv.posY - rv.lastTickPosY) * pt;
        double rpZ = rv.lastTickPosZ + (rv.posZ - rv.lastTickPosZ) * pt;

        double x = pos.getX() - rpX;
        double y = pos.getY() - rpY;
        double z = pos.getZ() - rpZ;
        double eps = 0.002;

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glLineWidth(1.5f);
        GlStateManager.depthMask(false);

        if (!mode.is("Fill")) {
            Color oc = new Color(outlineColor.getColor(), true);
            GlStateManager.color(oc.getRed()/255f, oc.getGreen()/255f, oc.getBlue()/255f, oc.getAlpha()/255f);
            GlStateManager.disableDepth();
            drawBoxOutline(x - eps, y - eps, z - eps, x + 1 + eps, y + 1 + eps, z + 1 + eps);
            GlStateManager.enableDepth();
        }

        if (!mode.is("Outline")) {
            Color fc = new Color(fillColor.getColor(), true);
            GlStateManager.color(fc.getRed()/255f, fc.getGreen()/255f, fc.getBlue()/255f, fc.getAlpha()/255f);
            drawBoxFill(x - eps, y - eps, z - eps, x + 1 + eps, y + 1 + eps, z + 1 + eps);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }

    private void drawBoxOutline(double x, double y, double z, double x2, double y2, double z2) {
        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        wr.pos(x,  y,  z).endVertex();  wr.pos(x2, y,  z).endVertex();
        wr.pos(x2, y,  z2).endVertex(); wr.pos(x,  y,  z2).endVertex();
        wr.pos(x,  y,  z).endVertex();  wr.pos(x,  y2, z).endVertex();
        wr.pos(x2, y2, z).endVertex();  wr.pos(x2, y2, z2).endVertex();
        wr.pos(x,  y2, z2).endVertex(); wr.pos(x,  y2, z).endVertex();
        t.draw();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        wr.pos(x2, y, z).endVertex();  wr.pos(x2, y2, z).endVertex();
        wr.pos(x2, y, z2).endVertex(); wr.pos(x2, y2, z2).endVertex();
        wr.pos(x,  y, z2).endVertex(); wr.pos(x,  y2, z2).endVertex();
        t.draw();
    }

    private void drawBoxFill(double x, double y, double z, double x2, double y2, double z2) {
        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.pos(x,  y,  z).endVertex();  wr.pos(x2, y,  z).endVertex();  wr.pos(x2, y,  z2).endVertex(); wr.pos(x,  y,  z2).endVertex();
        wr.pos(x,  y2, z).endVertex();  wr.pos(x,  y2, z2).endVertex(); wr.pos(x2, y2, z2).endVertex(); wr.pos(x2, y2, z).endVertex();
        wr.pos(x,  y,  z).endVertex();  wr.pos(x,  y2, z).endVertex();  wr.pos(x2, y2, z).endVertex();  wr.pos(x2, y,  z).endVertex();
        wr.pos(x,  y,  z2).endVertex(); wr.pos(x2, y,  z2).endVertex(); wr.pos(x2, y2, z2).endVertex(); wr.pos(x,  y2, z2).endVertex();
        wr.pos(x,  y,  z).endVertex();  wr.pos(x,  y,  z2).endVertex(); wr.pos(x,  y2, z2).endVertex(); wr.pos(x,  y2, z).endVertex();
        wr.pos(x2, y,  z).endVertex();  wr.pos(x2, y2, z).endVertex();  wr.pos(x2, y2, z2).endVertex(); wr.pos(x2, y,  z2).endVertex();
        t.draw();
    }
}
