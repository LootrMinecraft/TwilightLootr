package noobanidus.mods.twilightlootr.impl;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.data.ILootrInfoProvider;
import noobanidus.mods.lootr.common.api.data.LootFiller;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.boss.BaseTFBoss;

public class TFLootFiller implements LootFiller {
  private final BaseTFBoss boss;

  public TFLootFiller(BaseTFBoss boss) {
    this.boss = boss;
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
}
