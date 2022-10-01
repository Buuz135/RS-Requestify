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
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RefinedStorageRequestify.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, RefinedStorageRequestify.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RefinedStorageRequestify.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RefinedStorageRequestify.MOD_ID);

    public static final RegistryObject<BlockRequester> REQUESTER = BLOCKS.register("requester", () -> new BlockRequester());
    public static final RegistryObject<BlockCraftingEmitter> CRAFTING_EMITTER = BLOCKS.register("crafting_emitter", () -> new BlockCraftingEmitter());

    public static final RegistryObject<Item> REQUESTER_ITEM = ITEMS.register("requester", () -> new BlockItem(Registry.REQUESTER.get(), new Item.Properties().tab(RefinedStorageRequestify.TAB)));
    public static final RegistryObject<Item> CRAFTING_EMITTER_ITEM = ITEMS.register("crafting_emitter", () -> new BlockItem(Registry.CRAFTING_EMITTER.get(), new Item.Properties().tab(RefinedStorageRequestify.TAB)));

    public static final RegistryObject<BlockEntityType<TileRequester>> REQUESTER_TYPE = BLOCK_ENTITY_TYPES.register("requester", () -> BlockEntityType.Builder
            .of((pos, state) -> new TileRequester(pos, state), Registry.REQUESTER.get())
            .build(null));

    public static final RegistryObject<BlockEntityType<TileCraftingEmitter>> CRAFTING_EMITTER_TYPE = BLOCK_ENTITY_TYPES.register("crafting_emitter", () -> BlockEntityType.Builder
            .of((pos, state) -> new TileCraftingEmitter(pos, state), Registry.CRAFTING_EMITTER.get())
            .build(null));

    public static final RegistryObject<MenuType<ContainerRequester>> REQUESTER_CONTAINER = MENU_TYPES.register("requester", () -> IForgeMenuType.create(new BlockEntityContainerFactory<ContainerRequester, TileRequester>((windowId, inv, blockEntity) -> new ContainerRequester(blockEntity, inv.player, windowId))));
    public static final RegistryObject<MenuType<ContainerCraftingEmitter>> CRAFTING_EMITTER_CONTAINER = MENU_TYPES.register("crafting_emitter", () -> IForgeMenuType.create(new BlockEntityContainerFactory<ContainerCraftingEmitter, TileCraftingEmitter>((windowId, inv, blockEntity) -> new ContainerCraftingEmitter(blockEntity, inv.player, windowId))));
}
