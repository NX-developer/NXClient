package net.nx.client.mixin;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.nx.client.NXClient;
import net.nx.client.module.modules.hypixel.BedwarsLevelDisplay;
import net.nx.client.module.modules.hypixel.NetworkLevelHead;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(NetworkPlayerInfo.class)
public abstract class MixinNetworkPlayerInfo {

    @Shadow public abstract com.mojang.authlib.GameProfile getGameProfile();

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void onGetDisplayName(CallbackInfoReturnable<IChatComponent> cir) {
        if (NXClient.getInstance() == null) return;
        UUID uuid = getGameProfile().getId();

        BedwarsLevelDisplay bw = NXClient.getInstance().getModuleManager().get(BedwarsLevelDisplay.class);
        NetworkLevelHead nl   = NXClient.getInstance().getModuleManager().get(NetworkLevelHead.class);

        if (bw != null && bw.isEnabled() && bw.showTab()) {
            String star = bw.getStarLabel(uuid);
            if (!star.isEmpty()) {
                IChatComponent original = cir.getReturnValue();
                String name = original != null ? original.getFormattedText() : getGameProfile().getName();
                cir.setReturnValue(new ChatComponentText(star + " " + name));
                return;
            }
        }

        if (nl != null && nl.isEnabled() && nl.showTab()) {
            String level = nl.getLevelLabel(uuid);
            if (!level.isEmpty()) {
                IChatComponent original = cir.getReturnValue();
                String name = original != null ? original.getFormattedText() : getGameProfile().getName();
                cir.setReturnValue(new ChatComponentText(level + " " + name));
            }
        }
    }
}
