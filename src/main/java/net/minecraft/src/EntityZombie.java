package net.minecraft.src;

import net.minecraft.src.EntityMob;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.Item;
import net.minecraft.server.MathHelper;
import net.minecraft.server.PathfinderGoalFloat;
import net.minecraft.server.PathfinderGoalHurtByTarget;
import net.minecraft.server.PathfinderGoalLookAtPlayer;
import net.minecraft.server.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalRandomLookaround;
import net.minecraft.server.PathfinderGoalRandomStroll;

import org.bukkit.event.entity.EntityCombustEvent; // CraftBukkit

public class EntityZombie extends EntityMob {

    public EntityZombie(net.minecraft.src.World world) {
        super(world);
        this.texture = "/mob/zombie.png";
        this.bw = 0.23F;
        this.damage = 4;
        this.getNavigation().b(true);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new EntityAIBreakDoor(this));
        this.goalSelector.a(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.bw, false));
        this.goalSelector.a(3, new EntityAIAttackOnCollide(this, EntityVillager.class, this.bw, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, this.bw));
        this.goalSelector.a(5, new PathfinderGoalMoveThroughVillage(this, this.bw, false));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, this.bw));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityPlayer.class, 8.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
    }

    public int getMaxHealth() {
        return 20;
    }

    public int aO() {
        return 2;
    }

    protected boolean aV() {
        return true;
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

    protected String aQ() {
        return "mob.zombie";
    }

    protected String aR() {
        return "mob.zombiehurt";
    }

    protected String aS() {
        return "mob.zombiedeath";
    }

    protected int getLootId() {
        return Item.ROTTEN_FLESH.id;
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    // CraftBukkit start - return rare dropped item instead of dropping it
    protected net.minecraft.src.ItemStack l(int i) {
        switch (this.random.nextInt(4)) {
        case 0:
            return new net.minecraft.src.ItemStack(Item.IRON_SWORD.id, 1, 0);
        case 1:
            return new net.minecraft.src.ItemStack(Item.IRON_HELMET.id, 1, 0);
        case 2:
            return new net.minecraft.src.ItemStack(Item.IRON_INGOT.id, 1, 0);
        case 3:
            return new net.minecraft.src.ItemStack(Item.IRON_SPADE.id, 1, 0);
        default:
            return null;
        }
    }
    // CraftBukkit end
}
