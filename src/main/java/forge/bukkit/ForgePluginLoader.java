package forge.bukkit;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.UnknownDependencyException;

public class ForgePluginLoader implements PluginLoader {

	@Override
	public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException {
		// NOOP
		return null;
	}

	@Override
	public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
		// NOOP
		return null;
	}

	@Override
	public Pattern[] getPluginFileFilters() {
		// NOOP
		return null;
	}

	@Override
	public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener, Plugin plugin) {
		// NOOP
		return null;
	}

	@Override
	public void enablePlugin(Plugin plugin) {
		// NOOP
	}

	@Override
	public void disablePlugin(Plugin plugin) {
		// NOOP
	}

}
