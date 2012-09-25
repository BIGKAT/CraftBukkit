package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityIronGolem;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;

public class CraftIronGolem extends CraftGolem implements IronGolem {
    public CraftIronGolem(CraftServer server, EntityIronGolem entity) {
        super(server, entity);
    }

    @Override
    public EntityIronGolem getHandle() {
        return (EntityIronGolem) entity;
    }

    @Override
    public String toString() {
        return "CraftIronGolem";
    }

    public boolean isPlayerCreated() {
        return getHandle().getBit1Flag();
    }

    public void setPlayerCreated(boolean playerCreated) {
        getHandle().setBit1FlagTo(playerCreated);
    }

    @Override
    public EntityType getType() {
        return EntityType.IRON_GOLEM;
    }
}
