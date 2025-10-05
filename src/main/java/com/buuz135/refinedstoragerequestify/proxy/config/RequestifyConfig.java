/*
 * This file is part of RS: Requestify.
 *
 * Copyright 2018, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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
    public static int maxCraftAmount = 1000;

    @Config.Comment("The amount ticks between attempts to craft by the Requester where 20 ticks = 1 second (lower - better for you, but not the server)")
    @Config.RangeInt(min = 5, max = 1000)
    public static int ticksBetweenUpdates = 10;

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
