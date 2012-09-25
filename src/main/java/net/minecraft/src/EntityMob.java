package net.minecraft.src;

import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityGiantZombie;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.IMonster;
import net.minecraft.server.MathHelper;
import net.minecraft.src.*;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySilverfish;

import org.bukkit.event.entity.EntityTargetEvent; // CraftBukkit

public abstract class EntityMob extends EntityCreature implements IMonster {

    protected int damage = 2;

    public EntityMob(net.minecraft.src.World world) {
        super(world);
        this.aV = 5;
    }

    public void d() {
        float f = this.c(1.0F);

        if (f > 0.5F) {
            this.bq += 2;
        }

        super.d();
    }

    public void h_() {
        super.h_();
        if (!this.worldObj.isStatic && this.worldObj.difficulty == 0) {
            this.setDead();
        }
    }

    protected net.minecraft.src.Entity findTarget() {
        EntityPlayer entityhuman = this.worldObj.findNearbyVulnerablePlayer(this, 16.0D);

        return entityhuman != null && this.l(entityhuman) ? entityhuman : null;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (super.damageEntity(damagesource, i)) {
            net.minecraft.src.Entity entity = damagesource.getEntity();

            if (this.riddenByEntity != entity && this.ridingEntity != entity) {
                if (entity != this) {
                    // CraftBukkit start - we still need to call events for entities without goals
                    if (entity != this.entityToAttack && (this instanceof EntityBlaze || this instanceof EntityEnderman || this instanceof net.minecraft.src.EntitySpider || this instanceof EntityGiantZombie || this instanceof EntitySilverfish)) {
                        EntityTargetEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTargetEvent(this, entity, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);

                        if (!event.isCancelled()) {
                            if (event.getTarget() == null) {
                                this.entityToAttack = null;
                            } else {
                                this.entityToAttack = ((org.bukkit.craftbukkit.entity.CraftEntity) event.getTarget()).getHandle();
                            }
                        }
                    } else {
                        this.entityToAttack = entity;
                    }
                    // CraftBukkit end
                }

                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean k(net.minecraft.src.Entity entity) {
        int i = this.damage;

        if (this.hasEffect(Potion.INCREASE_DAMAGE)) {
            i += 3 << this.getEffect(Potion.INCREASE_DAMAGE).getAmplifier();
        }

        if (this.hasEffect(Potion.WEAKNESS)) {
            i -= 2 << this.getEffect(Potion.WEAKNESS).getAmplifier();
        }

        return entity.damageEntity(DamageSource.mobAttack(this), i);
    }

    protected void a(Entity entity, float f) {
        if (this.attackTicks <= 0 && f < 2.0F && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
            this.attackTicks = 20;
            this.k(entity);
        }
    }

    public float a(int i, int j, int k) {
        return 0.5F - this.worldObj.o(i, j, k);
    }

    protected boolean o() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.posZ);

        if (this.worldObj.b(EnumSkyBlock.Sky, i, j, k) > this.random.nextInt(32)) {
            return false;
        } else {
            int l = this.worldObj.getBlockLightValue(i, j, k);

            if (this.worldObj.I()) {
                int i1 = this.worldObj.k;

                this.worldObj.k = 10;
                l = this.worldObj.getBlockLightValue(i, j, k);
                this.worldObj.k = i1;
            }

            return l <= this.random.nextInt(8);
        }
    }

    public boolean canSpawn() {
        return this.o() && super.canSpawn();
    }
}
