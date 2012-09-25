package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start

import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
// CraftBukkit end

public class EntityArrow extends Entity {

    private int d = -1;
    private int e = -1;
    private int f = -1;
    private int g = 0;
    private int h = 0;
    private boolean inGround = false;
    public int fromPlayer = 0;
    public int shake = 0;
    public Entity shootingEntity;
    private int j;
    private int an = 0;
    private double damage = 2.0D;
    private int ap;

    public EntityArrow(net.minecraft.src.World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
    }

    public EntityArrow(net.minecraft.src.World world, double d0, double d1, double d2) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.setPosition(d0, d1, d2);
        this.yOffset = 0.0F;
    }

    public EntityArrow(net.minecraft.src.World world, net.minecraft.src.EntityLiving entityliving, net.minecraft.src.EntityLiving entityliving1, float f, float f1) {
        super(world);
        this.shootingEntity = entityliving;
        if (entityliving instanceof net.minecraft.src.EntityPlayer) {
            this.fromPlayer = 1;
        }

        this.posY = entityliving.posY + (double) entityliving.getHeadHeight() - 0.10000000149011612D;
        double d0 = entityliving1.posX - entityliving.posX;
        double d1 = entityliving1.posY + (double) entityliving1.getHeadHeight() - 0.699999988079071D - this.posY;
        double d2 = entityliving1.posZ - entityliving.posZ;
        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D) {
            float f2 = (float) (Math.atan2(d2, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
            float f3 = (float) (-(Math.atan2(d1, d3) * 180.0D / 3.1415927410125732D));
            double d4 = d0 / d3;
            double d5 = d2 / d3;

            this.setPositionRotation(entityliving.posX + d4, this.posY, entityliving.posZ + d5, f2, f3);
            this.yOffset = 0.0F;
            float f4 = (float) d3 * 0.2F;

            this.shoot(d0, d1 + (double) f4, d2, f, f1);
        }
    }

    public EntityArrow(net.minecraft.src.World world, net.minecraft.src.EntityLiving entityliving, float f) {
        super(world);
        this.shootingEntity = entityliving;
        if (entityliving instanceof net.minecraft.src.EntityPlayer) {
            this.fromPlayer = 1;
        }

        this.setSize(0.5F, 0.5F);
        this.setPositionRotation(entityliving.posX, entityliving.posY + (double) entityliving.getHeadHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F));
        this.shoot(this.motionX, this.motionY, this.motionZ, f * 1.5F, 1.0F);
    }

    protected void entityInit() {
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void shoot(double d0, double d1, double d2, float f, float f1) {
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
        this.j = 0;
    }

    public void h_() {
        super.h_();
        if (this.lastPitch == 0.0F && this.lastYaw == 0.0F) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            this.lastYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.1415927410125732D);
            this.lastPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f) * 180.0D / 3.1415927410125732D);
        }

        int i = this.worldObj.getBlockId(this.d, this.e, this.f);

        if (i > 0) {
            Block.blocksList[i].updateShape(this.worldObj, this.d, this.e, this.f);
            AxisAlignedBB axisalignedbb = Block.blocksList[i].e(this.worldObj, this.d, this.e, this.f);

            if (axisalignedbb != null && axisalignedbb.a(Vec3.a().create(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.shake > 0) {
            --this.shake;
        }

        if (this.inGround) {
            int j = this.worldObj.getBlockId(this.d, this.e, this.f);
            int k = this.worldObj.getData(this.d, this.e, this.f);

            if (j == this.g && k == this.h) {
                ++this.j;
                if (this.j == 1200) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= (double) (this.random.nextFloat() * 0.2F);
                this.motionY *= (double) (this.random.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.random.nextFloat() * 0.2F);
                this.j = 0;
                this.an = 0;
            }
        } else {
            ++this.an;
            Vec3 vec3d = Vec3.a().create(this.posX, this.posY, this.posZ);
            Vec3 vec3d1 = Vec3.a().create(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTrace(vec3d, vec3d1, false, true);

            vec3d = Vec3.a().create(this.posX, this.posY, this.posZ);
            vec3d1 = Vec3.a().create(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec3d1 = Vec3.a().create(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
            }

            Entity entity = null;
            List list = this.worldObj.getEntities(this, this.boundingBox.a(this.motionX, this.motionY, this.motionZ).grow(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            Iterator iterator = list.iterator();

            float f1;

            while (iterator.hasNext()) {
                Entity entity1 = (Entity) iterator.next();

                if (entity1.L() && (entity1 != this.shootingEntity || this.an >= 5)) {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.grow((double) f1, (double) f1, (double) f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.a(vec3d, vec3d1);

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

            float f2;

            if (movingobjectposition != null) {
                // CraftBukkit start
                Projectile projectile = (Projectile) this.getBukkitEntity();
                ProjectileHitEvent phe = new ProjectileHitEvent(projectile);
                this.worldObj.getServer().getPluginManager().callEvent(phe);
                // CraftBukkit end
                if (movingobjectposition.entity != null) {
                    f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int l = MathHelper.f((double) f2 * this.damage);

                    if (this.g()) {
                        l += this.random.nextInt(l / 2 + 2);
                    }

                    DamageSource damagesource = null;

                    if (this.shootingEntity == null) {
                        damagesource = DamageSource.arrow(this, this);
                    } else {
                        damagesource = DamageSource.arrow(this, this.shootingEntity);
                    }

                    // CraftBukkit start - moved damage call
                    if (movingobjectposition.entity.damageEntity(damagesource, l)) {
                    if (this.isBurning() && (!(movingobjectposition.entity instanceof EntityPlayerMP) || this.worldObj.pvpMode)) { // CraftBukkit - abide by pvp setting if destination is a player.
                        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 5);
                        org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                        if (!combustEvent.isCancelled()) {
                            movingobjectposition.entity.setOnFire(combustEvent.getDuration());
                        }
                        // CraftBukkit end
                    }

                    // if (movingobjectposition.entity.damageEntity(damagesource, l)) { // CraftBukkit - moved up
                        if (movingobjectposition.entity instanceof net.minecraft.src.EntityLiving) {
                            ++((EntityLiving) movingobjectposition.entity).bd;
                            if (this.ap > 0) {
                                float f3 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                if (f3 > 0.0F) {
                                    movingobjectposition.entity.g(this.motionX * (double) this.ap * 0.6000000238418579D / (double) f3, 0.1D, this.motionZ * (double) this.ap * 0.6000000238418579D / (double) f3);
                                }
                            }
                        }

                        this.worldObj.makeSound(this, "random.bowhit", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                        this.setDead();
                    } else {
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.rotationYaw += 180.0F;
                        this.lastYaw += 180.0F;
                        this.an = 0;
                    }
                } else {
                    this.d = movingobjectposition.b;
                    this.e = movingobjectposition.c;
                    this.f = movingobjectposition.d;
                    this.g = this.worldObj.getBlockId(this.d, this.e, this.f);
                    this.h = this.worldObj.getData(this.d, this.e, this.f);
                    this.motionX = (double) ((float) (movingobjectposition.pos.a - this.posX));
                    this.motionY = (double) ((float) (movingobjectposition.pos.b - this.posY));
                    this.motionZ = (double) ((float) (movingobjectposition.pos.c - this.posZ));
                    f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
                    this.worldObj.makeSound(this, "random.bowhit", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.shake = 7;
                    this.d(false);
                }
            }

            if (this.g()) {
                for (int i1 = 0; i1 < 4; ++i1) {
                    this.worldObj.a("crit", this.posX + this.motionX * (double) i1 / 4.0D, this.posY + this.motionY * (double) i1 / 4.0D, this.posZ + this.motionZ * (double) i1 / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.1415927410125732D);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) f2) * 180.0D / 3.1415927410125732D); this.rotationPitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
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
            float f4 = 0.99F;

            f1 = 0.05F;
            if (this.H()) {
                for (int j1 = 0; j1 < 4; ++j1) {
                    float f5 = 0.25F;

                    this.worldObj.a("bubble", this.posX - this.motionX * (double) f5, this.posY - this.motionY * (double) f5, this.posZ - this.motionZ * (double) f5, this.motionX, this.motionY, this.motionZ);
                }

                f4 = 0.8F;
            }

            this.motionX *= (double) f4;
            this.motionY *= (double) f4;
            this.motionZ *= (double) f4;
            this.motionY -= (double) f1;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.D();
        }
    }

    public void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.d);
        nbttagcompound.setShort("yTile", (short) this.e);
        nbttagcompound.setShort("zTile", (short) this.f);
        nbttagcompound.setByte("inTile", (byte) this.g);
        nbttagcompound.setByte("inData", (byte) this.h);
        nbttagcompound.setByte("shake", (byte) this.shake);
        nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        nbttagcompound.setByte("pickup", (byte) this.fromPlayer);
        nbttagcompound.setDouble("damage", this.damage);
    }

    public void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getShort("xTile");
        this.e = nbttagcompound.getShort("yTile");
        this.f = nbttagcompound.getShort("zTile");
        this.g = nbttagcompound.getByte("inTile") & 255;
        this.h = nbttagcompound.getByte("inData") & 255;
        this.shake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
        if (nbttagcompound.hasKey("damage")) {
            this.damage = nbttagcompound.getDouble("damage");
        }

        if (nbttagcompound.hasKey("pickup")) {
            this.fromPlayer = nbttagcompound.getByte("pickup");
        } else if (nbttagcompound.hasKey("player")) {
            this.fromPlayer = nbttagcompound.getBoolean("player") ? 1 : 0;
        }
    }

    public void b_(EntityPlayer entityhuman) {
        if (!this.worldObj.isStatic && this.inGround && this.shake <= 0) {
            // CraftBukkit start
            net.minecraft.src.ItemStack itemstack = new net.minecraft.src.ItemStack(Item.ARROW);
            if (this.inGround && this.fromPlayer == 1 && this.shake <= 0 && entityhuman.inventory.canHold(itemstack) > 0) {
                net.minecraft.src.EntityItem item = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, itemstack);

                PlayerPickupItemEvent event = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), new org.bukkit.craftbukkit.entity.CraftItem(this.worldObj.getServer(), this, item), 0);
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
            }
            // CraftBukkit end

            boolean flag = this.fromPlayer == 1 || this.fromPlayer == 2 && entityhuman.capabilities.canInstantlyBuild;

            if (this.fromPlayer == 1 && !entityhuman.inventory.pickup(new ItemStack(Item.ARROW, 1))) {
                flag = false;
            }

            if (flag) {
                this.worldObj.makeSound(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityhuman.receive(this, 1);
                this.setDead();
            }
        }
    }

    public void b(double d0) {
        this.damage = d0;
    }

    public double d() {
        return this.damage;
    }

    public void a(int i) {
        this.ap = i;
    }

    public boolean an() {
        return false;
    }

    public void d(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public boolean g() {
        byte b0 = this.datawatcher.getByte(16);

        return (b0 & 1) != 0;
    }
}
