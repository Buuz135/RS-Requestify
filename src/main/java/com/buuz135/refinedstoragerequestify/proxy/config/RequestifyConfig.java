package com.buuz135.refinedstoragerequestify.proxy.config;

import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = RefinedStorageRequestify.MOD_ID)
public class RequestifyConfig {
    @Config.Comment("The maximum amount requested as a craft by the Requester. It will still achieve the desired amount but in smaller crafting batches. (A sanity server check)")
    @Config.RangeInt(min = 1)
    public static int MAX_CRAFT_AMOUNT = 1000;

    @Mod.EventBusSubscriber(modid = RefinedStorageRequestify.MOD_ID)
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(RefinedStorageRequestify.MOD_ID)) {
                ConfigManager.sync(RefinedStorageRequestify.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
