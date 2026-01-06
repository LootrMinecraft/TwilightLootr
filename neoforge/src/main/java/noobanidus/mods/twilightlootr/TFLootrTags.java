package noobanidus.mods.twilightlootr;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class TFLootrTags {
  public static class Blocks {
    public static final TagKey<Block> BOSS_CHESTS = TagKey.create(Registries.BLOCK, TwilightLootr.rl("boss_chests"));
  }

  public static class Entity {
    public static final TagKey<EntityType<?>> PLAYERS = TagKey.create(Registries.ENTITY_TYPE, TwilightLootr.rl("player"));

    public static final TagKey<EntityType<?>> BOSSES = TagKey.create(Registries.ENTITY_TYPE, TwilightLootr.rl("bosses"));
  }
}
