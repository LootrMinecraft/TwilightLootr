package noobanidus.mods.twilightlootr.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.twilightlootr.TwilightLootr;

public class ModItems {
  private static final DeferredRegister<Item> REGISTER = DeferredRegister.create(BuiltInRegistries.ITEM, TwilightLootr.MODID);

  public static final DeferredHolder<Item, BlockItem> BOSS_CHEST = REGISTER.register("boss_chest", () -> new BlockItem(ModBlocks.BOSS_CHEST.get(), new BlockItem.Properties()));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
