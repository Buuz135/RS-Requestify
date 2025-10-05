/*
 * This file is part of RS: Requestify.
 *
 * Copyright 2018, Buuz135
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
import com.raoulvdberge.refinedstorage.gui.GuiBase;
import com.raoulvdberge.refinedstorage.gui.control.SideButtonRedstoneMode;
import com.raoulvdberge.refinedstorage.gui.control.SideButtonType;
import com.raoulvdberge.refinedstorage.tile.data.TileDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GuiRequester extends GuiBase {

    private GuiTextField textField;
    private static final int AMOUNT_FIELD_ID = -/*buuz*/135;
    private GuiButton button;

    public GuiRequester(ContainerRequester container) {
        super(container, 211, 137);
    }

    @Override
    public void init(int x, int y) {
        addSideButton(new SideButtonRedstoneMode(this, TileRequester.REDSTONE_MODE));
        addSideButton(new SideButtonType(this, TileRequester.TYPE));
        textField = new GuiTextField(AMOUNT_FIELD_ID, Minecraft.getMinecraft().fontRenderer, x + 86, y + 41, 80, 10);
        textField.setText(String.valueOf(TileRequester.AMOUNT.getValue()));
        textField.setCanLoseFocus(true);
        textField.setFocused(true);
        textField.setValidator(s -> s.isEmpty() || s.matches("\\d+")); // только числа
        textField.setText(String.valueOf(TileRequester.AMOUNT.getValue()));
        textField.setGuiResponder(new net.minecraft.client.gui.GuiPageButtonList.GuiResponder() {
            @Override
            public void setEntryValue(int id, String value) {
                if (id != AMOUNT_FIELD_ID) return;
                if (value == null || value.isEmpty()) return;
                try {
                    int result = value.isEmpty() ? 0 : Integer.parseInt(value);
                    TileDataManager.setParameter(TileRequester.AMOUNT, result);
                    System.out.println("TileDataManager AMOUNT" + result);
                } catch (NumberFormatException ignored) { /* NO-OP */ }
            }
            @Override public void setEntryValue(int id, boolean value) { /* NO-OP */ }
            @Override public void setEntryValue(int id, float value)    { /* NO-OP */ }
        });
    }

    @Override
    public void update(int x, int y) {
        textField.updateCursorCounter();
    }

    @Override
    public void drawBackground(int x, int y, int mouseX, int mouseY) {
        bindTexture(RefinedStorageRequestify.MOD_ID, "gui/requester.png");
        drawTexture(x, y, 0, 0, screenWidth, screenHeight);
        if (TileRequester.MISSING.getValue()) {
            bindTexture("gui/crafting_preview.png");
            drawTexture(x + 153, y + 1, 0, 256 - 16, 16, 16);
        }
        textField.drawTextBox();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        drawString(7, 7, t("block.refinedstoragerequestify:requester.name"));
        drawString(7, 43, t("container.inventory"));
        if (TileRequester.MISSING.getValue() && isPointInRegion(153, 1, 16, 16, mouseX + guiLeft, mouseY + guiTop)) {
            drawHoveringText(t("tooltip.refinedstoragerequestify:requester.missing"), mouseX, mouseY);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 || keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.player.closeScreen();
            return;
        }

        if (!textField.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public GuiTextField getAmount() {
        return textField;
    }

//    @Override
//    public void onGuiClosed() {
//        super.onGuiClosed();
//        String value = textField.getText();
//        int result = 0;
//        if (!(value == null || value.trim().isEmpty())) {
//            try {
//                result = Integer.parseInt(value.trim());
//            } catch (NumberFormatException ignored) { /* NO-OP */ }
//        }
//        System.out.println(result);
//        TileDataManager.setParameter(TileRequester.AMOUNT, result);
//    }
}
