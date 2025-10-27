package com.buuz135.refinedstoragerequestify;

import com.buuz135.refinedstoragerequestify.block.tile.CraftingEmitterBlockEntity;
import com.buuz135.refinedstoragerequestify.block.tile.RequesterBlockEntity;
import com.buuz135.refinedstoragerequestify.client.CraftingEmitterScreen;
import com.buuz135.refinedstoragerequestify.client.RequesterScreen;
import com.buuz135.refinedstoragerequestify.container.CraftingEmitterContainer;
import com.buuz135.refinedstoragerequestify.container.RequesterContainer;
import com.hrznstudio.titanium.event.handler.EventManager;
import com.hrznstudio.titanium.module.ModuleController;
import com.refinedmods.refinedstorage.common.api.RefinedStorageApi;
import com.refinedmods.refinedstorage.common.content.Items;
import com.refinedmods.refinedstorage.neoforge.api.RefinedStorageNeoForgeApi;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;


@Mod(RefinedStorageRequestify.MOD_ID)
public class RefinedStorageRequestify extends ModuleController {

    public static final String MOD_ID = "rsrequestify";

    public RefinedStorageRequestify(Dist dist, IEventBus modEventBus, ModContainer modContainer) {
        super(modContainer);
        EventManager.mod(RegisterCapabilitiesEvent.class).process(event -> {
            event.registerBlock(RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability(), (level, blockPos, blockState, blockEntity, direction) -> {
                if (blockEntity instanceof RequesterBlockEntity requesterBlockEntity) {
                    return requesterBlockEntity.getContainerProvider();
                }
                return null;
            }, RSRContent.Blocks.REQUESTER.get());
            event.registerBlock(RefinedStorageNeoForgeApi.INSTANCE.getNetworkNodeContainerProviderCapability(), (level, blockPos, blockState, blockEntity, direction) -> {
                if (blockEntity instanceof CraftingEmitterBlockEntity craftingEmitterBlockEntity) {
                    return craftingEmitterBlockEntity.getContainerProvider();
                }
                return null;
            }, RSRContent.Blocks.CRAFTING_EMITTER.get());
        }).subscribe();
        EventManager.mod(FMLCommonSetupEvent.class).process(event -> {
            RefinedStorageApi.INSTANCE.getUpgradeRegistry().forDestination(RSRContent.REQUESTER_DESTINATION).add(Items.INSTANCE.getStackUpgrade(), 4);
        }).subscribe();

        if (dist.isClient()) {
            EventManager.mod(RegisterMenuScreensEvent.class).process(event -> {
                event.register((MenuType<RequesterContainer>) RSRContent.Menus.REQUESTER.get(), RequesterScreen::new);
                event.register((MenuType<CraftingEmitterContainer>) RSRContent.Menus.CRAFTING_EMITTER.get(), CraftingEmitterScreen::new);

            }).subscribe();
        }
    }

    @Override
    protected void initModules() {
        addCreativeTab("main", () -> new ItemStack(RSRContent.Items.REQUESTER), "rsrequestify", RSRContent.TAB);

        RSRContent.Blocks.init();
        RSRContent.Items.init();
        RSRContent.Menus.init();

    }
}
