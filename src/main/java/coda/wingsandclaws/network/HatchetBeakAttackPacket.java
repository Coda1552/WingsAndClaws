package coda.wingsandclaws.network;

import coda.wingsandclaws.entity.HatchetBeakEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class HatchetBeakAttackPacket implements INetworkPacket {
    @Override
    public void handle(PlayerEntity player) {
        Entity ridingEntity = player.getVehicle();
        if (ridingEntity instanceof HatchetBeakEntity) {
            player.level.getEntities(ridingEntity, ridingEntity.getBoundingBox().move(ridingEntity.getLookAngle()))
                    .stream()
                    .reduce((e1, e2) -> e1.distanceToSqr(ridingEntity) > e2.distanceToSqr(ridingEntity) ? e2 : e1)
                    .ifPresent(((HatchetBeakEntity) ridingEntity)::performAttack);
        }
    }
}
