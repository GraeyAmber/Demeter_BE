package dev.teamcitrus.demeter;

import dev.teamcitrus.citruslib.network.PayloadHelper;
import dev.teamcitrus.citruslib.util.ModUtil;
import dev.teamcitrus.demeter.config.DemeterConfig;
import dev.teamcitrus.demeter.data.NamesLoader;
import dev.teamcitrus.demeter.data.StatsRegistry;
import dev.teamcitrus.demeter.network.BirthNotificationPacket;
import dev.teamcitrus.demeter.registry.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(Demeter.MODID)
public class Demeter {
    public static final String MODID = "demeter";
    public static final Logger LOGGER = LogManager.getLogger();

    public Demeter(IEventBus bus, ModContainer modContainer) {
        BlockRegistry.BLOCKS.register(bus);
        ItemRegistry.ITEMS.register(bus);
        ItemRegistry.CREATIVE_MODE_TABS.register(bus);
        FluidRegistry.FLUIDS.register(bus);
        FluidTypeRegistry.FLUID_TYPES.register(bus);
        LootModifierRegistry.LOOT_MODIFIERS.register(bus);
        AttachmentRegistry.ATTACHMENT_TYPES.register(bus);
        ComponentRegistry.COMPONENTS.register(bus);
        modContainer.registerConfig(ModConfig.Type.CLIENT, DemeterConfig.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, DemeterConfig.GENERAL_SPEC);
        WoodSetRegistry.init();
        bus.register(this);

        if (ModUtil.isDevelopmentEnvironment()) {
            ItemRegistry.DEV_ITEMS.register(bus);
            ItemRegistry.DEV_TABS.register(bus);
        }

        try {
            NamesLoader.load();
        } catch (IOException e) {
            LOGGER.error(Component.translatable("error.demeter.namesloadfail").getString());
        }
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        StatsRegistry.INSTANCE.registerToBus();
        PayloadHelper.registerPayload(new BirthNotificationPacket.Provider());
    }
}
