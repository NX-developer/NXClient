package net.nx.client.module.settings;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    public boolean isEnabled() { return value; }
    public void toggle() { value = !value; }
}
