package net.minecraft.entity.projectile;

import org.bukkit.event.entity.ExplosionPrimeEvent; // CraftBukkit
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityLargeFireball extends EntityFireball
{
    public int field_92012_e = 1;

    public EntityLargeFireball(World par1World)
    {
        super(par1World);
    }

    public EntityLargeFireball(World par1World, EntityLiving par2EntityLiving, double par3, double par5, double par7)
    {
        super(par1World, par2EntityLiving, par3, par5, par7);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (!this.worldObj.isRemote)
        {
            if (par1MovingObjectPosition.entityHit != null)
            {
                par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6);
            }

            // CraftBukkit start
            ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(this.worldObj.getServer(), this));
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled())
            {
                // give 'this' instead of (Entity) null so we know what causes the damage
                this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, event.getRadius(), event.getFire(), this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
            }

            // CraftBukkit end
            this.setDead();
        }
    }
}
