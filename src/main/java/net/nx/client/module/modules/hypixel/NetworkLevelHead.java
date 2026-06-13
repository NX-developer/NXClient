package net.nx.client.module.modules.hypixel;

import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ModeSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NetworkLevelHead extends Module {

    private final ModeSetting displayLocation;
    private final BooleanSetting showBrackets;
    private final Map<UUID, String> levelLabels = new HashMap<>();

    public NetworkLevelHead() {
        super("Network Level", "Shows Hypixel network level above players (API)", Category.HYPIXEL);
        this.displayLocation = addSetting(new ModeSetting("Location", "Display location", "Nametag", "Nametag", "Tab", "Both"));
        this.showBrackets    = addSetting(new BooleanSetting("Brackets", "Wrap level in brackets", true));
        setEnabled(false);
    }

    @Override public void onEnable()  { levelLabels.clear(); }
    @Override public void onDisable() { levelLabels.clear(); }

    public void fetchForPlayer(UUID uuid) {
        if (levelLabels.containsKey(uuid)) return;
        HypixelAPI.fetchPlayer(uuid, (data) -> {
            if (data == null) return;
            int level = HypixelAPI.getNetworkLevel(data);
            String label = showBrackets.isEnabled() ? "§7[§e" + level + "§7]" : "§e" + level;
            levelLabels.put(uuid, label);
        });
    }

    public String getLevelLabel(UUID uuid) {
        return levelLabels.getOrDefault(uuid, "");
    }

    public boolean showNametag() { return displayLocation.is("Nametag") || displayLocation.is("Both"); }
    public boolean showTab()     { return displayLocation.is("Tab")     || displayLocation.is("Both"); }
}
