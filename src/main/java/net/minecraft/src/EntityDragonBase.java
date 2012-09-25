package net.minecraft.src;

import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityComplexPart;

public class EntityDragonBase extends net.minecraft.src.EntityLiving {

    protected int a = 100;

    public EntityDragonBase(net.minecraft.src.World world) {
        super(world);
    }

    public int getMaxHealth() {
        return this.a;
    }

    public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
        return this.damageEntity(damagesource, i);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }

    public boolean dealDamage(DamageSource damagesource, int i) { // CraftBukkit - protected -> public
        return super.damageEntity(damagesource, i);
    }
}
