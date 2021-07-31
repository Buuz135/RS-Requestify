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
import com.mojang.blaze3d.matrix.MatrixStack;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiCraftingEmitter extends BaseScreen<ContainerCraftingEmitter> {


    public GuiCraftingEmitter(ContainerCraftingEmitter container) {
        super(container, 211, 137, container.getPlayer().inventory, new TranslationTextComponent("block.rsrequestify.crafting_emitter"));
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
    public void renderBackground(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "textures/gui/crafting_emitter.png"));
        blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void renderForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
        renderString(matrixStack, 7, 7, new TranslationTextComponent("block.rsrequestify.crafting_emitter").getString());
        renderString(matrixStack, 7, 43, new TranslationTextComponent("container.inventory").getString());
    }
}
