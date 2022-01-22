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
import com.mojang.blaze3d.vertex.PoseStack;
import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.blockentity.DetectorBlockEntity;
import com.refinedmods.refinedstorage.blockentity.data.BlockEntitySynchronizationManager;
import com.refinedmods.refinedstorage.render.RenderSettings;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class GuiRequester extends BaseScreen<ContainerRequester> {

    private EditBox textField;

    public GuiRequester(ContainerRequester container) {
        super(container, 211, 137, container.getPlayer().getInventory(), new TranslatableComponent("block.refinedstoragerequestify:requester.name"));
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
        textField = new EditBox(Minecraft.getInstance().font, x + 86, y + 41, 80, 10, new TextComponent(""));
        textField.setValue(TileRequester.AMOUNT.getValue() + "");
        textField.setValue(String.valueOf(DetectorBlockEntity.AMOUNT.getValue()));
        //textField.setEnableBackgroundDrawing(false);
        textField.setVisible(true);
        textField.setCanLoseFocus(true);
        textField.setFocus(false);
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
    public void renderBackground(PoseStack poseStack, int x, int y, int mouseX, int mouseY) {
        bindTexture(RefinedStorageRequestify.MOD_ID, "gui/requester.png");

        blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
        if (TileRequester.MISSING.getValue()) {
            bindTexture(RS.ID, "gui/crafting_preview.png");
            blit(poseStack, x + 153, y + 1, 0, 256 - 16, 16, 16);
        }
        textField.render(poseStack, mouseX, mouseY, 0);
    }

    @Override
    public void renderForeground(PoseStack poseStack, int mouseX, int mouseY) {
        renderString(poseStack, 7, 7, new TranslatableComponent("block.rsrequestify.requester").getString());
        renderString(poseStack, 7, 43, new TranslatableComponent("container.inventory").getString());
        if (TileRequester.MISSING.getValue() && isHovering(153, 1, 16, 16, mouseX + leftPos, mouseY + topPos)) {
            renderTooltip(poseStack, new TranslatableComponent("tooltip.refinedstoragerequestify:requester.missing"), mouseX, mouseY);
        }
    }
}
