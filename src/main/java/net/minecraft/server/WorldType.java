package net.minecraft.server;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;

public class WorldType {

	public static final BiomeBase[] base11Biomes = new BiomeBase[] {BiomeBase.DESERT, BiomeBase.FOREST, BiomeBase.EXTREME_HILLS, BiomeBase.SWAMPLAND, BiomeBase.PLAINS, BiomeBase.TAIGA};
    public static final BiomeBase[] base12Biomes = ObjectArrays.concat(base11Biomes, BiomeBase.JUNGLE);
    
    public static final WorldType[] types = new WorldType[16];
    public static final WorldType NORMAL = (new WorldType(0, "default", 1)).f();
    public static final WorldType FLAT = new WorldType(1, "flat");
    public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
    public static final WorldType NORMAL_1_1 = (new WorldType(8, "default_1_1", 0)).a(false);
    private final String name;
    private final int version;
    private boolean h;
    private boolean i;

    protected BiomeBase[] biomesForWorldType;
    
    private WorldType(int i, String s) {
        this(i, s, 0);
    }

    private WorldType(int i, String s, int j) {
        this.name = s;
        this.version = j;
        this.h = true;
        types[i] = this;
        switch (i)
        {
        case 8:
            biomesForWorldType = base11Biomes;
            break;
        default:
            biomesForWorldType = base12Biomes;
        }
    }

    public String name() {
        return this.name;
    }

    public int getVersion() {
        return this.version;
    }

    public WorldType a(int i) {
        return this == NORMAL && i == 0 ? NORMAL_1_1 : this;
    }

    private WorldType a(boolean flag) {
        this.h = flag;
        return this;
    }

    private WorldType f() {
        this.i = true;
        return this;
    }

    public boolean e() {
        return this.i;
    }

    public static WorldType getType(String s) {
        WorldType[] aworldtype = types;
        int i = aworldtype.length;

        for (int j = 0; j < i; ++j) {
            WorldType worldtype = aworldtype[j];

            if (worldtype != null && worldtype.name.equalsIgnoreCase(s)) {
                return worldtype;
            }
        }

        return null;
    }
    
    /** =================================== FML =============================== **/
    
    public WorldChunkManager getChunkManager(World world)
    {
        return this == FLAT ? new WorldChunkManagerHell(BiomeBase.PLAINS, 0.5F, 0.5F) : new WorldChunkManager(world);
    }

    public IChunkProvider getChunkGenerator(World world)
    {
        return (this == FLAT ? new ChunkProviderFlat(world, world.getSeed(), world.getWorldData().shouldGenerateMapFeatures()) : new ChunkProviderGenerate(world, world.getSeed(), world.getWorldData().shouldGenerateMapFeatures()));
    }

    public int getMinimumSpawnHeight(World world)
    {
        return this == FLAT ? 4 : 64;
    }

    public double getHorizon(World world)
    {
        return this == FLAT ? 0.0D : 63.0D;
    }

    public boolean hasVoidParticles(boolean var1)
    {
        return this != FLAT && !var1;
    }

    public double voidFadeMagnitude()
    {
        return this == FLAT ? 1.0D : 0.03125D;
    }

    public BiomeBase[] getBiomesForWorldType() {
        return biomesForWorldType;
    }

    public void addNewBiome(BiomeBase biome)
    {
        Set<BiomeBase> newBiomesForWorld = Sets.newIdentityHashSet();
        newBiomesForWorld.addAll(Arrays.asList(biomesForWorldType));
        newBiomesForWorld.add(biome);
        biomesForWorldType = newBiomesForWorld.toArray(new BiomeBase[0]);
    }

    public void removeBiome(BiomeBase biome)
    {
        Set<BiomeBase> newBiomesForWorld = Sets.newIdentityHashSet();
        newBiomesForWorld.addAll(Arrays.asList(biomesForWorldType));
        newBiomesForWorld.remove(biome);
        biomesForWorldType = newBiomesForWorld.toArray(new BiomeBase[0]);
    }

    public boolean handleSlimeSpawnReduction(Random random, World world)
    {
        return this == FLAT ? random.nextInt(4) != 1 : false;
    }
    /**
     * Called when 'Create New World' button is pressed before starting game
     */
    public void onGUICreateWorldPress() { }
}
