package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerEggThrowEvent;
// CraftBukkit end

public class EntityEgg extends EntityProjectile {

    public EntityEgg(World world) {
        super(world);
    }

    public EntityEgg(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityEgg(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entity != null) {
            movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.shootingEntity), 0);
        }

        // CraftBukkit start
        boolean hatching = !this.worldObj.isStatic && this.random.nextInt(8) == 0;
        int numHatching = (this.random.nextInt(32) == 0) ? 4 : 1;
        if (!hatching) {
            numHatching = 0;
        }

        EntityType hatchingType = EntityType.CHICKEN;

        if (this.shootingEntity instanceof EntityPlayer) {
            Player player = (this.shootingEntity == null) ? null : (Player) this.shootingEntity.getBukkitEntity();

            PlayerEggThrowEvent event = new PlayerEggThrowEvent(player, (org.bukkit.entity.Egg) this.getBukkitEntity(), hatching, (byte) numHatching, hatchingType);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            hatching = event.isHatching();
            numHatching = event.getNumHatches();
            hatchingType = event.getHatchingType();
        }

        if (hatching) {
            for (int k = 0; k < numHatching; k++) {
                org.bukkit.entity.Entity entity = worldObj.getWorld().spawn(new org.bukkit.Location(worldObj.getWorld(), this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F), hatchingType.getEntityClass(), SpawnReason.EGG);
                if (entity instanceof Ageable) {
                    ((Ageable) entity).setBaby();
                }
            }
        }
        // CraftBukkit end

        for (int j = 0; j < 8; ++j) {
            this.worldObj.a("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.worldObj.isStatic) {
            this.setDead();
        }
    }
}
