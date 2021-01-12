package net.msrandom.wings.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.msrandom.wings.WingsAndClaws;

public interface INetworkPacket {
    static SimpleChannel makeChannel(String name, String version) {
        return NetworkRegistry.newSimpleChannel(new ResourceLocation(WingsAndClaws.MOD_ID, name), () -> version, version::equals, version::equals);
    }

    default void read(PacketBuffer buffer) {}
    default void write(PacketBuffer buffer) {}
    void handle(PlayerEntity player);
}
