package com.buuz135.refinedstoragerequestify.container;

import com.refinedmods.refinedstorage.common.support.exportingindicator.ExportingIndicator;
import com.refinedmods.refinedstorage.common.support.resource.ResourceContainerData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

import static com.refinedmods.refinedstorage.common.util.PlatformUtil.enumStreamCodec;

public record RequesterData(ResourceContainerData filterContainerData, List<ExportingIndicator> exportingIndicators) {
    public static final StreamCodec<RegistryFriendlyByteBuf, RequesterData> STREAM_CODEC = StreamCodec.composite(
            ResourceContainerData.STREAM_CODEC, RequesterData::filterContainerData,
            ByteBufCodecs.collection(ArrayList::new, enumStreamCodec(ExportingIndicator.values())),
            RequesterData::exportingIndicators,
            RequesterData::new
    );
}