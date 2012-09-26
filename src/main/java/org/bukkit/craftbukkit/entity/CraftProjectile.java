package org.bukkit.craftbukkit.entity;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityThrowable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, Entity entity) {
        super(server, entity);
    }

    public LivingEntity getShooter() {
        if (getHandle().thrower instanceof EntityLiving) {
            return (LivingEntity) getHandle().thrower.getBukkitEntity();
        }

        return null;
    }

    public void setShooter(LivingEntity shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().thrower = (EntityLiving) ((CraftLivingEntity) shooter).entity;
        }
    }

    @Override
    public EntityThrowable getHandle() {
        return (EntityThrowable) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }
}
