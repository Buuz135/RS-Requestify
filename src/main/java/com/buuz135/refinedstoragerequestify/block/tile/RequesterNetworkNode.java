package com.buuz135.refinedstoragerequestify.block.tile;

import com.refinedmods.refinedstorage.api.network.autocrafting.AutocraftingNetworkComponent;
import com.refinedmods.refinedstorage.api.network.impl.autocrafting.TimeoutableCancellationToken;
import com.refinedmods.refinedstorage.api.network.impl.node.SimpleNetworkNode;
import com.refinedmods.refinedstorage.api.network.impl.node.iface.InterfaceTransferResult;
import com.refinedmods.refinedstorage.api.network.storage.StorageNetworkComponent;
import com.refinedmods.refinedstorage.common.content.Items;
import com.refinedmods.refinedstorage.common.support.FilterWithFuzzyMode;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class RequesterNetworkNode extends SimpleNetworkNode {

    private FilterWithFuzzyMode filter;
    private UpgradeContainer upgradeContainer;
    private Level level;
    private InterfaceTransferResult[] results;

    public RequesterNetworkNode(long energyUsage) {
        super(energyUsage);
    }

    public void setFilter(FilterWithFuzzyMode filter) {
        this.filter = filter;
        this.results = new InterfaceTransferResult[filter.getFilterContainer().size()];
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setUpgradeContainer(UpgradeContainer upgradeContainer) {
        this.upgradeContainer = upgradeContainer;
    }

    @Override
    public void doWork() {
        super.doWork();
        if (network != null && isActive() && this.filter != null && this.level != null && this.level.getGameTime() % 10 == 0) {
            var craftingComponent = network.getComponent(AutocraftingNetworkComponent.class);
            var storageComponent = network.getComponent(StorageNetworkComponent.class);
            for (int i = 0; i < this.filter.getFilterContainer().size(); i++) {
                try {
                    var resource = this.filter.getFilterContainer().get(i);
                    if (resource == null) {
                        results[i] = InterfaceTransferResult.EXPORTED;
                        continue;
                    }
                    var amount = resource.amount();
                    var needed = amount - storageComponent.get(resource.resource());
                    if (needed <= 0) {
                        results[i] = InterfaceTransferResult.EXPORTED;
                        continue;
                    }
                    var toRequestMaxAmount = 64 * (1 + 8 * this.upgradeContainer.getAmount(Items.INSTANCE.getStackUpgrade()));
                    AutocraftingNetworkComponent.EnsureResult ensure = craftingComponent.ensureTask(resource.resource(), (long) Math.min(needed, toRequestMaxAmount), () -> "Requester", new TimeoutableCancellationToken());
                    if (ensure == AutocraftingNetworkComponent.EnsureResult.TASK_CREATED || ensure == AutocraftingNetworkComponent.EnsureResult.TASK_ALREADY_RUNNING) {
                        results[i] = InterfaceTransferResult.AUTOCRAFTING_STARTED;
                    } else if (ensure == AutocraftingNetworkComponent.EnsureResult.MISSING_RESOURCES) {
                        results[i] = InterfaceTransferResult.AUTOCRAFTING_MISSING_RESOURCES;
                    }
                } catch (IllegalStateException e) {
                    results[i] = InterfaceTransferResult.RESOURCE_MISSING;
                }
            }
        }
    }

    @Override
    protected void onActiveChanged(boolean newActive) {
        super.onActiveChanged(newActive);
    }

    @Nullable
    public InterfaceTransferResult getLastResult(final int slot) {
        if (results == null) {
            return null;
        }
        return results[slot];
    }
}
