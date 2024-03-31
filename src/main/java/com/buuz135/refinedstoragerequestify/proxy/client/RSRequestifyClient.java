package com.buuz135.refinedstoragerequestify.proxy.client;

import com.buuz135.refinedstoragerequestify.proxy.Registry;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerRequester;
import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class RSRequestifyClient {
    @SuppressWarnings("RedundantCast")
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(Registry.REQUESTER_CONTAINER.get(), (MenuScreens.ScreenConstructor<ContainerRequester, GuiRequester>) (container, p_create_2_, p_create_3_) -> new GuiRequester(container));
        event.register(Registry.CRAFTING_EMITTER_CONTAINER.get(), (MenuScreens.ScreenConstructor<ContainerCraftingEmitter, GuiCraftingEmitter>) (p_create_1_, p_create_2_, p_create_3_) -> new GuiCraftingEmitter(p_create_1_));
    }
}
