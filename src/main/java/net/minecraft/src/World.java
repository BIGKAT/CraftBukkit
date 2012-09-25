package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

// CraftBukkit start

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFluids;
import net.minecraft.server.BlockStairs;
import net.minecraft.server.BlockStepAbstract;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkCache;
import net.minecraft.server.ChunkCoordinates;
import net.minecraft.server.ChunkPosition;
import net.minecraft.server.ChunkProviderServer;
import net.minecraft.src.EntityMob;
import net.minecraft.server.CrashReportChunkStats;
import net.minecraft.server.CrashReportEntities;
import net.minecraft.server.CrashReportPlayers;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityGolem;
import net.minecraft.server.EntityWaterAnimal;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IWorldAccess;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.PathEntity;
import net.minecraft.server.Pathfinder;
import net.minecraft.server.VillageCollection;
import net.minecraft.server.WorldChunkManager;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldMapBase;
import net.minecraft.server.WorldMapCollection;
import net.minecraft.server.WorldProvider;
import net.minecraft.server.WorldSettings;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.util.LongHashSet;
import org.bukkit.craftbukkit.util.UnsafeList;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
// CraftBukkit end

public abstract class World implements IBlockAccess {

    public boolean e = false;
    public List entityList = new ArrayList();
    protected List g = new ArrayList();
    public List tileEntityList = new ArrayList();
    private List a = new ArrayList();
    private List b = new ArrayList();
    public List players = new ArrayList();
    public List j = new ArrayList();
    private long c = 16777215L;
    public int k = 0;
    protected int l = (new Random()).nextInt();
    protected final int m = 1013904223;
    protected float n;
    protected float o;
    protected float p;
    protected float q;
    protected int r = 0;
    public int s = 0;
    public boolean suppressPhysics = false;
    public int difficulty;
    public Random rand = new Random();
    public WorldProvider worldProvider; // CraftBukkit - remove final
    protected List x = new ArrayList();
    public IChunkProvider chunkProvider; // CraftBukkit - protected -> public
    protected final ISaveHandler dataManager;
    public WorldData worldData; // CraftBukkit - protected -> public
    public boolean isLoading;
    public WorldMapCollection worldMaps;
    public final VillageCollection villages = new VillageCollection(this);
    protected final VillageSiege siegeManager = new VillageSiege(this);
    public final Profiler methodProfiler;
    private UnsafeList d = new UnsafeList(); // CraftBukkit - ArrayList -> UnsafeList
    private boolean L;
    // CraftBukkit start - public, longhashset
    public boolean allowMonsters = true;
    public boolean allowAnimals = true;
    protected LongHashSet chunkTickList = new LongHashSet();
    public long ticksPerAnimalSpawns;
    public long ticksPerMonsterSpawns;
    // CraftBukkit end
    private int M;
    int[] J;
    private List N;
    public boolean isStatic;

    public BiomeGenBase getBiome(int i, int j) {
        if (this.isLoaded(i, 0, j)) {
            Chunk chunk = this.getChunkAtWorldCoords(i, j);

            if (chunk != null) {
                return chunk.getBiomeGenForWorldCoords(i & 15, j & 15, this.worldProvider.c);
            }
        }

        return this.worldProvider.c.getBiome(i, j);
    }

    public WorldChunkManager getWorldChunkManager() {
        return this.worldProvider.c;
    }

    // CraftBukkit start
    private final CraftWorld world;
    public boolean pvpMode;
    public boolean keepSpawnInMemory = true;
    public ChunkGenerator generator;
    Chunk lastChunkAccessed;
    int lastXAccessed = Integer.MIN_VALUE;
    int lastZAccessed = Integer.MIN_VALUE;
    final Object chunkLock = new Object();

    public CraftWorld getWorld() {
        return this.world;
    }

    public CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

    // Changed signature
    public World(ISaveHandler idatamanager, String s, WorldSettings worldsettings, WorldProvider worldprovider, Profiler methodprofiler, ChunkGenerator gen, org.bukkit.World.Environment env) {
        this.generator = gen;
        this.world = new CraftWorld((net.minecraft.src.WorldServer) this, gen, env);
        this.ticksPerAnimalSpawns = this.getServer().getTicksPerAnimalSpawns(); // CraftBukkit
        this.ticksPerMonsterSpawns = this.getServer().getTicksPerMonsterSpawns(); // CraftBukkit
        // CraftBukkit end

        this.M = this.rand.nextInt(12000);
        this.J = new int['\u8000'];
        this.N = new UnsafeList(); // CraftBukkit - ArrayList -> UnsafeList
        this.isStatic = false;
        this.dataManager = idatamanager;
        this.methodProfiler = methodprofiler;
        this.worldMaps = new WorldMapCollection(idatamanager);
        this.worldData = idatamanager.getWorldData();
        if (worldprovider != null) {
            this.worldProvider = worldprovider;
        } else if (this.worldData != null && this.worldData.i() != 0) {
            this.worldProvider = WorldProvider.byDimension(this.worldData.i());
        } else {
            this.worldProvider = WorldProvider.byDimension(0);
        }

        if (this.worldData == null) {
            this.worldData = new WorldData(worldsettings, s);
        } else {
            this.worldData.setName(s);
        }

        this.worldProvider.a(this);
        this.chunkProvider = this.i();
        if (!this.worldData.isInitialized()) {
            this.a(worldsettings);
            this.worldData.d(true);
        }

        this.v();
        this.a();

        this.getServer().addWorld(this.world); // CraftBukkit
    }

    protected abstract IChunkProvider i();

    protected void a(WorldSettings worldsettings) {
        this.worldData.d(true);
    }

    public int b(int i, int j) {
        int k;

        for (k = 63; !this.isEmpty(i, k + 1, j); ++k) {
            ;
        }

        return this.getBlockId(i, k, j);
    }

    public int getBlockId(int i, int j, int k) {
        return i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000 ? (j < 0 ? 0 : (j >= 256 ? 0 : this.getChunkAt(i >> 4, k >> 4).getBlockID(i & 15, j, k & 15))) : 0;
    }

    public int b(int i, int j, int k) {
        return i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000 ? (j < 0 ? 0 : (j >= 256 ? 0 : this.getChunkAt(i >> 4, k >> 4).b(i & 15, j, k & 15))) : 0;
    }

    public boolean isEmpty(int i, int j, int k) {
        return this.getBlockId(i, j, k) == 0;
    }

    public boolean isTileEntity(int i, int j, int k) {
        int l = this.getBlockId(i, j, k);

        return Block.blocksList[l] != null && Block.blocksList[l].s();
    }

    public boolean isLoaded(int i, int j, int k) {
        return j >= 0 && j < 256 ? this.isChunkLoaded(i >> 4, k >> 4) : false;
    }

    public boolean areChunksLoaded(int i, int j, int k, int l) {
        return this.c(i - l, j - l, k - l, i + l, j + l, k + l);
    }

    public boolean c(int i, int j, int k, int l, int i1, int j1) {
        if (i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    // CraftBukkit - check unload queue too so we don't leak a chunk
                    if (!this.isChunkLoaded(k1, l1) || ((net.minecraft.src.WorldServer) this).chunkProviderServer.unloadQueue.contains(k1, l1)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean isChunkLoaded(int i, int j) {
        return this.chunkProvider.isChunkLoaded(i, j);
    }

    public Chunk getChunkAtWorldCoords(int i, int j) {
        return this.getChunkAt(i >> 4, j >> 4);
    }

    // CraftBukkit start
    public Chunk getChunkAt(int i, int j) {
        Chunk result = null;
        synchronized (this.chunkLock) {
            if (this.lastChunkAccessed == null || this.lastXAccessed != i || this.lastZAccessed != j) {
                this.lastXAccessed = i;
                this.lastZAccessed = j;
                this.lastChunkAccessed = this.chunkProvider.getOrCreateChunk(i, j);
            }
            result = this.lastChunkAccessed;
        }
        return result;
    }
    // CraftBukkit end

    public boolean setBlockAndMetadata(int i, int j, int k, int l, int i1) {
        return this.setRawTypeIdAndData(i, j, k, l, i1, true);
    }

    public boolean setRawTypeIdAndData(int i, int j, int k, int l, int i1, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
                boolean flag1 = chunk.a(i & 15, j, k & 15, l, i1);

                // this.methodProfiler.a("checkLight"); // CraftBukkit - not in production code
                this.x(i, j, k);
                // this.methodProfiler.b(); // CraftBukkit - not in production code
                if (flag && flag1 && (this.isStatic || chunk.seenByPlayer)) {
                    this.markBlockNeedsUpdate(i, j, k);
                }

                return flag1;
            }
        } else {
            return false;
        }
    }

    public boolean setBlock(int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
                boolean flag = chunk.a(i & 15, j, k & 15, l);

                // this.methodProfiler.a("checkLight"); // CraftBukkit - not in production code
                this.x(i, j, k);
                // this.methodProfiler.b(); // CraftBukkit - not in production code
                if (flag && (this.isStatic || chunk.seenByPlayer)) {
                    this.markBlockNeedsUpdate(i, j, k);
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public Material getMaterial(int i, int j, int k) {
        int l = this.getBlockId(i, j, k);

        return l == 0 ? Material.AIR : Block.blocksList[l].blockMaterial;
    }

    public int getData(int i, int j, int k) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return 0;
            } else if (j >= 256) {
                return 0;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.getBlockMetadata(i, j, k);
            }
        } else {
            return 0;
        }
    }

    public void setBlockMetadataWithNotify(int i, int j, int k, int l) {
        if (this.setBlockMetadata(i, j, k, l)) {
            this.update(i, j, k, this.getBlockId(i, j, k));
        }
    }

    public boolean setBlockMetadata(int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);
                int i1 = i & 15;
                int j1 = k & 15;
                boolean flag = chunk.b(i1, j, j1, l);

                if (flag && (this.isStatic || chunk.seenByPlayer && Block.r[chunk.getBlockID(i1, j, j1) & 4095])) {
                    this.markBlockNeedsUpdate(i, j, k);
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public boolean setBlockWithNotify(int i, int j, int k, int l) {
        // CraftBukkit start
        int old = this.getBlockId(i, j, k);
        if (this.setBlock(i, j, k, l)) {
            this.update(i, j, k, l == 0 ? old : l);
            // CraftBukkit end
            return true;
        } else {
            return false;
        }
    }

    public boolean setBlockAndMetadataWithNotify(int i, int j, int k, int l, int i1) {
        if (this.setBlockAndMetadata(i, j, k, l, i1)) {
            this.update(i, j, k, l);
            return true;
        } else {
            return false;
        }
    }

    public void markBlockNeedsUpdate(int i, int j, int k) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.a(i, j, k);
        }
    }

    public void update(int i, int j, int k, int l) {
        this.applyPhysics(i, j, k, l);
    }

    public void g(int i, int j, int k, int l) {
        int i1;

        if (k > l) {
            i1 = l;
            l = k;
            k = i1;
        }

        if (!this.worldProvider.e) {
            for (i1 = k; i1 <= l; ++i1) {
                this.c(EnumSkyBlock.Sky, i, i1, j);
            }
        }

        this.d(i, k, j, i, l, j);
    }

    public void i(int i, int j, int k) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.a(i, j, k, i, j, k);
        }
    }

    public void d(int i, int j, int k, int l, int i1, int j1) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.a(i, j, k, l, i1, j1);
        }
    }

    public void applyPhysics(int i, int j, int k, int l) {
        this.m(i - 1, j, k, l);
        this.m(i + 1, j, k, l);
        this.m(i, j - 1, k, l);
        this.m(i, j + 1, k, l);
        this.m(i, j, k - 1, l);
        this.m(i, j, k + 1, l);
    }

    private void m(int i, int j, int k, int l) {
        if (!this.suppressPhysics && !this.isStatic) {
            Block block = Block.blocksList[this.getBlockId(i, j, k)];

            if (block != null) {
                // CraftBukkit start
                CraftWorld world = ((net.minecraft.src.WorldServer) this).getWorld();
                if (world != null) {
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(i, j, k), l);
                    this.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
                // CraftBukkit end

                block.doPhysics(this, i, j, k, l);
            }
        }
    }

    public boolean j(int i, int j, int k) {
        return this.getChunkAt(i >> 4, k >> 4).d(i & 15, j, k & 15);
    }

    public int k(int i, int j, int k) {
        if (j < 0) {
            return 0;
        } else {
            if (j >= 256) {
                j = 255;
            }

            return this.getChunkAt(i >> 4, k >> 4).c(i & 15, j, k & 15, 0);
        }
    }

    public int getBlockLightValue(int i, int j, int k) {
        return this.a(i, j, k, true);
    }

    public int a(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (flag) {
                int l = this.getBlockId(i, j, k);

                if (l == Block.STEP.blockID || l == Block.WOOD_STEP.blockID || l == Block.SOIL.blockID || l == Block.COBBLESTONE_STAIRS.blockID || l == Block.WOOD_STAIRS.blockID) {
                    int i1 = this.a(i, j + 1, k, false);
                    int j1 = this.a(i + 1, j, k, false);
                    int k1 = this.a(i - 1, j, k, false);
                    int l1 = this.a(i, j, k + 1, false);
                    int i2 = this.a(i, j, k - 1, false);

                    if (j1 > i1) {
                        i1 = j1;
                    }

                    if (k1 > i1) {
                        i1 = k1;
                    }

                    if (l1 > i1) {
                        i1 = l1;
                    }

                    if (i2 > i1) {
                        i1 = i2;
                    }

                    return i1;
                }
            }

            if (j < 0) {
                return 0;
            } else {
                if (j >= 256) {
                    j = 255;
                }

                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return chunk.c(i, j, k, this.k);
            }
        } else {
            return 15;
        }
    }

    public int getHighestBlockYAt(int i, int j) {
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (!this.isChunkLoaded(i >> 4, j >> 4)) {
                return 0;
            } else {
                Chunk chunk = this.getChunkAt(i >> 4, j >> 4);

                return chunk.b(i & 15, j & 15);
            }
        } else {
            return 0;
        }
    }

    public int b(EnumSkyBlock enumskyblock, int i, int j, int k) {
        if (j < 0) {
            j = 0;
        }

        if (j >= 256) {
            j = 255;
        }

        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            int l = i >> 4;
            int i1 = k >> 4;

            if (!this.isChunkLoaded(l, i1)) {
                return enumskyblock.c;
            } else {
                Chunk chunk = this.getChunkAt(l, i1);

                return chunk.getSavedLightValue(enumskyblock, i & 15, j, k & 15);
            }
        } else {
            return enumskyblock.c;
        }
    }

    public void b(EnumSkyBlock enumskyblock, int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j >= 0) {
                if (j < 256) {
                    if (this.isChunkLoaded(i >> 4, k >> 4)) {
                        Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                        chunk.a(enumskyblock, i & 15, j, k & 15, l);
                        Iterator iterator = this.x.iterator();

                        while (iterator.hasNext()) {
                            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

                            iworldaccess.b(i, j, k);
                        }
                    }
                }
            }
        }
    }

    public void n(int i, int j, int k) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.b(i, j, k);
        }
    }

    public float o(int i, int j, int k) {
        return this.worldProvider.f[this.getBlockLightValue(i, j, k)];
    }

    public boolean s() {
        return this.k < 4;
    }

    public MovingObjectPosition a(Vec3 vec3d, Vec3 vec3d1) {
        return this.rayTrace(vec3d, vec3d1, false, false);
    }

    public MovingObjectPosition rayTrace(Vec3 vec3d, Vec3 vec3d1, boolean flag) {
        return this.rayTrace(vec3d, vec3d1, flag, false);
    }

    public MovingObjectPosition rayTrace(Vec3 vec3d, Vec3 vec3d1, boolean flag, boolean flag1) {
        if (!Double.isNaN(vec3d.a) && !Double.isNaN(vec3d.b) && !Double.isNaN(vec3d.c)) {
            if (!Double.isNaN(vec3d1.a) && !Double.isNaN(vec3d1.b) && !Double.isNaN(vec3d1.c)) {
                int i = MathHelper.floor(vec3d1.a);
                int j = MathHelper.floor(vec3d1.b);
                int k = MathHelper.floor(vec3d1.c);
                int l = MathHelper.floor(vec3d.a);
                int i1 = MathHelper.floor(vec3d.b);
                int j1 = MathHelper.floor(vec3d.c);
                int k1 = this.getBlockId(l, i1, j1);
                int l1 = this.getData(l, i1, j1);
                Block block = Block.blocksList[k1];

                if ((!flag1 || block == null || block.e(this, l, i1, j1) != null) && k1 > 0 && block.a(l1, flag)) {
                    MovingObjectPosition movingobjectposition = block.a(this, l, i1, j1, vec3d, vec3d1);

                    if (movingobjectposition != null) {
                        return movingobjectposition;
                    }
                }

                k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(vec3d.a) || Double.isNaN(vec3d.b) || Double.isNaN(vec3d.c)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return null;
                    }

                    boolean flag2 = true;
                    boolean flag3 = true;
                    boolean flag4 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag3 = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag4 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = vec3d1.a - vec3d.a;
                    double d7 = vec3d1.b - vec3d.b;
                    double d8 = vec3d1.c - vec3d.c;

                    if (flag2) {
                        d3 = (d0 - vec3d.a) / d6;
                    }

                    if (flag3) {
                        d4 = (d1 - vec3d.b) / d7;
                    }

                    if (flag4) {
                        d5 = (d2 - vec3d.c) / d8;
                    }

                    boolean flag5 = false;
                    byte b0;

                    if (d3 < d4 && d3 < d5) {
                        if (i > l) {
                            b0 = 4;
                        } else {
                            b0 = 5;
                        }

                        vec3d.a = d0;
                        vec3d.b += d7 * d3;
                        vec3d.c += d8 * d3;
                    } else if (d4 < d5) {
                        if (j > i1) {
                            b0 = 0;
                        } else {
                            b0 = 1;
                        }

                        vec3d.a += d6 * d4;
                        vec3d.b = d1;
                        vec3d.c += d8 * d4;
                    } else {
                        if (k > j1) {
                            b0 = 2;
                        } else {
                            b0 = 3;
                        }

                        vec3d.a += d6 * d5;
                        vec3d.b += d7 * d5;
                        vec3d.c = d2;
                    }

                    Vec3 vec3d2 = Vec3.a().create(vec3d.a, vec3d.b, vec3d.c);

                    l = (int) (vec3d2.a = (double) MathHelper.floor(vec3d.a));
                    if (b0 == 5) {
                        --l;
                        ++vec3d2.a;
                    }

                    i1 = (int) (vec3d2.b = (double) MathHelper.floor(vec3d.b));
                    if (b0 == 1) {
                        --i1;
                        ++vec3d2.b;
                    }

                    j1 = (int) (vec3d2.c = (double) MathHelper.floor(vec3d.c));
                    if (b0 == 3) {
                        --j1;
                        ++vec3d2.c;
                    }

                    int i2 = this.getBlockId(l, i1, j1);
                    int j2 = this.getData(l, i1, j1);
                    Block block1 = Block.blocksList[i2];

                    if ((!flag1 || block1 == null || block1.e(this, l, i1, j1) != null) && i2 > 0 && block1.a(j2, flag)) {
                        MovingObjectPosition movingobjectposition1 = block1.a(this, l, i1, j1, vec3d, vec3d1);

                        if (movingobjectposition1 != null) {
                            Vec3.a().release(vec3d2); // CraftBukkit
                            return movingobjectposition1;
                        }
                    }
                    Vec3.a().release(vec3d2); // CraftBukkit
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void makeSound(Entity entity, String s, float f, float f1) {
        if (entity != null && s != null) {
            Iterator iterator = this.x.iterator();

            while (iterator.hasNext()) {
                IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

                iworldaccess.a(s, entity.posX, entity.posY - (double) entity.height, entity.posZ, f, f1);
            }
        }
    }

    public void makeSound(double d0, double d1, double d2, String s, float f, float f1) {
        if (s != null) {
            Iterator iterator = this.x.iterator();

            while (iterator.hasNext()) {
                IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

                iworldaccess.a(s, d0, d1, d2, f, f1);
            }
        }
    }

    public void a(String s, int i, int j, int k) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.a(s, i, j, k);
        }
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.a(s, d0, d1, d2, d3, d4, d5);
        }
    }

    public boolean strikeLightning(Entity entity) {
        this.j.add(entity);
        return true;
    }

    // CraftBukkit start - used for entities other than creatures
    public boolean addEntity(Entity entity) {
        return this.addEntity(entity, SpawnReason.DEFAULT); // Set reason as DEFAULT
    }

    public boolean addEntity(Entity entity, SpawnReason spawnReason) { // Changed signature, added SpawnReason
        if (entity == null) return false;
        // CraftBukkit end

        int i = MathHelper.floor(entity.posX / 16.0D);
        int j = MathHelper.floor(entity.posZ / 16.0D);
        boolean flag = false;

        if (entity instanceof EntityPlayer) {
            flag = true;
        }

        // CraftBukkit start
        org.bukkit.event.Cancellable event = null;
        if (entity instanceof EntityLiving && !(entity instanceof EntityPlayerMP)) {
            boolean isAnimal = entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal || entity instanceof EntityGolem;
            boolean isMonster = entity instanceof EntityMob || entity instanceof EntityGhast || entity instanceof EntitySlime;

            if (spawnReason != SpawnReason.CUSTOM) {
                if (isAnimal && !allowAnimals || isMonster && !allowMonsters)  {
                    entity.isDead = true;
                    return false;
                }
            }

            event = CraftEventFactory.callCreatureSpawnEvent((EntityLiving) entity, spawnReason);
        } else if (entity instanceof EntityItem) {
            event = CraftEventFactory.callItemSpawnEvent((EntityItem) entity);
        } else if (entity.getBukkitEntity() instanceof org.bukkit.entity.Projectile) {
            // Not all projectiles extend EntityProjectile, so check for Bukkit interface instead
            event = CraftEventFactory.callProjectileLaunchEvent(entity);
        }

        if (event != null && (event.isCancelled() || entity.isDead)) {
            entity.isDead = true;
            return false;
        }
        // CraftBukkit end

        if (!flag && !this.isChunkLoaded(i, j)) {
            entity.isDead = true; // CraftBukkit
            return false;
        } else {
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityhuman = (EntityPlayer) entity;

                this.players.add(entityhuman);
                this.everyoneSleeping();
            }

            this.getChunkAt(i, j).a(entity);
            this.entityList.add(entity);
            this.a(entity);
            return true;
        }
    }

    protected void a(Entity entity) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.a(entity);
        }

        entity.valid = true; // CraftBukkit
    }

    protected void b(Entity entity) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.b(entity);
        }

        entity.valid = false; // CraftBukkit
    }

    public void kill(Entity entity) {
        if (entity.riddenByEntity != null) {
            entity.riddenByEntity.mount((Entity) null);
        }

        if (entity.ridingEntity != null) {
            entity.mount((Entity) null);
        }

        entity.setDead();
        if (entity instanceof EntityPlayer) {
            this.players.remove(entity);
            this.everyoneSleeping();
        }
    }

    public void removeEntity(Entity entity) {
        entity.setDead();
        if (entity instanceof EntityPlayer) {
            this.players.remove(entity);
            this.everyoneSleeping();
        }

        int i = entity.ah;
        int j = entity.aj;

        if (entity.ag && this.isChunkLoaded(i, j)) {
            this.getChunkAt(i, j).b(entity);
        }

        this.entityList.remove(entity);
        this.b(entity);
    }

    public void addIWorldAccess(IWorldAccess iworldaccess) {
        this.x.add(iworldaccess);
    }

    public List getCubes(Entity entity, AxisAlignedBB axisalignedbb) {
        this.d.clear();
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.isLoaded(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        Block block = Block.blocksList[this.getBlockId(k1, i2, l1)];

                        if (block != null) {
                            block.a(this, k1, i2, l1, axisalignedbb, this.d, entity);
                        }
                    }
                }
            }
        }

        double d0 = 0.25D;
        List list = this.getEntities(entity, axisalignedbb.grow(d0, d0, d0));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity1 = (Entity) iterator.next();
            AxisAlignedBB axisalignedbb1 = entity1.E();

            if (axisalignedbb1 != null && axisalignedbb1.a(axisalignedbb)) {
                this.d.add(axisalignedbb1);
            }

            axisalignedbb1 = entity.g(entity1);
            if (axisalignedbb1 != null && axisalignedbb1.a(axisalignedbb)) {
                this.d.add(axisalignedbb1);
            }
        }

        return this.d;
    }

    public List a(AxisAlignedBB axisalignedbb) {
        this.d.clear();
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.isLoaded(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        Block block = Block.blocksList[this.getBlockId(k1, i2, l1)];

                        if (block != null) {
                            block.a(this, k1, i2, l1, axisalignedbb, this.d, (Entity) null);
                        }
                    }
                }
            }
        }

        return this.d;
    }

    public int a(float f) {
        float f1 = this.c(f);
        float f2 = 1.0F - (MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F);

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        f2 = 1.0F - f2;
        f2 = (float) ((double) f2 * (1.0D - (double) (this.j(f) * 5.0F) / 16.0D));
        f2 = (float) ((double) f2 * (1.0D - (double) (this.i(f) * 5.0F) / 16.0D));
        f2 = 1.0F - f2;
        return (int) (f2 * 11.0F);
    }

    public float c(float f) {
        return this.worldProvider.a(this.worldData.getTime(), f);
    }

    public int g(int i, int j) {
        return this.getChunkAtWorldCoords(i, j).d(i & 15, j & 15);
    }

    public int h(int i, int j) {
        Chunk chunk = this.getChunkAtWorldCoords(i, j);
        int k = chunk.h() + 15;

        i &= 15;

        for (j &= 15; k > 0; --k) {
            int l = chunk.getBlockID(i, k, j);

            if (l != 0 && Block.blocksList[l].blockMaterial.isSolid() && Block.blocksList[l].blockMaterial != Material.LEAVES) {
                return k + 1;
            }
        }

        return -1;
    }

    public void a(int i, int j, int k, int l, int i1) {}

    public void b(int i, int j, int k, int l, int i1) {}

    public void tickEntities() {
        // this.methodProfiler.a("entities"); // CraftBukkit - not in production code
        // this.methodProfiler.a("global"); // CraftBukkit - not in production code

        int i;
        Entity entity;

        for (i = 0; i < this.j.size(); ++i) {
            entity = (Entity) this.j.get(i);
            // CraftBukkit start - fixed an NPE, don't process entities in chunks queued for unload
            if (entity == null) {
                continue;
            }

            ChunkProviderServer chunkProviderServer = ((net.minecraft.src.WorldServer) entity.worldObj).chunkProviderServer;
            if (chunkProviderServer.unloadQueue.contains(MathHelper.floor(entity.posX) >> 4, MathHelper.floor(entity.posZ) >> 4)) {
                continue;
            }
            // CraftBukkit end
            entity.h_();
            if (entity.isDead) {
                this.j.remove(i--);
            }
        }

        // this.methodProfiler.c("remove"); // CraftBukkit - not in production code
        this.entityList.removeAll(this.g);
        Iterator iterator = this.g.iterator();

        int j;
        int k;

        while (iterator.hasNext()) {
            entity = (Entity) iterator.next();
            j = entity.ah;
            k = entity.aj;
            if (entity.ag && this.isChunkLoaded(j, k)) {
                this.getChunkAt(j, k).b(entity);
            }
        }

        iterator = this.g.iterator();

        while (iterator.hasNext()) {
            entity = (Entity) iterator.next();
            this.b(entity);
        }

        this.g.clear();
        // this.methodProfiler.c("regular"); // CraftBukkit - not in production code

        for (i = 0; i < this.entityList.size(); ++i) {
            entity = (Entity) this.entityList.get(i);

            // CraftBukkit start - don't tick entities in chunks queued for unload
            ChunkProviderServer chunkProviderServer = ((net.minecraft.src.WorldServer) entity.worldObj).chunkProviderServer;
            if (chunkProviderServer.unloadQueue.contains(MathHelper.floor(entity.posX) >> 4, MathHelper.floor(entity.posZ) >> 4)) {
                continue;
            }
            // CraftBukkit end

            if (entity.ridingEntity != null) {
                if (!entity.ridingEntity.isDead && entity.ridingEntity.riddenByEntity == entity) {
                    continue;
                }

                entity.ridingEntity.riddenByEntity = null;
                entity.ridingEntity = null;
            }

            // this.methodProfiler.a("tick"); // CraftBukkit - not in production code
            if (!entity.isDead) {
                this.playerJoinedWorld(entity);
            }

            // this.methodProfiler.b(); // CraftBukkit - not in production code
            // this.methodProfiler.a("remove"); // CraftBukkit - not in production code
            if (entity.isDead) {
                j = entity.ah;
                k = entity.aj;
                if (entity.ag && this.isChunkLoaded(j, k)) {
                    this.getChunkAt(j, k).b(entity);
                }

                this.entityList.remove(i--);
                this.b(entity);
            }

            // this.methodProfiler.b(); // CraftBukkit - not in production code
        }

        // this.methodProfiler.c("tileEntities"); // CraftBukkit - not in production code
        this.L = true;
        iterator = this.tileEntityList.iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            // CraftBukkit start - don't tick entities in chunks queued for unload
            ChunkProviderServer chunkProviderServer = ((net.minecraft.src.WorldServer) tileentity.world).chunkProviderServer;
            if (chunkProviderServer.unloadQueue.contains(tileentity.x >> 4, tileentity.z >> 4)) {
                continue;
            }
            // CraftBukkit end

            if (!tileentity.p() && tileentity.m() && this.isLoaded(tileentity.x, tileentity.y, tileentity.z)) {
                tileentity.g();
            }

            if (tileentity.p()) {
                iterator.remove();
                if (this.isChunkLoaded(tileentity.x >> 4, tileentity.z >> 4)) {
                    Chunk chunk = this.getChunkAt(tileentity.x >> 4, tileentity.z >> 4);

                    if (chunk != null) {
                        chunk.f(tileentity.x & 15, tileentity.y, tileentity.z & 15);
                    }
                }
            }
        }

        this.L = false;
        if (!this.b.isEmpty()) {
            this.tileEntityList.removeAll(this.b);
            this.b.clear();
        }

        // this.methodProfiler.c("pendingTileEntities"); // CraftBukkit - not in production code
        if (!this.a.isEmpty()) {
            Iterator iterator1 = this.a.iterator();

            while (iterator1.hasNext()) {
                TileEntity tileentity1 = (TileEntity) iterator1.next();

                if (!tileentity1.p()) {
                    /* CraftBukkit start - order matters, moved down
                    if (!this.tileEntityList.contains(tileentity1)) {
                        this.tileEntityList.add(tileentity1);
                    }
                    // CraftBukkit end */

                    if (this.isChunkLoaded(tileentity1.x >> 4, tileentity1.z >> 4)) {
                        Chunk chunk1 = this.getChunkAt(tileentity1.x >> 4, tileentity1.z >> 4);

                        if (chunk1 != null) {
                            chunk1.a(tileentity1.x & 15, tileentity1.y, tileentity1.z & 15, tileentity1);
                            // CraftBukkit start - moved down from above
                            if (!this.tileEntityList.contains(tileentity1)) {
                                this.tileEntityList.add(tileentity1);
                            }
                            // CraftBukkit end
                        }
                    }

                    this.markBlockNeedsUpdate(tileentity1.x, tileentity1.y, tileentity1.z);
                }
            }

            this.a.clear();
        }

        // this.methodProfiler.b(); // CraftBukkit - not in production code
        // this.methodProfiler.b(); // CraftBukkit - not in production code
    }

    public void a(Collection collection) {
        if (this.L) {
            this.a.addAll(collection);
        } else {
            this.tileEntityList.addAll(collection);
        }
    }

    public void playerJoinedWorld(Entity entity) {
        this.entityJoinedWorld(entity, true);
    }

    public void entityJoinedWorld(Entity entity, boolean flag) {
        int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(entity.posZ);
        byte b0 = 32;

        if (!flag || this.c(i - b0, 0, j - b0, i + b0, 0, j + b0)) {
            entity.S = entity.posX;
            entity.T = entity.posY;
            entity.U = entity.posZ;
            entity.lastYaw = entity.rotationYaw;
            entity.lastPitch = entity.rotationPitch;
            if (flag && entity.ag) {
                if (entity.ridingEntity != null) {
                    entity.U();
                } else {
                    entity.h_();
                }
            }

            // this.methodProfiler.a("chunkCheck"); // CraftBukkit - not in production code
            if (Double.isNaN(entity.posX) || Double.isInfinite(entity.posX)) {
                entity.posX = entity.S;
            }

            if (Double.isNaN(entity.posY) || Double.isInfinite(entity.posY)) {
                entity.posY = entity.T;
            }

            if (Double.isNaN(entity.posZ) || Double.isInfinite(entity.posZ)) {
                entity.posZ = entity.U;
            }

            if (Double.isNaN((double) entity.rotationPitch) || Double.isInfinite((double) entity.rotationPitch)) {
                entity.rotationPitch = entity.lastPitch;
            }

            if (Double.isNaN((double) entity.rotationYaw) || Double.isInfinite((double) entity.rotationYaw)) {
                entity.rotationYaw = entity.lastYaw;
            }

            int k = MathHelper.floor(entity.posX / 16.0D);
            int l = MathHelper.floor(entity.posY / 16.0D);
            int i1 = MathHelper.floor(entity.posZ / 16.0D);

            if (!entity.ag || entity.ah != k || entity.ai != l || entity.aj != i1) {
                if (entity.ag && this.isChunkLoaded(entity.ah, entity.aj)) {
                    this.getChunkAt(entity.ah, entity.aj).a(entity, entity.ai);
                }

                if (this.isChunkLoaded(k, i1)) {
                    entity.ag = true;
                    this.getChunkAt(k, i1).a(entity);
                } else {
                    entity.ag = false;
                }
            }

            // this.methodProfiler.b(); // CraftBukkit - not in production code
            if (flag && entity.ag && entity.riddenByEntity != null) {
                if (!entity.riddenByEntity.isDead && entity.riddenByEntity.ridingEntity == entity) {
                    this.playerJoinedWorld(entity.riddenByEntity);
                } else {
                    entity.riddenByEntity.ridingEntity = null;
                    entity.riddenByEntity = null;
                }
            }
        }
    }

    public boolean b(AxisAlignedBB axisalignedbb) {
        return this.a(axisalignedbb, (Entity) null);
    }

    public boolean a(AxisAlignedBB axisalignedbb, Entity entity) {
        List list = this.getEntities((Entity) null, axisalignedbb);
        Iterator iterator = list.iterator();

        Entity entity1;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            entity1 = (Entity) iterator.next();
        } while (entity1.isDead || !entity1.m || entity1 == entity);

        return false;
    }

    public boolean c(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        if (axisalignedbb.a < 0.0D) {
            --i;
        }

        if (axisalignedbb.b < 0.0D) {
            --k;
        }

        if (axisalignedbb.c < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = Block.blocksList[this.getBlockId(k1, l1, i2)];

                    if (block != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean containsLiquid(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        if (axisalignedbb.a < 0.0D) {
            --i;
        }

        if (axisalignedbb.b < 0.0D) {
            --k;
        }

        if (axisalignedbb.c < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = Block.blocksList[this.getBlockId(k1, l1, i2)];

                    if (block != null && block.blockMaterial.isLiquid()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean e(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        if (this.c(i, k, i1, j, l, j1)) {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        int j2 = this.getBlockId(k1, l1, i2);

                        if (j2 == Block.FIRE.blockID || j2 == Block.LAVA.blockID || j2 == Block.STATIONARY_LAVA.blockID) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean a(AxisAlignedBB axisalignedbb, Material material, Entity entity) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        if (!this.c(i, k, i1, j, l, j1)) {
            return false;
        } else {
            boolean flag = false;
            Vec3 vec3d = Vec3.a().create(0.0D, 0.0D, 0.0D);

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        Block block = Block.blocksList[this.getBlockId(k1, l1, i2)];

                        if (block != null && block.blockMaterial == material) {
                            double d0 = (double) ((float) (l1 + 1) - BlockFluids.d(this.getData(k1, l1, i2)));

                            if ((double) l >= d0) {
                                flag = true;
                                block.a(this, k1, l1, i2, entity, vec3d);
                            }
                        }
                    }
                }
            }

            if (vec3d.c() > 0.0D) {
                vec3d = vec3d.b();
                double d1 = 0.014D;

                entity.motionX += vec3d.a * d1;
                entity.motionY += vec3d.b * d1;
                entity.motionZ += vec3d.c * d1;
            }
            Vec3.a().release(vec3d); // CraftBukkit - pop it - we're done

            return flag;
        }
    }

    public boolean a(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = Block.blocksList[this.getBlockId(k1, l1, i2)];

                    if (block != null && block.blockMaterial == material) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean b(AxisAlignedBB axisalignedbb, Material material) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    Block block = Block.blocksList[this.getBlockId(k1, l1, i2)];

                    if (block != null && block.blockMaterial == material) {
                        int j2 = this.getData(k1, l1, i2);
                        double d0 = (double) (l1 + 1);

                        if (j2 < 8) {
                            d0 = (double) (l1 + 1) - (double) j2 / 8.0D;
                        }

                        if (d0 >= axisalignedbb.b) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public Explosion explode(Entity entity, double d0, double d1, double d2, float f) {
        return this.createExplosion(entity, d0, d1, d2, f, false);
    }

    public Explosion createExplosion(Entity entity, double d0, double d1, double d2, float f, boolean flag) {
        Explosion explosion = new Explosion(this, entity, d0, d1, d2, f);

        explosion.a = flag;
        explosion.a();
        explosion.a(true);
        return explosion;
    }

    public float a(Vec3 vec3d, AxisAlignedBB axisalignedbb) {
        double d0 = 1.0D / ((axisalignedbb.d - axisalignedbb.a) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.e - axisalignedbb.b) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.f - axisalignedbb.c) * 2.0D + 1.0D);
        int i = 0;
        int j = 0;

        Vec3 vec3d2 = Vec3.a().create(0, 0, 0); // CraftBukkit
        for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
            for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                    double d3 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double) f;
                    double d4 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double) f1;
                    double d5 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double) f2;

                    if (this.a(vec3d2.b(d3, d4, d5), vec3d) == null) { // CraftBukkit
                        ++i;
                    }

                    ++j;
                }
            }
        }
        Vec3.a().release(vec3d2); // CraftBukkit

        return (float) i / (float) j;
    }

    public boolean douseFire(EntityPlayer entityhuman, int i, int j, int k, int l) {
        if (l == 0) {
            --j;
        }

        if (l == 1) {
            ++j;
        }

        if (l == 2) {
            --k;
        }

        if (l == 3) {
            ++k;
        }

        if (l == 4) {
            --i;
        }

        if (l == 5) {
            ++i;
        }

        if (this.getBlockId(i, j, k) == Block.FIRE.blockID) {
            this.a(entityhuman, 1004, i, j, k, 0);
            this.setBlockWithNotify(i, j, k, 0);
            return true;
        } else {
            return false;
        }
    }

    public TileEntity getTileEntity(int i, int j, int k) {
        if (j >= 256) {
            return null;
        } else {
            Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

            if (chunk == null) {
                return null;
            } else {
                TileEntity tileentity = chunk.e(i & 15, j, k & 15);

                if (tileentity == null) {
                    Iterator iterator = this.a.iterator();

                    while (iterator.hasNext()) {
                        TileEntity tileentity1 = (TileEntity) iterator.next();

                        if (!tileentity1.p() && tileentity1.x == i && tileentity1.y == j && tileentity1.z == k) {
                            tileentity = tileentity1;
                            break;
                        }
                    }
                }

                return tileentity;
            }
        }
    }

    public void setTileEntity(int i, int j, int k, TileEntity tileentity) {
        if (tileentity != null && !tileentity.p()) {
            if (this.L) {
                tileentity.x = i;
                tileentity.y = j;
                tileentity.z = k;
                this.a.add(tileentity);
            } else {
                this.tileEntityList.add(tileentity);
                Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

                if (chunk != null) {
                    chunk.a(i & 15, j, k & 15, tileentity);
                }
            }
        }
    }

    public void q(int i, int j, int k) {
        TileEntity tileentity = this.getTileEntity(i, j, k);

        if (tileentity != null && this.L) {
            tileentity.j();
            this.a.remove(tileentity);
        } else {
            if (tileentity != null) {
                this.a.remove(tileentity);
                this.tileEntityList.remove(tileentity);
            }

            Chunk chunk = this.getChunkAt(i >> 4, k >> 4);

            if (chunk != null) {
                chunk.f(i & 15, j, k & 15);
            }
        }
    }

    public void a(TileEntity tileentity) {
        this.b.add(tileentity);
    }

    public boolean r(int i, int j, int k) {
        Block block = Block.blocksList[this.getBlockId(i, j, k)];

        return block == null ? false : block.d();
    }

    public boolean s(int i, int j, int k) {
        return Block.i(this.getBlockId(i, j, k));
    }

    public boolean t(int i, int j, int k) {
        Block block = Block.blocksList[this.getBlockId(i, j, k)];

        return block == null ? false : (block.blockMaterial.k() && block.c() ? true : (block instanceof BlockStairs ? (this.getData(i, j, k) & 4) == 4 : (block instanceof BlockStepAbstract ? (this.getData(i, j, k) & 8) == 8 : false)));
    }

    public boolean b(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            Chunk chunk = this.chunkProvider.getOrCreateChunk(i >> 4, k >> 4);

            if (chunk != null && !chunk.isEmpty()) {
                Block block = Block.blocksList[this.getBlockId(i, j, k)];

                return block == null ? false : block.blockMaterial.k() && block.c();
            } else {
                return flag;
            }
        } else {
            return flag;
        }
    }

    public void v() {
        int i = this.a(1.0F);

        if (i != this.k) {
            this.k = i;
        }
    }

    public void setSpawnFlags(boolean flag, boolean flag1) {
        this.allowMonsters = flag;
        this.allowAnimals = flag1;
    }

    public void doTick() {
        this.m();
    }

    private void a() {
        if (this.worldData.hasStorm()) {
            this.o = 1.0F;
            if (this.worldData.isThundering()) {
                this.q = 1.0F;
            }
        }
    }

    protected void m() {
        if (!this.worldProvider.e) {
            if (this.r > 0) {
                --this.r;
            }

            int i = this.worldData.getThunderDuration();

            if (i <= 0) {
                if (this.worldData.isThundering()) {
                    this.worldData.setThunderDuration(this.rand.nextInt(12000) + 3600);
                } else {
                    this.worldData.setThunderDuration(this.rand.nextInt(168000) + 12000);
                }
            } else {
                --i;
                this.worldData.setThunderDuration(i);
                if (i <= 0) {
                    // CraftBukkit start
                    ThunderChangeEvent thunder = new ThunderChangeEvent(this.getWorld(), !this.worldData.isThundering());
                    this.getServer().getPluginManager().callEvent(thunder);
                    if (!thunder.isCancelled()) {
                        this.worldData.setThundering(!this.worldData.isThundering());
                    }
                    // CraftBukkit end
                }
            }

            int j = this.worldData.getWeatherDuration();

            if (j <= 0) {
                if (this.worldData.hasStorm()) {
                    this.worldData.setWeatherDuration(this.rand.nextInt(12000) + 12000);
                } else {
                    this.worldData.setWeatherDuration(this.rand.nextInt(168000) + 12000);
                }
            } else {
                --j;
                this.worldData.setWeatherDuration(j);
                if (j <= 0) {
                    // CraftBukkit start
                    WeatherChangeEvent weather = new WeatherChangeEvent(this.getWorld(), !this.worldData.hasStorm());
                    this.getServer().getPluginManager().callEvent(weather);

                    if (!weather.isCancelled()) {
                        this.worldData.setStorm(!this.worldData.hasStorm());
                    }
                    // CraftBukkit end
                }
            }

            this.n = this.o;
            if (this.worldData.hasStorm()) {
                this.o = (float) ((double) this.o + 0.01D);
            } else {
                this.o = (float) ((double) this.o - 0.01D);
            }

            if (this.o < 0.0F) {
                this.o = 0.0F;
            }

            if (this.o > 1.0F) {
                this.o = 1.0F;
            }

            this.p = this.q;
            if (this.worldData.isThundering()) {
                this.q = (float) ((double) this.q + 0.01D);
            } else {
                this.q = (float) ((double) this.q - 0.01D);
            }

            if (this.q < 0.0F) {
                this.q = 0.0F;
            }

            if (this.q > 1.0F) {
                this.q = 1.0F;
            }
        }
    }

    public void w() {
        this.worldData.setWeatherDuration(1);
    }

    protected void x() {
        // this.chunkTickList.clear(); // CraftBukkit - removed
        // this.methodProfiler.a("buildList"); // CraftBukkit - not in production code

        int i;
        EntityPlayer entityhuman;
        int j;
        int k;

        for (i = 0; i < this.players.size(); ++i) {
            entityhuman = (EntityPlayer) this.players.get(i);
            j = MathHelper.floor(entityhuman.posX / 16.0D);
            k = MathHelper.floor(entityhuman.posZ / 16.0D);
            byte b0 = 7;

            for (int l = -b0; l <= b0; ++l) {
                for (int i1 = -b0; i1 <= b0; ++i1) {
                    // CraftBukkit start - don't tick chunks queued for unload
                    ChunkProviderServer chunkProviderServer = ((net.minecraft.src.WorldServer) entityhuman.worldObj).chunkProviderServer;
                    if (chunkProviderServer.unloadQueue.contains(l + j, i1 + k)) {
                        continue;
                    }
                    // CraftBukkit end

                    this.chunkTickList.add(org.bukkit.craftbukkit.util.LongHash.toLong(l + j, i1 + k)); // CraftBukkit
                }
            }
        }

        // this.methodProfiler.b(); // CraftBukkit - not in production code
        if (this.M > 0) {
            --this.M;
        }

        // this.methodProfiler.a("playerCheckLight"); // CraftBukkit - not in production code
        if (!this.players.isEmpty()) {
            i = this.rand.nextInt(this.players.size());
            entityhuman = (EntityPlayer) this.players.get(i);
            j = MathHelper.floor(entityhuman.posX) + this.rand.nextInt(11) - 5;
            k = MathHelper.floor(entityhuman.posY) + this.rand.nextInt(11) - 5;
            int j1 = MathHelper.floor(entityhuman.posZ) + this.rand.nextInt(11) - 5;

            this.x(j, k, j1);
        }

        // this.methodProfiler.b(); // CraftBukkit - not in production code
    }

    protected void a(int i, int j, Chunk chunk) {
        // this.methodProfiler.c("moodSound"); // CraftBukkit - not in production code
        if (this.M == 0) {
            this.l = this.l * 3 + 1013904223;
            int k = this.l >> 2;
            int l = k & 15;
            int i1 = k >> 8 & 15;
            int j1 = k >> 16 & 255; // CraftBukkit - 127 -> 255
            int k1 = chunk.getBlockID(l, j1, i1);

            l += i;
            i1 += j;
            if (k1 == 0 && this.k(l, j1, i1) <= this.rand.nextInt(8) && this.b(EnumSkyBlock.Sky, l, j1, i1) <= 0) {
                EntityPlayer entityhuman = this.findNearbyPlayer((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D, 8.0D);

                if (entityhuman != null && entityhuman.e((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D) > 4.0D) {
                    this.makeSound((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
                    this.M = this.rand.nextInt(12000) + 6000;
                }
            }
        }

        // this.methodProfiler.c("checkLight"); // CraftBukkit - not in production code
        chunk.o();
    }

    protected void g() {
        this.x();
    }

    public boolean u(int i, int j, int k) {
        return this.c(i, j, k, false);
    }

    public boolean v(int i, int j, int k) {
        return this.c(i, j, k, true);
    }

    public boolean c(int i, int j, int k, boolean flag) {
        BiomeGenBase biomebase = this.getBiome(i, k);
        float f = biomebase.j();

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < 256 && this.b(EnumSkyBlock.Block, i, j, k) < 10) {
                int l = this.getBlockId(i, j, k);

                if ((l == Block.STATIONARY_WATER.blockID || l == Block.WATER.blockID) && this.getData(i, j, k) == 0) {
                    if (!flag) {
                        return true;
                    }

                    boolean flag1 = true;

                    if (flag1 && this.getMaterial(i - 1, j, k) != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getMaterial(i + 1, j, k) != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getMaterial(i, j, k - 1) != Material.WATER) {
                        flag1 = false;
                    }

                    if (flag1 && this.getMaterial(i, j, k + 1) != Material.WATER) {
                        flag1 = false;
                    }

                    if (!flag1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean w(int i, int j, int k) {
        BiomeGenBase biomebase = this.getBiome(i, k);
        float f = biomebase.j();

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < 256 && this.b(EnumSkyBlock.Block, i, j, k) < 10) {
                int l = this.getBlockId(i, j - 1, k);
                int i1 = this.getBlockId(i, j, k);

                if (i1 == 0 && Block.SNOW.canPlace(this, i, j, k) && l != 0 && l != Block.ICE.blockID && Block.blocksList[l].blockMaterial.isSolid()) {
                    return true;
                }
            }

            return false;
        }
    }

    public void x(int i, int j, int k) {
        if (!this.worldProvider.e) {
            this.c(EnumSkyBlock.Sky, i, j, k);
        }

        this.c(EnumSkyBlock.Block, i, j, k);
    }

    private int a(int i, int j, int k, int l, int i1, int j1) {
        int k1 = 0;

        if (this.j(j, k, l)) {
            k1 = 15;
        } else {
            if (j1 == 0) {
                j1 = 1;
            }

            int l1 = this.b(EnumSkyBlock.Sky, j - 1, k, l) - j1;
            int i2 = this.b(EnumSkyBlock.Sky, j + 1, k, l) - j1;
            int j2 = this.b(EnumSkyBlock.Sky, j, k - 1, l) - j1;
            int k2 = this.b(EnumSkyBlock.Sky, j, k + 1, l) - j1;
            int l2 = this.b(EnumSkyBlock.Sky, j, k, l - 1) - j1;
            int i3 = this.b(EnumSkyBlock.Sky, j, k, l + 1) - j1;

            if (l1 > k1) {
                k1 = l1;
            }

            if (i2 > k1) {
                k1 = i2;
            }

            if (j2 > k1) {
                k1 = j2;
            }

            if (k2 > k1) {
                k1 = k2;
            }

            if (l2 > k1) {
                k1 = l2;
            }

            if (i3 > k1) {
                k1 = i3;
            }
        }

        return k1;
    }

    private int f(int i, int j, int k, int l, int i1, int j1) {
        int k1 = Block.lightEmission[i1];
        int l1 = this.b(EnumSkyBlock.Block, j - 1, k, l) - j1;
        int i2 = this.b(EnumSkyBlock.Block, j + 1, k, l) - j1;
        int j2 = this.b(EnumSkyBlock.Block, j, k - 1, l) - j1;
        int k2 = this.b(EnumSkyBlock.Block, j, k + 1, l) - j1;
        int l2 = this.b(EnumSkyBlock.Block, j, k, l - 1) - j1;
        int i3 = this.b(EnumSkyBlock.Block, j, k, l + 1) - j1;

        if (l1 > k1) {
            k1 = l1;
        }

        if (i2 > k1) {
            k1 = i2;
        }

        if (j2 > k1) {
            k1 = j2;
        }

        if (k2 > k1) {
            k1 = k2;
        }

        if (l2 > k1) {
            k1 = l2;
        }

        if (i3 > k1) {
            k1 = i3;
        }

        return k1;
    }

    public void c(EnumSkyBlock enumskyblock, int i, int j, int k) {
        if (this.areChunksLoaded(i, j, k, 17)) {
            int l = 0;
            int i1 = 0;

            // this.methodProfiler.a("getSavedLightValue"); // CraftBukkit - not in production code
            int j1 = this.b(enumskyblock, i, j, k);
            boolean flag = false;
            int k1 = this.getBlockId(i, j, k);
            int l1 = this.b(i, j, k);

            if (l1 == 0) {
                l1 = 1;
            }

            boolean flag1 = false;
            int i2;

            if (enumskyblock == EnumSkyBlock.Sky) {
                i2 = this.a(j1, i, j, k, k1, l1);
            } else {
                i2 = this.f(j1, i, j, k, k1, l1);
            }

            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;
            int l3;
            int i4;

            if (i2 > j1) {
                this.J[i1++] = 133152;
            } else if (i2 < j1) {
                if (enumskyblock != EnumSkyBlock.Block) {
                    ;
                }

                this.J[i1++] = 133152 + (j1 << 18);

                while (l < i1) {
                    k1 = this.J[l++];
                    l1 = (k1 & 63) - 32 + i;
                    i2 = (k1 >> 6 & 63) - 32 + j;
                    j2 = (k1 >> 12 & 63) - 32 + k;
                    k2 = k1 >> 18 & 15;
                    l2 = this.b(enumskyblock, l1, i2, j2);
                    if (l2 == k2) {
                        this.b(enumskyblock, l1, i2, j2, 0);
                        if (k2 > 0) {
                            i3 = l1 - i;
                            k3 = i2 - j;
                            j3 = j2 - k;
                            if (i3 < 0) {
                                i3 = -i3;
                            }

                            if (k3 < 0) {
                                k3 = -k3;
                            }

                            if (j3 < 0) {
                                j3 = -j3;
                            }

                            if (i3 + k3 + j3 < 17) {
                                for (i4 = 0; i4 < 6; ++i4) {
                                    l3 = i4 % 2 * 2 - 1;
                                    int j4 = l1 + i4 / 2 % 3 / 2 * l3;
                                    int k4 = i2 + (i4 / 2 + 1) % 3 / 2 * l3;
                                    int l4 = j2 + (i4 / 2 + 2) % 3 / 2 * l3;

                                    l2 = this.b(enumskyblock, j4, k4, l4);
                                    int i5 = Block.lightBlock[this.getBlockId(j4, k4, l4)];

                                    if (i5 == 0) {
                                        i5 = 1;
                                    }

                                    if (l2 == k2 - i5 && i1 < this.J.length) {
                                        this.J[i1++] = j4 - i + 32 + (k4 - j + 32 << 6) + (l4 - k + 32 << 12) + (k2 - i5 << 18);
                                    }
                                }
                            }
                        }
                    }
                }

                l = 0;
            }

            // this.methodProfiler.b(); // CraftBukkit - not in production code
            // this.methodProfiler.a("tcp < tcc"); // CraftBukkit - not in production code

            while (l < i1) {
                k1 = this.J[l++];
                l1 = (k1 & 63) - 32 + i;
                i2 = (k1 >> 6 & 63) - 32 + j;
                j2 = (k1 >> 12 & 63) - 32 + k;
                k2 = this.b(enumskyblock, l1, i2, j2);
                l2 = this.getBlockId(l1, i2, j2);
                i3 = Block.lightBlock[l2];
                if (i3 == 0) {
                    i3 = 1;
                }

                boolean flag2 = false;

                if (enumskyblock == EnumSkyBlock.Sky) {
                    k3 = this.a(k2, l1, i2, j2, l2, i3);
                } else {
                    k3 = this.f(k2, l1, i2, j2, l2, i3);
                }

                if (k3 != k2) {
                    this.b(enumskyblock, l1, i2, j2, k3);
                    if (k3 > k2) {
                        j3 = l1 - i;
                        i4 = i2 - j;
                        l3 = j2 - k;
                        if (j3 < 0) {
                            j3 = -j3;
                        }

                        if (i4 < 0) {
                            i4 = -i4;
                        }

                        if (l3 < 0) {
                            l3 = -l3;
                        }

                        if (j3 + i4 + l3 < 17 && i1 < this.J.length - 6) {
                            if (this.b(enumskyblock, l1 - 1, i2, j2) < k3) {
                                this.J[i1++] = l1 - 1 - i + 32 + (i2 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, l1 + 1, i2, j2) < k3) {
                                this.J[i1++] = l1 + 1 - i + 32 + (i2 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, l1, i2 - 1, j2) < k3) {
                                this.J[i1++] = l1 - i + 32 + (i2 - 1 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, l1, i2 + 1, j2) < k3) {
                                this.J[i1++] = l1 - i + 32 + (i2 + 1 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, l1, i2, j2 - 1) < k3) {
                                this.J[i1++] = l1 - i + 32 + (i2 - j + 32 << 6) + (j2 - 1 - k + 32 << 12);
                            }

                            if (this.b(enumskyblock, l1, i2, j2 + 1) < k3) {
                                this.J[i1++] = l1 - i + 32 + (i2 - j + 32 << 6) + (j2 + 1 - k + 32 << 12);
                            }
                        }
                    }
                }
            }

            // this.methodProfiler.b(); // CraftBukkit - not in production code
        }
    }

    public boolean a(boolean flag) {
        return false;
    }

    public List a(Chunk chunk, boolean flag) {
        return null;
    }

    public List getEntities(Entity entity, AxisAlignedBB axisalignedbb) {
        this.N.clear();
        int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.isChunkLoaded(i1, j1)) {
                    this.getChunkAt(i1, j1).a(entity, axisalignedbb, this.N);
                }
            }
        }

        return this.N;
    }

    public List a(Class oclass, AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor((axisalignedbb.a - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.d + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.c - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.f + 2.0D) / 16.0D);
        ArrayList arraylist = new ArrayList();

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.isChunkLoaded(i1, j1)) {
                    this.getChunkAt(i1, j1).a(oclass, axisalignedbb, arraylist);
                }
            }
        }

        return arraylist;
    }

    public Entity a(Class oclass, AxisAlignedBB axisalignedbb, Entity entity) {
        List list = this.a(oclass, axisalignedbb);
        Entity entity1 = null;
        double d0 = Double.MAX_VALUE;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity2 = (Entity) iterator.next();

            if (entity2 != entity) {
                double d1 = entity.e(entity2);

                if (d1 <= d0) {
                    entity1 = entity2;
                    d0 = d1;
                }
            }
        }

        return entity1;
    }

    public void b(int i, int j, int k, TileEntity tileentity) {
        if (this.isLoaded(i, j, k)) {
            this.getChunkAtWorldCoords(i, k).e();
        }
    }

    public int a(Class oclass) {
        int i = 0;

        for (int j = 0; j < this.entityList.size(); ++j) {
            Entity entity = (Entity) this.entityList.get(j);

            if (oclass.isAssignableFrom(entity.getClass())) {
                ++i;
            }
        }

        return i;
    }

    public void a(List list) {
        // CraftBukkit start
        Entity entity = null;
        for (int i = 0; i < list.size(); ++i) {
            entity = (Entity) list.get(i);
            if (entity == null) {
                continue;
            }
            this.entityList.add(entity);
            // CraftBukkit end
            this.a((Entity) list.get(i));
        }
    }

    public void b(List list) {
        this.g.addAll(list);
    }

    public boolean mayPlace(int i, int j, int k, int l, boolean flag, int i1, Entity entity) {
        int j1 = this.getBlockId(j, k, l);
        Block block = Block.blocksList[j1];
        Block block1 = Block.blocksList[i];
        AxisAlignedBB axisalignedbb = block1.e(this, j, k, l);

        if (flag) {
            axisalignedbb = null;
        }

        boolean defaultReturn; // CraftBukkit - store the default action

        if (axisalignedbb != null && !this.a(axisalignedbb, entity)) {
            defaultReturn = false; // CraftBukkit
        } else {
            if (block != null && (block == Block.WATER || block == Block.STATIONARY_WATER || block == Block.LAVA || block == Block.STATIONARY_LAVA || block == Block.FIRE || block.blockMaterial.isReplaceable())) {
                block = null;
            }

            defaultReturn = i > 0 && block == null && block1.canPlace(this, j, k, l, i1);
        }

        // CraftBukkit start
        BlockCanBuildEvent event = new BlockCanBuildEvent(this.getWorld().getBlockAt(j, k, l), i, defaultReturn);
        this.getServer().getPluginManager().callEvent(event);

        return event.isBuildable();
        // CraftBukkit end
    }

    public PathEntity getPathEntityToEntity(Entity entity, Entity entity1, float f, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        // this.methodProfiler.a("pathfind"); // CraftBukkit - not in production code
        int i = MathHelper.floor(entity.posX);
        int j = MathHelper.floor(entity.posY + 1.0D);
        int k = MathHelper.floor(entity.posZ);
        int l = (int) (f + 16.0F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        ChunkCache chunkcache = new ChunkCache(this, i1, j1, k1, l1, i2, j2);
        PathEntity pathentity = (new Pathfinder(chunkcache, flag, flag1, flag2, flag3)).a(entity, entity1, f);

        // this.methodProfiler.b(); // CraftBukkit - not in production code
        return pathentity;
    }

    public PathEntity a(Entity entity, int i, int j, int k, float f, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        // this.methodProfiler.a("pathfind"); // CraftBukkit - not in production code
        int l = MathHelper.floor(entity.posX);
        int i1 = MathHelper.floor(entity.posY);
        int j1 = MathHelper.floor(entity.posZ);
        int k1 = (int) (f + 8.0F);
        int l1 = l - k1;
        int i2 = i1 - k1;
        int j2 = j1 - k1;
        int k2 = l + k1;
        int l2 = i1 + k1;
        int i3 = j1 + k1;
        ChunkCache chunkcache = new ChunkCache(this, l1, i2, j2, k2, l2, i3);
        PathEntity pathentity = (new Pathfinder(chunkcache, flag, flag1, flag2, flag3)).a(entity, i, j, k, f);

        // this.methodProfiler.b(); // CraftBukkit - not in production code
        return pathentity;
    }

    public boolean isBlockProvidingPowerTo(int i, int j, int k, int l) {
        int i1 = this.getBlockId(i, j, k);

        return i1 == 0 ? false : Block.blocksList[i1].c(this, i, j, k, l);
    }

    public boolean isBlockGettingPowered(int i, int j, int k) {
        return this.isBlockProvidingPowerTo(i, j - 1, k, 0) ? true : (this.isBlockProvidingPowerTo(i, j + 1, k, 1) ? true : (this.isBlockProvidingPowerTo(i, j, k - 1, 2) ? true : (this.isBlockProvidingPowerTo(i, j, k + 1, 3) ? true : (this.isBlockProvidingPowerTo(i - 1, j, k, 4) ? true : this.isBlockProvidingPowerTo(i + 1, j, k, 5)))));
    }

    public boolean isBlockIndirectlyProvidingPowerTo(int i, int j, int k, int l) {
        if (this.s(i, j, k)) {
            return this.isBlockGettingPowered(i, j, k);
        } else {
            int i1 = this.getBlockId(i, j, k);

            return i1 == 0 ? false : Block.blocksList[i1].a(this, i, j, k, l);
        }
    }

    public boolean isBlockIndirectlyGettingPowered(int i, int j, int k) {
        return this.isBlockIndirectlyProvidingPowerTo(i, j - 1, k, 0) ? true : (this.isBlockIndirectlyProvidingPowerTo(i, j + 1, k, 1) ? true : (this.isBlockIndirectlyProvidingPowerTo(i, j, k - 1, 2) ? true : (this.isBlockIndirectlyProvidingPowerTo(i, j, k + 1, 3) ? true : (this.isBlockIndirectlyProvidingPowerTo(i - 1, j, k, 4) ? true : this.isBlockIndirectlyProvidingPowerTo(i + 1, j, k, 5)))));
    }

    public EntityPlayer findNearbyPlayer(Entity entity, double d0) {
        return this.findNearbyPlayer(entity.posX, entity.posY, entity.posZ, d0);
    }

    public EntityPlayer findNearbyPlayer(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        EntityPlayer entityhuman = null;

        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityhuman1 = (EntityPlayer) this.players.get(i);
            // CraftBukkit start - fixed an NPE
            if (entityhuman1 == null || entityhuman1.isDead) {
                continue;
            }
            // CraftBukkit end
            double d5 = entityhuman1.e(d0, d1, d2);

            if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                d4 = d5;
                entityhuman = entityhuman1;
            }
        }

        return entityhuman;
    }

    public EntityPlayer findNearbyVulnerablePlayer(Entity entity, double d0) {
        return this.findNearbyVulnerablePlayer(entity.posX, entity.posY, entity.posZ, d0);
    }

    public EntityPlayer findNearbyVulnerablePlayer(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        EntityPlayer entityhuman = null;

        for (int i = 0; i < this.players.size(); ++i) {
            EntityPlayer entityhuman1 = (EntityPlayer) this.players.get(i);
            // CraftBukkit start - fixed an NPE
            if (entityhuman1 == null || entityhuman1.isDead) {
                continue;
            }
            // CraftBukkit end

            if (!entityhuman1.capabilities.isInvulnerable) {
                double d5 = entityhuman1.e(d0, d1, d2);

                if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    entityhuman = entityhuman1;
                }
            }
        }

        return entityhuman;
    }

    public EntityPlayer a(String s) {
        for (int i = 0; i < this.players.size(); ++i) {
            if (s.equals(((EntityPlayer) this.players.get(i)).username)) {
                return (EntityPlayer) this.players.get(i);
            }
        }

        return null;
    }

    public void B() throws ExceptionWorldConflict { // CraftBukkit - added throws
        this.dataManager.checkSession();
    }

    public void setTime(long i) {
        this.worldData.b(i);
    }

    public long getSeed() {
        return this.worldData.getSeed();
    }

    public long getTime() {
        return this.worldData.getTime();
    }

    public ChunkCoordinates getSpawn() {
        return new ChunkCoordinates(this.worldData.c(), this.worldData.d(), this.worldData.e());
    }

    public boolean a(EntityPlayer entityhuman, int i, int j, int k) {
        return true;
    }

    public void setEntityState(Entity entity, byte b0) {}

    public IChunkProvider F() {
        return this.chunkProvider;
    }

    public void addBlockEvent(int i, int j, int k, int l, int i1, int j1) {
        if (l > 0) {
            Block.blocksList[l].b(this, i, j, k, i1, j1);
        }
    }

    public ISaveHandler getDataManager() {
        return this.dataManager;
    }

    public WorldData getWorldData() {
        return this.worldData;
    }

    public void everyoneSleeping() {}

     // CraftBukkit start
    // Calls the method that checks to see if players are sleeping
    // Called by CraftPlayer.setPermanentSleeping()
    public void checkSleepStatus() {
        if (!this.isStatic) {
            this.everyoneSleeping();
        }
    }
    // CraftBukkit end

    public float i(float f) {
        return (this.p + (this.q - this.p) * f) * this.j(f);
    }

    public float j(float f) {
        return this.n + (this.o - this.n) * f;
    }

    public boolean I() {
        return (double) this.i(1.0F) > 0.9D;
    }

    public boolean J() {
        return (double) this.j(1.0F) > 0.2D;
    }

    public boolean B(int i, int j, int k) {
        if (!this.J()) {
            return false;
        } else if (!this.j(i, j, k)) {
            return false;
        } else if (this.g(i, k) > j) {
            return false;
        } else {
            BiomeGenBase biomebase = this.getBiome(i, k);

            return biomebase.c() ? false : biomebase.d();
        }
    }

    public boolean C(int i, int j, int k) {
        BiomeGenBase biomebase = this.getBiome(i, k);

        return biomebase.e();
    }

    public void a(String s, WorldMapBase worldmapbase) {
        this.worldMaps.a(s, worldmapbase);
    }

    public WorldMapBase a(Class oclass, String s) {
        return this.worldMaps.get(oclass, s);
    }

    public int b(String s) {
        return this.worldMaps.a(s);
    }

    public void triggerEffect(int i, int j, int k, int l, int i1) {
        this.a((EntityPlayer) null, i, j, k, l, i1);
    }

    public void a(EntityPlayer entityhuman, int i, int j, int k, int l, int i1) {
        for (int j1 = 0; j1 < this.x.size(); ++j1) {
            ((IWorldAccess) this.x.get(j1)).a(entityhuman, i, j, k, l, i1);
        }
    }

    public int getHeight() {
        return 256;
    }

    public int L() {
        return this.worldProvider.e ? 128 : 256;
    }

    public Random D(int i, int j, int k) {
        long l = (long) i * 341873128712L + (long) j * 132897987541L + this.getWorldData().getSeed() + (long) k;

        this.rand.setSeed(l);
        return this.rand;
    }

    public boolean updateLights() {
        return false;
    }

    public ChunkPosition b(String s, int i, int j, int k) {
        return this.F().findNearestMapFeature(this, s, i, j, k);
    }

    public CrashReport a(CrashReport crashreport) {
        crashreport.a("World " + this.worldData.getName() + " Entities", (Callable) (new CrashReportEntities(this)));
        crashreport.a("World " + this.worldData.getName() + " Players", (Callable) (new CrashReportPlayers(this)));
        crashreport.a("World " + this.worldData.getName() + " Chunk Stats", (Callable) (new CrashReportChunkStats(this)));
        return crashreport;
    }

    public void f(int i, int j, int k, int l, int i1) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            IWorldAccess iworldaccess = (IWorldAccess) iterator.next();

            iworldaccess.a(i, j, k, l, i1);
        }
    }
}
