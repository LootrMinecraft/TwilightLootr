package noobanidus.mods.twilightlootr.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.twilightlootr.block.entity.TFLootrBossChestBlockEntity;
import noobanidus.mods.twilightlootr.config.ConfigManager;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import noobanidus.mods.twilightlootr.init.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.IBossLootBuffer;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.loot.TFLootTables;

import java.util.List;
import java.util.UUID;

@Mixin(IBossLootBuffer.class)
public interface MixinIBossLootBuffer {
  // This is technically an overwrite
  @Inject(method = "depositDropsIntoChest", at = @At("HEAD"), cancellable = true)
  private static <T extends LivingEntity & IBossLootBuffer> void lootr$depositDropsIntoChest(T boss, BlockState incomingChest, BlockPos pos, ServerLevel serverLevel, CallbackInfo ci) {
    if (boss instanceof IHasBossTracking bossWithTracking) {
      var tracking = bossWithTracking.lootr$GetBossTracking();
      List<UUID> eligiblesPlayers = tracking.eligiblePlayers(serverLevel);

      BlockState newChest = ModBlocks.BOSS_CHEST.get().defaultBlockState()
          .setValue(ChestBlock.FACING, incomingChest.getValue(ChestBlock.FACING));

      if (eligiblesPlayers.isEmpty()) {
        // TODO: I guess this can happen when killed with a command?
        return;
      }

      boolean placed = lootr$createChest(newChest, pos, serverLevel);
      if (!placed) {
        BlockPos.MutableBlockPos chestPos = pos.mutable();
        for (int y = pos.getY(); y < serverLevel.getMaxBuildHeight(); y++) {
          chestPos.setY(y);
          if (lootr$createChest(newChest, chestPos, serverLevel)) {
            placed = true;
            pos = chestPos.immutable();
            break;
          }
        }
      }

      if (!placed) {
        // TODO: Couldn't place the chest somehow so just... do nothing?
        return;
      }

      if (!(LootrAPI.resolveBlockEntity(serverLevel.getBlockEntity(pos)) instanceof TFLootrBossChestBlockEntity tbe)) {
        // The block isn't the block
        return;
      }

      ci.cancel();

      tbe.setEligiblePlayers(eligiblesPlayers);

/*      if (ConfigManager.USE_STATIC_LOOT.get()) {
        NonNullList<ItemStack> stacks = NonNullList.withSize(tbe.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
          if (i >= tbe.getContainerSize()) {
            break;
          }
          stacks.set(i, boss.getItemStacks().get(i).copy());
        }
        tbe.setCustomInventory(stacks);
      } else {*/

      if (boss instanceof KnightPhantom phantom) {
        // TODO: Combine both loot tables
        tbe.setLootTable(TFLootTables.KNIGHT_PHANTOM_DEFEATED);
      } else {
        tbe.setLootTable(boss.getLootTable());
      }

      if (ConfigManager.ENABLE_DECAY.get()) {
        tbe.setDecaying(ConfigManager.DECAY_TIME.get());
      }

      IBossLootBuffer.celebrateAt(boss, pos.getCenter(), serverLevel);
    }
  }

  // Logic here copied from `tryDeposit`
  @Unique
  private static boolean lootr$createChest(BlockState chest, BlockPos pos, ServerLevel serverLevel) {
    return ((serverLevel.getBlockState(pos).is(chest.getBlock()) ||
        ((serverLevel.getBlockState(pos).canBeReplaced() || serverLevel.getBlockState(pos)
            .getPistonPushReaction() != PushReaction.BLOCK) && serverLevel.getBlockEntity(pos) == null && serverLevel.setBlock(pos, chest, TFLootTables.DEFAULT_PLACE_FLAG))) &&
        serverLevel.getBlockState(pos).is(chest.getBlock()));
  }
}
