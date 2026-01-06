package noobanidus.mods.twilightlootr.block.entity;

import com.google.auto.service.AutoService;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.mods.lootr.common.api.ILootrBlockEntityConverter;
import noobanidus.mods.lootr.common.api.LootrAPI;
import noobanidus.mods.lootr.common.api.NBTConstants;
import noobanidus.mods.lootr.common.api.data.LootrBlockType;
import noobanidus.mods.lootr.common.api.data.blockentity.ILootrBlockEntity;
import noobanidus.mods.lootr.common.block.entity.LootrChestBlockEntity;
import noobanidus.mods.twilightlootr.init.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class TFLootrBossChestBlockEntity extends LootrChestBlockEntity {
  private int decayingIn = 0;
  private boolean custom = false;
  private NonNullList<ItemStack> customInventory;
  private List<UUID> eligiblePlayers;

  public TFLootrBossChestBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.BOSS_CHEST.get(), pWorldPosition, pBlockState);
  }

  @Override
  public void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
    super.loadAdditional(compound, provider);
    if (compound.contains(NBTConstants.CUSTOM_INVENTORY) && compound.contains(NBTConstants.CUSTOM_SIZE)) {
      int size = compound.getInt(NBTConstants.CUSTOM_SIZE);
      this.customInventory = NonNullList.withSize(size, ItemStack.EMPTY);
      ContainerHelper.loadAllItems(compound.getCompound(NBTConstants.CUSTOM_INVENTORY), this.customInventory, provider);
      if (this.customInventory.stream().anyMatch(o -> !o.isEmpty())) {
        this.custom = true;
      }
    }
    if (compound.contains("EligiblePlayers")) {
      this.eligiblePlayers.clear();
      ListTag eligiblePlayersTag = compound.getList("EligiblePlayers", 10);
      for (int i = 0; i < eligiblePlayersTag.size(); i++) {
        CompoundTag tag = eligiblePlayersTag.getCompound(i);
        this.eligiblePlayers.add(tag.getUUID("UUID"));
      }
    }
    if (compound.contains("DecayingIn")) {
      this.decayingIn = compound.getInt("DecayingIn");
    } else {
      this.decayingIn = -1;
    }
  }

  @Override
  protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
    super.saveAdditional(compound, provider);
    if (this.customInventory != null) {
      compound.putInt(NBTConstants.CUSTOM_SIZE, this.customInventory.size());
      compound.put(NBTConstants.CUSTOM_INVENTORY, ContainerHelper.saveAllItems(new CompoundTag(), this.customInventory, provider));
    }
    ListTag eligiblePlayersTag = new ListTag();
    for (UUID uuid : this.eligiblePlayers) {
      CompoundTag tag = new CompoundTag();
      tag.putUUID("UUID", uuid);
      eligiblePlayersTag.add(tag);
    }
    compound.put("EligiblePlayers", eligiblePlayersTag);
    compound.putInt("DecayingIn", this.decayingIn);
  }

  @Override
  public void defaultTick(Level level, BlockPos pos, BlockState state) {
    super.defaultTick(level, pos, state);
    if (this.decayingIn > 0) {
      this.decayingIn--;
      if (this.decayingIn == 0 && !level.isClientSide()) {
        level.destroyBlock(pos, false);
      }
    }
  }

  @Override
  public boolean canOpen(Player player) {
    if (!super.canOpen(player)) {
      return false;
    }

    return this.eligiblePlayers.contains(player.getUUID());
  }

  @Override
  public void informPlayerCannotOpen(ServerPlayer player) {
    player.displayClientMessage(Component.translatable("twilight_lootr.boss_chest.ineligible"), true);
  }

  @Override
  @Nullable
  public NonNullList<ItemStack> getInfoReferenceInventory() {
    return customInventory;
  }

  public void setDecaying (int decayingIn) {
    this.decayingIn = decayingIn;
  }

  public void setCustomInventory(NonNullList<ItemStack> customInventory) {
    if (customInventory.isEmpty() || customInventory.stream().anyMatch(o -> !o.isEmpty())) {
      this.customInventory = customInventory;
      this.custom = true;
    }
  }

  public void setEligiblePlayers(List<UUID> eligiblePlayers) {
    this.eligiblePlayers = eligiblePlayers;
  }

  @Override
  protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int p_155868_, int p_155869_) {
    super.signalOpenCount(level, pos, state, p_155868_, p_155869_);
    if (LootrAPI.isCustomTrapped() && p_155868_ != p_155869_) {
      Block block = state.getBlock();
      level.updateNeighborsAt(pos, block);
      level.updateNeighborsAt(pos.below(), block);
    }
  }

  @Override
  public boolean isInfoReferenceInventory() {
    return custom;
  }

  @Override
  @Deprecated
  public LootrBlockType getInfoBlockType() {
    return LootrBlockType.INVENTORY;
  }

  @AutoService(ILootrBlockEntityConverter.class)
  public static class DefaultBlockEntityConverter implements ILootrBlockEntityConverter<TFLootrBossChestBlockEntity> {

    @Override
    public ILootrBlockEntity apply(TFLootrBossChestBlockEntity blockEntity) {
      return blockEntity;
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
      return ModBlockEntities.BOSS_CHEST.get();
    }
  }
}
