package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityTameable;
public class EntityAISit extends EntityAIBase
{
    private EntityTameable a;
    private boolean b = false;

    public EntityAISit(EntityTameable par1EntityTameable)
    {
        this.a = par1EntityTameable;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.a.isTamed())
        {
            return this.b && this.a.getAttackTarget() == null; // CraftBukkit - Allow sitting for wild animals
        }
        else if (this.a.isInWater())
        {
            return false;
        }
        else if (!this.a.onGround)
        {
            return false;
        }
        else
        {
            EntityLiving var1 = this.a.getOwner();
            return var1 == null ? true : (this.a.getDistanceSqToEntity(var1) < 144.0D && var1.getAITarget() != null ? false : this.b);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.a.getNavigator().clearPathEntity();
        this.a.setSitting(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.a.setSitting(false);
    }

    /**
     * Sets the sitting flag.
     */
    public void setSitting(boolean par1)
    {
        this.b = par1;
    }
}
