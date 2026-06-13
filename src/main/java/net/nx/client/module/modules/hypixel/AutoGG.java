package net.nx.client.module.modules.hypixel;

import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.nx.client.module.Category;
import net.nx.client.module.Module;
import net.nx.client.module.settings.BooleanSetting;
import net.nx.client.module.settings.ModeSetting;
import net.nx.client.module.settings.SliderSetting;

public class AutoGG extends Module {

    private final ModeSetting    message;
    private final SliderSetting  delay;
    private final BooleanSetting autoWP;

    private long lastSent = 0;

    private static final String[] GAME_END_PATTERNS = {
        "1st Killer", "Bed Destroyed", "You placed", "Game Over!", "WINNER", " won the game"
    };

    public AutoGG() {
        super("Auto GG", "Sends GG message at end of Hypixel games (legal macro)", Category.HYPIXEL);
        this.message = addSetting(new ModeSetting("Message", "Message to send", "GG", "GG", "gg", "Good Game", "GG WP"));
        this.delay   = addSetting(new SliderSetting("Delay", "Delay before sending (ms)", 500, 100, 3000, 100));
        this.autoWP  = addSetting(new BooleanSetting("Auto WP", "Also send WP if won", false));
        setEnabled(false);
    }

    @Override public void onEnable()  { MinecraftForge.EVENT_BUS.register(this); }
    @Override public void onDisable() { MinecraftForge.EVENT_BUS.unregister(this); }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (mc.thePlayer == null) return;
        String text = event.message.getUnformattedText();
        boolean isEnd = false;
        for (String pattern : GAME_END_PATTERNS) {
            if (text.contains(pattern)) { isEnd = true; break; }
        }
        if (!isEnd) return;

        long now = System.currentTimeMillis();
        if (now - lastSent < 5000) return;
        lastSent = now;

        long d = delay.getIntValue();
        new Thread(() -> {
            try { Thread.sleep(d); } catch (InterruptedException ignored) {}
            if (mc.thePlayer != null) {
                mc.thePlayer.sendChatMessage(message.getValue());
            }
        }).start();
    }
}
