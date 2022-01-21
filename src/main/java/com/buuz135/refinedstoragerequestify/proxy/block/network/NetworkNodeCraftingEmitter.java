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

package com.buuz135.refinedstoragerequestify.proxy.block.network;

import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.CommonProxy;
import com.buuz135.refinedstoragerequestify.proxy.block.BlockCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileCraftingEmitter;
import com.refinedmods.refinedstorage.api.autocrafting.task.ICraftingTask;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.blockentity.config.IType;
import com.refinedmods.refinedstorage.inventory.fluid.FluidInventory;
import com.refinedmods.refinedstorage.inventory.item.BaseItemHandler;
import com.refinedmods.refinedstorage.inventory.listener.NetworkNodeFluidInventoryListener;
import com.refinedmods.refinedstorage.inventory.listener.NetworkNodeInventoryListener;
import com.refinedmods.refinedstorage.util.StackUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;

public class NetworkNodeCraftingEmitter extends NetworkNode implements IType {

    public static final ResourceLocation ID = new ResourceLocation(RefinedStorageRequestify.MOD_ID, "crafting_emitter");

    private static final String NBT_TYPE = "Type";
    private static final String NBT_FLUID_FILTERS = "FluidFilter";

    private BaseItemHandler itemFilter = new BaseItemHandler(9).addListener(new NetworkNodeInventoryListener(this));
    private FluidInventory fluidFilter = new FluidInventory(9).addListener(new NetworkNodeFluidInventoryListener(this));

    private int type = IType.ITEMS;

    private ICraftingTask craftingTask = null;

    public NetworkNodeCraftingEmitter(Level level, BlockPos pos) {
        super(level, pos);
    }

    @Override
    public void update() {
        super.update();
        if (network == null) return;
        if (canUpdate() && ticks % 4 == 0 && (craftingTask == null || !network.getCraftingManager().getTasks().contains(craftingTask))) {
            if (craftingTask != null && (craftingTask.getCompletionPercentage() == 100 || !network.getCraftingManager().getTasks().contains(craftingTask))) {
                this.craftingTask = null;
                updateState();
            }
            if (type == IType.ITEMS) {
                for (ICraftingTask task : network.getCraftingManager().getTasks()) {
                    for (int i = 0; i < itemFilter.getSlots(); i++) {
                        if (task.getRequested().getItem() != null && task.getRequested().getItem().sameItem(itemFilter.getStackInSlot(i))) {
                            this.craftingTask = task;
                            updateState();
                            break;
                        }
                    }
                }
            }
            if (type == IType.FLUIDS) {
                for (ICraftingTask task : network.getCraftingManager().getTasks()) {
                    for (int i = 0; i < fluidFilter.getSlots(); i++) {
                        if (task.getRequested().getFluid() != null && task.getRequested().getFluid().isFluidEqual(fluidFilter.getFluid(i))) {
                            this.craftingTask = task;
                            updateState();
                            break;
                        }
                    }
                }
            }
        }
    }

    public ICraftingTask getCraftingTask() {
        return craftingTask;
    }

    @Override
    public int getEnergyUsage() {
        return 10;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public int getType() {
        return level.isClientSide ? TileCraftingEmitter.TYPE.getValue() : type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
        markDirty();
    }

    @Override
    public IItemHandlerModifiable getItemFilters() {
        return itemFilter;
    }

    @Override
    public FluidInventory getFluidFilters() {
        return fluidFilter;
    }

    @Override
    public void read(CompoundTag tag) {
        super.read(tag);
        //StackUtils.readItems(itemFilter, 0, tag);
    }

    @Override
    public CompoundTag write(CompoundTag tag) {
        super.write(tag);
        //StackUtils.writeItems(itemFilter, 0, tag);
        return tag;
    }

    @Override
    public CompoundTag writeConfiguration(CompoundTag tag) {
        super.writeConfiguration(tag);
        tag.putInt(NBT_TYPE, type);
        StackUtils.writeItems(itemFilter, 0, tag);
        tag.put(NBT_FLUID_FILTERS, fluidFilter.writeToNbt());
        return tag;
    }

    @Override
    public void readConfiguration(CompoundTag tag) {
        super.readConfiguration(tag);
        if (tag.contains(NBT_TYPE)) {
            type = tag.getInt(NBT_TYPE);
        }
        StackUtils.readItems(itemFilter, 0, tag);
        if (tag.contains(NBT_FLUID_FILTERS)) {
            fluidFilter.readFromNbt(tag.getCompound(NBT_FLUID_FILTERS));
        }
    }

    private void updateState() {
        this.level.setBlockAndUpdate(this.pos, CommonProxy.CRAFTING_EMITTER.defaultBlockState().setValue(BlockCraftingEmitter.POWERED, this.craftingTask != null));
        for (Direction direction : Direction.values()) {
            BlockPos blockpos = pos.offset(direction.getOpposite().getNormal());
            if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(level, pos, level.getBlockState(pos), java.util.EnumSet.of(direction.getOpposite()), false).isCanceled())
                return;
            level.neighborChanged(blockpos, CommonProxy.CRAFTING_EMITTER, pos);
            level.updateNeighborsAtExceptFromFacing(blockpos, CommonProxy.CRAFTING_EMITTER, direction);
        }
    }
}
