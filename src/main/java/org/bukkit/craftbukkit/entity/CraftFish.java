package org.bukkit.craftbukkit.entity;

import net.minecraft.src.EntityFishHook;
import net.minecraft.src.EntityPlayer;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;

public class CraftFish extends AbstractProjectile implements Fish {
    public CraftFish(CraftServer server, EntityFishHook entity) {
        super(server, entity);
    }

    public LivingEntity getShooter() {
        if (getHandle().angler != null) {
            return (LivingEntity) CraftServer.getBukkitEntity(getHandle().angler);
        }

        return null;
    }

    public void setShooter(LivingEntity shooter) {
        if (shooter instanceof CraftHumanEntity) {
            getHandle().angler = (EntityPlayer) ((CraftHumanEntity) shooter).entity;
        }
    }

    @Override
    public EntityFishHook getHandle() {
        return (EntityFishHook) entity;
    }

    @Override
    public String toString() {
        return "CraftFish";
    }

    public EntityType getType() {
        return EntityType.FISHING_HOOK;
    }
}
