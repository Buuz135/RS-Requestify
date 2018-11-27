package com.buuz135.refinedstoragerequestify.proxy;

import com.buuz135.refinedstoragerequestify.proxy.client.GuiHandler;
import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.block.BlockRequester;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy {

    public static BlockRequester REQUESTER;

    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(RefinedStorageRequestify.INSTANCE, new GuiHandler());
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(REQUESTER = new BlockRequester());
    }

    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(REQUESTER).setRegistryName(REQUESTER.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    public void modelRegistryEvent(ModelRegistryEvent event) {

    }
}
