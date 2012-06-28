package forge.bukkit;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import cpw.mods.fml.server.FMLBukkitProfiler;

public class ModTimingCommand extends Command {

	protected ModTimingCommand(String name) {
		super(name);
        this.description = "Controls the forge mod timing tracker";
        this.usageMessage = "/modtiming <start [seconds] | stop | reset | show [ count ]>";
        this.setPermission("bukkit.command.timings");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender))
		{
			return true;
		}
		if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
		}
		if ("start".equals(args[0]))
		{
			int seconds = 300;
			if (args.length > 1) {
				try
				{
					seconds = Integer.parseInt(args[1]);
				}
				catch (Exception e)
				{
					// noop
				}
			}
			long secondsToRun =  FMLBukkitProfiler.beginProfiling(seconds);
            sender.sendMessage(ChatColor.YELLOW + String.format("Timing run in progress. Timings will be gathered for another %d seconds.", secondsToRun));
			return true;
		}
		if ("stop".equals(args[0]))
		{
			long seconds = FMLBukkitProfiler.endProfiling();
            sender.sendMessage(ChatColor.YELLOW + String.format("Timing run stopped after %d seconds.", seconds));
            return true;
		}
		if ("reset".equals(args[0]))
		{
			FMLBukkitProfiler.resetProfiling();
            sender.sendMessage(ChatColor.YELLOW + String.format("Timing data has been reset"));
			return true;
		}
		if ("show".equals(args[0]))
		{
			int count = -1;
			if (args.length > 1) {
				try
				{
					count = Integer.parseInt(args[1]);
				}
				catch (Exception e)
				{
					// noop
				}
			}
			String[] dump = FMLBukkitProfiler.dumpProfileData(count);
            sender.sendMessage(ChatColor.YELLOW + String.format("Timing data for %s timings", count == -1 ? "all" : String.valueOf(count)));
            for (String str : dump)
            {
                sender.sendMessage(ChatColor.YELLOW + str);
            }
			return true;
		}
        sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
        return false;
	}

}
