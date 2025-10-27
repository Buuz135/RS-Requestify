package com.buuz135.refinedstoragerequestify.block;

import com.buuz135.refinedstoragerequestify.RSRContent;
import com.buuz135.refinedstoragerequestify.block.tile.RequesterBlockEntity;
import com.refinedmods.refinedstorage.common.support.AbstractBaseBlock;
import com.refinedmods.refinedstorage.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.common.support.network.NetworkNodeBlockEntityTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RequesterBlock extends AbstractBaseBlock implements EntityBlock {

    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 5, 16);
    public static BooleanProperty CONNECTED = BooleanProperty.create("connected");
    private static final AbstractBlockEntityTicker<RequesterBlockEntity> TICKER = new NetworkNodeBlockEntityTicker<>(
            () -> (BlockEntityType<RequesterBlockEntity>) RSRContent.Blocks.REQUESTER_BE_TYPE.get(),
            CONNECTED
    );


    public RequesterBlock() {
        super(Properties.ofFullCopy(Blocks.IRON_BLOCK));
        registerDefaultState(getStateDefinition().any().setValue(CONNECTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONNECTED);
    }

    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return (blockPos, blockState) -> new RequesterBlockEntity(RSRContent.Blocks.REQUESTER_BE_TYPE.get(), blockPos, blockState);
    }

    @Override
    public @Nullable <R extends BlockEntity> BlockEntityTicker<R> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<R> p_153214_) {
        return TICKER.get(p_153212_, p_153214_);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return this.getTileEntityFactory().create(p_153215_, p_153216_);
    }

}
