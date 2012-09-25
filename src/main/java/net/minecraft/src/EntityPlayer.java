package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start

import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
// CraftBukkit end

public abstract class EntityPlayer extends EntityLiving implements ICommandSender {

    public InventoryPlayer inventory = new InventoryPlayer(this);
    private InventoryEnderChest enderChest = new InventoryEnderChest();
    public Container defaultContainer;
    public Container craftingInventory;
    protected FoodStats foodData = new FoodStats();
    protected int bC = 0;
    public byte bD = 0;
    public int bE = 0;
    public float bF;
    public float bG;
    public boolean bH = false;
    public int bI = 0;
    public String username;
    public int dimension;
    public int bL = 0;
    public double bM;
    public double bN;
    public double bO;
    public double bP;
    public double bQ;
    public double bR;
    // CraftBukkit start
    public boolean sleeping;
    public boolean fauxSleeping;
    public String spawnWorld = "";

    public HumanEntity getBukkitEntity() {
        return (HumanEntity) super.getBukkitEntity();
    }
    // CraftBukkit end

    public ChunkCoordinates bT;
    public int sleepTicks; // CraftBukkit - private -> public
    public float bU;
    public float bV;
    private ChunkCoordinates c;
    private ChunkCoordinates d;
    public int bW = 20;
    protected boolean bX = false;
    public float bY;
    public PlayerCapabilities capabilities = new PlayerCapabilities();
    public int oldLevel = -1; // CraftBukkit
    public int expLevel;
    public int expTotal;
    public float exp;
    private ItemStack e;
    private int f;
    protected float cd = 0.1F;
    protected float ce = 0.02F;
    public EntityFishingHook hookedFish = null;

    public EntityPlayer(World world) {
        super(world);
        this.defaultContainer = new ContainerPlayer(this.inventory, !world.isStatic);
        this.craftingInventory = this.defaultContainer;
        this.yOffset = 1.62F;
        ChunkCoordinates chunkcoordinates = world.getSpawn();

        this.setPositionRotation((double) chunkcoordinates.x + 0.5D, (double) (chunkcoordinates.y + 1), (double) chunkcoordinates.z + 0.5D, 0.0F, 0.0F);
        this.aC = "humanoid";
        this.aB = 180.0F;
        this.fireResistance = 20;
        this.texture = "/mob/char.png";
    }

    public int getMaxHealth() {
        return 20;
    }

    protected void entityInit() {
        super.entityInit();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
        this.datawatcher.a(17, Byte.valueOf((byte) 0));
    }

    public boolean bw() {
        return this.e != null;
    }

    public void by() {
        if (this.e != null) {
            this.e.b(this.worldObj, this, this.f);
        }

        this.bz();
    }

    public void bz() {
        this.e = null;
        this.f = 0;
        if (!this.worldObj.isStatic) {
            this.c(false);
        }
    }

    public boolean isBlocking() {
        return this.bw() && Item.itemsList[this.e.id].b(this.e) == EnumAnimation.d;
    }

    public void h_() {
        if (this.e != null) {
            ItemStack itemstack = this.inventory.getItemInHand();

            if (itemstack == this.e) {
                if (this.f <= 25 && this.f % 4 == 0) {
                    this.c(itemstack, 5);
                }

                if (--this.f == 0 && !this.worldObj.isStatic) {
                    this.o();
                }
            } else {
                this.bz();
            }
        }

        if (this.bL > 0) {
            --this.bL;
        }

        if (this.isSleeping()) {
            ++this.sleepTicks;
            if (this.sleepTicks > 100) {
                this.sleepTicks = 100;
            }

            if (!this.worldObj.isStatic) {
                if (!this.l()) {
                    this.a(true, true, false);
                } else if (this.worldObj.s()) {
                    this.a(false, true, true);
                }
            }
        } else if (this.sleepTicks > 0) {
            ++this.sleepTicks;
            if (this.sleepTicks >= 110) {
                this.sleepTicks = 0;
            }
        }

        super.h_();
        if (!this.worldObj.isStatic && this.craftingInventory != null && !this.craftingInventory.c(this)) {
            this.closeScreen();
            this.craftingInventory = this.defaultContainer;
        }

        if (this.isBurning() && this.capabilities.isInvulnerable) {
            this.extinguish();
        }

        this.bM = this.bP;
        this.bN = this.bQ;
        this.bO = this.bR;
        double d0 = this.posX - this.bP;
        double d1 = this.posY - this.bQ;
        double d2 = this.posZ - this.bR;
        double d3 = 10.0D;

        if (d0 > d3) {
            this.bM = this.bP = this.posX;
        }

        if (d2 > d3) {
            this.bO = this.bR = this.posZ;
        }

        if (d1 > d3) {
            this.bN = this.bQ = this.posY;
        }

        if (d0 < -d3) {
            this.bM = this.bP = this.posX;
        }

        if (d2 < -d3) {
            this.bO = this.bR = this.posZ;
        }

        if (d1 < -d3) {
            this.bN = this.bQ = this.posY;
        }

        this.bP += d0 * 0.25D;
        this.bR += d2 * 0.25D;
        this.bQ += d1 * 0.25D;
        this.a(StatisticList.k, 1);
        if (this.ridingEntity == null) {
            this.d = null;
        }

        if (!this.worldObj.isStatic) {
            this.foodData.a(this);
        }
    }

    protected void c(ItemStack itemstack, int i) {
        if (itemstack.n() == EnumAnimation.c) {
            this.worldObj.makeSound(this, "random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (itemstack.n() == EnumAnimation.b) {
            for (int j = 0; j < i; ++j) {
                Vec3 vec3d = Vec3.a().create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

                vec3d.a(-this.rotationPitch * 3.1415927F / 180.0F);
                vec3d.b(-this.rotationYaw * 3.1415927F / 180.0F);
                Vec3 vec3d1 = Vec3.a().create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

                vec3d1.a(-this.rotationPitch * 3.1415927F / 180.0F);
                vec3d1.b(-this.rotationYaw * 3.1415927F / 180.0F);
                vec3d1 = vec3d1.add(this.posX, this.posY + (double) this.getHeadHeight(), this.posZ);
                this.worldObj.a("iconcrack_" + itemstack.getItem().id, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c);
            }

            this.worldObj.makeSound(this, "random.eat", 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected void o() {
        if (this.e != null) {
            this.c(this.e, 16);
            int i = this.e.count;
            ItemStack itemstack = this.e.b(this.worldObj, this);

            if (itemstack != this.e || itemstack != null && itemstack.count != i) {
                this.inventory.items[this.inventory.itemInHandIndex] = itemstack;
                if (itemstack.count == 0) {
                    this.inventory.items[this.inventory.itemInHandIndex] = null;
                }
            }

            this.bz();
        }
    }

    protected boolean aX() {
        return this.getHealth() <= 0 || this.isSleeping();
    }

    // CraftBukkit - protected -> public
    public void closeScreen() {
        this.craftingInventory = this.defaultContainer;
    }

    public void U() {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;

        super.U();
        this.bF = this.bG;
        this.bG = 0.0F;
        this.k(this.posX - d0, this.posY - d1, this.posZ - d2);
    }

    private int k() {
        return this.hasEffect(Potion.FASTER_DIG) ? 6 - (1 + this.getEffect(Potion.FASTER_DIG).getAmplifier()) * 1 : (this.hasEffect(Potion.SLOWER_DIG) ? 6 + (1 + this.getEffect(Potion.SLOWER_DIG).getAmplifier()) * 2 : 6);
    }

    protected void be() {
        int i = this.k();

        if (this.bH) {
            ++this.bI;
            if (this.bI >= i) {
                this.bI = 0;
                this.bH = false;
            }
        } else {
            this.bI = 0;
        }

        this.aJ = (float) this.bI / (float) i;
    }

    public void d() {
        if (this.bC > 0) {
            --this.bC;
        }

        if (this.worldObj.difficulty == 0 && this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 * 12 == 0) {
            // CraftBukkit - added regain reason of "REGEN" for filtering purposes.
            this.heal(1, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.REGEN);
        }

        this.inventory.k();
        this.bF = this.bG;
        super.d();
        this.aG = this.capabilities.b();
        this.aH = this.ce;
        if (this.isSprinting()) {
            this.aG = (float) ((double) this.aG + (double) this.capabilities.b() * 0.3D);
            this.aH = (float) ((double) this.aH + (double) this.ce * 0.3D);
        }

        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        // CraftBukkit - Math -> TrigMath
        float f1 = (float) org.bukkit.craftbukkit.TrigMath.atan(-this.motionY * 0.20000000298023224D) * 15.0F;

        if (f > 0.1F) {
            f = 0.1F;
        }

        if (!this.onGround || this.getHealth() <= 0) {
            f = 0.0F;
        }

        if (this.onGround || this.getHealth() <= 0) {
            f1 = 0.0F;
        }

        this.bG += (f - this.bG) * 0.4F;
        this.aT += (f1 - this.aT) * 0.8F;
        if (this.getHealth() > 0) {
            List list = this.worldObj.getEntities(this, this.boundingBox.grow(1.0D, 0.0D, 1.0D));

            if (list != null) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    if (!entity.isDead) {
                        this.o(entity);
                    }
                }
            }
        }
    }

    private void o(Entity entity) {
        entity.b_(this);
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.setSize(0.2F, 0.2F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.10000000149011612D;
        if (this.username.equals("Notch")) {
            this.a(new ItemStack(Item.APPLE, 1), true);
        }

        this.inventory.m();
        if (damagesource != null) {
            this.motionX = (double) (-MathHelper.cos((this.aP + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
            this.motionZ = (double) (-MathHelper.sin((this.aP + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
        } else {
            this.motionX = this.motionZ = 0.0D;
        }

        this.yOffset = 0.1F;
        this.a(StatisticList.y, 1);
    }

    public void c(Entity entity, int i) {
        this.bE += i;
        if (entity instanceof EntityPlayer) {
            this.a(StatisticList.A, 1);
        } else {
            this.a(StatisticList.z, 1);
        }
    }

    protected int h(int i) {
        int j = EnchantmentManager.getOxygenEnchantmentLevel(this.inventory);

        return j > 0 && this.random.nextInt(j + 1) > 0 ? i : super.h(i);
    }

    public EntityItem bB() {
        return this.a(this.inventory.splitStack(this.inventory.itemInHandIndex, 1), false);
    }

    public EntityItem drop(ItemStack itemstack) {
        return this.a(itemstack, false);
    }

    public EntityItem a(ItemStack itemstack, boolean flag) {
        if (itemstack == null) {
            return null;
        } else {
            EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY - 0.30000001192092896D + (double) this.getHeadHeight(), this.posZ, itemstack);

            entityitem.delayBeforeCanPickup = 40;
            float f = 0.1F;
            float f1;

            if (flag) {
                f1 = this.random.nextFloat() * 0.5F;
                float f2 = this.random.nextFloat() * 3.1415927F * 2.0F;

                entityitem.motionX = (double) (-MathHelper.sin(f2) * f1);
                entityitem.motionZ = (double) (MathHelper.cos(f2) * f1);
                entityitem.motionY = 0.20000000298023224D;
            } else {
                f = 0.3F;
                entityitem.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
                entityitem.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
                entityitem.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f + 0.1F);
                f = 0.02F;
                f1 = this.random.nextFloat() * 3.1415927F * 2.0F;
                f *= this.random.nextFloat();
                entityitem.motionX += Math.cos((double) f1) * (double) f;
                entityitem.motionY += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
                entityitem.motionZ += Math.sin((double) f1) * (double) f;
            }

            // CraftBukkit start
            Player player = (Player) this.getBukkitEntity();
            CraftItem drop = new CraftItem(this.worldObj.getServer(), entityitem);

            PlayerDropItemEvent event = new PlayerDropItemEvent(player, drop);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                player.getInventory().addItem(drop.getItemStack());
                return null;
            }
            // CraftBukkit end

            this.a(entityitem);
            this.a(StatisticList.v, 1);
            return entityitem;
        }
    }

    protected void a(EntityItem entityitem) {
        this.worldObj.addEntity(entityitem);
    }

    public float a(Block block) {
        float f = this.inventory.a(block);
        int i = EnchantmentManager.getDigSpeedEnchantmentLevel(this.inventory);

        if (i > 0 && this.inventory.b(block)) {
            f += (float) (i * i + 1);
        }

        if (this.hasEffect(Potion.FASTER_DIG)) {
            f *= 1.0F + (float) (this.getEffect(Potion.FASTER_DIG).getAmplifier() + 1) * 0.2F;
        }

        if (this.hasEffect(Potion.SLOWER_DIG)) {
            f *= 1.0F - (float) (this.getEffect(Potion.SLOWER_DIG).getAmplifier() + 1) * 0.2F;
        }

        if (this.a(Material.WATER) && !EnchantmentManager.hasWaterWorkerEnchantment(this.inventory)) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    public boolean b(Block block) {
        return this.inventory.b(block);
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Inventory");

        this.inventory.b(nbttaglist);
        this.dimension = nbttagcompound.getInteger("Dimension");
        this.sleeping = nbttagcompound.getBoolean("Sleeping");
        this.sleepTicks = nbttagcompound.getShort("SleepTimer");
        this.exp = nbttagcompound.getFloat("XpP");
        this.expLevel = nbttagcompound.getInteger("XpLevel");
        this.expTotal = nbttagcompound.getInteger("XpTotal");
        if (this.sleeping) {
            this.bT = new ChunkCoordinates(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
            this.a(true, true, false);
        }

        // CraftBukkit start
        this.spawnWorld = nbttagcompound.getString("SpawnWorld");
        if ("".equals(spawnWorld)) {
            this.spawnWorld = this.worldObj.getServer().getWorlds().get(0).getName();
        }
        // CraftBukkit end

        if (nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ")) {
            this.c = new ChunkCoordinates(nbttagcompound.getInteger("SpawnX"), nbttagcompound.getInteger("SpawnY"), nbttagcompound.getInteger("SpawnZ"));
        }

        this.foodData.a(nbttagcompound);
        this.capabilities.b(nbttagcompound);
        if (nbttagcompound.hasKey("EnderItems")) {
            NBTTagList nbttaglist1 = nbttagcompound.getList("EnderItems");

            this.enderChest.a(nbttaglist1);
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        nbttagcompound.set("Inventory", this.inventory.a(new NBTTagList()));
        nbttagcompound.setInteger("Dimension", this.dimension);
        nbttagcompound.setBoolean("Sleeping", this.sleeping);
        nbttagcompound.setShort("SleepTimer", (short) this.sleepTicks);
        nbttagcompound.setFloat("XpP", this.exp);
        nbttagcompound.setInteger("XpLevel", this.expLevel);
        nbttagcompound.setInteger("XpTotal", this.expTotal);
        if (this.c != null) {
            nbttagcompound.setInteger("SpawnX", this.c.x);
            nbttagcompound.setInteger("SpawnY", this.c.y);
            nbttagcompound.setInteger("SpawnZ", this.c.z);
            nbttagcompound.setString("SpawnWorld", spawnWorld); // CraftBukkit - fixes bed spawns for multiworld worlds
        }

        this.foodData.b(nbttagcompound);
        this.capabilities.a(nbttagcompound);
        nbttagcompound.set("EnderItems", this.enderChest.g());
    }

    public void displayGUIChest(IInventory iinventory) {}

    public void displayGUIEnchantment(int i, int j, int k) {}

    public void displayGUIWorkbench(int i, int j, int k) {}

    public void receive(Entity entity, int i) {}

    public float getHeadHeight() {
        return 0.12F;
    }

    protected void d_() {
        this.yOffset = 1.62F;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.capabilities.isInvulnerable && !damagesource.ignoresInvulnerability()) {
            return false;
        } else {
            this.bq = 0;
            if (this.getHealth() <= 0) {
                return false;
            } else {
                if (this.isSleeping() && !this.worldObj.isStatic) {
                    this.a(true, true, false);
                }

                Entity entity = damagesource.getEntity();

                if (damagesource.n()) {
                    if (this.worldObj.difficulty == 0) {
                        return false; // CraftBukkit - i = 0 -> return false
                    }

                    if (this.worldObj.difficulty == 1) {
                        i = i / 2 + 1;
                    }

                    if (this.worldObj.difficulty == 3) {
                        i = i * 3 / 2;
                    }
                }

                if (false && i == 0) { // CraftBukkit - Don't filter out 0 damage
                    return false;
                } else {
                    Entity entity1 = damagesource.getEntity();

                    if (entity1 instanceof EntityArrow && ((EntityArrow) entity1).shootingEntity != null) {
                        entity1 = ((EntityArrow) entity1).shootingEntity;
                    }

                    if (entity1 instanceof EntityLiving) {
                        this.a((EntityLiving) entity1, false);
                    }

                    this.a(StatisticList.x, i);
                    return super.damageEntity(damagesource, i);
                }
            }
        }
    }

    protected int c(DamageSource damagesource, int i) {
        int j = super.c(damagesource, i);

        if (j <= 0) {
            return 0;
        } else {
            int k = EnchantmentManager.a(this.inventory, damagesource);

            if (k > 20) {
                k = 20;
            }

            if (k > 0 && k <= 20) {
                int l = 25 - k;
                int i1 = j * l + this.aM;

                j = i1 / 25;
                this.aM = i1 % 25;
            }

            return j;
        }
    }

    protected boolean h() {
        return false;
    }

    protected void a(EntityLiving entityliving, boolean flag) {
        if (!(entityliving instanceof EntityCreeper) && !(entityliving instanceof EntityGhast)) {
            if (entityliving instanceof EntityWolf) {
                EntityWolf entitywolf = (EntityWolf) entityliving;

                if (entitywolf.isTamed() && this.username.equals(entitywolf.getOwnerName())) {
                    return;
                }
            }

            if (!(entityliving instanceof EntityPlayer) || this.h()) {
                List list = this.worldObj.a(EntityWolf.class, AxisAlignedBB.a().a(this.posX, this.posY, this.posZ, this.posX + 1.0D, this.posY + 1.0D, this.posZ + 1.0D).grow(16.0D, 4.0D, 16.0D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityWolf entitywolf1 = (EntityWolf) iterator.next();

                    if (entitywolf1.isTamed() && entitywolf1.m() == null && this.username.equals(entitywolf1.getOwnerName()) && (!flag || !entitywolf1.isSitting())) {
                        entitywolf1.setSitting(false);
                        entitywolf1.setTarget(entityliving);
                    }
                }
            }
        }
    }

    protected void k(int i) {
        this.inventory.g(i);
    }

    public int aO() {
        return this.inventory.l();
    }

    protected void d(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor() && this.isBlocking()) {
            i = 1 + i >> 1;
        }

        i = this.b(damagesource, i);
        i = this.c(damagesource, i);
        this.j(damagesource.d());
        this.health -= i;
    }

    public void DisplayGUIFurnace(TileEntityFurnace tileentityfurnace) {}

    public void displayGUIDispenser(TileEntityDispenser tileentitydispenser) {}

    public void a(TileEntitySign tileentitysign) {}

    public void displayGUIBrewingStand(TileEntityBrewingStand tileentitybrewingstand) {}

    public void openTrade(IMerchant imerchant) {}

    public void c(ItemStack itemstack) {}

    public boolean m(Entity entity) {
        if (entity.c(this)) {
            return true;
        } else {
            ItemStack itemstack = this.bC();

            if (itemstack != null && entity instanceof EntityLiving) {
                if (this.capabilities.canInstantlyBuild) {
                    itemstack = itemstack.cloneItemStack();
                }

                if (itemstack.a((EntityLiving) entity)) {
                    // CraftBukkit - bypass infinite items; <= 0 -> == 0
                    if (itemstack.count == 0 && !this.capabilities.canInstantlyBuild) {
                        this.bD();
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public ItemStack bC() {
        return this.inventory.getItemInHand();
    }

    public void bD() {
        this.inventory.setItem(this.inventory.itemInHandIndex, (ItemStack) null);
    }

    public double W() {
        return (double) (this.yOffset - 0.5F);
    }

    public void i() {
        if (!this.bH || this.bI >= this.k() / 2 || this.bI < 0) {
            this.bI = -1;
            this.bH = true;
        }
    }

    public void attack(Entity entity) {
        if (entity.an()) {
            int i = this.inventory.a(entity);

            if (this.hasEffect(Potion.INCREASE_DAMAGE)) {
                i += 3 << this.getEffect(Potion.INCREASE_DAMAGE).getAmplifier();
            }

            if (this.hasEffect(Potion.WEAKNESS)) {
                i -= 2 << this.getEffect(Potion.WEAKNESS).getAmplifier();
            }

            int j = 0;
            int k = 0;

            if (entity instanceof EntityLiving) {
                k = EnchantmentManager.a(this.inventory, (EntityLiving) entity);
                j += EnchantmentManager.getKnockbackEnchantmentLevel(this.inventory, (EntityLiving) entity);
            }

            if (this.isSprinting()) {
                ++j;
            }

            if (i > 0 || k > 0) {
                boolean flag = this.fallDistance > 0.0F && !this.onGround && !this.f_() && !this.H() && !this.hasEffect(Potion.BLINDNESS) && this.ridingEntity == null && entity instanceof EntityLiving;

                if (flag) {
                    i += this.random.nextInt(i / 2 + 2);
                }

                i += k;
                boolean flag1 = entity.damageEntity(DamageSource.playerAttack(this), i);

                // CraftBukkit start - Return when the damage fails so that the item will not lose durability
                if (!flag1) {
                    return;
                }
                // CraftBukkit end

                if (flag1) {
                    if (j > 0) {
                        entity.g((double) (-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * (float) j * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * (float) j * 0.5F));
                        this.motionX *= 0.6D;
                        this.motionZ *= 0.6D;
                        this.setSprinting(false);
                    }

                    if (flag) {
                        this.b(entity);
                    }

                    if (k > 0) {
                        this.c(entity);
                    }

                    if (i >= 18) {
                        this.a((Statistic) AchievementList.E);
                    }

                    this.j(entity);
                }

                ItemStack itemstack = this.bC();

                if (itemstack != null && entity instanceof EntityLiving) {
                    itemstack.a((EntityLiving) entity, this);
                    // CraftBukkit - bypass infinite items; <= 0 -> == 0
                    if (itemstack.count == 0) {
                        this.bD();
                    }
                }

                if (entity instanceof EntityLiving) {
                    if (entity.isEntityAlive()) {
                        this.a((EntityLiving) entity, true);
                    }

                    this.a(StatisticList.w, i);
                    int l = EnchantmentManager.getFireAspectEnchantmentLevel(this.inventory, (EntityLiving) entity);

                    if (l > 0) {
                        // CraftBukkit start - raise a combust event when somebody hits with a fire enchanted item
                        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), l * 4);
                        org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                        if (!combustEvent.isCancelled()) {
                            entity.setOnFire(combustEvent.getDuration());
                        }
                        // CraftBukkit end
                    }
                }

                this.j(0.3F);
            }
        }
    }

    public void b(Entity entity) {}

    public void c(Entity entity) {}

    public void setDead() {
        super.setDead();
        this.defaultContainer.a(this);
        if (this.craftingInventory != null) {
            this.craftingInventory.a(this);
        }
    }

    public boolean inBlock() {
        return !this.sleeping && super.inBlock();
    }

    public boolean bF() {
        return false;
    }

    public EnumBedResult a(int i, int j, int k) {
        if (!this.worldObj.isStatic) {
            if (this.isSleeping() || !this.isEntityAlive()) {
                return EnumBedResult.OTHER_PROBLEM;
            }

            if (!this.worldObj.worldProvider.d()) {
                return EnumBedResult.NOT_POSSIBLE_HERE;
            }

            if (this.worldObj.s()) {
                return EnumBedResult.NOT_POSSIBLE_NOW;
            }

            if (Math.abs(this.posX - (double) i) > 3.0D || Math.abs(this.posY - (double) j) > 2.0D || Math.abs(this.posZ - (double) k) > 3.0D) {
                return EnumBedResult.TOO_FAR_AWAY;
            }

            double d0 = 8.0D;
            double d1 = 5.0D;
            List list = this.worldObj.a(EntityMob.class, AxisAlignedBB.a().a((double) i - d0, (double) j - d1, (double) k - d0, (double) i + d0, (double) j + d1, (double) k + d0));

            if (!list.isEmpty()) {
                return EnumBedResult.NOT_SAFE;
            }
        }

        // CraftBukkit start
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();
            org.bukkit.block.Block bed = this.worldObj.getWorld().getBlockAt(i, j, k);

            PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, bed);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return EnumBedResult.OTHER_PROBLEM;
            }
        }
        // CraftBukkit end

        this.setSize(0.2F, 0.2F);
        this.yOffset = 0.2F;
        if (this.worldObj.isLoaded(i, j, k)) {
            int l = this.worldObj.getData(i, j, k);
            int i1 = BlockBed.d(l);
            float f = 0.5F;
            float f1 = 0.5F;

            switch (i1) {
            case 0:
                f1 = 0.9F;
                break;

            case 1:
                f = 0.1F;
                break;

            case 2:
                f1 = 0.1F;
                break;

            case 3:
                f = 0.9F;
            }

            this.b(i1);
            this.setPosition((double) ((float) i + f), (double) ((float) j + 0.9375F), (double) ((float) k + f1));
        } else {
            this.setPosition((double) ((float) i + 0.5F), (double) ((float) j + 0.9375F), (double) ((float) k + 0.5F));
        }

        this.sleeping = true;
        this.sleepTicks = 0;
        this.bT = new ChunkCoordinates(i, j, k);
        this.motionX = this.motionZ = this.motionY = 0.0D;
        if (!this.worldObj.isStatic) {
            this.worldObj.everyoneSleeping();
        }

        return EnumBedResult.OK;
    }

    private void b(int i) {
        this.bU = 0.0F;
        this.bV = 0.0F;
        switch (i) {
        case 0:
            this.bV = -1.8F;
            break;

        case 1:
            this.bU = 1.8F;
            break;

        case 2:
            this.bV = 1.8F;
            break;

        case 3:
            this.bU = -1.8F;
        }
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        this.setSize(0.6F, 1.8F);
        this.d_();
        ChunkCoordinates chunkcoordinates = this.bT;
        ChunkCoordinates chunkcoordinates1 = this.bT;

        if (chunkcoordinates != null && this.worldObj.getBlockId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) == Block.BED.blockID) {
            BlockBed.a(this.worldObj, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, false);
            chunkcoordinates1 = BlockBed.b(this.worldObj, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);
            if (chunkcoordinates1 == null) {
                chunkcoordinates1 = new ChunkCoordinates(chunkcoordinates.x, chunkcoordinates.y + 1, chunkcoordinates.z);
            }

            this.setPosition((double) ((float) chunkcoordinates1.x + 0.5F), (double) ((float) chunkcoordinates1.y + this.yOffset + 0.1F), (double) ((float) chunkcoordinates1.z + 0.5F));
        }

        this.sleeping = false;
        if (!this.worldObj.isStatic && flag1) {
            this.worldObj.everyoneSleeping();
        }

        // CraftBukkit start
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();

            org.bukkit.block.Block bed;
            if (chunkcoordinates != null) {
                bed = this.worldObj.getWorld().getBlockAt(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z);
            } else {
                bed = this.worldObj.getWorld().getBlockAt(player.getLocation());
            }

            PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, bed);
            this.worldObj.getServer().getPluginManager().callEvent(event);
        }
        // CraftBukkit end

        if (flag) {
            this.sleepTicks = 0;
        } else {
            this.sleepTicks = 100;
        }

        if (flag2) {
            this.setRespawnPosition(this.bT);
        }
    }

    private boolean l() {
        return this.worldObj.getBlockId(this.bT.x, this.bT.y, this.bT.z) == Block.BED.blockID;
    }

    public static ChunkCoordinates getBed(World world, ChunkCoordinates chunkcoordinates) {
        IChunkProvider ichunkprovider = world.F();

        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z + 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z + 3 >> 4);
        if (world.getBlockId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) != Block.BED.blockID) {
            return null;
        } else {
            ChunkCoordinates chunkcoordinates1 = BlockBed.b(world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);

            return chunkcoordinates1;
        }
    }

    public boolean isSleeping() {
        return this.sleeping;
    }

    public boolean isDeeplySleeping() {
        return this.sleeping && this.sleepTicks >= 100;
    }

    public void c(String s) {}

    public ChunkCoordinates getBed() {
        return this.c;
    }

    public void setRespawnPosition(ChunkCoordinates chunkcoordinates) {
        if (chunkcoordinates != null) {
            this.c = new ChunkCoordinates(chunkcoordinates);
            this.spawnWorld = this.worldObj.worldData.getName();
        } else {
            this.c = null;
        }
    }

    public void a(Statistic statistic) {
        this.a(statistic, 1);
    }

    public void a(Statistic statistic, int i) {}

    protected void aZ() {
        super.aZ();
        this.a(StatisticList.u, 1);
        if (this.isSprinting()) {
            this.j(0.8F);
        } else {
            this.j(0.2F);
        }
    }

    public void e(float f, float f1) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;

        if (this.capabilities.isFlying && this.ridingEntity == null) {
            double d3 = this.motionY;
            float f2 = this.aH;

            this.aH = this.capabilities.a();
            super.e(f, f1);
            this.motionY = d3 * 0.6D;
            this.aH = f2;
        } else {
            super.e(f, f1);
        }

        this.checkMovement(this.posX - d0, this.posY - d1, this.posZ - d2);
    }

    public void checkMovement(double d0, double d1, double d2) {
        if (this.ridingEntity == null) {
            int i;

            if (this.a(Material.WATER)) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.q, i);
                    this.j(0.015F * (float) i * 0.01F);
                }
            } else if (this.H()) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.m, i);
                    this.j(0.015F * (float) i * 0.01F);
                }
            } else if (this.f_()) {
                if (d1 > 0.0D) {
                    this.a(StatisticList.o, (int) Math.round(d1 * 100.0D));
                }
            } else if (this.onGround) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.l, i);
                    if (this.isSprinting()) {
                        this.j(0.099999994F * (float) i * 0.01F);
                    } else {
                        this.j(0.01F * (float) i * 0.01F);
                    }
                }
            } else {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.a(StatisticList.p, i);
                }
            }
        }
    }

    private void k(double d0, double d1, double d2) {
        if (this.ridingEntity != null) {
            int i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);

            if (i > 0) {
                if (this.ridingEntity instanceof EntityMinecart) {
                    this.a(StatisticList.r, i);
                    if (this.d == null) {
                        this.d = new ChunkCoordinates(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
                    } else if ((double) this.d.e(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) >= 1000000.0D) {
                        this.a((Statistic) AchievementList.q, 1);
                    }
                } else if (this.ridingEntity instanceof EntityBoat) {
                    this.a(StatisticList.s, i);
                } else if (this.ridingEntity instanceof EntityPig) {
                    this.a(StatisticList.t, i);
                }
            }
        }
    }

    protected void a(float f) {
        if (!this.capabilities.canFly) {
            if (f >= 2.0F) {
                this.a(StatisticList.n, (int) Math.round((double) f * 100.0D));
            }

            super.a(f);
        }
    }

    public void a(EntityLiving entityliving) {
        if (entityliving instanceof EntityMob) {
            this.a((Statistic) AchievementList.s);
        }
    }

    public void aa() {
        if (this.bW > 0) {
            this.bW = 10;
        } else {
            this.bX = true;
        }
    }

    public void giveExp(int i) {
        this.bE += i;
        int j = Integer.MAX_VALUE - this.expTotal;

        if (i > j) {
            i = j;
        }

        this.exp += (float) i / (float) this.xpBarCap();

        for (this.expTotal += i; this.exp >= 1.0F; this.exp /= (float) this.xpBarCap()) {
            this.exp = (this.exp - 1.0F) * (float) this.xpBarCap();
            this.levelUp();
        }
    }

    public void levelDown(int i) {
        this.expLevel -= i;
        if (this.expLevel < 0) {
            this.expLevel = 0;
        }
    }

    public int xpBarCap() {
        return this.expLevel >= 30 ? 62 + (this.expLevel - 30) * 7 : (this.expLevel >= 15 ? 17 + (this.expLevel - 15) * 3 : 17);
    }

    private void levelUp() {
        ++this.expLevel;
    }

    public void j(float f) {
        if (!this.capabilities.isInvulnerable) {
            if (!this.worldObj.isStatic) {
                this.foodData.a(f);
            }
        }
    }

    public FoodStats getFoodData() {
        return this.foodData;
    }

    public boolean e(boolean flag) {
        return (flag || this.foodData.c()) && !this.capabilities.isInvulnerable;
    }

    public boolean bM() {
        return this.getHealth() > 0 && this.getHealth() < this.getMaxHealth();
    }

    public void a(ItemStack itemstack, int i) {
        if (itemstack != this.e) {
            this.e = itemstack;
            this.f = i;
            if (!this.worldObj.isStatic) {
                this.c(true);
            }
        }
    }

    public boolean e(int i, int j, int k) {
        return this.capabilities.mayBuild;
    }

    protected int getExpValue(EntityPlayer entityhuman) {
        int i = this.expLevel * 7;

        return i > 100 ? 100 : i;
    }

    protected boolean alwaysGivesExp() {
        return true;
    }

    public String getLocalizedName() {
        return this.username;
    }

    public void c(int i) {}

    public void copyTo(EntityPlayer entityhuman, boolean flag) {
        if (flag) {
            this.inventory.b(entityhuman.inventory);
            this.health = entityhuman.health;
            this.foodData = entityhuman.foodData;
            this.expLevel = entityhuman.expLevel;
            this.expTotal = entityhuman.expTotal;
            this.exp = entityhuman.exp;
            this.bE = entityhuman.bE;
        }

        this.enderChest = entityhuman.enderChest;
    }

    protected boolean e_() {
        return !this.capabilities.isFlying;
    }

    public void sendPlayerAbilities() {}

    public void a(EnumGamemode enumgamemode) {}

    public String getName() {
        return this.username;
    }

    public LocaleLanguage getLocale() {
        return LocaleLanguage.a();
    }

    public String a(String s, Object... aobject) {
        return this.getLocale().a(s, aobject);
    }

    public InventoryEnderChest getInventoryEnderChest() {
        return this.enderChest;
    }
}
