package mett.palemannie.squakeported.mixin;

import mett.palemannie.squakeported.ISquakeEntity;
import mett.palemannie.squakeported.SquakeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {

    @Unique
    private Player sqe$damagedPlayer;
    @Unique
    private int sqe$damageMotionDeadline;
    @Unique
    private boolean sqe$damageHasKnockback;

    @Redirect(method = "handleDamageEvent", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;handleDamageEvent(Lnet/minecraft/world/damagesource/DamageSource;)V"))
    private void sqe$rememberDamage(Entity entity, DamageSource source) {
        entity.handleDamageEvent(source);
        if (entity == Minecraft.getInstance().player) {
            // Damage without impact does not cause a matching motion packet.
            sqe$damagedPlayer = source.is(DamageTypeTags.NO_IMPACT) ? null : (Player) entity;
            sqe$damageMotionDeadline = entity.tickCount + 10;
            sqe$damageHasKnockback = !source.is(DamageTypeTags.NO_KNOCKBACK);
        }
    }

    @Redirect(method = "handleSetEntityMotion", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;lerpMotion(DDD)V"))
    private void sqe$preserveDamageMomentum(Entity entity, double x, double y, double z) {
        Vec3 movement = new Vec3(x, y, z);
        if (entity == sqe$damagedPlayer) {
            Player player = sqe$damagedPlayer;
            sqe$damagedPlayer = null;
            if (player == Minecraft.getInstance().player && player.tickCount <= sqe$damageMotionDeadline
                    && SquakeConfig.isEnabled() && player.isAlive() && !player.isPassenger()
                    && !player.getAbilities().flying && !player.isFallFlying()
                    && !player.hasEffect(MobEffects.LEVITATION) && !player.onClimbable()
                    && !player.isInLava() && (!player.isInWater() || SquakeConfig.sharkingEnabled)
                    && !((ISquakeEntity) player).shouldReturnMovement_Squake()) {

                // Quake momentum is client-side. Add the damage impulse instead of
                // replacing the complete velocity with the server's stale value.
                Vec3 current = player.getDeltaMovement();
                movement = sqe$damageHasKnockback
                        ? new Vec3(current.x + movement.x, current.y + movement.y, current.z + movement.z)
                        : current;
            }
        }
        entity.lerpMotion(movement.x, movement.y, movement.z);
    }
}