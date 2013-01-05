package cpw.mods.fml.common.event;

import org.bukkit.command.Command;

import net.minecraft.server.CommandHandler;
import net.minecraft.server.ICommand;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.LoaderState.ModState;

public class FMLServerStartingEvent extends FMLStateEvent
{

    private MinecraftServer server;

    public FMLServerStartingEvent(Object... data)
    {
        super(data);
        this.server = (MinecraftServer) data[0];
    }
    @Override
    public ModState getModState()
    {
        return ModState.AVAILABLE;
    }

    public MinecraftServer getServer()
    {
        return server;
    }

    public void registerServerCommand(ICommand command)
    {
        CommandHandler ch = (CommandHandler) getServer().getCommandHandler();
        ch.a(command);
    }

    // add support to register commands through Bukkit
    // MCPC start
    public void registerServerCommand(String fallbackPrefix, Command command)
    {
        org.bukkit.command.SimpleCommandMap commandMap = getServer().server.getCommandMap();
        commandMap.register(fallbackPrefix, command);
    }
    // MCPC end
}
