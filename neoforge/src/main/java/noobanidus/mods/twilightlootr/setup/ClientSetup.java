package noobanidus.mods.twilightlootr.setup;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import noobanidus.mods.lootr.common.api.registry.LootrRegistry;
import noobanidus.mods.lootr.common.block.entity.LootrChestBlockEntity;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.client.block.TFLootrBossChestBlockRenderer;
import noobanidus.mods.twilightlootr.client.item.TFLootrBossChestItemRenderer;
import noobanidus.mods.twilightlootr.init.ModBlockEntities;
import noobanidus.mods.twilightlootr.init.ModItems;

@EventBusSubscriber(modid = TwilightLootr.MODID, value = Dist.CLIENT)
public class ClientSetup {
  @SubscribeEvent
  public static void modelAdditional(ModelEvent.RegisterAdditional event) {
  }

  @SubscribeEvent
  public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(ModBlockEntities.BOSS_CHEST.get(), TFLootrBossChestBlockRenderer::new);
  }

  @SubscribeEvent
  public static void registerExtensions (RegisterClientExtensionsEvent event) {
    event.registerItem(new IClientItemExtensions() {
      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return TFLootrBossChestItemRenderer.getInstance();
      }
    }, ModItems.BOSS_CHEST.get());
  }
}
