package net.minecraft.src;

// CraftBukkit start

import net.minecraft.server.*;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityGhast extends EntityFlying implements IMonster {

    public int a = 0;
    public double b;
    public double c;
    public double d;
    private Entity target = null;
    private int h = 0;
    public int e = 0;
    public int f = 0;

    public EntityGhast(net.minecraft.src.World world) {
        super(world);
        this.texture = "/mob/ghast.png";
        this.a(4.0F, 4.0F);
        this.fireProof = true;
        this.aV = 5;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if ("fireball".equals(damagesource.l()) && damagesource.getEntity() instanceof EntityPlayer) {
            super.damageEntity(damagesource, 1000);
            ((EntityPlayer) damagesource.getEntity()).a((Statistic) AchievementList.y);
            return true;
        } else {
            return super.damageEntity(damagesource, i);
        }
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public int getMaxHealth() {
        return 10;
    }

    public void h_() {
        super.h_();
        byte b0 = this.datawatcher.getByte(16);

        this.texture = b0 == 1 ? "/mob/ghast_fire.png" : "/mob/ghast.png";
    }

    protected void be() {
        if (!this.worldObj.isStatic && this.worldObj.difficulty == 0) {
            this.setDead();
        }

        this.bb();
        this.e = this.f;
        double d0 = this.b - this.posX;
        double d1 = this.c - this.posY;
        double d2 = this.d - this.posZ;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 < 1.0D || d3 > 3600.0D) {
            this.b = this.posX + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.c = this.posY + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.d = this.posZ + (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 16.0F);
        }

        if (this.a-- <= 0) {
            this.a += this.random.nextInt(5) + 2;
            d3 = (double) MathHelper.sqrt(d3);
            if (this.a(this.b, this.c, this.d, d3)) {
                this.motionX += d0 / d3 * 0.1D;
                this.motionY += d1 / d3 * 0.1D;
                this.motionZ += d2 / d3 * 0.1D;
            } else {
                this.b = this.posX;
                this.c = this.posY;
                this.d = this.posZ;
            }
        }

        if (this.target != null && this.target.isDead) {
            // CraftBukkit start
            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null, EntityTargetEvent.TargetReason.TARGET_DIED);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.target = null;
                } else {
                    this.target = ((CraftEntity) event.getTarget()).getHandle();
                }
            }
            // CraftBukkit end
        }

        if (this.target == null || this.h-- <= 0) {
            // CraftBukkit start
            Entity target = this.worldObj.findNearbyVulnerablePlayer(this, 100.0D);
            if (target != null) {
                EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), target.getBukkitEntity(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    if (event.getTarget() == null) {
                        this.target = null;
                    } else {
                        this.target = ((CraftEntity) event.getTarget()).getHandle();
                    }
                }
            }
            // CraftBukkit end

            if (this.target != null) {
                this.h = 20;
            }
        }

        double d4 = 64.0D;

        if (this.target != null && this.target.e((Entity) this) < d4 * d4) {
            double d5 = this.target.posX - this.posX;
            double d6 = this.target.boundingBox.b + (double) (this.target.length / 2.0F) - (this.posY + (double) (this.length / 2.0F));
            double d7 = this.target.posZ - this.posZ;

            this.aq = this.rotationYaw = -((float) Math.atan2(d5, d7)) * 180.0F / 3.1415927F;
            if (this.l(this.target)) {
                if (this.f == 10) {
                    this.worldObj.a((EntityPlayer) null, 1007, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
                }

                ++this.f;
                if (this.f == 20) {
                    this.worldObj.a((EntityPlayer) null, 1008, (int) this.posX, (int) this.posY, (int) this.posZ, 0);
                    EntityFireball entityfireball = new EntityFireball(this.worldObj, this, d5, d6, d7);
                    double d8 = 4.0D;
                    Vec3 vec3d = this.i(1.0F);

                    entityfireball.posX = this.posX + vec3d.a * d8;
                    entityfireball.posY = this.posY + (double) (this.length / 2.0F) + 0.5D;
                    entityfireball.posZ = this.posZ + vec3d.c * d8;
                    this.worldObj.addEntity(entityfireball);
                    this.f = -40;
                }
            } else if (this.f > 0) {
                --this.f;
            }
        } else {
            this.aq = this.rotationYaw = -((float) Math.atan2(this.motionX, this.motionZ)) * 180.0F / 3.1415927F;
            if (this.f > 0) {
                --this.f;
            }
        }

        if (!this.worldObj.isStatic) {
            byte b0 = this.datawatcher.getByte(16);
            byte b1 = (byte) (this.f > 10 ? 1 : 0);

            if (b0 != b1) {
                this.datawatcher.watch(16, Byte.valueOf(b1));
            }
        }
    }

    private boolean a(double d0, double d1, double d2, double d3) {
        double d4 = (this.b - this.posX) / d3;
        double d5 = (this.c - this.posY) / d3;
        double d6 = (this.d - this.posZ) / d3;
        AxisAlignedBB axisalignedbb = this.boundingBox.clone();

        for (int i = 1; (double) i < d3; ++i) {
            axisalignedbb.d(d4, d5, d6);
            if (!this.worldObj.getCubes(this, axisalignedbb).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    protected String aQ() {
        return "mob.ghast.moan";
    }

    protected String aR() {
        return "mob.ghast.scream";
    }

    protected String aS() {
        return "mob.ghast.death";
    }

    protected int getLootId() {
        return Item.SULPHUR.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(2) + this.random.nextInt(1 + i);

        int k;

        if (j > 0) {
            loot.add(new CraftItemStack(Item.GHAST_TEAR.id, j));
        }

        j = this.random.nextInt(3) + this.random.nextInt(1 + i);

        if (j > 0) {
            loot.add(new CraftItemStack(Item.SULPHUR.id, j));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    protected float aP() {
        return 10.0F;
    }

    public boolean canSpawn() {
        return this.random.nextInt(20) == 0 && super.canSpawn() && this.worldObj.difficulty > 0;
    }

    public int bl() {
        return 1;
    }
}
