package org.bukkit.event;


public class WorldTimingsHandler {
    public CustomTimingsHandler mobSpawn;
    public CustomTimingsHandler doTickRest;
    public CustomTimingsHandler entityBaseTick;
    public CustomTimingsHandler entityTick;
    public CustomTimingsHandler tileEntityTick;
    public WorldTimingsHandler(net.minecraft.world.World/*was:World*/ server) {
        String name = server.worldInfo/*was:worldData*/.getWorldName/*was:getName*/() +" - ";

        mobSpawn       = new CustomTimingsHandler(name + "mobSpawn");
        doTickRest     = new CustomTimingsHandler(name + "doTickRest");
        entityBaseTick = new CustomTimingsHandler(name + "entityBaseTick");
        entityTick     = new CustomTimingsHandler(name + "entityTick");
        tileEntityTick = new CustomTimingsHandler(name + "tileEntityTick");
    }
}
