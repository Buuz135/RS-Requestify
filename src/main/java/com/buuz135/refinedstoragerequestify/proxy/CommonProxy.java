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

package com.buuz135.refinedstoragerequestify.proxy;

import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.block.BlockCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.block.BlockRequester;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerRequester;
import com.refinedmods.refinedstorage.container.factory.BlockEntityContainerFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.RegistryEvent;

public class CommonProxy {

    public static BlockRequester REQUESTER;
    public static MenuType<ContainerRequester> REQUESTER_CONTAINER = null;
    public static BlockEntityType<?> REQUESTER_TYPE;

    public static BlockCraftingEmitter CRAFTING_EMITTER;
    public static BlockEntityType<?> CRAFTING_EMITTER_TYPE;
    public static MenuType<ContainerCraftingEmitter> CRAFTING_EMITTER_CONTAINER = null;

    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(REQUESTER = new BlockRequester());
        event.getRegistry().register(CRAFTING_EMITTER = new BlockCraftingEmitter());
    }

    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new BlockItem(REQUESTER, new Item.Properties().tab(RefinedStorageRequestify.TAB)).setRegistryName(REQUESTER.getRegistryName()));
        event.getRegistry().register(new BlockItem(CRAFTING_EMITTER, new Item.Properties().tab(RefinedStorageRequestify.TAB)).setRegistryName(CRAFTING_EMITTER.getRegistryName()));
    }

    public void registerTiles(RegistryEvent.Register<BlockEntityType<?>> event) {
        event.getRegistry().register(REQUESTER_TYPE = BlockEntityType.Builder.of(TileRequester::new, REQUESTER).build(null).setRegistryName(REQUESTER.getRegistryName()));
        event.getRegistry().register(CRAFTING_EMITTER_TYPE = BlockEntityType.Builder.of(TileCraftingEmitter::new, CRAFTING_EMITTER).build(null).setRegistryName(CRAFTING_EMITTER.getRegistryName()));
    }

    public void registerContainers(RegistryEvent.Register<MenuType<?>> event) {
        event.getRegistry().register(REQUESTER_CONTAINER = (MenuType<ContainerRequester>) IForgeMenuType.create(new BlockEntityContainerFactory<ContainerRequester, TileRequester>((i, playerInventory, tileRequester) -> new ContainerRequester(tileRequester, playerInventory.player, i))).setRegistryName(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "requester")));
        event.getRegistry().register(CRAFTING_EMITTER_CONTAINER = (MenuType<ContainerCraftingEmitter>) IForgeMenuType.create(new BlockEntityContainerFactory<ContainerCraftingEmitter, TileCraftingEmitter>((i, playerInventory, tile) -> new ContainerCraftingEmitter(tile, playerInventory.player, i))).setRegistryName(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "crafting_emitter")));

    }
}
