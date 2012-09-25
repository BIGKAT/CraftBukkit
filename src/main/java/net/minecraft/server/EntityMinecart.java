package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.util.Vector;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
// CraftBukkit end

public class EntityMinecart extends Entity implements IInventory {

    private ItemStack[] items;
    private int e;
    private boolean f;
    public int type;
    public double b;
    public double c;
    private static final int[][][] matrix = new int[][][] { { { 0, 0, -1}, { 0, 0, 1}}, { { -1, 0, 0}, { 1, 0, 0}}, { { -1, -1, 0}, { 1, 0, 0}}, { { -1, 0, 0}, { 1, -1, 0}}, { { 0, 0, -1}, { 0, -1, 1}}, { { 0, -1, -1}, { 0, 0, 1}}, { { 0, 0, 1}, { 1, 0, 0}}, { { 0, 0, 1}, { -1, 0, 0}}, { { 0, 0, -1}, { -1, 0, 0}}, { { 0, 0, -1}, { 1, 0, 0}}};
    private int h;
    private double i;
    private double j;
    private double an;
    private double ao;
    private double ap;

    // CraftBukkit start
    public boolean slowWhenEmpty = true;
    private double derailedX = 0.5;
    private double derailedY = 0.5;
    private double derailedZ = 0.5;
    private double flyingX = 0.95;
    private double flyingY = 0.95;
    private double flyingZ = 0.95;
    public double maxSpeed = 0.4D;
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public ItemStack[] getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public InventoryHolder getOwner() {
        org.bukkit.entity.Entity cart = getBukkitEntity();
        if(cart instanceof InventoryHolder) return (InventoryHolder) cart;
        return null;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public EntityMinecart(World world) {
        super(world);
        this.items = new ItemStack[27]; // CraftBukkit
        this.e = 0;
        this.f = false;
        this.m = true;
        this.a(0.98F, 0.7F);
        this.height = this.length / 2.0F;
    }

    protected boolean e_() {
        return false;
    }

    protected void a() {
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(1));
        this.datawatcher.a(19, new Integer(0));
    }

    public AxisAlignedBB g(Entity entity) {
        return entity.boundingBox;
    }

    public AxisAlignedBB E() {
        return null;
    }

    public boolean M() {
        return true;
    }

    public EntityMinecart(World world, double d0, double d1, double d2, int i) {
        this(world);
        this.setPosition(d0, d1 + (double) this.height, d2);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
        this.type = i;

        this.worldObj.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleCreateEvent((Vehicle) this.getBukkitEntity())); // CraftBukkit
    }

    public double X() {
        return (double) this.length * 0.0D - 0.30000001192092896D;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (!this.worldObj.isStatic && !this.isDead) {
            // CraftBukkit start
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();
            org.bukkit.entity.Entity passenger = (damagesource.getEntity() == null) ? null : damagesource.getEntity().getBukkitEntity();

            VehicleDamageEvent event = new VehicleDamageEvent(vehicle, passenger, i);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }

            i = event.getDamage();
            // CraftBukkit end

            this.i(-this.k());
            this.h(10);
            this.K();
            this.setDamage(this.getDamage() + i * 10);
            if (damagesource.getEntity() instanceof EntityHuman && ((EntityHuman) damagesource.getEntity()).capabilities.canInstantlyBuild) {
                this.setDamage(100);
            }

            if (this.getDamage() > 40) {
                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mount(this);
                }

                // CraftBukkit start
                VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, passenger);
                this.worldObj.getServer().getPluginManager().callEvent(destroyEvent);

                if (destroyEvent.isCancelled()) {
                    this.setDamage(40); // Maximize damage so this doesn't get triggered again right away
                    return true;
                }
                // CraftBukkit end

                this.setDead();
                this.a(Item.MINECART.id, 1, 0.0F);
                if (this.type == 1) {
                    EntityMinecart entityminecart = this;

                    for (int j = 0; j < entityminecart.getSize(); ++j) {
                        ItemStack itemstack = entityminecart.getItem(j);

                        if (itemstack != null) {
                            float f = this.random.nextFloat() * 0.8F + 0.1F;
                            float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                            float f2 = this.random.nextFloat() * 0.8F + 0.1F;

                            while (itemstack.count > 0) {
                                int k = this.random.nextInt(21) + 10;

                                if (k > itemstack.count) {
                                    k = itemstack.count;
                                }

                                itemstack.count -= k;
                                // CraftBukkit - include enchantments in the new itemstack
                                EntityItem entityitem = new EntityItem(this.worldObj, this.posX + (double) f, this.posY + (double) f1, this.posZ + (double) f2, new ItemStack(itemstack.id, k, itemstack.getData(), itemstack.getEnchantments()));
                                float f3 = 0.05F;

                                entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
                                entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
                                entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
                                this.worldObj.addEntity(entityitem);
                            }
                        }
                    }

                    this.a(Block.CHEST.blockID, 1, 0.0F);
                } else if (this.type == 2) {
                    this.a(Block.FURNACE.blockID, 1, 0.0F);
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public boolean L() {
        return !this.isDead;
    }

    public void setDead() {
        for (int i = 0; i < this.getSize(); ++i) {
            ItemStack itemstack = this.getItem(i);

            if (itemstack != null) {
                float f = this.random.nextFloat() * 0.8F + 0.1F;
                float f1 = this.random.nextFloat() * 0.8F + 0.1F;
                float f2 = this.random.nextFloat() * 0.8F + 0.1F;

                while (itemstack.count > 0) {
                    int j = this.random.nextInt(21) + 10;

                    if (j > itemstack.count) {
                        j = itemstack.count;
                    }

                    itemstack.count -= j;
                    EntityItem entityitem = new EntityItem(this.worldObj, this.posX + (double) f, this.posY + (double) f1, this.posZ + (double) f2, new ItemStack(itemstack.id, j, itemstack.getData()));

                    if (itemstack.hasTag()) {
                        entityitem.item.setTag((NBTTagCompound) itemstack.getTag().clone());
                    }

                    float f3 = 0.05F;

                    entityitem.motionX = (double) ((float) this.random.nextGaussian() * f3);
                    entityitem.motionY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double) ((float) this.random.nextGaussian() * f3);
                    this.worldObj.addEntity(entityitem);
                }
            }
        }

        super.setDead();
    }

    public void h_() {
        // CraftBukkit start
        double prevX = this.posX;
        double prevY = this.posY;
        double prevZ = this.posZ;
        float prevYaw = this.rotationYaw;
        float prevPitch = this.rotationPitch;
        // CraftBukkit end

        if (this.j() > 0) {
            this.h(this.j() - 1);
        }

        if (this.getDamage() > 0) {
            this.setDamage(this.getDamage() - 1);
        }

        if (this.posY < -64.0D) {
            this.C();
        }

        if (this.h() && this.random.nextInt(4) == 0) {
            this.worldObj.a("largesmoke", this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (this.worldObj.isStatic) {
            if (this.h > 0) {
                double d0 = this.posX + (this.i - this.posX) / (double) this.h;
                double d1 = this.posY + (this.j - this.posY) / (double) this.h;
                double d2 = this.posZ + (this.an - this.posZ) / (double) this.h;
                double d3 = MathHelper.g(this.ao - (double) this.rotationYaw);

                this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.h);
                this.rotationPitch = (float) ((double) this.rotationPitch + (this.ap - (double) this.rotationPitch) / (double) this.h);
                --this.h;
                this.setPosition(d0, d1, d2);
                this.b(this.rotationYaw, this.rotationPitch);
            } else {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.b(this.rotationYaw, this.rotationPitch);
            }
        } else {
            this.lastX = this.posX;
            this.lastY = this.posY;
            this.lastZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            int i = MathHelper.floor(this.posX);
            int j = MathHelper.floor(this.posY);
            int k = MathHelper.floor(this.posZ);

            if (BlockMinecartTrack.d_(this.worldObj, i, j - 1, k)) {
                --j;
            }

            // CraftBukkit
            double d4 = this.maxSpeed;
            double d5 = 0.0078125D;
            int l = this.worldObj.getBlockId(i, j, k);

            if (BlockMinecartTrack.d(l)) {
                Vec3D vec3d = this.a(this.posX, this.posY, this.posZ);
                int i1 = this.worldObj.getData(i, j, k);

                this.posY = (double) j;
                boolean flag = false;
                boolean flag1 = false;

                if (l == Block.GOLDEN_RAIL.blockID) {
                    flag = (i1 & 8) != 0;
                    flag1 = !flag;
                }

                if (((BlockMinecartTrack) Block.blocksList[l]).n()) {
                    i1 &= 7;
                }

                if (i1 >= 2 && i1 <= 5) {
                    this.posY = (double) (j + 1);
                }

                if (i1 == 2) {
                    this.motionX -= d5;
                }

                if (i1 == 3) {
                    this.motionX += d5;
                }

                if (i1 == 4) {
                    this.motionZ += d5;
                }

                if (i1 == 5) {
                    this.motionZ -= d5;
                }

                int[][] aint = matrix[i1];
                double d6 = (double) (aint[1][0] - aint[0][0]);
                double d7 = (double) (aint[1][2] - aint[0][2]);
                double d8 = Math.sqrt(d6 * d6 + d7 * d7);
                double d9 = this.motionX * d6 + this.motionZ * d7;

                if (d9 < 0.0D) {
                    d6 = -d6;
                    d7 = -d7;
                }

                double d10 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                this.motionX = d10 * d6 / d8;
                this.motionZ = d10 * d7 / d8;
                double d11;
                double d12;

                if (this.riddenByEntity != null) {
                    d12 = this.riddenByEntity.motionX * this.riddenByEntity.motionX + this.riddenByEntity.motionZ * this.riddenByEntity.motionZ;
                    d11 = this.motionX * this.motionX + this.motionZ * this.motionZ;
                    if (d12 > 1.0E-4D && d11 < 0.01D) {
                        this.motionX += this.riddenByEntity.motionX * 0.1D;
                        this.motionZ += this.riddenByEntity.motionZ * 0.1D;
                        flag1 = false;
                    }
                }

                if (flag1) {
                    d12 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (d12 < 0.03D) {
                        this.motionX *= 0.0D;
                        this.motionY *= 0.0D;
                        this.motionZ *= 0.0D;
                    } else {
                        this.motionX *= 0.5D;
                        this.motionY *= 0.0D;
                        this.motionZ *= 0.5D;
                    }
                }

                d12 = 0.0D;
                d11 = (double) i + 0.5D + (double) aint[0][0] * 0.5D;
                double d13 = (double) k + 0.5D + (double) aint[0][2] * 0.5D;
                double d14 = (double) i + 0.5D + (double) aint[1][0] * 0.5D;
                double d15 = (double) k + 0.5D + (double) aint[1][2] * 0.5D;

                d6 = d14 - d11;
                d7 = d15 - d13;
                double d16;
                double d17;

                if (d6 == 0.0D) {
                    this.posX = (double) i + 0.5D;
                    d12 = this.posZ - (double) k;
                } else if (d7 == 0.0D) {
                    this.posZ = (double) k + 0.5D;
                    d12 = this.posX - (double) i;
                } else {
                    d16 = this.posX - d11;
                    d17 = this.posZ - d13;
                    d12 = (d16 * d6 + d17 * d7) * 2.0D;
                }

                this.posX = d11 + d6 * d12;
                this.posZ = d13 + d7 * d12;
                this.setPosition(this.posX, this.posY + (double) this.height, this.posZ);
                d16 = this.motionX;
                d17 = this.motionZ;
                if (this.riddenByEntity != null) {
                    d16 *= 0.75D;
                    d17 *= 0.75D;
                }

                if (d16 < -d4) {
                    d16 = -d4;
                }

                if (d16 > d4) {
                    d16 = d4;
                }

                if (d17 < -d4) {
                    d17 = -d4;
                }

                if (d17 > d4) {
                    d17 = d4;
                }

                this.move(d16, 0.0D, d17);
                if (aint[0][1] != 0 && MathHelper.floor(this.posX) - i == aint[0][0] && MathHelper.floor(this.posZ) - k == aint[0][2]) {
                    this.setPosition(this.posX, this.posY + (double) aint[0][1], this.posZ);
                } else if (aint[1][1] != 0 && MathHelper.floor(this.posX) - i == aint[1][0] && MathHelper.floor(this.posZ) - k == aint[1][2]) {
                    this.setPosition(this.posX, this.posY + (double) aint[1][1], this.posZ);
                }

                // CraftBukkit
                if (this.riddenByEntity != null || !this.slowWhenEmpty) {
                    this.motionX *= 0.996999979019165D;
                    this.motionY *= 0.0D;
                    this.motionZ *= 0.996999979019165D;
                } else {
                    if (this.type == 2) {
                        double d18 = this.b * this.b + this.c * this.c;

                        if (d18 > 1.0E-4D) {
                            d18 = (double) MathHelper.sqrt(d18);
                            this.b /= d18;
                            this.c /= d18;
                            double d19 = 0.04D;

                            this.motionX *= 0.800000011920929D;
                            this.motionY *= 0.0D;
                            this.motionZ *= 0.800000011920929D;
                            this.motionX += this.b * d19;
                            this.motionZ += this.c * d19;
                        } else {
                            this.motionX *= 0.8999999761581421D;
                            this.motionY *= 0.0D;
                            this.motionZ *= 0.8999999761581421D;
                        }
                    }

                    this.motionX *= 0.9599999785423279D;
                    this.motionY *= 0.0D;
                    this.motionZ *= 0.9599999785423279D;
                }

                Vec3D vec3d1 = this.a(this.posX, this.posY, this.posZ);

                if (vec3d1 != null && vec3d != null) {
                    double d20 = (vec3d.b - vec3d1.b) * 0.05D;

                    d10 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (d10 > 0.0D) {
                        this.motionX = this.motionX / d10 * (d10 + d20);
                        this.motionZ = this.motionZ / d10 * (d10 + d20);
                    }

                    this.setPosition(this.posX, vec3d1.b, this.posZ);
                }

                int j1 = MathHelper.floor(this.posX);
                int k1 = MathHelper.floor(this.posZ);

                if (j1 != i || k1 != k) {
                    d10 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    this.motionX = d10 * (double) (j1 - i);
                    this.motionZ = d10 * (double) (k1 - k);
                }

                double d21;

                if (this.type == 2) {
                    d21 = this.b * this.b + this.c * this.c;
                    if (d21 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
                        d21 = (double) MathHelper.sqrt(d21);
                        this.b /= d21;
                        this.c /= d21;
                        if (this.b * this.motionX + this.c * this.motionZ < 0.0D) {
                            this.b = 0.0D;
                            this.c = 0.0D;
                        } else {
                            this.b = this.motionX;
                            this.c = this.motionZ;
                        }
                    }
                }

                if (flag) {
                    d21 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                    if (d21 > 0.01D) {
                        double d22 = 0.06D;

                        this.motionX += this.motionX / d21 * d22;
                        this.motionZ += this.motionZ / d21 * d22;
                    } else if (i1 == 1) {
                        if (this.worldObj.s(i - 1, j, k)) {
                            this.motionX = 0.02D;
                        } else if (this.worldObj.s(i + 1, j, k)) {
                            this.motionX = -0.02D;
                        }
                    } else if (i1 == 0) {
                        if (this.worldObj.s(i, j, k - 1)) {
                            this.motionZ = 0.02D;
                        } else if (this.worldObj.s(i, j, k + 1)) {
                            this.motionZ = -0.02D;
                        }
                    }
                }

                this.D();
            } else {
                if (this.motionX < -d4) {
                    this.motionX = -d4;
                }

                if (this.motionX > d4) {
                    this.motionX = d4;
                }

                if (this.motionZ < -d4) {
                    this.motionZ = -d4;
                }

                if (this.motionZ > d4) {
                    this.motionZ = d4;
                }

                if (this.onGround) {
                    // CraftBukkit start
                    this.motionX *= this.derailedX;
                    this.motionY *= this.derailedY;
                    this.motionZ *= this.derailedZ;
                    // CraftBukkit end
                }

                this.move(this.motionX, this.motionY, this.motionZ);
                if (!this.onGround) {
                    // CraftBukkit start
                    this.motionX *= this.flyingX;
                    this.motionY *= this.flyingY;
                    this.motionZ *= this.flyingZ;
                    // CraftBukkit end
                }
            }

            this.rotationPitch = 0.0F;
            double d23 = this.lastX - this.posX;
            double d24 = this.lastZ - this.posZ;

            if (d23 * d23 + d24 * d24 > 0.001D) {
                this.rotationYaw = (float) (Math.atan2(d24, d23) * 180.0D / 3.141592653589793D);
                if (this.f) {
                    this.rotationYaw += 180.0F;
                }
            }

            double d25 = (double) MathHelper.g(this.rotationYaw - this.lastYaw);

            if (d25 < -170.0D || d25 >= 170.0D) {
                this.rotationYaw += 180.0F;
                this.f = !this.f;
            }

            this.b(this.rotationYaw, this.rotationPitch);

            // CraftBukkit start
            org.bukkit.World bworld = this.worldObj.getWorld();
            Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
            Location to = new Location(bworld, this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();

            this.worldObj.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));

            if (!from.equals(to)) {
                this.worldObj.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleMoveEvent(vehicle, from, to));
            }
            // CraftBukkit end

            List list = this.worldObj.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

            if (list != null && !list.isEmpty()) {
                for (int l1 = 0; l1 < list.size(); ++l1) {
                    Entity entity = (Entity) list.get(l1);

                    if (entity != this.riddenByEntity && entity.M() && entity instanceof EntityMinecart) {
                        entity.collide(this);
                    }
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                if (this.riddenByEntity.ridingEntity == this) {
                    this.riddenByEntity.ridingEntity = null;
                }

                this.riddenByEntity = null;
            }

            if (this.e > 0) {
                --this.e;
            }

            if (this.e <= 0) {
                this.b = this.c = 0.0D;
            }

            this.d(this.e > 0);
        }
    }

    public Vec3D a(double d0, double d1, double d2) {
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);

        if (BlockMinecartTrack.d_(this.worldObj, i, j - 1, k)) {
            --j;
        }

        int l = this.worldObj.getBlockId(i, j, k);

        if (BlockMinecartTrack.d(l)) {
            int i1 = this.worldObj.getData(i, j, k);

            d1 = (double) j;
            if (((BlockMinecartTrack) Block.blocksList[l]).n()) {
                i1 &= 7;
            }

            if (i1 >= 2 && i1 <= 5) {
                d1 = (double) (j + 1);
            }

            int[][] aint = matrix[i1];
            double d3 = 0.0D;
            double d4 = (double) i + 0.5D + (double) aint[0][0] * 0.5D;
            double d5 = (double) j + 0.5D + (double) aint[0][1] * 0.5D;
            double d6 = (double) k + 0.5D + (double) aint[0][2] * 0.5D;
            double d7 = (double) i + 0.5D + (double) aint[1][0] * 0.5D;
            double d8 = (double) j + 0.5D + (double) aint[1][1] * 0.5D;
            double d9 = (double) k + 0.5D + (double) aint[1][2] * 0.5D;
            double d10 = d7 - d4;
            double d11 = (d8 - d5) * 2.0D;
            double d12 = d9 - d6;

            if (d10 == 0.0D) {
                d0 = (double) i + 0.5D;
                d3 = d2 - (double) k;
            } else if (d12 == 0.0D) {
                d2 = (double) k + 0.5D;
                d3 = d0 - (double) i;
            } else {
                double d13 = d0 - d4;
                double d14 = d2 - d6;

                d3 = (d13 * d10 + d14 * d12) * 2.0D;
            }

            d0 = d4 + d10 * d3;
            d1 = d5 + d11 * d3;
            d2 = d6 + d12 * d3;
            if (d11 < 0.0D) {
                ++d1;
            }

            if (d11 > 0.0D) {
                d1 += 0.5D;
            }

            return Vec3D.a().create(d0, d1, d2);
        } else {
            return null;
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("Type", this.type);
        if (this.type == 2) {
            nbttagcompound.setDouble("PushX", this.b);
            nbttagcompound.setDouble("PushZ", this.c);
            nbttagcompound.setShort("Fuel", (short) this.e);
        } else if (this.type == 1) {
            NBTTagList nbttaglist = new NBTTagList();

            for (int i = 0; i < this.items.length; ++i) {
                if (this.items[i] != null) {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                    nbttagcompound1.setByte("Slot", (byte) i);
                    this.items[i].save(nbttagcompound1);
                    nbttaglist.add(nbttagcompound1);
                }
            }

            nbttagcompound.set("Items", nbttaglist);
        }
    }

    protected void a(NBTTagCompound nbttagcompound) {
        this.type = nbttagcompound.getInteger("Type");
        if (this.type == 2) {
            this.b = nbttagcompound.getDouble("PushX");
            this.c = nbttagcompound.getDouble("PushZ");
            this.e = nbttagcompound.getShort("Fuel");
        } else if (this.type == 1) {
            NBTTagList nbttaglist = nbttagcompound.getList("Items");

            this.items = new ItemStack[this.getSize()];

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.get(i);
                int j = nbttagcompound1.getByte("Slot") & 255;

                if (j >= 0 && j < this.items.length) {
                    this.items[j] = ItemStack.a(nbttagcompound1);
                }
            }
        }
    }

    public void collide(Entity entity) {
        if (!this.worldObj.isStatic) {
            if (entity != this.riddenByEntity) {
                // CraftBukkit start
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity hitEntity = (entity == null) ? null : entity.getBukkitEntity();

                VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent(vehicle, hitEntity);
                this.worldObj.getServer().getPluginManager().callEvent(collisionEvent);

                if (collisionEvent.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                if (entity instanceof EntityLiving && !(entity instanceof EntityHuman) && !(entity instanceof EntityIronGolem) && this.type == 0 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && entity.ridingEntity == null) {
                    entity.mount(this);
                }

                double d0 = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                double d2 = d0 * d0 + d1 * d1;

                // CraftBukkit - collision
                if (d2 >= 9.999999747378752E-5D && !collisionEvent.isCollisionCancelled()) {
                    d2 = (double) MathHelper.sqrt(d2);
                    d0 /= d2;
                    d1 /= d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D) {
                        d3 = 1.0D;
                    }

                    d0 *= d3;
                    d1 *= d3;
                    d0 *= 0.10000000149011612D;
                    d1 *= 0.10000000149011612D;
                    d0 *= (double) (1.0F - this.Y);
                    d1 *= (double) (1.0F - this.Y);
                    d0 *= 0.5D;
                    d1 *= 0.5D;
                    if (entity instanceof EntityMinecart) {
                        double d4 = entity.posX - this.posX;
                        double d5 = entity.posZ - this.posZ;
                        Vec3D vec3d = Vec3D.a().create(d4, 0.0D, d5).b();
                        Vec3D vec3d1 = Vec3D.a().create((double) MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F), 0.0D, (double) MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F)).b();
                        double d6 = Math.abs(vec3d.b(vec3d1));

                        if (d6 < 0.800000011920929D) {
                            return;
                        }

                        double d7 = entity.motionX + this.motionX;
                        double d8 = entity.motionZ + this.motionZ;

                        if (((EntityMinecart) entity).type == 2 && this.type != 2) {
                            this.motionX *= 0.20000000298023224D;
                            this.motionZ *= 0.20000000298023224D;
                            this.g(entity.motionX - d0, 0.0D, entity.motionZ - d1);
                            entity.motionX *= 0.949999988079071D;
                            entity.motionZ *= 0.949999988079071D;
                        } else if (((EntityMinecart) entity).type != 2 && this.type == 2) {
                            entity.motionX *= 0.20000000298023224D;
                            entity.motionZ *= 0.20000000298023224D;
                            entity.g(this.motionX + d0, 0.0D, this.motionZ + d1);
                            this.motionX *= 0.949999988079071D;
                            this.motionZ *= 0.949999988079071D;
                        } else {
                            d7 /= 2.0D;
                            d8 /= 2.0D;
                            this.motionX *= 0.20000000298023224D;
                            this.motionZ *= 0.20000000298023224D;
                            this.g(d7 - d0, 0.0D, d8 - d1);
                            entity.motionX *= 0.20000000298023224D;
                            entity.motionZ *= 0.20000000298023224D;
                            entity.g(d7 + d0, 0.0D, d8 + d1);
                        }
                    } else {
                        this.g(-d0, 0.0D, -d1);
                        entity.g(d0 / 4.0D, 0.0D, d1 / 4.0D);
                    }
                }
            }
        }
    }

    public int getSize() {
        return 27;
    }

    public ItemStack getItem(int i) {
        return this.items[i];
    }

    public ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    public ItemStack splitWithoutUpdate(int i) {
        if (this.items[i] != null) {
            ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, ItemStack itemstack) {
        this.items[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }
    }

    public String getName() {
        return "container.minecart";
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public void update() {}

    public boolean c(EntityHuman entityhuman) {
        if (this.type == 0) {
            if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityHuman && this.riddenByEntity != entityhuman) {
                return true;
            }

            if (!this.worldObj.isStatic) {
                entityhuman.mount(this);
            }
        } else if (this.type == 1) {
            if (!this.worldObj.isStatic) {
                entityhuman.displayGUIChest(this);
            }
        } else if (this.type == 2) {
            ItemStack itemstack = entityhuman.inventory.getItemInHand();

            if (itemstack != null && itemstack.id == Item.COAL.id) {
                if (--itemstack.count == 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }

                this.e += 3600;
            }

            this.b = this.posX - entityhuman.posX;
            this.c = this.posZ - entityhuman.posZ;
        }

        return true;
    }

    public boolean a(EntityHuman entityhuman) {
        return this.isDead ? false : entityhuman.e(this) <= 64.0D;
    }

    protected boolean h() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    protected void d(boolean flag) {
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (this.datawatcher.getByte(16) | 1)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (this.datawatcher.getByte(16) & -2)));
        }
    }

    public void startOpen() {}

    public void f() {}

    public void setDamage(int i) {
        this.datawatcher.watch(19, Integer.valueOf(i));
    }

    public int getDamage() {
        return this.datawatcher.getInt(19);
    }

    public void h(int i) {
        this.datawatcher.watch(17, Integer.valueOf(i));
    }

    public int j() {
        return this.datawatcher.getInt(17);
    }

    public void i(int i) {
        this.datawatcher.watch(18, Integer.valueOf(i));
    }

    public int k() {
        return this.datawatcher.getInt(18);
    }

    // CraftBukkit start - methods for getting and setting flying and derailed velocity modifiers
    public Vector getFlyingVelocityMod() {
        return new Vector(flyingX, flyingY, flyingZ);
    }

    public void setFlyingVelocityMod(Vector flying) {
        flyingX = flying.getX();
        flyingY = flying.getY();
        flyingZ = flying.getZ();
    }

    public Vector getDerailedVelocityMod() {
        return new Vector(derailedX, derailedY, derailedZ);
    }

    public void setDerailedVelocityMod(Vector derailed) {
        derailedX = derailed.getX();
        derailedY = derailed.getY();
        derailedZ = derailed.getZ();
    }
    // CraftBukkit end
}
