package org.bukkit.craftbukkit.entity;

import net.minecraft.src.EntityBoat;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;

public class CraftBoat extends CraftVehicle implements Boat {

    private double maxSpeed;
	private double occupiedDeceleration;
	private double unoccupiedDeceleration;
	private boolean landBoats;

	public CraftBoat(CraftServer server, EntityBoat entity) {
        super(server, entity);
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            maxSpeed = speed;
        }
    }

    public double getOccupiedDeceleration() {
        return occupiedDeceleration;
    }

    public void setOccupiedDeceleration(double speed) {
        if (speed >= 0D) {
            occupiedDeceleration = speed;
        }
    }

    public double getUnoccupiedDeceleration() {
        return unoccupiedDeceleration;
    }

    public void setUnoccupiedDeceleration(double speed) {
        unoccupiedDeceleration = speed;
    }

    public boolean getWorkOnLand() {
        return landBoats;
    }

    public void setWorkOnLand(boolean workOnLand) {
        landBoats = workOnLand;
    }

    @Override
    public EntityBoat getHandle() {
        return (EntityBoat) entity;
    }

    @Override
    public String toString() {
        return "CraftBoat";
    }

    public EntityType getType() {
        return EntityType.BOAT;
    }
}
