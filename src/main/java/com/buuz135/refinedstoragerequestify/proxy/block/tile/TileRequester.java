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
package com.buuz135.refinedstoragerequestify.proxy.block.tile;

import com.buuz135.refinedstoragerequestify.proxy.block.network.NetworkNodeRequester;
import com.buuz135.refinedstoragerequestify.proxy.client.GuiRequester;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.gui.GuiBase;
import com.raoulvdberge.refinedstorage.tile.TileNode;
import com.raoulvdberge.refinedstorage.tile.config.IType;
import com.raoulvdberge.refinedstorage.tile.data.TileDataManager;
import com.raoulvdberge.refinedstorage.tile.data.TileDataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileRequester extends TileNode<NetworkNodeRequester> {

    public static final TileDataParameter<Integer, TileRequester> TYPE = IType.createParameter();
    public static final TileDataParameter<Integer, TileRequester> AMOUNT = new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getAmount(), (t, v) -> { if (t.getWorld() == null || t.getWorld().isRemote || t.getNode().getAmount() == v) return; t.getNode().setAmount(v);}, (initial, p) -> { if (initial) { System.out.println("skipped initial"); return;} GuiBase.executeLater(GuiRequester.class, requester -> { if (!requester.getAmount().isFocused() || p != 0) { requester.getAmount().setText(String.valueOf(p));}});});
    public static final TileDataParameter<Boolean, TileRequester> MISSING = new TileDataParameter<>(DataSerializers.BOOLEAN, false, tileRequester -> tileRequester.getNode().isMissingItems(), (tileRequester, aBoolean) -> {
    });

    static {
        TileDataManager.registerParameter(TYPE);
        TileDataManager.registerParameter(AMOUNT);
        TileDataManager.registerParameter(MISSING);
        API.instance().getNetworkNodeRegistry().add(NetworkNodeRequester.ID, (tag, world1, pos1) -> {
            NetworkNodeRequester networkNodeRequester = new NetworkNodeRequester(world1, pos1);
            networkNodeRequester.read(tag);
            return networkNodeRequester;
        });
    }

    public TileRequester() {
        dataManager.addWatchedParameter(TYPE);
        dataManager.addWatchedParameter(AMOUNT);
        dataManager.addParameter(MISSING);
    }

    @Override
    public NetworkNodeRequester createNode(World world, BlockPos pos) {
        return new NetworkNodeRequester(world, pos);
    }

    @Override
    public String getNodeId() {
        return NetworkNodeRequester.ID;
    }


}
