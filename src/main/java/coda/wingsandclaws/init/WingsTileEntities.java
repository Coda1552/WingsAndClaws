package coda.wingsandclaws.init;

import coda.wingsandclaws.WingsAndClaws;
import coda.wingsandclaws.block.NestBlock;
import coda.wingsandclaws.init.WingsBlocks;
import coda.wingsandclaws.tileentity.DEDNestTileEntity;
import coda.wingsandclaws.tileentity.HBNestTileEntity;
import coda.wingsandclaws.tileentity.NestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class WingsTileEntities {
    public static final DeferredRegister<TileEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, WingsAndClaws.MOD_ID);
    public static final RegistryObject<TileEntityType<DEDNestTileEntity>> DED_NEST = create("ded_nest", DEDNestTileEntity::new, WingsBlocks.DED_NEST);
    public static final RegistryObject<TileEntityType<HBNestTileEntity>> HB_NEST = create("hb_nest", HBNestTileEntity::new, WingsBlocks.HB_NEST);

    private static <T extends NestTileEntity> RegistryObject<TileEntityType<T>> create(String name, Supplier<T> factory, Supplier<NestBlock<T>> block) {
        return REGISTRY.register(name, () -> {
            TileEntityType<T> type = TileEntityType.Builder.of(factory, block.get()).build(null);
            block.get().setItem(type);
            return type;
        });
    }
}
