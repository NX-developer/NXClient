package net.nx.client.module.modules.hypixel;

import com.google.gson.JsonObject;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ModeSetting;
import net.nx.client.NXClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BedwarsLevelDisplay extends Module {

    private final ModeSetting displayLocation;
    private final BooleanSetting showInTab;
    private final BooleanSetting showInChat;

    private final Map<UUID, String> starLabels = new HashMap<>();

    public BedwarsLevelDisplay() {
        super("BedWars Stars", "Shows BedWars star level above player heads (Hypixel API)", Category.HYPIXEL);
        this.displayLocation = addSetting(new ModeSetting("Location", "Where to show the level", "Both", "Nametag", "Tab", "Both"));
        this.showInTab  = addSetting(new BooleanSetting("Tab List",  "Show stars in tab list",  true));
        this.showInChat = addSetting(new BooleanSetting("Chat",      "Show stars in chat hover", false));
        setEnabled(false);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); starLabels.clear(); }

    @SubscribeEvent
    public void onPlayerJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        fetchAndCache(event.player);
    }

    public void fetchForPlayer(EntityPlayer player) {
        fetchAndCache(player);
    }

    private void fetchAndCache(EntityPlayer player) {
        if (player == null) return;
        UUID uuid = player.getUniqueID();
        if (starLabels.containsKey(uuid)) return;

        String apiKey = NXClient.getInstance().getConfigManager().getHypixelApiKey();
        if (apiKey == null || apiKey.isEmpty()) return;

        HypixelAPI.fetchPlayer(uuid, (data) -> {
            if (data == null) return;
            int stars = HypixelAPI.getBedwarsStars(data);
            String color = HypixelAPI.getBedwarsPrestigeColor(stars);
            starLabels.put(uuid, color + "[" + stars + "✫]");
        });
    }

    public String getStarLabel(UUID uuid) {
        return starLabels.getOrDefault(uuid, "");
    }

    public boolean showNametag() {
        return displayLocation.is("Nametag") || displayLocation.is("Both");
    }

    public boolean showTab() {
        return (displayLocation.is("Tab") || displayLocation.is("Both")) && showInTab.isEnabled();
    }
}
