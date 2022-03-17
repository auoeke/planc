package user11681.planc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@EnvironmentInterface(value = EnvType.CLIENT, itf = ClientModInitializer.class)
public class Planc implements ModInitializer, ClientModInitializer {
    public static final String ID = "planc";

    public static Identifier id(String path) {
        return new Identifier(ID, path);
    }

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, id("planc"), PlancBlock.instance);
        Registry.register(Registry.ITEM, id("planc"), PlancBlockItem.instance);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("planc"), PlancBlockEntity.type);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(PlancBlock.instance, RenderLayer.getCutoutMipped());
        BlockEntityRendererRegistry.INSTANCE.register(PlancBlockEntity.type, PlancBlockEntityRenderer::new);
    }
}
