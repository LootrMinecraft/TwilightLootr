package noobanidus.mods.twilightlootr.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.config.TFCommonConfig;
import twilightforest.config.TFConfig;

@Mixin(TFConfig.class)
public class MixinTFConfig {
  @Inject(method="rebakeCommonOptions", at=@At("TAIL"))
  private static void lootr$rebakeCommonOptions(TFCommonConfig config, CallbackInfo ci) {
    TFConfig.bossDropChests = true;
    TFConfig.multiplayerFightAdjuster = switch (TFConfig.multiplayerFightAdjuster) {
      case MORE_LOOT_AND_HEALTH, MORE_HEALTH -> TFConfig.MultiplayerFightAdjuster.MORE_HEALTH;
      default -> TFConfig.MultiplayerFightAdjuster.NONE;
    };
  }
}
