package com.buuz135.refinedstoragerequestify.proxy.block;

import com.buuz135.refinedstoragerequestify.proxy.client.GuiHandler;
import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.block.network.NetworkNodeRequester;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import com.raoulvdberge.refinedstorage.block.BlockNode;
import com.raoulvdberge.refinedstorage.block.info.BlockInfoBuilder;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRequester extends BlockNode {

    public BlockRequester() {
        super(BlockInfoBuilder.forMod(RefinedStorageRequestify.INSTANCE, RefinedStorageRequestify.MOD_ID, NetworkNodeRequester.ID).material(Material.IRON).soundType(SoundType.METAL).hardness(0.35F).tileEntity(TileRequester::new).create());
        GameRegistry.registerTileEntity(TileRequester.class, this.getRegistryName());
        setCreativeTab(RefinedStorageRequestify.TAB);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return openNetworkGui(GuiHandler.REQUESTER, player, world, pos, side);
    }

    @Override
    public boolean hasConnectedState() {
        return true;
    }
}
