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

import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import com.buuz135.refinedstoragerequestify.proxy.config.RequestifyConfig;
import com.raoulvdberge.refinedstorage.api.autocrafting.task.ICraftingTask;
import com.raoulvdberge.refinedstorage.api.util.Action;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.NetworkNode;
import com.raoulvdberge.refinedstorage.inventory.fluid.FluidInventory;
import com.raoulvdberge.refinedstorage.inventory.item.ItemHandlerBase;
import com.raoulvdberge.refinedstorage.inventory.listener.ListenerNetworkNode;
import com.raoulvdberge.refinedstorage.tile.config.IType;
import com.raoulvdberge.refinedstorage.util.StackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class NetworkNodeRequester extends NetworkNode implements IType {

    public static final String ID = "requester";

    private static final String NBT_TYPE = "Type";
    private static final String NBT_FLUID_FILTERS = "FluidFilter";
    private static final String NBT_AMOUNT = "Amount";
    private static final String NBT_MISSING = "MissingItems";


    private ItemHandlerBase itemFilter = new ItemHandlerBase(9, new ListenerNetworkNode(this));
    private FluidInventory fluidFilter = new FluidInventory(9, new ListenerNetworkNode(this));

    private int type = IType.ITEMS;
    private int amount = 0;
    private int slot = 0;
    private boolean isMissingItems = false;
    private int attemptAmount = RequestifyConfig.maxCraftAmount;
    private ICraftingTask craftingTask = null;
    private int ticksBetweenUpdates = RequestifyConfig.ticksBetweenUpdates;

    public NetworkNodeRequester(World world, BlockPos pos) {
        super(world, pos);
    }

    @Override
    public void update() {
        super.update();
        if (network == null) return;
        if (canUpdate() && ticks % ticksBetweenUpdates == 0 && (craftingTask == null || !network.getCraftingManager().getTasks().contains(craftingTask))) {
            if (type == IType.ITEMS) {
                while (slot < itemFilter.getSlots() && itemFilter.getStackInSlot(slot).isEmpty()) {
                    ++slot;
                }
                if (slot >= itemFilter.getSlots()) {
                    slot = 0;
                }
                ItemStack filter = itemFilter.getStackInSlot(slot);
                if (!filter.isEmpty()) {
                    ItemStack current = network.extractItem(filter, amount, Action.SIMULATE);
                    if (current == null || current.isEmpty() || current.getCount() < amount) {
                        int count = current == null || current.isEmpty() ? amount : amount - current.getCount();
                        if (count > 0) {
                            craftingTask = network.getCraftingManager().request(this, filter, Math.min(attemptAmount, count));
                            isMissingItems = true;
                        }
                    } else {
                        isMissingItems = false;
                    }
                }
                ++slot;
                if (slot >= itemFilter.getSlots()) {
                    slot = 0;
                }
                if (attemptAmount > amount) {
                    attemptAmount = amount;
                }
                if (craftingTask == null) {
                    if (attemptAmount <= 1) {
                        attemptAmount = RequestifyConfig.maxCraftAmount;
                    }
                    attemptAmount /= 2;
                }
            }
            if (type == IType.FLUIDS) {
                while (fluidFilter.getFluid(slot) == null && slot < fluidFilter.getSlots()) {
                    ++slot;
                }
                if (slot >= fluidFilter.getSlots()) {
                    slot = 0;
                }
                FluidStack filter = fluidFilter.getFluid(slot);
                if (filter != null) {
                    FluidStack current = network.extractFluid(filter, amount, Action.SIMULATE);
                    if (current == null || current.amount < amount) {
                        int count = current == null ? amount : amount - current.amount;
                        if (count > 0) {
                            craftingTask = network.getCraftingManager().request(this, filter, count);
                            isMissingItems = true;
                        }
                    } else {
                        isMissingItems = false;
                    }
                }
                ++slot;
                if (slot >= itemFilter.getSlots()) {
                    slot = 0;
                }
                if (attemptAmount > amount) {
                    attemptAmount = amount;
                }
                if (craftingTask == null) {
                    if (attemptAmount <= 1) {
                        attemptAmount = RequestifyConfig.maxCraftAmount;
                    }
                    attemptAmount /= 2;
                }
            }
        }
    }

    @Override
    public int getEnergyUsage() {
        return 10;
    }

    @Override
    public String getId() {
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
        System.out.println("Amount getted: " + amount);
        return amount;
    }

    public void setAmount(int amount) {
        System.out.println("setAmount: " + amount);
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
    public void read(NBTTagCompound tag) {
        super.read(tag);
        StackUtils.readItems(itemFilter, 0, tag);
        if (tag.hasKey(NBT_AMOUNT)) {
            amount = tag.getInteger(NBT_AMOUNT);
        }
        if (tag.hasKey(NBT_MISSING)) {
            isMissingItems = tag.getBoolean(NBT_MISSING);
        }
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tag) {
        super.write(tag);
        StackUtils.writeItems(itemFilter, 0, tag);
        tag.setInteger(NBT_AMOUNT, amount);
        tag.setBoolean(NBT_MISSING, isMissingItems);
        return tag;
    }

    @Override
    public NBTTagCompound writeConfiguration(NBTTagCompound tag) {
        super.writeConfiguration(tag);
        tag.setInteger(NBT_TYPE, type);
        StackUtils.writeItems(itemFilter, 0, tag);
        tag.setTag(NBT_FLUID_FILTERS, fluidFilter.writeToNbt());
        tag.setInteger(NBT_AMOUNT, amount);
        tag.setBoolean(NBT_MISSING, isMissingItems);
        return tag;
    }

    @Override
    public void readConfiguration(NBTTagCompound tag) {
        super.readConfiguration(tag);
        if (tag.hasKey(NBT_TYPE)) {
            type = tag.getInteger(NBT_TYPE);
        }
        StackUtils.readItems(itemFilter, 0, tag);
        if (tag.hasKey(NBT_FLUID_FILTERS)) {
            fluidFilter.readFromNbt(tag.getCompoundTag(NBT_FLUID_FILTERS));
        }
        if (tag.hasKey(NBT_AMOUNT)) {
            amount = tag.getInteger(NBT_AMOUNT);
        }
        if (tag.hasKey(NBT_MISSING)) {
            isMissingItems = tag.getBoolean(NBT_MISSING);
        }
    }
}
