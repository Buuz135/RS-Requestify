package com.buuz135.refinedstoragerequestify.proxy.block.tile;

import com.buuz135.refinedstoragerequestify.proxy.client.GuiRequester;
import com.buuz135.refinedstoragerequestify.proxy.block.network.NetworkNodeRequester;
import com.raoulvdberge.refinedstorage.apiimpl.API;
import com.raoulvdberge.refinedstorage.gui.GuiBase;
import com.raoulvdberge.refinedstorage.tile.TileNode;
import com.raoulvdberge.refinedstorage.tile.config.IType;
import com.raoulvdberge.refinedstorage.tile.data.TileDataManager;
import com.raoulvdberge.refinedstorage.tile.data.TileDataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileRequester extends TileNode<NetworkNodeRequester> {

    public static final TileDataParameter<Integer, TileRequester> TYPE = IType.createParameter();
    public static final TileDataParameter<Integer, TileRequester> AMOUNT =  new TileDataParameter<>(DataSerializers.VARINT, 0, t -> t.getNode().getAmount(), (t, v) -> t.getNode().setAmount(v), (initial, p) -> GuiBase.executeLater(GuiRequester.class, requester -> requester.getAmount().setText(String.valueOf(p))));
    public static final TileDataParameter<Boolean, TileRequester> MISSING = new TileDataParameter<>(DataSerializers.BOOLEAN, false, tileRequester -> tileRequester.getNode().isMissingItems(), (tileRequester, aBoolean) -> {});

    static {
        TileDataManager.registerParameter(TYPE);
        TileDataManager.registerParameter(AMOUNT);
        TileDataManager.registerParameter(MISSING);
        API.instance().getNetworkNodeRegistry().add(NetworkNodeRequester.ID, (tag, world1, pos1) -> {
            NetworkNodeRequester networkNodeRequester = new NetworkNodeRequester(world1, pos1);
            networkNodeRequester.read(tag);
            return networkNodeRequester;
        });
    }

    public TileRequester() {
        dataManager.addWatchedParameter(TYPE);
        dataManager.addWatchedParameter(AMOUNT);
        dataManager.addParameter(MISSING);
    }

    @Override
    public NetworkNodeRequester createNode(World world, BlockPos pos) {
        return new NetworkNodeRequester(world, pos);
    }

    @Override
    public String getNodeId() {
        return NetworkNodeRequester.ID;
    }


}
