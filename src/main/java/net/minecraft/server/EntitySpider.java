package net.minecraft.server;

import org.bukkit.event.entity.EntityTargetEvent; // CraftBukkit

public class EntitySpider extends EntityMonster {

    public EntitySpider(World world) {
        super(world);
        this.texture = "/mob/spider.png";
        this.a(1.4F, 0.9F);
        this.bw = 0.8F;
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    public void h_() {
        super.h_();
        if (!this.worldObj.isStatic) {
            this.e(this.positionChanged);
        }
    }

    public int getMaxHealth() {
        return 16;
    }

    public double X() {
        return (double) this.length * 0.75D - 0.5D;
    }

    protected boolean e_() {
        return false;
    }

    protected Entity findTarget() {
        float f = this.c(1.0F);

        if (f < 0.5F) {
            double d0 = 16.0D;

            return this.worldObj.findNearbyVulnerablePlayer(this, d0);
        } else {
            return null;
        }
    }

    protected String aQ() {
        return "mob.spider";
    }

    protected String aR() {
        return "mob.spider";
    }

    protected String aS() {
        return "mob.spiderdeath";
    }

    protected void a(Entity entity, float f) {
        float f1 = this.c(1.0F);

        if (f1 > 0.5F && this.random.nextInt(100) == 0) {
            // CraftBukkit start
            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null, EntityTargetEvent.TargetReason.FORGOT_TARGET);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.entityToAttack = null;
                } else {
                    this.entityToAttack = ((org.bukkit.craftbukkit.entity.CraftEntity) event.getTarget()).getHandle();
                }
                return;
            }
            // CraftBukkit end
        } else {
            if (f > 2.0F && f < 6.0F && this.random.nextInt(10) == 0) {
                if (this.onGround) {
                    double d0 = entity.posX - this.posX;
                    double d1 = entity.posZ - this.posZ;
                    float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1);

                    this.motionX = d0 / (double) f2 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
                    this.motionZ = d1 / (double) f2 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
                    this.motionY = 0.4000000059604645D;
                }
            } else {
                super.a(entity, f);
            }
        }
    }

    protected int getLootId() {
        return Item.STRING.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method; adapted from super.dropDeathLoot.
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        int k = this.random.nextInt(3);

        if (i > 0) {
            k += this.random.nextInt(i + 1);
        }

        if (k > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(Item.STRING.id, k));
        }

        if (flag && (this.random.nextInt(3) == 0 || this.random.nextInt(1 + i) > 0)) {
            loot.add(new org.bukkit.inventory.ItemStack(Item.SPIDER_EYE.id, 1));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot); // raise event even for those times when the entity does not drop loot
        // CraftBukkit end
    }

    public boolean f_() {
        return this.p();
    }

    public void aj() {}

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.ARTHROPOD;
    }

    public boolean e(MobEffect mobeffect) {
        return mobeffect.getEffectId() == MobEffectList.POISON.id ? false : super.e(mobeffect);
    }

    public boolean p() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void e(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 &= -2;
        }

        this.datawatcher.watch(16, Byte.valueOf(b0));
    }
}
