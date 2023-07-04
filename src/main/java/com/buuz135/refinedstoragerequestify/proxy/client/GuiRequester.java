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
import com.buuz135.refinedstoragerequestify.proxy.block.tile.TileRequester;
import com.buuz135.refinedstoragerequestify.proxy.container.ContainerRequester;
import com.refinedmods.refinedstorage.blockentity.DetectorBlockEntity;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import com.refinedmods.refinedstorage.render.RenderSettings;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class GuiRequester extends BaseScreen<ContainerRequester> {

    private EditBox textField;

    public GuiRequester(ContainerRequester container) {
        super(container, 211, 137, container.getPlayer().getInventory(), Component.translatable("block.refinedstoragerequestify:requester.name"));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            minecraft.player.closeContainer();
            return true;
        }
        if (textField.keyPressed(keyCode, scanCode, modifiers) || textField.canConsumeInput()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void onPostInit(int x, int y) {
        addSideButton(new RedstoneModeSideButton(this, TileRequester.REDSTONE_MODE));
        addSideButton(new TypeSideButton(this, TileRequester.TYPE));
        textField = new EditBox(Minecraft.getInstance().font, x + 86, y + 41, 80, 10, Component.literal(""));
        textField.setValue(TileRequester.AMOUNT.getValue() + "");
        textField.setValue(String.valueOf(DetectorBlockEntity.AMOUNT.getValue()));
        //textField.setEnableBackgroundDrawing(false);
        textField.setVisible(true);
        textField.setCanLoseFocus(true);
        textField.setFocused(false);
        textField.setTextColor(RenderSettings.INSTANCE.getSecondaryColor());
        textField.setResponder(value -> {
            try {
                int result = Integer.parseInt(value);

                BlockEntitySynchronizationManager.setParameter(TileRequester.AMOUNT, result);
            } catch (NumberFormatException e) {
                // NO OP
            }
        });
        addWidget(textField);
    }

    public EditBox getTextField() {
        return textField;
    }

    @Override
    public void tick(int i, int i1) {
        textField.tick();
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int x, int y, int mouseX, int mouseY) {

        graphics.blit(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "textures/gui/requester.png"), x, y, 0, 0, this.imageWidth, this.imageHeight);

        if (TileRequester.MISSING.getValue()) {
            graphics.blit(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "textures/gui/crafting_preview.png"), x + 153, y + 1, 0, 256 - 16, 16, 16);
        }
        textField.render(graphics, mouseX, mouseY, 0);
    }

    @Override
    public void renderForeground(GuiGraphics graphics, int mouseX, int mouseY) {
        renderString(graphics, 7, 7, Component.translatable("block.rsrequestify.requester").getString());
        renderString(graphics, 7, 43, Component.translatable("container.inventory").getString());
        if (TileRequester.MISSING.getValue() && isHovering(153, 1, 16, 16, mouseX + leftPos, mouseY + topPos)) {
            renderTooltip(graphics, mouseX, mouseY, Component.translatable("tooltip.refinedstoragerequestify:requester.missing").getString());
        }
    }
}
