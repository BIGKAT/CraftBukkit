package net.minecraft.src;

import java.io.File;

import net.minecraft.server.ExceptionWorldConflict;
import net.minecraft.server.IChunkLoader;
import net.minecraft.server.PlayerFileData;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldProvider;

public interface ISaveHandler {

    WorldData getWorldData();

    void checkSession() throws ExceptionWorldConflict; // CraftBukkit - throws ExceptionWorldConflict

    IChunkLoader createChunkLoader(WorldProvider worldprovider);

    void saveWorldData(WorldData worlddata, net.minecraft.src.NBTTagCompound nbttagcompound);

    void saveWorldData(WorldData worlddata);

    PlayerFileData getPlayerFileData();

    void a();

    File getDataFile(String s);

    String g();

    java.util.UUID getUUID(); // CraftBukkit
}
