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
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RefinedStorageRequestify.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RefinedStorageRequestify.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RefinedStorageRequestify.MOD_ID);
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, RefinedStorageRequestify.MOD_ID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RefinedStorageRequestify.MOD_ID);

    public static final DeferredBlock<BlockRequester> REQUESTER = BLOCKS.register("requester", () -> new BlockRequester());
    public static final DeferredBlock<BlockCraftingEmitter> CRAFTING_EMITTER = BLOCKS.register("crafting_emitter", () -> new BlockCraftingEmitter());

    public static final DeferredItem<Item> REQUESTER_ITEM = ITEMS.register("requester", () -> new BlockItem(Registry.REQUESTER.get(), new Item.Properties()));
    public static final DeferredItem<Item> CRAFTING_EMITTER_ITEM = ITEMS.register("crafting_emitter", () -> new BlockItem(Registry.CRAFTING_EMITTER.get(), new Item.Properties()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileRequester>> REQUESTER_TYPE = BLOCK_ENTITY_TYPES.register("requester", () -> BlockEntityType.Builder
            .of(TileRequester::new, Registry.REQUESTER.get())
            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCraftingEmitter>> CRAFTING_EMITTER_TYPE = BLOCK_ENTITY_TYPES.register("crafting_emitter", () -> BlockEntityType.Builder
            .of(TileCraftingEmitter::new, Registry.CRAFTING_EMITTER.get())
            .build(null));

    public static final DeferredHolder<MenuType<?>, MenuType<ContainerRequester>> REQUESTER_CONTAINER = MENU_TYPES.register("requester", () -> IMenuTypeExtension.create(new BlockEntityContainerFactory<ContainerRequester, TileRequester>((windowId, inv, blockEntity) -> new ContainerRequester(blockEntity, inv.player, windowId))));
    public static final DeferredHolder<MenuType<?>, MenuType<ContainerCraftingEmitter>> CRAFTING_EMITTER_CONTAINER = MENU_TYPES.register("crafting_emitter", () -> IMenuTypeExtension.create(new BlockEntityContainerFactory<ContainerCraftingEmitter, TileCraftingEmitter>((windowId, inv, blockEntity) -> new ContainerCraftingEmitter(blockEntity, inv.player, windowId))));

    public static final Holder<CreativeModeTab> TAB = CREATIVE_TABS.register("rsrequestify", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(REQUESTER_ITEM.get()))
            .title(Component.translatable("itemGroup.rsrequestify"))
            .displayItems((p_270258_, a) -> {
                a.accept(new ItemStack(REQUESTER_ITEM.get()));
                a.accept(new ItemStack(CRAFTING_EMITTER_ITEM.get()));
            })
            .build());
}
