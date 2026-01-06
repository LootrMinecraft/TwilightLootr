package noobanidus.mods.twilightlootr.mixin;

import net.minecraft.nbt.CompoundTag;
import noobanidus.mods.twilightlootr.entity.BossTracking;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.BaseTFBoss;

@Mixin(BaseTFBoss.class)
public class MixinTFBossEntity implements IHasBossTracking {
  @Unique
  private BossTracking lootr$bossTracking;

  @Override
  public BossTracking lootr$GetBossTracking() {
    if (lootr$bossTracking == null) {
      lootr$bossTracking = new BossTracking();
    }
    return lootr$bossTracking;
  }

  @Inject(method="addAdditionalSaveData", at=@At("TAIL"))
  private void lootr$saveBossTrackingData(CompoundTag compound, CallbackInfo callbackInfo) {
    lootr$GetBossTracking().save(compound);
  }

  @Inject(method="readAdditionalSaveData", at=@At("TAIL"))
  private void lootr$loadBossTrackingData(CompoundTag compound, CallbackInfo callbackInfo) {
    this.lootr$bossTracking = BossTracking.load(compound);
  }
}
