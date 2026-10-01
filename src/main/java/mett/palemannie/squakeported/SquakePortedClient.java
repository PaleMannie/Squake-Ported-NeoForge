package mett.palemannie.squakeported;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = SquakePorted.MODID, dist = Dist.CLIENT)
public class SquakePortedClient {
    public SquakePortedClient(IEventBus modEventBus, ModContainer container) {

        modEventBus.addListener(ToggleKeyHandler::registerKeys);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
