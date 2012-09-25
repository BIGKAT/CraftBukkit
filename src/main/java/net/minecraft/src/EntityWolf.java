package net.minecraft.src;

import net.minecraft.src.EntityTameableAnimal;
import net.minecraft.src.PathfinderGoalBeg;
import net.minecraft.src.PathfinderGoalFloat;
import net.minecraft.src.PathfinderGoalFollowOwner;
import net.minecraft.src.PathfinderGoalHurtByTarget;
import net.minecraft.src.PathfinderGoalLeapAtTarget;
import net.minecraft.src.PathfinderGoalLookAtPlayer;
import net.minecraft.src.PathfinderGoalOwnerHurtByTarget;
import net.minecraft.src.PathfinderGoalOwnerHurtTarget;
import net.minecraft.src.PathfinderGoalRandomLookaround;
import net.minecraft.src.PathfinderGoalRandomStroll;
import net.minecraft.src.PathfinderGoalRandomTargetNonTamed;

public class EntityWolf extends EntityTameable {

    private float e;
    private float f;
    private boolean g;
    private boolean h;
    private float i;
    private float j;

    public EntityWolf(net.minecraft.src.World world) {
        super(world);
        this.texture = "/mob/wolf.png";
        this.setSize(0.6F, 0.8F);
        this.moveSpeed = 0.3F;
        this.getNavigation().a(true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, this.d);
        this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
        this.goalSelector.a(4, new EntityAIAttackOnCollide(this, this.moveSpeed, true));
        this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, this.moveSpeed, 10.0F, 2.0F));
        this.goalSelector.a(6, new EntityAIMate(this, this.moveSpeed));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, this.moveSpeed));
        this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityPlayer.class, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
        this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
        this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntitySheep.class, 16.0F, 200, false));
    }

    public boolean aV() {
        return true;
    }

    public void b(EntityLiving entityliving) {
        super.b(entityliving);
        if (entityliving instanceof EntityPlayer) {
            this.setAngry(true);
        }
    }

    protected void bd() {
        this.datawatcher.watch(18, Integer.valueOf(this.getHealth()));
    }

    public int getMaxHealth() {
        return this.isTamed() ? 20 : 8;
    }

    protected void entityInit() {
        super.entityInit();
        this.datawatcher.a(18, new Integer(this.getHealth()));
        this.datawatcher.a(19, new Byte((byte) 0));
    }

    protected boolean e_() {
        return false;
    }

    public void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        nbttagcompound.setBoolean("Angry", this.isAngry());
    }

    public void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        this.setAngry(nbttagcompound.getBoolean("Angry"));
    }

    protected boolean ba() {
        return this.isAngry();
    }

    protected String aQ() {
        return this.isAngry() ? "mob.wolf.growl" : (this.random.nextInt(3) == 0 ? (this.isTamed() && this.datawatcher.getInt(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    protected String aR() {
        return "mob.wolf.hurt";
    }

    protected String aS() {
        return "mob.wolf.death";
    }

    protected float aP() {
        return 0.4F;
    }

    protected int getLootId() {
        return -1;
    }

    public void d() {
        super.d();
        if (!this.worldObj.isStatic && this.g && !this.h && !this.l() && this.onGround) {
            this.h = true;
            this.i = 0.0F;
            this.j = 0.0F;
            this.worldObj.broadcastEntityEffect(this, (byte) 8);
        }
    }

    public void h_() {
        super.h_();
        this.f = this.e;
        if (this.bv()) {
            this.e += (1.0F - this.e) * 0.4F;
        } else {
            this.e += (0.0F - this.e) * 0.4F;
        }

        if (this.bv()) {
            this.bx = 10;
        }

        if (this.G()) {
            this.g = true;
            this.h = false;
            this.i = 0.0F;
            this.j = 0.0F;
        } else if ((this.g || this.h) && this.h) {
            if (this.i == 0.0F) {
                this.worldObj.makeSound(this, "mob.wolf.shake", this.aP(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.j = this.i;
            this.i += 0.05F;
            if (this.j >= 2.0F) {
                this.g = false;
                this.h = false;
                this.j = 0.0F;
                this.i = 0.0F;
            }

            if (this.i > 0.4F) {
                float f = (float) this.boundingBox.b;
                int i = (int) (MathHelper.sin((this.i - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.worldObj.a("splash", this.posX + (double) f1, (double) (f + 0.8F), this.posZ + (double) f2, this.motionX, this.motionY, this.motionZ);
                }
            }
        }
    }

    public float getHeadHeight() {
        return this.length * 0.8F;
    }

    public int bf() {
        return this.isSitting() ? 20 : super.bf();
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        Entity entity = damagesource.getEntity();

        this.d.a(false);
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
            i = (i + 1) / 2;
        }

        return super.damageEntity(damagesource, i);
    }

    public boolean k(Entity entity) {
        int i = this.isTamed() ? 4 : 2;

        return entity.damageEntity(DamageSource.mobAttack(this), i);
    }

    public boolean c(EntityPlayer entityhuman) {
        net.minecraft.src.ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (this.isTamed()) {
            if (itemstack != null && Item.itemsList[itemstack.id] instanceof net.minecraft.src.ItemFood) {
                net.minecraft.src.ItemFood itemfood = (net.minecraft.src.ItemFood) Item.itemsList[itemstack.id];

                if (itemfood.h() && this.datawatcher.getInt(18) < 20) {
                    if (!entityhuman.capabilities.canInstantlyBuild) {
                        --itemstack.count;
                    }

                    this.heal(itemfood.getNutrition());
                    if (itemstack.count <= 0) {
                        entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (net.minecraft.src.ItemStack) null);
                    }

                    return true;
                }
            }

            if (entityhuman.username.equalsIgnoreCase(this.getOwnerName()) && !this.worldObj.isStatic && !this.b(itemstack)) {
                this.d.a(!this.isSitting());
                this.bu = false;
                this.setPathToEntity((PathEntity) null);
            }
        } else if (itemstack != null && itemstack.id == Item.BONE.id && !this.isAngry()) {
            if (!entityhuman.capabilities.canInstantlyBuild) {
                --itemstack.count;
            }

            if (itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (net.minecraft.src.ItemStack) null);
            }

            if (!this.worldObj.isStatic) {
                // CraftBukkit - added event call and isCancelled check.
                if (this.random.nextInt(3) == 0 && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                    this.setTamed(true);
                    this.setPathToEntity((PathEntity) null);
                    this.b((EntityLiving) null);
                    this.d.a(true);
                    this.setHealth(20);
                    this.setOwnerName(entityhuman.username);
                    this.e(true);
                    this.worldObj.broadcastEntityEffect(this, (byte) 7);
                } else {
                    this.e(false);
                    this.worldObj.broadcastEntityEffect(this, (byte) 6);
                }
            }

            return true;
        }

        return super.c(entityhuman);
    }

    public boolean b(net.minecraft.src.ItemStack itemstack) {
        return itemstack == null ? false : (!(Item.itemsList[itemstack.id] instanceof net.minecraft.src.ItemFood) ? false : ((net.minecraft.src.ItemFood) Item.itemsList[itemstack.id]).h());
    }

    public int bl() {
        return 8;
    }

    public boolean isAngry() {
        return (this.datawatcher.getByte(16) & 2) != 0;
    }

    public void setAngry(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -3)));
        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        EntityWolf entitywolf = new EntityWolf(this.worldObj);

        entitywolf.setOwnerName(this.getOwnerName());
        entitywolf.setTamed(true);
        return entitywolf;
    }

    public void i(boolean flag) {
        byte b0 = this.datawatcher.getByte(19);

        if (flag) {
            this.datawatcher.watch(19, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(19, Byte.valueOf((byte) 0));
        }
    }

    public boolean mate(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(entityanimal instanceof EntityWolf)) {
            return false;
        } else {
            EntityWolf entitywolf = (EntityWolf) entityanimal;

            return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : this.s() && entitywolf.s());
        }
    }

    public boolean bv() {
        return this.datawatcher.getByte(19) == 1;
    }
}
