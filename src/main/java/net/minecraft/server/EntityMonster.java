package net.minecraft.server;

import org.bukkit.event.entity.EntityTargetEvent; // CraftBukkit

public abstract class EntityMonster extends EntityCreature implements IMonster {

    protected int damage = 2;

    public EntityMonster(World world) {
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

    protected Entity findTarget() {
        EntityHuman entityhuman = this.worldObj.findNearbyVulnerablePlayer(this, 16.0D);

        return entityhuman != null && this.l(entityhuman) ? entityhuman : null;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (super.damageEntity(damagesource, i)) {
            Entity entity = damagesource.getEntity();

            if (this.riddenByEntity != entity && this.ridingEntity != entity) {
                if (entity != this) {
                    // CraftBukkit start - we still need to call events for entities without goals
                    if (entity != this.entityToAttack && (this instanceof EntityBlaze || this instanceof EntityEnderman || this instanceof EntitySpider || this instanceof EntityGiantZombie || this instanceof EntitySilverfish)) {
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

    public boolean k(Entity entity) {
        int i = this.damage;

        if (this.hasEffect(MobEffectList.INCREASE_DAMAGE)) {
            i += 3 << this.getEffect(MobEffectList.INCREASE_DAMAGE).getAmplifier();
        }

        if (this.hasEffect(MobEffectList.WEAKNESS)) {
            i -= 2 << this.getEffect(MobEffectList.WEAKNESS).getAmplifier();
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
