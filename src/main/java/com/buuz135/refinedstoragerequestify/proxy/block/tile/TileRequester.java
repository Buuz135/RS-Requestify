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

package com.buuz135.refinedstoragerequestify.proxy.block.tile;

import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.Registry;
import com.buuz135.refinedstoragerequestify.proxy.block.network.NetworkNodeRequester;
import com.buuz135.refinedstoragerequestify.proxy.client.GuiRequester;
import com.refinedmods.refinedstorage.blockentity.NetworkNodeBlockEntity;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationParameter;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationSpec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TileRequester extends NetworkNodeBlockEntity<NetworkNodeRequester> {

    public static final BlockEntitySynchronizationParameter<Integer, TileRequester> TYPE = IType.createParameter(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "tile_requester_type"));
    public static final BlockEntitySynchronizationParameter<Integer, TileRequester> AMOUNT = new BlockEntitySynchronizationParameter<Integer, TileRequester>(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "tile_requester_amount"), EntityDataSerializers.INT, 0, t -> t.getNode().getAmount(), (t, v) -> t.getNode().setAmount(v), (initial, p) -> Minecraft.getInstance().tell(() -> {
        if (Minecraft.getInstance().screen instanceof GuiRequester) {
            ((GuiRequester) Minecraft.getInstance().screen).getTextField().setValue(String.valueOf(p));
        }
    }));
    public static final BlockEntitySynchronizationParameter<Boolean, TileRequester> MISSING = new BlockEntitySynchronizationParameter<>(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "tile_requester_missing"), EntityDataSerializers.BOOLEAN, false, tileRequester -> tileRequester.getNode().isMissingItems(), (tileRequester, aBoolean) -> {
    });

    static {
        BlockEntitySynchronizationManager.registerParameter(TYPE);
        BlockEntitySynchronizationManager.registerParameter(AMOUNT);
        BlockEntitySynchronizationManager.registerParameter(MISSING);
    }

    public static BlockEntitySynchronizationSpec SPEC = BlockEntitySynchronizationSpec.builder()
            .addWatchedParameter(TYPE)
            .addWatchedParameter(AMOUNT)
            .addWatchedParameter(MISSING)
            .addWatchedParameter(REDSTONE_MODE)
            .build();

    public TileRequester(BlockPos pos, BlockState state) {
        super(Registry.REQUESTER_TYPE.get(), pos, state, SPEC);
    }

    @Override
    public NetworkNodeRequester createNode(Level level, BlockPos pos) {
        return new NetworkNodeRequester(level, pos);
    }

    public ResourceLocation getNodeId() {
        return NetworkNodeRequester.ID;
    }


}
