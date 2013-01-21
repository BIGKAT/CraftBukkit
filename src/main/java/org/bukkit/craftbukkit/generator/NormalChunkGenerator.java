package org.bukkit.craftbukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.generator.BlockPopulator;

public class NormalChunkGenerator extends InternalChunkGenerator {
    private final net.minecraft.world.chunk.IChunkProvider/*was:IChunkProvider*/ provider;

    public NormalChunkGenerator(net.minecraft.world.World/*was:World*/ world, long seed) {
        provider = world.provider/*was:worldProvider*/.createChunkGenerator/*was:getChunkProvider*/();
    }

    public byte[] generate(org.bukkit.World world, Random random, int x, int z) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean canSpawn(org.bukkit.World world, int x, int z) {
        return ((CraftWorld) world).getHandle().provider/*was:worldProvider*/.canCoordinateBeSpawn/*was:canSpawn*/(x, z);
    }

    public List<BlockPopulator> getDefaultPopulators(org.bukkit.World world) {
        return new ArrayList<BlockPopulator>();
    }

    public boolean chunkExists/*was:isChunkLoaded*/(int i, int i1) {
        return provider.chunkExists/*was:isChunkLoaded*/(i, i1);
    }

    public net.minecraft.world.chunk.Chunk/*was:Chunk*/ provideChunk/*was:getOrCreateChunk*/(int i, int i1) {
        return provider.provideChunk/*was:getOrCreateChunk*/(i, i1);
    }

    public net.minecraft.world.chunk.Chunk/*was:Chunk*/ loadChunk/*was:getChunkAt*/(int i, int i1) {
        return provider.loadChunk/*was:getChunkAt*/(i, i1);
    }

    public void populate/*was:getChunkAt*/(net.minecraft.world.chunk.IChunkProvider/*was:IChunkProvider*/ icp, int i, int i1) {
        provider.populate/*was:getChunkAt*/(icp, i, i1);
    }

    public boolean saveChunks/*was:saveChunks*/(boolean bln, net.minecraft.util.IProgressUpdate/*was:IProgressUpdate*/ ipu) {
        return provider.saveChunks/*was:saveChunks*/(bln, ipu);
    }

    public boolean unload100OldestChunks/*was:unloadChunks*/() {
        return provider.unload100OldestChunks/*was:unloadChunks*/();
    }

    public boolean canSave/*was:canSave*/() {
        return provider.canSave/*was:canSave*/();
    }

    public List getPossibleCreatures/*was:getMobsFor*/(net.minecraft.entity.EnumCreatureType/*was:EnumCreatureType*/ ect, int i, int i1, int i2) { // CBMCP - return type List<?> -> List for matching remap
        return provider.getPossibleCreatures/*was:getMobsFor*/(ect, i, i1, i2);
    }

    public net.minecraft.world.ChunkPosition/*was:ChunkPosition*/ findClosestStructure/*was:findNearestMapFeature*/(net.minecraft.world.World/*was:World*/ world, String string, int i, int i1, int i2) {
        return provider.findClosestStructure/*was:findNearestMapFeature*/(world, string, i, i1, i2);
    }

    public void recreateStructures/*was:recreateStructures*/(int i, int j) {
        provider.recreateStructures/*was:recreateStructures*/(i, j);
    }

    // n.m.s implementations always return 0. (The true implementation is in ChunkProviderServer)
    public int getLoadedChunkCount/*was:getLoadedChunks*/() {
        return 0;
    }

    public String makeString/*was:getName*/() {
        return "NormalWorldGenerator";
    }
}
