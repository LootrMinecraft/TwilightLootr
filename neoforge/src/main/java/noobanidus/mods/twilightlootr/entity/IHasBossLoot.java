package noobanidus.mods.twilightlootr.entity;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IHasBossLoot {
  default ObjectArrayList<ItemStack> lootr$getBossUniqueItems() {
    return null;
  }
}
