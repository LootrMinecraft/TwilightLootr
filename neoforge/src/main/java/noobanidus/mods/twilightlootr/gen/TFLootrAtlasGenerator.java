package noobanidus.mods.twilightlootr.gen;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.client.block.TFLootrBossChestBlockRenderer;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TFLootrAtlasGenerator extends SpriteSourceProvider {
  public TFLootrAtlasGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
    super(output, lookupProvider, TwilightLootr.MODID, fileHelper);
  }

  @Override
  protected void gather() {
    this.atlas(CHESTS_ATLAS)
        .addSource(new SingleFile(TFLootrBossChestBlockRenderer.MATERIAL.texture(), Optional.empty()))
        .addSource(new SingleFile(TFLootrBossChestBlockRenderer.MATERIAL2.texture(), Optional.empty()))
        .addSource(new SingleFile(TFLootrBossChestBlockRenderer.MATERIAL3.texture(), Optional.empty()));
  }
}
