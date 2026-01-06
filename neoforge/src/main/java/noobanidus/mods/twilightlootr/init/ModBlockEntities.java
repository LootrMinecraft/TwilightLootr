package noobanidus.mods.twilightlootr.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.block.entity.TFLootrBossChestBlockEntity;

@SuppressWarnings("DataFlowIssue")
public class ModBlockEntities {
  private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TwilightLootr.MODID);

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }

  public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TFLootrBossChestBlockEntity>> BOSS_CHEST = REGISTER.register("boss_chest", () -> BlockEntityType.Builder.of(TFLootrBossChestBlockEntity::new, ModBlocks.BOSS_CHEST.get())
      .build(null));
}
