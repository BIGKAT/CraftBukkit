package org.bukkit.craftbukkit.entity;


import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, net.minecraft.entity.item.EntityTNTPrimed/*was:EntityTNTPrimed*/ entity) {
        super(server, entity);
    }

    public float getYield() {
        return getHandle().yield;
    }

    public boolean isIncendiary() {
        return getHandle().isIncendiary;
    }

    public void setIsIncendiary(boolean isIncendiary) {
        getHandle().isIncendiary = isIncendiary;
    }

    public void setYield(float yield) {
        getHandle().yield = yield;
    }

    public int getFuseTicks() {
        return getHandle().fuse/*was:fuseTicks*/;
    }

    public void setFuseTicks(int fuseTicks) {
        getHandle().fuse/*was:fuseTicks*/ = fuseTicks;
    }

    @Override
    public net.minecraft.entity.item.EntityTNTPrimed/*was:EntityTNTPrimed*/ getHandle() {
        return (net.minecraft.entity.item.EntityTNTPrimed/*was:EntityTNTPrimed*/) entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }

}
