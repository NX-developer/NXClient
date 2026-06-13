package net.nx.client.module.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<String> {

    private final List<String> modes;
    private int currentIndex;

    public ModeSetting(String name, String description, String defaultValue, String... modes) {
        super(name, description, defaultValue);
        this.modes = Arrays.asList(modes);
        this.currentIndex = this.modes.indexOf(defaultValue);
        if (this.currentIndex < 0) this.currentIndex = 0;
    }

    public void cycle() {
        currentIndex = (currentIndex + 1) % modes.size();
        value = modes.get(currentIndex);
    }

    public void cycleBack() {
        currentIndex = (currentIndex - 1 + modes.size()) % modes.size();
        value = modes.get(currentIndex);
    }

    @Override
    public void setValue(String value) {
        int idx = modes.indexOf(value);
        if (idx >= 0) {
            this.currentIndex = idx;
            this.value = value;
        }
    }

    public List<String> getModes() { return modes; }
    public int getCurrentIndex() { return currentIndex; }
    public boolean is(String mode) { return value.equalsIgnoreCase(mode); }
}
