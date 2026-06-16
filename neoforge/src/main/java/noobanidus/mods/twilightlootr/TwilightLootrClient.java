package noobanidus.mods.twilightlootr;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = TwilightLootr.MODID, dist = Dist.CLIENT)
public class TwilightLootrClient {
  public TwilightLootrClient(ModContainer modContainer, IEventBus modBus) {
    modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }
}
