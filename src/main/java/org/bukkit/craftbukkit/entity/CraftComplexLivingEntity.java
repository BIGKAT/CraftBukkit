package org.bukkit.craftbukkit.entity;

import net.minecraft.src.EntityDragonBase;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexLivingEntity;

public abstract class CraftComplexLivingEntity extends CraftLivingEntity implements ComplexLivingEntity {
    public CraftComplexLivingEntity(CraftServer server, EntityDragonBase entity) {
        super(server, entity);
    }

    @Override
    public EntityDragonBase getHandle() {
        return (EntityDragonBase) entity;
    }

    @Override
    public String toString() {
        return "CraftComplexLivingEntity";
    }
}
