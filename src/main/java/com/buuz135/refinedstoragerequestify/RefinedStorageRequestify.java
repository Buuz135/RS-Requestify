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

package com.buuz135.refinedstoragerequestify;

import com.buuz135.refinedstoragerequestify.proxy.CommonProxy;
import com.buuz135.refinedstoragerequestify.proxy.client.GuiCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.client.GuiRequester;
import com.buuz135.refinedstoragerequestify.proxy.config.RequestifyConfig;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerRequester;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(
        RefinedStorageRequestify.MOD_ID
)
public class RefinedStorageRequestify {

    public static final String MOD_ID = "rsrequestify";
    public static final ItemGroup TAB = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(CommonProxy.REQUESTER);
        }
    };

    public static CommonProxy proxy;

    public RefinedStorageRequestify() {
        proxy = new CommonProxy();
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, proxy::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, proxy::registerItems);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, proxy::registerTiles);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, proxy::registerContainers);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RequestifyConfig.COMMON.SPEC);
        IEventBus mod = FMLJavaModLoadingContext.get().getModEventBus();
        mod.addListener(RequestifyConfig.COMMON::onConfigReload);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(CommonProxy.REQUESTER_CONTAINER, (ScreenManager.IScreenFactory<ContainerRequester, GuiRequester>) (p_create_1_, p_create_2_, p_create_3_) -> new GuiRequester(p_create_1_));
        ScreenManager.registerFactory(CommonProxy.CRAFTING_EMITTER_CONTAINER, (ScreenManager.IScreenFactory<ContainerCraftingEmitter, GuiCraftingEmitter>) (p_create_1_, p_create_2_, p_create_3_) -> new GuiCraftingEmitter(p_create_1_));
        RenderTypeLookup.setRenderLayer(CommonProxy.CRAFTING_EMITTER, RenderType.getCutout());
    }


}
