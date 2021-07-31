/*
 * This file is part of RSRequestifyu.
 *
 * Copyright 2021, Buuz135
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

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class RequestifyConfig {

    public static int MAX_CRAFT_AMOUNT = 1000;


    public static Common COMMON = new Common();

    private static abstract class ConfigClass {
        public ForgeConfigSpec SPEC;

        public abstract void onConfigReload(ModConfig.Reloading event);
    }

    public static class Common extends ConfigClass {
        public ForgeConfigSpec.ConfigValue<Integer> MAX_CRAFT_AMOUNT;
        public ForgeConfigSpec.ConfigValue<Boolean> IGNORE_ITEM_DAMAGE;

        public Common() {
            final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
            BUILDER.push("COMMON");
            MAX_CRAFT_AMOUNT = BUILDER.comment("Max amount of items per request").defineInRange("MAX_CRAFT_AMOUNT", 1000, 1, Integer.MAX_VALUE);
            BUILDER.pop();
            SPEC = BUILDER.build();
        }

        @Override
        public void onConfigReload(ModConfig.Reloading event) {
            if (event.getConfig().getType() == ModConfig.Type.COMMON) {
                SPEC.setConfig(event.getConfig().getConfigData());
            }
        }
    }

}
