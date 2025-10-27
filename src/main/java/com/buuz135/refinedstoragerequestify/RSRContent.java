package com.buuz135.refinedstoragerequestify;

import com.buuz135.refinedstoragerequestify.block.CraftingEmitterBlock;
import com.buuz135.refinedstoragerequestify.block.RequesterBlock;
import com.buuz135.refinedstoragerequestify.container.CraftingEmitterContainer;
import com.buuz135.refinedstoragerequestify.container.CraftingEmitterData;
import com.buuz135.refinedstoragerequestify.container.RequesterContainer;
import com.buuz135.refinedstoragerequestify.container.RequesterData;
import com.hrznstudio.titanium.module.DeferredRegistryHelper;
import com.hrznstudio.titanium.tab.TitaniumTab;
import com.refinedmods.refinedstorage.common.api.upgrade.UpgradeDestination;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class RSRContent {

    public static DeferredRegistryHelper REGISTRY = new DeferredRegistryHelper(RefinedStorageRequestify.MOD_ID);
    public static TitaniumTab TAB = new TitaniumTab(ResourceLocation.fromNamespaceAndPath(RefinedStorageRequestify.MOD_ID, "main"));
    public static UpgradeDestination REQUESTER_DESTINATION = new UpgradeDestination() {
        @Override
        public Component getName() {
            return Component.translatable("block.rsrequestify.requester");
        }

        @Override
        public ItemStack getStackRepresentation() {
            return new ItemStack(Items.REQUESTER.get());
        }
    };

    public static class Blocks {

        public static DeferredHolder<Block, Block> REQUESTER = REGISTRY.registerGeneric(Registries.BLOCK, "requester", RequesterBlock::new);
        public static DeferredHolder<Block, Block> CRAFTING_EMITTER = REGISTRY.registerGeneric(Registries.BLOCK, "crafting_emitter", CraftingEmitterBlock::new);        public static DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> REQUESTER_BE_TYPE = REGISTRY.registerBlockEntityType("requester", () -> BlockEntityType.Builder.of(((RequesterBlock) REQUESTER.get()).getTileEntityFactory(), REQUESTER.get()).build(null));

        public static void init() {
        }
        public static DeferredHolder<BlockEntityType<?>, BlockEntityType<?>> CRAFTING_EMITTER_BE_TYPE = REGISTRY.registerBlockEntityType("crafting_emitter", () -> BlockEntityType.Builder.of(((CraftingEmitterBlock) CRAFTING_EMITTER.get()).getTileEntityFactory(), CRAFTING_EMITTER.get()).build(null));



    }

    public static class Items {

        public static DeferredHolder<Item, Item> REQUESTER = REGISTRY.registerGeneric(Registries.ITEM, "requester", () -> {
            var item = new BlockItem(Blocks.REQUESTER.get(), new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                    tooltipComponents.add(Component.translatable("block.rsrequestify.requester.tooltip").withStyle(ChatFormatting.GRAY));
                }
            };
            TAB.getTabList().add(item);
            return item;
        });
        public static DeferredHolder<Item, Item> CRAFTING_EMITTER = REGISTRY.registerGeneric(Registries.ITEM, "crafting_emitter", () -> {
            var item = new BlockItem(Blocks.CRAFTING_EMITTER.get(), new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                    tooltipComponents.add(Component.translatable("block.rsrequestify.crafting_emitter.tooltip").withStyle(ChatFormatting.GRAY));
                }
            };
            TAB.getTabList().add(item);
            return item;
        });

        public static void init() {
        }
    }

    public static class Menus {

        public static DeferredHolder<MenuType<?>, MenuType<?>> REQUESTER = REGISTRY.registerGeneric(Registries.MENU, "requester", () ->
                (MenuType) IMenuTypeExtension.create((i, inventory, registryFriendlyByteBuf) -> new RequesterContainer(i, inventory, RequesterData.STREAM_CODEC.decode(registryFriendlyByteBuf))));
        public static DeferredHolder<MenuType<?>, MenuType<?>> CRAFTING_EMITTER = REGISTRY.registerGeneric(Registries.MENU, "crafting_emitter", () ->
                (MenuType) IMenuTypeExtension.create((i, inventory, registryFriendlyByteBuf) -> new CraftingEmitterContainer(i, inventory, CraftingEmitterData.STREAM_CODEC.decode(registryFriendlyByteBuf))));

        public static void init() {
        }
    }

}
