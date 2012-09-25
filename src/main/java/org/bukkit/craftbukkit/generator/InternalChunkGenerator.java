package org.bukkit.craftbukkit.generator;

import java.util.List;

import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;

import org.bukkit.generator.ChunkGenerator;

public abstract class InternalChunkGenerator extends ChunkGenerator implements IChunkProvider {
	abstract boolean isChunkLoaded(int var1, int var2);
	abstract Chunk getOrCreateChunk(int var1, int var2);
	abstract Chunk getChunkAt(int var1, int var2);
	abstract void getChunkAt(IChunkProvider var1, int var2, int var3);
	abstract boolean unloadChunks();
	abstract String getName();
	abstract List getMobsFor(EnumCreatureType var1, int var2, int var3, int var4);
	abstract ChunkPosition findNearestMapFeature(World var1, String var2, int var3, int var4, int var5);
	abstract int getLoadedChunks();

	@Override
	public boolean chunkExists(int var1, int var2) {
		return isChunkLoaded(var1, var2);
	}

	@Override
	public Chunk provideChunk(int var1, int var2) {
		return getOrCreateChunk(var1, var2);
	}

	@Override
	public Chunk loadChunk(int var1, int var2) {
		return getChunkAt(var1, var2);
	}

	@Override
	public void populate(IChunkProvider var1, int var2, int var3) {
		getChunkAt(var1, var2, var3);
	}

	@Override
	public boolean unload100OldestChunks() {
		return unloadChunks();
	}

	@Override
	public String makeString() {
		return getName();
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4) {
		return getMobsFor(var1, var2, var3, var4);
	}

	@Override
	public ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5) {
		return findNearestMapFeature(var1, var2, var3, var4, var5);
	}

	@Override
	public int getLoadedChunkCount() {
		return getLoadedChunks();
	}

}
