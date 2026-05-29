package noobanidus.mods.twilightlootr.client.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.ChestType;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.block.entity.TFLootrBossChestBlockEntity;

@SuppressWarnings({"NullableProblems"})
public class TFLootrBossChestBlockRenderer extends ChestRenderer<TFLootrBossChestBlockEntity> {
  public static final Material MATERIAL = new Material(Sheets.CHEST_SHEET, TwilightLootr.rl("entity/boss_chest"));
  public static final Material MATERIAL2 = new Material(Sheets.CHEST_SHEET, TwilightLootr.rl("entity/boss_chest_opened"));

  public TFLootrBossChestBlockRenderer(BlockEntityRendererProvider.Context p_173607_) {
    super(p_173607_);
  }

  @Override
  protected Material getMaterial(TFLootrBossChestBlockEntity blockEntity, ChestType type) {
    if (Minecraft.getInstance().player == null) {
      return MATERIAL2;
    }
    if (blockEntity.hasClientOpened(Minecraft.getInstance().player.getUUID())) {
      return MATERIAL2;
    } else {
      return MATERIAL;
    }
  }
}
