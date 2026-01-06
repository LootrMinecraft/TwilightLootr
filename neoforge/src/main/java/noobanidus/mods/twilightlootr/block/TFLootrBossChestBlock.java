package noobanidus.mods.twilightlootr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.mods.lootr.common.block.LootrChestBlock;
import noobanidus.mods.twilightlootr.block.entity.TFLootrBossChestBlockEntity;

public class TFLootrBossChestBlock extends LootrChestBlock {
  public TFLootrBossChestBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TFLootrBossChestBlockEntity(pos, state);
  }
}
