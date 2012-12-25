package cpw.mods.fml.common.modloader;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Connection;
import net.minecraft.server.INetworkManager;
import net.minecraft.server.Packet250CustomPayload;
import cpw.mods.fml.common.network.EntitySpawnPacket;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;

public interface IModLoaderSidedHelper
{

    void finishModLoading(ModLoaderModContainer mc);

    Object getClientGui(BaseModProxy mod, EntityHuman player, int iD, int x, int y, int z);

    Entity spawnEntity(BaseModProxy mod, EntitySpawnPacket input, EntityRegistration registration);

    void sendClientPacket(BaseModProxy mod, Packet250CustomPayload packet);

    void clientConnectionOpened(Connection connection, INetworkManager manager, BaseModProxy mod);

    boolean clientConnectionClosed(INetworkManager manager, BaseModProxy mod);

}
