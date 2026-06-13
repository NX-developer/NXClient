package net.nx.client.mixin;

import net.minecraft.client.renderer.entity.RenderManager;
import net.nx.client.NXClient;
import net.nx.client.module.modules.render.NametagTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderManager.class)
public class MixinRenderManager {

    @ModifyVariable(method = "renderEntityLabel", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double modifyNametagY(double y) {
        NametagTweaks nt = NXClient.getInstance().getModuleManager().get(NametagTweaks.class);
        if (nt != null && nt.isEnabled()) {
            return y * nt.nametagScale.getValue();
        }
        return y;
    }
}
