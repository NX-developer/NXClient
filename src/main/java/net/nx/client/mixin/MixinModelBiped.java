package net.nx.client.mixin;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.nx.client.NXClient;
import net.nx.client.module.modules.animation.OldAnimations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBiped.class)
public class MixinModelBiped {

    @Shadow public net.minecraft.client.model.ModelRenderer bipedRightArm;
    @Shadow public net.minecraft.client.model.ModelRenderer bipedLeftArm;

    @Inject(method = "setRotationAngles", at = @At("TAIL"))
    private void onSetAngles(float limbSwing, float limbSwingAmount, float ageInTicks,
                             float netHeadYaw, float headPitch, float scaleFactor,
                             EntityLivingBase entity, CallbackInfo ci) {
        OldAnimations anim = NXClient.getInstance().getModuleManager().get(OldAnimations.class);
        if (anim == null || !anim.isEnabled()) return;

        ItemStack held = entity.getHeldItem();
        if (held == null) return;

        EnumAction action = held.getItemUseAction();

        if (anim.oldBlocking.isEnabled() && action == EnumAction.BLOCK) {
            bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5f - 0.9424779f;
            bipedRightArm.rotateAngleY = -0.2617994f;
        }

        if (anim.oldBow.isEnabled() && action == EnumAction.BOW) {
            bipedRightArm.rotateAngleX = -1.5707964f + bipedRightArm.rotateAngleX * 0.1f;
            bipedLeftArm.rotateAngleX  = -1.5707964f + bipedLeftArm.rotateAngleX  * 0.1f;
        }
    }
}
