package com.buuz135.refinedstoragerequestify.proxy.client;

import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import com.raoulvdberge.refinedstorage.container.ContainerBase;
import com.raoulvdberge.refinedstorage.container.slot.filter.SlotFilter;
import com.raoulvdberge.refinedstorage.container.slot.filter.SlotFilterFluid;
import com.raoulvdberge.refinedstorage.tile.config.IType;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerRequester extends ContainerBase {

    public ContainerRequester(TileRequester tile, EntityPlayer player) {
        super(tile, player);
        addSlotToContainer(new SlotFilter(tile.getNode().getItemFilters(), 0, 17, 20).setEnableHandler(() -> tile.getNode().getType() == IType.ITEMS));
        addSlotToContainer(new SlotFilterFluid(tile.getNode().getFluidFilters(), 0, 17, 20).setEnableHandler(() -> tile.getNode().getType() == IType.FLUIDS));
        addPlayerInventory(8, 55);
        transferManager.addFilterTransfer(player.inventory, tile.getNode().getItemFilters(), tile.getNode().getFluidFilters(), tile.getNode()::getType);
    }
}
