package cpw.mods.fml.common.network;

import net.minecraft.server.Connection;
import net.minecraft.server.Packet3Chat;

public interface IChatListener
{
    /**
     * Called when there is a chat message received on the server
     * @param handler
     * @param message
     */
    public Packet3Chat serverChat(Connection connection, Packet3Chat message);

    /**
     * Called when there is a chat message recived on the client
     *
     * @param handler
     * @param message
     */
    public Packet3Chat clientChat(Connection connection, Packet3Chat message);
}
