package net.nx.client.module;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.nx.client.module.modules.animation.OldAnimations;
import net.nx.client.module.modules.hud.*;
import net.nx.client.module.modules.hypixel.AutoGG;
import net.nx.client.module.modules.hypixel.BedwarsLevelDisplay;
import net.nx.client.module.modules.hypixel.NetworkLevelHead;
import net.nx.client.module.modules.render.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public void registerAll() {
        register(new FPSBoost());
        register(new OldAnimations());
        register(new Zoom());
        register(new Fullbright());
        register(new CustomCrosshair());
        register(new MotionBlur());
        register(new BlockOverlay());
        register(new NametagTweaks());
        register(new ChromaUI());
        register(new FPSDisplay());
        register(new CPSCounter());
        register(new Keystrokes());
        register(new Coordinates());
        register(new ArmorHUD());
        register(new PotionHUD());
        register(new PingDisplay());
        register(new ReachDisplay());
        register(new ComboCounter());
        register(new ClockHUD());
        register(new ScoreboardTweaks());
        register(new BedwarsLevelDisplay());
        register(new NetworkLevelHead());
        register(new AutoGG());
    }

    private void register(Module module) {
        modules.add(module);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        for (Module m : modules) {
            if (m.isEnabled()) m.onUpdate();
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        for (Module m : modules) {
            if (m.getKeybind() != org.lwjgl.input.Keyboard.KEY_NONE
                    && org.lwjgl.input.Keyboard.isKeyDown(m.getKeybind())) {
                m.toggle();
            }
        }
    }

    public Module getByName(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public <T extends Module> T get(Class<T> clazz) {
        return modules.stream()
                .filter(m -> m.getClass() == clazz)
                .map(clazz::cast)
                .findFirst()
                .orElse(null);
    }

    public List<Module> getByCategory(Category category) {
        return modules.stream().filter(m -> m.getCategory() == category).collect(Collectors.toList());
    }

    public List<Module> getModules() { return modules; }
}
