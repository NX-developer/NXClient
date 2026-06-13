package net.nx.client.gui.hud;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.nx.client.NXClient;
import net.nx.client.module.Module;
import net.nx.client.module.modules.hud.HUDModule;
import net.nx.client.ui.NXColors;
import net.nx.client.util.RenderUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HUDEditor extends GuiScreen {

    private HUDModule dragging;
    private double dragOffX, dragOffY;

    private static final int SNAP_GRID    = 4;
    private static final int SNAP_EDGE    = 8;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRect(0, 0, width, height, NXColors.withAlpha(0x000000, 160));

        drawGrid();

        ScaledResolution sr = new ScaledResolution(mc);
        List<HUDModule> huds = getHUDs();

        for (HUDModule hud : huds) {
            hud.renderHUD(sr);
            drawHandle(hud, mouseX, mouseY);
        }

        fontRendererObj.drawStringWithShadow("§bHUD Editor  §7— Drag elements | Right-click to reset | ESC to close", 4, 4, 0xFFFFFFFF);
        fontRendererObj.drawStringWithShadow("§7Grid Snap: " + SNAP_GRID + "px", 4, height - 12, NXColors.TEXT_SECONDARY);

        if (dragging != null) {
            double nx = snap(mouseX - dragOffX, SNAP_GRID, 0, width - dragging.getWidth());
            double ny = snap(mouseY - dragOffY, SNAP_GRID, 0, height - dragging.getHeight());
            nx = snapEdge(nx, width - dragging.getWidth(), SNAP_EDGE);
            ny = snapEdge(ny, height - dragging.getHeight(), SNAP_EDGE);
            dragging.setPos(nx, ny);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawGrid() {
        int gridColor = NXColors.withAlpha(0x2D7FF9, 15);
        for (int gx = 0; gx < width;  gx += SNAP_GRID) RenderUtil.drawRect(gx, 0, gx + 1, height, gridColor);
        for (int gy = 0; gy < height; gy += SNAP_GRID) RenderUtil.drawRect(0, gy, width, gy + 1, gridColor);
    }

    private void drawHandle(HUDModule hud, int mx, int my) {
        double x  = hud.getPosX();
        double y  = hud.getPosY();
        double x2 = x + hud.getWidth();
        double y2 = y + hud.getHeight();

        boolean hovered = mx >= x && mx <= x2 && my >= y && my <= y2;
        int outline = hovered ? NXColors.ACCENT_PRIMARY : NXColors.withAlpha(NXColors.ACCENT_PRIMARY, 80);
        RenderUtil.drawBorderedRect(x, y, x2, y2, 1, NXColors.withAlpha(NXColors.ACCENT_PRIMARY, 20), outline);
    }

    @Override
    protected void mouseClicked(int mx, int my, int button) throws IOException {
        super.mouseClicked(mx, my, button);
        List<HUDModule> huds = getHUDs();
        for (int i = huds.size() - 1; i >= 0; i--) {
            HUDModule h = huds.get(i);
            if (mx >= h.getPosX() && mx <= h.getPosX() + h.getWidth()
             && my >= h.getPosY() && my <= h.getPosY() + h.getHeight()) {
                if (button == 0) {
                    dragging = h;
                    dragOffX = mx - h.getPosX();
                    dragOffY = my - h.getPosY();
                } else if (button == 1) {
                    h.setPos(2, 2);
                }
                return;
            }
        }
    }

    @Override
    protected void mouseReleased(int mx, int my, int state) {
        if (state == 0) {
            dragging = null;
            NXClient.getInstance().getConfigManager().save();
        }
    }

    @Override
    protected void keyTyped(char c, int key) throws IOException {
        if (key == org.lwjgl.input.Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
        }
    }

    private double snap(double val, int grid, double min, double max) {
        return Math.max(min, Math.min(max, Math.round(val / grid) * grid));
    }

    private double snapEdge(double val, double maxVal, int threshold) {
        if (val < threshold) return 0;
        if (Math.abs(val - maxVal) < threshold) return maxVal;
        return val;
    }

    private List<HUDModule> getHUDs() {
        List<HUDModule> out = new ArrayList<>();
        for (Module m : NXClient.getInstance().getModuleManager().getModules()) {
            if (m instanceof HUDModule && m.isEnabled()) out.add((HUDModule) m);
        }
        return out;
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
