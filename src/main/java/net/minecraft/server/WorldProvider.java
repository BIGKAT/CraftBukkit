package net.minecraft.server;

import net.minecraftforge.client.SkyProvider;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public abstract class WorldProvider {

    public World a;
    public WorldType type;
    public WorldChunkManager c;
    public boolean d = false;
    public boolean e = false;
    public float[] f = new float[16];
    public int dimension = 0;
    private float[] h = new float[4];

    private SkyProvider skyProvider = null;
    
    public WorldProvider() {}

    public final void a(World world) {
        this.a = world;
        this.type = world.getWorldData().getType();
        this.b();
        this.a();
    }

    protected void a() {
        float f = 0.0F;

        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float) i / 15.0F;

            this.f[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }

    protected void b() {
        if (this.a.getWorldData().getType() == WorldType.FLAT) {
            this.c = new WorldChunkManagerHell(BiomeBase.PLAINS, 0.5F, 0.5F);
        } else {
            this.c = new WorldChunkManager(this.a);
        }
    }

    public IChunkProvider getChunkProvider() {
        return (IChunkProvider) (this.type == WorldType.FLAT ? new ChunkProviderFlat(this.a, this.a.getSeed(), this.a.getWorldData().shouldGenerateMapFeatures()) : new ChunkProviderGenerate(this.a, this.a.getSeed(), this.a.getWorldData().shouldGenerateMapFeatures()));
    }

    public boolean canSpawn(int i, int j) {
        int k = this.a.b(i, j);

        return k == Block.GRASS.id;
    }

    public float a(long i, float f) {
        int j = (int) (i % 24000L);
        float f1 = ((float) j + f) / 24000.0F - 0.25F;

        if (f1 < 0.0F) {
            ++f1;
        }

        if (f1 > 1.0F) {
            --f1;
        }

        float f2 = f1;

        f1 = 1.0F - (float) ((Math.cos((double) f1 * 3.141592653589793D) + 1.0D) / 2.0D);
        f1 = f2 + (f1 - f2) / 3.0F;
        return f1;
    }

    public boolean d() {
        return true;
    }

    public boolean e() {
        return true;
    }

    public static WorldProvider byDimension(int i) {
//        return (WorldProvider) (i == -1 ? new WorldProviderHell() : (i == 0 ? new WorldProviderNormal() : (i == 1 ? new WorldProviderTheEnd() : null)));
    	return DimensionManager.createProviderFor(i);
    }

    public ChunkCoordinates h() {
        return null;
    }

    public int getSeaLevel() {
        return this.type == WorldType.FLAT ? 4 : 64;
    }

    public abstract String getName();
    
    /*======================================= Forge Start =========================================*/
    private int dimensionID = 0;

    /**
     * Sets the providers current dimension ID, used in default getSaveFolder()
     * Added to allow default providers to be registered for multiple dimensions.
     * 
     * @param dim Dimension ID
     */
    public void setDimension(int dim)
    {
        this.dimensionID = dim;
    }

    /**
     * Returns the sub-folder of the world folder that this WorldProvider saves to.
     * EXA: DIM1, DIM-1
     * @return The sub-folder name to save this world's chunks to.
     */
    public String getSaveFolder()
    {
        return (dimensionID == 0 ? null : "DIM" + dimensionID);
    }

    /**
     * A message to display to the user when they transfer to this dimension.
     *
     * @return The message to be displayed
     */
    public String getWelcomeMessage()
    {
        if (this instanceof WorldProviderTheEnd)
        {
            return "Entering the End";
        }
        else if (this instanceof WorldProviderHell)
        {
            return "Entering the Nether";
        }
        return null;
    }

    /**
     * A Message to display to the user when they transfer out of this dismension.
     *
     * @return The message to be displayed
     */
    public String getDepartMessage()
    {
        if (this instanceof WorldProviderTheEnd)
        {
            return "Leaving the End";
        }
        else if (this instanceof WorldProviderHell)
        {
            return "Leaving the Nether";
        } 
        return null;
    }

    /**
     * The dimensions movement factor. Relative to normal overworld.
     * It is applied to the players position when they transfer dimensions.
     * Exa: Nether movement is 8.0
     * @return The movement factor
     */
    public double getMovementFactor()
    {
        if (this instanceof WorldProviderHell)
        {
            return 8.0;
        }
        return 1.0;
    }

//    @SideOnly(Side.CLIENT)
//    public SkyProvider getSkyProvider()
//    {
//        return this.skyProvider;
//    }
//
//    @SideOnly(Side.CLIENT)
//    public void setSkyProvider(SkyProvider skyProvider)
//    {
//        this.skyProvider = skyProvider;
//    }
}
