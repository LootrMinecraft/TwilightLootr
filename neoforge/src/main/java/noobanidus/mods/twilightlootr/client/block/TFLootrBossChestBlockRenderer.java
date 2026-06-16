package noobanidus.mods.twilightlootr.client.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.block.entity.TFLootrBossChestBlockEntity;

@SuppressWarnings({"NullableProblems"})
public class TFLootrBossChestBlockRenderer extends ChestRenderer<TFLootrBossChestBlockEntity> {
  public static final ResourceLocation CHEST_SHEET = ResourceLocation.withDefaultNamespace("textures/atlas/chest.png");

  public static final Material MATERIAL = new Material(CHEST_SHEET, TwilightLootr.rl("entity/boss_chest"));
  public static final Material MATERIAL2 = new Material(CHEST_SHEET, TwilightLootr.rl("entity/boss_chest_opened"));
  public static final Material MATERIAL3 = new Material(CHEST_SHEET, TwilightLootr.rl("entity/boss_chest_invalid"));

  public TFLootrBossChestBlockRenderer(BlockEntityRendererProvider.Context p_173607_) {
    super(p_173607_);
  }

  @Override
  protected Material getMaterial(TFLootrBossChestBlockEntity blockEntity, ChestType type) {
    var player = Minecraft.getInstance().player;
    if (blockEntity.isItemRendering) {
      return MATERIAL;
    }
    if (player == null) {
      return MATERIAL2;
    }
    if (!blockEntity.isEligiblePlayer(player)) {
      return MATERIAL3;
    }

    if (blockEntity.hasClientOpened(player.getUUID())) {
      return MATERIAL2;
    } else {
      return MATERIAL;
    }
  }
}
