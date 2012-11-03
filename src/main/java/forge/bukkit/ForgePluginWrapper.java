package forge.bukkit;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;

import com.avaje.ebean.EbeanServer;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.server.FMLBukkitHandler;

public class ForgePluginWrapper implements Plugin {
	private ModContainer wrappedMod;
	private PluginLoader loader;
	public ForgePluginWrapper(ModContainer mod, PluginLoader pluginLoader) {
		this.wrappedMod=mod;
		this.loader=pluginLoader;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getDataFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PluginDescriptionFile getDescription() {
		return new PluginDescriptionFile(wrappedMod.getName(), "ForgeMod", "DummyMainClass");
	}

	@Override
	public FileConfiguration getConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getResource(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveConfig() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDefaultConfig() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveResource(String resourcePath, boolean replace) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadConfig() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PluginLoader getPluginLoader() {
		return loader;
	}

	@Override
	public Server getServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNaggable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setNaggable(boolean canNag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EbeanServer getDatabase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Logger getLogger() {
		return FMLBukkitHandler.instance().getMinecraftLogger();
	}

	@Override
	public String getName() {
		return wrappedMod.getName();
	}

}
