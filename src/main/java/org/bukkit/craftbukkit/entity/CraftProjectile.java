package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityProjectile;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

public abstract class CraftProjectile extends AbstractProjectile implements Projectile {
    public CraftProjectile(CraftServer server, net.minecraft.server.Entity entity) {
        super(server, entity);
    }

    public LivingEntity getShooter() {
        if (getHandle().shootingEntity instanceof EntityLiving) {
            return (LivingEntity) getHandle().shootingEntity.getBukkitEntity();
        }

        return null;
    }

    public void setShooter(LivingEntity shooter) {
        if (shooter instanceof CraftLivingEntity) {
            getHandle().shootingEntity = (EntityLiving) ((CraftLivingEntity) shooter).entity;
        }
    }

    @Override
    public EntityProjectile getHandle() {
        return (EntityProjectile) entity;
    }

    @Override
    public String toString() {
        return "CraftProjectile";
    }
}
