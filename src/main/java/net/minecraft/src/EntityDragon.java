package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.BlockEnderPortal;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.MathHelper;
import net.minecraft.server.Packet53BlockChange;
import net.minecraft.src.*;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityEnderCrystal;
import net.minecraft.src.EntityLiving;

import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.Bukkit;
// CraftBukkit end

public class EntityDragon extends EntityDragonBase {

    public double b;
    public double c;
    public double d;
    public double[][] e = new double[64][3];
    public int f = -1;
    public EntityComplexPart[] children;
    public EntityComplexPart h;
    public EntityComplexPart i;
    public EntityComplexPart j;
    public EntityComplexPart by;
    public EntityComplexPart bz;
    public EntityComplexPart bA;
    public EntityComplexPart bB;
    public float bC = 0.0F;
    public float bD = 0.0F;
    public boolean bE = false;
    public boolean bF = false;
    private net.minecraft.src.Entity bI;
    public int bG = 0;
    public net.minecraft.src.EntityEnderCrystal bH = null;

    public EntityDragon(net.minecraft.src.World world) {
        super(world);
        this.children = new EntityComplexPart[] { this.h = new EntityComplexPart(this, "head", 6.0F, 6.0F), this.i = new EntityComplexPart(this, "body", 8.0F, 8.0F), this.j = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.by = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.bz = new EntityComplexPart(this, "tail", 4.0F, 4.0F), this.bA = new EntityComplexPart(this, "wing", 4.0F, 4.0F), this.bB = new EntityComplexPart(this, "wing", 4.0F, 4.0F)};
        this.a = 200;
        this.setHealth(this.a);
        this.texture = "/mob/enderdragon/ender.png";
        this.a(16.0F, 8.0F);
        this.X = true;
        this.fireProof = true;
        this.c = 100.0D;
        this.ak = true;
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, new Integer(this.a));
    }

    public double[] a(int i, float f) {
        if (this.health <= 0) {
            f = 0.0F;
        }

        f = 1.0F - f;
        int j = this.f - i * 1 & 63;
        int k = this.f - i * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.e[j][0];
        double d1 = MathHelper.g(this.e[k][0] - d0);

        adouble[0] = d0 + d1 * (double) f;
        d0 = this.e[j][1];
        d1 = this.e[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.e[j][2] + (this.e[k][2] - this.e[j][2]) * (double) f;
        return adouble;
    }

    public void d() {
        this.bC = this.bD;
        if (!this.worldObj.isStatic) {
            this.datawatcher.watch(16, Integer.valueOf(this.health));
        }

        float f;
        float f1;
        float d05;

        if (this.health <= 0) {
            f = (this.random.nextFloat() - 0.5F) * 8.0F;
            d05 = (this.random.nextFloat() - 0.5F) * 4.0F;
            f1 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.worldObj.a("largeexplode", this.posX + (double) f, this.posY + 2.0D + (double) d05, this.posZ + (double) f1, 0.0D, 0.0D, 0.0D);
        } else {
            this.j();
            f = 0.2F / (MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
            f *= (float) Math.pow(2.0D, this.motionY);
            if (this.bF) {
                this.bD += f * 0.5F;
            } else {
                this.bD += f;
            }

            this.rotationYaw = MathHelper.g(this.rotationYaw);
            if (this.f < 0) {
                for (int i = 0; i < this.e.length; ++i) {
                    this.e[i][0] = (double) this.rotationYaw;
                    this.e[i][1] = this.posY;
                }
            }

            if (++this.f == this.e.length) {
                this.f = 0;
            }

            this.e[this.f][0] = (double) this.rotationYaw;
            this.e[this.f][1] = this.posY;
            double d0;
            double d1;
            double d2;
            double d3;
            float f3;

            if (this.worldObj.isStatic) {
                if (this.bi > 0) {
                    d0 = this.posX + (this.bj - this.posX) / (double) this.bi;
                    d1 = this.posY + (this.bk - this.posY) / (double) this.bi;
                    d2 = this.posZ + (this.bl - this.posZ) / (double) this.bi;
                    d3 = MathHelper.g(this.bm - (double) this.rotationYaw);
                    this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.bi);
                    this.rotationPitch = (float) ((double) this.rotationPitch + (this.bn - (double) this.rotationPitch) / (double) this.bi);
                    --this.bi;
                    this.setPosition(d0, d1, d2);
                    this.b(this.rotationYaw, this.rotationPitch);
                }
            } else {
                d0 = this.b - this.posX;
                d1 = this.c - this.posY;
                d2 = this.d - this.posZ;
                d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (this.bI != null) {
                    this.b = this.bI.posX;
                    this.d = this.bI.posZ;
                    double d4 = this.b - this.posX;
                    double d5 = this.d - this.posZ;
                    double d6 = Math.sqrt(d4 * d4 + d5 * d5);
                    double d7 = 0.4000000059604645D + d6 / 80.0D - 1.0D;

                    if (d7 > 10.0D) {
                        d7 = 10.0D;
                    }

                    this.c = this.bI.boundingBox.b + d7;
                } else {
                    this.b += this.random.nextGaussian() * 2.0D;
                    this.d += this.random.nextGaussian() * 2.0D;
                }

                if (this.bE || d3 < 100.0D || d3 > 22500.0D || this.positionChanged || this.G) {
                    this.k();
                }

                d1 /= (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                f3 = 0.6F;
                if (d1 < (double) (-f3)) {
                    d1 = (double) (-f3);
                }

                if (d1 > (double) f3) {
                    d1 = (double) f3;
                }

                this.motionY += d1 * 0.10000000149011612D;
                this.rotationYaw = MathHelper.g(this.rotationYaw);
                double d8 = 180.0D - Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D;
                double d9 = MathHelper.g(d8 - (double) this.rotationYaw);

                if (d9 > 50.0D) {
                    d9 = 50.0D;
                }

                if (d9 < -50.0D) {
                    d9 = -50.0D;
                }

                Vec3 vec3d = Vec3.a().create(this.b - this.posX, this.c - this.posY, this.d - this.posZ).b();
                Vec3 vec3d1 = Vec3.a().create((double) MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F), this.motionY, (double) (-MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F))).b();
                float f4 = (float) (vec3d1.b(vec3d) + 0.5D) / 1.5F;

                if (f4 < 0.0F) {
                    f4 = 0.0F;
                }

                this.bt *= 0.8F;
                float f5 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
                double d10 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;

                if (d10 > 40.0D) {
                    d10 = 40.0D;
                }

                this.bt = (float) ((double) this.bt + d9 * (0.699999988079071D / d10 / (double) f5));
                this.rotationYaw += this.bt * 0.1F;
                float f6 = (float) (2.0D / (d10 + 1.0D));
                float f7 = 0.06F;

                this.a(0.0F, -1.0F, f7 * (f4 * f6 + (1.0F - f6)));
                if (this.bF) {
                    this.move(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
                } else {
                    this.move(this.motionX, this.motionY, this.motionZ);
                }

                Vec3 vec3d2 = Vec3.a().create(this.motionX, this.motionY, this.motionZ).b();
                float f8 = (float) (vec3d2.b(vec3d1) + 1.0D) / 2.0F;

                f8 = 0.8F + 0.15F * f8;
                this.motionX *= (double) f8;
                this.motionZ *= (double) f8;
                this.motionY *= 0.9100000262260437D;
            }

            this.aq = this.rotationYaw;
            this.h.width = this.h.length = 3.0F;
            this.j.width = this.j.length = 2.0F;
            this.by.width = this.by.length = 2.0F;
            this.bz.width = this.bz.length = 2.0F;
            this.i.length = 3.0F;
            this.i.width = 5.0F;
            this.bA.length = 2.0F;
            this.bA.width = 4.0F;
            this.bB.length = 3.0F;
            this.bB.width = 4.0F;
            d05 = (float) (this.a(5, 1.0F)[1] - this.a(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
            f1 = MathHelper.cos(d05);
            float f9 = -MathHelper.sin(d05);
            float f10 = this.rotationYaw * 3.1415927F / 180.0F;
            float f11 = MathHelper.sin(f10);
            float f12 = MathHelper.cos(f10);

            this.i.h_();
            this.i.setPositionRotation(this.posX + (double) (f11 * 0.5F), this.posY, this.posZ - (double) (f12 * 0.5F), 0.0F, 0.0F);
            this.bA.h_();
            this.bA.setPositionRotation(this.posX + (double) (f12 * 4.5F), this.posY + 2.0D, this.posZ + (double) (f11 * 4.5F), 0.0F, 0.0F);
            this.bB.h_();
            this.bB.setPositionRotation(this.posX - (double) (f12 * 4.5F), this.posY + 2.0D, this.posZ - (double) (f11 * 4.5F), 0.0F, 0.0F);
            if (!this.worldObj.isStatic && this.hurtTicks == 0) {
                this.a(this.worldObj.getEntities(this, this.bA.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
                this.a(this.worldObj.getEntities(this, this.bB.boundingBox.grow(4.0D, 2.0D, 4.0D).d(0.0D, -2.0D, 0.0D)));
                this.b(this.worldObj.getEntities(this, this.h.boundingBox.grow(1.0D, 1.0D, 1.0D)));
            }

            double[] adouble = this.a(5, 1.0F);
            double[] adouble1 = this.a(0, 1.0F);

            f3 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F - this.bt * 0.01F);
            float f13 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F - this.bt * 0.01F);

            this.h.h_();
            this.h.setPositionRotation(this.posX + (double) (f3 * 5.5F * f1), this.posY + (adouble1[1] - adouble[1]) * 1.0D + (double) (f9 * 5.5F), this.posZ - (double) (f13 * 5.5F * f1), 0.0F, 0.0F);

            for (int j = 0; j < 3; ++j) {
                EntityComplexPart entitycomplexpart = null;

                if (j == 0) {
                    entitycomplexpart = this.j;
                }

                if (j == 1) {
                    entitycomplexpart = this.by;
                }

                if (j == 2) {
                    entitycomplexpart = this.bz;
                }

                double[] adouble2 = this.a(12 + j * 2, 1.0F);
                float f14 = this.rotationYaw * 3.1415927F / 180.0F + this.b(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F;
                float f15 = MathHelper.sin(f14);
                float f16 = MathHelper.cos(f14);
                float f17 = 1.5F;
                float f18 = (float) (j + 1) * 2.0F;

                entitycomplexpart.h_();
                entitycomplexpart.setPositionRotation(this.posX - (double) ((f11 * f17 + f15 * f18) * f1), this.posY + (adouble2[1] - adouble[1]) * 1.0D - (double) ((f18 + f17) * f9) + 1.5D, this.posZ + (double) ((f12 * f17 + f16 * f18) * f1), 0.0F, 0.0F);
            }

            if (!this.worldObj.isStatic) {
                this.bF = this.a(this.h.boundingBox) | this.a(this.i.boundingBox);
            }
        }
    }

    private void j() {
        if (this.bH != null) {
            if (this.bH.isDead) {
                if (!this.worldObj.isStatic) {
                    this.a(this.h, DamageSource.EXPLOSION, 10);
                }

                this.bH = null;
            } else if (this.ticksExisted % 10 == 0 && this.health < this.a) {
                // CraftBukkit start
                EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), 1, EntityRegainHealthEvent.RegainReason.ENDER_CRYSTAL);
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.health += event.getAmount();
                }
                // CraftBukkit end
            }
        }

        if (this.random.nextInt(10) == 0) {
            float f = 32.0F;
            List list = this.worldObj.a(net.minecraft.src.EntityEnderCrystal.class, this.boundingBox.grow((double) f, (double) f, (double) f));
            net.minecraft.src.EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                net.minecraft.src.EntityEnderCrystal entityendercrystal1 = (EntityEnderCrystal) iterator.next();
                double d1 = entityendercrystal1.e(this);

                if (d1 < d0) {
                    d0 = d1;
                    entityendercrystal = entityendercrystal1;
                }
            }

            this.bH = entityendercrystal;
        }
    }

    private void a(List list) {
        double d0 = (this.i.boundingBox.a + this.i.boundingBox.d) / 2.0D;
        double d1 = (this.i.boundingBox.c + this.i.boundingBox.f) / 2.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            net.minecraft.src.Entity entity = (net.minecraft.src.Entity) iterator.next();

            if (entity instanceof net.minecraft.src.EntityLiving) {
                double d2 = entity.posX - d0;
                double d3 = entity.posZ - d1;
                double d4 = d2 * d2 + d3 * d3;

                entity.g(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
            }
        }
    }

    private void b(List list) {
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            net.minecraft.src.Entity entity = (net.minecraft.src.Entity) iterator.next();

            if (entity instanceof EntityLiving) {
                // CraftBukkit start - throw damage events when the dragon attacks
                // The EntityHuman case is handled in EntityHuman, so don't throw it here
                if (!(entity instanceof net.minecraft.src.EntityPlayer)) {
                    EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK, 10);
                    Bukkit.getPluginManager().callEvent(damageEvent);

                    if (!damageEvent.isCancelled()) {
                        entity.getBukkitEntity().setLastDamageCause(damageEvent);
                        entity.damageEntity(DamageSource.mobAttack(this), damageEvent.getDamage());
                    }
                } else {
                    entity.damageEntity(DamageSource.mobAttack(this), 10);
                }
                // CraftBukkit end
            }
        }
    }

    private void k() {
        this.bE = false;
        if (this.random.nextInt(2) == 0 && !this.worldObj.players.isEmpty()) {
            this.bI = (net.minecraft.src.Entity) this.worldObj.players.get(this.random.nextInt(this.worldObj.players.size()));
        } else {
            boolean flag = false;

            do {
                this.b = 0.0D;
                this.c = (double) (70.0F + this.random.nextFloat() * 50.0F);
                this.d = 0.0D;
                this.b += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                this.d += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                double d0 = this.posX - this.b;
                double d1 = this.posY - this.c;
                double d2 = this.posZ - this.d;

                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            } while (!flag);

            this.bI = null;
        }
    }

    private float b(double d0) {
        return (float) MathHelper.g(d0);
    }

    private boolean a(AxisAlignedBB axisalignedbb) {
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.b);
        int k = MathHelper.floor(axisalignedbb.c);
        int l = MathHelper.floor(axisalignedbb.d);
        int i1 = MathHelper.floor(axisalignedbb.e);
        int j1 = MathHelper.floor(axisalignedbb.f);
        boolean flag = false;
        boolean flag1 = false;

        // CraftBukkit start - create a list to hold all the destroyed blocks
        List<org.bukkit.block.Block> destroyedBlocks = new java.util.ArrayList<org.bukkit.block.Block>();
        org.bukkit.craftbukkit.CraftWorld craftWorld = this.worldObj.getWorld();
        // CraftBukkit end

        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    int j2 = this.worldObj.getBlockId(k1, l1, i2);

                    if (j2 != 0) {
                        if (j2 != Block.obsidian.blockID && j2 != Block.WHITESTONE.blockID && j2 != Block.BEDROCK.blockID) {
                            flag1 = true;
                            // CraftBukkit start - add blocks to list rather than destroying them
                            // this.world.setBlockWithNotify(k1, l1, i2, 0);
                            destroyedBlocks.add(craftWorld.getBlockAt(k1, l1, i2));
                            // CraftBukkit end
                        } else {
                            flag = true;
                        }
                    }
                }
            }
        }

        if (flag1) {
            // CraftBukkit start - set off an EntityExplodeEvent for the dragon exploding all these blocks
            org.bukkit.entity.Entity bukkitEntity = this.getBukkitEntity();
            EntityExplodeEvent event = new EntityExplodeEvent(bukkitEntity, bukkitEntity.getLocation(), destroyedBlocks, 0F);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                // this flag literally means 'Dragon hit something hard' (Obsidian, White Stone or Bedrock) and will cause the dragon to slow down.
                // We should consider adding an event extension for it, or perhaps returning true if the event is cancelled.
                return flag;
            } else {
                for (org.bukkit.block.Block block : event.blockList()) {
                    craftWorld.explodeBlock(block, event.getYield());
                }
            }
            // CraftBukkit end

            double d0 = axisalignedbb.a + (axisalignedbb.d - axisalignedbb.a) * (double) this.random.nextFloat();
            double d1 = axisalignedbb.b + (axisalignedbb.e - axisalignedbb.b) * (double) this.random.nextFloat();
            double d2 = axisalignedbb.c + (axisalignedbb.f - axisalignedbb.c) * (double) this.random.nextFloat();

            this.worldObj.a("largeexplode", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }

        return flag;
    }

    public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
        if (entitycomplexpart != this.h) {
            i = i / 4 + 1;
        }

        float f = this.rotationYaw * 3.1415927F / 180.0F;
        float f1 = MathHelper.sin(f);
        float f2 = MathHelper.cos(f);

        this.b = this.posX + (double) (f1 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.c = this.posY + (double) (this.random.nextFloat() * 3.0F) + 1.0D;
        this.d = this.posZ - (double) (f2 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.bI = null;
        if (damagesource.getEntity() instanceof net.minecraft.src.EntityPlayer || damagesource == DamageSource.EXPLOSION) {
            this.dealDamage(damagesource, i);
        }

        return true;
    }

    protected void aI() {
        ++this.bG;
        if (this.bG >= 180 && this.bG <= 200) {
            float f = (this.random.nextFloat() - 0.5F) * 8.0F;
            float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;

            this.worldObj.a("hugeexplosion", this.posX + (double) f, this.posY + 2.0D + (double) f1, this.posZ + (double) f2, 0.0D, 0.0D, 0.0D);
        }

        int i;
        int j;

        if (!this.worldObj.isStatic && this.bG > 150 && this.bG % 5 == 0) {
            i = expToDrop / 12; // CraftBukkit - drop experience as dragon falls from sky. use experience drop from death event. This is now set in getExpReward()

            while (i > 0) {
                j = EntityXPOrb.getOrbValue(i);
                i -= j;
                this.worldObj.addEntity(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }
        }

        this.move(0.0D, 0.10000000149011612D, 0.0D);
        this.aq = this.rotationYaw += 20.0F;
        if (this.bG == 200 && !this.worldObj.isStatic) {
            i = expToDrop - 10 * (expToDrop / 12); // CraftBukkit - drop the remaining experience

            while (i > 0) {
                j = EntityXPOrb.getOrbValue(i);
                i -= j;
                this.worldObj.addEntity(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }

            this.a(MathHelper.floor(this.posX), MathHelper.floor(this.posZ));
            this.setDead();
        }
    }

    private void a(int i, int j) {
        byte b0 = 64;

        BlockEnderPortal.a = true;
        byte b1 = 4;

        // CraftBukkit start - Replace any "this.world" in the following with just "world"!
        BlockStateListPopulator world = new BlockStateListPopulator(this.worldObj.getWorld());

        for (int k = b0 - 1; k <= b0 + 32; ++k) {
            for (int l = i - b1; l <= i + b1; ++l) {
                for (int i1 = j - b1; i1 <= j + b1; ++i1) {
                    double d0 = (double) (l - i);
                    double d1 = (double) (i1 - j);
                    double d2 = d0 * d0 + d1 * d1;

                    if (d2 <= ((double) b1 - 0.5D) * ((double) b1 - 0.5D)) {
                        if (k < b0) {
                            if (d2 <= ((double) (b1 - 1) - 0.5D) * ((double) (b1 - 1) - 0.5D)) {
                                world.setTypeId(l, k, i1, Block.BEDROCK.blockID);
                            }
                        } else if (k > b0) {
                            world.setTypeId(l, k, i1, 0);
                        } else if (d2 > ((double) (b1 - 1) - 0.5D) * ((double) (b1 - 1) - 0.5D)) {
                            world.setTypeId(l, k, i1, Block.BEDROCK.blockID);
                        } else {
                            world.setTypeId(l, k, i1, Block.ENDER_PORTAL.blockID);
                        }
                    }
                }
            }
        }

        world.setTypeId(i, b0 + 0, j, Block.BEDROCK.blockID);
        world.setTypeId(i, b0 + 1, j, Block.BEDROCK.blockID);
        world.setTypeId(i, b0 + 2, j, Block.BEDROCK.blockID);
        world.setTypeId(i - 1, b0 + 2, j, Block.TORCH.blockID);
        world.setTypeId(i + 1, b0 + 2, j, Block.TORCH.blockID);
        world.setTypeId(i, b0 + 2, j - 1, Block.TORCH.blockID);
        world.setTypeId(i, b0 + 2, j + 1, Block.TORCH.blockID);
        world.setTypeId(i, b0 + 3, j, Block.BEDROCK.blockID);
        world.setTypeId(i, b0 + 4, j, Block.DRAGON_EGG.blockID);

        EntityCreatePortalEvent event = new EntityCreatePortalEvent((org.bukkit.entity.LivingEntity) this.getBukkitEntity(), java.util.Collections.unmodifiableList(world.getList()), org.bukkit.PortalType.ENDER);
        this.worldObj.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            for (BlockState state : event.getBlocks()) {
                state.update(true);
            }
        } else {
            for (BlockState state : event.getBlocks()) {
                Packet53BlockChange packet = new Packet53BlockChange(state.getX(), state.getY(), state.getZ(), this.worldObj);
                for (Iterator it = this.worldObj.players.iterator(); it.hasNext();) {
                    net.minecraft.src.EntityPlayer entity = (net.minecraft.src.EntityPlayer) it.next();
                    if (entity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) entity).serverForThisPlayer.sendPacketToPlayer(packet);
                    }
                }
            }
        }
        // CraftBukkit end

        BlockEnderPortal.a = false;
    }

    protected void bb() {}

    public Entity[] al() {
        return this.children;
    }

    public boolean L() {
        return false;
    }

    // CraftBukkit start
    public int getExpReward() {
        // This value is equal to the amount of experience dropped while falling from the sky (10 * 1000)
        // plus what is dropped when the dragon hits the ground (2000)
        return 12000;
    }
    // CraftBukkit end
}
