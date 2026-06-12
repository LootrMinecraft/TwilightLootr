package noobanidus.mods.twilightlootr.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import noobanidus.mods.twilightlootr.TwilightLootr;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class TFLootrLootTableProvider {
  public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
    return new LootTableProvider(output, Set.of(TwilightLootr.PLACEHOLDER), List.of(new LootTableProvider.SubProviderEntry(PlaceholderLootTable::new, LootContextParamSets.CHEST)), provider);
  }

  public static class PlaceholderLootTable implements LootTableSubProvider {
    private final HolderLookup.Provider provider;

    public PlaceholderLootTable(HolderLookup.Provider provider) {
      this.provider = provider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
      output.accept(TwilightLootr.PLACEHOLDER, LootTable.lootTable());
    }
  }
}
