package user11681.planc;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class PlancBlockEntityRenderer implements BlockEntityRenderer<PlancBlockEntity> {
    private static final BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
    private static final BlockModelRenderer blockModelRenderer = blockRenderManager.getModelRenderer();

    protected final BlockEntityRendererFactory.Context context;

    public PlancBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public void render(PlancBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        for (DirectionVec3d part : entity.parts) {
            matrices.push();
            matrices.translate(part.x, part.y, part.z);

/*
            switch (part.direction) {
                case NORTH:
                    matrices.multiply(new Quaternion(0, (float) -(Math.PI / 2), 0, false));
                    matrices.translate(0, 0, -PlancBlock.WIDTH);

                    break;
                case EAST:
                    matrices.multiply(new Quaternion(0, (float) -(Math.PI / 2), 0, false));
                    matrices.translate(-3 * PlancBlock.WIDTH, 0, -PlancBlock.LENGTH);

                    break;
                case SOUTH:
                    matrices.multiply(new Quaternion(0, (float) (Math.PI / 2), 0, false));
                    matrices.translate(-PlancBlock.LENGTH, 0, -3 * PlancBlock.WIDTH);

                    break;
                case WEST:
                    matrices.multiply(new Quaternion(0, (float) (Math.PI / 2), 0, false));
                    matrices.translate(-PlancBlock.WIDTH, 0, 0);
            }
*/

            World world = entity.getWorld();
            BlockState state = entity.getCachedState();
            BlockPos pos = entity.getPos();

            blockModelRenderer.render(
                world,
                blockRenderManager.getModel(state),
                state,
                pos,
                matrices,
                vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()),
                true,
                world == null ? new Random() : world.random,
                state.getRenderingSeed(pos),
                OverlayTexture.DEFAULT_UV
            );

            matrices.pop();
        }
    }
}
