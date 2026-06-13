package net.nx.client.module.modules.hypixel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.nx.client.NXClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class HypixelAPI {

    private static final String BASE_URL = "https://api.hypixel.net/";
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(3);
    private static final Map<UUID, CachedPlayer> CACHE = new HashMap<>();
    private static final long CACHE_TTL = 5 * 60 * 1000L;
    private static long lastRequest = 0;
    private static final long RATE_LIMIT_MS = 200;

    public static void fetchPlayer(UUID uuid, Consumer<JsonObject> callback) {
        String apiKey = NXClient.getInstance().getConfigManager().getHypixelApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            callback.accept(null);
            return;
        }

        CachedPlayer cached = CACHE.get(uuid);
        if (cached != null && System.currentTimeMillis() - cached.timestamp < CACHE_TTL) {
            callback.accept(cached.data);
            return;
        }

        EXECUTOR.submit(() -> {
            try {
                long now = System.currentTimeMillis();
                long sinceLastReq = now - lastRequest;
                if (sinceLastReq < RATE_LIMIT_MS) {
                    Thread.sleep(RATE_LIMIT_MS - sinceLastReq);
                }
                lastRequest = System.currentTimeMillis();

                URL url = new URL(BASE_URL + "player?uuid=" + uuid.toString() + "&key=" + apiKey);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200) {
                    callback.accept(null);
                    return;
                }

                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line);
                }

                JsonObject root = new JsonParser().parse(sb.toString()).getAsJsonObject();
                if (!root.get("success").getAsBoolean()) {
                    callback.accept(null);
                    return;
                }

                JsonObject player = root.has("player") && !root.get("player").isJsonNull()
                        ? root.getAsJsonObject("player") : null;

                CACHE.put(uuid, new CachedPlayer(player, System.currentTimeMillis()));
                callback.accept(player);
            } catch (Exception e) {
                callback.accept(null);
            }
        });
    }

    public static int getBedwarsStars(JsonObject player) {
        if (player == null) return 0;
        try {
            return player.getAsJsonObject("stats")
                    .getAsJsonObject("Bedwars")
                    .get("Experience").getAsInt() / 487 + 1;
        } catch (Exception e) { return 0; }
    }

    public static int getNetworkLevel(JsonObject player) {
        if (player == null) return 0;
        try {
            double exp = player.get("networkExp").getAsDouble();
            return (int)((Math.sqrt(exp + 15312.5) - 88.38834764831843) / 35.35533905932738) + 1;
        } catch (Exception e) { return 0; }
    }

    public static String getBedwarsPrestigeColor(int stars) {
        if (stars < 100)  return "§7";
        if (stars < 200)  return "§f";
        if (stars < 300)  return "§6";
        if (stars < 400)  return "§b";
        if (stars < 500)  return "§2";
        if (stars < 600)  return "§3";
        if (stars < 700)  return "§4";
        if (stars < 800)  return "§d";
        if (stars < 900)  return "§9";
        if (stars < 1000) return "§5";
        return "§6";
    }

    private static class CachedPlayer {
        final JsonObject data;
        final long timestamp;
        CachedPlayer(JsonObject data, long timestamp) { this.data = data; this.timestamp = timestamp; }
    }
}
