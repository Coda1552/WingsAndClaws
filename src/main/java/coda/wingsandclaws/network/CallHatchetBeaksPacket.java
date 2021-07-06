package coda.wingsandclaws.network;

import coda.wingsandclaws.init.WingsSounds;
import coda.wingsandclaws.init.WingsEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;

public class CallHatchetBeaksPacket implements INetworkPacket {
    @Override
    public void handle(PlayerEntity player) {
        final Vector3d playerPosition = player.position();
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), WingsSounds.PLAYER_WHISTLE.get(), SoundCategory.PLAYERS, 0.5f, 0.4f / (player.getRandom().nextFloat() * 0.4f + 0.8f));
        player.level.getEntities(
                WingsEntities.HATCHET_BEAK.get(),
                player.getBoundingBox().inflate(256).move(0, 128, 0),
                entity -> entity.isOwnedBy(player)
        )
                .stream()
                .reduce((a, b) -> a.distanceToSqr(player) < b.distanceToSqr(player) ? a : b)
                .ifPresent(entity -> entity.callerPosition = playerPosition);
    }
}
