package mett.palemannie.squakeported.mixin;

import mett.palemannie.squakeported.ISquakeEntity;
import mett.palemannie.squakeported.SquakeClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements ISquakeEntity {

    @Shadow protected boolean jumping;

    @Unique
    public boolean getJumping(){
        return this.jumping;
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    public void afterJump(CallbackInfo ci) {
        if ((Object) this instanceof Player asPlayer) {
            SquakeClientPlayer.afterJump(asPlayer);
        }
    }

    /*protected LivingEntityMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super();
    }*/
}