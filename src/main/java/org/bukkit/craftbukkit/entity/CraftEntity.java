package org.bukkit.craftbukkit.entity;

import java.util.List;
import java.util.UUID;

import net.minecraft.src.*;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public abstract class CraftEntity implements org.bukkit.entity.Entity {
    protected final CraftServer server;
    protected Entity entity;
    private EntityDamageEvent lastDamageEvent;

    public CraftEntity(final CraftServer server, final Entity entity) {
        this.server = server;
        this.entity = entity;
    }

    public static CraftEntity getEntity(CraftServer server, Entity entity) {
        /**
         * Order is *EXTREMELY* important -- keep it right! =D
         */
        if (entity instanceof net.minecraft.src.EntityLiving) {
            // Players
            if (entity instanceof net.minecraft.src.EntityPlayer) {
                if (entity instanceof EntityPlayerMP) { return new CraftPlayer(server, (EntityPlayerMP) entity); }
                else { return new CraftHumanEntity(server, (net.minecraft.src.EntityPlayer) entity); }
            }
            else if (entity instanceof EntityCreature) {
                // Animals
                if (entity instanceof EntityAnimal) {
                    if (entity instanceof EntityChicken) { return new CraftChicken(server, (EntityChicken) entity); }
                    else if (entity instanceof EntityCow) {
                        if (entity instanceof EntityMooshroom) { return new CraftMushroomCow(server, (EntityMooshroom) entity); }
                        else { return new CraftCow(server, (EntityCow) entity); }
                    }
                    else if (entity instanceof net.minecraft.src.EntityPig) { return new CraftPig(server, (net.minecraft.src.EntityPig) entity); }
                    else if (entity instanceof EntityTameable) {
                        if (entity instanceof net.minecraft.src.EntityWolf) { return new CraftWolf(server, (net.minecraft.src.EntityWolf) entity); }
                        else if (entity instanceof net.minecraft.src.EntityOcelot) { return new CraftOcelot(server, (net.minecraft.src.EntityOcelot) entity); }
                    }
                    else if (entity instanceof net.minecraft.src.EntitySheep) { return new CraftSheep(server, (net.minecraft.src.EntitySheep) entity); }
                    else  { return new CraftAnimals(server, (EntityAnimal) entity); }
                }
                // Monsters
                else if (entity instanceof EntityMob) {
                    if (entity instanceof net.minecraft.src.EntityZombie) {
                        if (entity instanceof net.minecraft.src.EntityPigZombie) { return new CraftPigZombie(server, (net.minecraft.src.EntityPigZombie) entity); }
                        else { return new CraftZombie(server, (net.minecraft.src.EntityZombie) entity); }
                    }
                    else if (entity instanceof EntityCreeper) { return new CraftCreeper(server, (EntityCreeper) entity); }
                    else if (entity instanceof EntityEnderman) { return new CraftEnderman(server, (EntityEnderman) entity); }
                    else if (entity instanceof net.minecraft.src.EntitySilverfish) { return new CraftSilverfish(server, (net.minecraft.src.EntitySilverfish) entity); }
                    else if (entity instanceof EntityGiantZombie) { return new CraftGiant(server, (EntityGiantZombie) entity); }
                    else if (entity instanceof net.minecraft.src.EntitySkeleton) { return new CraftSkeleton(server, (net.minecraft.src.EntitySkeleton) entity); }
                    else if (entity instanceof EntityBlaze) { return new CraftBlaze(server, (EntityBlaze) entity); }
                    else if (entity instanceof net.minecraft.src.EntitySpider) {
                        if (entity instanceof EntityCaveSpider) { return new CraftCaveSpider(server, (EntityCaveSpider) entity); }
                        else { return new CraftSpider(server, (net.minecraft.src.EntitySpider) entity); }
                    }

                    else  { return new CraftMonster(server, (EntityMob) entity); }
                }
                // Water Animals
                else if (entity instanceof EntityWaterMob) {
                    if (entity instanceof net.minecraft.src.EntitySquid) { return new CraftSquid(server, (net.minecraft.src.EntitySquid) entity); }
                    else { return new CraftWaterMob(server, (EntityWaterAnimal) entity); }
                }
                else if (entity instanceof EntityGolem) {
                    if (entity instanceof net.minecraft.src.EntitySnowman) { return new CraftSnowman(server, (net.minecraft.src.EntitySnowman) entity); }
                    else if (entity instanceof net.minecraft.src.EntityIronGolem) { return new CraftIronGolem(server, (net.minecraft.src.EntityIronGolem) entity); }
                }
                else if (entity instanceof EntityVillager) { return new CraftVillager(server, (EntityVillager) entity); }
                else { return new CraftCreature(server, (EntityCreature) entity); }
            }
            // Slimes are a special (and broken) case
            else if (entity instanceof net.minecraft.src.EntitySlime) {
                if (entity instanceof net.minecraft.src.EntityMagmaCube) { return new CraftMagmaCube(server, (net.minecraft.src.EntityMagmaCube) entity); }
                else { return new CraftSlime(server, (net.minecraft.src.EntitySlime) entity); }
            }
            // Flying
            else if (entity instanceof EntityFlying) {
                if (entity instanceof net.minecraft.src.EntityGhast) { return new CraftGhast(server, (net.minecraft.src.EntityGhast) entity); }
                else { return new CraftFlying(server, (EntityFlying) entity); }
            }
            else if (entity instanceof EntityDragonBase) {
                if (entity instanceof EntityDragon) { return new CraftEnderDragon(server, (EntityDragon) entity); }
            }
            else  { return new CraftLivingEntity(server, (net.minecraft.src.EntityLiving) entity); }
        }
        else if (entity instanceof EntityDragonPart) {
			EntityDragonPart part = (EntityDragonPart) entity;
            if (part.entityDragonObj instanceof EntityDragon) { return new CraftEnderDragonPart(server, (EntityDragonPart) entity); }
            else { return new CraftComplexPart(server, (EntityDragonPart) entity); }
        }
        else if (entity instanceof EntityXPOrb) { return new CraftExperienceOrb(server, (EntityXPOrb) entity); }
        else if (entity instanceof EntityArrow) { return new CraftArrow(server, (EntityArrow) entity); }
        else if (entity instanceof EntityBoat) { return new CraftBoat(server, (EntityBoat) entity); }
        else if (entity instanceof EntityProjectile) {
            if (entity instanceof EntityEgg) { return new CraftEgg(server, (EntityEgg) entity); }
            else if (entity instanceof EntitySnowball) { return new CraftSnowball(server, (EntitySnowball) entity); }
            else if (entity instanceof net.minecraft.src.EntityPotion) { return new CraftThrownPotion(server, (net.minecraft.src.EntityPotion) entity); }
            else if (entity instanceof EntityEnderPearl) { return new CraftEnderPearl(server, (EntityEnderPearl) entity); }
            else if (entity instanceof EntityExpBottle) { return new CraftThrownExpBottle(server, (EntityExpBottle) entity); }
        }
        else if (entity instanceof EntityFallingSand) { return new CraftFallingSand(server, (EntityFallingSand) entity); }
        else if (entity instanceof EntityFireball) {
            if (entity instanceof net.minecraft.src.EntitySmallFireball) { return new CraftSmallFireball(server, (net.minecraft.src.EntitySmallFireball) entity); }
            else { return new CraftFireball(server, (EntityFireball) entity); }
        }
        else if (entity instanceof EntityEnderSignal) { return new CraftEnderSignal(server, (EntityEnderSignal) entity); }
        else if (entity instanceof EntityEnderCrystal) { return new CraftEnderCrystal(server, (EntityEnderCrystal) entity); }
        else if (entity instanceof EntityFishingHook) { return new CraftFish(server, (EntityFishingHook) entity); }
        else if (entity instanceof net.minecraft.src.EntityItem) { return new CraftItem(server, (net.minecraft.src.EntityItem) entity); }
        else if (entity instanceof EntityWeatherEffect) {
            if (entity instanceof EntityLightningBolt) { return new CraftLightningStrike(server, (EntityLightningBolt) entity); }
            else { return new CraftWeather(server, (EntityWeatherEffect) entity); }
        }
        else if (entity instanceof net.minecraft.src.EntityMinecart) {
            net.minecraft.src.EntityMinecart mc = (net.minecraft.src.EntityMinecart) entity;
            if (mc.type == CraftMinecart.Type.StorageMinecart.getId()) { return new CraftStorageMinecart(server, mc); }
            else if (mc.type == CraftMinecart.Type.PoweredMinecart.getId()) { return new CraftPoweredMinecart(server, mc); }
            else { return new CraftMinecart(server, mc); }
        }
        else if (entity instanceof net.minecraft.src.EntityPainting) { return new CraftPainting(server, (net.minecraft.src.EntityPainting) entity); }
        else if (entity instanceof net.minecraft.src.EntityTNTPrimed) { return new CraftTNTPrimed(server, (net.minecraft.src.EntityTNTPrimed) entity); }

        throw new IllegalArgumentException("Unknown entity");
    }

    public Location getLocation() {
        return new Location(getWorld(), entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
    }

    public Vector getVelocity() {
        return new Vector(entity.motionX, entity.motionY, entity.motionZ);
    }

    public void setVelocity(Vector vel) {
        entity.motionX = vel.getX();
        entity.motionY = vel.getY();
        entity.motionZ = vel.getZ();
        entity.velocityChanged = true;
    }

    public World getWorld() {
        return entity.worldObj.getWorld();
    }

    public boolean teleport(Location location) {
        return teleport(location, TeleportCause.PLUGIN);
    }

    public boolean teleport(Location location, TeleportCause cause) {
        entity.worldObj = ((CraftWorld) location.getWorld()).getHandle();
        entity.setPositionAndRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        // entity.setLocation() throws no event, and so cannot be cancelled
        return true;
    }

    public boolean teleport(org.bukkit.entity.Entity destination) {
        return teleport(destination.getLocation());
    }

    public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {
        return teleport(destination.getLocation(), cause);
    }

    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        @SuppressWarnings("unchecked")
        List<Entity> notchEntityList = entity.worldObj.getEntities(entity, entity.boundingBox.grow(x, y, z));
        List<org.bukkit.entity.Entity> bukkitEntityList = new java.util.ArrayList<org.bukkit.entity.Entity>(notchEntityList.size());

        for (Entity e : notchEntityList) {
            bukkitEntityList.add(e.getBukkitEntity());
        }
        return bukkitEntityList;
    }

    public int getEntityId() {
        return entity.entityId;
    }

    public int getFireTicks() {
        return entity.fire;
    }

    public int getMaxFireTicks() {
        return entity.fireResistance;
    }

    public void setFireTicks(int ticks) {
        entity.fire = ticks;
    }

    public void remove() {
        entity.isDead = true;
    }

    public boolean isDead() {
        return !entity.isEntityAlive();
    }

    public boolean isValid() {
        return entity.isEntityAlive() && entity.valid;
    }

    public Server getServer() {
        return server;
    }

    public Vector getMomentum() {
        return getVelocity();
    }

    public void setMomentum(Vector value) {
        setVelocity(value);
    }

    public org.bukkit.entity.Entity getPassenger() {
        return isEmpty() ? null : (CraftEntity) getHandle().riddenByEntity.getBukkitEntity();
    }

    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        if (passenger instanceof CraftEntity) {
            ((CraftEntity) passenger).getHandle().setPassengerOf(getHandle());
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return getHandle().riddenByEntity == null;
    }

    public boolean eject() {
        if (getHandle().riddenByEntity == null) {
            return false;
        }

        getHandle().riddenByEntity.setPassengerOf(null);
        return true;
    }

    public float getFallDistance() {
        return getHandle().fallDistance;
    }

    public void setFallDistance(float distance) {
        getHandle().fallDistance = distance;
    }

    public void setLastDamageCause(EntityDamageEvent event) {
        lastDamageEvent = event;
    }

    public EntityDamageEvent getLastDamageCause() {
        return lastDamageEvent;
    }

    public UUID getUniqueId() {
        return getHandle().uniqueId;
    }

    public int getTicksLived() {
        return getHandle().ticksExisted;
    }

    public void setTicksLived(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Age must be at least 1 tick");
        }
        getHandle().ticksExisted = value;
    }

    public Entity getHandle() {
        return entity;
    }

    public void playEffect(EntityEffect type) {
        this.getHandle().worldObj.setEntityState(getHandle(), type.getData());
    }

    public void setHandle(final Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "CraftEntity{" + "id=" + getEntityId() + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftEntity other = (CraftEntity) obj;
        return (this.getEntityId() == other.getEntityId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.getEntityId();
        return hash;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        server.getEntityMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return server.getEntityMetadata().getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return server.getEntityMetadata().hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        server.getEntityMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    public boolean isInsideVehicle() {
        return getHandle().ridingEntity != null;
    }

    public boolean leaveVehicle() {
        if (getHandle().ridingEntity == null) {
            return false;
        }

        getHandle().setPassengerOf(null);
        return true;
    }

    public org.bukkit.entity.Entity getVehicle() {
        if (getHandle().ridingEntity == null) {
            return null;
        }

        return getHandle().ridingEntity.getBukkitEntity();
    }
}
