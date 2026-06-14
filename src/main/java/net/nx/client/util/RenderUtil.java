package net.nx.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class RenderUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void drawRect(double x, double y, double x2, double y2, int color) {
        if (x > x2) { double t = x; x = x2; x2 = t; }
        if (y > y2) { double t = y; y = y2; y2 = t; }

        Color c = new Color(color, true);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.pos(x,  y,  0).endVertex();
        wr.pos(x,  y2, 0).endVertex();
        wr.pos(x2, y2, 0).endVertex();
        wr.pos(x2, y,  0).endVertex();
        tess.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawRoundedRect(double x, double y, double x2, double y2, double radius, int color) {
        drawRect(x + radius, y, x2 - radius, y2, color);
        drawRect(x, y + radius, x + radius, y2 - radius, color);
        drawRect(x2 - radius, y + radius, x2, y2 - radius, color);
        drawFilledCircleQuadrant(x + radius,  y + radius,  radius, 2, color);
        drawFilledCircleQuadrant(x2 - radius, y + radius,  radius, 1, color);
        drawFilledCircleQuadrant(x + radius,  y2 - radius, radius, 3, color);
        drawFilledCircleQuadrant(x2 - radius, y2 - radius, radius, 0, color);
    }

    private static void drawFilledCircleQuadrant(double cx, double cy, double r, int quadrant, int color) {
        Color c = new Color(color, true);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(cx, cy);
        int startAngle = quadrant * 90;
        for (int i = startAngle; i <= startAngle + 90; i += 5) {
            double rad = Math.toRadians(i);
            GL11.glVertex2d(cx + Math.cos(rad) * r, cy - Math.sin(rad) * r);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(double x, double y, double x2, double y2, int colorTop, int colorBottom) {
        Color t = new Color(colorTop, true);
        Color b = new Color(colorBottom, true);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        wr.pos(x,  y,  0).color(t.getRed(), t.getGreen(), t.getBlue(), t.getAlpha()).endVertex();
        wr.pos(x2, y,  0).color(t.getRed(), t.getGreen(), t.getBlue(), t.getAlpha()).endVertex();
        wr.pos(x2, y2, 0).color(b.getRed(), b.getGreen(), b.getBlue(), b.getAlpha()).endVertex();
        wr.pos(x,  y2, 0).color(b.getRed(), b.getGreen(), b.getBlue(), b.getAlpha()).endVertex();
        tess.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawBorderedRect(double x, double y, double x2, double y2, double bw, int fill, int border) {
        drawRect(x, y, x2, y2, fill);
        drawRect(x - bw, y - bw, x2 + bw, y, border);
        drawRect(x - bw, y2, x2 + bw, y2 + bw, border);
        drawRect(x - bw, y, x, y2, border);
        drawRect(x2, y, x2 + bw, y2, border);
    }

    public static String trimString(FontRenderer fr, String text, int maxWidth) {
        if (fr.getStringWidth(text) <= maxWidth) return text;
        while (!text.isEmpty() && fr.getStringWidth(text + "...") > maxWidth)
            text = text.substring(0, text.length() - 1);
        return text + "...";
    }

    public static void scissor(int x, int y, int width, int height) {
        // Use actual ScaledResolution factor, not raw guiScale setting
        ScaledResolution sr = new ScaledResolution(mc);
        int factor = sr.getScaleFactor();
        int screenH = mc.displayHeight;
        GL11.glScissor(
            x * factor,
            screenH - (y + height) * factor,
            width * factor,
            height * factor
        );
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void endScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
