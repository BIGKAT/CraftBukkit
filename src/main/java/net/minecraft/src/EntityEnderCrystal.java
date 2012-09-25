package net.minecraft.src;

public class EntityEnderCrystal extends Entity {

    public int a = 0;
    public int b;

    public EntityEnderCrystal(net.minecraft.src.World world) {
        super(world);
        this.m = true;
        this.setSize(2.0F, 2.0F);
        this.yOffset = this.length / 2.0F;
        this.b = 5;
        this.a = this.random.nextInt(100000);
    }

    protected boolean e_() {
        return false;
    }

    protected void entityInit() {
        this.datawatcher.a(8, Integer.valueOf(this.b));
    }

    public void h_() {
        this.lastX = this.posX;
        this.lastY = this.posY;
        this.lastZ = this.posZ;
        ++this.a;
        this.datawatcher.watch(8, Integer.valueOf(this.b));
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY);
        int k = MathHelper.floor(this.posZ);

        if (this.worldObj.getBlockId(i, j, k) != Block.FIRE.blockID) {
            this.worldObj.setBlockWithNotify(i, j, k, Block.FIRE.blockID);
        }
    }

    protected void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {}

    protected void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {}

    public boolean L() {
        return true;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (!this.isDead && !this.worldObj.isStatic) {
            // CraftBukkit start - All non-living entities need this
            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, i)) {
                return false;
            }
            // CraftBukkit end

            this.b = 0;
            if (this.b <= 0) {
                this.setDead();
                if (!this.worldObj.isStatic) {
                    this.worldObj.explode(this, this.posX, this.posY, this.posZ, 6.0F); // CraftBukkit - (Entity) null -> this
                }
            }
        }

        return true;
    }
}
