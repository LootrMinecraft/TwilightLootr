package noobanidus.mods.twilightlootr.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(LootTable.class)
public interface AccessorMixinLootTable {
  @Invoker("shuffleAndSplitItems")
  void lootr$shuffleAndSplitItems(ObjectArrayList<ItemStack> stacks, int emptySlotsCount, RandomSource random);

  @Invoker("getAvailableSlots")
  List<Integer> lootr$getAvailableSlots(Container inventory, RandomSource random);
}
