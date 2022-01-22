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

package com.buuz135.refinedstoragerequestify.proxy.container;

import com.buuz135.refinedstoragerequestify.proxy.CommonProxy;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.container.BaseContainerMenu;
import com.refinedmods.refinedstorage.container.slot.filter.FilterSlot;
import com.refinedmods.refinedstorage.container.slot.filter.FluidFilterSlot;
import net.minecraft.world.entity.player.Player;

public class ContainerRequester extends BaseContainerMenu {

    public ContainerRequester(TileRequester tile, Player player, int windowid) {
        super(CommonProxy.REQUESTER_CONTAINER, tile, player, windowid);
        for (int i = 0; i < 9; ++i) {
            addSlot(new FilterSlot(tile.getNode().getItemFilters(), i, 8 + (18 * i), 20).setEnableHandler(() -> tile.getNode().getType() == IType.ITEMS));
        }
        for (int i = 0; i < 9; ++i) {
            addSlot(new FluidFilterSlot(tile.getNode().getFluidFilters(), i, 8 + (18 * i), 20).setEnableHandler(() -> tile.getNode().getType() == IType.FLUIDS));
        }
        addPlayerInventory(8, 55);
        transferManager.addFilterTransfer(player.getInventory(), tile.getNode().getItemFilters(), tile.getNode().getFluidFilters(), tile.getNode()::getType);
    }
}
