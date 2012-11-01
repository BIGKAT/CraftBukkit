package org.bukkit.craftbukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebean.config.dbplatform.SQLitePlatform;
import com.avaje.ebeaninternal.server.lib.sql.TransactionIsolation;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;

import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AnvilSaveHandler;
import net.minecraft.src.BanEntry;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.CraftingManager;
import net.minecraft.src.DedicatedServer;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EntityTracker;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.MapData;
import net.minecraft.src.MapStorage;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.Potion;
import net.minecraft.src.PropertyManager;
import net.minecraft.src.SaveHandler;
import net.minecraft.src.ServerCommand;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.World;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;
import net.minecraftforge.common.DimensionManager;

import org.apache.commons.lang.Validate;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.MarkedYAMLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Warning.WarningState;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.conversations.Conversable;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.generator.NormalChunkGenerator;
import org.bukkit.craftbukkit.help.SimpleHelpMap;
import org.bukkit.craftbukkit.inventory.CraftFurnaceRecipe;
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.craftbukkit.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.inventory.RecipeIterator;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.metadata.EntityMetadataStore;
import org.bukkit.craftbukkit.metadata.PlayerMetadataStore;
import org.bukkit.craftbukkit.metadata.WorldMetadataStore;
import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.bukkit.craftbukkit.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.updater.AutoUpdater;
import org.bukkit.craftbukkit.updater.BukkitDLUpdaterService;
import org.bukkit.craftbukkit.util.DatFileFilter;
import org.bukkit.craftbukkit.util.Versioning;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.SimpleServicesManager;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitWorker;
import org.bukkit.util.permissions.DefaultPermissions;

public final class CraftServer implements Server {
	private final String serverName = "CraftBukkit";
    private final String serverVersion;
    private final String bukkitVersion = Versioning.getBukkitVersion();
    private final ServicesManager servicesManager = new SimpleServicesManager();
    private final CraftScheduler scheduler = new CraftScheduler();
    private final SimpleCommandMap commandMap;
    private final SimpleHelpMap helpMap = new SimpleHelpMap(this);
    private final StandardMessenger messenger = new StandardMessenger();
    private final PluginManager pluginManager;
    protected final MinecraftServer console;
    protected final ServerConfigurationManager server;
    private final Map<String, org.bukkit.World> worlds = new LinkedHashMap<String, org.bukkit.World>();
    private YamlConfiguration configuration;
    private final Yaml yaml = new Yaml(new SafeConstructor());
    private final Map<String, OfflinePlayer> offlinePlayers = new MapMaker().softValues().makeMap();
    private AutoUpdater updater;
    private final EntityMetadataStore entityMetadata = new EntityMetadataStore();
    private final PlayerMetadataStore playerMetadata = new PlayerMetadataStore();
    private final WorldMetadataStore worldMetadata = new WorldMetadataStore();
    private int monsterSpawn = -1;
    private int animalSpawn = -1;
    private int waterAnimalSpawn = -1;
    private File container;
    private WarningState warningState = WarningState.DEFAULT;
	private Map<Entity, CraftEntity> forgeEntities = new MapMaker().weakKeys().weakValues().makeMap();
	private Map<World, CraftWorld> forgeWorlds = new MapMaker().weakKeys().weakValues().makeMap();
	private Map<Chunk, org.bukkit.Chunk> forgeChunks = new MapMaker().weakKeys().weakValues().makeMap();
	private OptionSet options;
	private Map<EntityPlayerMP,CraftPlayer> forgePlayers;

    static {
        ConfigurationSerialization.registerClass(CraftOfflinePlayer.class);
    }

	public static CraftServer getInstance()  {
		Server server = Bukkit.getServer();
		return server instanceof CraftServer ? (CraftServer) server : null;
	}

    public CraftServer(MinecraftServer console, ServerConfigurationManager server, SimplePluginManager pluginManager, SimpleCommandMap commandMap, YamlConfiguration configuration) {
        this.console = console;
        this.server = server;
        this.serverVersion = CraftServer.class.getPackage().getImplementationVersion();
        this.pluginManager = pluginManager;
        this.commandMap = commandMap;

//        Bukkit.setServer(this);

        // Register all the Enchantments and PotionTypes now so we can stop new registration immediately after
        Enchantment.protection.getClass();
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();

        org.bukkit.potion.Potion.setPotionBrewer(new CraftPotionBrewer());
        Potion.blindness.getClass();
        PotionEffectType.stopAcceptingRegistrations();
        // Ugly hack :(

        if (!Main.useConsole) {
            getLogger().info("Console input is disabled due to --noconsole command argument");
        }

        ((SimplePluginManager) pluginManager).useTimings(configuration.getBoolean("settings.plugin-profiling"));
        monsterSpawn = configuration.getInt("spawn-limits.monsters");
        animalSpawn = configuration.getInt("spawn-limits.animals");
        waterAnimalSpawn = configuration.getInt("spawn-limits.water-animals");
        warningState = WarningState.value(configuration.getString("settings.deprecated-verbose"));

/*        updater = new AutoUpdater(new BukkitDLUpdaterService(configuration.getString("auto-updater.host")), getLogger(), configuration.getString("auto-updater.preferred-channel"));
        updater.setEnabled(configuration.getBoolean("auto-updater.enabled"));
        updater.setSuggestChannels(configuration.getBoolean("auto-updater.suggest-channels"));
        updater.getOnBroken().addAll(configuration.getStringList("auto-updater.on-broken"));
        updater.getOnUpdate().addAll(configuration.getStringList("auto-updater.on-update"));
        updater.check(serverVersion);
*/
//        loadPlugins();
        enablePlugins(PluginLoadOrder.STARTUP);

        ChunkCompressionThread.startThread();
    }

    private File getConfigFile() {
        return (File) options.valueOf("bukkit-settings");
    }

    private void saveConfig() {
        try {
            configuration.save(getConfigFile());
        } catch (IOException ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, "Could not save " + getConfigFile(), ex);
        }
    }

    public void loadPlugins() {
        pluginManager.registerInterface(JavaPluginLoader.class);

        File pluginFolder = (File) options.valueOf("plugins");

        if (pluginFolder.exists()) {
            Plugin[] plugins = pluginManager.loadPlugins(pluginFolder);
            for (Plugin plugin : plugins) {
                try {
                    String message = String.format("Loading %s", plugin.getDescription().getFullName());
                    plugin.getLogger().info(message);
                    plugin.onLoad();
                } catch (Throwable ex) {
                    Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " initializing " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
                }
            }
        } else {
            pluginFolder.mkdir();
        }
    }

    public void enablePlugins(PluginLoadOrder type) {
        if (type == PluginLoadOrder.STARTUP) {
            helpMap.clear();
            helpMap.initializeGeneralTopics();
        }

        Plugin[] plugins = pluginManager.getPlugins();

        for (Plugin plugin : plugins) {
            if ((!plugin.isEnabled()) && (plugin.getDescription().getLoad() == type)) {
                loadPlugin(plugin);
            }
        }

        if (type == PluginLoadOrder.POSTWORLD) {
            commandMap.registerServerAliases();
            loadCustomPermissions();
            DefaultPermissions.registerCorePermissions();
            helpMap.initializeCommands();
        }
    }

    public void disablePlugins() {
        pluginManager.disablePlugins();
    }

    private void loadPlugin(Plugin plugin) {
        try {
            pluginManager.enablePlugin(plugin);

            List<Permission> perms = plugin.getDescription().getPermissions();

            for (Permission perm : perms) {
                try {
                    pluginManager.addPermission(perm);
                } catch (IllegalArgumentException ex) {
                    getLogger().log(Level.WARNING, "Plugin " + plugin.getDescription().getFullName() + " tried to register permission '" + perm.getName() + "' but it's already registered", ex);
                }
            }
        } catch (Throwable ex) {
            Logger.getLogger(CraftServer.class.getName()).log(Level.SEVERE, ex.getMessage() + " loading " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }
    }

    public String getName() {
        return serverName;
    }

    public String getVersion() {
        return serverVersion + " (MC: " + console.getMinecraftVersion() + ")";
    }

    public String getBukkitVersion() {
        return bukkitVersion;
    }

    @SuppressWarnings("unchecked")
    public Player[] getOnlinePlayers() {
        List<EntityPlayerMP> online = server.playerEntityList;
        Player[] players = new Player[online.size()];

        for (int i = 0; i < players.length; i++) {
            players[i] = getPlayer(online.get(i));
        }

        return players;
    }

    public Player getPlayer(final String name) {
        Player[] players = getOnlinePlayers();

        Player found = null;
        String lowerName = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : players) {
            if (player.getName().toLowerCase().startsWith(lowerName)) {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                }
				if (curDelta == 0) {
					break;
				}
            }
        }
        return found;
    }

    public Player getPlayerExact(String name) {
        String lname = name.toLowerCase();

        for (Player player : getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(lname)) {
                return player;
            }
        }

        return null;
    }

    public int broadcastMessage(String message) {
        return broadcast(message, BROADCAST_CHANNEL_USERS);
    }

    public Player getPlayer(final EntityPlayerMP entity) {
        return forgePlayers.get(entity);
    }

    public List<Player> matchPlayer(String partialName) {
        List<Player> matchedPlayers = new ArrayList<Player>();

        for (Player iterPlayer : this.getOnlinePlayers()) {
            String iterPlayerName = iterPlayer.getName();

            if (partialName.equalsIgnoreCase(iterPlayerName)) {
                // Exact match
                matchedPlayers.clear();
                matchedPlayers.add(iterPlayer);
                break;
            }
            if (iterPlayerName.toLowerCase().contains(partialName.toLowerCase())) {
                // Partial match
                matchedPlayers.add(iterPlayer);
            }
        }

        return matchedPlayers;
    }

    public int getMaxPlayers() {
        return server.getMaxPlayers();
    }

    // NOTE: These are dependent on the corrisponding call in MinecraftServer
    // so if that changes this will need to as well
    public int getPort() {
        return this.getConfigInt("server-port", 25565);
    }

    public int getViewDistance() {
        return this.getConfigInt("view-distance", 10);
    }

    public String getIp() {
        return this.getConfigString("server-ip", "");
    }

    public String getServerName() {
        return this.getConfigString("server-name", "Unknown Server");
    }

    public String getServerId() {
        return this.getConfigString("server-id", "unnamed");
    }

    public String getWorldType() {
        return this.getConfigString("level-type", "DEFAULT");
    }

    public boolean getGenerateStructures() {
        return this.getConfigBoolean("generate-structures", true);
    }

    public boolean getAllowEnd() {
        return this.configuration.getBoolean("settings.allow-end");
    }

    public boolean getAllowNether() {
        return this.getConfigBoolean("allow-nether", true);
    }

    public boolean getWarnOnOverload() {
        return this.configuration.getBoolean("settings.warn-on-overload");
    }

    public boolean getQueryPlugins() {
        return this.configuration.getBoolean("settings.query-plugins");
    }

    public boolean hasWhitelist() {
        return this.getConfigBoolean("white-list", false);
    }

    // NOTE: Temporary calls through to server.properies until its replaced
    private String getConfigString(String variable, String defaultValue) {
        return this.console.getPropertyManager().getString(variable, defaultValue);
    }

    private int getConfigInt(String variable, int defaultValue) {
        return this.console.getPropertyManager().getInt(variable, defaultValue);
    }

    private boolean getConfigBoolean(String variable, boolean defaultValue) {
        return this.console.getPropertyManager().getBoolean(variable, defaultValue);
    }

    // End Temporary calls

    public String getUpdateFolder() {
        return this.configuration.getString("settings.update-folder", "update");
    }

    public File getUpdateFolderFile() {
        return new File((File) options.valueOf("plugins"), this.configuration.getString("settings.update-folder", "update"));
    }

    public int getPingPacketLimit() {
        return this.configuration.getInt("settings.ping-packet-limit", 100);
    }

    public long getConnectionThrottle() {
        return this.configuration.getInt("settings.connection-throttle");
    }

    public int getTicksPerAnimalSpawns() {
        return this.configuration.getInt("ticks-per.animal-spawns");
    }

    public int getTicksPerMonsterSpawns() {
        return this.configuration.getInt("ticks-per.monster-spawns");
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public CraftScheduler getScheduler() {
        return scheduler;
    }

    public ServicesManager getServicesManager() {
        return servicesManager;
    }

    public List<org.bukkit.World> getWorlds() {
        return new ArrayList<org.bukkit.World>(worlds.values());
    }

    public ServerConfigurationManager getHandle() {
        return server;
    }

    // NOTE: Should only be called from DedicatedServer.ah()
    public boolean dispatchServerCommand(CommandSender sender, ServerCommand serverCommand) {
        if (sender instanceof Conversable) {
            Conversable conversable = (Conversable)sender;

            if (conversable.isConversing()) {
                conversable.acceptConversationInput(serverCommand.command);
                return true;
            }
        }
        return dispatchCommand(sender, serverCommand.command);
    }

    public boolean dispatchCommand(CommandSender sender, String commandLine) {
        if (commandMap.dispatch(sender, commandLine)) {
            return true;
        }

        sender.sendMessage("Unknown command. Type \"help\" for help.");

        return false;
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(getConfigFile());
        PropertyManager config = new PropertyManager((File) options.valueOf("config"));

        ((DedicatedServer) console).settings = config;

        boolean animals = config.getBooleanProperty("spawn-animals", console.getCanSpawnAnimals());
        boolean monsters = config.getBooleanProperty("spawn-monsters", console.worldServers[0].difficultySetting > 0);
        int difficulty = config.getIntProperty("difficulty", console.worldServers[0].difficultySetting);

        console.setOnlineMode(config.getBooleanProperty("online-mode", console.isServerInOnlineMode()));
        console.setCanSpawnAnimals(config.getBooleanProperty("spawn-animals", console.getCanSpawnAnimals()));
        console.setAllowPvp(config.getBooleanProperty("pvp", console.isPVPEnabled()));
        console.setAllowFlight(config.getBooleanProperty("allow-flight", console.isFlightAllowed()));
        console.setMOTD(config.getProperty("motd", console.getMOTD()));
        monsterSpawn = configuration.getInt("spawn-limits.monsters");
        animalSpawn = configuration.getInt("spawn-limits.animals");
        waterAnimalSpawn = configuration.getInt("spawn-limits.water-animals");
        warningState = WarningState.value(configuration.getString("settings.deprecated-verbose"));

        for (WorldServer world : console.worldServers) {
            world.difficultySetting = difficulty;
            world.setAllowedSpawnTypes(monsters, animals);
            if (this.getTicksPerAnimalSpawns() < 0) {
                world.ticksPerAnimalSpawns = 400;
            } else {
                world.ticksPerAnimalSpawns = this.getTicksPerAnimalSpawns();
            }

            if (this.getTicksPerMonsterSpawns() < 0) {
                world.ticksPerMonsterSpawns = 1;
            } else {
                world.ticksPerMonsterSpawns = this.getTicksPerMonsterSpawns();
            }
        }

        pluginManager.clearPlugins();
        commandMap.clearCommands();
        resetRecipes();

        int pollCount = 0;

        // Wait for at most 2.5 seconds for plugins to close their threads
        while (pollCount < 50 && getScheduler().getActiveWorkers().size() > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}
            pollCount++;
        }

        List<BukkitWorker> overdueWorkers = getScheduler().getActiveWorkers();
        for (BukkitWorker worker : overdueWorkers) {
            Plugin plugin = worker.getOwner();
            String author = "<NoAuthorGiven>";
            if (plugin.getDescription().getAuthors().size() > 0) {
                author = plugin.getDescription().getAuthors().get(0);
            }
            getLogger().log(Level.SEVERE, String.format(
                "Nag author: '%s' of '%s' about the following: %s",
                author,
                plugin.getDescription().getName(),
                "This plugin is not properly shutting down its async tasks when it is being reloaded.  This may cause conflicts with the newly loaded version of the plugin"
            ));
        }
        loadPlugins();
        enablePlugins(PluginLoadOrder.STARTUP);
        enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    @SuppressWarnings({ "unchecked", "finally" })
    private void loadCustomPermissions() {
        File file = new File(configuration.getString("settings.permissions-file"));
        FileInputStream stream;

        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            try {
                file.createNewFile();
            } finally {
                return;
            }
        }

        Map<String, Map<String, Object>> perms;

        try {
            perms = (Map<String, Map<String, Object>>) yaml.load(stream);
        } catch (MarkedYAMLException ex) {
            getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML: " + ex.toString());
            return;
        } catch (Throwable ex) {
            getLogger().log(Level.WARNING, "Server permissions file " + file + " is not valid YAML.", ex);
            return;
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {}
        }

        if (perms == null) {
            getLogger().log(Level.INFO, "Server permissions file " + file + " is empty, ignoring it");
            return;
        }

        List<Permission> permsList = Permission.loadPermissions(perms, "Permission node '%s' in " + file + " is invalid", Permission.DEFAULT_PERMISSION);

        for (Permission perm : permsList) {
            try {
                pluginManager.addPermission(perm);
            } catch (IllegalArgumentException ex) {
                getLogger().log(Level.SEVERE, "Permission in " + file + " was already defined", ex);
            }
        }
    }

    @Override
    public String toString() {
        return "CraftServer{" + "serverName=" + serverName + ",serverVersion=" + serverVersion + ",minecraftVersion=" + console.getMinecraftVersion() + '}';
    }

    public org.bukkit.World createWorld(String name, org.bukkit.World.Environment environment) {
        return WorldCreator.name(name).environment(environment).createWorld();
    }

    public org.bukkit.World createWorld(String name, org.bukkit.World.Environment environment, long seed) {
        return WorldCreator.name(name).environment(environment).seed(seed).createWorld();
    }

    public org.bukkit.World createWorld(String name, Environment environment, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).generator(generator).createWorld();
    }

    public org.bukkit.World createWorld(String name, Environment environment, long seed, ChunkGenerator generator) {
        return WorldCreator.name(name).environment(environment).seed(seed).generator(generator).createWorld();
    }

    public org.bukkit.World createWorld(WorldCreator creator) {
        if (creator == null) {
            throw new IllegalArgumentException("Creator may not be null");
        }

        String name = creator.name();
        ChunkGenerator generator = creator.generator();
        File folder = new File(getWorldContainer(), name);
		org.bukkit.World world = getWorld(name);
        WorldType type = WorldType.parseWorldType(creator.type().getName());
        boolean generateStructures = creator.generateStructures();

        if (world != null) {
            return world;
        }

        if ((folder.exists()) && (!folder.isDirectory())) {
            throw new IllegalArgumentException("File exists with the name '" + name + "' and isn't a folder");
        }

        if (generator == null) {
            generator = getGenerator(name);
        }
/*
        Convertable converter = new WorldLoaderServer(getWorldContainer());
        if (converter.isConvertable(name)) {
            getLogger().info("Converting world '" + name + "'");
            converter.convert(name, new ConvertProgressUpdater(console));
        }
*/
        int dimension = DimensionManager.getNextFreeDimId();
        boolean hardcore = false;
        DimensionManager.registerProviderType(dimension, BukkitWorldProvider.class, false);
        WorldServer internal = new WorldServerMulti(console, new AnvilSaveHandler(getWorldContainer(), name, true), name, dimension, new WorldSettings(creator.seed(), EnumGameType.getByID(getDefaultGameMode().getValue()), generateStructures, hardcore, type), DimensionManager.getWorld(0), console.theProfiler);
		world = getBukkitWorld(internal);

        if (!(worlds.containsKey(name.toLowerCase()))) {
            return null;
        }

        internal.mapStorage = console.worldServers[0].mapStorage;

        internal.theEntityTracker = new EntityTracker(internal); // CraftBukkit
        internal.addWorldAccess(new WorldManager(console, internal));
        internal.difficultySetting = 1;
        internal.setAllowedSpawnTypes(true, true);

        if (generator != null) {
            world.getPopulators().addAll(generator.getDefaultPopulators(world));
        }

        pluginManager.callEvent(new WorldInitEvent(world));
        System.out.print("Preparing start region for level " + dimension + " (Seed: " + internal.getSeed() + ")");

        if (world.getKeepSpawnInMemory()) {
            short short1 = 196;
            long i = System.currentTimeMillis();
            for (int j = -short1; j <= short1; j += 16) {
                for (int k = -short1; k <= short1; k += 16) {
                    long l = System.currentTimeMillis();

                    if (l < i) {
                        i = l;
                    }

                    if (l > i + 1000L) {
                        int i1 = (short1 * 2 + 1) * (short1 * 2 + 1);
                        int j1 = (j + short1) * (short1 * 2 + 1) + k + 1;

                        System.out.println("Preparing spawn area for " + name + ", " + (j1 * 100 / i1) + "%");
                        i = l;
                    }

                    ChunkCoordinates chunkcoordinates = internal.getSpawnPoint();
                    internal.theChunkProviderServer.loadChunk(chunkcoordinates.posX + j >> 4, chunkcoordinates.posZ + k >> 4);

                    while (internal.updatingLighting()) {
                        ;
                    }
                }
            }
        }
        pluginManager.callEvent(new WorldLoadEvent(world));
        return world;
    }

    public boolean unloadWorld(String name, boolean save) {
        return unloadWorld(getWorld(name), save);
    }

    public boolean unloadWorld(org.bukkit.World world, boolean save) {
        if (world == null) {
            return false;
        }

        WorldServer handle = ((CraftWorld) world).getHandle();

        if (!Arrays.asList(DimensionManager.getWorlds()).contains(handle)) {
            return false;
        }

        if (!(handle.provider.dimensionId > 1)) {
            return false;
        }

        if (handle.playerEntities.size() > 0) {
            return false;
        }

        WorldUnloadEvent e = new WorldUnloadEvent(world);
        pluginManager.callEvent(e);

        if (e.isCancelled()) {
            return false;
        }

        if (save) {
            try {
                handle.saveAllChunks(true, null);
                handle.flush();
                WorldSaveEvent event = new WorldSaveEvent(world);
                getPluginManager().callEvent(event);
            } catch (MinecraftException ex) {
			}
		}

        worlds.remove(world.getName().toLowerCase());
        DimensionManager.unloadWorld(handle.provider.dimensionId);
        return true;
    }

    public MinecraftServer getServer() {
        return console;
    }

    public org.bukkit.World getWorld(String name) {
        return worlds.get(name.toLowerCase());
    }

    public org.bukkit.World getWorld(UUID uid) {
        for (org.bukkit.World world : worlds.values()) {
            if (world.getUID().equals(uid)) {
                return world;
            }
        }
        return null;
    }

    public void addWorld(org.bukkit.World world) {
        // Check if a World already exists with the UID.
        if (getWorld(world.getUID()) != null) {
            System.out.println("World " + world.getName() + " is a duplicate of another world and has been prevented from loading. Please delete the uid.dat file from " + world.getName() + "'s world directory if you want to be able to load the duplicate world.");
            return;
        }
        worlds.put(world.getName().toLowerCase(), world);
    }

    public Logger getLogger() {
        return MinecraftServer.logger;
    }

    public ConsoleReader getReader() {
        return console.reader;
    }

    public PluginCommand getPluginCommand(String name) {
        Command command = commandMap.getCommand(name);

        if (command instanceof PluginCommand) {
            return (PluginCommand) command;
        } else {
            return null;
        }
    }

    public void savePlayers() {
        server.saveAllPlayerData();
    }

    public void configureDbConfig(ServerConfig config) {
        DataSourceConfig ds = new DataSourceConfig();
        ds.setDriver(configuration.getString("database.driver"));
        ds.setUrl(configuration.getString("database.url"));
        ds.setUsername(configuration.getString("database.username"));
        ds.setPassword(configuration.getString("database.password"));
        ds.setIsolationLevel(TransactionIsolation.getLevel(configuration.getString("database.isolation")));

        if (ds.getDriver().contains("sqlite")) {
            config.setDatabasePlatform(new SQLitePlatform());
            config.getDatabasePlatform().getDbDdlSyntax().setIdentity("");
        }

        config.setDataSourceConfig(ds);
    }

    public boolean addRecipe(Recipe recipe) {
        CraftRecipe toAdd;
        if (recipe instanceof CraftRecipe) {
            toAdd = (CraftRecipe) recipe;
        } else {
            if (recipe instanceof ShapedRecipe) {
                toAdd = CraftShapedRecipe.fromBukkitRecipe((ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                toAdd = CraftShapelessRecipe.fromBukkitRecipe((ShapelessRecipe) recipe);
            } else if (recipe instanceof FurnaceRecipe) {
                toAdd = CraftFurnaceRecipe.fromBukkitRecipe((FurnaceRecipe) recipe);
            } else {
                return false;
            }
        }
        toAdd.addToCraftingManager();
//        CraftingManager.getInstance().sort();
        return true;
    }

    public List<Recipe> getRecipesFor(ItemStack result) {
        List<Recipe> results = new ArrayList<Recipe>();
        Iterator<Recipe> iter = recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            ItemStack stack = recipe.getResult();
            if (stack.getType() != result.getType()) {
                continue;
            }
            if (result.getDurability() == -1 || result.getDurability() == stack.getDurability()) {
                results.add(recipe);
            }
        }
        return results;
    }

    public Iterator<Recipe> recipeIterator() {
        return new RecipeIterator();
    }

    public void clearRecipes() {
        CraftingManager.getInstance().getRecipeList().clear();
        FurnaceRecipes.smelting().getSmeltingList().clear();
    }

    public void resetRecipes() {
//        CraftingManager.getInstance().recipes = new CraftingManager().recipes;
//        FurnaceRecipes.getInstance().recipes = new FurnaceRecipes().recipes;
    }

    public Map<String, String[]> getCommandAliases() {
        ConfigurationSection section = configuration.getConfigurationSection("aliases");
        Map<String, String[]> result = new LinkedHashMap<String, String[]>();

        if (section != null) {
            for (String key : section.getKeys(false)) {
                List<String> commands;

                if (section.isList(key)) {
                    commands = section.getStringList(key);
                } else {
                    commands = ImmutableList.<String>of(section.getString(key));
                }

                result.put(key, commands.toArray(new String[commands.size()]));
            }
        }

        return result;
    }

    public int getSpawnRadius() {
        return configuration.getInt("settings.spawn-radius", 16);
    }

    public void setSpawnRadius(int value) {
        configuration.set("settings.spawn-radius", value);
        saveConfig();
    }

    public boolean getOnlineMode() {
        return console.isServerInOnlineMode();
    }

    public boolean getAllowFlight() {
        return console.isFlightAllowed();
    }

    public boolean useExactLoginLocation() {
        return configuration.getBoolean("settings.use-exact-login-location");
    }

    public ChunkGenerator getGenerator(String world) {
        ConfigurationSection section = configuration.getConfigurationSection("worlds");
        ChunkGenerator result = null;

        if (section != null) {
            section = section.getConfigurationSection(world);

            if (section != null) {
                String name = section.getString("generator");

                if ((name != null) && (!name.equals(""))) {
                    String[] split = name.split(":", 2);
                    String id = (split.length > 1) ? split[1] : null;
                    Plugin plugin = pluginManager.getPlugin(split[0]);

                    if (plugin == null) {
                        getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' does not exist");
                    } else if (!plugin.isEnabled()) {
                        getLogger().severe("Could not set generator for default world '" + world + "': Plugin '" + split[0] + "' is not enabled yet (is it load:STARTUP?)");
                    } else {
                        result = plugin.getDefaultWorldGenerator(world, id);
                    }
                }
            }
        }

        return result;
    }

    public CraftMapView getMap(short id) {
        MapStorage collection = console.worldServers[0].mapStorage;
        MapData worldmap = (MapData) collection.loadData(MapData.class, "map_" + id);
        if (worldmap == null) {
            return null;
        }
        return worldmap.mapView;
    }

    public CraftMapView createMap(org.bukkit.World world) {
        net.minecraft.src.ItemStack stack = new net.minecraft.src.ItemStack(Item.map, 1, -1);
        MapData worldmap = Item.map.getMapData(stack, ((CraftWorld) world).getHandle());
        return worldmap.mapView;
    }

    public void shutdown() {
//        console.setServerStopping();
    }

    public int broadcast(String message, String permission) {
        int count = 0;
        Set<Permissible> permissibles = getPluginManager().getPermissionSubscriptions(permission);

        for (Permissible permissible : permissibles) {
            if (permissible instanceof CommandSender && permissible.hasPermission(permission)) {
                CommandSender user = (CommandSender) permissible;
                user.sendMessage(message);
                count++;
            }
        }

        return count;
    }

    public OfflinePlayer getOfflinePlayer(String name) {
        OfflinePlayer result = getPlayerExact(name);
        String lname = name.toLowerCase();

        if (result == null) {
            result = offlinePlayers.get(lname);

            if (result == null) {
                result = new CraftOfflinePlayer(this, name);
                offlinePlayers.put(lname, result);
            }
        } else {
            offlinePlayers.remove(lname);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public Set<String> getIPBans() {
        return server.getBannedIPs().getBannedList().keySet();
    }

    public void banIP(String address) {
        BanEntry entry = new BanEntry(address);
        server.getBannedIPs().put(entry);
        server.getBannedIPs().saveToFileWithHeader();
    }

    public void unbanIP(String address) {
        server.getBannedIPs().remove(address);
        server.getBannedIPs().saveToFileWithHeader();
    }

    public Set<OfflinePlayer> getBannedPlayers() {
        Set<OfflinePlayer> result = new HashSet<OfflinePlayer>();

        for (Object name : server.getBannedPlayers().getBannedList().keySet()) {
            result.add(getOfflinePlayer((String) name));
        }

        return result;
    }

    public void setWhitelist(boolean value) {
        server.setWhiteListEnabled(value);
        console.getPropertyManager().a("white-list", value);
    }

    public Set<OfflinePlayer> getWhitelistedPlayers() {
        Set<OfflinePlayer> result = new LinkedHashSet<OfflinePlayer>();

        for (Object name : server.getWhiteListedPlayers()) {
            if (((String)name).length() == 0 || ((String)name).startsWith("#")) {
                continue;
            }
            result.add(getOfflinePlayer((String) name));
        }

        return result;
    }

    public Set<OfflinePlayer> getOperators() {
        Set<OfflinePlayer> result = new HashSet<OfflinePlayer>();

        for (Object name : server.getWhiteListedPlayers()) {
            result.add(getOfflinePlayer((String) name));
        }

        return result;
    }

    public void reloadWhitelist() {
        server.loadWhiteList();
    }

    public GameMode getDefaultGameMode() {
        return GameMode.getByValue(console.worldServers[0].getWorldInfo().getGameType().getID());
    }

    public void setDefaultGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        for (org.bukkit.World world : getWorlds()) {
            ((CraftWorld) world).getHandle().getWorldInfo().setGameType(EnumGameType.getByID(mode.getValue()));
        }
    }

    public ConsoleCommandSender getConsoleSender() {
        return console.console;
    }

    public EntityMetadataStore getEntityMetadata() {
        return entityMetadata;
    }

    public PlayerMetadataStore getPlayerMetadata() {
        return playerMetadata;
    }

    public WorldMetadataStore getWorldMetadata() {
        return worldMetadata;
    }

    public void detectListNameConflict(EntityPlayerMP entityPlayer) {
        // Collisions will make for invisible people
        for (int i = 0; i < getHandle().playerEntityList.size(); ++i) {
            EntityPlayerMP testEntityPlayer = (EntityPlayerMP) getHandle().playerEntityList.get(i);

            // We have a problem!
            if (testEntityPlayer != entityPlayer && testEntityPlayer.username.equals(entityPlayer.username)) {
                String oldName = entityPlayer.username;
                int spaceLeft = 16 - oldName.length();

                if (spaceLeft <= 1) { // We also hit the list name length limit!
                    entityPlayer.username = oldName.subSequence(0, oldName.length() - 2 - spaceLeft) + String.valueOf(System.currentTimeMillis() % 99);
                } else {
                    entityPlayer.username = oldName + String.valueOf(System.currentTimeMillis() % 99);
                }

                return;
            }
        }
    }

    public File getWorldContainer() {
        if (((AnvilSaveConverter)this.getServer().getActiveAnvilConverter()).savesDirectory != null) {
            return ((AnvilSaveConverter)this.getServer().getActiveAnvilConverter()).savesDirectory;
        }

        if (container == null) {
            container = new File(configuration.getString("settings.world-container", "."));
        }

        return container;
    }

    public OfflinePlayer[] getOfflinePlayers() {
        SaveHandler storage = (SaveHandler) console.worldServers[0].getSaveHandler();
        String[] files = storage.playersDirectory.list(new DatFileFilter());
        Set<OfflinePlayer> players = new HashSet<OfflinePlayer>();

        for (int i = 0; i < files.length; i++) {
            players.add(getOfflinePlayer(files[i].substring(0, files[i].length() - 4)));
        }
        players.addAll(Arrays.asList(getOnlinePlayers()));

        return players.toArray(new OfflinePlayer[players.size()]);
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(getMessenger(), source, channel, message);

        for (Player player : getOnlinePlayers()) {
            player.sendPluginMessage(source, channel, message);
        }
    }

    public Set<String> getListeningPluginChannels() {
        Set<String> result = new HashSet<String>();

        for (Player player : getOnlinePlayers()) {
            result.addAll(player.getListeningPluginChannels());
        }

        return result;
    }

    public void onPlayerJoin(Player player) {
        if ((updater.isEnabled()) && (updater.getCurrent() != null) && (player.hasPermission(Server.BROADCAST_CHANNEL_ADMINISTRATIVE))) {
            if ((updater.getCurrent().isBroken()) && (updater.getOnBroken().contains(updater.WARN_OPERATORS))) {
                player.sendMessage(ChatColor.DARK_RED + "The version of CraftBukkit that this server is running is known to be broken. Please consider updating to the latest version at dl.bukkit.org.");
            } else if ((updater.isUpdateAvailable()) && (updater.getOnUpdate().contains(updater.WARN_OPERATORS))) {
                player.sendMessage(ChatColor.DARK_PURPLE + "The version of CraftBukkit that this server is running is out of date. Please consider updating to the latest version at dl.bukkit.org.");
            }
        }
    }

    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        // TODO: Create the appropriate type, rather than Custom?
        return new CraftInventoryCustom(owner, type);
    }

    public Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
        Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
        return new CraftInventoryCustom(owner, size);
    }

    public Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException {
        Validate.isTrue(size % 9 == 0, "Chests must have a size that is a multiple of 9!");
        return new CraftInventoryCustom(owner, size, title);
    }

    public HelpMap getHelpMap() {
        return helpMap;
    }

    public SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    public int getMonsterSpawnLimit() {
        return monsterSpawn;
    }

    public int getAnimalSpawnLimit() {
        return animalSpawn;
    }

    public int getWaterAnimalSpawnLimit() {
        return waterAnimalSpawn;
    }

    public boolean isPrimaryThread() {
        return Thread.currentThread().equals(console.primaryThread);
    }

    public String getMotd() {
        return console.getMOTD();
    }

    public WarningState getWarningState() {
        return warningState;
    }

	public static CraftWorld getBukkitWorld(World world) {
		CraftServer server = getInstance();
		if (server == null) return null;
		if (!server.forgeWorlds.containsKey(world)) {
			if (world instanceof WorldServer) {
				CraftWorld bWorld = new CraftWorld((WorldServer) world, new NormalChunkGenerator(world, world.getSeed()), Environment.NORMAL);
				server.forgeWorlds.put(world, bWorld);
			}
		}
		return server.forgeWorlds.get(world);
	}

	public static CraftPlayer getBukkitPlayer(EntityPlayerMP player)
	{
		CraftServer server = getInstance();
		if (server == null) return null;
		if (!server.forgePlayers.containsKey(player))
		{
			server.forgePlayers.put(player, new CraftPlayer(server, player));
		}
		return server.forgePlayers.get(player);
	}
	public static CraftEntity getBukkitEntity(Entity entity) {
		CraftServer server = getInstance();
		if (server == null) return null;
		if (!server.forgeEntities.containsKey(entity))
		{
			server.forgeEntities.put(entity, CraftEntity.getEntity(server, entity));
		}
		return server.forgeEntities.get(entity);
	}

	public static org.bukkit.Chunk getBukkitChunk(Chunk chunk)
	{
		CraftServer server = getInstance();
		if (server == null) return null;
		if (!server.forgeChunks.containsKey(chunk))
		{
			server.forgeChunks.put(chunk, new CraftChunk(chunk));
		}
		return server.forgeChunks.get(chunk);
	}

	public static UUID getWorldUUID(WorldServer world) {
		return null;
	}

	public static SpawnReason getBukkitSpawnReason(Entity entity) {
		return null;
	}

	public static void bindBukkitChunk(Chunk nmsChunk, org.bukkit.Chunk chunk) {
		CraftServer server = getInstance();
		if (server == null) return;
		server.forgeChunks.put(nmsChunk, chunk);
	}
}
