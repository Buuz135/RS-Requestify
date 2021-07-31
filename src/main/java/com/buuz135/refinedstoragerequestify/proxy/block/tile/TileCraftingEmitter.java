package com.buuz135.refinedstoragerequestify.proxy.block.tile;

import com.buuz135.refinedstoragerequestify.proxy.CommonProxy;
import com.buuz135.refinedstoragerequestify.proxy.block.network.NetworkNodeCraftingEmitter;
import com.refinedmods.refinedstorage.tile.NetworkNodeTile;
import com.refinedmods.refinedstorage.tile.config.IType;
import com.refinedmods.refinedstorage.tile.data.TileDataManager;
import com.refinedmods.refinedstorage.tile.data.TileDataParameter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileCraftingEmitter extends NetworkNodeTile<NetworkNodeCraftingEmitter> {

    public static final TileDataParameter<Integer, TileCraftingEmitter> TYPE = IType.createParameter();

    static {
        TileDataManager.registerParameter(TYPE);
    }

    public TileCraftingEmitter() {
        super(CommonProxy.CRAFTING_EMITTER_TYPE);
        dataManager.addWatchedParameter(TYPE);
    }

    @Override
    public NetworkNodeCraftingEmitter createNode(World world, BlockPos blockPos) {
        return new NetworkNodeCraftingEmitter(world, pos);
    }


}
