package net.minecraft.server;
// Forge start
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.DimensionManager;
// Forge end

public abstract class WorldProvider {

    /** world object being used */
    public World a;
    public WorldType type;
    public String c;

    /** World chunk manager being used to generate chunks */
    public WorldChunkManager d;

    /**
     * States whether the Hell world provider is used(true) or if the normal world provider is used(false)
     */
    public boolean e = false;

    /**
     * A boolean that tells if a world does not have a sky. Used in calculating weather and skylight
     */
    public boolean f = false;

    /** Light to brightness conversion table */
    public float[] g = new float[16];

    /** The id for the dimension (ex. -1: Nether, 0: Overworld, 1: The End) */
    public int dimension = 0;

    /** Array for sunrise/sunset colors (RGBA) */
    private float[] i = new float[4];

    /**
     * associate an existing world with a World provider, and setup its lightbrightness table
     */
    public final void a(World var1)
    {
        this.a = var1;
        this.type = var1.getWorldData().getType();
        this.c = var1.getWorldData().getGeneratorOptions();
        this.b();
        this.a();
    }

    /**
     * Creates the light to brightness table
     */
    protected void a()
    {
        float var1 = 0.0F;

        for (int var2 = 0; var2 <= 15; ++var2)
        {
            float var3 = 1.0F - (float)var2 / 15.0F;
            this.g[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }
    }

    /**
     * creates a new world chunk manager for WorldProvider
     */
    protected void b()
    {
        this.d = this.type.getChunkManager(this.a); // Forge
    }

    /**
     * Returns the chunk provider back for the world provider
     */
    public IChunkProvider getChunkProvider()
    {
        return this.type.getChunkGenerator(this.a, this.c); // Forge
    }

    /**
     * Will check if the x, z position specified is alright to be set as the map spawn point
     */
    public boolean canSpawn(int var1, int var2)
    {
        int var3 = this.a.b(var1, var2);
        return var3 == Block.GRASS.id;
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     */
    public float a(long var1, float var3)
    {
        int var4 = (int)(var1 % 24000L);
        float var5 = ((float)var4 + var3) / 24000.0F - 0.25F;

        if (var5 < 0.0F)
        {
            ++var5;
        }

        if (var5 > 1.0F)
        {
            --var5;
        }

        float var6 = var5;
        var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0D) / 2.0D);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    public boolean d()
    {
        return true;
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    public boolean e()
    {
        return true;
    }

    public static WorldProvider byDimension(int var0)
    {
        return DimensionManager.createProviderFor(var0); // Forge
    }

    /**
     * Gets the hard-coded portal location to use when entering this dimension.
     */
    public ChunkCoordinates h()
    {
        return null;
    }

    public int getSeaLevel()
    {
        return this.type.getMinimumSpawnHeight(this.a); // Forge
    }

    /**
     * Returns the dimension's name, e.g. "The End", "Nether", or "Overworld".
     */
    public abstract String getName();

    /*======================================= Forge Start =========================================*/

    /**
     * Sets the providers current dimension ID, used in default getSaveFolder()
     * Added to allow default providers to be registered for multiple dimensions.
     * 
     * @param dim Dimension ID
     */
    public void setDimension(int dim)
    {
        this.dimension = dim;
    }

    /**
     * Returns the sub-folder of the world folder that this WorldProvider saves to.
     * EXA: DIM1, DIM-1
     * @return The sub-folder name to save this world's chunks to.
     */
    public String getSaveFolder()
    {
        return (dimension == 0 ? null : "DIM" + dimension);
    }

    /**
     * A message to display to the user when they transfer to this dimension.
     *
     * @return The message to be displayed
     */
    public String getWelcomeMessage()
    {
        return this instanceof WorldProviderTheEnd ? "Entering the End" : (this instanceof WorldProviderHell ? "Entering the Nether" : null);
    }

    /**
     * A Message to display to the user when they transfer out of this dismension.
     *
     * @return The message to be displayed
     */
    public String getDepartMessage()
    {
        return this instanceof WorldProviderTheEnd ? "Leaving the End" : (this instanceof WorldProviderHell ? "Leaving the Nether" : null);
    }

    /**
     * The dimensions movement factor. Relative to normal overworld.
     * It is applied to the players position when they transfer dimensions.
     * Exa: Nether movement is 8.0
     * @return The movement factor
     */
    public double getMovementFactor()
    {
        return this instanceof WorldProviderHell ? 8.0D : 1.0D;
    }


    public ChunkCoordinates getRandomizedSpawnPoint()
    {
        ChunkCoordinates chunkCoords = new ChunkCoordinates(this.a.getSpawn());
        boolean isAdventure = this.a.getWorldData().getGameType() != EnumGamemode.ADVENTURE;
        int spawnFuzz = this.type.getSpawnFuzz();
        int spawnFuzzHalf = spawnFuzz / 2;

        if (!this.f && !isAdventure)
        {
            chunkCoords.x += this.a.random.nextInt(spawnFuzz) - spawnFuzzHalf;
            chunkCoords.z += this.a.random.nextInt(spawnFuzz) - spawnFuzzHalf;
            chunkCoords.y = this.a.i(chunkCoords.x, chunkCoords.z);
        }

        return chunkCoords;
    }

    /**
     * Determine if the cusor on the map should 'spin' when rendered, like it does for the player in the nether.
     * 
     * @param entity The entity holding the map, playername, or frame-ENTITYID
     * @param x X Position
     * @param y Y Position
     * @param z Z Postion
     * @return True to 'spin' the cursor
     */
    public boolean shouldMapSpin(String entity, double x, double y, double z)
    {
        return dimension < 0;
    }

    /**
     * Determines the dimension the player will be respawned in, typically this brings them back to the overworld.
     * 
     * @param player The player that is respawning
     * @return The dimension to respawn the player in
     */
    public int getRespawnDimension(EntityPlayer player)
    {
        return 0;
    }

    /*======================================= Start Moved From World =========================================*/

    public BiomeBase getBiomeGenForCoords(int x, int z)
    {
        return this.a.getBiomeGenForCoordsBody(x, z);
    }

    public boolean isDaytime()
    {
        return this.a.j < 4;
    }

    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
    {
        this.a.allowMonsters = allowHostile;
        this.a.allowAnimals = allowPeaceful;
    }

    public void calculateInitialWeather()
    {
        this.a.calculateInitialWeatherBody();
    }

    public void updateWeather()
    {
        this.a.updateWeatherBody();
    }

    public void toggleRain()
    {
        this.a.worldData.setWeatherDuration(1);
    }

    public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
    {
        return this.a.canBlockFreezeBody(x, y, z, byWater);
    }

    public boolean canSnowAt(int x, int y, int z)
    {
        return this.a.canSnowAtBody(x, y, z);
    }

    public void setWorldTime(long time)
    {
        this.a.worldData.setDayTime(time);
    }

    public long getSeed()
    {
        return this.a.worldData.getSeed();
    }

    public long getWorldTime()
    {
        return this.a.worldData.getDayTime();
    }

    public ChunkCoordinates getSpawnPoint()
    {
        WorldData info = this.a.worldData;
        return new ChunkCoordinates(info.c(), info.d(), info.e());
    }

    public void setSpawnPoint(int x, int y, int z)
    {
        this.a.worldData.setSpawn(x, y, z);
    }

    public boolean canMineBlock(EntityHuman player, int x, int y, int z)
    {
        return this.a.canMineBlockBody(player, x, y, z);
    }

    public boolean isBlockHighHumidity(int x, int y, int z)
    {
        return this.a.getBiome(x, z).e();
    }

    public int getHeight()
    {
        return 256;
    }

    public int getActualHeight()
    {
        return this.f ? 128 : 256;
    }

    public double getHorizon()
    {
        return this.a.worldData.getType().getHorizon(this.a);
    }

    public void resetRainAndThunder()
    {
        this.a.worldData.setWeatherDuration(0);
        this.a.worldData.setStorm(false);
        this.a.worldData.setThunderDuration(0);
        this.a.worldData.setThundering(false);
    }

    public boolean canDoLightning(Chunk chunk)
    {
        return true;
    }

    public boolean canDoRainSnowIce(Chunk chunk)
    {
        return true;
    }
}
