package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
// CraftBukkit end

public class EntityFishingHook extends Entity {

    private int d = -1;
    private int e = -1;
    private int f = -1;
    private int g = 0;
    private boolean h = false;
    public int a = 0;
    public net.minecraft.src.EntityPlayer angler;
    private int i;
    private int j = 0;
    private int an = 0;
    public Entity hooked = null;
    private int ao;
    private double ap;
    private double aq;
    private double ar;
    private double as;
    private double at;

    public EntityFishingHook(net.minecraft.src.World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
        this.ak = true;
    }

    public EntityFishingHook(net.minecraft.src.World world, EntityPlayer entityhuman) {
        super(world);
        this.ak = true;
        this.angler = entityhuman;
        this.angler.hookedFish = this;
        this.setSize(0.25F, 0.25F);
        this.setPositionRotation(entityhuman.posX, entityhuman.posY + 1.62D - (double) entityhuman.yOffset, entityhuman.posZ, entityhuman.rotationYaw, entityhuman.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        float f = 0.4F;

        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f);
        this.c(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    protected void entityInit() {}

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
        this.i = 0;
    }

    public void h_() {
        super.h_();
        if (this.ao > 0) {
            double d0 = this.posX + (this.ap - this.posX) / (double) this.ao;
            double d1 = this.posY + (this.aq - this.posY) / (double) this.ao;
            double d2 = this.posZ + (this.ar - this.posZ) / (double) this.ao;
            double d3 = MathHelper.g(this.as - (double) this.rotationYaw);

            this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.ao);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.at - (double) this.rotationPitch) / (double) this.ao);
            --this.ao;
            this.setPosition(d0, d1, d2);
            this.b(this.rotationYaw, this.rotationPitch);
        } else {
            if (!this.worldObj.isStatic) {
                net.minecraft.src.ItemStack itemstack = this.angler.bC();

                if (this.angler.isDead || !this.angler.isEntityAlive() || itemstack == null || itemstack.getItem() != Item.FISHING_ROD || this.e(this.angler) > 1024.0D) {
                    this.setDead();
                    this.angler.hookedFish = null;
                    return;
                }

                if (this.hooked != null) {
                    if (!this.hooked.isDead) {
                        this.posX = this.hooked.posX;
                        this.posY = this.hooked.boundingBox.b + (double) this.hooked.length * 0.8D;
                        this.posZ = this.hooked.posZ;
                        return;
                    }

                    this.hooked = null;
                }
            }

            if (this.a > 0) {
                --this.a;
            }

            if (this.h) {
                int i = this.worldObj.getBlockId(this.d, this.e, this.f);

                if (i == this.g) {
                    ++this.i;
                    if (this.i == 1200) {
                        this.setDead();
                    }

                    return;
                }

                this.h = false;
                this.motionX *= (double) (this.random.nextFloat() * 0.2F);
                this.motionY *= (double) (this.random.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.random.nextFloat() * 0.2F);
                this.i = 0;
                this.j = 0;
            } else {
                ++this.j;
            }

            Vec3 vec3d = Vec3.a().create(this.posX, this.posY, this.posZ);
            Vec3 vec3d1 = Vec3.a().create(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.a(vec3d, vec3d1);

            vec3d = Vec3.a().create(this.posX, this.posY, this.posZ);
            vec3d1 = Vec3.a().create(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec3d1 = Vec3.a().create(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
            }

            Entity entity = null;
            List list = this.worldObj.getEntities(this, this.boundingBox.a(this.motionX, this.motionY, this.motionZ).grow(1.0D, 1.0D, 1.0D));
            double d4 = 0.0D;
            Iterator iterator = list.iterator();

            double d5;

            while (iterator.hasNext()) {
                Entity entity1 = (Entity) iterator.next();

                if (entity1.L() && (entity1 != this.angler || this.j >= 5)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.grow((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        d5 = vec3d.distanceSquared(movingobjectposition1.pos); // CraftBukkit - distance efficiency
                        if (d5 < d4 || d4 == 0.0D) {
                            entity = entity1;
                            d4 = d5;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            if (movingobjectposition != null) {
                if (movingobjectposition.entity != null) {
                    if (movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.angler), 0)) {
                        this.hooked = movingobjectposition.entity;
                    }
                } else {
                    this.h = true;
                }
            }

            if (!this.h) {
                this.move(this.motionX, this.motionY, this.motionZ);
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
                float f2 = 0.92F;

                if (this.onGround || this.positionChanged) {
                    f2 = 0.5F;
                }

                byte b0 = 5;
                double d6 = 0.0D;

                for (int j = 0; j < b0; ++j) {
                    double d7 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (j + 0) / (double) b0 - 0.125D + 0.125D;
                    double d8 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (j + 1) / (double) b0 - 0.125D + 0.125D;
                    AxisAlignedBB axisalignedbb1 = AxisAlignedBB.a().a(this.boundingBox.a, d7, this.boundingBox.c, this.boundingBox.d, d8, this.boundingBox.f);

                    if (this.worldObj.b(axisalignedbb1, Material.WATER)) {
                        d6 += 1.0D / (double) b0;
                    }
                }

                if (d6 > 0.0D) {
                    if (this.an > 0) {
                        --this.an;
                    } else {
                        short short1 = 500;

                        if (this.worldObj.B(MathHelper.floor(this.posX), MathHelper.floor(this.posY) + 1, MathHelper.floor(this.posZ))) {
                            short1 = 300;
                        }

                        if (this.random.nextInt(short1) == 0) {
                            this.an = this.random.nextInt(30) + 10;
                            this.motionY -= 0.20000000298023224D;
                            this.worldObj.makeSound(this, "random.splash", 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                            float f3 = (float) MathHelper.floor(this.boundingBox.b);

                            float f4;
                            int k;
                            float f5;

                            for (k = 0; (float) k < 1.0F + this.width * 20.0F; ++k) {
                                f5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                                f4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                                this.worldObj.a("bubble", this.posX + (double) f5, (double) (f3 + 1.0F), this.posZ + (double) f4, this.motionX, this.motionY - (double) (this.random.nextFloat() * 0.2F), this.motionZ);
                            }

                            for (k = 0; (float) k < 1.0F + this.width * 20.0F; ++k) {
                                f5 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                                f4 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                                this.worldObj.a("splash", this.posX + (double) f5, (double) (f3 + 1.0F), this.posZ + (double) f4, this.motionX, this.motionY, this.motionZ);
                            }
                        }
                    }
                }

                if (this.an > 0) {
                    this.motionY -= (double) (this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat()) * 0.2D;
                }

                d5 = d6 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * d5;
                if (d6 > 0.0D) {
                    f2 = (float) ((double) f2 * 0.9D);
                    this.motionY *= 0.8D;
                }

                this.motionX *= (double) f2;
                this.motionY *= (double) f2;
                this.motionZ *= (double) f2;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
    }

    public void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.d);
        nbttagcompound.setShort("yTile", (short) this.e);
        nbttagcompound.setShort("zTile", (short) this.f);
        nbttagcompound.setByte("inTile", (byte) this.g);
        nbttagcompound.setByte("shake", (byte) this.a);
        nbttagcompound.setByte("inGround", (byte) (this.h ? 1 : 0));
    }

    public void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getShort("xTile");
        this.e = nbttagcompound.getShort("yTile");
        this.f = nbttagcompound.getShort("zTile");
        this.g = nbttagcompound.getByte("inTile") & 255;
        this.a = nbttagcompound.getByte("shake") & 255;
        this.h = nbttagcompound.getByte("inGround") == 1;
    }

    public int d() {
        if (this.worldObj.isStatic) {
            return 0;
        } else {
            byte b0 = 0;

            if (this.hooked != null) {
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), this.hooked.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_ENTITY);
                this.worldObj.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    this.setDead();
                    this.angler.hookedFish = null;
                    return 0;
                }
                // CraftBukkit end

                double d0 = this.angler.posX - this.posX;
                double d1 = this.angler.posY - this.posY;
                double d2 = this.angler.posZ - this.posZ;
                double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                double d4 = 0.1D;

                this.hooked.motionX += d0 * d4;
                this.hooked.motionY += d1 * d4 + (double) MathHelper.sqrt(d3) * 0.08D;
                this.hooked.motionZ += d2 * d4;
                b0 = 3;
            } else if (this.an > 0) {
                net.minecraft.src.EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.RAW_FISH));
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), entityitem.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_FISH);
                this.worldObj.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    this.setDead();
                    this.angler.hookedFish = null;
                    return 0;
                }
                // CraftBukkit end

                double d5 = this.angler.posX - this.posX;
                double d6 = this.angler.posY - this.posY;
                double d7 = this.angler.posZ - this.posZ;
                double d8 = (double) MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
                double d9 = 0.1D;

                entityitem.motionX = d5 * d9;
                entityitem.motionY = d6 * d9 + (double) MathHelper.sqrt(d8) * 0.08D;
                entityitem.motionZ = d7 * d9;
                this.worldObj.addEntity(entityitem);
                this.angler.a(StatisticList.B, 1);
                b0 = 1;
            }

            if (this.h) {
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), null, PlayerFishEvent.State.IN_GROUND);
                this.worldObj.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    this.setDead();
                    this.angler.hookedFish = null;
                    return 0;
                }
                // CraftBukkit end

                b0 = 2;
            }

            // CraftBukkit start
            if (b0 == 0) {
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), null, PlayerFishEvent.State.FAILED_ATTEMPT);
                this.worldObj.getServer().getPluginManager().callEvent(playerFishEvent);
            }
            // CraftBukkit end

            this.setDead();
            this.angler.hookedFish = null;
            return b0;
        }
    }

    public void setDead() {
        super.setDead();
        if (this.angler != null) {
            this.angler.hookedFish = null;
        }
    }
}
