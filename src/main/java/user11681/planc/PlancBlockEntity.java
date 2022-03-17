package user11681.planc;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class PlancBlockEntity extends BlockEntity {
    public static final BlockEntityType<PlancBlockEntity> type = FabricBlockEntityTypeBuilder.create(PlancBlockEntity::new, PlancBlock.instance).build();

    public final ObjectArrayList<DirectionVec3d> parts = ObjectArrayList.wrap(new DirectionVec3d[0]);

    public VoxelShape shape = VoxelShapes.empty();

    public PlancBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    public boolean place(PlayerEntity player, ItemStack itemStack, Vec3d position) {
        if (itemStack.isOf(PlancBlockItem.instance) && this.addPart(position, player.getHorizontalFacing())) {
            if (!player.getAbilities().creativeMode) {
                itemStack.decrement(1);
            }

            return true;
        }

        return false;
    }

    public boolean addPart(Direction direction) {
        return this.addPart(new Vec3d(this.pos.getX(), this.pos.getY(), this.pos.getZ()), direction);
    }

    public boolean addPart(Vec3d position, Direction direction) {
        position = this.align(direction, position);

        if (this.parts.contains(position)) {
            return false;
        }

        this.parts.add((DirectionVec3d) position);
        this.shape = VoxelShapes.union(this.shape, this.relativeShape(direction, position));

        return true;
    }

    protected DirectionVec3d align(Direction direction, Vec3d vector) {
        double xLength;
        double zLength;

        switch (direction) {
            case NORTH:
            case SOUTH:
                xLength = PlancBlock.WIDTH;
                zLength = PlancBlock.LENGTH;

                break;
            case EAST:
            case WEST:
                xLength = PlancBlock.LENGTH;
                zLength = PlancBlock.WIDTH;

                break;
            default:
                throw new IllegalArgumentException(String.valueOf(direction));
        }

        return new DirectionVec3d(direction,
            xLength * Math.floor((vector.x - this.pos.getX()) / xLength),
            PlancBlock.HEIGHT * Math.floor((vector.y - this.pos.getY()) / PlancBlock.HEIGHT),
            zLength * Math.floor((vector.z - this.pos.getZ()) / zLength)
        );
    }

    protected VoxelShape relativeShape(Direction direction, Vec3d position) {
        switch (direction) {
            case NORTH:
            case SOUTH:
                return VoxelShapes.cuboid(position.x, position.y, 1 - PlancBlock.LENGTH, position.x + PlancBlock.WIDTH, position.y + PlancBlock.HEIGHT, 1);
            case EAST:
            case WEST:
                return VoxelShapes.cuboid(1 - PlancBlock.LENGTH, position.y, position.z, 1, position.y + PlancBlock.HEIGHT, position.z + PlancBlock.WIDTH);
        }

        return VoxelShapes.cuboid(position.x, position.y, position.z, position.x + PlancBlock.WIDTH, position.y + PlancBlock.HEIGHT, position.z + PlancBlock.LENGTH);
    }
}
