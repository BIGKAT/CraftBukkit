package cpw.mods.fml.common.modloader;

import net.minecraft.server.Connection;
import net.minecraft.server.PlayerConnection;
import net.minecraft.server.Packet3Chat;
import cpw.mods.fml.common.network.IChatListener;

public class ModLoaderChatListener implements IChatListener
{

    private BaseModProxy mod;

    public ModLoaderChatListener(BaseModProxy mod)
    {
        this.mod = mod;
    }

    @Override
    public Packet3Chat serverChat(Connection handler, Packet3Chat message)
    {
        mod.serverChat((PlayerConnection)handler, message.message);
        return message;
    }

    @Override
    public Packet3Chat clientChat(Connection connection, Packet3Chat message)
    {
        mod.clientChat(message.message);
        return message;
    }

}
