package user11681.planc;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class PlancBlockItem extends BlockItem {
    public static final PlancBlockItem instance = new PlancBlockItem(PlancBlock.instance, new Item.Settings().group(ItemGroup.DECORATIONS));

    public PlancBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (super.useOnBlock(context) == ActionResult.FAIL) {
            BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos().offset(context.getSide()));

            if (blockEntity instanceof PlancBlockEntity && ((PlancBlockEntity) blockEntity).place(context.getPlayer(), context.getStack(), context.getHitPos())) {
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}
