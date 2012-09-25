package net.minecraft.src;

import net.minecraft.server.AchievementList;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EnchantmentManager;
import net.minecraft.src.EntityMob;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.Item;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathfinderGoalFleeSun;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;
import net.minecraft.server.PathfinderGoalRestrictSun;
import net.minecraft.server.Statistic;

import org.bukkit.event.entity.EntityCombustEvent; // CraftBukkit

public class EntitySkeleton extends EntityMob {

    private static final net.minecraft.src.ItemStack d = new net.minecraft.src.ItemStack(Item.BOW, 1);

    public EntitySkeleton(net.minecraft.src.World world) {
        super(world);
        this.texture = "/mob/skeleton.png";
        this.bw = 0.25F;
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
        this.goalSelector.a(3, new PathfinderGoalFleeSun(this, this.bw));
        this.goalSelector.a(4, new EntityAIArrowAttack(this, this.bw, 1, 60));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, this.bw));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityPlayer.class, 8.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
    }

    public boolean aV() {
        return true;
    }

    public int getMaxHealth() {
        return 20;
    }

    protected String aQ() {
        return "mob.skeleton";
    }

    protected String aR() {
        return "mob.skeletonhurt";
    }

    protected String aS() {
        return "mob.skeletonhurt";
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    public void d() {
        if (this.worldObj.s() && !this.worldObj.isStatic) {
            float f = this.c(1.0F);

            if (f > 0.5F && this.worldObj.j(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                // CraftBukkit start
                EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.setOnFire(event.getDuration());
                }
                // CraftBukkit end
            }
        }

        super.d();
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        if (damagesource.f() instanceof EntityArrow && damagesource.getEntity() instanceof EntityPlayer) {
            EntityPlayer entityhuman = (EntityPlayer) damagesource.getEntity();
            double d0 = entityhuman.posX - this.posX;
            double d1 = entityhuman.posZ - this.posZ;

            if (d0 * d0 + d1 * d1 >= 2500.0D) {
                entityhuman.a((Statistic) AchievementList.v);
            }
        }
    }

    protected int getLootId() {
        return Item.ARROW.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(3 + i);

        int count = this.random.nextInt(3 + i);
        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.ARROW, count));
        }

        count = this.random.nextInt(3 + i);
        if (count > 0) {
            loot.add(new org.bukkit.inventory.ItemStack(org.bukkit.Material.BONE, count));
        }

        // Determine rare item drops and add them to the loot
        if (this.lastDamageByPlayerTime > 0) {
            int k = this.random.nextInt(200) - i;

            if (k < 5) {
                net.minecraft.src.ItemStack itemstack = this.l(k <= 0 ? 1 : 0);
                if (itemstack != null) {
                    loot.add(new org.bukkit.craftbukkit.inventory.CraftItemStack(itemstack));
                }
            }
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    // CraftBukkit start - return rare dropped item instead of dropping it
    protected net.minecraft.src.ItemStack l(int i) {
        if (i > 0) {
            net.minecraft.src.ItemStack itemstack = new net.minecraft.src.ItemStack(Item.BOW);

            EnchantmentManager.a(this.random, itemstack, 5);
            return itemstack;
        } else {
            return new net.minecraft.src.ItemStack(Item.BOW.id, 1, 0);
        }
    }
    // CraftBukkit end
}
