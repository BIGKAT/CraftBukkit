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
// Forge start
import java.util.ArrayList;
import java.util.Iterator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.IMinecartCollisionHandler;
import net.minecraftforge.common.MinecartRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;
// Forge end

public class EntityMinecart extends Entity implements IInventory {

    private ItemStack[] items;
    private int e;
    private boolean f;
    public int type;
    public double b;
    public double c;
    private final IUpdatePlayerListBox g;
    private boolean h;
    private static final int[][][] matrix = new int[][][] { { { 0, 0, -1}, { 0, 0, 1}}, { { -1, 0, 0}, { 1, 0, 0}}, { { -1, -1, 0}, { 1, 0, 0}}, { { -1, 0, 0}, { 1, -1, 0}}, { { 0, 0, -1}, { 0, -1, 1}}, { { 0, -1, -1}, { 0, 0, 1}}, { { 0, 0, 1}, { 1, 0, 0}}, { { 0, 0, 1}, { -1, 0, 0}}, { { 0, 0, -1}, { -1, 0, 0}}, { { 0, 0, -1}, { 1, 0, 0}}};
    private int j;
    private double at;
    private double au;
    private double av;
    private double aw;
    private double ax;

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

    /* Forge: Minecart Compatibility Layer Integration. */
    public static float defaultMaxSpeedRail = 0.4F;
    public static float defaultMaxSpeedGround = 0.4F;
    public static float defaultMaxSpeedAirLateral = 0.4F;
    public static float defaultMaxSpeedAirVertical = -1.0F;
    public static double defaultDragRidden = 0.996999979019165D;
    public static double defaultDragEmpty = 0.9599999785423279D;
    public static double defaultDragAir = 0.949999988079071D;
    protected boolean canUseRail = true;
    protected boolean canBePushed = true;
    private static IMinecartCollisionHandler collisionHandler = null;
    
    /* Instance versions of the above physics properties */
    protected float maxSpeedRail;
    protected float maxSpeedGround;
    protected float maxSpeedAirLateral;
    protected float maxSpeedAirVertical;
    protected double dragAir;
    // Forge end

    public EntityMinecart(World world) {
        super(world);
        this.items = new ItemStack[27]; // CraftBukkit
        this.e = 0;
        this.f = false;
        this.h = true;
        this.m = true;
        this.a(0.98F, 0.7F);
        this.height = this.length / 2.0F;
        this.g = world != null ? world.a(this) : null;
        // Forge start
        this.maxSpeedRail = defaultMaxSpeedRail;
        this.maxSpeedGround = defaultMaxSpeedGround;
        this.maxSpeedAirLateral = defaultMaxSpeedAirLateral;
        this.maxSpeedAirVertical = defaultMaxSpeedAirVertical;
        this.dragAir = defaultDragAir;
        // Forge end
        this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleCreateEvent((Vehicle) this.getBukkitEntity())); // CraftBukkit
    }

    // Forge start
    public EntityMinecart(World world, int type) {
        this(world);
        this.type = type;
    }
    // Forge end

    protected boolean f_() {
        return false;
    }

    protected void a() {
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(17, new Integer(0));
        this.datawatcher.a(18, new Integer(1));
        this.datawatcher.a(19, new Integer(0));
    }

    public AxisAlignedBB g(Entity entity) {
        if (getCollisionHandler() != null) return getCollisionHandler().getCollisionBox(this, entity); // Forge
        return entity.M() ? entity.boundingBox : null;
    }

    public AxisAlignedBB E() {
        return getCollisionHandler() != null ? getCollisionHandler().getBoundingBox(this) : null; // Forge
    }

    public boolean M() {
        return this.canBePushed;
    }

    public EntityMinecart(World world, double d0, double d1, double d2, int i) {
        this(world);
        this.setPosition(d0, d1 + (double) this.height, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
        this.type = i;

        this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleCreateEvent((Vehicle) this.getBukkitEntity())); // CraftBukkit
    }

    public double X() {
        return (double) this.length * 0.0D - 0.30000001192092896D;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            if (!this.world.isStatic && !this.dead) {
                // CraftBukkit start
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity passenger = (damagesource.getEntity() == null) ? null : damagesource.getEntity().getBukkitEntity();

                VehicleDamageEvent event = new VehicleDamageEvent(vehicle, passenger, i);
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }

                i = event.getDamage();
                // CraftBukkit end

                this.i(-this.k());
                this.h(10);
                this.K();
                this.setDamage(this.getDamage() + i * 10);
                if (damagesource.getEntity() instanceof EntityHuman && ((EntityHuman) damagesource.getEntity()).abilities.canInstantlyBuild) {
                    this.setDamage(100);
                }

                if (this.getDamage() > 40) {
                    if (this.passenger != null) {
                        this.passenger.mount(this);
                    }

                    // CraftBukkit start
                    VehicleDestroyEvent destroyEvent = new VehicleDestroyEvent(vehicle, passenger);
                    this.world.getServer().getPluginManager().callEvent(destroyEvent);

                    if (destroyEvent.isCancelled()) {
                        this.setDamage(40); // Maximize damage so this doesn't get triggered again right away
                        return true;
                    }
                    // CraftBukkit end

                    this.die();
                    this.dropCartAsItem(); // Forge
                }

                return true;
            } else {
                return true;
            }
        }
    }

    public boolean L() {
        return !this.dead;
    }

    public void die() {
        if (this.h) {
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
                        EntityItem entityitem = new EntityItem(this.world, this.locX + (double) f, this.locY + (double) f1, this.locZ + (double) f2, new ItemStack(itemstack.id, j, itemstack.getData()));

                        if (itemstack.hasTag()) {
                            entityitem.getItemStack().setTag((NBTTagCompound) itemstack.getTag().clone());
                        }

                        float f3 = 0.05F;

                        entityitem.motX = (double) ((float) this.random.nextGaussian() * f3);
                        entityitem.motY = (double) ((float) this.random.nextGaussian() * f3 + 0.2F);
                        entityitem.motZ = (double) ((float) this.random.nextGaussian() * f3);
                        this.world.addEntity(entityitem);
                    }
                }
            }
        }

        super.die();
        if (this.g != null) {
            this.g.a();
        }
    }

    public void b(int i) {
        this.h = false;
        super.b(i);
    }

    public void j_() {
        // CraftBukkit start
        double prevX = this.locX;
        double prevY = this.locY;
        double prevZ = this.locZ;
        float prevYaw = this.yaw;
        float prevPitch = this.pitch;
        // CraftBukkit end

        if (this.g != null) {
            this.g.a();
        }

        if (this.j() > 0) {
            this.h(this.j() - 1);
        }

        if (this.getDamage() > 0) {
            this.setDamage(this.getDamage() - 1);
        }

        if (this.locY < -64.0D) {
            this.C();
        }

        if (this.h() && this.random.nextInt(4) == 0 && this.type == 2 && this.getClass() == EntityMinecart.class) { // Forge
            this.world.addParticle("largesmoke", this.locX, this.locY + 0.8D, this.locZ, 0.0D, 0.0D, 0.0D);
        }

        int i;

        if (!this.world.isStatic && this.world instanceof WorldServer) {
            this.world.methodProfiler.a("portal");
            MinecraftServer minecraftserver = ((WorldServer) this.world).getMinecraftServer();

            i = this.z();
            if (this.ao) {
                if (minecraftserver.getAllowNether()) {
                    if (this.vehicle == null && this.ap++ >= i) {
                        this.ap = i;
                        this.portalCooldown = this.ab();
                        byte b0;

                        if (this.world.worldProvider.dimension == -1) {
                            b0 = 0;
                        } else {
                            b0 = -1;
                        }

                        this.b(b0);
                    }

                    this.ao = false;
                }
            } else {
                if (this.ap > 0) {
                    this.ap -= 4;
                }

                if (this.ap < 0) {
                    this.ap = 0;
                }
            }

            if (this.portalCooldown > 0) {
                --this.portalCooldown;
            }

            this.world.methodProfiler.b();
        }

        if (this.world.isStatic) {
            if (this.j > 0) {
                double d0 = this.locX + (this.at - this.locX) / (double) this.j;
                double d1 = this.locY + (this.au - this.locY) / (double) this.j;
                double d2 = this.locZ + (this.av - this.locZ) / (double) this.j;
                double d3 = MathHelper.g(this.aw - (double) this.yaw);

                this.yaw = (float) ((double) this.yaw + d3 / (double) this.j);
                this.pitch = (float) ((double) this.pitch + (this.ax - (double) this.pitch) / (double) this.j);
                --this.j;
                this.setPosition(d0, d1, d2);
                this.b(this.yaw, this.pitch);
            } else {
                this.setPosition(this.locX, this.locY, this.locZ);
                this.b(this.yaw, this.pitch);
            }
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            this.motY -= 0.03999999910593033D;
            int j = MathHelper.floor(this.locX);
            i = MathHelper.floor(this.locY);
            int k = MathHelper.floor(this.locZ);

            if (BlockMinecartTrack.e_(this.world, j, i - 1, k)) {
                --i;
            }

            // CraftBukkit
            double d4 = this.maxSpeed;
            double d5 = 0.0078125D;
            int l = this.world.getTypeId(j, i, k);

            if (canUseRail() && BlockMinecartTrack.e(l)) { // Forge
                this.fallDistance = 0.0F;
                Vec3D vec3d = this.a(this.locX, this.locY, this.locZ);
                int i1 = ((BlockMinecartTrack)Block.byId[l]).getBasicRailMetadata(world, this, j, i, k); // Forge

                this.locY = (double) i;
                boolean flag = false;
                boolean flag1 = false;

                if (l == Block.GOLDEN_RAIL.id) {
                    flag = (world.getData(j, i, k) & 8) != 0; // Forge
                    flag1 = !flag;
                }

                if (((BlockMinecartTrack) Block.byId[l]).p()) {
                    i1 &= 7;
                }

                if (i1 >= 2 && i1 <= 5) {
                    this.locY = (double) (i + 1);
                }

                this.adjustSlopeVelocities(i1); // Forge

                int[][] aint = matrix[i1];
                double d6 = (double) (aint[1][0] - aint[0][0]);
                double d7 = (double) (aint[1][2] - aint[0][2]);
                double d8 = Math.sqrt(d6 * d6 + d7 * d7);
                double d9 = this.motX * d6 + this.motZ * d7;

                if (d9 < 0.0D) {
                    d6 = -d6;
                    d7 = -d7;
                }

                double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);

                this.motX = d10 * d6 / d8;
                this.motZ = d10 * d7 / d8;
                double d11;
                double d12;

                if (this.passenger != null) {
                    d12 = this.passenger.motX * this.passenger.motX + this.passenger.motZ * this.passenger.motZ;
                    d11 = this.motX * this.motX + this.motZ * this.motZ;
                    if (d12 > 1.0E-4D && d11 < 0.01D) {
                        this.motX += this.passenger.motX * 0.1D;
                        this.motZ += this.passenger.motZ * 0.1D;
                        flag1 = false;
                    }
                }

                if (flag1 && this.shouldDoRailFunctions()) { // Forge
                    d12 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
                    if (d12 < 0.03D) {
                        this.motX *= 0.0D;
                        this.motY *= 0.0D;
                        this.motZ *= 0.0D;
                    } else {
                        this.motX *= 0.5D;
                        this.motY *= 0.0D;
                        this.motZ *= 0.5D;
                    }
                }

                d12 = 0.0D;
                d11 = (double) j + 0.5D + (double) aint[0][0] * 0.5D;
                double d13 = (double) k + 0.5D + (double) aint[0][2] * 0.5D;
                double d14 = (double) j + 0.5D + (double) aint[1][0] * 0.5D;
                double d15 = (double) k + 0.5D + (double) aint[1][2] * 0.5D;

                d6 = d14 - d11;
                d7 = d15 - d13;
                double d16;
                double d17;

                if (d6 == 0.0D) {
                    this.locX = (double) j + 0.5D;
                    d12 = this.locZ - (double) k;
                } else if (d7 == 0.0D) {
                    this.locZ = (double) k + 0.5D;
                    d12 = this.locX - (double) j;
                } else {
                    d16 = this.locX - d11;
                    d17 = this.locZ - d13;
                    d12 = (d16 * d6 + d17 * d7) * 2.0D;
                }

                this.locX = d11 + d6 * d12;
                this.locZ = d13 + d7 * d12;
                this.setPosition(this.locX, this.locY + (double) this.height, this.locZ);
                this.moveMinecartOnRail(j, i, k); // Forge
                if (aint[0][1] != 0 && MathHelper.floor(this.locX) - j == aint[0][0] && MathHelper.floor(this.locZ) - k == aint[0][2]) {
                    this.setPosition(this.locX, this.locY + (double) aint[0][1], this.locZ);
                } else if (aint[1][1] != 0 && MathHelper.floor(this.locX) - j == aint[1][0] && MathHelper.floor(this.locZ) - k == aint[1][2]) {
                    this.setPosition(this.locX, this.locY + (double) aint[1][1], this.locZ);
                }

                this.applyDragAndPushForces(); // Forge

                Vec3D vec3d1 = this.a(this.locX, this.locY, this.locZ);

                if (vec3d1 != null && vec3d != null) {
                    double d20 = (vec3d.d - vec3d1.d) * 0.05D;

                    d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
                    if (d10 > 0.0D) {
                        this.motX = this.motX / d10 * (d10 + d20);
                        this.motZ = this.motZ / d10 * (d10 + d20);
                    }

                    this.setPosition(this.locX, vec3d1.d, this.locZ);
                }

                int j1 = MathHelper.floor(this.locX);
                int k1 = MathHelper.floor(this.locZ);

                if (j1 != j || k1 != k) {
                    d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
                    this.motX = d10 * (double) (j1 - j);
                    this.motZ = d10 * (double) (k1 - k);
                }

                double d21;
                // Forge start
                this.updatePushForces();

                if (this.shouldDoRailFunctions()) {
                    ((BlockMinecartTrack)Block.byId[l]).onMinecartPass(this.world, this, i, j, k);
                }

                if (flag && this.shouldDoRailFunctions()) {
                // Forge end
                    d21 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ);
                    if (d21 > 0.01D) {
                        double d22 = 0.06D;

                        this.motX += this.motX / d21 * d22;
                        this.motZ += this.motZ / d21 * d22;
                    } else if (i1 == 1) {
                        if (this.world.t(j - 1, i, k)) {
                            this.motX = 0.02D;
                        } else if (this.world.t(j + 1, i, k)) {
                            this.motX = -0.02D;
                        }
                    } else if (i1 == 0) {
                        if (this.world.t(j, i, k - 1)) {
                            this.motZ = 0.02D;
                        } else if (this.world.t(j, i, k + 1)) {
                            this.motZ = -0.02D;
                        }
                    }
                }
            } else {
                this.moveMinecartOffRail(j, i, k); // Forge
            }

            this.D();
            this.pitch = 0.0F;
            double d23 = this.lastX - this.locX;
            double d24 = this.lastZ - this.locZ;

            if (d23 * d23 + d24 * d24 > 0.001D) {
                this.yaw = (float) (Math.atan2(d24, d23) * 180.0D / 3.141592653589793D);
                if (this.f) {
                    this.yaw += 180.0F;
                }
            }

            double d25 = (double) MathHelper.g(this.yaw - this.lastYaw);

            if (d25 < -170.0D || d25 >= 170.0D) {
                this.yaw += 180.0F;
                this.f = !this.f;
            }

            this.b(this.yaw, this.pitch);

            // CraftBukkit start
            org.bukkit.World bworld = this.world.getWorld();
            Location from = new Location(bworld, prevX, prevY, prevZ, prevYaw, prevPitch);
            Location to = new Location(bworld, this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            Vehicle vehicle = (Vehicle) this.getBukkitEntity();

            this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleUpdateEvent(vehicle));

            if (!from.equals(to)) {
                this.world.getServer().getPluginManager().callEvent(new org.bukkit.event.vehicle.VehicleMoveEvent(vehicle, from, to));
            }
            // CraftBukkit end
            // Forge start
            AxisAlignedBB box = null;
            if (getCollisionHandler() != null) {
                box = getCollisionHandler().getMinecartCollisionBox(this);
            } else {
                box = this.boundingBox.grow(0.2D, 0.0D, 0.2D);
            }

            List list = this.world.getEntities(this, box);
            // Forge end
            if (list != null && !list.isEmpty()) {
                for (int l1 = 0; l1 < list.size(); ++l1) {
                    Entity entity = (Entity) list.get(l1);

                    if (entity != this.passenger && entity.M() && entity instanceof EntityMinecart) {
                        entity.collide(this);
                    }
                }
            }

            if (this.passenger != null && this.passenger.dead) {
                if (this.passenger.vehicle == this) {
                    this.passenger.vehicle = null;
                }

                this.passenger = null;
            }
            // Forge start
            this.updateFuel();
            MinecraftForge.EVENT_BUS.post(new MinecartUpdateEvent(this, i, j, k));
            // Forge end
        }
    }

    public Vec3D a(double d0, double d1, double d2) {
        int i = MathHelper.floor(d0);
        int j = MathHelper.floor(d1);
        int k = MathHelper.floor(d2);

        if (BlockMinecartTrack.e_(this.world, i, j - 1, k)) {
            --j;
        }

        int l = this.world.getTypeId(i, j, k);

        if (BlockMinecartTrack.e(l)) {
            int i1= ((BlockMinecartTrack)Block.byId[l]).getBasicRailMetadata(this.world, this, i, j, k); // Forge

            d1 = (double) j;

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

            return this.world.getVec3DPool().create(d0, d1, d2);
        } else {
            return null;
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("Type", this.type);

        if (this.isPoweredCart()) { // Forge
            nbttagcompound.setDouble("PushX", this.b);
            nbttagcompound.setDouble("PushZ", this.c);
            nbttagcompound.setInt("Fuel", this.e); // Forge
        }

        if (this.getSize() > 0) { // Forge
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
        this.type = nbttagcompound.getInt("Type");

        if (this.isPoweredCart()) { // Forge
            this.b = nbttagcompound.getDouble("PushX");
            this.c = nbttagcompound.getDouble("PushZ");
            // Forge start
            try {
                this.e = nbttagcompound.getInt("Fuel");
            }
            catch (ClassCastException e) {
                 this.e = nbttagcompound.getShort("Fuel");
            }
            // Forge end
        }

        if (this.getSize() > 0) {
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
        // Forge start
        MinecraftForge.EVENT_BUS.post(new MinecartCollisionEvent(this, entity));
        if (getCollisionHandler() != null) {
            getCollisionHandler().onEntityCollision(this, entity);
            return;
        }
        // Forge end
        if (!this.world.isStatic) {
            if (entity != this.passenger) {
                // CraftBukkit start
                Vehicle vehicle = (Vehicle) this.getBukkitEntity();
                org.bukkit.entity.Entity hitEntity = (entity == null) ? null : entity.getBukkitEntity();

                VehicleEntityCollisionEvent collisionEvent = new VehicleEntityCollisionEvent(vehicle, hitEntity);
                this.world.getServer().getPluginManager().callEvent(collisionEvent);

                if (collisionEvent.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                if (entity instanceof EntityLiving && !(entity instanceof EntityHuman) && !(entity instanceof EntityIronGolem) && this.canBeRidden() && this.motX * this.motX + this.motZ * this.motZ > 0.01D && this.passenger == null && entity.vehicle == null) { // Forge
                    entity.mount(this);
                }

                double d0 = entity.locX - this.locX;
                double d1 = entity.locZ - this.locZ;
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
                    d0 *= (double) (1.0F - this.Z);
                    d1 *= (double) (1.0F - this.Z);
                    d0 *= 0.5D;
                    d1 *= 0.5D;
                    if (entity instanceof EntityMinecart) {
                        double d4 = entity.locX - this.locX;
                        double d5 = entity.locZ - this.locZ;
                        Vec3D vec3d = this.world.getVec3DPool().create(d4, 0.0D, d5).a();
                        Vec3D vec3d1 = this.world.getVec3DPool().create((double) MathHelper.cos(this.yaw * 3.1415927F / 180.0F), 0.0D, (double) MathHelper.sin(this.yaw * 3.1415927F / 180.0F)).a();
                        double d6 = Math.abs(vec3d.b(vec3d1));

                        if (d6 < 0.800000011920929D) {
                            return;
                        }

                        double d7 = entity.motX + this.motX;
                        double d8 = entity.motZ + this.motZ;

                        if (((EntityMinecart)entity).isPoweredCart() && !this.isPoweredCart()) { // Forge
                            this.motX *= 0.20000000298023224D;
                            this.motZ *= 0.20000000298023224D;
                            this.g(entity.motX - d0, 0.0D, entity.motZ - d1);
                            entity.motX *= 0.949999988079071D;
                            entity.motZ *= 0.949999988079071D;
                        } else if (!((EntityMinecart)entity).isPoweredCart() && this.isPoweredCart()) { // Forge
                            entity.motX *= 0.20000000298023224D;
                            entity.motZ *= 0.20000000298023224D;
                            entity.g(this.motX + d0, 0.0D, this.motZ + d1);
                            this.motX *= 0.949999988079071D;
                            this.motZ *= 0.949999988079071D;
                        } else {
                            d7 /= 2.0D;
                            d8 /= 2.0D;
                            this.motX *= 0.20000000298023224D;
                            this.motZ *= 0.20000000298023224D;
                            this.g(d7 - d0, 0.0D, d8 - d1);
                            entity.motX *= 0.20000000298023224D;
                            entity.motZ *= 0.20000000298023224D;
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
        return this.type == 1 && this.getClass() == EntityMinecart.class ? 27 : 0; // Forge
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

    public boolean a(EntityHuman entityhuman) {
        // Forge start
        if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, entityhuman)))
            return true;
        if (this.canBeRidden()) {
        // Forge end
            if (this.passenger != null && this.passenger instanceof EntityHuman && this.passenger != entityhuman) {
                return true;
            }

            if (!this.world.isStatic) {
                entityhuman.mount(this);
            }
        } else if (this.getSize() > 0) { // Forge
            if (!this.world.isStatic) {
                entityhuman.openContainer(this);
            }
        } else if (this.type == 2 && this.getClass() == EntityMinecart.class) { // Forge
            ItemStack itemstack = entityhuman.inventory.getItemInHand();

            if (itemstack != null && itemstack.id == Item.COAL.id) {
                if (--itemstack.count == 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }

                this.e += 3600;
            }

            this.b = this.locX - entityhuman.locX;
            this.c = this.locZ - entityhuman.locZ;
        }

        return true;
    }

    public boolean a_(EntityHuman entityhuman) {
        return this.dead ? false : entityhuman.e(this) <= 64.0D;
    }

    protected boolean h() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    protected void e(boolean flag) {
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
    /* =================================================== FORGE START =====================================*/    
    /**
     * Drops the cart as a item. The exact item dropped is defined by getItemDropped().
     */
    public void dropCartAsItem() {
        for(ItemStack item : getItemsDropped()) {
            a(item, 0);
        }
    }

    /**
     * Override this to define which items your cart drops when broken.
     * This does not include items contained in the inventory,
     * that is handled elsewhere.
     * @return A list of items dropped.
     */
    public List<ItemStack> getItemsDropped() {
        List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(Item.MINECART));

        switch (this.type) {
            case 1:
                items.add(new ItemStack(Block.CHEST));
                break;

            case 2:
                items.add(new ItemStack(Block.FURNACE));
        }

        return items;
    }

    /**
     * This function returns an ItemStack that represents this cart.
     * This should be an ItemStack that can be used by the player to place the cart.
     * This is the item that was registered with the cart via the registerMinecart function,
     * but is not necessary the item the cart drops when destroyed.
     * @return An ItemStack that can be used to place the cart.
     */
    public ItemStack getCartItem() {
        return MinecartRegistry.getItemForCart(this);
    }

    /**
     * Returns true if this cart is self propelled.
     * @return True if powered.
     */
    public boolean isPoweredCart() {
        return this.type == 2 && this.getClass() == EntityMinecart.class;
    }

    /**
     * Returns true if this cart is a storage cart
     * Some carts may have inventories but not be storage carts
     * and some carts without inventories may be storage carts.
     * @return True if this cart should be classified as a storage cart.
     */
    public boolean isStorageCart() {
        return this.type == 1 && this.getClass() == EntityMinecart.class;
    }

    /**
     * Returns true if this cart can be ridden by an Entity.
     * @return True if this cart can be ridden.
     */
    public boolean canBeRidden() {
        return this.type == 0 && this.getClass() == EntityMinecart.class;
    }

    /**
     * Returns true if this cart can currently use rails.
     * This function is mainly used to gracefully detach a minecart from a rail.
     * @return True if the minecart can use rails.
     */
    public boolean canUseRail() {
        return this.canUseRail;
    }

    /**
     * Set whether the minecart can use rails.
     * This function is mainly used to gracefully detach a minecart from a rail.
     * @param use Whether the minecart can currently use rails.
     */
    public void setCanUseRail(boolean use) {
        this.canUseRail = use;
    }

    /**
     * Return false if this cart should not call IRail.onMinecartPass() and should ignore Powered Rails.
     * @return True if this cart should call IRail.onMinecartPass().
     */
    public boolean shouldDoRailFunctions() {
        return true;
    }

    /**
     * Simply returns the minecartType variable.
     * @return minecartType
     */ 
    public int getMinecartType() {
        return this.type;
    }

    /**
     * Gets the current global Minecart Collision handler if none
     * is registered, returns null
     * @return The collision handler or null
     */
    public static IMinecartCollisionHandler getCollisionHandler() {
        return collisionHandler;
    }

    /**
     * Sets the global Minecart Collision handler, overwrites any
     * that is currently set.
     * @param handler The new handler
     */
    public static void setCollisionHandler(IMinecartCollisionHandler handler) {
        collisionHandler = handler;
    }

    /**
     * Carts should return their drag factor here
     * @return The drag rate.
     */
    protected double getDrag() {
        return this.passenger != null ? defaultDragRidden : defaultDragEmpty;
    }

    /**
     * Moved to allow overrides.
     * This code applies drag and updates push forces.
     */
    protected void applyDragAndPushForces() {
        if (this.isPoweredCart()) {
            double d0 = (double)MathHelper.sqrt(this.b * this.b + this.c * this.c);

            if (d0 > 0.01D) {
                this.b /= d0;
                this.c /= d0;
                double d1 = 0.04D;
                this.motX *= 0.8D;
                this.motY *= 0.0D;
                this.motZ *= 0.8D;
                this.motX += this.b * d1;
                this.motZ += this.c * d1;
            } else {
                this.motX *= 0.9D;
                this.motY *= 0.0D;
                this.motZ *= 0.9D;
            }
        }

        this.motX *= this.getDrag();
        this.motY *= 0.0D;
        this.motZ *= this.getDrag();
    }

    /**
     * Moved to allow overrides.
     * This code updates push forces.
     */
    protected void updatePushForces() {
        if (this.isPoweredCart()) {
            double push = (double)MathHelper.sqrt(this.b * this.b + this.c * this.c);

            if (push > 0.01D && this.motX * this.motX + this.motZ * this.motZ > 0.001D) {
                this.b /= push;
                this.c /= push;

                if (this.b * this.motX + this.c * this.motZ < 0.0D) {
                    this.b = 0.0D;
                    this.c = 0.0D;
                } else {
                    this.b = this.motX;
                    this.c = this.motZ;
                }
            }
        }
    }

    /**
     * Moved to allow overrides.
     * This code handles minecart movement and speed capping when on a rail.
     */
    protected void moveMinecartOnRail(int i, int j, int k) {
        int id = this.world.getTypeId(i, j, k);

        if (BlockMinecartTrack.e(id))
        {
            float railMaxSpeed = ((BlockMinecartTrack)Block.byId[id]).getRailMaxSpeed(this.world, this, i, j, k);
            double maxSpeed = (double)Math.min(railMaxSpeed, this.getMaxSpeedRail());
            double mX = this.motX;
            double mZ = this.motZ;

            if (this.passenger != null) {
                mX *= 0.75D;
                mZ *= 0.75D;
            }

            if (mX < -maxSpeed) mX = -maxSpeed;
            if (mX > maxSpeed) mX = maxSpeed;
            if (mZ < -maxSpeed) mZ = -maxSpeed;
            if (mZ > maxSpeed) mZ = maxSpeed;

            this.move(mX, 0.0D, mZ);
        }
    }

    /**
     * Moved to allow overrides.
     * This code handles minecart movement and speed capping when not on a rail.
     */
    protected void moveMinecartOffRail(int i, int j, int k)
    {
        double d2 = (double)this.getMaxSpeedGround();

        if (!this.onGround)
        {
            d2 = (double)this.getMaxSpeedAirLateral();
        }

        if (this.motX < -d2) this.motX = -d2;
        if (this.motX > d2) this.motX = d2;
        if (this.motZ < -d2) this.motZ = -d2;
        if (this.motZ > d2) this.motZ = d2;

        double moveY = this.motY;

        if (this.getMaxSpeedAirVertical() > 0.0F && this.motY > (double)this.getMaxSpeedAirVertical())
        {
            moveY = (double)this.getMaxSpeedAirVertical();

            if (Math.abs(this.motX) < 0.30000001192092896D && Math.abs(this.motZ) < 0.30000001192092896D)
            {
                moveY = 0.15000000596046448D;
                this.motY = moveY;
            }
        }

        if (this.onGround)
        {
            this.motX *= 0.5D;
            this.motY *= 0.5D;
            this.motZ *= 0.5D;
        }

        this.move(this.motX, moveY, this.motZ);

        if (!this.onGround)
        {
            this.motX *= this.getDragAir();
            this.motY *= this.getDragAir();
            this.motZ *= this.getDragAir();
        }
    }

    /**
     * Moved to allow overrides.
     * This code applies fuel consumption.
     */
    protected void updateFuel() {
        if (this.e > 0) --this.e;
        if (this.e <= 0) this.b = this.c = 0.0D;
        this.e(this.e > 0);
    }

    /**
     * Moved to allow overrides, This code handle slopes affecting velocity.
     * @param metadata The blocks position metadata
     */
    protected void adjustSlopeVelocities(int metadata) {
        double acceleration = 0.0078125D;

        if (metadata == 2)
        {
            this.motX -= acceleration;
        }
        else if (metadata == 3)
        {
            this.motX += acceleration;
        }
        else if (metadata == 4)
        {
            this.motZ += acceleration;
        }
        else if (metadata == 5)
        {
            this.motZ -= acceleration;
        }
    }
    
    /**
     * Getters/setters for physics variables
     */

    /**
     * Returns the carts max speed.
     * Carts going faster than 1.1 cause issues with chunk loading.
     * Carts cant traverse slopes or corners at greater than 0.5 - 0.6.
     * This value is compared with the rails max speed to determine
     * the carts current max speed. A normal rails max speed is 0.4.
     * @return Carts max speed.
     */
    public float getMaxSpeedRail() {
        return this.maxSpeedRail;
    }

    public void setMaxSpeedRail(float value) {
        this.maxSpeedRail = value;
    }

    public float getMaxSpeedGround() {
        return this.maxSpeedGround;
    }

    public void setMaxSpeedGround(float value) {
        this.maxSpeedGround = value;
    }

    public float getMaxSpeedAirLateral() {
        return this.maxSpeedAirLateral;
    }

    public void setMaxSpeedAirLateral(float value) {
        this.maxSpeedAirLateral = value;
    }

    public float getMaxSpeedAirVertical() {
        return this.maxSpeedAirVertical;
    }

    public void setMaxSpeedAirVertical(float value) {
        this.maxSpeedAirVertical = value;
    }

    public double getDragAir() {
        return this.dragAir;
    }

    public void setDragAir(double value) {
        this.dragAir = value;
    }
    /* =================================================== FORGE END =====================================*/    
}