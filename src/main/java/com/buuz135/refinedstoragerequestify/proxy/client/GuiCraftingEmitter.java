/*
 * This file is part of RSRequestifyu.
 *
 * Copyright 2021, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.buuz135.refinedstoragerequestify.proxy.client;

import com.buuz135.refinedstoragerequestify.RefinedStorageRequestify;
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileCraftingEmitter;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerCraftingEmitter;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiCraftingEmitter extends BaseScreen<ContainerCraftingEmitter> {


    public GuiCraftingEmitter(ContainerCraftingEmitter container) {
        super(container, 211, 137, container.getPlayer().getInventory(), Component.translatable("block.rsrequestify.crafting_emitter"));
    }

    @Override
    public void onPostInit(int x, int y) {
        //addSideButton(new RedstoneModeSideButton(this, TileCraftingEmitter.REDSTONE_MODE));
        addSideButton(new TypeSideButton(this, TileCraftingEmitter.TYPE));
    }

    @Override
    public void tick(int i, int i1) {

    }

    @Override
    public void renderBackground(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {
        //super.renderBg(graphics, renderPartialTicks, mouseX, mouseY);
        graphics.blit(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "textures/gui/crafting_emitter.png"), x, y, 0, 0, this.imageWidth, this.imageHeight);
    }


    @Override
    public void renderForeground(GuiGraphics guiGraphics, int i, int i1) {
        renderString(guiGraphics, 7, 7, Component.translatable("block.rsrequestify.crafting_emitter").getString());
        renderString(guiGraphics, 7, 43, Component.translatable("container.inventory").getString());
    }

}
