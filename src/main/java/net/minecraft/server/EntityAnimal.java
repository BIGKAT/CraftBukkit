package net.minecraft.server;

import java.util.List;

public abstract class EntityAnimal extends EntityAgeable implements IAnimal
{
    private int love;

    /**
     * This is representation of a counter for reproduction progress. (Note that this is different from the inLove which
     * represent being in Love-Mode)
     */
    private int e = 0;

    public EntityAnimal(World par1World)
    {
        super(par1World);
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void bm()
    {
        if (this.getAge() != 0)
        {
            this.love = 0;
        }

        super.bm();
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void c()
    {
        super.c();

        if (this.getAge() != 0)
        {
            this.love = 0;
        }

        if (this.love > 0)
        {
            --this.love;
            String var1 = "heart";

            if (this.love % 10 == 0)
            {
                double var2 = this.random.nextGaussian() * 0.02D;
                double var4 = this.random.nextGaussian() * 0.02D;
                double var6 = this.random.nextGaussian() * 0.02D;
                this.world.addParticle(var1, this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + 0.5D + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, var2, var4, var6);
            }
        }
        else
        {
            this.e = 0;
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void a(Entity par1Entity, float par2)
    {
        if (par1Entity instanceof EntityHuman)
        {
            if (par2 < 3.0F)
            {
                double var3 = par1Entity.locX - this.locX;
                double var5 = par1Entity.locZ - this.locZ;
                this.yaw = (float)(Math.atan2(var5, var3) * 180.0D / Math.PI) - 90.0F;
                this.b = true;
            }

            EntityHuman var7 = (EntityHuman)par1Entity;

            if (var7.bS() == null || !this.c(var7.bS()))
            {
                this.target = null;
            }
        }
        else if (par1Entity instanceof EntityAnimal)
        {
            EntityAnimal var8 = (EntityAnimal)par1Entity;

            if (this.getAge() > 0 && var8.getAge() < 0)
            {
                if ((double)par2 < 2.5D)
                {
                    this.b = true;
                }
            }
            else if (this.love > 0 && var8.love > 0)
            {
                if (var8.target == null)
                {
                    var8.target = this;
                }

                if (var8.target == this && (double)par2 < 3.5D)
                {
                    ++var8.love;
                    ++this.love;
                    ++this.e;

                    if (this.e % 4 == 0)
                    {
                        this.world.addParticle("heart", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + 0.5D + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, 0.0D, 0.0D, 0.0D);
                    }

                    if (this.e == 60)
                    {
                        this.b((EntityAnimal) par1Entity);
                    }
                }
                else
                {
                    this.e = 0;
                }
            }
            else
            {
                this.e = 0;
                this.target = null;
            }
        }
    }

    /**
     * Creates a baby animal according to the animal type of the target at the actual position and spawns 'love'
     * particles.
     */
    private void b(EntityAnimal par1EntityAnimal)
    {
        EntityAgeable var2 = this.createChild(par1EntityAnimal);

        if (var2 != null)
        {
            this.setAge(6000);
            par1EntityAnimal.setAge(6000);
            this.love = 0;
            this.e = 0;
            this.target = null;
            par1EntityAnimal.target = null;
            par1EntityAnimal.e = 0;
            par1EntityAnimal.love = 0;
            var2.setAge(-24000);
            var2.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);

            for (int var3 = 0; var3 < 7; ++var3)
            {
                double var4 = this.random.nextGaussian() * 0.02D;
                double var6 = this.random.nextGaussian() * 0.02D;
                double var8 = this.random.nextGaussian() * 0.02D;
                this.world.addParticle("heart", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + 0.5D + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, var4, var6, var8);
            }

            this.world.addEntity(var2);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean damageEntity(DamageSource par1DamageSource, int par2)
    {
        if (this.isInvulnerable())
        {
            return false;
        }
        else
        {
            this.c = 60;
            this.target = null;
            this.love = 0;
            return super.damageEntity(par1DamageSource, par2);
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float a(int par1, int par2, int par3)
    {
        return this.world.getTypeId(par1, par2 - 1, par3) == Block.GRASS.id ? 10.0F : this.world.p(par1, par2, par3) - 0.5F;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void b(NBTTagCompound par1NBTTagCompound)
    {
        super.b(par1NBTTagCompound);
        par1NBTTagCompound.setInt("InLove", this.love);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void a(NBTTagCompound par1NBTTagCompound)
    {
        super.a(par1NBTTagCompound);
        this.love = par1NBTTagCompound.getInt("InLove");
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findTarget()
    {
        if (this.c > 0)
        {
            return null;
        }
        else
        {
            float var1 = 8.0F;
            List var2;
            int var3;
            EntityAnimal var4;

            if (this.love > 0)
            {
                var2 = this.world.a(this.getClass(), this.boundingBox.grow((double) var1, (double) var1, (double) var1));

                for (var3 = 0; var3 < var2.size(); ++var3)
                {
                    var4 = (EntityAnimal)var2.get(var3);

                    if (var4 != this && var4.love > 0)
                    {
                        return var4;
                    }
                }
            }
            else if (this.getAge() == 0)
            {
                var2 = this.world.a(EntityHuman.class, this.boundingBox.grow((double) var1, (double) var1, (double) var1));

                for (var3 = 0; var3 < var2.size(); ++var3)
                {
                    EntityHuman var5 = (EntityHuman)var2.get(var3);

                    if (var5.bS() != null && this.c(var5.bS()))
                    {
                        return var5;
                    }
                }
            }
            else if (this.getAge() > 0)
            {
                var2 = this.world.a(this.getClass(), this.boundingBox.grow((double) var1, (double) var1, (double) var1));

                for (var3 = 0; var3 < var2.size(); ++var3)
                {
                    var4 = (EntityAnimal)var2.get(var3);

                    if (var4 != this && var4.getAge() < 0)
                    {
                        return var4;
                    }
                }
            }

            return null;
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean canSpawn()
    {
        int var1 = MathHelper.floor(this.locX);
        int var2 = MathHelper.floor(this.boundingBox.b);
        int var3 = MathHelper.floor(this.locZ);
        return this.world.getTypeId(var1, var2 - 1, var3) == Block.GRASS.id && this.world.l(var1, var2, var3) > 8 && super.canSpawn();
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int aN()
    {
        return 120;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean bj()
    {
    	return !isNearTorch(this, 12D, world); // MCPC
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExpValue(EntityHuman par1EntityPlayer)
    {
        return 1 + this.world.random.nextInt(3);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean c(ItemStack par1ItemStack)
    {
        return par1ItemStack.id == Item.WHEAT.id;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean a(EntityHuman par1EntityPlayer)
    {
        ItemStack var2 = par1EntityPlayer.inventory.getItemInHand();

        if (var2 != null && this.c(var2) && this.getAge() == 0)
        {
            if (!par1EntityPlayer.abilities.canInstantlyBuild)
            {
                --var2.count;

                if (var2.count <= 0)
                {
                    par1EntityPlayer.inventory.setItem(par1EntityPlayer.inventory.itemInHandIndex, (ItemStack) null);
                }
            }

            this.love = 600;
            this.target = null;

            for (int var3 = 0; var3 < 7; ++var3)
            {
                double var4 = this.random.nextGaussian() * 0.02D;
                double var6 = this.random.nextGaussian() * 0.02D;
                double var8 = this.random.nextGaussian() * 0.02D;
                this.world.addParticle("heart", this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + 0.5D + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, var4, var6, var8);
            }

            return true;
        }
        else
        {
            return super.a(par1EntityPlayer);
        }
    }

    /**
     * Returns if the entity is currently in 'love mode'.
     */
    public boolean r()
    {
        return this.love > 0;
    }

    public void s()
    {
        this.love = 0;
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean mate(EntityAnimal par1EntityAnimal)
    {
        return par1EntityAnimal == this ? false : (par1EntityAnimal.getClass() != this.getClass() ? false : this.r() && par1EntityAnimal.r());
    }
    
    // MCPC start
    public static boolean isNearTorch(Entity entity, Double dist, World worldObj)
    {
        AxisAlignedBB axisalignedbb = entity.boundingBox.grow(dist, dist / 2D, dist);
        int i = MathHelper.floor(axisalignedbb.a);
        int j = MathHelper.floor(axisalignedbb.d + 1.0D);
        int k = MathHelper.floor(axisalignedbb.b);
        int l = MathHelper.floor(axisalignedbb.e + 1.0D);
        int i1 = MathHelper.floor(axisalignedbb.c);
        int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);
        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = k; l1 < l; l1++)
            {
                for (int i2 = i1; i2 < j1; i2++)
                {
                    int j2 = worldObj.getTypeId(k1, l1, i2);

                    if (j2 != 0)
                    {
                        String nameToCheck = "";
                        nameToCheck = Block.byId[j2].a();
                        if (nameToCheck != null && nameToCheck != "")
                        {
                            if (nameToCheck.equals("tile.torch") || nameToCheck.equals("tile.lightgem") || nameToCheck.equals("tile.redstoneLight") || nameToCheck.equals("tile.litpumpkin")) { return true; }
                        }

                    }

                }

            }

        }

        return false;
    }
    // MCPC end
}