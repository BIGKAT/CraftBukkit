package org.bukkit.craftbukkit.entity;


import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

public class CraftEnderman extends CraftMonster implements Enderman {
    public CraftEnderman(CraftServer server, net.minecraft.entity.monster.EntityEnderman/*was:EntityEnderman*/ entity) {
        super(server, entity);
    }

    public MaterialData getCarriedMaterial() {
        return Material.getMaterial(getHandle().getCarried/*was:getCarriedId*/()).getNewData((byte) getHandle().getCarryingData/*was:getCarriedData*/());
    }

    public void setCarriedMaterial(MaterialData data) {
        getHandle().setCarried/*was:setCarriedId*/(data.getItemTypeId());
        getHandle().setCarryingData/*was:setCarriedData*/(data.getData());
    }

    @Override
    public net.minecraft.entity.monster.EntityEnderman/*was:EntityEnderman*/ getHandle() {
        return (net.minecraft.entity.monster.EntityEnderman/*was:EntityEnderman*/) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderman";
    }

    public EntityType getType() {
        return EntityType.ENDERMAN;
    }
}
