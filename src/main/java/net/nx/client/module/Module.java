package net.nx.client.module;

import net.minecraft.client.Minecraft;
import net.nx.client.module.settings.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private final String description;
    private final Category category;
    private int keybind;
    private boolean enabled;
    private final List<Setting<?>> settings = new ArrayList<>();

    public Module(String name, String description, Category category, int keybind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keybind = keybind;
        this.enabled = false;
    }

    public Module(String name, String description, Category category) {
        this(name, description, category, Keyboard.KEY_NONE);
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onUpdate() {}

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable(); else onDisable();
    }

    protected <T extends Setting<?>> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }
    public boolean isEnabled() { return enabled; }
    public List<Setting<?>> getSettings() { return settings; }
}
