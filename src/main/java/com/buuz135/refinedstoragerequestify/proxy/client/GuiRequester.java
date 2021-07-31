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
import com.mojang.blaze3d.matrix.MatrixStack;
import com.refinedmods.refinedstorage.RS;
import com.refinedmods.refinedstorage.render.RenderSettings;
import com.refinedmods.refinedstorage.screen.BaseScreen;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.RedstoneModeSideButton;
import com.refinedmods.refinedstorage.screen.widget.sidebutton.TypeSideButton;
import com.refinedmods.refinedstorage.tile.DetectorTile;
import com.refinedmods.refinedstorage.tile.data.TileDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

public class GuiRequester extends BaseScreen<ContainerRequester> {

    private TextFieldWidget textField;

    public GuiRequester(ContainerRequester container) {
        super(container, 211, 137, container.getPlayer().inventory, new TranslationTextComponent("block.refinedstoragerequestify:requester.name"));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            minecraft.player.closeScreen();
            return true;
        }
        if (textField.keyPressed(keyCode, scanCode, modifiers) || textField.canWrite()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void onPostInit(int x, int y) {
        addSideButton(new RedstoneModeSideButton(this, TileRequester.REDSTONE_MODE));
        addSideButton(new TypeSideButton(this, TileRequester.TYPE));
        textField = new TextFieldWidget(Minecraft.getInstance().fontRenderer, x + 86, y + 41, 80, 10, new StringTextComponent(""));
        textField.setText(TileRequester.AMOUNT.getValue() + "");
        textField.setText(String.valueOf(DetectorTile.AMOUNT.getValue()));
        //textField.setEnableBackgroundDrawing(false);
        textField.setVisible(true);
        textField.setCanLoseFocus(true);
        textField.setFocused2(false);
        textField.setTextColor(RenderSettings.INSTANCE.getSecondaryColor());
        textField.setResponder(value -> {
            try {
                int result = Integer.parseInt(value);

                TileDataManager.setParameter(TileRequester.AMOUNT, result);
            } catch (NumberFormatException e) {
                // NO OP
            }
        });
        addButton(textField);
    }

    public TextFieldWidget getTextField() {
        return textField;
    }

    @Override
    public void tick(int i, int i1) {
        textField.tick();
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int x, int y, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation(RefinedStorageRequestify.MOD_ID, "textures/gui/requester.png"));
        blit(matrixStack, x, y, 0, 0, this.xSize, this.ySize);
        if (TileRequester.MISSING.getValue()) {
            bindTexture(RS.ID, "gui/crafting_preview.png");
            blit(matrixStack, x + 153, y + 1, 0, 256 - 16, 16, 16);
        }
        textField.render(matrixStack, mouseX, mouseY, 0);
    }

    @Override
    public void renderForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
        renderString(matrixStack, 7, 7, new TranslationTextComponent("block.rsrequestify.requester").getString());
        renderString(matrixStack, 7, 43, new TranslationTextComponent("container.inventory").getString());
        if (TileRequester.MISSING.getValue() && isPointInRegion(153, 1, 16, 16, mouseX + guiLeft, mouseY + guiTop)) {
            renderTooltip(matrixStack, new TranslationTextComponent("tooltip.refinedstoragerequestify:requester.missing"), mouseX, mouseY);
        }
    }
}
