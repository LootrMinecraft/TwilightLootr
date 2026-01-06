package noobanidus.mods.twilightlootr.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.IBossLootBuffer;

import java.util.List;
import java.util.UUID;

@Mixin(IBossLootBuffer.class)
public interface MixinIBossLootBuffer {
  @Inject(method = "depositDropsIntoChest", at=@At("HEAD"), cancellable = true)
  private static <T extends LivingEntity & IBossLootBuffer> void lootr$depositDropsIntoChest(T boss, BlockState chest, BlockPos pos, ServerLevel serverLevel, CallbackInfo ci) {
    if ((boss instanceof IHasBossTracking bossWithTracking)) {
      ci.cancel();

      var tracking = bossWithTracking.lootr$GetBossTracking();
      List<UUID> eligiblesPlayers = tracking.eligiblePlayers(serverLevel);

      ResourceKey<LootTable> table = boss.getLootTable();
      boss.getLootTableSeed()
    }
  }
}
