package com.buuz135.refinedstoragerequestify.block.tile;

import com.refinedmods.refinedstorage.api.autocrafting.status.TaskStatus;
import com.refinedmods.refinedstorage.api.network.autocrafting.AutocraftingNetworkComponent;
import com.refinedmods.refinedstorage.api.network.impl.node.SimpleNetworkNode;
import com.refinedmods.refinedstorage.api.network.impl.node.iface.InterfaceTransferResult;
import com.refinedmods.refinedstorage.common.support.FilterWithFuzzyMode;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CraftingEmitterNetworkNode extends SimpleNetworkNode {

    private FilterWithFuzzyMode filter;
    private Level level;
    private InterfaceTransferResult[] results;
    private Consumer<Boolean> shouldEmmitRedstone;

    public CraftingEmitterNetworkNode(long energyUsage) {
        super(energyUsage);
    }

    public void setFilter(FilterWithFuzzyMode filter) {
        this.filter = filter;
        this.results = new InterfaceTransferResult[filter.getFilterContainer().size()];
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setShouldEmmitRedstone(Consumer<Boolean> shouldEmmitRedstone) {
        this.shouldEmmitRedstone = shouldEmmitRedstone;
    }

    @Override
    public void doWork() {
        super.doWork();
        if (network != null && isActive() && this.filter != null && this.level != null && this.level.getGameTime() % 20 == 0) {
            var craftingComponent = network.getComponent(AutocraftingNetworkComponent.class);
            for (int i = 0; i < this.filter.getFilterContainer().size(); i++) {
                try {
                    var resource = this.filter.getFilterContainer().get(i);
                    if (resource == null) {
                        results[i] = InterfaceTransferResult.EXPORTED;
                        continue;
                    }
                    var patterns = craftingComponent.getPatternsByOutput(resource.resource());
                    for (var pattern : patterns) {
                        var patterProvider = craftingComponent.getProviderByPattern(pattern);
                        for (TaskStatus taskStatus : patterProvider.getTaskStatuses()) {
                            if (taskStatus.info().resource().equals(resource.resource()) && taskStatus.info().amount() >= resource.amount()) {
                                this.shouldEmmitRedstone.accept(true);
                                return;
                            }
                        }
                    }
                } catch (IllegalStateException e) {
                    results[i] = InterfaceTransferResult.RESOURCE_MISSING;
                }
            }
            this.shouldEmmitRedstone.accept(false);
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
