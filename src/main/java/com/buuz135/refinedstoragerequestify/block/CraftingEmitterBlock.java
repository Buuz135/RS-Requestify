package com.buuz135.refinedstoragerequestify.block;

import com.buuz135.refinedstoragerequestify.RSRContent;
import com.buuz135.refinedstoragerequestify.block.tile.CraftingEmitterBlockEntity;
import com.refinedmods.refinedstorage.common.support.AbstractBaseBlock;
import com.refinedmods.refinedstorage.common.support.AbstractBlockEntityTicker;
import com.refinedmods.refinedstorage.common.support.network.NetworkNodeBlockEntityTicker;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CraftingEmitterBlock extends AbstractBaseBlock implements EntityBlock {

    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 5, 16);
    private static final AbstractBlockEntityTicker<CraftingEmitterBlockEntity> TICKER = new NetworkNodeBlockEntityTicker<>(
            () -> (BlockEntityType<CraftingEmitterBlockEntity>) RSRContent.Blocks.CRAFTING_EMITTER_BE_TYPE.get(),
            null
    );
    public static BooleanProperty POWERED = BooleanProperty.create("powered");

    public CraftingEmitterBlock() {
        super(Properties.ofFullCopy(Blocks.IRON_BLOCK));
        registerDefaultState(getStateDefinition().any().setValue(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return (blockPos, blockState) -> new CraftingEmitterBlockEntity(RSRContent.Blocks.CRAFTING_EMITTER_BE_TYPE.get(), blockPos, blockState);
    }

    @Override
    public @Nullable <R extends BlockEntity> BlockEntityTicker<R> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<R> p_153214_) {
        return TICKER.get(p_153212_, p_153214_);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return this.getTileEntityFactory().create(p_153215_, p_153216_);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        BlockEntity entity = blockAccess.getBlockEntity(pos);
        if (entity instanceof CraftingEmitterBlockEntity craftingEmitterBlockEntity) {
            if (craftingEmitterBlockEntity.getShouldEmmitRedstone()) return 15;
        }
        return 0;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        BlockEntity entity = blockAccess.getBlockEntity(pos);
        if (entity instanceof CraftingEmitterBlockEntity craftingEmitterBlockEntity) {
            if (craftingEmitterBlockEntity.getShouldEmmitRedstone()) return 15;
        }
        return 0;
    }
}
