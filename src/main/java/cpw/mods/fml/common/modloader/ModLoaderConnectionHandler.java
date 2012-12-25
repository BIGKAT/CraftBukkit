package cpw.mods.fml.common.modloader;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.Connection;
import net.minecraft.server.PendingConnection;
import net.minecraft.server.INetworkManager;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;

public class ModLoaderConnectionHandler implements IConnectionHandler
{
    private BaseModProxy mod;

    public ModLoaderConnectionHandler(BaseModProxy mod)
    {
        this.mod = mod;
    }

    @Override
    public void playerLoggedIn(Player player, Connection netHandler, INetworkManager manager)
    {
        mod.onClientLogin((EntityHuman)player);
    }

    @Override
    public String connectionReceived(PendingConnection netHandler, INetworkManager manager)
    {
        return null;
    }

    @Override
    public void connectionOpened(Connection netClientHandler, String server, int port, INetworkManager manager)
    {
        ModLoaderHelper.sidedHelper.clientConnectionOpened(netClientHandler, manager, mod);
    }

    @Override
    public void connectionClosed(INetworkManager manager)
    {
        if (ModLoaderHelper.sidedHelper==null || !ModLoaderHelper.sidedHelper.clientConnectionClosed(manager, mod))
        {
            mod.serverDisconnect();
            mod.onClientLogout(manager);
        }
    }

    @Override
    public void clientLoggedIn(Connection nh, INetworkManager manager, Packet1Login login)
    {
        mod.serverConnect(nh);
    }

    @Override
    public void connectionOpened(Connection netClientHandler, MinecraftServer server, INetworkManager manager)
    {
        ModLoaderHelper.sidedHelper.clientConnectionOpened(netClientHandler, manager, mod);
    }

}
