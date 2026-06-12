package noobanidus.mods.twilightlootr.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import noobanidus.mods.twilightlootr.entity.BossTracking;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.BaseTFBoss;

@Mixin(BaseTFBoss.class)
public class MixinBaseTFBoss implements IHasBossTracking {
  @Unique
  private BossTracking lootr$bossTracking;

  @Unique
  private DamageSource lootr$lastDamageSource;

  @Override
  public BossTracking lootr$getBossTracking() {
    if (lootr$bossTracking == null) {
      lootr$bossTracking = new BossTracking();
    }
    return lootr$bossTracking;
  }

  @Override
  public @Nullable DamageSource lootr$getLastDamageSource() {
    return lootr$lastDamageSource;
  }

  @Inject(method="addAdditionalSaveData", at=@At("TAIL"))
  private void lootr$saveBossTrackingData(CompoundTag compound, CallbackInfo callbackInfo) {
    lootr$getBossTracking().save(compound);
  }

  @Inject(method="readAdditionalSaveData", at=@At("TAIL"))
  private void lootr$loadBossTrackingData(CompoundTag compound, CallbackInfo callbackInfo) {
    this.lootr$bossTracking = BossTracking.load(compound);
  }

  @Inject(method="postmortem", at=@At("HEAD"))
  private void lootr$saveDamageSource (ServerLevel serverLevel, DamageSource cause, CallbackInfo ci) {
    this.lootr$lastDamageSource = cause;
  }
}
