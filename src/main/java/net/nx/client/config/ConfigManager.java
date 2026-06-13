package net.nx.client.config;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.nx.client.NXClient;
import net.nx.client.module.Module;
import net.nx.client.module.modules.hud.HUDModule;
import net.nx.client.module.settings.*;

import java.awt.Color;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File configDir;
    private final File configFile;
    private String activeProfile = "default";
    private String hypixelApiKey = "";

    public ConfigManager() {
        configDir  = new File(Minecraft.getMinecraft().mcDataDir, "nxclient");
        configFile = new File(configDir, "config.json");
        if (!configDir.exists()) configDir.mkdirs();
    }

    public void save() {
        try {
            JsonObject root = new JsonObject();
            root.addProperty("version", NXClient.VERSION);
            root.addProperty("profile", activeProfile);
            root.addProperty("hypixelApiKey", hypixelApiKey);

            JsonArray modulesArr = new JsonArray();
            for (Module m : NXClient.getInstance().getModuleManager().getModules()) {
                JsonObject mo = new JsonObject();
                mo.addProperty("name",    m.getName());
                mo.addProperty("enabled", m.isEnabled());
                mo.addProperty("keybind", m.getKeybind());

                if (m instanceof HUDModule) {
                    HUDModule hud = (HUDModule) m;
                    mo.addProperty("posX", hud.getPosX());
                    mo.addProperty("posY", hud.getPosY());
                }

                JsonArray settingsArr = new JsonArray();
                for (Setting<?> s : m.getSettings()) {
                    JsonObject so = new JsonObject();
                    so.addProperty("name", s.getName());
                    serializeSetting(so, s);
                    settingsArr.add(so);
                }
                mo.add("settings", settingsArr);
                modulesArr.add(mo);
            }
            root.add("modules", modulesArr);

            Files.write(configFile.toPath(), GSON.toJson(root).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("[NXClient] Failed to save config: " + e.getMessage());
        }
    }

    public void load() {
        if (!configFile.exists()) { save(); return; }
        try {
            String json = new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_8);
            JsonObject root = new JsonParser().parse(json).getAsJsonObject();

            if (root.has("hypixelApiKey")) hypixelApiKey = root.get("hypixelApiKey").getAsString();
            if (root.has("profile"))       activeProfile = root.get("profile").getAsString();

            if (root.has("modules")) {
                for (JsonElement el : root.getAsJsonArray("modules")) {
                    JsonObject mo = el.getAsJsonObject();
                    String name = mo.get("name").getAsString();
                    Module m = NXClient.getInstance().getModuleManager().getByName(name);
                    if (m == null) continue;

                    m.setEnabled(mo.get("enabled").getAsBoolean());
                    if (mo.has("keybind")) m.setKeybind(mo.get("keybind").getAsInt());

                    if (m instanceof HUDModule && mo.has("posX")) {
                        ((HUDModule) m).setPos(mo.get("posX").getAsDouble(), mo.get("posY").getAsDouble());
                    }

                    if (mo.has("settings")) {
                        for (JsonElement se : mo.getAsJsonArray("settings")) {
                            JsonObject so = se.getAsJsonObject();
                            String sName = so.get("name").getAsString();
                            for (Setting<?> s : m.getSettings()) {
                                if (s.getName().equals(sName)) {
                                    deserializeSetting(so, s);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[NXClient] Failed to load config: " + e.getMessage());
        }
    }

    private void serializeSetting(JsonObject so, Setting<?> s) {
        if (s instanceof BooleanSetting) {
            so.addProperty("value", ((BooleanSetting) s).isEnabled());
        } else if (s instanceof SliderSetting) {
            so.addProperty("value", ((SliderSetting) s).getValue());
        } else if (s instanceof ModeSetting) {
            so.addProperty("value", ((ModeSetting) s).getValue());
        } else if (s instanceof ColorSetting) {
            ColorSetting cs = (ColorSetting) s;
            so.addProperty("r",      cs.getValue().getRed());
            so.addProperty("g",      cs.getValue().getGreen());
            so.addProperty("b",      cs.getValue().getBlue());
            so.addProperty("a",      cs.getValue().getAlpha());
            so.addProperty("chroma", cs.isChromaEnabled());
            so.addProperty("chromaSpeed", cs.getChromaSpeed());
        } else if (s instanceof KeybindSetting) {
            so.addProperty("value", ((KeybindSetting) s).getKey());
        }
    }

    @SuppressWarnings("unchecked")
    private void deserializeSetting(JsonObject so, Setting<?> s) {
        try {
            if (s instanceof BooleanSetting && so.has("value")) {
                ((BooleanSetting) s).setValue(so.get("value").getAsBoolean());
            } else if (s instanceof SliderSetting && so.has("value")) {
                ((SliderSetting) s).setValue(so.get("value").getAsDouble());
            } else if (s instanceof ModeSetting && so.has("value")) {
                ((ModeSetting) s).setValue(so.get("value").getAsString());
            } else if (s instanceof ColorSetting && so.has("r")) {
                ColorSetting cs = (ColorSetting) s;
                cs.setValue(new Color(so.get("r").getAsInt(), so.get("g").getAsInt(),
                        so.get("b").getAsInt(), so.get("a").getAsInt()));
                if (so.has("chroma"))      cs.setChromaEnabled(so.get("chroma").getAsBoolean());
                if (so.has("chromaSpeed")) cs.setChromaSpeed(so.get("chromaSpeed").getAsFloat());
            } else if (s instanceof KeybindSetting && so.has("value")) {
                ((KeybindSetting) s).setValue(so.get("value").getAsInt());
            }
        } catch (Exception ignored) {}
    }

    public String getHypixelApiKey() { return hypixelApiKey; }
    public void setHypixelApiKey(String key) { this.hypixelApiKey = key; save(); }
    public String getActiveProfile() { return activeProfile; }

    public List<String> getProfiles() {
        List<String> profiles = new ArrayList<>();
        File[] files = configDir.listFiles((d, n) -> n.endsWith(".json") && !n.equals("config.json"));
        if (files != null) for (File f : files) profiles.add(f.getName().replace(".json", ""));
        return profiles;
    }
}
