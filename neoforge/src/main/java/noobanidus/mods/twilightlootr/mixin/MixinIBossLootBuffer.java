package noobanidus.mods.twilightlootr.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.block.entity.TFLootrBossChestBlockEntity;
import noobanidus.mods.twilightlootr.config.ConfigManager;
import noobanidus.mods.twilightlootr.entity.BossTracking;
import noobanidus.mods.twilightlootr.entity.IHasBossLoot;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import noobanidus.mods.twilightlootr.impl.TFLootFiller;
import noobanidus.mods.twilightlootr.init.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.entity.boss.BaseTFBoss;
import twilightforest.entity.boss.IBossLootBuffer;
import twilightforest.loot.TFLootTables;

import java.util.List;
import java.util.UUID;

@Mixin(IBossLootBuffer.class)
public interface MixinIBossLootBuffer {
  // This is technically an overwrite
  @Inject(method = "depositDropsIntoChest", at = @At("HEAD"), cancellable = true)
  private static <T extends LivingEntity & IBossLootBuffer> void lootr$depositDropsIntoChest(T boss, BlockState incomingChest, BlockPos pos, ServerLevel serverLevel, CallbackInfo ci) {
    if (boss instanceof IHasBossTracking bossWithTracking) {
      var tracking = bossWithTracking.lootr$getBossTracking();

      // Additionally track players nearby who died as they are
      // eligible for he "Structure completed" achievement
      tracking.trackPlayers(serverLevel, pos);

      List<UUID> eligiblePlayers = tracking.eligiblePlayers(serverLevel);
      List<BossTracking.PlayerEntry> playerList = tracking.eligiblePlayersList(serverLevel);

      BlockState newChest = ModBlocks.BOSS_CHEST.get().defaultBlockState()
          .setValue(ChestBlock.FACING, incomingChest.getValue(ChestBlock.FACING));

      if (eligiblePlayers.isEmpty()) {
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

      tbe.setEligiblePlayers(eligiblePlayers);

      ObjectArrayList<ItemStack> bossUniqueItems = ((boss instanceof IHasBossLoot hasBossLoot) ? hasBossLoot.lootr$getBossUniqueItems() : new ObjectArrayList<>());

      TFLootFiller filler = new TFLootFiller((BaseTFBoss) boss, bossUniqueItems);

      var data = LootrAPI.getData(tbe);
      if (data == null) {
        return;
      }

      for (BossTracking.PlayerEntry entry : playerList) {
        var inventory = data.createInventory(tbe, entry.id(), filler);
      }

      tbe.setLootTable(TwilightLootr.PLACEHOLDER);

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
