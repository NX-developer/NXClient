package net.nx.client.gui.mainmenu;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.nx.client.NXClient;
import net.nx.client.gui.clickgui.ClickGUI;
import net.nx.client.ui.NXColors;
import net.nx.client.util.RenderUtil;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomMainMenu extends GuiScreen {

    private static final int BUTTON_W = 160;
    private static final int BUTTON_H = 24;

    private final List<Particle> particles = new ArrayList<>();
    private final Random rand = new Random();
    private long openTime;
    private MenuButton[] menuButtons;
    private float fadeIn = 0f;

    @Override
    public void initGui() {
        openTime = System.currentTimeMillis();
        particles.clear();
        for (int i = 0; i < 60; i++) spawnParticle();

        int cx = width / 2;
        int startY = height / 2 - 48;

        menuButtons = new MenuButton[]{
            new MenuButton(0, cx - BUTTON_W / 2, startY,       BUTTON_W, BUTTON_H, "Singleplayer"),
            new MenuButton(1, cx - BUTTON_W / 2, startY + 32,  BUTTON_W, BUTTON_H, "Multiplayer"),
            new MenuButton(2, cx - BUTTON_W / 2, startY + 64,  BUTTON_W, BUTTON_H, "Options"),
            new MenuButton(3, cx - BUTTON_W / 2, startY + 96,  BUTTON_W, BUTTON_H, "Modules"),
            new MenuButton(4, cx - BUTTON_W / 2, startY + 128, BUTTON_W, BUTTON_H, "Quit Game"),
        };
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Solid black base — prevents world/inventory bleeding through on re-open
        GL11.glDisable(GL11.GL_BLEND);
        drawRect(0, 0, width, height, 0xFF000000);

        long elapsed = System.currentTimeMillis() - openTime;
        fadeIn = Math.min(1f, elapsed / 600f);

        drawBackground();
        updateAndDrawParticles();

        GlStateManager.enableBlend();
        GlStateManager.color(1f, 1f, 1f, fadeIn);

        drawLogo();
        drawVersionBadge();

        for (MenuButton btn : menuButtons) {
            btn.draw(mouseX, mouseY, fontRendererObj, fadeIn);
        }

        fontRendererObj.drawStringWithShadow(
            "§7Build: §b" + NXClient.VERSION + "  §7|  MC 1.8.9 Forge",
            4, height - 11, NXColors.TEXT_SECONDARY);

        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawBackground() {
        long t = System.currentTimeMillis();
        float wave = (float)(Math.sin(t * 0.001) * 0.5 + 0.5);
        int top    = blendColors(0xFF050510, 0xFF0A0A20, wave);
        int bottom = blendColors(0xFF080818, 0xFF0D1030, wave);
        RenderUtil.drawGradientRect(0, 0, width, height, top, bottom);
    }

    private void updateAndDrawParticles() {
        particles.removeIf(p -> p.life <= 0);
        while (particles.size() < 60) spawnParticle();
        for (Particle p : particles) { p.update(); p.draw(); }
    }

    private void spawnParticle() {
        particles.add(new Particle(
            rand.nextFloat() * width,
            rand.nextFloat() * height,
            (rand.nextFloat() - 0.5f) * 0.4f,
            -rand.nextFloat() * 0.5f - 0.1f,
            rand.nextInt(120) + 80
        ));
    }

    private void drawLogo() {
        int lx = width / 2;
        int ly = height / 4 - 10;
        long t = System.currentTimeMillis();
        float glow = (float)(Math.sin(t * 0.002) * 0.5 + 0.5);
        int glowAlpha = (int)(30 + glow * 30);

        // Glow halo
        for (int d = 5; d > 0; d--) {
            int a = (int)(glowAlpha * (1f - d / 5f));
            int gc = NXColors.withAlpha(NXColors.ACCENT_PRIMARY, a);
            drawScaledText("NX", lx + d, ly, gc, 2.5f);
            drawScaledText("NX", lx - d, ly, gc, 2.5f);
        }
        // Main text — no shadow at large scale to avoid K-artifact on Android
        drawScaledTextNoShadow("NX", lx, ly, NXColors.ACCENT_PRIMARY, 2.5f);
        drawScaledTextNoShadow("CLIENT", lx, ly + 26, NXColors.withAlpha(0xFFFFFFFF, 200), 1.4f);

        // Separator line
        int lineW = 90;
        RenderUtil.drawGradientRect(lx - lineW, ly + 47, lx, ly + 48,
            NXColors.withAlpha(NXColors.ACCENT_PRIMARY, 0), NXColors.ACCENT_PRIMARY);
        RenderUtil.drawGradientRect(lx, ly + 47, lx + lineW, ly + 48,
            NXColors.ACCENT_PRIMARY, NXColors.withAlpha(NXColors.ACCENT_PRIMARY, 0));
    }

    private void drawScaledText(String text, int x, int y, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1f);
        int w = fontRendererObj.getStringWidth(text);
        fontRendererObj.drawStringWithShadow(text, -w / 2f, 0, color);
        GlStateManager.popMatrix();
    }

    private void drawScaledTextNoShadow(String text, int x, int y, int color, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1f);
        int w = fontRendererObj.getStringWidth(text);
        fontRendererObj.drawString(text, -w / 2, 0, color);
        GlStateManager.popMatrix();
    }

    private void drawVersionBadge() {
        String ver = " v" + NXClient.VERSION + " ";
        int vw = fontRendererObj.getStringWidth(ver) + 4;
        int vx = width / 2 - vw / 2;
        int vy = height / 4 + 40;
        RenderUtil.drawRoundedRect(vx, vy, vx + vw, vy + 12, 3,
            NXColors.withAlpha(NXColors.ACCENT_PRIMARY, 50));
        fontRendererObj.drawStringWithShadow(ver, vx + 2, vy + 2, NXColors.TEXT_SECONDARY);
    }

    @Override
    protected void mouseClicked(int mx, int my, int button) throws IOException {
        super.mouseClicked(mx, my, button);
        if (button != 0) return;
        for (MenuButton btn : menuButtons) {
            if (btn.isHovered(mx, my)) { btn.click(this); return; }
        }
    }

    private int blendColors(int a, int b, float t) {
        Color ca = new Color(a, true), cb = new Color(b, true);
        int r  = (int)(ca.getRed()   + (cb.getRed()   - ca.getRed())   * t);
        int g  = (int)(ca.getGreen() + (cb.getGreen() - ca.getGreen()) * t);
        int bl = (int)(ca.getBlue()  + (cb.getBlue()  - ca.getBlue())  * t);
        return new Color(r, g, bl, 255).getRGB();
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }

    // ---- Particle ----

    private class Particle {
        float x, y, vx, vy;
        int life, maxLife;
        float size;

        Particle(float x, float y, float vx, float vy, int life) {
            this.x = x; this.y = y; this.vx = vx; this.vy = vy;
            this.life = this.maxLife = life;
            this.size = rand.nextFloat() * 1.5f + 0.5f;
        }

        void update() { x += vx; y += vy; life--; if (x < 0 || x > width || y < 0 || y > height) life = 0; }

        void draw() {
            int a = (int)((float) life / maxLife * 100);
            RenderUtil.drawRect(x - size, y - size, x + size, y + size,
                NXColors.withAlpha(NXColors.ACCENT_PRIMARY, a));
        }
    }

    // ---- MenuButton ----

    private class MenuButton {
        final int id, x, y, w, h;
        final String label;
        float hoverAnim = 0f;

        MenuButton(int id, int x, int y, int w, int h, String label) {
            this.id = id; this.x = x; this.y = y; this.w = w; this.h = h; this.label = label;
        }

        boolean isHovered(int mx, int my) {
            return mx >= x && mx <= x + w && my >= y && my <= y + h;
        }

        void draw(int mx, int my, FontRenderer fr, float alpha) {
            boolean hov = isHovered(mx, my);
            hoverAnim += hov ? 0.12f : -0.12f;
            hoverAnim = Math.max(0f, Math.min(1f, hoverAnim));

            // id=3 (Modules) gets accent tint always
            float tint = (id == 3) ? Math.max(0.15f, hoverAnim) : hoverAnim * 0.4f;
            RenderUtil.drawRoundedRect(x, y, x + w, y + h, 5,
                NXColors.lerp(NXColors.withAlpha(0x0A0A1A, 200),
                              NXColors.withAlpha(NXColors.ACCENT_PRIMARY, 80), tint));
            RenderUtil.drawBorderedRect(x, y, x + w, y + h, 1, 0x00000000,
                NXColors.lerp(NXColors.BORDER, NXColors.ACCENT_PRIMARY, hoverAnim));

            if (hoverAnim > 0.05f) {
                RenderUtil.drawGradientRect(x + 2, y + 1, x + w - 2, y + 3,
                    NXColors.withAlpha(NXColors.ACCENT_PRIMARY, (int)(hoverAnim * 40)), 0x00000000);
            }

            int textC = NXColors.lerp(NXColors.TEXT_SECONDARY, 0xFFFFFFFF, hoverAnim);
            fr.drawStringWithShadow(label, x + (w - fr.getStringWidth(label)) / 2f, y + (h - 8) / 2f, textC);
        }

        void click(GuiScreen screen) {
            switch (id) {
                case 0: mc.displayGuiScreen(new GuiSelectWorld(screen)); break;
                case 1: mc.displayGuiScreen(new GuiMultiplayer(screen)); break;
                case 2: mc.displayGuiScreen(new GuiOptions(screen, mc.gameSettings)); break;
                case 3: mc.displayGuiScreen(new ClickGUI()); break;
                case 4: mc.shutdown(); break;
            }
        }
    }
}
