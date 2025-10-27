package com.buuz135.refinedstoragerequestify.block.tile;

import com.buuz135.refinedstoragerequestify.RSRContent;
import com.buuz135.refinedstoragerequestify.container.RequesterContainer;
import com.buuz135.refinedstoragerequestify.container.RequesterData;
import com.refinedmods.refinedstorage.api.network.impl.node.iface.InterfaceTransferResult;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.api.support.network.InWorldNetworkNodeContainer;
import com.refinedmods.refinedstorage.common.api.support.resource.ResourceContainer;
import com.refinedmods.refinedstorage.common.support.FilterWithFuzzyMode;
import com.refinedmods.refinedstorage.common.support.containermenu.NetworkNodeExtendedMenuProvider;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicator;
import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicators;
import com.refinedmods.refinedstorage.common.support.network.AbstractBaseNetworkNodeContainerBlockEntity;
import com.refinedmods.refinedstorage.common.support.network.SimpleConnectionStrategy;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerData;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerImpl;
import com.refinedmods.refinedstorage.common.upgrade.UpgradeContainer;
import com.refinedmods.refinedstorage.common.util.ContainerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class RequesterBlockEntity extends AbstractBaseNetworkNodeContainerBlockEntity<RequesterNetworkNode> implements NetworkNodeExtendedMenuProvider<RequesterData> {

    private static final int EXPORT_SLOTS = 9;
    private static final String TAG_UPGRADES = "upgr";

    private final FilterWithFuzzyMode filter;
    private final UpgradeContainer upgradeContainer;

    public RequesterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, new RequesterNetworkNode(16));
        this.filter = FilterWithFuzzyMode.create(createFilterContainer(), this::setChanged);
        this.upgradeContainer = new UpgradeContainer(4, RSRContent.REQUESTER_DESTINATION, (c, upgradeEnergyUsage) -> {
            final long baseEnergyUsage = 16;
            mainNetworkNode.setEnergyUsage(baseEnergyUsage + upgradeEnergyUsage);
            setChanged();
        });
        this.mainNetworkNode.setFilter(this.filter);
        this.mainNetworkNode.setUpgradeContainer(this.upgradeContainer);
    }

    public static ResourceContainer createFilterContainer() {
        return new ResourceContainerImpl(
                EXPORT_SLOTS,
                value -> Integer.MAX_VALUE,
                RefinedStorageApi.INSTANCE.getItemResourceFactory(),
                RefinedStorageApi.INSTANCE.getAlternativeResourceFactories()
        );
    }

    public static ResourceContainer createFilterContainer(final RequesterData interfaceData) {
        final ResourceContainer filterContainer = createFilterContainer();
        final ResourceContainerData resourceContainerData = interfaceData.filterContainerData();
        for (int i = 0; i < resourceContainerData.resources().size(); ++i) {
            final int ii = i;
            resourceContainerData.resources().get(i).ifPresent(resource -> filterContainer.set(ii, resource));
        }
        return filterContainer;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level != null) {
            this.mainNetworkNode.setLevel(level);
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(final int syncId, final Inventory inventory, final Player player) {
        return new RequesterContainer(syncId, player, this, filter.getFilterContainer(), upgradeContainer, getExportingIndicators());
    }


    @Override
    public RequesterData getMenuData() {
        return new RequesterData(ResourceContainerData.of(filter.getFilterContainer()), getExportingIndicators().getAll());
    }

    @Override
    public StreamEncoder<RegistryFriendlyByteBuf, RequesterData> getMenuCodec() {
        return RequesterData.STREAM_CODEC;
    }


    private ExportingIndicators getExportingIndicators() {
        return new ExportingIndicators(filter.getFilterContainer(), i -> toExportingIndicator(mainNetworkNode.getLastResult(i)), true);
    }

    private ExportingIndicator toExportingIndicator(@Nullable final InterfaceTransferResult result) {
        return switch (result) {
            case STORAGE_DOES_NOT_ACCEPT_RESOURCE -> ExportingIndicator.DESTINATION_DOES_NOT_ACCEPT_RESOURCE;
            case RESOURCE_MISSING -> ExportingIndicator.RESOURCE_MISSING;
            case AUTOCRAFTING_STARTED -> ExportingIndicator.AUTOCRAFTING_WAS_STARTED;
            case AUTOCRAFTING_MISSING_RESOURCES -> ExportingIndicator.AUTOCRAFTING_MISSING_RESOURCES;
            case null, default -> ExportingIndicator.NONE;
        };
    }

    @Override
    public Component getName() {
        return Component.translatable("block.rsrequestify.requester");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    protected InWorldNetworkNodeContainer createMainContainer(RequesterNetworkNode networkNode) {
        return RefinedStorageApi.INSTANCE.createNetworkNodeContainer(this, networkNode)
                .connectionStrategy(new SimpleConnectionStrategy(getBlockPos()))
                .build();
    }

    @Override
    public void saveAdditional(final CompoundTag tag, final HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put(TAG_UPGRADES, ContainerUtil.write(upgradeContainer, provider));
    }

    @Override
    public void loadAdditional(final CompoundTag tag, final HolderLookup.Provider provider) {
        if (tag.contains(TAG_UPGRADES)) {
            ContainerUtil.read(tag.getCompound(TAG_UPGRADES), upgradeContainer, provider);
        }
        super.loadAdditional(tag, provider);
    }

    @Override
    public void writeConfiguration(final CompoundTag tag, final HolderLookup.Provider provider) {
        super.writeConfiguration(tag, provider);
        filter.save(tag, provider);
    }

    @Override
    public void readConfiguration(final CompoundTag tag, final HolderLookup.Provider provider) {
        super.readConfiguration(tag, provider);
        filter.load(tag, provider);
    }

}
