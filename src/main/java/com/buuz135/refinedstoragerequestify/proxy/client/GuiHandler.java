package com.buuz135.refinedstoragerequestify.proxy.client;

import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    public static final int REQUESTER = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case REQUESTER:
                return new ContainerRequester((TileRequester) world.getTileEntity(new BlockPos(x, y, z)), player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case REQUESTER:
                return new GuiRequester((ContainerRequester) getServerGuiElement(ID, player, world, x, y, z));
        }
        return null;
    }
}
