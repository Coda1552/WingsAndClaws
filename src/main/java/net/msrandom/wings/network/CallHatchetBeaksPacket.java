package net.msrandom.wings.network;

import net.minecraft.entity.player.PlayerEntity;
import net.msrandom.wings.entity.WingsEntities;

public class CallHatchetBeaksPacket implements INetworkPacket {
    @Override
    public void handle(PlayerEntity player) {
        player.world.getEntitiesWithinAABB(
                WingsEntities.HATCHET_BEAK,
                player.getBoundingBox().grow(256).offset(0, 128, 0),
                entity -> entity.isOwner(player)
        ).stream()
                .reduce((a, b) -> a.getDistanceSq(player) < b.getDistanceSq(player) ? a : b)
                .ifPresent(entity -> entity.targetSupplier = player::getPositionVec);
    }
}
