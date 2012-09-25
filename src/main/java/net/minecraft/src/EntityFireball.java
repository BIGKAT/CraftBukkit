package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.DamageSource;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.NBTTagDouble;
import net.minecraft.server.NBTTagList;

import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
// CraftBukkit end

public class EntityFireball extends net.minecraft.src.Entity {

    private int e = -1;
    private int f = -1;
    private int g = -1;
    private int h = 0;
    private boolean i = false;
    public net.minecraft.src.EntityLiving shootingEntity;
    private int j;
    private int an = 0;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public float yield = 1; // CraftBukkit
    public boolean isIncendiary = true; // CraftBukkit

    public EntityFireball(net.minecraft.src.World world) {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    protected void entityInit() {}

    public EntityFireball(net.minecraft.src.World world, double d0, double d1, double d2, double d3, double d4, double d5) {
        super(world);
        this.setSize(1.0F, 1.0F);
        this.setPositionRotation(d0, d1, d2, this.rotationYaw, this.rotationPitch);
        this.setPosition(d0, d1, d2);
        double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

        this.accelerationX = d3 / d6 * 0.1D;
        this.accelerationY = d4 / d6 * 0.1D;
        this.accelerationZ = d5 / d6 * 0.1D;
    }

    public EntityFireball(net.minecraft.src.World world, net.minecraft.src.EntityLiving entityliving, double d0, double d1, double d2) {
        super(world);
        this.shootingEntity = entityliving;
        this.setSize(1.0F, 1.0F);
        this.setPositionRotation(entityliving.posX, entityliving.posY, entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        // CraftBukkit start - (added setDirection method)
        this.setDirection(d0, d1, d2);
    }

    public void setDirection(double d0, double d1, double d2) {
        // CraftBukkit end
        d0 += this.random.nextGaussian() * 0.4D;
        d1 += this.random.nextGaussian() * 0.4D;
        d2 += this.random.nextGaussian() * 0.4D;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        this.accelerationX = d0 / d3 * 0.1D;
        this.accelerationY = d1 / d3 * 0.1D;
        this.accelerationZ = d2 / d3 * 0.1D;
    }

    public void h_() {
        if (!this.worldObj.isStatic && (this.shootingEntity != null && this.shootingEntity.isDead || !this.worldObj.isLoaded((int) this.posX, (int) this.posY, (int) this.posZ))) {
            this.setDead();
        } else {
            super.h_();
            this.setOnFire(1);
            if (this.i) {
                int i = this.worldObj.getBlockId(this.e, this.f, this.g);

                if (i == this.h) {
                    ++this.j;
                    if (this.j == 600) {
                        this.setDead();
                    }

                    return;
                }

                this.i = false;
                this.motionX *= (double) (this.random.nextFloat() * 0.2F);
                this.motionY *= (double) (this.random.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.random.nextFloat() * 0.2F);
                this.j = 0;
                this.an = 0;
            } else {
                ++this.an;
            }

            Vec3 vec3d = Vec3.a().create(this.posX, this.posY, this.posZ);
            Vec3 vec3d1 = Vec3.a().create(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.a(vec3d, vec3d1);

            vec3d = Vec3.a().create(this.posX, this.posY, this.posZ);
            vec3d1 = Vec3.a().create(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec3d1 = Vec3.a().create(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
            }

            net.minecraft.src.Entity entity = null;
            List list = this.worldObj.getEntities(this, this.boundingBox.a(this.motionX, this.motionY, this.motionZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                net.minecraft.src.Entity entity1 = (Entity) iterator.next();

                if (entity1.L() && (!entity1.i(this.shootingEntity) || this.an >= 25)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.grow((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.distanceSquared(movingobjectposition1.pos); // CraftBukkit - distance efficiency

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null) {
                this.a(movingobjectposition);

                // CraftBukkit start
                if (this.isDead) {
                    ProjectileHitEvent phe = new ProjectileHitEvent((org.bukkit.entity.Projectile) this.getBukkitEntity());
                    this.worldObj.getServer().getPluginManager().callEvent(phe);
                }
                // CraftBukkit end
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.1415927410125732D);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f1) * 180.0D / 3.1415927410125732D); this.rotationPitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
                ;
            }

            while (this.rotationPitch - this.lastPitch >= 180.0F) {
                this.lastPitch += 360.0F;
            }

            while (this.rotationYaw - this.lastYaw < -180.0F) {
                this.lastYaw -= 360.0F;
            }

            while (this.rotationYaw - this.lastYaw >= 180.0F) {
                this.lastYaw += 360.0F;
            }

            this.rotationPitch = this.lastPitch + (this.rotationPitch - this.lastPitch) * 0.2F;
            this.rotationYaw = this.lastYaw + (this.rotationYaw - this.lastYaw) * 0.2F;
            float f2 = 0.95F;

            if (this.H()) {
                for (int j = 0; j < 4; ++j) {
                    float f3 = 0.25F;

                    this.worldObj.a("bubble", this.posX - this.motionX * (double) f3, this.posY - this.motionY * (double) f3, this.posZ - this.motionZ * (double) f3, this.motionX, this.motionY, this.motionZ);
                }

                f2 = 0.8F;
            }

            this.motionX += this.accelerationX;
            this.motionY += this.accelerationY;
            this.motionZ += this.accelerationZ;
            this.motionX *= (double) f2;
            this.motionY *= (double) f2;
            this.motionZ *= (double) f2;
            this.worldObj.a("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.worldObj.isStatic) {
            if (movingobjectposition.entity != null) {
                movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shootingEntity), 6);
            }

            // CraftBukkit start
            ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(this.worldObj.getServer(), this));
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                // give 'this' instead of (Entity) null so we know what causes the damage
                this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, event.getRadius(), event.getFire());
            }
            // CraftBukkit end
            this.setDead();
        }
    }

    public void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.e);
        nbttagcompound.setShort("yTile", (short) this.f);
        nbttagcompound.setShort("zTile", (short) this.g);
        nbttagcompound.setByte("inTile", (byte) this.h);
        nbttagcompound.setByte("inGround", (byte) (this.i ? 1 : 0));
        nbttagcompound.set("direction", this.a(new double[] { this.motionX, this.motionY, this.motionZ}));
    }

    public void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getShort("xTile");
        this.f = nbttagcompound.getShort("yTile");
        this.g = nbttagcompound.getShort("zTile");
        this.h = nbttagcompound.getByte("inTile") & 255;
        this.i = nbttagcompound.getByte("inGround") == 1;
        if (nbttagcompound.hasKey("direction")) {
            NBTTagList nbttaglist = nbttagcompound.getList("direction");

            this.motionX = ((NBTTagDouble) nbttaglist.get(0)).data;
            this.motionY = ((NBTTagDouble) nbttaglist.get(1)).data;
            this.motionZ = ((NBTTagDouble) nbttaglist.get(2)).data;
        } else {
            this.setDead();
        }
    }

    public boolean L() {
        return true;
    }

    public float Y() {
        return 1.0F;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.K();
        if (damagesource.getEntity() != null) {
            Vec3 vec3d = damagesource.getEntity().Z();

            if (vec3d != null) {
                this.motionX = vec3d.a;
                this.motionY = vec3d.b;
                this.motionZ = vec3d.c;
                this.accelerationX = this.motionX * 0.1D;
                this.accelerationY = this.motionY * 0.1D;
                this.accelerationZ = this.motionZ * 0.1D;
            }

            if (damagesource.getEntity() instanceof net.minecraft.src.EntityLiving) {
                this.shootingEntity = (EntityLiving) damagesource.getEntity();
            }

            return true;
        } else {
            return false;
        }
    }

    public float c(float f) {
        return 1.0F;
    }
}
