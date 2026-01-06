package noobanidus.mods.twilightlootr;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import noobanidus.mods.twilightlootr.init.*;

@Mod(value = TwilightLootr.MODID)
public class TwilightLootr {
  public static final String MODID = "twilight_lootr";

  public static ResourceLocation rl(String path) {
    return ResourceLocation.fromNamespaceAndPath(MODID, path);
  }

  public static TwilightLootr instance;

  public TwilightLootr(ModContainer modContainer, IEventBus modBus) {
    instance = this;
    ModBlockEntities.register(modBus);
    ModBlocks.register(modBus);
    ModEntities.register(modBus);
    ModItems.register(modBus);
    ModAdvancements.register(modBus);
  }
}
