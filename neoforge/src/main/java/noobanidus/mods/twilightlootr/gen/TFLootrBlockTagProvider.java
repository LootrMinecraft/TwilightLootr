package noobanidus.mods.twilightlootr.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import noobanidus.mods.lootr.common.api.LootrTags;
import noobanidus.mods.twilightlootr.TFLootrTags;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.init.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TFLootrBlockTagProvider extends BlockTagsProvider {
  public TFLootrBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, TwilightLootr.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    tag(TFLootrTags.Blocks.BOSS_CHESTS).add(ModBlocks.BOSS_CHEST.get());
    tag(LootrTags.Blocks.CONTAINERS).addTag(TFLootrTags.Blocks.BOSS_CHESTS);
  }

  @Override
  public String getName() {
    return "Twilight Lootr Block Tags";
  }
}
