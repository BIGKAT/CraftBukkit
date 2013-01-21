package net.minecraft.world;

import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.ISaveHandler;
public class WorldServerMulti extends WorldServer
{
    // CraftBukkit start - Changed signature
    public WorldServerMulti(MinecraftServer minecraftserver, ISaveHandler idatamanager, String s, int i, WorldSettings worldsettings, WorldServer worldserver, Profiler methodprofiler, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen)
    {
        super(minecraftserver, idatamanager, s, i, worldsettings, methodprofiler, env, gen);
        // CraftBukkit end
        this.mapStorage = worldserver.mapStorage;
        // this.worldData = new SecondaryWorldData(worldserver.getWorldData()); // CraftBukkit - use unique worlddata
    }

    // protected void a() {} // CraftBukkit - save world data!
}
