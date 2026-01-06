package noobanidus.mods.twilightlootr.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BossTracking {
  public static final Codec<BossTracking> CODEC = Codec.unboundedMap(UUIDUtil.CODEC, Codec.LONG)
      .xmap(BossTracking::new, BossTracking::getTrackingMap);
  public static final StreamCodec<ByteBuf, BossTracking> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(Object2LongOpenHashMap::new, UUIDUtil.STREAM_CODEC, ByteBufCodecs.VAR_LONG), BossTracking::getTrackingMap, BossTracking::new);

  private final Object2LongMap<UUID> trackingMap;

  public BossTracking() {
    trackingMap = new Object2LongOpenHashMap<>();
  }

  public BossTracking(Object2LongMap<UUID> trackingMap) {
    this.trackingMap = trackingMap;
  }

  public BossTracking(Map<UUID, Long> uuidLongMap) {
    this.trackingMap = new Object2LongOpenHashMap<>(uuidLongMap);
  }

  Object2LongMap<UUID> getTrackingMap() {
    return trackingMap;
  }

  public void trackPlayer(ServerPlayer player) {
    trackingMap.put(player.getUUID(), player.serverLevel().getGameTime());
  }

  public List<UUID> eligiblePlayers(ServerLevel level) {
    // 2 minutes, should be configurable
    return eligiblePlayers(level, 20 * 60 * 2);
  }

  public List<UUID> eligiblePlayers(ServerLevel level, long cooldown) {
    List<UUID> result = new ArrayList<>();
    for (var entry : trackingMap.object2LongEntrySet()) {
      if ((entry.getLongValue() + cooldown) <= level.getGameTime()) {
        result.add(entry.getKey());
      }
    }
    return result;
  }

  public CompoundTag save (CompoundTag tag) {
    tag.put("trackingMap", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow());
    return tag;
  }

  public static BossTracking load (CompoundTag tag) {
    if (!tag.contains("trackingMap")) {
      return new BossTracking();
    }
    return CODEC.decode(NbtOps.INSTANCE, tag.get("trackingMap")).getOrThrow().getFirst();
  }
}
