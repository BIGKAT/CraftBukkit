package net.minecraft.server;

import forge.DimensionManager;


public abstract class WorldProvider {

    public World a;
    public WorldType type;
    public WorldChunkManager c;
    public boolean d = false;
    public boolean e = false;
    public float[] f = new float[16];
    public int dimension = 0;
    private float[] h = new float[4];

    public WorldProvider() {}

    public final void a(World world) {
        this.a = world;
        this.type = world.getWorldData().getType();
        this.a();
        this.g();
    }

    protected void g() {
        float f = 0.0F;

        for (int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float) i / 15.0F;

            this.f[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }

    protected void a() {
    	this.c = this.a.getWorldData().getType().getChunkManager(this.a);
    }

    public IChunkProvider getChunkProvider() {
    	return this.type.getChunkGenerator(this.a);
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

    public boolean c() {
        return true;
    }

    public static WorldProvider byDimension(int i) {
        return DimensionManager.getProvider(i);
    }

    public ChunkCoordinates e() {
        return null;
    }

    public int getSeaLevel() {
        return this.type.getMinimumSpawnHeight(this.a);
    }
    /**
     * Returns the sub-folder of the world folder that this WorldProvider saves to.
     * EXA: DIM1, DIM-1
     * @return The sub-folder name to save this world's chunks to.
     */
    public abstract String getSaveFolder();
    /**
     * A message to display to the user when they transfer to this dimension.
     *
     * @return The message to be displayed
     */
    public abstract String getWelcomeMessage();
    /**
     * A Message to display to the user when they transfer out of this dismension.
     *
     * @return The message to be displayed
     */
    public abstract String getDepartMessage();
    /**
     * The dimensions movement factor. Relative to normal overworld.
     * It is applied to the players position when they transfer dimensions.
     * Exa: Nether movement is 8.0
     * @return The movement factor
     */
    public double getMovementFactor()
    {
        return 1.0;
    }
}
