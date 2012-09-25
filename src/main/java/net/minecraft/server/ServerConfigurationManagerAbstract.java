package net.minecraft.server;

import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.Bukkit;
// CraftBukkit end

public abstract class ServerConfigurationManagerAbstract {

    private static final SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");
    public static final Logger a = Logger.getLogger("Minecraft");
    private final MinecraftServer server;
    public final List players = new java.util.concurrent.CopyOnWriteArrayList(); // CraftBukkit - ArrayList -> CopyOnWriteArrayList: Iterator safety
    private final BanList banByName = new BanList(new File("banned-players.txt"));
    private final BanList banByIP = new BanList(new File("banned-ips.txt"));
    private Set operators = new HashSet();
    private Set whitelist = new java.util.LinkedHashSet(); // CraftBukkit - HashSet -> LinkedHashSet
    public PlayerFileData playerFileData; // CraftBukkit - private -> public
    public boolean hasWhitelist; // CraftBukkit - private -> public
    protected int maxPlayers;
    protected int d;
    private EnumGamemode m;
    private boolean n;
    private int o = 0;

    // CraftBukkit start
    private CraftServer cserver;

    public ServerConfigurationManagerAbstract(MinecraftServer minecraftserver) {
        minecraftserver.server = new CraftServer(minecraftserver, this);
        minecraftserver.console = org.bukkit.craftbukkit.command.ColouredConsoleSender.getInstance();
        this.cserver = minecraftserver.server;
        // CraftBukkit end

        this.server = minecraftserver;
        this.banByName.setEnabled(false);
        this.banByIP.setEnabled(false);
        this.maxPlayers = 8;
    }

    public void a(INetworkManager inetworkmanager, EntityPlayer entityplayer) {
        this.a(entityplayer);
        entityplayer.spawnIn(this.server.getWorldServer(entityplayer.dimension));
        entityplayer.itemInWorldManager.a((WorldServer) entityplayer.worldObj);
        String s = "local";

        if (inetworkmanager.getSocketAddress() != null) {
            s = inetworkmanager.getSocketAddress().toString();
        }

        // CraftBukkit - add world and location to 'logged in' message.
        a.info(entityplayer.username + "[" + s + "] logged in with entity id " + entityplayer.entityId + " at ([" + entityplayer.worldObj.worldData.getName() + "] " + entityplayer.posX + ", " + entityplayer.posY + ", " + entityplayer.posZ + ")");
        WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);
        ChunkCoordinates chunkcoordinates = worldserver.getSpawn();

        this.a(entityplayer, (EntityPlayer) null, worldserver);
        NetServerHandler netserverhandler = new NetServerHandler(this.server, inetworkmanager, entityplayer);

        // CraftBukkit start -- Don't send a higher than 60 MaxPlayer size, otherwise the PlayerInfo window won't render correctly.
        int maxPlayers = this.getMaxPlayers();
        if (maxPlayers > 60) {
            maxPlayers = 60;
        }
        netserverhandler.sendPacketToPlayer(new Packet1Login(entityplayer.entityId, worldserver.getWorldData().getType(), entityplayer.itemInWorldManager.getGameMode(), worldserver.getWorldData().isHardcore(), worldserver.worldProvider.dimension, worldserver.difficulty, worldserver.getHeight(), maxPlayers));
        entityplayer.getBukkitEntity().sendSupportedChannels();
        // CraftBukkit end

        netserverhandler.sendPacketToPlayer(new Packet6SpawnPosition(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z));
        netserverhandler.sendPacketToPlayer(new Packet202Abilities(entityplayer.capabilities));
        this.b(entityplayer, worldserver);
        // this.sendAll(new Packet3Chat("\u00A7e" + entityplayer.name + " joined the game.")); // CraftBukkit - handled in event
        this.c(entityplayer);
        netserverhandler.a(entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
        this.server.ac().a(netserverhandler);
        netserverhandler.sendPacketToPlayer(new Packet4UpdateTime(worldserver.getTime()));
        if (this.server.getTexturePack().length() > 0) {
            entityplayer.a(this.server.getTexturePack(), this.server.R());
        }

        Iterator iterator = entityplayer.getEffects().iterator();

        while (iterator.hasNext()) {
            MobEffect mobeffect = (MobEffect) iterator.next();

            netserverhandler.sendPacketToPlayer(new Packet41MobEffect(entityplayer.entityId, mobeffect));
        }

        entityplayer.syncInventory();
    }

    public void setPlayerFileData(WorldServer[] aworldserver) {
        if (this.playerFileData != null) return; // CraftBukkit
        this.playerFileData = aworldserver[0].getDataManager().getPlayerFileData();
    }

    public void a(EntityPlayer entityplayer, WorldServer worldserver) {
        WorldServer worldserver1 = entityplayer.q();

        if (worldserver != null) {
            worldserver.getPlayerManager().removePlayer(entityplayer);
        }

        worldserver1.getPlayerManager().addPlayer(entityplayer);
        worldserver1.chunkProviderServer.getChunkAt((int) entityplayer.posX >> 4, (int) entityplayer.posZ >> 4);
    }

    public int a() {
        return PlayerManager.getFurthestViewableBlock(this.o());
    }

    public void a(EntityPlayer entityplayer) {
        NBTTagCompound nbttagcompound = this.server.worlds.get(0).getWorldData().h(); // CraftBukkit

        if (entityplayer.getName().equals(this.server.G()) && nbttagcompound != null) {
            entityplayer.e(nbttagcompound);
        } else {
            this.playerFileData.load(entityplayer);
        }
    }

    protected void b(EntityPlayer entityplayer) {
        this.playerFileData.save(entityplayer);
    }

    public void c(EntityPlayer entityplayer) {
        cserver.detectListNameConflict(entityplayer); // CraftBukkit
        // this.sendAll(new Packet201PlayerInfo(entityplayer.name, true, 1000)); // CraftBukkit - replaced with loop below
        this.playerEntityList.add(entityplayer);
        WorldServer worldserver = this.server.getWorldServer(entityplayer.dimension);

        // CraftBukkit start
        if (!cserver.useExactLoginLocation()) {
            while (!worldserver.getCubes(entityplayer, entityplayer.boundingBox).isEmpty()) {
                entityplayer.setPosition(entityplayer.posX, entityplayer.posY + 1.0D, entityplayer.posZ);
            }
        } else {
            entityplayer.setPosition(entityplayer.posX, entityplayer.posY + entityplayer.getBukkitEntity().getEyeHeight(), entityplayer.posZ);
        }

        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(this.cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.username + " joined the game.");
        this.cserver.getPluginManager().callEvent(playerJoinEvent);

        String joinMessage = playerJoinEvent.getJoinMessage();

        if ((joinMessage != null) && (joinMessage.length() > 0)) {
            this.server.getServerConfigurationManager().sendAll(new Packet3Chat(joinMessage));
        }
        this.cserver.onPlayerJoin(playerJoinEvent.getPlayer());
        // CraftBukkit end

        worldserver.addEntity(entityplayer);
        this.a(entityplayer, (WorldServer) null);
        Iterator iterator = this.playerEntityList.iterator();

        // CraftBukkit start - sendAll above replaced with this loop
        Packet201PlayerInfo packet = new Packet201PlayerInfo(entityplayer.listName, true, 1000);
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer) this.playerEntityList.get(i);

            if (entityplayer1.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer1.serverForThisPlayer.sendPacketToPlayer(packet);
            }
        }
        // CraftBukkit end

        while (iterator.hasNext()) {
            EntityPlayer entityplayer1 = (EntityPlayer) iterator.next();

            // CraftBukkit start - .name -> .listName
            if (entityplayer.getBukkitEntity().canSee(entityplayer1.getBukkitEntity())) {
                entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet201PlayerInfo(entityplayer1.listName, true, entityplayer1.ping));
            }
            // CraftBukkit end
        }
    }

    public void d(EntityPlayer entityplayer) {
        entityplayer.q().getPlayerManager().movePlayer(entityplayer);
    }

    public String disconnect(EntityPlayer entityplayer) { // CraftBukkit - return string
        if (entityplayer.serverForThisPlayer.disconnected) return null; // CraftBukkit - exploitsies fix

        // CraftBukkit start - quitting must be before we do final save of data, in case plugins need to modify it
        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(this.cserver.getPlayer(entityplayer), "\u00A7e" + entityplayer.username + " left the game.");
        this.cserver.getPluginManager().callEvent(playerQuitEvent);
        // CraftBukkit end

        this.b(entityplayer);
        WorldServer worldserver = entityplayer.q();

        worldserver.kill(entityplayer);
        worldserver.getPlayerManager().removePlayer(entityplayer);
        this.playerEntityList.remove(entityplayer);

        // CraftBukkit start - .name -> .listName, replace sendAll with loop
        Packet201PlayerInfo packet = new Packet201PlayerInfo(entityplayer.listName, false, 9999);
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer) this.playerEntityList.get(i);

            if (entityplayer1.getBukkitEntity().canSee(entityplayer.getBukkitEntity())) {
                entityplayer1.serverForThisPlayer.sendPacketToPlayer(packet);
            }
        }

        return playerQuitEvent.getQuitMessage();
        // CraftBukkit end
    }

    // CraftBukkit start - Whole method and signature
    public EntityPlayer attemptLogin(NetLoginHandler netloginhandler, String s, String hostname) {
        // Instead of kicking then returning, we need to store the kick reason
        // in the event, check with plugins to see if it's ok, and THEN kick
        // depending on the outcome.
        EntityPlayer entity = new EntityPlayer(this.server, this.server.getWorldServer(0), s, this.server.L() ? new DemoItemInWorldManager(this.server.getWorldServer(0)) : new ItemInWorldManager(this.server.getWorldServer(0)));
        Player player = entity.getBukkitEntity();
        PlayerLoginEvent event = new PlayerLoginEvent(player, hostname, netloginhandler.getSocket().getInetAddress());

        SocketAddress socketaddress = netloginhandler.networkManager.getSocketAddress();

        if (this.banByName.isBanned(s)) {
            BanEntry banentry = (BanEntry) this.banByName.getEntries().get(s);
            String s1 = "You are banned from this server!\nReason: " + banentry.getReason();

            if (banentry.getExpires() != null) {
                s1 = s1 + "\nYour ban will be removed on " + e.format(banentry.getExpires());
            }

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s1);
        } else if (!this.isWhitelisted(s)) {
            event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, "You are not white-listed on this server!");
        } else {
            String s2 = socketaddress.toString();

            s2 = s2.substring(s2.indexOf("/") + 1);
            s2 = s2.substring(0, s2.indexOf(":"));
            if (this.banByIP.isBanned(s2)) {
                BanEntry banentry1 = (BanEntry) this.banByIP.getEntries().get(s2);
                String s3 = "Your IP address is banned from this server!\nReason: " + banentry1.getReason();

                if (banentry1.getExpires() != null) {
                    s3 = s3 + "\nYour ban will be removed on " + e.format(banentry1.getExpires());
                }

                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, s3);
            } else if (this.playerEntityList.size() >= this.maxPlayers) {
                event.disallow(PlayerLoginEvent.Result.KICK_FULL, "The server is full!");
            } else {
                event.disallow(PlayerLoginEvent.Result.ALLOWED, s2);
            }
        }

        this.cserver.getPluginManager().callEvent(event);
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            netloginhandler.disconnect(event.getKickMessage());
            return null;
        }

        return entity;
        // CraftBukkit end
    }

    public EntityPlayer processLogin(EntityPlayer player) { // CraftBukkit - String -> EntityPlayer
        String s = player.username; // CraftBukkit
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.playerEntityList.iterator();

        EntityPlayer entityplayer;

        while (iterator.hasNext()) {
            entityplayer = (EntityPlayer) iterator.next();
            if (entityplayer.username.equalsIgnoreCase(s)) {
                arraylist.add(entityplayer);
            }
        }

        iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            entityplayer = (EntityPlayer) iterator.next();
            entityplayer.serverForThisPlayer.kickPlayerFromServer("You logged in from another location");
        }

        /* CraftBukkit start
        Object object;

        if (this.server.L()) {
            object = new DemoItemInWorldManager(this.server.getWorldServer(0));
        } else {
            object = new ItemInWorldManager(this.server.getWorldServer(0));
        }

        return new EntityPlayer(this.server, this.server.getWorldServer(0), s, (ItemInWorldManager) object);
        */
        return player;
        // CraftBukkit end
    }

    // CraftBukkit start
    public EntityPlayer moveToWorld(EntityPlayer entityplayer, int i, boolean flag) {
        return this.moveToWorld(entityplayer, i, flag, null);
    }

    public EntityPlayer moveToWorld(EntityPlayer entityplayer, int i, boolean flag, Location location) {
        // CraftBukkit end
        entityplayer.q().getTracker().untrackPlayer(entityplayer);
        // entityplayer.q().getTracker().untrackEntity(entityplayer); // CraftBukkit
        entityplayer.q().getPlayerManager().removePlayer(entityplayer);
        this.playerEntityList.remove(entityplayer);
        this.server.getWorldServer(entityplayer.dimension).removeEntity(entityplayer);
        ChunkCoordinates chunkcoordinates = entityplayer.getBed();

        // CraftBukkit start
        EntityPlayer entityplayer1 = entityplayer;
        org.bukkit.World fromWorld = entityplayer1.getBukkitEntity().getWorld();
        entityplayer1.viewingCredits = false;
        entityplayer1.copyTo(entityplayer, flag);

        ChunkCoordinates chunkcoordinates1;

        if (location == null) {
            boolean isBedSpawn = false;
            CraftWorld cworld = (CraftWorld) this.server.server.getWorld(entityplayer.spawnWorld);
            if (cworld != null && chunkcoordinates != null) {
                chunkcoordinates1 = EntityHuman.getBed(cworld.getHandle(), chunkcoordinates);
                if (chunkcoordinates1 != null) {
                    isBedSpawn = true;
                    location = new Location(cworld, chunkcoordinates1.x + 0.5, chunkcoordinates1.y, chunkcoordinates1.z + 0.5);
                } else {
                    entityplayer1.setRespawnPosition(null);
                    entityplayer1.serverForThisPlayer.sendPacketToPlayer(new Packet70Bed(0, 0));
                }
            }

            if (location == null) {
                cworld = (CraftWorld) this.server.server.getWorlds().get(0);
                chunkcoordinates = cworld.getHandle().getSpawn();
                location = new Location(cworld, chunkcoordinates.x + 0.5, chunkcoordinates.y, chunkcoordinates.z + 0.5);
            }

            Player respawnPlayer = this.cserver.getPlayer(entityplayer1);
            PlayerRespawnEvent respawnEvent = new PlayerRespawnEvent(respawnPlayer, location, isBedSpawn);
            this.cserver.getPluginManager().callEvent(respawnEvent);

            location = respawnEvent.getRespawnLocation();
            entityplayer.reset();
        } else {
            location.setWorld(this.server.getWorldServer(i).getWorld());
        }
        WorldServer worldserver = ((CraftWorld) location.getWorld()).getHandle();
        entityplayer1.setPositionAndRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // CraftBukkit end

        worldserver.chunkProviderServer.getChunkAt((int) entityplayer1.posX >> 4, (int) entityplayer1.posZ >> 4);

        while (!worldserver.getCubes(entityplayer1, entityplayer1.boundingBox).isEmpty()) {
            entityplayer1.setPosition(entityplayer1.posX, entityplayer1.posY + 1.0D, entityplayer1.posZ);
        }

        // CraftBukkit start
        byte actualDimension = (byte) (worldserver.getWorld().getEnvironment().getId());
        // Force the client to refresh their chunk cache.
        entityplayer1.serverForThisPlayer.sendPacketToPlayer(new Packet9Respawn((byte) (actualDimension >= 0 ? -1 : 0), (byte) worldserver.difficulty, worldserver.getWorldData().getType(), worldserver.getHeight(), entityplayer.itemInWorldManager.getGameMode()));
        entityplayer1.serverForThisPlayer.sendPacketToPlayer(new Packet9Respawn(actualDimension, (byte) worldserver.difficulty, worldserver.getWorldData().getType(), worldserver.getHeight(), entityplayer.itemInWorldManager.getGameMode()));
        entityplayer1.spawnIn(worldserver);
        entityplayer1.isDead = false;
        entityplayer1.serverForThisPlayer.teleport(new Location(worldserver.getWorld(), entityplayer1.posX, entityplayer1.posY, entityplayer1.posZ, entityplayer1.rotationYaw, entityplayer1.rotationPitch));

        chunkcoordinates1 = worldserver.getSpawn();
        // CraftBukkit end
        entityplayer1.serverForThisPlayer.sendPacketToPlayer(new Packet6SpawnPosition(chunkcoordinates1.x, chunkcoordinates1.y, chunkcoordinates1.z));
        this.b(entityplayer1, worldserver);
        worldserver.getPlayerManager().addPlayer(entityplayer1);
        worldserver.addEntity(entityplayer1);
        this.playerEntityList.add(entityplayer1);
        // CraftBukkit start - added from changeDimension
        this.updateClient(entityplayer1); // CraftBukkit
        Iterator iterator = entityplayer1.getEffects().iterator();

        while (iterator.hasNext()) {
            MobEffect mobeffect = (MobEffect) iterator.next();

            entityplayer1.serverForThisPlayer.sendPacketToPlayer(new Packet41MobEffect(entityplayer1.entityId, mobeffect));
        }
        // entityplayer1.syncInventory();
        // CraftBukkit end

        // CraftBukkit start - don't fire on respawn
        if (fromWorld != location.getWorld()) {
            PlayerChangedWorldEvent event = new PlayerChangedWorldEvent((Player) entityplayer1.getBukkitEntity(), fromWorld);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
        // CraftBukkit end

        return entityplayer1;
    }

    public void changeDimension(EntityPlayer entityplayer, int i) {
        // CraftBukkit start -- Replaced the standard handling of portals with a more customised method.
        int dimension = i;
        WorldServer fromWorld = this.server.getWorldServer(entityplayer.dimension);
        WorldServer toWorld = null;
        if (entityplayer.dimension < 10) {
            for (WorldServer world : this.server.worlds) {
                if (world.dimension == dimension) {
                    toWorld = world;
                }
            }
        }

        Location fromLocation = new Location(fromWorld.getWorld(), entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
        Location toLocation = null;

        if (toWorld != null) {
            if (((dimension == -1) || (dimension == 0)) && ((entityplayer.dimension == -1) || (entityplayer.dimension == 0))) {
                double blockRatio = dimension == 0 ? 8 : 0.125;

                toLocation = toWorld == null ? null : new Location(toWorld.getWorld(), (entityplayer.posX * blockRatio), entityplayer.posY, (entityplayer.posZ * blockRatio), entityplayer.rotationYaw, entityplayer.rotationPitch);
            } else {
                ChunkCoordinates coords = toWorld.getDimensionSpawn();
                if (coords != null) {
                    toLocation = new Location(toWorld.getWorld(), coords.x, coords.y, coords.z, 90, 0);
                }
            }
        }

        TeleportCause cause = TeleportCause.UNKNOWN;
        int playerEnvironmentId = entityplayer.getBukkitEntity().getWorld().getEnvironment().getId();
        switch (dimension) {
            case -1:
                cause = TeleportCause.NETHER_PORTAL;
                break;
            case 0:
                if (playerEnvironmentId == -1) {
                    cause = TeleportCause.NETHER_PORTAL;
                } else if (playerEnvironmentId == 1) {
                    cause = TeleportCause.END_PORTAL;
                }

                break;
            case 1:
                cause = TeleportCause.END_PORTAL;
                break;
        }

        org.bukkit.craftbukkit.PortalTravelAgent pta = new org.bukkit.craftbukkit.PortalTravelAgent();
        PlayerPortalEvent event = new PlayerPortalEvent((Player) entityplayer.getBukkitEntity(), fromLocation, toLocation, pta, cause);

        if (entityplayer.dimension == 1) {
            event.useTravelAgent(false);
        }

        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled() || event.getTo() == null) {
            return;
        }

        Location finalLocation = event.getTo();
        if (event.useTravelAgent()) {
            finalLocation = event.getPortalTravelAgent().findOrCreate(finalLocation);
        }
        toWorld = ((CraftWorld) finalLocation.getWorld()).getHandle();
        this.moveToWorld(entityplayer, toWorld.dimension, true, finalLocation);
        // CraftBukkit end
    }

    public void tick() {
        if (++this.o > 600) {
            this.o = 0;
        }

        /* CraftBukkit start - remove updating of lag to players -- it spams way to much on big servers.
        if (this.o < this.players.size()) {
            EntityPlayer entityplayer = (EntityPlayer) this.players.get(this.o);

            this.sendAll(new Packet201PlayerInfo(entityplayer.name, true, entityplayer.ping));
        }
        // CraftBukkit end */
    }

    public void sendAll(Packet packet) {
        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            ((EntityPlayer) this.playerEntityList.get(i)).serverForThisPlayer.sendPacketToPlayer(packet);
        }
    }

    public void a(Packet packet, int i) {
        Iterator iterator = this.playerEntityList.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (entityplayer.dimension == i) {
                entityplayer.serverForThisPlayer.sendPacketToPlayer(packet);
            }
        }
    }

    public String c() {
        String s = "";

        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            if (i > 0) {
                s = s + ", ";
            }

            s = s + ((EntityPlayer) this.playerEntityList.get(i)).username;
        }

        return s;
    }

    public String[] d() {
        String[] astring = new String[this.playerEntityList.size()];

        for (int i = 0; i < this.playerEntityList.size(); ++i) {
            astring[i] = ((EntityPlayer) this.playerEntityList.get(i)).username;
        }

        return astring;
    }

    public BanList getNameBans() {
        return this.banByName;
    }

    public BanList getIPBans() {
        return this.banByIP;
    }

    public void addOp(String s) {
        this.operators.add(s.toLowerCase());

        // CraftBukkit start
        Player player = server.server.getPlayer(s);
        if (player != null) {
            player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    public void removeOp(String s) {
        this.operators.remove(s.toLowerCase());

        // CraftBukkit start
        Player player = server.server.getPlayer(s);
        if (player != null) {
            player.recalculatePermissions();
        }
        // CraftBukkit end
    }

    public boolean isWhitelisted(String s) {
        s = s.trim().toLowerCase();
        return !this.hasWhitelist || this.operators.contains(s) || this.whitelist.contains(s);
    }

    public boolean isOp(String s) {
        // CraftBukkit
        return this.operators.contains(s.trim().toLowerCase()) || this.server.H() && this.server.worlds.get(0).getWorldData().allowCommands() && this.server.G().equalsIgnoreCase(s) || this.n;
    }

    public EntityPlayer f(String s) {
        Iterator iterator = this.playerEntityList.iterator();

        EntityPlayer entityplayer;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entityplayer = (EntityPlayer) iterator.next();
        } while (!entityplayer.username.equalsIgnoreCase(s));

        return entityplayer;
    }

    public void sendPacketNearby(double d0, double d1, double d2, double d3, int i, Packet packet) {
        this.sendPacketNearby((EntityHuman) null, d0, d1, d2, d3, i, packet);
    }

    public void sendPacketNearby(EntityHuman entityhuman, double d0, double d1, double d2, double d3, int i, Packet packet) {
        Iterator iterator = this.playerEntityList.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (entityplayer != entityhuman && entityplayer.dimension == i) {
                double d4 = d0 - entityplayer.posX;
                double d5 = d1 - entityplayer.posY;
                double d6 = d2 - entityplayer.posZ;

                if (d4 * d4 + d5 * d5 + d6 * d6 < d3 * d3) {
                    entityplayer.serverForThisPlayer.sendPacketToPlayer(packet);
                }
            }
        }
    }

    public void savePlayers() {
        Iterator iterator = this.playerEntityList.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            this.b(entityplayer);
        }
    }

    public void addWhitelist(String s) {
        this.whitelist.add(s);
    }

    public void removeWhitelist(String s) {
        this.whitelist.remove(s);
    }

    public Set getWhitelisted() {
        return this.whitelist;
    }

    public Set getOPs() {
        return this.operators;
    }

    public void reloadWhitelist() {}

    public void b(EntityPlayer entityplayer, WorldServer worldserver) {
        entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet4UpdateTime(worldserver.getTime()));
        if (worldserver.J()) {
            entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet70Bed(1, 0));
        }
    }

    public void updateClient(EntityPlayer entityplayer) {
        entityplayer.updateInventory(entityplayer.defaultContainer);
        entityplayer.n();
    }

    public int getPlayerCount() {
        return this.playerEntityList.size();
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public String[] getSeenPlayers() {
        return this.server.worlds.get(0).getDataManager().getPlayerFileData().getSeenPlayers(); // CraftBukkit
    }

    public boolean getHasWhitelist() {
        return this.hasWhitelist;
    }

    public void setHasWhitelist(boolean flag) {
        this.hasWhitelist = flag;
    }

    public List j(String s) {
        ArrayList arraylist = new ArrayList();
        Iterator iterator = this.playerEntityList.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (entityplayer.r().equals(s)) {
                arraylist.add(entityplayer);
            }
        }

        return arraylist;
    }

    public int o() {
        return this.d;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public NBTTagCompound q() {
        return null;
    }

    private void a(EntityPlayer entityplayer, EntityPlayer entityplayer1, World world) {
        if (entityplayer1 != null) {
            entityplayer.itemInWorldManager.setGameMode(entityplayer1.itemInWorldManager.getGameMode());
        } else if (this.m != null) {
            entityplayer.itemInWorldManager.setGameMode(this.m);
        }

        entityplayer.itemInWorldManager.b(world.getWorldData().getGameType());
    }

    public void r() {
        while (!this.playerEntityList.isEmpty()) {
            ((EntityPlayer) this.playerEntityList.get(0)).serverForThisPlayer.kickPlayerFromServer("Server closed");
        }
    }
}
