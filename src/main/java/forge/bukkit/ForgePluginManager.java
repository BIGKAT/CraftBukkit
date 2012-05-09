package forge.bukkit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Server;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.PluginsCommand;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.UnknownDependencyException;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ForgePluginManager extends SimplePluginManager {
	private ForgePluginLoader forgePluginLoader;
	private Map<File,ForgePluginWrapper> wraps;
	public ForgePluginManager(Server instance, SimpleCommandMap commandMap) {
		super(instance, commandMap);
		forgePluginLoader=new ForgePluginLoader();
	}
	
	@Override
	protected Map<String,File> prePopulatePluginList() {
		Map<String,File> parent=super.prePopulatePluginList();
		wraps=new HashMap<File,ForgePluginWrapper>();
		for (ModContainer mod : Loader.getModList()) {
			ForgePluginWrapper wrapper=new ForgePluginWrapper(mod, forgePluginLoader);
			File dummy=new File(mod.getName());
			parent.put(wrapper.getName(), dummy);
			wraps.put(dummy, wrapper);
		}
		return parent;
	}
	
	@Override
	public synchronized Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
		if (wraps.containsKey(file)) {
			ForgePluginWrapper wrapper = wraps.get(file);
			plugins.add(wrapper);
			lookupNames.put(wrapper.getName(),wrapper);
			return wrapper;
		} else {
			return super.loadPlugin(file);
		}
	}
}
