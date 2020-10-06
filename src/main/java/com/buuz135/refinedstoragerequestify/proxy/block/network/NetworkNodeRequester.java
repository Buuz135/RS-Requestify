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
package com.buuz135.refinedstoragerequestify.proxy.block.network;

import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import com.buuz135.refinedstoragerequestify.proxy.config.RequestifyConfig;
import com.refinedmods.refinedstorage.api.autocrafting.task.ICraftingTask;
import com.refinedmods.refinedstorage.api.util.Action;
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode;
import com.refinedmods.refinedstorage.inventory.fluid.FluidInventory;
import com.refinedmods.refinedstorage.inventory.item.BaseItemHandler;
import com.refinedmods.refinedstorage.inventory.listener.NetworkNodeFluidInventoryListener;
import com.refinedmods.refinedstorage.inventory.listener.NetworkNodeInventoryListener;
import com.refinedmods.refinedstorage.tile.config.IType;
import com.refinedmods.refinedstorage.util.StackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class NetworkNodeRequester extends NetworkNode implements IType {

    public static final ResourceLocation ID = new ResourceLocation(RefinedStorageRequestify.MOD_ID, "requester");

    private static final String NBT_TYPE = "Type";
    private static final String NBT_FLUID_FILTERS = "FluidFilter";
    private static final String NBT_AMOUNT = "Amount";
    private static final String NBT_MISSING = "MissingItems";

    private BaseItemHandler itemFilter = new BaseItemHandler(1).addListener(new NetworkNodeInventoryListener(this));
    private FluidInventory fluidFilter = new FluidInventory(1).addListener(new NetworkNodeFluidInventoryListener(this));

    private int type = IType.ITEMS;
    private int amount = 0;
    private boolean isMissingItems = false;
    private ICraftingTask craftingTask = null;

    public NetworkNodeRequester(World world, BlockPos pos) {
        super(world, pos);
    }

    @Override
    public void update() {
        super.update();
        if (network == null) return;
        if (canUpdate() && ticks % 10 == 0 && (craftingTask == null || !network.getCraftingManager().getTasks().contains(craftingTask))) {
            if (type == IType.ITEMS) {
                ItemStack filter = itemFilter.getStackInSlot(0);
                if (!filter.isEmpty()) {
                    ItemStack current = network.extractItem(filter, amount, Action.SIMULATE);
                    if (current == null || current.isEmpty() || current.getCount() < amount) {
                        int count = current == null || current.isEmpty() ? amount : amount - current.getCount();
                        if (count > 0) {
                            craftingTask = network.getCraftingManager().request(this, filter, Math.min(RequestifyConfig.MAX_CRAFT_AMOUNT, count));
                            isMissingItems = true;
                        }
                    } else {
                        isMissingItems = false;
                    }
                }
            }
            if (type == IType.FLUIDS) {
                FluidStack filter = fluidFilter.getFluid(0);
                if (!filter.isEmpty()) {
                    FluidStack current = network.extractFluid(filter, amount, Action.SIMULATE);
                    if (current.isEmpty() || current.getAmount() < amount) {
                        int count = current.isEmpty() ? amount : amount - current.getAmount();
                        if (count > 0) {
                            craftingTask = network.getCraftingManager().request(this, filter, count);
                            isMissingItems = true;
                        }
                    } else {
                        isMissingItems = false;
                    }
                }
            }
        }
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
        return world.isRemote ? TileRequester.TYPE.getValue() : type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
        markDirty();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        markDirty();
    }

    public boolean isMissingItems() {
        return isMissingItems && craftingTask == null;
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
    public void read(CompoundNBT tag) {
        super.read(tag);
        StackUtils.readItems(itemFilter, 0, tag);
        if (tag.contains(NBT_AMOUNT)) {
            amount = tag.getInt(NBT_AMOUNT);
        }
        if (tag.contains(NBT_MISSING)) {
            isMissingItems = tag.getBoolean(NBT_MISSING);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        StackUtils.writeItems(itemFilter, 0, tag);
        tag.putInt(NBT_AMOUNT, amount);
        tag.putBoolean(NBT_MISSING, isMissingItems);
        return tag;
    }

    @Override
    public CompoundNBT writeConfiguration(CompoundNBT tag) {
        super.writeConfiguration(tag);
        tag.putInt(NBT_TYPE, type);
        StackUtils.writeItems(itemFilter, 0, tag);
        tag.put(NBT_FLUID_FILTERS, fluidFilter.writeToNbt());
        tag.putInt(NBT_AMOUNT, amount);
        tag.putBoolean(NBT_MISSING, isMissingItems);
        return tag;
    }

    @Override
    public void readConfiguration(CompoundNBT tag) {
        super.readConfiguration(tag);
        if (tag.contains(NBT_TYPE)) {
            type = tag.getInt(NBT_TYPE);
        }
        StackUtils.readItems(itemFilter, 0, tag);
        if (tag.contains(NBT_FLUID_FILTERS)) {
            fluidFilter.readFromNbt(tag.getCompound(NBT_FLUID_FILTERS));
        }
        if (tag.contains(NBT_AMOUNT)) {
            amount = tag.getInt(NBT_AMOUNT);
        }
        if (tag.contains(NBT_MISSING)) {
            isMissingItems = tag.getBoolean(NBT_MISSING);
        }
    }
}
