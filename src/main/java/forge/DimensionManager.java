package forge;

import java.util.Hashtable;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

import cpw.mods.fml.common.FMLCommonHandler;

import net.minecraft.server.*;

public class DimensionManager
{
    private static Hashtable<Integer, WorldProvider> providers = new Hashtable<Integer, WorldProvider>();
    private static Hashtable<Integer, Boolean> spawnSettings = new Hashtable<Integer, Boolean>();
    private static Hashtable<Integer, World> worlds = new Hashtable<Integer, World>();
    private static boolean hasInit = false;

    public static boolean registerDimension(int id, WorldProvider provider, boolean keepLoaded)
    {
        if (providers.containsValue(id))
        {
            return false;
        }
        providers.put(id, provider);
        spawnSettings.put(id, keepLoaded);
        return true;
    }

    public static void init()
    {
        if (hasInit)
        {
            return;
        }
        registerDimension( 0, new WorldProviderNormal(), true);
        registerDimension(-1, new WorldProviderHell(),    true);
        registerDimension( 1, new WorldProviderTheEnd(),     false);
    }

    public static WorldProvider getProvider(int id)
    {
        return providers.get(id);
    }

    public static Integer[] getIDs()
    {
        return providers.keySet().toArray(new Integer[0]);
    }

    public static void setWorld(int id, World world)
    {
    	WorldProvider wp = world.worldProvider;
    	int wpid = 0;
    	for (Map.Entry<Integer,WorldProvider> wpe : providers.entrySet())
    	{
    		if (wpe.getValue()==wp)
    		{
    			wpid = wpe.getKey();
    			break;
    		}
    	}
    	FMLCommonHandler.instance().getFMLLogger().info(String.format("Registering world %s, dimension %d, of providertype %s(%d) with Minecraft Forge", world.worldData.name, id, wp, wpid));
        worlds.put(id, world);
    }

    public static World getWorld(int id)
    {
        return worlds.get(id);
    }

    public static World[] getWorlds()
    {
    	return worlds.values().toArray(new World[0]);
    }

    public static boolean shouldLoadSpawn(int id)
    {
        return spawnSettings.contains(id) && spawnSettings.get(id);
    }

    static
    {
        init();
    }
}
