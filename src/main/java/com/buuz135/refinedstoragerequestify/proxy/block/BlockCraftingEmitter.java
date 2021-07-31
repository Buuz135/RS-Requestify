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

package com.buuz135.refinedstoragerequestify.proxy.block;

import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.block.network.NetworkNodeCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerCraftingEmitter;
import com.refinedmods.refinedstorage.api.network.node.INetworkNode;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.block.NetworkNodeBlock;
import com.refinedmods.refinedstorage.container.factory.PositionalTileContainerProvider;
import com.refinedmods.refinedstorage.util.BlockUtils;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class BlockCraftingEmitter extends NetworkNodeBlock {

    public static final BooleanProperty POWERED = BooleanProperty.create("powered");

    private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 5, 16);

    public BlockCraftingEmitter() {
        super(BlockUtils.DEFAULT_ROCK_PROPERTIES);
        setRegistryName(RefinedStorageRequestify.MOD_ID, "crafting_emitter");
        API.instance().getNetworkNodeRegistry().add(NetworkNodeCraftingEmitter.ID, (compoundNBT, world, blockPos) -> readAndReturn(compoundNBT, new NetworkNodeCraftingEmitter(world, blockPos)));
        this.setDefaultState(this.getStateContainer().getBaseState().with(POWERED, false));
        //setCreativeTab(RefinedStorageRequestify.TAB);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);

        builder.add(POWERED);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileCraftingEmitter();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            return NetworkUtils.attempt(world, pos, player, () -> NetworkHooks.openGui(
                    (ServerPlayerEntity) player,
                    new PositionalTileContainerProvider<TileCraftingEmitter>(
                            new TranslationTextComponent("block.refinedstoragerequestify:crafting_emitter.name"),
                            (tile, windowId, inventory, p) -> new ContainerCraftingEmitter(tile, player, windowId),
                            pos
                    ),
                    pos
            ), Permission.MODIFY, Permission.INSERT, Permission.EXTRACT);
        }
        return ActionResultType.SUCCESS;
    }


    @Override
    public boolean hasConnectedState() {
        return false;
    }

    private INetworkNode readAndReturn(CompoundNBT tag, NetworkNode node) {
        node.read(tag);
        return node;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        TileEntity entity = blockAccess.getTileEntity(pos);
        if (entity instanceof TileCraftingEmitter) {
            if (((TileCraftingEmitter) entity).getNode().getCraftingTask() != null) return 15;
        }
        return 0;
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        TileEntity entity = blockAccess.getTileEntity(pos);
        if (entity instanceof TileCraftingEmitter) {
            if (((TileCraftingEmitter) entity).getNode().getCraftingTask() != null) return 15;
        }
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("block.rsrequestify.crafting_emitter.tooltip").mergeStyle(TextFormatting.GRAY));
    }
}
