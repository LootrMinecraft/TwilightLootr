package noobanidus.mods.twilightlootr.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.lootr.common.api.LootrConstants;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.block.TFLootrBossChestBlock;

public class ModBlocks {
  private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK, TwilightLootr.MODID);

  public static final DeferredHolder<Block, TFLootrBossChestBlock> BOSS_CHEST = REGISTER.register("boss_chest'", () -> new TFLootrBossChestBlock(LootrConstants.CHEST_PROPERTIES));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
