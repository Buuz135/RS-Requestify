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
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;

public class GuiRequester extends GuiBase {

    private GuiTextField textField;
    private GuiButton button;

    public GuiRequester(ContainerRequester container) {
        super(container, 211, 137);
    }

    @Override
    public void init(int x, int y) {
        addSideButton(new SideButtonRedstoneMode(this, TileRequester.REDSTONE_MODE));
        addSideButton(new SideButtonType(this, TileRequester.TYPE));
        textField = new GuiTextField(-135, Minecraft.getMinecraft().fontRenderer, x+20+18,y+23,80,10);
        textField.setText(TileRequester.AMOUNT.getValue() +"");
        textField.setCanLoseFocus(true);
        textField.setFocused(true);
        button = addButton(x+40+86,y+19,40,20,t("button.refinedstoragerequestify:requester.save"));
    }

    @Override
    public void update(int x, int y) {
        textField.updateCursorCounter();
    }

    @Override
    public void drawBackground(int x, int y, int mouseX, int mouseY) {
        bindTexture(RefinedStorageRequestify.MOD_ID, "gui/requester.png");
        drawTexture(x, y, 0, 0, screenWidth, screenHeight);
        if (TileRequester.MISSING.getValue()){
            bindTexture("gui/crafting_preview.png");
            drawTexture(x+153,y+1, 0,256-16,16,16);
        }
        textField.drawTextBox();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        drawString(7, 7, t("block.refinedstoragerequestify:requester.name"));
        drawString(7, 43, t("container.inventory"));
        if (TileRequester.MISSING.getValue() && isPointInRegion(153,1, 16,16, mouseX+guiLeft, mouseY+guiTop)){
            drawHoveringText(t("tooltip.refinedstoragerequestify:requester.missing"), mouseX, mouseY);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.player.closeScreen();
        }
        textField.textboxKeyTyped(typedChar, keyCode);
        StringBuilder builder = new StringBuilder();
        int pos = 0;
        for (char c : textField.getText().toCharArray()) {
            if (pos == 0 && c == '0' && textField.getText().length() > 1){
                continue;
            }
            if (NumberUtils.isCreatable(c+""))builder = builder.append(c);
            ++pos;
        }
        textField.setText(builder.toString());
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (this.button.equals(button) && NumberUtils.isCreatable(textField.getText())){
            long amount = NumberUtils.createLong(textField.getText());
            if (amount > Integer.MAX_VALUE) amount = Integer.MAX_VALUE;
            TileDataManager.setParameter(TileRequester.AMOUNT, (int)amount);
        }
    }

    public GuiTextField getAmount() {
        return textField;
    }
}
