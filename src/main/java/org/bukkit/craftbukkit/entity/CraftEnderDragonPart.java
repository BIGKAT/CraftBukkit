package org.bukkit.craftbukkit.entity;

import net.minecraft.src.EntityDragonPart;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;

public class CraftEnderDragonPart extends CraftComplexPart implements EnderDragonPart {
    public CraftEnderDragonPart(CraftServer server, EntityDragonPart entity) {
        super(server, entity);
    }

    @Override
    public EnderDragon getParent() {
        return (EnderDragon) super.getParent();
    }

    @Override
    public EntityDragonPart getHandle() {
        return (EntityDragonPart) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragonPart";
    }
}
