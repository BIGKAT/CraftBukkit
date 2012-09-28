package org.bukkit.craftbukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.World;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator extends InternalChunkGenerator {
    private final IChunkProvider provider;

    public NormalChunkGenerator(World world, long seed) {
        provider = world.provider.getChunkProvider();
    }

    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().provider.canCoordinateBeSpawn(x, z);
    }

    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    public boolean isChunkLoaded(int i, int i1) {
        return provider.chunkExists(i, i1);
    }

    public Chunk getOrCreateChunk(int i, int i1) {
        return provider.provideChunk(i, i1);
    }

    public Chunk getChunkAt(int i, int i1) {
        return provider.loadChunk(i, i1);
    }

    public void getChunkAt(IChunkProvider icp, int i, int i1) {
        provider.populate(icp, i, i1);
    }

	public boolean saveChunks(boolean bln, IProgressUpdate ipu) {
        return provider.saveChunks(bln, ipu);
    }

	public boolean unloadChunks() {
        return provider.unload100OldestChunks();
    }

    public boolean canSave() {
        return provider.canSave();
    }

	public List<?> getMobsFor(EnumCreatureType ect, int i, int i1, int i2) {
        return provider.getPossibleCreatures(ect, i, i1, i2);
    }

    public ChunkPosition findNearestMapFeature(World world, String string, int i, int i1, int i2) {
        return provider.findClosestStructure(world, string, i, i1, i2);
    }

    public int getLoadedChunks() {
        return 0;
    }

    public String getName() {
        return "NormalWorldGenerator";
    }

}
