package noobanidus.mods.twilightlootr;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import noobanidus.mods.lootr.common.api.ILootrType;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.twilightlootr.config.ConfigManager;
import noobanidus.mods.twilightlootr.init.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = TwilightLootr.MODID)
public class TwilightLootr {
  public static final Logger LOG = LogManager.getLogger();
  public static final String MODID = "twilight_lootr";
  public static final String TYPE_NAME = rl("boss_chest").toString();
  public static final ResourceKey<LootTable> PLACEHOLDER = ResourceKey.create(Registries.LOOT_TABLE, rl("placeholder"));
  public static ILootrType TYPE;

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(MODID, path);
  }

  public static TwilightLootr instance;

  public TwilightLootr(ModContainer modContainer, IEventBus modBus) {
    instance = this;
    modContainer.registerConfig(ModConfig.Type.COMMON, ConfigManager.COMMON_CONFIG);
    ModBlockEntities.register(modBus);
    ModBlocks.register(modBus);
    ModEntities.register(modBus);
    ModItems.register(modBus);
    ModAdvancements.register(modBus);
  }
}
