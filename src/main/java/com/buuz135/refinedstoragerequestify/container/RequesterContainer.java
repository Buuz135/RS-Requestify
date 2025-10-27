package com.buuz135.refinedstoragerequestify.container;

import com.buuz135.refinedstoragerequestify.RSRContent;
import com.buuz135.refinedstoragerequestify.block.tile.RequesterBlockEntity;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.support.RedstoneMode;
import com.refinedmods.refinedstorage.common.support.containermenu.*;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicator;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicatorListener;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicators;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

import java.util.function.Predicate;

public class RequesterContainer extends AbstractResourceContainerMenu implements ExportingIndicatorListener {
    private static final int EXPORT_CONFIG_SLOT_X = 8;
    private static final int EXPORT_CONFIG_SLOT_Y = 20;

    private final ExportingIndicators indicators;
    private final Predicate<Player> stillValid;

    public RequesterContainer(final int syncId,
                              final Player player,
                              final RequesterBlockEntity blockEntity,
                              final ResourceContainer exportConfig,
                              final UpgradeContainer upgradeContainer,
                              final ExportingIndicators indicators) {
        super(RSRContent.Menus.REQUESTER.get(), syncId, player);
        addSlots(player, exportConfig, upgradeContainer);
        registerProperty(new ServerProperty<>(
                PropertyTypes.REDSTONE_MODE,
                blockEntity::getRedstoneMode,
                blockEntity::setRedstoneMode
        ));
        this.indicators = indicators;
        this.stillValid = p -> Container.stillValidBlockEntity(blockEntity, p);
    }

    public RequesterContainer(final int syncId,
                              final Inventory playerInventory,
                              final RequesterData interfaceData) {
        super(RSRContent.Menus.REQUESTER.get(), syncId);
        final ResourceContainer filterContainer = RequesterBlockEntity.createFilterContainer(interfaceData);
        addSlots(playerInventory.player, filterContainer, new UpgradeContainer(RSRContent.REQUESTER_DESTINATION, 4));
        registerProperty(new ClientProperty<>(PropertyTypes.FUZZY_MODE, false));
        registerProperty(new ClientProperty<>(PropertyTypes.REDSTONE_MODE, RedstoneMode.IGNORE));
        this.indicators = new ExportingIndicators(interfaceData.exportingIndicators());
        this.stillValid = p -> true;
    }

    private static int getExportSlotX(final int index) {
        return EXPORT_CONFIG_SLOT_X + (18 * index);
    }

    private void addSlots(final Player player, final ResourceContainer exportConfig, final UpgradeContainer upgradeContainer) {
        for (int i = 0; i < exportConfig.size(); ++i) {
            addSlot(createExportConfigSlot(exportConfig, i));
        }
        addSlot(new UpgradeSlot(upgradeContainer, 0, 187, 6));
        addSlot(new UpgradeSlot(upgradeContainer, 1, 187, 6 + 18));
        addSlot(new UpgradeSlot(upgradeContainer, 2, 187, 6 + 18 * 2));
        addSlot(new UpgradeSlot(upgradeContainer, 3, 187, 6 + 18 * 3));
        addPlayerInventory(player.getInventory(), 8, 55);
        transferManager.addFilterTransfer(player.getInventory());
    }

    private Slot createExportConfigSlot(
            final ResourceContainer exportConfig,
            final int index
    ) {
        final int x = getExportSlotX(index);
        return new ResourceSlot(
                exportConfig,
                index,
                Component.translatable("block.rsrequestify.requester.tooltip.filter"),
                x,
                EXPORT_CONFIG_SLOT_Y,
                ResourceSlotType.FILTER_WITH_AMOUNT
        );
    }

    public ExportingIndicator getIndicator(final int idx) {
        return indicators.get(idx);
    }

    public int getIndicators() {
        return indicators.size();
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        if (player instanceof ServerPlayer serverPlayer) {
            indicators.detectChanges(serverPlayer);
        }
    }

    @Override
    public boolean stillValid(final Player player) {
        return stillValid.test(player);
    }

    @Override
    public void indicatorChanged(final int index, final ExportingIndicator indicator) {
        indicators.set(index, indicator);
    }
}
