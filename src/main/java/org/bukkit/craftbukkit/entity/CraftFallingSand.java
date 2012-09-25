package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityFallingBlock;

import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingSand;

public class CraftFallingSand extends CraftEntity implements FallingSand {

    public CraftFallingSand(CraftServer server, EntityFallingBlock entity) {
        super(server, entity);
    }

    @Override
    public EntityFallingBlock getHandle() {
        return (EntityFallingBlock) entity;
    }

    @Override
    public String toString() {
        return "CraftFallingSand";
    }

    public EntityType getType() {
        return EntityType.FALLING_BLOCK;
    }

    public Material getMaterial() {
        return Material.getMaterial(getBlockId());
    }

    public int getBlockId() {
        return getHandle().blockID;
    }

    public byte getBlockData() {
        return (byte) getHandle().field_70285_b;
    }

    public boolean getDropItem() {
        return getHandle().field_70284_d;
    }

    public void setDropItem(boolean drop) {
        getHandle().field_70284_d = drop;
    }
}
