package cpw.mods.fml.common.network;

import net.minecraft.server.Connection;
import net.minecraft.server.Packet131ItemData;

public interface ITinyPacketHandler
{
    void handle(Connection connection, Packet131ItemData mapData);
}
