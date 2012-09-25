package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.src.*;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;

import org.bukkit.event.entity.ProjectileHitEvent; // CraftBukkit

public abstract class EntityProjectile extends net.minecraft.src.Entity {

    private int blockX = -1;
    private int blockY = -1;
    private int blockZ = -1;
    private int inBlockId = 0;
    protected boolean inGround = false;
    public int shake = 0;
    public net.minecraft.src.EntityLiving shootingEntity; // CraftBukkit - protected -> public
    private int h;
    private int i = 0;

    public EntityProjectile(net.minecraft.src.World world) {
        super(world);
        this.a(0.25F, 0.25F);
    }

    protected void a() {}

    public EntityProjectile(net.minecraft.src.World world, EntityLiving entityliving) {
        super(world);
        this.shootingEntity = entityliving;
        this.a(0.25F, 0.25F);
        this.setPositionRotation(entityliving.posX, entityliving.posY + (double) entityliving.getHeadHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.height = 0.0F;
        float f = 0.4F;

        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.motionY = (double) (-MathHelper.sin((this.rotationPitch + this.g()) / 180.0F * 3.1415927F) * f);
        this.c(this.motionX, this.motionY, this.motionZ, this.d(), 1.0F);
    }

    public EntityProjectile(net.minecraft.src.World world, double d0, double d1, double d2) {
        super(world);
        this.h = 0;
        this.a(0.25F, 0.25F);
        this.setPosition(d0, d1, d2);
        this.height = 0.0F;
    }

    protected float d() {
        return 1.5F;
    }

    protected float g() {
        return 0.0F;
    }

    public void c(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= (double) f2;
        d1 /= (double) f2;
        d2 /= (double) f2;
        d0 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d1 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d2 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d0 *= (double) f;
        d1 *= (double) f;
        d2 *= (double) f;
        this.motionX = d0;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2);

        this.lastYaw = this.rotationYaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D);
        this.lastPitch = this.rotationPitch = (float) (Math.atan2(d1, (double) f3) * 180.0D / 3.1415927410125732D);
        this.h = 0;
    }

    public void h_() {
        this.S = this.posX;
        this.T = this.posY;
        this.U = this.posZ;
        super.h_();
        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            int i = this.worldObj.getBlockId(this.blockX, this.blockY, this.blockZ);

            if (i == this.inBlockId) {
                ++this.h;
                if (this.h == 1200) {
                    this.setDead();
                }

                return;
            }

            this.inGround = false;
            this.motionX *= (double) (this.random.nextFloat() * 0.2F);
            this.motionY *= (double) (this.random.nextFloat() * 0.2F);
            this.motionZ *= (double) (this.random.nextFloat() * 0.2F);
            this.h = 0;
            this.i = 0;
        } else {
            ++this.i;
        }

        Vec3 vec3d = Vec3.a().create(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3.a().create(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.a(vec3d, vec3d1);

        vec3d = Vec3.a().create(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3.a().create(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (movingobjectposition != null) {
            vec3d1 = Vec3.a().create(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
        }

        if (!this.worldObj.isStatic) {
            net.minecraft.src.Entity entity = null;
            List list = this.worldObj.getEntities(this, this.boundingBox.a(this.motionX, this.motionY, this.motionZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                net.minecraft.src.Entity entity1 = (Entity) iterator.next();

                if (entity1.L() && (entity1 != this.shootingEntity || this.i >= 5)) {
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
        }

        if (movingobjectposition != null) {
            this.a(movingobjectposition);
            // CraftBukkit start
            if (this.isDead) {
                ProjectileHitEvent hitEvent = new ProjectileHitEvent((org.bukkit.entity.Projectile) this.getBukkitEntity());
                org.bukkit.Bukkit.getPluginManager().callEvent(hitEvent);
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
        float f2 = 0.99F;
        float f3 = this.h();

        if (this.H()) {
            for (int j = 0; j < 4; ++j) {
                float f4 = 0.25F;

                this.worldObj.a("bubble", this.posX - this.motionX * (double) f4, this.posY - this.motionY * (double) f4, this.posZ - this.motionZ * (double) f4, this.motionX, this.motionY, this.motionZ);
            }

            f2 = 0.8F;
        }

        this.motionX *= (double) f2;
        this.motionY *= (double) f2;
        this.motionZ *= (double) f2;
        this.motionY -= (double) f3;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    protected float h() {
        return 0.03F;
    }

    protected abstract void a(MovingObjectPosition movingobjectposition);

    public void b(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.blockX);
        nbttagcompound.setShort("yTile", (short) this.blockY);
        nbttagcompound.setShort("zTile", (short) this.blockZ);
        nbttagcompound.setByte("inTile", (byte) this.inBlockId);
        nbttagcompound.setByte("shake", (byte) this.shake);
        nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
    }

    public void a(net.minecraft.src.NBTTagCompound nbttagcompound) {
        this.blockX = nbttagcompound.getShort("xTile");
        this.blockY = nbttagcompound.getShort("yTile");
        this.blockZ = nbttagcompound.getShort("zTile");
        this.inBlockId = nbttagcompound.getByte("inTile") & 255;
        this.shake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
    }
}
