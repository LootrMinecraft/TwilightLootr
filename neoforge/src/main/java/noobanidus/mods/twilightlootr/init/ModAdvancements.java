package noobanidus.mods.twilightlootr.init;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import noobanidus.mods.lootr.common.advancement.ContainerTrigger;
import noobanidus.mods.twilightlootr.TwilightLootr;

public class ModAdvancements {
  private static final DeferredRegister<CriterionTrigger<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, TwilightLootr.MODID);

  public static final DeferredHolder<CriterionTrigger<?>, ContainerTrigger> BOSS_CHEST = REGISTER.register("boss_chest_opened", ContainerTrigger::new);

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
