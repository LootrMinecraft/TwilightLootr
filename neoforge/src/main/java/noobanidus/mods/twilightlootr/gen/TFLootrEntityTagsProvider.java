package noobanidus.mods.twilightlootr.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import noobanidus.mods.twilightlootr.TwilightLootr;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TFLootrEntityTagsProvider extends EntityTypeTagsProvider {
  public TFLootrEntityTagsProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, completableFuture, TwilightLootr.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
  }

  @Override
  public String getName() {
    return "Twilight Lootr Entity Type Tags";
  }
}
