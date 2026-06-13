package net.nx.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.nx.client.NXClient;
import net.nx.client.module.modules.animation.OldAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Shadow private float prevEquippedProgress;
    @Shadow private float equippedProgress;
    @Shadow private ItemStack itemToRender;

    @Inject(method = "renderItemInFirstPerson", at = @At("HEAD"), cancellable = true)
    private void onRenderItem(CallbackInfo ci) {
        OldAnimations anim = NXClient.getInstance().getModuleManager().get(OldAnimations.class);
        if (anim == null || !anim.isEnabled()) return;

        if (anim.oldSwing.isEnabled()) {
            float speed = anim.swingSpeed.getValue().floatValue();
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer != null) {
                mc.thePlayer.swingProgressInt = (int)(mc.thePlayer.swingProgressInt * speed);
            }
        }
    }
}
