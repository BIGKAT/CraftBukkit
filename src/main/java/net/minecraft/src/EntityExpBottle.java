package net.minecraft.src;

import net.minecraft.server.MovingObjectPosition;

public class EntityExpBottle extends EntityProjectile {

    public EntityExpBottle(net.minecraft.src.World world) {
        super(world);
    }

    public EntityExpBottle(net.minecraft.src.World world, net.minecraft.src.EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityExpBottle(net.minecraft.src.World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    protected float h() {
        return 0.07F;
    }

    protected float d() {
        return 0.7F;
    }

    protected float g() {
        return -20.0F;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.worldObj.isStatic) {
            // CraftBukkit moved after event
            // this.world.triggerEffect(2002, (int) Math.round(this.locX), (int) Math.round(this.locY), (int) Math.round(this.locZ), 0);
            int i = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);

            // CraftBukkit start
            org.bukkit.event.entity.ExpBottleEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callExpBottleEvent(this, i);
            i = event.getExperience();
            if (event.getShowEffect()) {
                this.worldObj.triggerEffect(2002, (int) Math.round(this.posX), (int) Math.round(this.posY), (int) Math.round(this.posZ), 0);
            }
            // CraftBukkit end

            while (i > 0) {
                int j = EntityXPOrb.getOrbValue(i);

                i -= j;
                this.worldObj.addEntity(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }

            this.setDead();
        }
    }
}
