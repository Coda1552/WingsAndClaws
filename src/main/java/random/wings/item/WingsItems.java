package random.wings.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;
import random.wings.WingsAndClaws;
import random.wings.block.WingsBlocks;
import random.wings.client.renderer.NestItemRenderer;
import random.wings.entity.PlowheadEggEntity;

import java.util.ArrayList;
import java.util.List;

public class WingsItems {
    public static final ItemGroup GROUP = new ItemGroup(WingsAndClaws.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(DUMPY_EGG_DRAKE_EGG);
        }
    };
    public static final List<Item> LIST = new ArrayList<>();
    public static final Item DRAGON_EGG = new DragonEggItem();//add("dragon_egg", );
    public static final Item DUMPY_EGG_DRAKE_EGG = add("dumpy_egg_drake_egg", new Item(new Item.Properties().group(GROUP)));
    public static final Item DRAGON_MEAT = add("dragon_meat", new Item(new Item.Properties().food(WingsFoods.DRAGON_MEAT).group(GROUP)));
    public static final Item COOKED_DRAGON_MEAT = add("cooked_dragon_meat", new Item(new Item.Properties().food(WingsFoods.COOKED_DRAGON_MEAT).group(GROUP)));
    public static final Item GLACIAL_PLANKTON = add("glacial_plankton", new Item(new Item.Properties().group(GROUP)));
    public static final Item GLISTENING_GLACIAL_PLANKTON = add("glistening_glacial_plankton", new Item(new Item.Properties().group(GROUP)){
        @Override
        public boolean hasEffect(ItemStack stack) {
            return true;
        }
    });
    public static final Item ICY_PLOWHEAD_EGG = add("icy_plowhead_egg", new Item(new Item.Properties().group(GROUP)){
        @Override
        public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            if (!playerIn.abilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            if (!worldIn.isRemote) {
                PlowheadEggEntity eggentity = new PlowheadEggEntity(worldIn, playerIn);
                eggentity.func_213884_b(itemstack);
                eggentity.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
                worldIn.addEntity(eggentity);
            }

            playerIn.addStat(Stats.ITEM_USED.get(this));
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
    });

    static {
        add("nest", new BlockItem(WingsBlocks.NEST, new Item.Properties().group(WingsItems.GROUP).setTEISR(() -> NestItemRenderer::new)));
    }

    private static Item add(String name, Item item) {
        LIST.add(item.setRegistryName(name));
        return item;
    }
}
