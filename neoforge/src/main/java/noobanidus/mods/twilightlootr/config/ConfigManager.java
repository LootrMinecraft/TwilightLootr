package noobanidus.mods.twilightlootr.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigManager {
  public static final ModConfigSpec.BooleanValue USE_STATIC_LOOT;
  public static final ModConfigSpec.BooleanValue ENABLE_DECAY;
  public static final ModConfigSpec.IntValue DECAY_TIME;

  private static final ModConfigSpec.Builder COMMON_BUILDER = new ModConfigSpec.Builder();
  public static ModConfigSpec COMMON_CONFIG;

  static {
    COMMON_BUILDER.push("general");
    USE_STATIC_LOOT = COMMON_BUILDER
        .comment("if true, boss chests will generate loot from the boss's loot table and each eligible player will get a copy. if false, boss chests will generate loot individually for each player. [default false]")
        .define("use_static_loot", false);
    ENABLE_DECAY = COMMON_BUILDER
        .comment("if true, boss chests will decay after a set amount of time from the boss dying [default false]")
        .define("enable_decay", true);
    DECAY_TIME = COMMON_BUILDER
        .comment("the time in ticks before a boss chest decays (if decay is enabled) [default 3 minutes]")
        .defineInRange("decay_time", 60 * 20 * 3, 0, Integer.MAX_VALUE);
    COMMON_BUILDER.pop();
    COMMON_CONFIG = COMMON_BUILDER.build();
  }
}
