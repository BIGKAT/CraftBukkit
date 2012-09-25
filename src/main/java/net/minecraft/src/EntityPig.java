package net.minecraft.src;

public class EntityPig extends EntityAnimal {

    public EntityPig(net.minecraft.src.World world) {
        super(world);
        this.texture = "/mob/pig.png";
        this.setSize(0.9F, 0.9F);
        this.getNavigation().a(true);
        float f = 0.25F;

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 0.38F));
        this.goalSelector.a(2, new EntityAIMate(this, f));
        this.goalSelector.a(3, new PathfinderGoalTempt(this, 0.25F, Item.WHEAT.id, false));
        this.goalSelector.a(4, new PathfinderGoalFollowParent(this, 0.28F));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, f));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, net.minecraft.src.EntityPlayer.class, 6.0F));
        this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
    }

    public boolean aV() {
        return true;
    }

    public int getMaxHealth() {
        return 10;
    }

    protected void entityInit() {
        super.entityInit();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        nbttagcompound.setBoolean("Saddle", this.getSaddled());
    }

    public void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        this.setSaddled(nbttagcompound.getBoolean("Saddle"));
    }

    protected String aQ() {
        return "mob.pig";
    }

    protected String aR() {
        return "mob.pig";
    }

    protected String aS() {
        return "mob.pigdeath";
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        if (super.c(entityhuman)) {
            return true;
        } else if (this.getSaddled() && !this.worldObj.isStatic && (this.riddenByEntity == null || this.riddenByEntity == entityhuman)) {
            entityhuman.mount(this);
            return true;
        } else {
            return false;
        }
    }

    protected int getLootId() {
        return this.isBurning() ? Item.GRILLED_PORK.id : Item.PORK.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(3) + 1 + this.random.nextInt(1 + i);

        if (j > 0) {
            if (this.isBurning()) {
                loot.add(new org.bukkit.inventory.ItemStack(Item.GRILLED_PORK.id, j));
            } else {
                loot.add(new org.bukkit.inventory.ItemStack(Item.PORK.id, j));
            }
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public boolean getSaddled() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setSaddled(boolean flag) {
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) 0));
        }
    }

    public void a(EntityLightningBolt entitylightning) {
        if (!this.worldObj.isStatic) {
            EntityPigZombie entitypigzombie = new EntityPigZombie(this.worldObj);

            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callPigZapEvent(this, entitylightning, entitypigzombie).isCancelled()) {
                return;
            }
            // CraftBukkit end

            entitypigzombie.setPositionRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            // CraftBukkit - added a reason for spawning this creature
            this.worldObj.addEntity(entitypigzombie, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.LIGHTNING);
            this.setDead();
        }
    }

    protected void a(float f) {
        super.a(f);
        if (f > 5.0F && this.riddenByEntity instanceof net.minecraft.src.EntityPlayer) {
            ((net.minecraft.src.EntityPlayer) this.riddenByEntity).a((Statistic) AchievementList.u);
        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityPig(this.worldObj);
    }
}
