package noobanidus.mods.twilightlootr.gen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import noobanidus.mods.lootr.common.advancement.ContainerTrigger;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.init.ModAdvancements;
import noobanidus.mods.twilightlootr.init.ModBlocks;

import java.util.function.Consumer;

public class TFLootrAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
  @Override
  public void generate(HolderLookup.Provider arg, Consumer<AdvancementHolder> consumer, ExistingFileHelper existingFileHelper) {
    var id = TwilightLootr.rl("boss_chest");

    // Shame on me
    var parent = new AdvancementHolder(LootrAPI.rl("root"), null);

    var advancement = Advancement.Builder.advancement().parent(parent)
        .display(ModBlocks.BOSS_CHEST.get(), Component.translatable("twilight_lootr.advancements.1boss_chest.title"), Component.translatable("twilight_lootr.advancements.1boss_chest.description"), null, AdvancementType.TASK, true, true, false)
        .addCriterion("opened_boss_chest", ContainerTrigger.looted(ModAdvancements.BOSS_CHEST.get())).build(id);
    consumer.accept(advancement);
    existingFileHelper.trackGenerated(id, PackType.SERVER_DATA, ".json", "advancement");
  }
}
