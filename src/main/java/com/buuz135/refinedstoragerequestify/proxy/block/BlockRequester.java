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
package com.buuz135.refinedstoragerequestify.proxy.block;

import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.block.network.NetworkNodeRequester;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import com.buuz135.refinedstoragerequestify.proxy.client.ContainerRequester;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockRequester extends NetworkNodeBlock {

    public BlockRequester() {
        super(BlockUtils.DEFAULT_ROCK_PROPERTIES);
        setRegistryName(RefinedStorageRequestify.MOD_ID, "requester");
        API.instance().getNetworkNodeRegistry().add(NetworkNodeRequester.ID, (compoundNBT, world, blockPos) -> readAndReturn(compoundNBT, new NetworkNodeRequester(world, blockPos)));
        //setCreativeTab(RefinedStorageRequestify.TAB);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileRequester();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            return NetworkUtils.attempt(world, pos, player, () -> NetworkHooks.openGui(
                    (ServerPlayerEntity) player,
                    new PositionalTileContainerProvider<TileRequester>(
                            new TranslationTextComponent("block.refinedstoragerequestify:requester.name"),
                            (tile, windowId, inventory, p) -> new ContainerRequester(tile, player, windowId),
                            pos
                    ),
                    pos
            ), Permission.MODIFY, Permission.INSERT, Permission.EXTRACT);
        }

        return ActionResultType.SUCCESS;
    }


    @Override
    public boolean hasConnectedState() {
        return true;
    }

    private INetworkNode readAndReturn(CompoundNBT tag, NetworkNode node) {
        node.read(tag);
        return node;
    }
}
