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
    add("twilight_lootr.advancements.1boss_chest.description", "Open a Twilight Lootr boss chest!");
    add("twilight_lootr.boss_chest.ineligible", "You are not eligible to open this container.");

    add("twilight_lootr.configuration.decay_time", "Decay Time");
    add("twilight_lootr.configuration.enable_decay", "Enable Decay");
    add("twilight_lootr.configuration.general", "General");
    add("twilight_lootr.configuration.decay_time.tooltip", "The time (in ticks) from when a boss chest is spawn until it decays.");
    add("twilight_lootr.configuration.enable_decay.tooltip", "When enabled, causes boss chests to start decaying as soon as they are spawned. After the decay time elapses, they will be destroyed.");
    add("twilight_lootr.configuration.title", "Twilight Lootr");
  }
}
