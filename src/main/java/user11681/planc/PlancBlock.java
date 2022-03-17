package user11681.planc;

import java.util.EnumMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"deprecation", "ConstantConditions"})
public class PlancBlock extends BlockWithEntity {
    public static final PlancBlock instance = new PlancBlock(AbstractBlock.Settings.of(Material.WOOD));

    public static final EnumMap<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
    public static final double LENGTH = 1;
    public static final double WIDTH = 1D / 4;
    public static final double HEIGHT = 1D / 8;

    static {
        shapes.put(Direction.NORTH, VoxelShapes.cuboid(0, 0, 0, LENGTH, HEIGHT, WIDTH));
        shapes.put(Direction.EAST, VoxelShapes.cuboid(1 - WIDTH, 0, 0, 1, HEIGHT, LENGTH));
        shapes.put(Direction.SOUTH, VoxelShapes.cuboid(0, 0, 1 - WIDTH, LENGTH, HEIGHT, 1));
        shapes.put(Direction.WEST, VoxelShapes.cuboid(0, 0, 0, WIDTH, HEIGHT, LENGTH));
    }

    public PlancBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.getStateManager().getDefaultState().with(HorizontalFacingBlock.FACING, Direction.NORTH));
    }

    protected static BlockHitResult raycast(World world, Entity entity) {
        return world.raycast(new RaycastContext(entity.getCameraPosVec(0), entity.getCameraPosVec(0).add(entity.getRotationVector().multiply(5)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);

        builder.add(HorizontalFacingBlock.FACING);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        PlancBlockEntity entity = this.getBlockEntity(world, pos);

        return entity == null ? VoxelShapes.empty() : entity.shape;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return super.getVisualShape(state, world, pos, context);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return PlancBlockEntity.type.instantiate(pos, state);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        PlancBlockEntity blockEntity = this.getBlockEntity(world, pos);

        if (placer != null) {
            BlockHitResult raycast = raycast(world, placer);
            Vec3d hitPos = raycast.getPos();

            blockEntity.addPart(hitPos, placer.getHorizontalFacing());
        } else {
            blockEntity.addPart(state.get(HorizontalFacingBlock.FACING));
        }

        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (new BlockPos(hit.getPos()).equals(pos) && this.getBlockEntity(world, pos).place(player, player.getStackInHand(hand), hit.getPos())) {
            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(HorizontalFacingBlock.FACING, ctx.getPlayerFacing());
    }

    protected final PlancBlockEntity getBlockEntity(BlockView world, BlockPos pos) {
        return (PlancBlockEntity) world.getBlockEntity(pos);
    }
}
