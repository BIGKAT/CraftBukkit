package org.bukkit.craftbukkit.entity;

import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityLiving;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

public class CraftCreature extends CraftLivingEntity implements Creature {
    public CraftCreature(CraftServer server, EntityCreature entity) {
        super(server, entity);
    }

    public void setTarget(LivingEntity target) {
        EntityCreature entity = getHandle();
        if (target == null) {
            entity.setTarget(null);
        } else if (target instanceof CraftLivingEntity) {
            EntityLiving victim = ((CraftLivingEntity) target).getHandle();
            entity.setTarget(victim);
            entity.setPathToEntity(entity.worldObj.getPathEntityToEntity(entity, entity.getEntityToAttack(), 16.0F, true, false, false, true));
        }
    }

    public CraftLivingEntity getTarget() {
        if (getHandle().getEntityToAttack() == null) return null;
        if (!(getHandle().getEntityToAttack() instanceof EntityLiving)) return null;

        return (CraftLivingEntity) CraftServer.getBukkitEntity(getHandle().getEntityToAttack());
    }

    @Override
    public EntityCreature getHandle() {
        return (EntityCreature) entity;
    }

    @Override
    public String toString() {
        return "CraftCreature";
    }
}
