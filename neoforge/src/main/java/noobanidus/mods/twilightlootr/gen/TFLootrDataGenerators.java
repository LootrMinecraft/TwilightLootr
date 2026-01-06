package noobanidus.mods.twilightlootr.gen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import noobanidus.mods.twilightlootr.TwilightLootr;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TwilightLootr.MODID)
public class TFLootrDataGenerators {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    if (!event.getMods().contains(TwilightLootr.MODID)) {
      return;
    }
    DataGenerator generator = event.getGenerator();
    PackOutput output = event.getGenerator().getPackOutput();
    CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
    ExistingFileHelper helper = event.getExistingFileHelper();

    TFLootrBlockTagProvider blocks;
    generator.addProvider(event.includeServer(), blocks = new TFLootrBlockTagProvider(output, provider, helper));
    generator.addProvider(event.includeServer(), new TFLootrItemTagsProvider(output, provider, blocks.contentsGetter(), helper));
    generator.addProvider(event.includeClient(), new TFLootrAtlasGenerator(output, provider, helper));
    generator.addProvider(event.includeServer(), new TFLootrEntityTagsProvider(output, provider, helper));
    generator.addProvider(event.includeClient(), new TFLootrLangProvider(output));
    generator.addProvider(event.includeServer(), new AdvancementProvider(output, provider, helper, List.of(new TFLootrAdvancementGenerator())));
  }
}
