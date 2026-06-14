package net.nx.client.mixin;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;

// CustomMainMenu is opened via EventManager.onGuiOpen — no injection needed here.
@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
}
