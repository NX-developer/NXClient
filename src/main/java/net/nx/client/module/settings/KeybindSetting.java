package net.nx.client.module.settings;

import org.lwjgl.input.Keyboard;

public class KeybindSetting extends Setting<Integer> {

    private boolean binding;

    public KeybindSetting(String name, String description, int defaultKey) {
        super(name, description, defaultKey);
        this.binding = false;
    }

    public String getKeyName() {
        if (value == Keyboard.KEY_NONE) return "None";
        return Keyboard.getKeyName(value);
    }

    public boolean isBinding() { return binding; }
    public void setBinding(boolean binding) { this.binding = binding; }
    public int getKey() { return value; }
}
