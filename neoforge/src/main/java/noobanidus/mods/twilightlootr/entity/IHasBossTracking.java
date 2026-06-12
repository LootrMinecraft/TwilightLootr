package noobanidus.mods.twilightlootr.entity;

import net.minecraft.world.damagesource.DamageSource;
import org.jetbrains.annotations.Nullable;

public interface IHasBossTracking {
  BossTracking lootr$getBossTracking();

  @Nullable
  DamageSource lootr$getLastDamageSource ();
}
