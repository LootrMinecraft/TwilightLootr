package noobanidus.mods.twilightlootr.setup;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import noobanidus.mods.lootr.common.api.registry.LootrRegistry;
import noobanidus.mods.lootr.common.block.entity.LootrChestBlockEntity;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.client.block.TFLootrBossChestBlockRenderer;

@EventBusSubscriber(modid = TwilightLootr.MODID, value = Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void modelAdditional(ModelEvent.RegisterAdditional event) {
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer((BlockEntityType<LootrChestBlockEntity>) LootrRegistry.getChestBlockEntity(), TFLootrBossChestBlockRenderer::new);
  }
}
