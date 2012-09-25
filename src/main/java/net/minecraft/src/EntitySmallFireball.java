package net.minecraft.src;

// CraftBukkit start

import net.minecraft.server.Block;
import net.minecraft.server.DamageSource;
import net.minecraft.src.EntityFireball;
import net.minecraft.server.MovingObjectPosition;

import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
// CraftBukkit end

public class EntitySmallFireball extends EntityFireball {

    public EntitySmallFireball(net.minecraft.src.World world) {
        super(world);
        this.a(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(net.minecraft.src.World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.a(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(net.minecraft.src.World world, double d0, double d1, double d2, double d3, double d4, double d5) {
        super(world, d0, d1, d2, d3, d4, d5);
        this.a(0.3125F, 0.3125F);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.worldObj.isStatic) {
            if (movingobjectposition.entity != null) {
                if (!movingobjectposition.entity.isFireproof() && movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shootingEntity), 5)) {
                    // CraftBukkit start - entity damage by entity event + combust event
                    EntityCombustByEntityEvent event = new EntityCombustByEntityEvent((org.bukkit.entity.Projectile) this.getBukkitEntity(), movingobjectposition.entity.getBukkitEntity(), 5);
                    movingobjectposition.entity.worldObj.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        movingobjectposition.entity.setOnFire(event.getDuration());
                    }
                    // CraftBukkit end
                }
            } else {
                int i = movingobjectposition.b;
                int j = movingobjectposition.c;
                int k = movingobjectposition.d;

                switch (movingobjectposition.face) {
                case 0:
                    --j;
                    break;

                case 1:
                    ++j;
                    break;

                case 2:
                    --k;
                    break;

                case 3:
                    ++k;
                    break;

                case 4:
                    --i;
                    break;

                case 5:
                    ++i;
                }

                if (this.worldObj.isEmpty(i, j, k)) {
                    // CraftBukkit start
                    org.bukkit.block.Block block = worldObj.getWorld().getBlockAt(i, j, k);
                    BlockIgniteEvent event = new BlockIgniteEvent(block, BlockIgniteEvent.IgniteCause.FIREBALL, null);
                    worldObj.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        this.worldObj.setBlockWithNotify(i, j, k, Block.FIRE.blockID);
                    }
                    // CraftBukkit end
                }
            }

            this.setDead();
        }
    }

    public boolean L() {
        return false;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }
}
