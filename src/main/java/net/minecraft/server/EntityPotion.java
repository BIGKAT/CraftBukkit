package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import java.util.HashMap;

import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
// CraftBukkit end

public class EntityPotion extends EntityProjectile {

    private int d;

    public EntityPotion(World world) {
        super(world);
    }

    public EntityPotion(World world, EntityLiving entityliving, int i) {
        super(world, entityliving);
        this.d = i;
    }

    public EntityPotion(World world, double d0, double d1, double d2, int i) {
        super(world, d0, d1, d2);
        this.d = i;
    }

    protected float h() {
        return 0.05F;
    }

    protected float d() {
        return 0.5F;
    }

    protected float g() {
        return -20.0F;
    }

    public int getPotionDamage() {
        return this.d;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.worldObj.isStatic) {
            List list = Item.POTION.f(this.d);

            if (list != null && !list.isEmpty()) {
                AxisAlignedBB axisalignedbb = this.boundingBox.grow(4.0D, 2.0D, 4.0D);
                List list1 = this.worldObj.a(EntityLiving.class, axisalignedbb);

                if (list1 != null && !list1.isEmpty()) {
                    Iterator iterator = list1.iterator();

                    // CraftBukkit
                    HashMap<LivingEntity, Double> affected = new HashMap<LivingEntity, Double>();

                    while (iterator.hasNext()) {
                        EntityLiving entityliving = (EntityLiving) iterator.next();
                        double d0 = this.e(entityliving);

                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entityliving == movingobjectposition.entity) {
                                d1 = 1.0D;
                            }

                            // CraftBukkit start
                            affected.put((LivingEntity) entityliving.getBukkitEntity(), d1);
                        }
                    }

                    org.bukkit.event.entity.PotionSplashEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPotionSplashEvent(this, affected);
                    if (!event.isCancelled()) {
                        for (LivingEntity victim : event.getAffectedEntities()) {
                            if (!(victim instanceof CraftLivingEntity)) {
                                continue;
                            }

                            EntityLiving entityliving = ((CraftLivingEntity) victim).getHandle();
                            double d1 = event.getIntensity(victim);
                            // CraftBukkit end

                            Iterator iterator1 = list.iterator();

                            while (iterator1.hasNext()) {
                                MobEffect mobeffect = (MobEffect) iterator1.next();
                                int i = mobeffect.getEffectId();

                                // CraftBukkit start - abide by PVP settings
                                if (!this.worldObj.pvpMode && entityliving instanceof EntityPlayer && entityliving != this.shootingEntity) {
                                    // Block SLOWER_MOVEMENT, SLOWER_DIG, HARM, BLINDNESS, HUNGER, WEAKNESS and POISON potions
                                    if (i == 2 || i == 4 || i == 7 || i == 15 || i == 17 || i == 18 || i == 19) continue;
                                }
                                // CraftBukkit end

                                if (MobEffectList.byId[i].isInstant()) {
                                    // CraftBukkit - added 'this'
                                    MobEffectList.byId[i].applyInstantEffect(this.shootingEntity, entityliving, mobeffect.getAmplifier(), d1, this);
                                } else {
                                    int j = (int) (d1 * (double) mobeffect.getDuration() + 0.5D);

                                    if (j > 20) {
                                        entityliving.addPotionEffect(new MobEffect(i, j, mobeffect.getAmplifier()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            this.worldObj.triggerEffect(2002, (int) Math.round(this.posX), (int) Math.round(this.posY), (int) Math.round(this.posZ), this.d);
            this.setDead();
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.d = nbttagcompound.getInteger("potionValue");
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInteger("potionValue", this.d);
    }
}
