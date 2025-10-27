package com.buuz135.refinedstoragerequestify.client;

import com.buuz135.refinedstoragerequestify.container.CraftingEmitterContainer;
import com.refinedmods.refinedstorage.common.support.AbstractFilterScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CraftingEmitterScreen extends AbstractFilterScreen<CraftingEmitterContainer> {

    public CraftingEmitterScreen(final CraftingEmitterContainer menu, final Inventory playerInventory, final Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderTooltip(final GuiGraphics graphics, final int x, final int y) {
        if (renderExportingIndicators(graphics, leftPos, topPos - 10, x, y, getMenu().getIndicators(),
                getMenu()::getIndicator)) {
            return;
        }
        super.renderTooltip(graphics, x, y);
    }
}
