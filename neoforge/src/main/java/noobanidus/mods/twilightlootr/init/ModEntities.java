package noobanidus.mods.twilightlootr.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.twilightlootr.TwilightLootr;

public class ModEntities {
  private static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, TwilightLootr.MODID);

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
