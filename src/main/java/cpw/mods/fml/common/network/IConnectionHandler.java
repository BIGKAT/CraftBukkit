package cpw.mods.fml.common.network;

import net.minecraft.server.Connection;
import net.minecraft.server.PendingConnection;
import net.minecraft.server.INetworkManager;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.MinecraftServer;

public interface IConnectionHandler
{
    /**
     * Called when a player logs into the server
     *  SERVER SIDE
     *
     * @param player
     * @param netHandler
     * @param manager
     */
    void playerLoggedIn(Player player, Connection netHandler, INetworkManager manager);

    /**
     * If you don't want the connection to continue, return a non-empty string here
     * If you do, you can do other stuff here- note no FML negotiation has occured yet
     * though the client is verified as having FML installed
     *
     * SERVER SIDE
     *
     * @param netHandler
     * @param manager
     */
    String connectionReceived(PendingConnection netHandler, INetworkManager manager);

    /**
     * Fired when a remote connection is opened
     * CLIENT SIDE
     *
     * @param netClientHandler
     * @param server
     * @param port
     */
    void connectionOpened(Connection netClientHandler, String server, int port, INetworkManager manager);
    /**
     *
     * Fired when a local connection is opened
     *
     * CLIENT SIDE
     *
     * @param netClientHandler
     * @param server
     */
    void connectionOpened(Connection netClientHandler, MinecraftServer server, INetworkManager manager);

    /**
     * Fired when a connection closes
     *
     * ALL SIDES
     *
     * @param manager
     */
    void connectionClosed(INetworkManager manager);

    /**
     * Fired when the client established the connection to the server
     *
     * CLIENT SIDE
     * @param clientHandler
     * @param manager
     * @param login
     */
    void clientLoggedIn(Connection clientHandler, INetworkManager manager, Packet1Login login);

}
