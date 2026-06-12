package noobanidus.mods.twilightlootr.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.KnightPhantom;

import java.util.List;

@Mixin(KnightPhantom.class)
public class MixinKnightPhantom {
  @Inject(method="postmortem", at=@At(value="INVOKE", target="Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"))
  private void lootr$KnightPhantomPostMortem (ServerLevel serverLevel, DamageSource cause, CallbackInfo ci, @Local(name = "knights") List<KnightPhantom> phantoms) {
    if ((Object) this instanceof IHasBossTracking withTracking) {
      for (var kn : phantoms) {
        ((IHasBossTracking)kn).lootr$getBossTracking().merge(withTracking.lootr$getBossTracking());
      }
    }
  }
}
