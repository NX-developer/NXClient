package net.nx.client.cosmetic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

public class CapeManager {

    private static final ResourceLocation CAPE_TEXTURE =
            new ResourceLocation("nxclient", "textures/cosmetic/cape.png");

    private final Set<String> nxUsers = new HashSet<>();
    private final Minecraft mc = Minecraft.getMinecraft();

    public CapeManager() {
        if (mc.getSession() != null && mc.getSession().getUsername() != null) {
            nxUsers.add(mc.getSession().getUsername());
        }
    }

    public boolean isNXUser(String username) {
        return nxUsers.contains(username);
    }

    public void registerNXUser(String username) {
        nxUsers.add(username);
    }

    public ResourceLocation getCapeTexture() {
        return CAPE_TEXTURE;
    }

    public boolean hasCape(AbstractClientPlayer player) {
        String name = player.getGameProfile().getName();
        return isNXUser(name);
    }
}
