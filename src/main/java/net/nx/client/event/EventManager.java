package net.nx.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.nx.client.NXClient;
import net.nx.client.gui.clickgui.ClickGUI;
import net.nx.client.gui.hud.HUDEditor;
import net.nx.client.gui.mainmenu.CustomMainMenu;
import net.nx.client.module.modules.hud.HUDModule;
import net.nx.client.module.Module;
import org.lwjgl.input.Keyboard;

public class EventManager {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_RSHIFT) {
                if (mc.currentScreen == null) {
                    mc.displayGuiScreen(new ClickGUI());
                } else if (mc.currentScreen instanceof ClickGUI) {
                    mc.displayGuiScreen(null);
                }
            }
            if (key == Keyboard.KEY_F8 && mc.currentScreen == null) {
                mc.displayGuiScreen(new HUDEditor());
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiMainMenu) {
            event.gui = new CustomMainMenu();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (mc.currentScreen != null) return;

        for (Module m : NXClient.getInstance().getModuleManager().getModules()) {
            if (m instanceof HUDModule && m.isEnabled()) {
                ((HUDModule) m).renderHUD(event.resolution);
            }
        }
    }
}
