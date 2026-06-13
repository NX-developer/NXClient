package net.nx.client.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import net.nx.client.NXClient;
import net.nx.client.module.modules.render.FPSBoost;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "renderFog", at = @At("HEAD"), cancellable = true)
    private void onRenderFog(int startCoords, float partialTicks, CallbackInfo ci) {
        FPSBoost fpsBoost = NXClient.getInstance().getModuleManager().get(FPSBoost.class);
        if (fpsBoost != null && fpsBoost.isEnabled() && fpsBoost.disableFog.isEnabled()) {
            ci.cancel();
        }
    }
}
