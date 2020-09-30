package net.msrandom.wings.tileentity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class NestTileEntity extends TileEntity implements ITickableTileEntity {
    public NestTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public abstract boolean addEgg();

    public abstract boolean removeEgg();

    public abstract int getEggCount();
}
