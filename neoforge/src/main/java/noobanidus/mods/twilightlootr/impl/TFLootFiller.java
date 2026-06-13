package noobanidus.mods.twilightlootr.impl;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.data.DefaultLootFiller;
import noobanidus.mods.lootr.common.api.data.ILootrInfoProvider;
import noobanidus.mods.lootr.common.api.data.LootFiller;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import noobanidus.mods.twilightlootr.mixin.AccessorMixinLootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.boss.BaseTFBoss;

import java.util.List;

public class TFLootFiller implements LootFiller {
  private final BaseTFBoss boss;
  private final ObjectArrayList<ItemStack> bossItems;

  public TFLootFiller(BaseTFBoss boss, ObjectArrayList<ItemStack> bossUniqueItems) {
    this.boss = boss;
    this.bossItems = bossUniqueItems;
  }

  private LootParams createLootParamsFor(@Nullable ServerPlayer player, ServerLevel level) {
    DamageSource existing = ((IHasBossTracking) boss).lootr$getLastDamageSource();
    if (existing == null) {
      // `playerAttack` isn't marked @Nullable but simply passes the parameter into the constructor that *is* @Nullable
      //noinspection DataFlowIssue
      existing = level.damageSources().playerAttack(player);
    }

    DamageSource newSource = new DamageSource(existing.typeHolder(), player, player, existing.sourcePositionRaw());

    // TODO: Check origin
    LootParams.Builder lootcontext$builder = (new LootParams.Builder(level).withParameter(LootContextParams.THIS_ENTITY, boss)
        .withParameter(LootContextParams.ORIGIN, boss.position())
        .withParameter(LootContextParams.DAMAGE_SOURCE, newSource)
        .withOptionalParameter(LootContextParams.ATTACKING_ENTITY, player)
        .withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, player));
    if (player != null) {
      lootcontext$builder = lootcontext$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
          .withLuck(player.getLuck());
    }

    return lootcontext$builder.create(LootContextParamSets.ENTITY);
  }

  @Override
  public void unpackLootTable(@NotNull ILootrInfoProvider provider, @Nullable Player player, Container inventory) {
    if (!(provider.getInfoLevel() instanceof ServerLevel level)) {
      TwilightLootr.LOG.error("Provider {} isn't from a server!?", provider);
      return;
    }
    if (player != null && !(player instanceof ServerPlayer)) {
      TwilightLootr.LOG.error("Player {} isn't a server player?!", player);
      return;
    }
    LootParams params = createLootParamsFor((ServerPlayer) player, level);
    ResourceKey<LootTable> lootTable = boss.getLootTable();
    LootTable table = level.getServer().reloadableRegistries().getLootTable(lootTable);

    if (table == LootTable.EMPTY) {
      LootrAPI.LOG.error("Unable to fill loot container in {} at {} as the loot table '{}' couldn't be resolved! Please search the loot table in `latest.log` to see if there are errors in loading.", level.dimension()
          .location(), provider.getInfoPos(), lootTable);
      if (LootrAPI.reportUnresolvedTables()) {
        player.displayClientMessage(LootrAPI.getInvalidTableComponent(lootTable), false);
      }
      return;
    }

    long seed = LootrAPI.getLootSeed(boss.getLootTableSeed());
    this.fill(provider, player, lootTable, table, inventory, params, seed);
  }

  @Override
  public void fill(ILootrInfoProvider provider, Player player, ResourceKey<LootTable> lootTableKey, LootTable lootTable, Container container, LootParams parameters, long seed) {
    DefaultLootFiller.setFillerState(new LootFillerState(provider, player, lootTableKey, lootTable, container, parameters, seed));
    ObjectArrayList<ItemStack> objectarraylist = new ObjectArrayList<>();
    for (ItemStack item : bossItems) {
      objectarraylist.add(item.copy());
    }
    objectarraylist.addAll(lootTable.getRandomItems(parameters));
    RandomSource randomSource = RandomSource.create(seed);
    List<Integer> list = ((AccessorMixinLootTable)lootTable).lootr$getAvailableSlots(container, randomSource);
    ((AccessorMixinLootTable)lootTable).lootr$shuffleAndSplitItems(objectarraylist, list.size(), randomSource);

    for (ItemStack itemstack : objectarraylist) {
      if (list.isEmpty()) {
        TwilightLootr.LOG.warn("Tried to over-fill a container");
        return;
      }

      if (itemstack.isEmpty()) {
        container.setItem(list.removeLast(), ItemStack.EMPTY);
      } else {
        container.setItem(list.removeLast(), itemstack);
      }
    }

    DefaultLootFiller.setFillerState(null);
  }
}
