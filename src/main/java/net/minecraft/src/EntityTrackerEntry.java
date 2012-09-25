package net.minecraft.src;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// CraftBukkit start

import net.minecraft.server.*;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;
// CraftBukkit end

public class EntityTrackerEntry {

    public Entity tracker;
    public int b;
    public int c;
    public int xLoc;
    public int yLoc;
    public int zLoc;
    public int yRot;
    public int xRot;
    public int i;
    public double j;
    public double k;
    public double l;
    public int m = 0;
    private double p;
    private double q;
    private double r;
    private boolean s = false;
    private boolean isMoving;
    private int u = 0;
    private Entity v;
    public boolean n = false;
    public Set trackedPlayers = new HashSet();

    public EntityTrackerEntry(Entity entity, int i, int j, boolean flag) {
        this.tracker = entity;
        this.b = i;
        this.c = j;
        this.isMoving = flag;
        this.xLoc = MathHelper.floor(entity.posX * 32.0D);
        this.yLoc = MathHelper.floor(entity.posY * 32.0D);
        this.zLoc = MathHelper.floor(entity.posZ * 32.0D);
        this.yRot = MathHelper.d(entity.rotationYaw * 256.0F / 360.0F);
        this.xRot = MathHelper.d(entity.rotationPitch * 256.0F / 360.0F);
        this.i = MathHelper.d(entity.am() * 256.0F / 360.0F);
    }

    public boolean equals(Object object) {
        return object instanceof EntityTrackerEntry ? ((EntityTrackerEntry) object).tracker.entityId == this.tracker.entityId : false;
    }

    public int hashCode() {
        return this.tracker.entityId;
    }

    public void track(List list) {
        this.n = false;
        if (!this.s || this.tracker.e(this.p, this.q, this.r) > 16.0D) {
            this.p = this.tracker.posX;
            this.q = this.tracker.posY;
            this.r = this.tracker.posZ;
            this.s = true;
            this.n = true;
            this.scanPlayers(list);
        }

        if (this.v != this.tracker.ridingEntity) {
            this.v = this.tracker.ridingEntity;
            this.broadcast(new Packet39AttachEntity(this.tracker, this.tracker.ridingEntity));
        }

        if (this.tracker.ridingEntity == null) {
            ++this.u;
            if (this.m++ % this.c == 0 || this.tracker.al) {
                int i = this.tracker.am.a(this.tracker.posX);
                int j = MathHelper.floor(this.tracker.posY * 32.0D);
                int k = this.tracker.am.a(this.tracker.posZ);
                int l = MathHelper.d(this.tracker.rotationYaw * 256.0F / 360.0F);
                int i1 = MathHelper.d(this.tracker.rotationPitch * 256.0F / 360.0F);
                int j1 = i - this.xLoc;
                int k1 = j - this.yLoc;
                int l1 = k - this.zLoc;
                Object object = null;
                boolean flag = Math.abs(j1) >= 4 || Math.abs(k1) >= 4 || Math.abs(l1) >= 4;
                boolean flag1 = Math.abs(l - this.yRot) >= 4 || Math.abs(i1 - this.xRot) >= 4;

                // CraftBukkit start - code moved from below
                if (flag) {
                    this.xLoc = i;
                    this.yLoc = j;
                    this.zLoc = k;
                }

                if (flag1) {
                    this.yRot = l;
                    this.xRot = i1;
                }
                // CraftBukkit end

                if (j1 >= -128 && j1 < 128 && k1 >= -128 && k1 < 128 && l1 >= -128 && l1 < 128 && this.u <= 400) {
                    if (flag && flag1) {
                        object = new Packet33RelEntityMoveLook(this.tracker.entityId, (byte) j1, (byte) k1, (byte) l1, (byte) l, (byte) i1);
                    } else if (flag) {
                        object = new Packet31RelEntityMove(this.tracker.entityId, (byte) j1, (byte) k1, (byte) l1);
                    } else if (flag1) {
                        object = new Packet32EntityLook(this.tracker.entityId, (byte) l, (byte) i1);
                    }
                } else {
                    this.u = 0;
                    // CraftBukkit start - refresh list of who can see a player before sending teleport packet
                    if (this.tracker instanceof EntityPlayerMP) {
                        this.scanPlayers(new java.util.ArrayList(this.trackedPlayers));
                    }
                    // CraftBukkit end
                    object = new Packet34EntityTeleport(this.tracker.entityId, i, j, k, (byte) l, (byte) i1);
                }

                if (this.isMoving) {
                    double d0 = this.tracker.motionX - this.j;
                    double d1 = this.tracker.motionY - this.k;
                    double d2 = this.tracker.motionZ - this.l;
                    double d3 = 0.02D;
                    double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d4 > d3 * d3 || d4 > 0.0D && this.tracker.motionX == 0.0D && this.tracker.motionY == 0.0D && this.tracker.motionZ == 0.0D) {
                        this.j = this.tracker.motionX;
                        this.k = this.tracker.motionY;
                        this.l = this.tracker.motionZ;
                        this.broadcast(new Packet28EntityVelocity(this.tracker.entityId, this.j, this.k, this.l));
                    }
                }

                if (object != null) {
                    this.broadcast((net.minecraft.src.Packet) object);
                }

                DataWatcher datawatcher = this.tracker.getDataWatcher();

                if (datawatcher.a()) {
                    this.broadcastIncludingSelf(new Packet40EntityMetadata(this.tracker.entityId, datawatcher));
                }

                int i2 = MathHelper.d(this.tracker.am() * 256.0F / 360.0F);

                if (Math.abs(i2 - this.i) >= 4) {
                    this.broadcast(new Packet35EntityHeadRotation(this.tracker.entityId, (byte) i2));
                    this.i = i2;
                }

                /* CraftBukkit start - code moved up
                if (flag) {
                    this.xLoc = i;
                    this.yLoc = j;
                    this.zLoc = k;
                }

                if (flag1) {
                    this.yRot = l;
                    this.xRot = i1;
                }
                // CraftBukkit end */
            }

            this.tracker.al = false;
        }

        if (this.tracker.velocityChanged) {
            // CraftBukkit start - create PlayerVelocity event
            boolean cancelled = false;

            if (this.tracker instanceof EntityPlayerMP) {
                Player player = (Player) this.tracker.getBukkitEntity();
                org.bukkit.util.Vector velocity = player.getVelocity();

                PlayerVelocityEvent event = new PlayerVelocityEvent(player, velocity);
                this.tracker.worldObj.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    cancelled = true;
                } else if (!velocity.equals(event.getVelocity())) {
                    player.setVelocity(velocity);
                }
            }

            if (!cancelled) {
                this.broadcastIncludingSelf((net.minecraft.src.Packet) (new Packet28EntityVelocity(this.tracker)));
            }
            // CraftBukkit end
            this.tracker.velocityChanged = false;
        }
    }

    public void broadcast(net.minecraft.src.Packet packet) {
        Iterator iterator = this.trackedPlayers.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            entityplayer.serverForThisPlayer.sendPacketToPlayer(packet);
        }
    }

    public void broadcastIncludingSelf(net.minecraft.src.Packet packet) {
        this.broadcast(packet);
        if (this.tracker instanceof EntityPlayerMP) {
            ((EntityPlayerMP) this.tracker).serverForThisPlayer.sendPacketToPlayer(packet);
        }
    }

    public void a() {
        Iterator iterator = this.trackedPlayers.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            entityplayer.g.add(Integer.valueOf(this.tracker.entityId));
        }
    }

    public void a(EntityPlayerMP entityplayer) {
        if (this.trackedPlayers.contains(entityplayer)) {
            this.trackedPlayers.remove(entityplayer);
        }
    }

    public void updatePlayer(EntityPlayerMP entityplayer) {
        if (entityplayer != this.tracker) {
            double d0 = entityplayer.posX - (double) (this.xLoc / 32);
            double d1 = entityplayer.posZ - (double) (this.zLoc / 32);

            if (d0 >= (double) (-this.b) && d0 <= (double) this.b && d1 >= (double) (-this.b) && d1 <= (double) this.b) {
                if (!this.trackedPlayers.contains(entityplayer) && this.d(entityplayer)) {
                    // CraftBukkit start
                    if (tracker instanceof EntityPlayerMP) {
                        Player player = ((EntityPlayerMP) tracker).getBukkitEntity();
                        if (!entityplayer.getBukkitEntity().canSee(player)) {
                            return;
                        }
                    }
                    // CraftBukkit end
                    this.trackedPlayers.add(entityplayer);
                    net.minecraft.src.Packet packet = this.b();

                    entityplayer.serverForThisPlayer.sendPacketToPlayer(packet);
                    this.j = this.tracker.motionX;
                    this.k = this.tracker.motionY;
                    this.l = this.tracker.motionZ;
                    if (this.isMoving && !(packet instanceof Packet24MobSpawn)) {
                        entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet28EntityVelocity(this.tracker.entityId, this.tracker.motionX, this.tracker.motionY, this.tracker.motionZ));
                    }

                    if (this.tracker.ridingEntity != null) {
                        entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet39AttachEntity(this.tracker, this.tracker.ridingEntity));
                    }

                    net.minecraft.src.ItemStack[] aitemstack = this.tracker.getEquipment();

                    if (aitemstack != null) {
                        for (int i = 0; i < aitemstack.length; ++i) {
                            entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet5EntityEquipment(this.tracker.entityId, i, aitemstack[i]));
                        }
                    }

                    if (this.tracker instanceof EntityPlayer) {
                        EntityPlayer entityhuman = (EntityPlayer) this.tracker;

                        if (entityhuman.isSleeping()) {
                            entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet17EntityLocationAction(this.tracker, 0, MathHelper.floor(this.tracker.posX), MathHelper.floor(this.tracker.posY), MathHelper.floor(this.tracker.posZ)));
                        }
                    }

                    // CraftBukkit start - Fix for nonsensical head yaw
                    this.i = MathHelper.d(this.tracker.am() * 256.0F / 360.0F); // tracker.am() should be getHeadRotation
                    this.broadcast(new Packet35EntityHeadRotation(this.tracker.entityId, (byte) i));
                    // CraftBukkit end

                    if (this.tracker instanceof EntityLiving) {
                        EntityLiving entityliving = (EntityLiving) this.tracker;
                        Iterator iterator = entityliving.getEffects().iterator();

                        while (iterator.hasNext()) {
                            MobEffect mobeffect = (MobEffect) iterator.next();

                            entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet41MobEffect(this.tracker.entityId, mobeffect));
                        }
                    }
                }
            } else if (this.trackedPlayers.contains(entityplayer)) {
                this.trackedPlayers.remove(entityplayer);
                entityplayer.g.add(Integer.valueOf(this.tracker.entityId));
            }
        }
    }

    private boolean d(EntityPlayerMP entityplayer) {
        return entityplayer.q().getPlayerManager().a(entityplayer, this.tracker.ah, this.tracker.aj);
    }

    public void scanPlayers(List list) {
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityhuman = (EntityPlayer) iterator.next();

            this.updatePlayer((EntityPlayerMP) entityhuman);
        }
    }

    private net.minecraft.src.Packet b() {
        if (this.tracker.isDead) {
            // CraftBukkit start - remove useless error spam, just return
            // System.out.println("Fetching addPacket for removed entity");
            return null;
            // CraftBukkit end
        }

        if (this.tracker instanceof EntityItem) {
            EntityItem entityitem = (EntityItem) this.tracker;
            Packet21PickupSpawn packet21pickupspawn = new Packet21PickupSpawn(entityitem);

            entityitem.posX = (double) packet21pickupspawn.b / 32.0D;
            entityitem.posY = (double) packet21pickupspawn.c / 32.0D;
            entityitem.posZ = (double) packet21pickupspawn.d / 32.0D;
            return packet21pickupspawn;
        } else if (this.tracker instanceof EntityPlayerMP) {
            return new net.minecraft.server.Packet20NamedEntitySpawn((EntityPlayer) this.tracker);
        } else {
            if (this.tracker instanceof EntityMinecart) {
                EntityMinecart entityminecart = (EntityMinecart) this.tracker;

                if (entityminecart.type == 0) {
                    return new Packet23VehicleSpawn(this.tracker, 10);
                }

                if (entityminecart.type == 1) {
                    return new Packet23VehicleSpawn(this.tracker, 11);
                }

                if (entityminecart.type == 2) {
                    return new Packet23VehicleSpawn(this.tracker, 12);
                }
            }

            if (this.tracker instanceof EntityBoat) {
                return new Packet23VehicleSpawn(this.tracker, 1);
            } else if (!(this.tracker instanceof IAnimal) && !(this.tracker instanceof EntityDragon)) {
                if (this.tracker instanceof EntityFishingHook) {
                    EntityPlayer entityhuman = ((EntityFishingHook) this.tracker).angler;

                    return new Packet23VehicleSpawn(this.tracker, 90, entityhuman != null ? entityhuman.entityId : this.tracker.entityId);
                } else if (this.tracker instanceof EntityArrow) {
                    Entity entity = ((EntityArrow) this.tracker).shootingEntity;

                    return new Packet23VehicleSpawn(this.tracker, 60, entity != null ? entity.entityId : this.tracker.entityId);
                } else if (this.tracker instanceof EntitySnowball) {
                    return new Packet23VehicleSpawn(this.tracker, 61);
                } else if (this.tracker instanceof EntityPotion) {
                    return new Packet23VehicleSpawn(this.tracker, 73, ((EntityPotion) this.tracker).getPotionDamage());
                } else if (this.tracker instanceof EntityExpBottle) {
                    return new Packet23VehicleSpawn(this.tracker, 75);
                } else if (this.tracker instanceof EntityEnderPearl) {
                    return new Packet23VehicleSpawn(this.tracker, 65);
                } else if (this.tracker instanceof EntityEnderSignal) {
                    return new Packet23VehicleSpawn(this.tracker, 72);
                } else {
                    Packet23VehicleSpawn packet23vehiclespawn;

                    if (this.tracker instanceof EntitySmallFireball) {
                        EntitySmallFireball entitysmallfireball = (EntitySmallFireball) this.tracker;

                        packet23vehiclespawn = null;
                        if (entitysmallfireball.shootingEntity != null) {
                            packet23vehiclespawn = new Packet23VehicleSpawn(this.tracker, 64, entitysmallfireball.shootingEntity.entityId);
                        } else {
                            packet23vehiclespawn = new Packet23VehicleSpawn(this.tracker, 64, 0);
                        }

                        packet23vehiclespawn.e = (int) (entitysmallfireball.accelerationX * 8000.0D);
                        packet23vehiclespawn.f = (int) (entitysmallfireball.accelerationY * 8000.0D);
                        packet23vehiclespawn.g = (int) (entitysmallfireball.accelerationZ * 8000.0D);
                        return packet23vehiclespawn;
                    } else if (this.tracker instanceof EntityFireball) {
                        EntityFireball entityfireball = (EntityFireball) this.tracker;

                        packet23vehiclespawn = null;
                        if (entityfireball.shootingEntity != null) {
                            packet23vehiclespawn = new Packet23VehicleSpawn(this.tracker, 63, ((EntityFireball) this.tracker).shootingEntity.entityId);
                        } else {
                            packet23vehiclespawn = new Packet23VehicleSpawn(this.tracker, 63, 0);
                        }

                        packet23vehiclespawn.e = (int) (entityfireball.accelerationX * 8000.0D);
                        packet23vehiclespawn.f = (int) (entityfireball.accelerationY * 8000.0D);
                        packet23vehiclespawn.g = (int) (entityfireball.accelerationZ * 8000.0D);
                        return packet23vehiclespawn;
                    } else if (this.tracker instanceof EntityEgg) {
                        return new Packet23VehicleSpawn(this.tracker, 62);
                    } else if (this.tracker instanceof EntityTNTPrimed) {
                        return new Packet23VehicleSpawn(this.tracker, 50);
                    } else if (this.tracker instanceof EntityEnderCrystal) {
                        return new Packet23VehicleSpawn(this.tracker, 51);
                    } else if (this.tracker instanceof EntityFallingBlock) {
                        EntityFallingBlock entityfallingblock = (EntityFallingBlock) this.tracker;

                        return new Packet23VehicleSpawn(this.tracker, 70, entityfallingblock.blockID | entityfallingblock.field_70285_b << 16);
                    } else if (this.tracker instanceof EntityPainting) {
                        return new Packet25EntityPainting((EntityPainting) this.tracker);
                    } else if (this.tracker instanceof EntityXPOrb) {
                        return new Packet26AddExpOrb((EntityXPOrb) this.tracker);
                    } else {
                        throw new IllegalArgumentException("Don\'t know how to add " + this.tracker.getClass() + "!");
                    }
                }
            } else {
                this.i = MathHelper.d(this.tracker.am() * 256.0F / 360.0F);
                return new Packet24MobSpawn((EntityLiving) this.tracker);
            }
        }
    }

    public void clear(EntityPlayerMP entityplayer) {
        if (this.trackedPlayers.contains(entityplayer)) {
            this.trackedPlayers.remove(entityplayer);
            entityplayer.g.add(Integer.valueOf(this.tracker.entityId));
        }
    }
}
