package org.bukkit.craftbukkit.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.src.Packet42RemoveEntityEffect;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityDragonBase;
import net.minecraft.src.EntityEgg;
import net.minecraft.src.EntityEnderPearl;
import net.minecraft.src.EntityFireball;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.Potion;
import net.minecraft.src.World;
import org.apache.commons.lang.Validate;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    public CraftLivingEntity(final CraftServer server, final EntityLiving entity) {
        super(server, entity);
    }

    public int getHealth() {
        return getHandle().getHealth();
    }

    public void setHealth(int health) {
        if ((health < 0) || (health > getMaxHealth())) {
            throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth());
        }

        if (entity instanceof EntityPlayerMP && health == 0) {
            ((EntityPlayerMP) entity).onDeath(DamageSource.generic);
        }

        getHandle().setEntityHealth(health);
    }

    public int getMaxHealth() {
        return getHandle().getMaxHealth();
    }

    @Deprecated
    public Egg throwEgg() {
        return launchProjectile(Egg.class);
    }

    @Deprecated
    public Snowball throwSnowball() {
        return launchProjectile(Snowball.class);
    }

    public double getEyeHeight() {
        return 1.0D;
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        return getEyeHeight();
    }

    private List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            int id = block.getTypeId();
            if (transparent == null) {
                if (id != 0) {
                    break;
                }
            } else {
                if (!transparent.contains((byte) id)) {
                    break;
                }
            }
        }
        return blocks;
    }

    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 2);
    }

    @Deprecated
    public Arrow shootArrow() {
        return launchProjectile(Arrow.class);
    }

    public int getRemainingAir() {
        return getHandle().getAir();
    }

    public void setRemainingAir(int ticks) {
        getHandle().setAir(ticks);
    }

    public int getMaximumAir() {
        return getHandle().getAir();
    }

    public void setMaximumAir(int ticks) {
        getHandle().setAir(ticks);
    }

    public void damage(int amount) {
        damage(amount, null);
    }

    public void damage(int amount, org.bukkit.entity.Entity source) {
        DamageSource reason = DamageSource.generic;

        if (source instanceof HumanEntity) {
            reason = DamageSource.causePlayerDamage(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.causeMobDamage(((CraftLivingEntity) source).getHandle());
        }

        if (entity instanceof EntityDragonBase) {
            entity.attackEntityFrom(reason, amount);
        } else {
            entity.attackEntityFrom(reason, amount);
        }
    }

    public Location getEyeLocation() {
        Location loc = getLocation();
        loc.setY(loc.getY() + getEyeHeight());
        return loc;
    }

    public int getMaximumNoDamageTicks() {
        return getHandle().maxHurtResistantTime;
    }

    public void setMaximumNoDamageTicks(int ticks) {
        getHandle().maxHurtResistantTime = ticks;
    }

    public int getLastDamage() {
        return getHandle().carryoverDamage;
    }

    public void setLastDamage(int damage) {
        getHandle().carryoverDamage = damage;
    }

    public int getNoDamageTicks() {
        return getHandle().hurtResistantTime;
    }

    public void setNoDamageTicks(int ticks) {
        getHandle().hurtResistantTime = ticks;
    }

    @Override
    public EntityLiving getHandle() {
        return (EntityLiving) entity;
    }

    public void setHandle(final EntityLiving entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{" + "id=" + getEntityId() + '}';
    }

    public Player getKiller() {
        return getHandle().getLastAttackingEntity() == null ? null : (Player) CraftServer.getBukkitEntity(getHandle().getLastAttackingEntity());
    }

    public boolean addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, false);
    }

    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        if (hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }
            removePotionEffect(effect.getType());
        }
        getHandle().addPotionEffect(new net.minecraft.src.PotionEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier()));
        return true;
    }

    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= addPotionEffect(effect);
        }
        return success;
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        return getHandle().isPotionActive(Potion.potionTypes[type.getId()]);
    }

    public void removePotionEffect(PotionEffectType type) {
        getHandle().removePotionEffect(type.getId());
        getHandle().potionsNeedUpdate = true;
        if (getHandle() instanceof EntityPlayerMP) {
			if (((EntityPlayerMP) getHandle()).serverForThisPlayer == null) {
				return;
			}
            ((EntityPlayerMP) getHandle()).serverForThisPlayer.sendPacketToPlayer(new Packet42RemoveEntityEffect(getHandle().entityId, new net.minecraft.src.PotionEffect(type.getId(), 0, 0)));
        }
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (Object raw : getHandle().getActivePotionEffects()) {
			if (!(raw instanceof net.minecraft.src.PotionEffect)) {
				continue;
			}
            net.minecraft.src.PotionEffect handle = (net.minecraft.src.PotionEffect) raw;
            effects.add(new PotionEffect(PotionEffectType.getById(handle.getPotionID()), handle.getDuration(), handle.getAmplifier()));
        }
        return effects;
    }

    @SuppressWarnings("unchecked")
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        World world = ((CraftWorld) getWorld()).getHandle();
        net.minecraft.src.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, getHandle());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, getHandle());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, getHandle());
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new EntityArrow(world, getHandle(), 1);
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world);
            } else {
                launch = new EntityFireball(world);
            }

            Location location = getEyeLocation();
            Vector direction = location.getDirection().multiply(10);

            launch.setPositionAndRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            launch.setVelocity(direction.getX(), direction.getY(), direction.getZ());
        }

        Validate.notNull(launch, "Projectile not supported");

        world.joinEntityInSurroundings(launch);
        return (T) CraftServer.getBukkitEntity(launch);
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    public boolean hasLineOfSight(Entity other) {
        return getHandle().getEntitySenses().canSee(((CraftEntity) other).getHandle()); // am should be getEntitySenses
    }
}
