package user11681.planc;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class DirectionVec3d extends Vec3d {
    public final Direction direction;

    public DirectionVec3d(Direction direction, double x, double y, double z) {
        super(x, y, z);

        this.direction = direction;
    }
}
