package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
// CraftBukkit end

public class EntityEnderPearl extends EntityProjectile {

    public EntityEnderPearl(World world) {
        super(world);
    }

    public EntityEnderPearl(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entity != null) {
            movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.shootingEntity), 0);
        }

        for (int i = 0; i < 32; ++i) {
            this.worldObj.a("portal", this.posX, this.posY + this.random.nextDouble() * 2.0D, this.posZ, this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.worldObj.isStatic) {
            if (this.shootingEntity != null && this.shootingEntity instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) this.shootingEntity;

                if (!entityplayer.serverForThisPlayer.disconnected && entityplayer.worldObj == this.worldObj) {
                    // CraftBukkit start
                    CraftPlayer player = entityplayer.getBukkitEntity();
                    org.bukkit.Location location = getBukkitEntity().getLocation();
                    location.setPitch(player.getLocation().getPitch());
                    location.setYaw(player.getLocation().getYaw());

                    PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                    Bukkit.getPluginManager().callEvent(teleEvent);

                    if (!teleEvent.isCancelled() && !entityplayer.serverForThisPlayer.disconnected) {
                        entityplayer.serverForThisPlayer.teleport(teleEvent.getTo());
                        this.shootingEntity.fallDistance = 0.0F;

                        EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(this.getBukkitEntity(), player, EntityDamageByEntityEvent.DamageCause.FALL, 5);
                        Bukkit.getPluginManager().callEvent(damageEvent);

                        if (!damageEvent.isCancelled() && !entityplayer.serverForThisPlayer.disconnected) {
                            entityplayer.initialInvulnerability = -1; // Remove spawning invulnerability
                            player.setLastDamageCause(damageEvent);
                            entityplayer.damageEntity(DamageSource.FALL, damageEvent.getDamage());
                        }
                    }
                    // CraftBukkit end
                }
            }

            this.setDead();
        }
    }
}
