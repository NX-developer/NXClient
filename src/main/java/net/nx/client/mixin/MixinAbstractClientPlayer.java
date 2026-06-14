package net.nx.client.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import net.nx.client.NXClient;
import net.nx.client.cosmetic.CapeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true, require = 0)
    private void onGetCapeLocation(CallbackInfoReturnable<ResourceLocation> cir) {
        AbstractClientPlayer player = (AbstractClientPlayer)(Object)this;
        CapeManager cm = NXClient.getInstance().getCapeManager();
        if (cm != null && cm.hasCape(player)) {
            cir.setReturnValue(cm.getCapeTexture());
        }
    }

    // require = 0: hasCape may not be found if refmap is incomplete; fail silently rather than crash
    @Inject(method = "hasCape", at = @At("HEAD"), cancellable = true, require = 0)
    private void onHasCape(CallbackInfoReturnable<Boolean> cir) {
        AbstractClientPlayer player = (AbstractClientPlayer)(Object)this;
        CapeManager cm = NXClient.getInstance().getCapeManager();
        if (cm != null && cm.hasCape(player)) {
            cir.setReturnValue(true);
        }
    }
}
