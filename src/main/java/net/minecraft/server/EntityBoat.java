package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
// CraftBukkit end

public class EntityBoat extends Entity {

    private boolean a;
    private double b;
    private int c;
    private double d;
    private double e;
    private double f;
    private double g;
    private double h;

    // CraftBukkit start
    public double maxSpeed = 0.4D;
    public double occupiedDeceleration = 0.2D;
    public double unoccupiedDeceleration = -1;
    public boolean landBoats = false;

    @Override
    public void collide(Entity entity) {
        org.bukkit.entity.Entity hitEntity = (entity == null) ? null : entity.getBukkitEntity();

        VehicleEntityCollisionEvent event = new VehicleEntityCollisionEvent((Vehicle) this.getBukkitEntity(), hitEntity);
        this.worldObj.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        super.collide(entity);
    }
    // CraftBukkit end

    public EntityBoat(World world) {
        super(world);
        this.a = true;
        this.b = 0.07D;
        this.m = true;
        this.a(1.5F, 0.6F);
        this.height = this.length / 2.0F;
    }

    protected boolean e_() {
        return false;
    }

    protected void a() {
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(1));
        this.datawatcher.a(19, new Integer(0));
    }

    public AxisAlignedBB g(Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB E() {
        return this.boundingBox;
    }

    public boolean M() {
        return true;
    }

    public EntityBoat(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1 + (double) this.height, d2);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;

        this.worldObj.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleCreateEvent((Vehicle) this.getBukkitEntity())); // CraftBukkit
    }

    public double X() {
        return (double) this.length * 0.0D - 0.30000001192092896D;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (!this.worldObj.isStatic && !this.isDead) {
            // CraftBukkit start
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();
            org.bukkit.entity.Entity attacker = (damagesource.getEntity() == null) ? null : damagesource.getEntity().getBukkitEntity();

            VehicleDamageEvent event = new VehicleDamageEvent(vehicle, attacker, i);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            // i = event.getDamage(); // TODO Why don't we do this?
            // CraftBukkit end

            this.c(-this.i());
            this.b(10);
            this.setDamage(this.getDamage() + i * 10);
            this.K();
            if (damagesource.getEntity() instanceof EntityHuman && ((EntityHuman) damagesource.getEntity()).capabilities.canInstantlyBuild) {
                this.setDamage(100);
            }

            if (this.getDamage() > 40) {
                // CraftBukkit start
                VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, attacker);
                this.worldObj.getServer().getPluginManager().callEvent(destroyEvent);

                if (destroyEvent.isCancelled()) {
                    this.setDamage(40); // Maximize damage so this doesn't get triggered again right away
                    return true;
                }
                // CraftBukkit end

                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mount(this);
                }

                this.a(Item.BOAT.id, 1, 0.0F);
                this.setDead();
            }

            return true;
        } else {
            return true;
        }
    }

    public boolean L() {
        return !this.isDead;
    }

    public void h_() {
        // CraftBukkit start
        double prevX = this.posX;
        double prevY = this.posY;
        double prevZ = this.posZ;
        float prevYaw = this.rotationYaw;
        float prevPitch = this.rotationPitch;
        // CraftBukkit end

        super.h_();
        if (this.h() > 0) {
            this.b(this.h() - 1);
        }

        if (this.getDamage() > 0) {
            this.setDamage(this.getDamage() - 1);
        }

        this.lastX = this.posX;
        this.lastY = this.posY;
        this.lastZ = this.posZ;
        byte b0 = 5;
        double d0 = 0.0D;

        for (int i = 0; i < b0; ++i) {
            double d1 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (i + 0) / (double) b0 - 0.125D;
            double d2 = this.boundingBox.b + (this.boundingBox.e - this.boundingBox.b) * (double) (i + 1) / (double) b0 - 0.125D;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.a().a(this.boundingBox.a, d1, this.boundingBox.c, this.boundingBox.d, d2, this.boundingBox.f);

            if (this.worldObj.b(axisalignedbb, Material.WATER)) {
                d0 += 1.0D / (double) b0;
            }
        }

        double d3 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        double d4;
        double d5;

        if (d3 > 0.26249999999999996D) {
            d4 = Math.cos((double) this.rotationYaw * 3.141592653589793D / 180.0D);
            d5 = Math.sin((double) this.rotationYaw * 3.141592653589793D / 180.0D);

            for (int j = 0; (double) j < 1.0D + d3 * 60.0D; ++j) {
                double d6 = (double) (this.random.nextFloat() * 2.0F - 1.0F);
                double d7 = (double) (this.random.nextInt(2) * 2 - 1) * 0.7D;
                double d8;
                double d9;

                if (this.random.nextBoolean()) {
                    d8 = this.posX - d4 * d6 * 0.8D + d5 * d7;
                    d9 = this.posZ - d5 * d6 * 0.8D - d4 * d7;
                    this.worldObj.a("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
                } else {
                    d8 = this.posX + d4 + d5 * d6 * 0.7D;
                    d9 = this.posZ + d5 - d4 * d6 * 0.7D;
                    this.worldObj.a("splash", d8, this.posY - 0.125D, d9, this.motionX, this.motionY, this.motionZ);
                }
            }
        }

        double d10;
        double d11;

        if (this.worldObj.isStatic && this.a) {
            if (this.c > 0) {
                d4 = this.posX + (this.d - this.posX) / (double) this.c;
                d5 = this.posY + (this.e - this.posY) / (double) this.c;
                d10 = this.posZ + (this.f - this.posZ) / (double) this.c;
                d11 = MathHelper.g(this.g - (double) this.rotationYaw);
                this.rotationYaw = (float) ((double) this.rotationYaw + d11 / (double) this.c);
                this.rotationPitch = (float) ((double) this.rotationPitch + (this.h - (double) this.rotationPitch) / (double) this.c);
                --this.c;
                this.setPosition(d4, d5, d10);
                this.b(this.rotationYaw, this.rotationPitch);
            } else {
                d4 = this.posX + this.motionX;
                d5 = this.posY + this.motionY;
                d10 = this.posZ + this.motionZ;
                this.setPosition(d4, d5, d10);
                if (this.onGround) {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
        } else {
            if (d0 < 1.0D) {
                d4 = d0 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * d4;
            } else {
                if (this.motionY < 0.0D) {
                    this.motionY /= 2.0D;
                }

                this.motionY += 0.007000000216066837D;
            }

            if (this.riddenByEntity != null) {
                this.motionX += this.riddenByEntity.motionX * this.b;
                this.motionZ += this.riddenByEntity.motionZ * this.b;
            }
            // CraftBukkit start - block not in vanilla
            else if (unoccupiedDeceleration >= 0) {
                this.motionX *= unoccupiedDeceleration;
                this.motionZ *= unoccupiedDeceleration;
                // Kill lingering speed
                if (motionX <= 0.00001) {
                    motionX = 0;
                }
                if (motionZ <= 0.00001) {
                    motionZ = 0;
                }
            }
            // CraftBukkit end

            d4 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d4 > 0.35D) {
                d5 = 0.35D / d4;
                this.motionX *= d5;
                this.motionZ *= d5;
                d4 = 0.35D;
            }

            if (d4 > d3 && this.b < 0.35D) {
                this.b += (0.35D - this.b) / 35.0D;
                if (this.b > 0.35D) {
                    this.b = 0.35D;
                }
            } else {
                this.b -= (this.b - 0.07D) / 35.0D;
                if (this.b < 0.07D) {
                    this.b = 0.07D;
                }
            }

            if (this.onGround && !this.landBoats) { // CraftBukkit
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
            }

            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.positionChanged && d3 > 0.2D) {
                if (!this.worldObj.isStatic) {
                    // CraftBukkit start
                    Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, null);
                    this.worldObj.getServer().getPluginManager().callEvent(destroyEvent);
                    if (!destroyEvent.isCancelled()) {
                        this.setDead();

                        int k;

                        for (k = 0; k < 3; ++k) {
                            this.a(Block.WOOD.blockID, 1, 0.0F);
                        }

                        for (k = 0; k < 2; ++k) {
                            this.a(Item.STICK.id, 1, 0.0F);
                        }
                    }
                    // CraftBukkit end
                }
            } else {
                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }

            this.rotationPitch = 0.0F;
            d5 = (double) this.rotationYaw;
            d10 = this.lastX - this.posX;
            d11 = this.lastZ - this.posZ;
            if (d10 * d10 + d11 * d11 > 0.001D) {
                d5 = (double) ((float) (Math.atan2(d11, d10) * 180.0D / 3.141592653589793D));
            }

            double d12 = MathHelper.g(d5 - (double) this.rotationYaw);

            if (d12 > 20.0D) {
                d12 = 20.0D;
            }

            if (d12 < -20.0D) {
                d12 = -20.0D;
            }

            this.rotationYaw = (float) ((double) this.rotationYaw + d12);
            this.b(this.rotationYaw, this.rotationPitch);

            // CraftBukkit start
            org.bukkit.Server server = this.worldObj.getServer();
            org.bukkit.World bworld = this.worldObj.getWorld();

            Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
            Location to = new Location(bworld, this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();

            server.getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));

            if (!from.equals(to)) {
                VehicleMoveEvent event = new VehicleMoveEvent(vehicle, from, to);
                server.getPluginManager().callEvent(event);
            }
            // CraftBukkit end

            if (!this.worldObj.isStatic) {
                List list = this.worldObj.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

                if (list != null && !list.isEmpty()) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        if (entity != this.riddenByEntity && entity.M() && entity instanceof EntityBoat) {
                            entity.collide(this);
                        }
                    }
                }

                for (int l = 0; l < 4; ++l) {
                    int i1 = MathHelper.floor(this.posX + ((double) (l % 2) - 0.5D) * 0.8D);
                    int j1 = MathHelper.floor(this.posZ + ((double) (l / 2) - 0.5D) * 0.8D);

                    for (int k1 = 0; k1 < 2; ++k1) {
                        int l1 = MathHelper.floor(this.posY) + k1;
                        int i2 = this.worldObj.getBlockId(i1, l1, j1);
                        int j2 = this.worldObj.getData(i1, l1, j1);

                        if (i2 == Block.SNOW.blockID) {
                            this.worldObj.setBlockWithNotify(i1, l1, j1, 0);
                        } else if (i2 == Block.WATER_LILY.blockID) {
                            Block.WATER_LILY.dropBlockAsItemWithChance(this.worldObj, i1, l1, j1, j2, 0.3F, 0);
                            this.worldObj.setBlockWithNotify(i1, l1, j1, 0);
                        }
                    }
                }

                if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity.ridingEntity = null; // CraftBukkit
                    this.riddenByEntity = null;
                }
            }
        }
    }

    public void V() {
        if (this.riddenByEntity != null) {
            double d0 = Math.cos((double) this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
            double d1 = Math.sin((double) this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;

            this.riddenByEntity.setPosition(this.posX + d0, this.posY + this.X() + this.riddenByEntity.W(), this.posZ + d1);
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {}

    protected void a(NBTTagCompound nbttagcompound) {}

    public boolean c(EntityHuman entityhuman) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityHuman && this.riddenByEntity != entityhuman) {
            return true;
        } else {
            if (!this.worldObj.isStatic) {
                entityhuman.mount(this);
            }

            return true;
        }
    }

    public void setDamage(int i) {
        this.datawatcher.watch(19, Integer.valueOf(i));
    }

    public int getDamage() {
        return this.datawatcher.getInt(19);
    }

    public void b(int i) {
        this.datawatcher.watch(17, Integer.valueOf(i));
    }

    public int h() {
        return this.datawatcher.getInt(17);
    }

    public void c(int i) {
        this.datawatcher.watch(18, Integer.valueOf(i));
    }

    public int i() {
        return this.datawatcher.getInt(18);
    }
}
