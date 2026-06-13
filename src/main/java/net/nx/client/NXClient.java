package net.nx.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.nx.client.config.ConfigManager;
import net.nx.client.cosmetic.CapeManager;
import net.nx.client.event.EventManager;
import net.nx.client.module.ModuleManager;
import net.nx.client.ui.NXColors;

@Mod(modid = NXClient.MODID, name = NXClient.NAME, version = NXClient.VERSION, acceptedMinecraftVersions = "[1.8.9]")
public class NXClient {

    public static final String MODID = "nxclient";
    public static final String NAME = "NX Client";
    public static final String VERSION = "1.0.0";

    private static NXClient instance;

    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private EventManager eventManager;
    private CapeManager capeManager;

    public static NXClient getInstance() {
        return instance;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NXColors.init();

        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        configManager = new ConfigManager();
        capeManager = new CapeManager();

        moduleManager.registerAll();
        configManager.load();

        MinecraftForge.EVENT_BUS.register(eventManager);
        MinecraftForge.EVENT_BUS.register(moduleManager);
        MinecraftForge.EVENT_BUS.register(capeManager);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> configManager.save()));
    }

    public ModuleManager getModuleManager() { return moduleManager; }
    public ConfigManager getConfigManager() { return configManager; }
    public EventManager getEventManager() { return eventManager; }
    public CapeManager getCapeManager() { return capeManager; }

    public static Minecraft mc() { return Minecraft.getMinecraft(); }
}
