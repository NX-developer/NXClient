package net.nx.client.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.nx.client.NXClient;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.ui.NXColors;
import net.nx.client.util.RenderUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends GuiScreen {

    private final List<CategoryPanel> panels = new ArrayList<>();
    private GuiTextField searchField;
    private float blurAlpha = 0f;
    private long openTime;

    @Override
    public void initGui() {
        openTime = System.currentTimeMillis();
        blurAlpha = 0f;
        panels.clear();

        int panelX = 8;
        int panelY = 30;
        int panelW = 140;

        for (Category cat : Category.values()) {
            List<Module> mods = NXClient.getInstance().getModuleManager().getByCategory(cat);
            CategoryPanel panel = new CategoryPanel(cat, mods, panelX, panelY, panelW);
            panels.add(panel);
            panelX += panelW + 6;
        }

        searchField = new GuiTextField(0, fontRendererObj, width / 2 - 75, 6, 150, 16);
        searchField.setMaxStringLength(32);
        searchField.setFocused(false);
        searchField.setCanLoseFocus(true);
        searchField.setGuiBackgroundColor(NXColors.BACKGROUND_DARK);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        long elapsed = System.currentTimeMillis() - openTime;
        blurAlpha = Math.min(1f, elapsed / 120f);

        RenderUtil.drawRect(0, 0, width, height, NXColors.withAlpha(0x000000, (int)(120 * blurAlpha)));

        drawSearchBar(mouseX, mouseY);

        String filter = searchField.getText().toLowerCase().trim();
        for (CategoryPanel panel : panels) {
            panel.draw(mouseX, mouseY, filter);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawSearchBar(int mx, int my) {
        int bx = width / 2 - 78;
        int by = 4;
        RenderUtil.drawRoundedRect(bx, by, bx + 156, by + 20, 4, NXColors.BACKGROUND_PANEL);
        RenderUtil.drawRoundedRect(bx, by, bx + 156, by + 20, 4, NXColors.withAlpha(NXColors.ACCENT_PRIMARY, 40));
        searchField.drawTextBox();

        if (searchField.getText().isEmpty() && !searchField.isFocused()) {
            fontRendererObj.drawStringWithShadow("Search modules...", bx + 5, by + 5, NXColors.TEXT_DISABLED);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            return;
        }
        if (searchField.isFocused()) {
            searchField.textboxKeyTyped(typedChar, keyCode);
            return;
        }
        for (CategoryPanel panel : panels) {
            panel.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        int bx = width / 2 - 78, by = 4;
        boolean onSearch = mouseX >= bx && mouseX <= bx + 156 && mouseY >= by && mouseY <= by + 20;
        searchField.setFocused(onSearch);
        if (onSearch) searchField.mouseClicked(mouseX, mouseY, mouseButton);

        String filter = searchField.getText().toLowerCase().trim();
        for (CategoryPanel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton, filter);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (CategoryPanel panel : panels) panel.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scroll = Mouse.getEventDWheel();
        if (scroll != 0) {
            String filter = searchField.getText().toLowerCase().trim();
            for (CategoryPanel panel : panels) panel.mouseScrolled(Mouse.getEventX(), Mouse.getEventY(), scroll > 0 ? 1 : -1, filter);
        }
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
