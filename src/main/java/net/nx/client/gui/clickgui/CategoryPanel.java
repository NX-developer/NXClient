package net.nx.client.gui.clickgui;

import net.minecraft.client.Minecraft;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.ui.NXColors;
import net.nx.client.util.RenderUtil;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class CategoryPanel {

    private static final int HEADER_H = 18;
    private static final int MODULE_H = 17;
    private static final int PAD      = 4;
    private static final double RADIUS = 4;

    private final Category category;
    private final List<ModuleButton> buttons = new ArrayList<>();
    private double x, y;
    private final int width;
    private boolean collapsed = false;
    private boolean dragging  = false;
    private double dragOffX, dragOffY;

    private int scrollOffset = 0;
    private static final int MAX_VISIBLE = 12;

    private final Minecraft mc = Minecraft.getMinecraft();

    public CategoryPanel(Category category, List<Module> modules, int x, int y, int width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        for (Module m : modules) buttons.add(new ModuleButton(m, width - PAD * 2));
    }

    public void draw(int mx, int my, String filter) {
        List<ModuleButton> visible = getVisible(filter);
        int contentH = collapsed ? 0 : Math.min(visible.size(), MAX_VISIBLE) * MODULE_H + PAD;
        int totalH   = HEADER_H + contentH;

        RenderUtil.drawRoundedRect(x, y, x + width, y + totalH, RADIUS, NXColors.BACKGROUND_PANEL);
        RenderUtil.drawRoundedRect(x, y, x + width, y + HEADER_H, RADIUS, NXColors.ACCENT_PRIMARY);

        mc.fontRendererObj.drawStringWithShadow(category.getDisplayName(), (float)(x + PAD + 2), (float)(y + 5), 0xFFFFFFFF);
        String arrow = collapsed ? "▶" : "▼";
        mc.fontRendererObj.drawStringWithShadow(arrow, (float)(x + width - PAD - 8), (float)(y + 5), 0xFFFFFFFF);

        if (!collapsed) {
            RenderUtil.scissor((int)x, (int)(y + HEADER_H), width, contentH);
            int iy = (int)(y + HEADER_H + PAD / 2);
            for (int i = scrollOffset; i < Math.min(scrollOffset + MAX_VISIBLE, visible.size()); i++) {
                ModuleButton btn = visible.get(i);
                btn.draw(mx, my, (int)(x + PAD), iy);
                iy += MODULE_H;
            }
            RenderUtil.endScissor();

            if (visible.size() > MAX_VISIBLE) {
                drawScrollbar(contentH);
            }
        }
    }

    private void drawScrollbar(int contentH) {
        List<ModuleButton> vis = getVisible("");
        int total = vis.size();
        if (total <= MAX_VISIBLE) return;
        double trackH = contentH - PAD * 2;
        double thumbH = trackH * MAX_VISIBLE / total;
        double thumbY = y + HEADER_H + PAD + (trackH - thumbH) * scrollOffset / (total - MAX_VISIBLE);
        RenderUtil.drawRect(x + width - 3, y + HEADER_H + PAD, x + width - 1, y + HEADER_H + contentH - PAD, NXColors.withAlpha(NXColors.SCROLLBAR, 60));
        RenderUtil.drawRect(x + width - 3, thumbY, x + width - 1, thumbY + thumbH, NXColors.SCROLLBAR);
    }

    public void mouseClicked(int mx, int my, int button, String filter) {
        boolean onHeader = mx >= x && mx <= x + width && my >= y && my <= y + HEADER_H;
        if (onHeader) {
            if (button == 0) { dragging = true; dragOffX = mx - x; dragOffY = my - y; }
            if (button == 1) { collapsed = !collapsed; }
            return;
        }
        if (collapsed) return;
        List<ModuleButton> visible = getVisible(filter);
        int iy = (int)(y + HEADER_H + PAD / 2);
        for (int i = scrollOffset; i < Math.min(scrollOffset + MAX_VISIBLE, visible.size()); i++) {
            ModuleButton btn = visible.get(i);
            if (my >= iy && my < iy + MODULE_H) {
                btn.mouseClicked(mx, my, button, (int)(x + PAD), iy);
            }
            iy += MODULE_H;
        }
    }

    public void mouseReleased(int mx, int my, int state) {
        if (state == 0) dragging = false;
        for (ModuleButton btn : buttons) btn.mouseReleased();
        if (dragging) { x = mx - dragOffX; y = my - dragOffY; }
    }

    public void mouseScrolled(int mx, int my, int direction, String filter) {
        List<ModuleButton> visible = getVisible(filter);
        int contentH = Math.min(visible.size(), MAX_VISIBLE) * MODULE_H + PAD;
        if (mx >= x && mx <= x + width && my >= y && my <= y + HEADER_H + contentH) {
            scrollOffset = Math.max(0, Math.min(scrollOffset - direction, Math.max(0, visible.size() - MAX_VISIBLE)));
        }
    }

    public void keyTyped(char c, int keyCode) {
        for (ModuleButton btn : buttons) btn.keyTyped(c, keyCode);
    }

    public void update(int mx, int my) {
        if (dragging) { x = mx - dragOffX; y = my - dragOffY; }
    }

    private List<ModuleButton> getVisible(String filter) {
        if (filter.isEmpty()) return buttons;
        List<ModuleButton> out = new ArrayList<>();
        for (ModuleButton b : buttons) {
            if (b.getModule().getName().toLowerCase().contains(filter)
             || b.getModule().getDescription().toLowerCase().contains(filter)) {
                out.add(b);
            }
        }
        return out;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
