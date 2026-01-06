package noobanidus.mods.twilightlootr.gen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.init.ModBlocks;

public class TFLootrLangProvider extends LanguageProvider {
  public TFLootrLangProvider(PackOutput output) {
    super(output, TwilightLootr.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    add(ModBlocks.BOSS_CHEST.get(), "Boss Chest");
    add("twilight_lootr.advancements.1boss_chest.title", "Bossing Around");
    // TODO:
    add("twilight_lootr.advancements.1boss_chest.description", "Open ???");
    add("twilight_lootr.boss_chest.ineligible", "You are not eligible to open this container.");
  }
}
