package mett.palemannie.squakeported;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(SquakePorted.MODID)
public class SquakePorted {

    public static final String MODID = "squakeported";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SquakePorted(IEventBus modEventBus, ModContainer modContainer) {

        //NeoForge.EVENT_BUS.register(this);
        modContainer.registerConfig(ModConfig.Type.COMMON, SquakeConfig.SPEC);
        modEventBus.addListener(ToggleKeyHandler::registerKeys);
    }
}
