package org.bukkit.craftbukkit.entity;

import net.minecraft.src.Entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Vehicle;

public abstract class CraftVehicle extends CraftEntity implements Vehicle {
    public CraftVehicle(CraftServer server, Entity entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftVehicle{passenger=" + getPassenger() + '}';
    }
}
