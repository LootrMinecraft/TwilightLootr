package noobanidus.mods.twilightlootr.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import noobanidus.mods.twilightlootr.TwilightLootr;
import noobanidus.mods.twilightlootr.entity.IHasBossTracking;

@EventBusSubscriber(modid = TwilightLootr.MODID)
public class EntityEventHandler {
  @SubscribeEvent
  public static void onEntityDamage(LivingDamageEvent.Post event) {
    if (event.getEntity().level().isClientSide()) {
      return;
    }
    IHasBossTracking boss = null;
    Player player = null;
    if (event.getEntity() instanceof Player player2) {
      player = player2;
      var source = event.getSource().getEntity();
      if (source instanceof IHasBossTracking) {
        boss = (IHasBossTracking) source;
      } else if (source == null) {
        source = event.getSource().getDirectEntity();
        if (source instanceof IHasBossTracking) {
          boss = (IHasBossTracking) source;
        }
      }
    } else if (event.getEntity() instanceof
        IHasBossTracking tracking) {
      var source = event.getSource().getEntity();
      if (source instanceof Player player2) {
        // Player attacked a boss
        boss = tracking;
        player = player2;
      }
      if (source == null) {
        source = event.getSource().getDirectEntity();
        if (source instanceof Player player2) {
          // Player projectile hit a boss
          boss = tracking;
          player = player2;
        }
      }
    }
    if (boss == null || !(player instanceof ServerPlayer sPlayer)) {
      return;
    }
    boss.lootr$GetBossTracking().trackPlayer(sPlayer);
  }
}
