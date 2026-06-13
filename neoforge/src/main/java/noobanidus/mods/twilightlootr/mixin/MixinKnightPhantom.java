package noobanidus.mods.twilightlootr.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import noobanidus.mods.twilightlootr.entity.IHasBossLoot;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.KnightPhantom;

import java.util.List;

@Mixin(KnightPhantom.class)
public class MixinKnightPhantom implements IHasBossLoot {
  @Unique
  private final ObjectArrayList<ItemStack> twilightLootr$bossItems = new ObjectArrayList<>();

  @Inject(method="postmortem", at=@At(value="INVOKE", target="Ljava/util/List;forEach(Ljava/util/function/Consumer;)V"))
  private void lootr$KnightPhantomPostMortem (ServerLevel serverLevel, DamageSource cause, CallbackInfo ci, @Local(name = "knights") List<KnightPhantom> knights) {
    if ((Object) this instanceof IHasBossTracking withTracking) {
      for (var kn : knights) {
        ((IHasBossTracking)kn).lootr$getBossTracking().merge(withTracking.lootr$getBossTracking());
      }
    }
  }

  @Inject(method="giveKnightLoot", at=@At(value="HEAD"))
  private static void lootr$giveKnightLoot (KnightPhantom phantom, ObjectArrayList<ItemStack> items, ServerLevel serverLevel, List<Integer> list, Vec3 dropOff, CallbackInfo ci) {
    ((IHasBossLoot)phantom).lootr$getBossUniqueItems().addAll(items);
  }

  @Override
  public ObjectArrayList<ItemStack> lootr$getBossUniqueItems() {
    return twilightLootr$bossItems;
  }
}
