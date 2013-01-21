package net.minecraft.world.storage;

import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;

public interface ISaveHandler
{
    /**
     * Loads and returns the world info
     */
    WorldInfo loadWorldInfo();

    void checkSessionLock() throws MinecraftException; // CraftBukkit - throws ExceptionWorldConflict

    IChunkLoader getChunkLoader(WorldProvider worldprovider);

    void saveWorldInfoWithPlayer(WorldInfo worlddata, NBTTagCompound nbttagcompound);

    void saveWorldInfo(WorldInfo worlddata);

    /**
     * returns null if no saveHandler is relevent (eg. SMP)
     */
    IPlayerFileData getSaveHandler();

    /**
     * Called to flush all changes to disk, waiting for them to complete.
     */
    void flush();

    File getMapFileFromName(String s);

    /**
     * Returns the name of the directory where world information is saved.
     */
    String getSaveDirectoryName();

    java.util.UUID getUUID(); // CraftBukkit
}
