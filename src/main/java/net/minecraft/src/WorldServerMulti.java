package net.minecraft.src;

import net.minecraft.src.ISaveHandler;
import net.minecraft.src.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldSettings;

public class WorldServerMulti extends net.minecraft.src.WorldServer {
    // CraftBukkit start - Changed signature
    public WorldServerMulti(MinecraftServer minecraftserver, ISaveHandler idatamanager, String s, int i, WorldSettings worldsettings, net.minecraft.src.WorldServer worldserver, Profiler methodprofiler, org.bukkit.World.Environment env, org.bukkit.generator.ChunkGenerator gen) {
        super(minecraftserver, idatamanager, s, i, worldsettings, methodprofiler, env, gen);
        // CraftBukkit end
        this.worldMaps = worldserver.worldMaps;
        // this.worldData = new SecondaryWorldData(worldserver.getWorldData()); // CraftBukkit - use unique worlddata
    }

    // protected void a() {} // CraftBukkit - save world data!
}
