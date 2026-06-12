package noobanidus.mods.twilightlootr.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.phys.AABB;
import noobanidus.mods.twilightlootr.mixin.MixinBaseTFBoss;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.boss.BaseTFBoss;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BossTracking {
  private record UUIDPair(UUID id, long value) {
    static final Codec<UUIDPair> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(UUIDUtil.CODEC.fieldOf("id").forGetter(UUIDPair::id), Codec.LONG.fieldOf("value")
            .forGetter(UUIDPair::value)).apply(instance, UUIDPair::new));
    static final Codec<List<UUIDPair>> LIST_CODEC = CODEC.listOf();
  }

  public static final Codec<BossTracking> CODEC = UUIDPair.LIST_CODEC.xmap(
      list -> new BossTracking(
          list.stream().collect(Object2LongOpenHashMap::new, (m, p) -> m.put(p.id, p.value), Object2LongMap::putAll)),
      tracking -> tracking.trackingMap.object2LongEntrySet().stream()
          .map(e -> new UUIDPair(e.getKey(), e.getLongValue()))
          .toList()
  );

  private final Object2LongMap<UUID> trackingMap;

  public BossTracking() {
    trackingMap = new Object2LongOpenHashMap<>();
  }

  public BossTracking(Object2LongMap<UUID> trackingMap) {
    this.trackingMap = trackingMap;
  }

  public void merge (BossTracking otherTracking) {
    for (Object2LongMap.Entry<UUID> entry : otherTracking.trackingMap.object2LongEntrySet()) {
      trackingMap.mergeLong(entry.getKey(), entry.getLongValue(), Math::min);
    }
  }

  public void trackPlayer(ServerPlayer player) {
    trackingMap.put(player.getUUID(), player.serverLevel().getGameTime());
  }

  public void trackPlayers (ServerLevel level, BlockPos pos) {
    for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(32.0F))) {
      trackPlayer(player);
    }
  }

  public List<UUID> eligiblePlayers(ServerLevel level) {
    // 2 minutes, should be configurable
    return eligiblePlayers(level, 20 * 60 * 2);
  }

  public List<UUID> eligiblePlayers(ServerLevel level, long cooldown) {
    List<UUID> result = new ArrayList<>();
    for (var entry : trackingMap.object2LongEntrySet()) {
      if (entry.getLongValue() >= level.getGameTime() - cooldown) {
        result.add(entry.getKey());
      }
    }
    return result;
  }

  public List<PlayerEntry> eligiblePlayersList (ServerLevel level) {
    List<PlayerEntry> result = new ArrayList<>();
    MinecraftServer server = level.getServer();
    PlayerList playerList = server.getPlayerList();
    for (UUID playerId : eligiblePlayers(level)) {
      ServerPlayer player = playerList.getPlayer(playerId);
      result.add(new PlayerEntry(playerId, player));
    }
    return result;
  }

  public record PlayerEntry (UUID id, @Nullable ServerPlayer player) {
  }

  public CompoundTag save(CompoundTag tag) {
    tag.put("trackingMap", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
    return tag;
  }

  public static BossTracking load(CompoundTag tag) {
    if (!tag.contains("trackingMap")) {
      return new BossTracking();
    }
    return CODEC.decode(NbtOps.INSTANCE, tag.get("trackingMap")).getOrThrow().getFirst();
  }
}
