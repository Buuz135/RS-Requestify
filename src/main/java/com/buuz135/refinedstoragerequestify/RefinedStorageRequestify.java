package com.buuz135.refinedstoragerequestify;

import com.buuz135.refinedstoragerequestify.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
        modid = RefinedStorageRequestify.MOD_ID,
        name = RefinedStorageRequestify.MOD_NAME,
        version = RefinedStorageRequestify.VERSION,
        dependencies = "required-after:refinedstorage@[1.6.9-319,)"
)
public class RefinedStorageRequestify {

    public static final String MOD_ID = "refinedstoragerequestify";
    public static final String MOD_NAME = "RefinedStorageRequestify";
    public static final String VERSION = "${version}";

    @Mod.Instance(MOD_ID)
    public static RefinedStorageRequestify INSTANCE;
    @SidedProxy(clientSide = "com.buuz135.refinedstoragerequestify.proxy.ClientProxy", serverSide = "com.buuz135.refinedstoragerequestify.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static final CreativeTabs TAB = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(CommonProxy.REQUESTER);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {

        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
            proxy.registerItems(event);
        }

        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
            proxy.registerBlocks(event);
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void modelRegistryEvent(ModelRegistryEvent event) {
            proxy.modelRegistryEvent(event);
        }
    }

}
