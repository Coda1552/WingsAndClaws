package coda.wingsandclaws.network;

import coda.wingsandclaws.client.WingsSounds;
import coda.wingsandclaws.entity.WingsEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;

public class CallHatchetBeaksPacket implements INetworkPacket {
    @Override
    public void handle(PlayerEntity player) {
        final Vector3d playerPosition = player.getPositionVec();
        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), WingsSounds.PLAYER_WHISTLE.get(), SoundCategory.PLAYERS, 0.5f, 0.4f / (player.getRNG().nextFloat() * 0.4f + 0.8f));
        player.world.getEntitiesWithinAABB(
                WingsEntities.HATCHET_BEAK.get(),
                player.getBoundingBox().grow(256).offset(0, 128, 0),
                entity -> entity.isOwner(player)
        )
                .stream()
                .reduce((a, b) -> a.getDistanceSq(player) < b.getDistanceSq(player) ? a : b)
                .ifPresent(entity -> entity.callerPosition = playerPosition);
    }
}
