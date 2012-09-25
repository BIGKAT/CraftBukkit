package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
// CraftBukkit end

public abstract class Entity {

    private static int entityCount = 0;
    public int entityId;
    public double l;
    public boolean m;
    public Entity riddenByEntity;
    public Entity ridingEntity;
    public World worldObj;
    public double lastX;
    public double lastY;
    public double lastZ;
    public double posX;
    public double posY;
    public double posZ;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float rotationYaw;
    public float rotationPitch;
    public float lastYaw;
    public float lastPitch;
    public final AxisAlignedBB boundingBox;
    public boolean onGround;
    public boolean positionChanged;
    public boolean G;
    public boolean H;
    public boolean velocityChanged;
    protected boolean J;
    public boolean K;
    public boolean isDead;
    public float yOffset;
    public float width;
    public float length;
    public float P;
    public float Q;
    public float fallDistance;
    private int b;
    public double S;
    public double T;
    public double U;
    public float V;
    public float W;
    public boolean X;
    public float Y;
    protected Random random;
    public int ticksExisted;
    public int fireResistance;
    public int fire; // CraftBukkit - private -> public
    protected boolean ac;
    public int hurtResistantTime;
    private boolean justCreated;
    protected boolean fireProof;
    protected DataWatcher datawatcher;
    private double e;
    private double f;
    public boolean ag;
    public int ah;
    public int ai;
    public int aj;
    public boolean ak;
    public boolean al;
    public EnumEntitySize am;
    public UUID uniqueId = UUID.randomUUID(); // CraftBukkit
    public boolean valid = false; // CraftBukkit

    public Entity(World world) {
        this.entityId = entityCount++;
        this.l = 1.0D;
        this.m = false;
        this.boundingBox = AxisAlignedBB.a(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.onGround = false;
        this.H = false;
        this.velocityChanged = false;
        this.K = true;
        this.isDead = false;
        this.yOffset = 0.0F;
        this.width = 0.6F;
        this.length = 1.8F;
        this.P = 0.0F;
        this.Q = 0.0F;
        this.fallDistance = 0.0F;
        this.b = 1;
        this.V = 0.0F;
        this.W = 0.0F;
        this.X = false;
        this.Y = 0.0F;
        this.random = new Random();
        this.ticksExisted = 0;
        this.fireResistance = 1;
        this.fire = 0;
        this.ac = false;
        this.hurtResistantTime = 0;
        this.justCreated = true;
        this.fireProof = false;
        this.datawatcher = new DataWatcher();
        this.ag = false;
        this.am = EnumEntitySize.SIZE_2;
        this.worldObj = world;
        this.setPosition(0.0D, 0.0D, 0.0D);
        this.datawatcher.a(0, Byte.valueOf((byte) 0));
        this.datawatcher.a(1, Short.valueOf((short) 300));
        this.entityInit();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher() {
        return this.datawatcher;
    }

    public boolean equals(Object object) {
        return object instanceof Entity ? ((Entity) object).entityId == this.entityId : false;
    }

    public int hashCode() {
        return this.entityId;
    }

    public void setDead() {
        this.isDead = true;
    }

    protected void setSize(float f, float f1) {
        this.width = f;
        this.length = f1;
        float f2 = f % 2.0F;

        if ((double) f2 < 0.375D) {
            this.am = EnumEntitySize.SIZE_1;
        } else if ((double) f2 < 0.75D) {
            this.am = EnumEntitySize.SIZE_2;
        } else if ((double) f2 < 1.0D) {
            this.am = EnumEntitySize.SIZE_3;
        } else if ((double) f2 < 1.375D) {
            this.am = EnumEntitySize.SIZE_4;
        } else if ((double) f2 < 1.75D) {
            this.am = EnumEntitySize.SIZE_5;
        } else {
            this.am = EnumEntitySize.SIZE_6;
        }
    }

    protected void b(float f, float f1) {
        // CraftBukkit start - yaw was sometimes set to NaN, so we need to set it back to 0
        if (Float.isNaN(f)) {
            f = 0;
        }

        if ((f == Float.POSITIVE_INFINITY) || (f == Float.NEGATIVE_INFINITY)) {
            if (this instanceof EntityPlayerMP) {
                System.err.println(((CraftPlayer) this.getBukkitEntity()).getName() + " was caught trying to crash the server with an invalid yaw");
                ((CraftPlayer) this.getBukkitEntity()).kickPlayer("Nope");
            }
            f = 0;
        }

        // pitch was sometimes set to NaN, so we need to set it back to 0.
        if (Float.isNaN(f1)) {
            f1 = 0;
        }

        if ((f1 == Float.POSITIVE_INFINITY) || (f1 == Float.NEGATIVE_INFINITY)) {
            if (this instanceof EntityPlayerMP) {
                System.err.println(((CraftPlayer) this.getBukkitEntity()).getName() + " was caught trying to crash the server with an invalid pitch");
                ((CraftPlayer) this.getBukkitEntity()).kickPlayer("Nope");
            }
            f1 = 0;
        }
        // CraftBukkit end

        this.rotationYaw = f % 360.0F;
        this.rotationPitch = f1 % 360.0F;
    }

    public void setPosition(double d0, double d1, double d2) {
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        float f = this.width / 2.0F;
        float f1 = this.length;

        this.boundingBox.b(d0 - (double) f, d1 - (double) this.yOffset + (double) this.V, d2 - (double) f, d0 + (double) f, d1 - (double) this.yOffset + (double) this.V + (double) f1, d2 + (double) f);
    }

    public void h_() {
        this.z();
    }

    public void z() {
        // this.world.methodProfiler.a("entityBaseTick"); // CraftBukkit - not in production code
        if (this.ridingEntity != null && this.ridingEntity.isDead) {
            this.ridingEntity = null;
        }

        ++this.ticksExisted;
        this.P = this.Q;
        this.lastX = this.posX;
        this.lastY = this.posY;
        this.lastZ = this.posZ;
        this.lastPitch = this.rotationPitch;
        this.lastYaw = this.rotationYaw;
        int i;

        if (this.isSprinting() && !this.H()) {
            int j = MathHelper.floor(this.posX);
            int k = MathHelper.floor(this.posY - 0.20000000298023224D - (double) this.yOffset);

            i = MathHelper.floor(this.posZ);
            int l = this.worldObj.getBlockId(j, k, i);

            if (l > 0) {
                this.worldObj.a("tilecrack_" + l, this.posX + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, this.boundingBox.b + 0.1D, this.posZ + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D);
            }
        }

        if (this.I()) {
            if (!this.ac && !this.justCreated) {
                float f = MathHelper.sqrt(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;

                if (f > 1.0F) {
                    f = 1.0F;
                }

                this.worldObj.makeSound(this, "random.splash", f, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                float f1 = (float) MathHelper.floor(this.boundingBox.b);

                float f2;
                float f3;

                for (i = 0; (float) i < 1.0F + this.width * 20.0F; ++i) {
                    f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.worldObj.a("bubble", this.posX + (double) f3, (double) (f1 + 1.0F), this.posZ + (double) f2, this.motionX, this.motionY - (double) (this.random.nextFloat() * 0.2F), this.motionZ);
                }

                for (i = 0; (float) i < 1.0F + this.width * 20.0F; ++i) {
                    f3 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.worldObj.a("splash", this.posX + (double) f3, (double) (f1 + 1.0F), this.posZ + (double) f2, this.motionX, this.motionY, this.motionZ);
                }
            }

            this.fallDistance = 0.0F;
            this.ac = true;
            this.fire = 0;
        } else {
            this.ac = false;
        }

        if (this.worldObj.isStatic) {
            this.fire = 0;
        } else if (this.fire > 0) {
            if (this.fireProof) {
                this.fire -= 4;
                if (this.fire < 0) {
                    this.fire = 0;
                }
            } else {
                if (this.fire % 20 == 0) {
                    // CraftBukkit start - TODO: this event spams!
                    if (this instanceof net.minecraft.src.EntityLiving) {
                        EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FIRE_TICK, 1);
                        this.worldObj.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            event.getEntity().setLastDamageCause(event);
                            this.damageEntity(DamageSource.BURN, event.getDamage());
                        }
                    } else {
                        this.damageEntity(DamageSource.BURN, 1);
                    }
                    // CraftBukkit end
                }

                --this.fire;
            }
        }

        if (this.J()) {
            this.A();
            this.fallDistance *= 0.5F;
        }

        if (this.posY < -64.0D) {
            this.C();
        }

        if (!this.worldObj.isStatic) {
            this.a(0, this.fire > 0);
            this.a(2, this.ridingEntity != null);
        }

        this.justCreated = false;
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
    }

    protected void A() {
        if (!this.fireProof) {
            // CraftBukkit start - fallen in lava TODO: this event spams!
            if (this instanceof net.minecraft.src.EntityLiving) {
                Server server = this.worldObj.getServer();

                // TODO: shouldn't be sending null for the block.
                org.bukkit.block.Block damager = null; // ((WorldServer) this.l).getWorld().getBlockAt(i, j, k);
                org.bukkit.entity.Entity damagee = this.getBukkitEntity();

                EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(damager, damagee, EntityDamageEvent.DamageCause.LAVA, 4);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    damagee.setLastDamageCause(event);
                    this.damageEntity(DamageSource.LAVA, event.getDamage());
                }

                if (this.fire <= 0) {
                    // not on fire yet
                    EntityCombustEvent combustEvent = new org.bukkit.event.entity.EntityCombustByBlockEvent(damager, damagee, 15);
                    server.getPluginManager().callEvent(combustEvent);

                    if (!combustEvent.isCancelled()) {
                        this.setOnFire(combustEvent.getDuration());
                    }
                } else {
                    // This will be called every single tick the entity is in lava, so don't throw an event
                    this.setOnFire(15);
                }
                return;
            }
            // CraftBukkit end - we also don't throw an event unless the object in lava is living, to save on some event calls

            this.damageEntity(DamageSource.LAVA, 4);
            this.setOnFire(15);
        }
    }

    public void setOnFire(int i) {
        int j = i * 20;

        if (this.fire < j) {
            this.fire = j;
        }
    }

    public void extinguish() {
        this.fire = 0;
    }

    protected void C() {
        this.setDead();
    }

    public boolean c(double d0, double d1, double d2) {
        AxisAlignedBB axisalignedbb = this.boundingBox.c(d0, d1, d2);
        List list = this.worldObj.getCubes(this, axisalignedbb);

        return !list.isEmpty() ? false : !this.worldObj.containsLiquid(axisalignedbb);
    }

    public void move(double d0, double d1, double d2) {
        if (this.X) {
            this.boundingBox.d(d0, d1, d2);
            this.posX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.posY = this.boundingBox.b + (double) this.yOffset - (double) this.V;
            this.posZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
        } else {
            // this.world.methodProfiler.a("move"); // CraftBukkit - not in production code
            this.V *= 0.4F;
            double d3 = this.posX;
            double d4 = this.posZ;

            if (this.J) {
                this.J = false;
                d0 *= 0.25D;
                d1 *= 0.05000000074505806D;
                d2 *= 0.25D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            double d5 = d0;
            double d6 = d1;
            double d7 = d2;
            AxisAlignedBB axisalignedbb = this.boundingBox.clone();
            boolean flag = this.onGround && this.isSneaking() && this instanceof net.minecraft.src.EntityPlayer;

            if (flag) {
                double d8;

                for (d8 = 0.05D; d0 != 0.0D && this.worldObj.getCubes(this, this.boundingBox.c(d0, -1.0D, 0.0D)).isEmpty(); d5 = d0) {
                    if (d0 < d8 && d0 >= -d8) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= d8;
                    } else {
                        d0 += d8;
                    }
                }

                for (; d2 != 0.0D && this.worldObj.getCubes(this, this.boundingBox.c(0.0D, -1.0D, d2)).isEmpty(); d7 = d2) {
                    if (d2 < d8 && d2 >= -d8) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= d8;
                    } else {
                        d2 += d8;
                    }
                }

                while (d0 != 0.0D && d2 != 0.0D && this.worldObj.getCubes(this, this.boundingBox.c(d0, -1.0D, d2)).isEmpty()) {
                    if (d0 < d8 && d0 >= -d8) {
                        d0 = 0.0D;
                    } else if (d0 > 0.0D) {
                        d0 -= d8;
                    } else {
                        d0 += d8;
                    }

                    if (d2 < d8 && d2 >= -d8) {
                        d2 = 0.0D;
                    } else if (d2 > 0.0D) {
                        d2 -= d8;
                    } else {
                        d2 += d8;
                    }

                    d5 = d0;
                    d7 = d2;
                }
            }

            List list = this.worldObj.getCubes(this, this.boundingBox.a(d0, d1, d2));

            AxisAlignedBB axisalignedbb1;

            for (Iterator iterator = list.iterator(); iterator.hasNext(); d1 = axisalignedbb1.b(this.boundingBox, d1)) {
                axisalignedbb1 = (AxisAlignedBB) iterator.next();
            }

            this.boundingBox.d(0.0D, d1, 0.0D);
            if (!this.K && d6 != d1) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            boolean flag1 = this.onGround || d6 != d1 && d6 < 0.0D;

            AxisAlignedBB axisalignedbb2;
            Iterator iterator1;

            for (iterator1 = list.iterator(); iterator1.hasNext(); d0 = axisalignedbb2.a(this.boundingBox, d0)) {
                axisalignedbb2 = (AxisAlignedBB) iterator1.next();
            }

            this.boundingBox.d(d0, 0.0D, 0.0D);
            if (!this.K && d5 != d0) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            for (iterator1 = list.iterator(); iterator1.hasNext(); d2 = axisalignedbb2.c(this.boundingBox, d2)) {
                axisalignedbb2 = (AxisAlignedBB) iterator1.next();
            }

            this.boundingBox.d(0.0D, 0.0D, d2);
            if (!this.K && d7 != d2) {
                d2 = 0.0D;
                d1 = 0.0D;
                d0 = 0.0D;
            }

            double d9;
            double d10;

            if (this.W > 0.0F && flag1 && (flag || this.V < 0.05F) && (d5 != d0 || d7 != d2)) {
                d9 = d0;
                d10 = d1;
                double d11 = d2;

                d0 = d5;
                d1 = (double) this.W;
                d2 = d7;
                AxisAlignedBB axisalignedbb3 = this.boundingBox.clone();

                this.boundingBox.c(axisalignedbb);
                list = this.worldObj.getCubes(this, this.boundingBox.a(d5, d1, d7));

                Iterator iterator2;
                AxisAlignedBB axisalignedbb4;

                for (iterator2 = list.iterator(); iterator2.hasNext(); d1 = axisalignedbb4.b(this.boundingBox, d1)) {
                    axisalignedbb4 = (AxisAlignedBB) iterator2.next();
                }

                this.boundingBox.d(0.0D, d1, 0.0D);
                if (!this.K && d6 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (iterator2 = list.iterator(); iterator2.hasNext(); d0 = axisalignedbb4.a(this.boundingBox, d0)) {
                    axisalignedbb4 = (AxisAlignedBB) iterator2.next();
                }

                this.boundingBox.d(d0, 0.0D, 0.0D);
                if (!this.K && d5 != d0) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                for (iterator2 = list.iterator(); iterator2.hasNext(); d2 = axisalignedbb4.c(this.boundingBox, d2)) {
                    axisalignedbb4 = (AxisAlignedBB) iterator2.next();
                }

                this.boundingBox.d(0.0D, 0.0D, d2);
                if (!this.K && d7 != d2) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                }

                if (!this.K && d6 != d1) {
                    d2 = 0.0D;
                    d1 = 0.0D;
                    d0 = 0.0D;
                } else {
                    d1 = (double) (-this.W);

                    for (iterator2 = list.iterator(); iterator2.hasNext(); d1 = axisalignedbb4.b(this.boundingBox, d1)) {
                        axisalignedbb4 = (AxisAlignedBB) iterator2.next();
                    }

                    this.boundingBox.d(0.0D, d1, 0.0D);
                }

                if (d9 * d9 + d11 * d11 >= d0 * d0 + d2 * d2) {
                    d0 = d9;
                    d1 = d10;
                    d2 = d11;
                    this.boundingBox.c(axisalignedbb3);
                } else {
                    double d12 = this.boundingBox.b - (double) ((int) this.boundingBox.b);

                    if (d12 > 0.0D) {
                        this.V = (float) ((double) this.V + d12 + 0.01D);
                    }
                }
            }

            // this.world.methodProfiler.b(); // CraftBukkit - not in production code
            // this.world.methodProfiler.a("rest"); // CraftBukkit - not in production code
            this.posX = (this.boundingBox.a + this.boundingBox.d) / 2.0D;
            this.posY = this.boundingBox.b + (double) this.yOffset - (double) this.V;
            this.posZ = (this.boundingBox.c + this.boundingBox.f) / 2.0D;
            this.positionChanged = d5 != d0 || d7 != d2;
            this.G = d6 != d1;
            this.onGround = d6 != d1 && d6 < 0.0D;
            this.H = this.positionChanged || this.G;
            this.a(d1, this.onGround);
            if (d5 != d0) {
                this.motionX = 0.0D;
            }

            if (d6 != d1) {
                this.motionY = 0.0D;
            }

            if (d7 != d2) {
                this.motionZ = 0.0D;
            }

            d9 = this.posX - d3;
            d10 = this.posZ - d4;

            // CraftBukkit start
            if ((this.positionChanged) && (this.getBukkitEntity() instanceof Vehicle)) {
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.block.Block block = this.worldObj.getWorld().getBlockAt(MathHelper.floor(this.posX), MathHelper.floor(this.posY - (double) this.yOffset), MathHelper.floor(this.posZ));

                if (d5 > d0) {
                    block = block.getRelative(BlockFace.SOUTH);
                } else if (d5 < d0) {
                    block = block.getRelative(BlockFace.NORTH);
                } else if (d7 > d2) {
                    block = block.getRelative(BlockFace.WEST);
                } else if (d7 < d2) {
                    block = block.getRelative(BlockFace.EAST);
                }

                VehicleBlockCollisionEvent event = new VehicleBlockCollisionEvent(vehicle, block);
                this.worldObj.getServer().getPluginManager().callEvent(event);
            }
            // CraftBukkit end

            if (this.e_() && !flag && this.ridingEntity == null) {
                this.Q = (float) ((double) this.Q + (double) MathHelper.sqrt(d9 * d9 + d10 * d10) * 0.6D);
                int i = MathHelper.floor(this.posX);
                int j = MathHelper.floor(this.posY - 0.20000000298023224D - (double) this.yOffset);
                int k = MathHelper.floor(this.posZ);
                int l = this.worldObj.getBlockId(i, j, k);

                if (l == 0 && this.worldObj.getBlockId(i, j - 1, k) == Block.FENCE.blockID) {
                    l = this.worldObj.getBlockId(i, j - 1, k);
                }

                if (this.Q > (float) this.b && l > 0) {
                    this.b = (int) this.Q + 1;
                    this.a(i, j, k, l);
                    Block.blocksList[l].b(this.worldObj, i, j, k, this);
                }
            }

            this.D();
            boolean flag2 = this.G();

            if (this.worldObj.e(this.boundingBox.shrink(0.001D, 0.001D, 0.001D))) {
                this.burn(1);
                if (!flag2) {
                    ++this.fire;
                    // CraftBukkit start - not on fire yet
                    if (this.fire <= 0) { // only throw events on the first combust, otherwise it spams
                        EntityCombustEvent event = new EntityCombustEvent(this.getBukkitEntity(), 8);
                        this.worldObj.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            this.setOnFire(event.getDuration());
                        }
                    } else {
                        // CraftBukkit end
                        this.setOnFire(8);
                    }
                }
            } else if (this.fire <= 0) {
                this.fire = -this.fireResistance;
            }

            if (flag2 && this.fire > 0) {
                this.worldObj.makeSound(this, "random.fizz", 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                this.fire = -this.fireResistance;
            }

            // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        }
    }

    protected void D() {
        int i = MathHelper.floor(this.boundingBox.a + 0.001D);
        int j = MathHelper.floor(this.boundingBox.b + 0.001D);
        int k = MathHelper.floor(this.boundingBox.c + 0.001D);
        int l = MathHelper.floor(this.boundingBox.d - 0.001D);
        int i1 = MathHelper.floor(this.boundingBox.e - 0.001D);
        int j1 = MathHelper.floor(this.boundingBox.f - 0.001D);

        if (this.worldObj.c(i, j, k, l, i1, j1)) {
            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = j; l1 <= i1; ++l1) {
                    for (int i2 = k; i2 <= j1; ++i2) {
                        int j2 = this.worldObj.getBlockId(k1, l1, i2);

                        if (j2 > 0) {
                            Block.blocksList[j2].a(this.worldObj, k1, l1, i2, this);
                        }
                    }
                }
            }
        }
    }

    protected void a(int i, int j, int k, int l) {
        StepSound stepsound = Block.blocksList[l].stepSound;

        if (this.worldObj.getBlockId(i, j + 1, k) == Block.SNOW.blockID) {
            stepsound = Block.SNOW.stepSound;
            this.worldObj.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
        } else if (!Block.blocksList[l].blockMaterial.isLiquid()) {
            this.worldObj.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.15F, stepsound.getVolume2());
        }
    }

    protected boolean e_() {
        return true;
    }

    protected void a(double d0, boolean flag) {
        if (flag) {
            if (this.fallDistance > 0.0F) {
                if (this instanceof net.minecraft.src.EntityLiving) {
                    int i = MathHelper.floor(this.posX);
                    int j = MathHelper.floor(this.posY - 0.20000000298023224D - (double) this.yOffset);
                    int k = MathHelper.floor(this.posZ);
                    int l = this.worldObj.getBlockId(i, j, k);

                    if (l == 0 && this.worldObj.getBlockId(i, j - 1, k) == Block.FENCE.blockID) {
                        l = this.worldObj.getBlockId(i, j - 1, k);
                    }

                    if (l > 0) {
                        Block.blocksList[l].a(this.worldObj, i, j, k, this, this.fallDistance);
                    }
                }

                this.a(this.fallDistance);
                this.fallDistance = 0.0F;
            }
        } else if (d0 < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - d0);
        }
    }

    public AxisAlignedBB E() {
        return null;
    }

    protected void burn(int i) {
        if (!this.fireProof) {
            // CraftBukkit start
            if (this instanceof net.minecraft.src.EntityLiving) {
                EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FIRE, i);
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                i = event.getDamage();
                event.getEntity().setLastDamageCause(event);
            }
            // CraftBukkit end

            this.damageEntity(DamageSource.FIRE, i);
        }
    }

    public final boolean isFireproof() {
        return this.fireProof;
    }

    protected void a(float f) {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.a(f);
        }
    }

    public boolean G() {
        return this.ac || this.worldObj.B(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
    }

    public boolean H() {
        return this.ac;
    }

    public boolean I() {
        return this.worldObj.a(this.boundingBox.grow(0.0D, -0.4000000059604645D, 0.0D).shrink(0.001D, 0.001D, 0.001D), Material.WATER, this);
    }

    public boolean a(Material material) {
        double d0 = this.posY + (double) this.getHeadHeight();
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.d((float) MathHelper.floor(d0));
        int k = MathHelper.floor(this.posZ);
        int l = this.worldObj.getBlockId(i, j, k);

        if (l != 0 && Block.blocksList[l].blockMaterial == material) {
            float f = BlockFluids.d(this.worldObj.getData(i, j, k)) - 0.11111111F;
            float f1 = (float) (j + 1) - f;

            return d0 < (double) f1;
        } else {
            return false;
        }
    }

    public float getHeadHeight() {
        return 0.0F;
    }

    public boolean J() {
        return this.worldObj.a(this.boundingBox.grow(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.LAVA);
    }

    public void a(float f, float f1, float f2) {
        float f3 = f * f + f1 * f1;

        if (f3 >= 1.0E-4F) {
            f3 = MathHelper.c(f3);
            if (f3 < 1.0F) {
                f3 = 1.0F;
            }

            f3 = f2 / f3;
            f *= f3;
            f1 *= f3;
            float f4 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
            float f5 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);

            this.motionX += (double) (f * f5 - f1 * f4);
            this.motionZ += (double) (f1 * f5 + f * f4);
        }
    }

    public float c(float f) {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posZ);

        if (this.worldObj.isLoaded(i, 0, j)) {
            double d0 = (this.boundingBox.e - this.boundingBox.b) * 0.66D;
            int k = MathHelper.floor(this.posY - (double) this.yOffset + d0);

            return this.worldObj.o(i, k, j);
        } else {
            return 0.0F;
        }
    }

    public void spawnIn(net.minecraft.src.World world) {
        // CraftBukkit start
        if (world == null) {
            this.setDead();
            this.worldObj = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
            return;
        }
        // CraftBukkit end

        this.worldObj = world;
    }

    public void setPositionAndRotation(double d0, double d1, double d2, float f, float f1) {
        this.lastX = this.posX = d0;
        this.lastY = this.posY = d1;
        this.lastZ = this.posZ = d2;
        this.lastYaw = this.rotationYaw = f;
        this.lastPitch = this.rotationPitch = f1;
        this.V = 0.0F;
        double d3 = (double) (this.lastYaw - f);

        if (d3 < -180.0D) {
            this.lastYaw += 360.0F;
        }

        if (d3 >= 180.0D) {
            this.lastYaw -= 360.0F;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.b(f, f1);
    }

    public void setPositionRotation(double d0, double d1, double d2, float f, float f1) {
        this.S = this.lastX = this.posX = d0;
        this.T = this.lastY = this.posY = d1 + (double) this.yOffset;
        this.U = this.lastZ = this.posZ = d2;
        this.rotationYaw = f;
        this.rotationPitch = f1;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public float d(Entity entity) {
        float f = (float) (this.posX - entity.posX);
        float f1 = (float) (this.posY - entity.posY);
        float f2 = (float) (this.posZ - entity.posZ);

        return MathHelper.c(f * f + f1 * f1 + f2 * f2);
    }

    public double e(double d0, double d1, double d2) {
        double d3 = this.posX - d0;
        double d4 = this.posY - d1;
        double d5 = this.posZ - d2;

        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    public double f(double d0, double d1, double d2) {
        double d3 = this.posX - d0;
        double d4 = this.posY - d1;
        double d5 = this.posZ - d2;

        return (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
    }

    public double e(Entity entity) {
        double d0 = this.posX - entity.posX;
        double d1 = this.posY - entity.posY;
        double d2 = this.posZ - entity.posZ;

        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    public void b_(net.minecraft.src.EntityPlayer entityhuman) {}

    public void collide(Entity entity) {
        if (entity.riddenByEntity != this && entity.ridingEntity != this) {
            double d0 = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            double d2 = MathHelper.a(d0, d1);

            if (d2 >= 0.009999999776482582D) {
                d2 = (double) MathHelper.sqrt(d2);
                d0 /= d2;
                d1 /= d2;
                double d3 = 1.0D / d2;

                if (d3 > 1.0D) {
                    d3 = 1.0D;
                }

                d0 *= d3;
                d1 *= d3;
                d0 *= 0.05000000074505806D;
                d1 *= 0.05000000074505806D;
                d0 *= (double) (1.0F - this.Y);
                d1 *= (double) (1.0F - this.Y);
                this.g(-d0, 0.0D, -d1);
                entity.g(d0, 0.0D, d1);
            }
        }
    }

    public void g(double d0, double d1, double d2) {
        this.motionX += d0;
        this.motionY += d1;
        this.motionZ += d2;
        this.al = true;
    }

    protected void K() {
        this.velocityChanged = true;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.K();
        return false;
    }

    public boolean L() {
        return false;
    }

    public boolean M() {
        return false;
    }

    public void c(Entity entity, int i) {}

    public boolean c(net.minecraft.src.NBTTagCompound nbttagcompound) {
        String s = this.Q();

        if (!this.isDead && s != null) {
            nbttagcompound.setString("id", s);
            this.d(nbttagcompound);
            return true;
        } else {
            return false;
        }
    }

    public void d(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.set("Pos", this.a(new double[] { this.posX, this.posY + (double) this.V, this.posZ}));
        nbttagcompound.set("Motion", this.a(new double[] { this.motionX, this.motionY, this.motionZ}));

        // CraftBukkit start - checking for NaN pitch/yaw and resetting to zero
        // TODO: make sure this is the best way to address this.
        if (Float.isNaN(this.rotationYaw)) {
            this.rotationYaw = 0;
        }

        if (Float.isNaN(this.rotationPitch)) {
            this.rotationPitch = 0;
        }
        // CraftBukkit end

        nbttagcompound.set("Rotation", this.a(new float[] { this.rotationYaw, this.rotationPitch}));
        nbttagcompound.setFloat("FallDistance", this.fallDistance);
        nbttagcompound.setShort("Fire", (short) this.fire);
        nbttagcompound.setShort("Air", (short) this.getAir());
        nbttagcompound.setBoolean("OnGround", this.onGround);
        // CraftBukkit start
        nbttagcompound.setLong("WorldUUIDLeast", this.worldObj.getDataManager().getUUID().getLeastSignificantBits());
        nbttagcompound.setLong("WorldUUIDMost", this.worldObj.getDataManager().getUUID().getMostSignificantBits());
        nbttagcompound.setLong("UUIDLeast", this.uniqueId.getLeastSignificantBits());
        nbttagcompound.setLong("UUIDMost", this.uniqueId.getMostSignificantBits());
        // CraftBukkit end
        this.readEntityFromNBT(nbttagcompound);
    }

    public void e(net.minecraft.src.NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getList("Pos");
        NBTTagList nbttaglist1 = nbttagcompound.getList("Motion");
        NBTTagList nbttaglist2 = nbttagcompound.getList("Rotation");

        this.motionX = ((NBTTagDouble) nbttaglist1.get(0)).data;
        this.motionY = ((NBTTagDouble) nbttaglist1.get(1)).data;
        this.motionZ = ((NBTTagDouble) nbttaglist1.get(2)).data;
        /* CraftBukkit start - moved section down
        if (Math.abs(this.motX) > 10.0D) {
            this.motX = 0.0D;
        }

        if (Math.abs(this.motY) > 10.0D) {
            this.motY = 0.0D;
        }

        if (Math.abs(this.motZ) > 10.0D) {
            this.motZ = 0.0D;
        }
        // CraftBukkit end */

        this.lastX = this.S = this.posX = ((NBTTagDouble) nbttaglist.get(0)).data;
        this.lastY = this.T = this.posY = ((NBTTagDouble) nbttaglist.get(1)).data;
        this.lastZ = this.U = this.posZ = ((NBTTagDouble) nbttaglist.get(2)).data;
        this.lastYaw = this.rotationYaw = ((NBTTagFloat) nbttaglist2.get(0)).data;
        this.lastPitch = this.rotationPitch = ((NBTTagFloat) nbttaglist2.get(1)).data;
        this.fallDistance = nbttagcompound.getFloat("FallDistance");
        this.fire = nbttagcompound.getShort("Fire");
        this.setAir(nbttagcompound.getShort("Air"));
        this.onGround = nbttagcompound.getBoolean("OnGround");
        this.setPosition(this.posX, this.posY, this.posZ);

        // CraftBukkit start
        long least = nbttagcompound.getLong("UUIDLeast");
        long most = nbttagcompound.getLong("UUIDMost");

        if (least != 0L && most != 0L) {
            this.uniqueId = new UUID(most, least);
        }
        // CraftBukkit end

        this.b(this.rotationYaw, this.rotationPitch);
        this.writeEntityToNBT(nbttagcompound);

        // CraftBukkit start - exempt Vehicles from notch's sanity check
        if (!(this.getBukkitEntity() instanceof Vehicle)) {
            if (Math.abs(this.motionX) > 10.0D) {
                this.motionX = 0.0D;
            }

            if (Math.abs(this.motionY) > 10.0D) {
                this.motionY = 0.0D;
            }

            if (Math.abs(this.motionZ) > 10.0D) {
                this.motionZ = 0.0D;
            }
        }
        // CraftBukkit end

        // CraftBukkit start - reset world
        if (this instanceof EntityPlayerMP) {
            Server server = Bukkit.getServer();
            org.bukkit.World bworld = null;

            // TODO: Remove World related checks, replaced with WorldUID.
            String worldName = nbttagcompound.getString("World");

            if (nbttagcompound.hasKey("WorldUUIDMost") && nbttagcompound.hasKey("WorldUUIDLeast")) {
                UUID uid = new UUID(nbttagcompound.getLong("WorldUUIDMost"), nbttagcompound.getLong("WorldUUIDLeast"));
                bworld = server.getWorld(uid);
            } else {
                bworld = server.getWorld(worldName);
            }

            if (bworld == null) {
                EntityPlayerMP entityPlayer = (EntityPlayerMP) this;
                bworld = ((org.bukkit.craftbukkit.CraftServer) server).getServer().getWorldServer(entityPlayer.dimension).getWorld();
            }

            this.spawnIn(bworld == null ? null : ((CraftWorld) bworld).getHandle());
        }
        // CraftBukkit end
    }

    protected final String Q() {
        return EntityTypes.b(this);
    }

    protected abstract void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound);

    protected abstract void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound);

    protected NBTTagList a(double... adouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble1 = adouble;
        int i = adouble.length;

        for (int j = 0; j < i; ++j) {
            double d0 = adouble1[j];

            nbttaglist.add(new NBTTagDouble((String) null, d0));
        }

        return nbttaglist;
    }

    protected NBTTagList a(float... afloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat1 = afloat;
        int i = afloat.length;

        for (int j = 0; j < i; ++j) {
            float f = afloat1[j];

            nbttaglist.add(new NBTTagFloat((String) null, f));
        }

        return nbttaglist;
    }

    public net.minecraft.src.EntityItem b(int i, int j) {
        return this.a(i, j, 0.0F);
    }

    public net.minecraft.src.EntityItem a(int i, int j, float f) {
        return this.a(new net.minecraft.src.ItemStack(i, j, 0), f);
    }

    public net.minecraft.src.EntityItem a(net.minecraft.src.ItemStack itemstack, float f) {
        net.minecraft.src.EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY + (double) f, this.posZ, itemstack);

        entityitem.delayBeforeCanPickup = 10;
        this.worldObj.addEntity(entityitem);
        return entityitem;
    }

    public boolean isEntityAlive() {
        return !this.isDead;
    }

    public boolean inBlock() {
        for (int i = 0; i < 8; ++i) {
            float f = ((float) ((i >> 0) % 2) - 0.5F) * this.width * 0.8F;
            float f1 = ((float) ((i >> 1) % 2) - 0.5F) * 0.1F;
            float f2 = ((float) ((i >> 2) % 2) - 0.5F) * this.width * 0.8F;
            int j = MathHelper.floor(this.posX + (double) f);
            int k = MathHelper.floor(this.posY + (double) this.getHeadHeight() + (double) f1);
            int l = MathHelper.floor(this.posZ + (double) f2);

            if (this.worldObj.s(j, k, l)) {
                return true;
            }
        }

        return false;
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        return false;
    }

    public AxisAlignedBB g(Entity entity) {
        return null;
    }

    public void U() {
        if (this.ridingEntity.isDead) {
            this.ridingEntity = null;
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.h_();
            if (this.ridingEntity != null) {
                this.ridingEntity.V();
                this.f += (double) (this.ridingEntity.rotationYaw - this.ridingEntity.lastYaw);

                for (this.e += (double) (this.ridingEntity.rotationPitch - this.ridingEntity.lastPitch); this.f >= 180.0D; this.f -= 360.0D) {
                    ;
                }

                while (this.f < -180.0D) {
                    this.f += 360.0D;
                }

                while (this.e >= 180.0D) {
                    this.e -= 360.0D;
                }

                while (this.e < -180.0D) {
                    this.e += 360.0D;
                }

                double d0 = this.f * 0.5D;
                double d1 = this.e * 0.5D;
                float f = 10.0F;

                if (d0 > (double) f) {
                    d0 = (double) f;
                }

                if (d0 < (double) (-f)) {
                    d0 = (double) (-f);
                }

                if (d1 > (double) f) {
                    d1 = (double) f;
                }

                if (d1 < (double) (-f)) {
                    d1 = (double) (-f);
                }

                this.f -= d0;
                this.e -= d1;
                this.rotationYaw = (float) ((double) this.rotationYaw + d0);
                this.rotationPitch = (float) ((double) this.rotationPitch + d1);
            }
        }
    }

    public void V() {
        if (!(this.riddenByEntity instanceof net.minecraft.src.EntityPlayer) || !((EntityPlayer) this.riddenByEntity).bF()) {
            this.riddenByEntity.S = this.riddenByEntity.posX;
            this.riddenByEntity.T = this.riddenByEntity.posY;
            this.riddenByEntity.U = this.riddenByEntity.posZ;
        }

        this.riddenByEntity.setPosition(this.posX, this.posY + this.X() + this.riddenByEntity.W(), this.posZ);
    }

    public double W() {
        return (double) this.yOffset;
    }

    public double X() {
        return (double) this.length * 0.75D;
    }

    public void mount(Entity entity) {
        // CraftBukkit start
        this.setPassengerOf(entity);
    }

    protected org.bukkit.entity.Entity bukkitEntity;

    public org.bukkit.entity.Entity getBukkitEntity() {
        if (this.bukkitEntity == null) {
            this.bukkitEntity = org.bukkit.craftbukkit.entity.CraftEntity.getEntity(this.worldObj.getServer(), this);
        }
        return this.bukkitEntity;
    }

    public void setPassengerOf(Entity entity) {
        // b(null) doesn't really fly for overloaded methods,
        // so this method is needed

        PluginManager pluginManager = Bukkit.getPluginManager();
        this.getBukkitEntity(); // make sure bukkitEntity is initialised
        // CraftBukkit end
        this.e = 0.0D;
        this.f = 0.0D;
        if (entity == null) {
            if (this.ridingEntity != null) {
                // CraftBukkit start
                if ((this.bukkitEntity instanceof LivingEntity) && (this.ridingEntity.getBukkitEntity() instanceof Vehicle)) {
                    VehicleExitEvent event = new VehicleExitEvent((Vehicle) this.ridingEntity.getBukkitEntity(), (LivingEntity) this.bukkitEntity);
                    pluginManager.callEvent(event);
                }
                // CraftBukkit end

                this.setPositionRotation(this.ridingEntity.posX, this.ridingEntity.boundingBox.b + (double) this.ridingEntity.length, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
                this.ridingEntity.riddenByEntity = null;
            }

            this.ridingEntity = null;
        } else if (this.ridingEntity == entity) {
            // CraftBukkit start
            if ((this.bukkitEntity instanceof LivingEntity) && (this.ridingEntity.getBukkitEntity() instanceof Vehicle)) {
                VehicleExitEvent event = new VehicleExitEvent((Vehicle) this.ridingEntity.getBukkitEntity(), (LivingEntity) this.bukkitEntity);
                pluginManager.callEvent(event);
            }
            // CraftBukkit end

            this.h(entity);
            this.ridingEntity.riddenByEntity = null;
            this.ridingEntity = null;
        } else {
            // CraftBukkit start
            if ((this.bukkitEntity instanceof LivingEntity) && (entity.getBukkitEntity() instanceof Vehicle)) {
                VehicleEnterEvent event = new VehicleEnterEvent((Vehicle) entity.getBukkitEntity(), this.bukkitEntity);
                pluginManager.callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
            }
            // CraftBukkit end

            if (this.ridingEntity != null) {
                this.ridingEntity.riddenByEntity = null;
            }

            if (entity.riddenByEntity != null) {
                entity.riddenByEntity.ridingEntity = null;
            }

            this.ridingEntity = entity;
            entity.riddenByEntity = this;
        }
    }

    public void h(Entity entity) {
        double d0 = entity.posX;
        double d1 = entity.boundingBox.b + (double) entity.length;
        double d2 = entity.posZ;

        for (double d3 = -1.5D; d3 < 2.0D; ++d3) {
            for (double d4 = -1.5D; d4 < 2.0D; ++d4) {
                if (d3 != 0.0D || d4 != 0.0D) {
                    int i = (int) (this.posX + d3);
                    int j = (int) (this.posZ + d4);
                    AxisAlignedBB axisalignedbb = this.boundingBox.c(d3, 1.0D, d4);

                    if (this.worldObj.a(axisalignedbb).isEmpty()) {
                        if (this.worldObj.t(i, (int) this.posY, j)) {
                            this.setPositionRotation(this.posX + d3, this.posY + 1.0D, this.posZ + d4, this.rotationYaw, this.rotationPitch);
                            return;
                        }

                        if (this.worldObj.t(i, (int) this.posY - 1, j) || this.worldObj.getMaterial(i, (int) this.posY - 1, j) == Material.WATER) {
                            d0 = this.posX + d3;
                            d1 = this.posY + 1.0D;
                            d2 = this.posZ + d4;
                        }
                    }
                }
            }
        }

        this.setPositionRotation(d0, d1, d2, this.rotationYaw, this.rotationPitch);
    }

    public float Y() {
        return 0.1F;
    }

    public Vec3 Z() {
        return null;
    }

    public void aa() {}

    public ItemStack[] getEquipment() {
        return null;
    }

    public boolean isBurning() {
        return this.fire > 0 || this.f(0);
    }

    public boolean isSneaking() {
        return this.f(1);
    }

    public void setSneaking(boolean flag) {
        this.a(1, flag);
    }

    public boolean isSprinting() {
        return this.f(3);
    }

    public void setSprinting(boolean flag) {
        this.a(3, flag);
    }

    public void c(boolean flag) {
        this.a(4, flag);
    }

    protected boolean f(int i) {
        return (this.datawatcher.getByte(0) & 1 << i) != 0;
    }

    protected void a(int i, boolean flag) {
        byte b0 = this.datawatcher.getByte(0);

        if (flag) {
            this.datawatcher.watch(0, Byte.valueOf((byte) (b0 | 1 << i)));
        } else {
            this.datawatcher.watch(0, Byte.valueOf((byte) (b0 & ~(1 << i))));
        }
    }

    public int getAir() {
        return this.datawatcher.getShort(1);
    }

    public void setAir(int i) {
        this.datawatcher.watch(1, Short.valueOf((short) i));
    }

    public void a(EntityLightningBolt entitylightning) {
        // CraftBukkit start
        final org.bukkit.entity.Entity thisBukkitEntity = this.getBukkitEntity();
        final org.bukkit.entity.Entity stormBukkitEntity = entitylightning.getBukkitEntity();
        final PluginManager pluginManager = Bukkit.getPluginManager();

        if (thisBukkitEntity instanceof Painting) {
            PaintingBreakByEntityEvent event = new PaintingBreakByEntityEvent((Painting) thisBukkitEntity, stormBukkitEntity);
            pluginManager.callEvent(event);

            if (event.isCancelled()) {
                return;
            }
        }

        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(stormBukkitEntity, thisBukkitEntity, EntityDamageEvent.DamageCause.LIGHTNING, 5);
        pluginManager.callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        thisBukkitEntity.setLastDamageCause(event);
        this.burn(event.getDamage());
        // CraftBukkit end

        ++this.fire;
        if (this.fire == 0) {
            // CraftBukkit start - raise a combust event when lightning strikes
            EntityCombustByEntityEvent entityCombustEvent = new EntityCombustByEntityEvent(stormBukkitEntity, thisBukkitEntity, 8);
            pluginManager.callEvent(entityCombustEvent);
            if (!entityCombustEvent.isCancelled()) {
                this.setOnFire(entityCombustEvent.getDuration());
            }
            // CraftBukkit end
        }
    }

    public void a(EntityLiving entityliving) {}

    protected boolean i(double d0, double d1, double d2) {
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);
        double d3 = d0 - (double) i;
        double d4 = d1 - (double) j;
        double d5 = d2 - (double) k;

        if (this.worldObj.s(i, j, k)) {
            boolean flag = !this.worldObj.s(i - 1, j, k);
            boolean flag1 = !this.worldObj.s(i + 1, j, k);
            boolean flag2 = !this.worldObj.s(i, j - 1, k);
            boolean flag3 = !this.worldObj.s(i, j + 1, k);
            boolean flag4 = !this.worldObj.s(i, j, k - 1);
            boolean flag5 = !this.worldObj.s(i, j, k + 1);
            byte b0 = -1;
            double d6 = 9999.0D;

            if (flag && d3 < d6) {
                d6 = d3;
                b0 = 0;
            }

            if (flag1 && 1.0D - d3 < d6) {
                d6 = 1.0D - d3;
                b0 = 1;
            }

            if (flag2 && d4 < d6) {
                d6 = d4;
                b0 = 2;
            }

            if (flag3 && 1.0D - d4 < d6) {
                d6 = 1.0D - d4;
                b0 = 3;
            }

            if (flag4 && d5 < d6) {
                d6 = d5;
                b0 = 4;
            }

            if (flag5 && 1.0D - d5 < d6) {
                d6 = 1.0D - d5;
                b0 = 5;
            }

            float f = this.random.nextFloat() * 0.2F + 0.1F;

            if (b0 == 0) {
                this.motionX = (double) (-f);
            }

            if (b0 == 1) {
                this.motionX = (double) f;
            }

            if (b0 == 2) {
                this.motionY = (double) (-f);
            }

            if (b0 == 3) {
                this.motionY = (double) f;
            }

            if (b0 == 4) {
                this.motionZ = (double) (-f);
            }

            if (b0 == 5) {
                this.motionZ = (double) f;
            }

            return true;
        } else {
            return false;
        }
    }

    public void aj() {
        this.J = true;
        this.fallDistance = 0.0F;
    }

    public String getLocalizedName() {
        String s = EntityTypes.b(this);

        if (s == null) {
            s = "generic";
        }

        return LocaleI18n.get("entity." + s + ".name");
    }

    public Entity[] al() {
        return null;
    }

    public boolean i(Entity entity) {
        return this == entity;
    }

    public float am() {
        return 0.0F;
    }

    public boolean an() {
        return true;
    }

    public String toString() {
        return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", new Object[] { this.getClass().getSimpleName(), this.getLocalizedName(), Integer.valueOf(this.entityId), this.worldObj == null ? "~NULL~" : this.worldObj.getWorldData().getName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ)});
    }
}
