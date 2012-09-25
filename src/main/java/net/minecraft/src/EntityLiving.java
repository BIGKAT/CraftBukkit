package net.minecraft.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// CraftBukkit start

import net.minecraft.server.*;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
// CraftBukkit end

public abstract class EntityLiving extends Entity {

    public int maxHurtResistantTime = 20;
    public float ao;
    public float ap;
    public float aq = 0.0F;
    public float ar = 0.0F;
    public float as = 0.0F;
    public float at = 0.0F;
    protected float au;
    protected float av;
    protected float aw;
    protected float ax;
    protected boolean ay = true;
    protected String texture = "/mob/char.png";
    protected boolean aA = true;
    protected float aB = 0.0F;
    protected String aC = null;
    protected float aD = 1.0F;
    protected int aE = 0;
    protected float aF = 0.0F;
    public float aG = 0.1F;
    public float aH = 0.02F;
    public float aI;
    public float aJ;
    protected int health = this.getMaxHealth();
    public int aL;
    protected int aM;
    private int a;
    public int hurtTicks;
    public int aO;
    public float aP = 0.0F;
    public int deathTicks = 0;
    public int attackTicks = 0;
    public float aS;
    public float aT;
    protected boolean aU = false;
    protected int aV;
    public int aW = -1;
    public float aX = (float) (Math.random() * 0.8999999761581421D + 0.10000000149011612D);
    public float aY;
    public float aZ;
    public float ba;
    public net.minecraft.src.EntityPlayer attackingPlayer = null; // CraftBukkit - protected -> public
    protected int lastDamageByPlayerTime = 0;
    public EntityLiving lastDamager = null; // CraftBukkit - private -> public
    private int c = 0;
    private EntityLiving d = null;
    public int bd = 0;
    public int be = 0;
    public HashMap activePotionsMap = new HashMap(); // CraftBukkit - protected -> public
    public boolean potionsNeedUpdate = true; // CraftBukkit - private -> public
    private int f;
    private EntityLookHelper lookController;
    private EntityMoveHelper moveController;
    private ControllerJump jumpController;
    private EntityAIBodyControl senses;
    private Navigation navigation;
    protected final EntityAITasks goalSelector;
    protected final EntityAITasks targetSelector;
    private EntityLiving bz;
    private EntitySenses bA;
    private float bB;
    private ChunkCoordinates bC = new ChunkCoordinates(0, 0, 0);
    private float bD = -1.0F;
    protected int bi;
    protected double bj;
    protected double bk;
    protected double bl;
    protected double bm;
    protected double bn;
    float bo = 0.0F;
    public int lastDamage = 0; // CraftBukkit - protected -> public
    protected int bq = 0;
    protected float br;
    protected float bs;
    protected float bt;
    protected boolean bu = false;
    protected float bv = 0.0F;
    protected float bw = 0.7F;
    private int bE = 0;
    private Entity bF;
    protected int bx = 0;
    public int expToDrop = 0; // CraftBukkit
    public int maxAirTicks = 300; // CraftBukkit

    public EntityLiving(net.minecraft.src.World world) {
        super(world);
        this.m = true;
        this.goalSelector = new EntityAITasks(world != null && world.methodProfiler != null ? world.methodProfiler : null);
        this.targetSelector = new EntityAITasks(world != null && world.methodProfiler != null ? world.methodProfiler : null);
        this.lookController = new EntityLookHelper(this);
        this.moveController = new EntityMoveHelper(this);
        this.jumpController = new ControllerJump(this);
        this.senses = new EntityAIBodyControl(this);
        this.navigation = new Navigation(this, world, 16.0F);
        this.bA = new EntitySenses(this);
        this.ap = (float) (Math.random() + 1.0D) * 0.01F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.ao = (float) Math.random() * 12398.0F;
        this.rotationYaw = (float) (Math.random() * 3.1415927410125732D * 2.0D);
        this.as = this.rotationYaw;
        this.W = 0.5F;
    }

    public EntityLookHelper getControllerLook() {
        return this.lookController;
    }

    public EntityMoveHelper getControllerMove() {
        return this.moveController;
    }

    public ControllerJump getControllerJump() {
        return this.jumpController;
    }

    public Navigation getNavigation() {
        return this.navigation;
    }

    public EntitySenses getEntitySenses() {
        return this.bA;
    }

    public Random au() {
        return this.random;
    }

    public EntityLiving av() {
        return this.lastDamager;
    }

    public EntityLiving aw() {
        return this.d;
    }

    public void j(Entity entity) {
        if (entity instanceof EntityLiving) {
            this.d = (EntityLiving) entity;
        }
    }

    public int ax() {
        return this.bq;
    }

    public float am() {
        return this.as;
    }

    public float ay() {
        return this.bB;
    }

    public void e(float f) {
        this.bB = f;
        this.f(f);
    }

    public boolean k(Entity entity) {
        this.j(entity);
        return false;
    }

    public EntityLiving az() {
        return this.bz;
    }

    public void b(EntityLiving entityliving) {
        this.bz = entityliving;
    }

    public boolean a(Class oclass) {
        return EntityCreeper.class != oclass && EntityGhast.class != oclass;
    }

    public void aA() {}

    public boolean aB() {
        return this.d(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
    }

    public boolean d(int i, int j, int k) {
        return this.bD == -1.0F ? true : this.bC.e(i, j, k) < this.bD * this.bD;
    }

    public void b(int i, int j, int k, int l) {
        this.bC.b(i, j, k);
        this.bD = (float) l;
    }

    public ChunkCoordinates aC() {
        return this.bC;
    }

    public float aD() {
        return this.bD;
    }

    public void aE() {
        this.bD = -1.0F;
    }

    public boolean aF() {
        return this.bD != -1.0F;
    }

    public void c(EntityLiving entityliving) {
        this.lastDamager = entityliving;
        this.c = this.lastDamager != null ? 60 : 0;
    }

    protected void a() {
        this.datawatcher.a(8, Integer.valueOf(this.f));
    }

    public boolean l(Entity entity) {
        return this.worldObj.a(Vec3.a().create(this.posX, this.posY + (double) this.getHeadHeight(), this.posZ), Vec3.a().create(entity.posX, entity.posY + (double) entity.getHeadHeight(), entity.posZ)) == null;
    }

    public boolean L() {
        return !this.isDead;
    }

    public boolean M() {
        return !this.isDead;
    }

    public float getHeadHeight() {
        return this.length * 0.85F;
    }

    public int aG() {
        return 80;
    }

    public void aH() {
        String s = this.aQ();

        if (s != null) {
            this.worldObj.makeSound(this, s, this.aP(), this.i());
        }
    }

    public void z() {
        this.aI = this.aJ;
        super.z();
        // this.world.methodProfiler.a("mobBaseTick"); // CraftBukkit - not in production code
        if (this.isEntityAlive() && this.random.nextInt(1000) < this.a++) {
            this.a = -this.aG();
            this.aH();
        }

        // CraftBukkit start
        if (this.isEntityAlive() && this.inBlock() && !(this instanceof EntityDragon)) { // EnderDragon's don't suffocate.
            EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.SUFFOCATION, 1);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                event.getEntity().setLastDamageCause(event);
                this.damageEntity(DamageSource.STUCK, event.getDamage());
            }
            // CraftBukkit end
        }

        if (this.isFireproof() || this.worldObj.isStatic) {
            this.extinguish();
        }

        if (this.isEntityAlive() && this.a(Material.WATER) && !this.aU() && !this.activePotionsMap.containsKey(Integer.valueOf(Potion.WATER_BREATHING.id))) {
            this.setAir(this.h(this.getAir()));
            if (this.getAir() == -20) {
                this.setAir(0);

                for (int i = 0; i < 8; ++i) {
                    float f = this.random.nextFloat() - this.random.nextFloat();
                    float f1 = this.random.nextFloat() - this.random.nextFloat();
                    float f2 = this.random.nextFloat() - this.random.nextFloat();

                    this.worldObj.a("bubble", this.posX + (double) f, this.posY + (double) f1, this.posZ + (double) f2, this.motionX, this.motionY, this.motionZ);
                }

                // CraftBukkit start
                EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.DROWNING, 2);
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled() && event.getDamage() != 0) {
                    event.getEntity().setLastDamageCause(event);
                    this.damageEntity(DamageSource.DROWN, event.getDamage());
                }
                // CraftBukkit end
            }

            this.extinguish();
        } else {
            // CraftBukkit start - only set if needed to work around a datawatcher inefficiency
            if (this.getAir() != 300) {
                this.setAir(maxAirTicks);
            }
            // CraftBukkit end
        }

        this.aS = this.aT;
        if (this.attackTicks > 0) {
            --this.attackTicks;
        }

        if (this.hurtTicks > 0) {
            --this.hurtTicks;
        }

        if (this.hurtResistantTime > 0) {
            --this.hurtResistantTime;
        }

        if (this.health <= 0) {
            this.aI();
        }

        if (this.lastDamageByPlayerTime > 0) {
            --this.lastDamageByPlayerTime;
        } else {
            this.attackingPlayer = null;
        }

        if (this.d != null && !this.d.isEntityAlive()) {
            this.d = null;
        }

        if (this.lastDamager != null) {
            if (!this.lastDamager.isEntityAlive()) {
                this.c((EntityLiving) null);
            } else if (this.c > 0) {
                --this.c;
            } else {
                this.c((EntityLiving) null);
            }
        }

        this.bo();
        this.ax = this.aw;
        this.ar = this.aq;
        this.at = this.as;
        this.lastYaw = this.rotationYaw;
        this.lastPitch = this.rotationPitch;
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
    }

    // CraftBukkit start
    public int getExpReward() {
        int exp = this.getExpValue(this.attackingPlayer);

        if (!this.worldObj.isStatic && (this.lastDamageByPlayerTime > 0 || this.alwaysGivesExp()) && !this.isBaby()) {
            return exp;
        } else {
            return 0;
        }
    }
    // CraftBukkit end

    protected void aI() {
        ++this.deathTicks;
        if (this.deathTicks >= 20 && !this.isDead) { // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead)
            int i;

            // CraftBukkit start - update getExpReward() above if the removed if() changes!
            i = expToDrop;
            while (i > 0) {
                int j = EntityXPOrb.getOrbValue(i);

                i -= j;
                this.worldObj.addEntity(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
            }
            // CraftBukkit end

            this.setDead();

            for (i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.worldObj.a("explode", this.posX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + (double) (this.random.nextFloat() * this.length), this.posZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
            }
        }
    }

    protected int h(int i) {
        return i - 1;
    }

    protected int getExpValue(net.minecraft.src.EntityPlayer entityhuman) {
        return this.aV;
    }

    protected boolean alwaysGivesExp() {
        return false;
    }

    public void aK() {
        for (int i = 0; i < 20; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            double d3 = 10.0D;

            this.worldObj.a("explode", this.posX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d0 * d3, this.posY + (double) (this.random.nextFloat() * this.length) - d1 * d3, this.posZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width - d2 * d3, d0, d1, d2);
        }
    }

    public void U() {
        super.U();
        this.au = this.av;
        this.av = 0.0F;
        this.fallDistance = 0.0F;
    }

    public void h_() {
        super.h_();
        if (this.bd > 0) {
            if (this.be <= 0) {
                this.be = 60;
            }

            --this.be;
            if (this.be <= 0) {
                --this.bd;
            }
        }

        this.d();
        double d0 = this.posX - this.lastX;
        double d1 = this.posZ - this.lastZ;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.aq;
        float f2 = 0.0F;

        this.au = this.av;
        float f3 = 0.0F;

        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt((double) f) * 3.0F;
            // CraftBukkit - Math -> TrigMath
            f1 = (float) org.bukkit.craftbukkit.TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
        }

        if (this.aJ > 0.0F) {
            f1 = this.rotationYaw;
        }

        if (!this.onGround) {
            f3 = 0.0F;
        }

        this.av += (f3 - this.av) * 0.3F;
        // this.world.methodProfiler.a("headTurn"); // CraftBukkit - not in production code
        if (this.aV()) {
            this.senses.a();
        } else {
            float f4 = MathHelper.g(f1 - this.aq);

            this.aq += f4 * 0.3F;
            float f5 = MathHelper.g(this.rotationYaw - this.aq);
            boolean flag = f5 < -90.0F || f5 >= 90.0F;

            if (f5 < -75.0F) {
                f5 = -75.0F;
            }

            if (f5 >= 75.0F) {
                f5 = 75.0F;
            }

            this.aq = this.rotationYaw - f5;
            if (f5 * f5 > 2500.0F) {
                this.aq += f5 * 0.2F;
            }

            if (flag) {
                f2 *= -1.0F;
            }
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("rangeChecks"); // CraftBukkit - not in production code

        while (this.rotationYaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.rotationYaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        while (this.aq - this.ar < -180.0F) {
            this.ar -= 360.0F;
        }

        while (this.aq - this.ar >= 180.0F) {
            this.ar += 360.0F;
        }

        while (this.rotationPitch - this.lastPitch < -180.0F) {
            this.lastPitch -= 360.0F;
        }

        while (this.rotationPitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.as - this.at < -180.0F) {
            this.at -= 360.0F;
        }

        while (this.as - this.at >= 180.0F) {
            this.at += 360.0F;
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        this.aw += f2;
    }

    // CraftBukkit start - delegate so we can handle providing a reason for health being regained
    public void heal(int i) {
        heal(i, EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    public void heal(int i, EntityRegainHealthEvent.RegainReason regainReason) {
        if (this.health > 0) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), i, regainReason);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.health += event.getAmount();
            }
            // CraftBukkit end

            if (this.health > this.getMaxHealth()) {
                this.health = this.getMaxHealth();
            }

            this.hurtResistantTime = this.maxHurtResistantTime / 2;
        }
    }

    public abstract int getMaxHealth();

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int i) {
        this.health = i;
        if (i > this.getMaxHealth()) {
            i = this.getMaxHealth();
        }
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.worldObj.isStatic) {
            return false;
        } else {
            this.bq = 0;
            if (this.health <= 0) {
                return false;
            } else if (damagesource.k() && this.hasEffect(Potion.FIRE_RESISTANCE)) {
                return false;
            } else {
                this.aZ = 1.5F;
                boolean flag = true;

                // CraftBukkit start
                if (damagesource instanceof EntityDamageSource) {
                    EntityDamageEvent event = CraftEventFactory.handleEntityDamageEvent(this, damagesource, i);
                    if (event.isCancelled()) {
                        return false;
                    }
                    i = event.getDamage();
                }
                // CraftBukkit end

                if ((float) this.hurtResistantTime > (float) this.maxHurtResistantTime / 2.0F) {
                    if (i <= this.lastDamage) {
                        return false;
                    }

                    this.d(damagesource, i - this.lastDamage);
                    this.lastDamage = i;
                    flag = false;
                } else {
                    this.lastDamage = i;
                    this.aL = this.health;
                    this.hurtResistantTime = this.maxHurtResistantTime;
                    this.d(damagesource, i);
                    this.hurtTicks = this.aO = 10;
                }

                this.aP = 0.0F;
                Entity entity = damagesource.getEntity();

                if (entity != null) {
                    if (entity instanceof EntityLiving) {
                        this.c((EntityLiving) entity);
                    }

                    if (entity instanceof net.minecraft.src.EntityPlayer) {
                        this.lastDamageByPlayerTime = 60;
                        this.attackingPlayer = (net.minecraft.src.EntityPlayer) entity;
                    } else if (entity instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf) entity;

                        if (entitywolf.isTamed()) {
                            this.lastDamageByPlayerTime = 60;
                            this.attackingPlayer = null;
                        }
                    }
                }

                if (flag) {
                    this.worldObj.setEntityState(this, (byte) 2);
                    if (damagesource != DamageSource.DROWN && damagesource != DamageSource.EXPLOSION2) {
                        this.K();
                    }

                    if (entity != null) {
                        double d0 = entity.posX - this.posX;

                        double d1;

                        for (d1 = entity.posZ - this.posZ; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.aP = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - this.rotationYaw;
                        this.a(entity, i, d0, d1);
                    } else {
                        this.aP = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (this.health <= 0) {
                    if (flag) {
                        this.worldObj.makeSound(this, this.aS(), this.aP(), this.i());
                    }

                    this.die(damagesource);
                } else if (flag) {
                    this.worldObj.makeSound(this, this.aR(), this.aP(), this.i());
                }

                return true;
            }
        }
    }

    private float i() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
    }

    public int aO() {
        return 0;
    }

    protected void k(int i) {}

    protected int b(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor()) {
            int j = 25 - this.aO();
            int k = i * j + this.aM;

            this.k(i);
            i = k / 25;
            this.aM = k % 25;
        }

        return i;
    }

    protected int c(DamageSource damagesource, int i) {
        if (this.hasEffect(Potion.RESISTANCE)) {
            int j = (this.getEffect(Potion.RESISTANCE).getAmplifier() + 1) * 5;
            int k = 25 - j;
            int l = i * k + this.aM;

            i = l / 25;
            this.aM = l % 25;
        }

        return i;
    }

    protected void d(DamageSource damagesource, int i) {
        i = this.b(damagesource, i);
        i = this.c(damagesource, i);
        this.health -= i;
    }

    protected float aP() {
        return 1.0F;
    }

    protected String aQ() {
        return null;
    }

    protected String aR() {
        return "damage.hurtflesh";
    }

    protected String aS() {
        return "damage.hurtflesh";
    }

    public void a(Entity entity, int i, double d0, double d1) {
        this.al = true;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f1 = 0.4F;

        this.motionX /= 2.0D;
        this.motionY /= 2.0D;
        this.motionZ /= 2.0D;
        this.motionX -= d0 / (double) f * (double) f1;
        this.motionY += (double) f1;
        this.motionZ -= d1 / (double) f * (double) f1;
        if (this.motionY > 0.4000000059604645D) {
            this.motionY = 0.4000000059604645D;
        }
    }

    public void die(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();

        if (this.aE >= 0 && entity != null) {
            entity.c(this, this.aE);
        }

        if (entity != null) {
            entity.a(this);
        }

        this.aU = true;
        if (!this.worldObj.isStatic) {
            int i = 0;

            if (entity instanceof net.minecraft.src.EntityPlayer) {
                i = EnchantmentManager.getBonusMonsterLootEnchantmentLevel(((net.minecraft.src.EntityPlayer) entity).inventory);
            }

            if (!this.isBaby()) {
                this.dropDeathLoot(this.lastDamageByPlayerTime > 0, i);
                if (false && this.lastDamageByPlayerTime > 0) { // CraftBukkit - move rare item drop call to dropDeathLoot
                    int j = this.random.nextInt(200) - i;

                    if (j < 5) {
                        this.l(j <= 0 ? 1 : 0);
                    }
                }
            } else { // CraftBukkit
                CraftEventFactory.callEntityDeathEvent(this); // CraftBukkit
            }
        }

        this.worldObj.setEntityState(this, (byte) 3);
    }

    // CraftBukkit start - change return type to ItemStack
    protected net.minecraft.src.ItemStack l(int i) {
        return null;
    }
    // CraftBukkit end

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start - whole method
        List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.getLootId();

        if (j > 0) {
            int k = this.random.nextInt(3);

            if (i > 0) {
                k += this.random.nextInt(i + 1);
            }

            if (k > 0) {
                loot.add(new org.bukkit.inventory.ItemStack(j, k));
            }
        }

        // Determine rare item drops and add them to the loot
        if (this.lastDamageByPlayerTime > 0) {
            int k = this.random.nextInt(200) - i;

            if (k < 5) {
                net.minecraft.src.ItemStack itemstack = this.l(k <= 0 ? 1 : 0);
                if (itemstack != null) {
                    loot.add(new org.bukkit.craftbukkit.inventory.CraftItemStack(itemstack));
                }
            }
        }

        CraftEventFactory.callEntityDeathEvent(this, loot); // raise event even for those times when the entity does not drop loot
        // CraftBukkit end
    }

    protected int getLootId() {
        return 0;
    }

    protected void a(float f) {
        super.a(f);
        int i = MathHelper.f(f - 3.0F);

        if (i > 0) {
            // CraftBukkit start
            EntityDamageEvent event = new EntityDamageEvent(this.getBukkitEntity(), EntityDamageEvent.DamageCause.FALL, i);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled() && event.getDamage() != 0) {
                i = event.getDamage();

                if (i > 4) {
                    this.worldObj.makeSound(this, "damage.fallbig", 1.0F, 1.0F);
                } else {
                    this.worldObj.makeSound(this, "damage.fallsmall", 1.0F, 1.0F);
                }

                this.getBukkitEntity().setLastDamageCause(event);
                this.damageEntity(DamageSource.FALL, i);
            }
            // CraftBukkit end

            int j = this.worldObj.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.posY - 0.20000000298023224D - (double) this.height), MathHelper.floor(this.posZ));

            if (j > 0) {
                StepSound stepsound = Block.blocksList[j].stepSound;

                this.worldObj.makeSound(this, stepsound.getName(), stepsound.getVolume1() * 0.5F, stepsound.getVolume2() * 0.75F);
            }
        }
    }

    public void e(float f, float f1) {
        double d0;

        if (this.H() && (!(this instanceof net.minecraft.src.EntityPlayer) || !((net.minecraft.src.EntityPlayer) this).capabilities.isFlying)) {
            d0 = this.posY;
            this.a(f, f1, this.aV() ? 0.04F : 0.02F);
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
            this.motionY -= 0.02D;
            if (this.positionChanged && this.c(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ)) {
                this.motionY = 0.30000001192092896D;
            }
        } else if (this.J() && (!(this instanceof net.minecraft.src.EntityPlayer) || !((net.minecraft.src.EntityPlayer) this).capabilities.isFlying)) {
            d0 = this.posY;
            this.a(f, f1, 0.02F);
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
            this.motionY -= 0.02D;
            if (this.positionChanged && this.c(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ)) {
                this.motionY = 0.30000001192092896D;
            }
        } else {
            float f2 = 0.91F;

            if (this.onGround) {
                f2 = 0.54600006F;
                int i = this.worldObj.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.posZ));

                if (i > 0) {
                    f2 = Block.blocksList[i].frictionFactor * 0.91F;
                }
            }

            float f3 = 0.16277136F / (f2 * f2 * f2);
            float f4;

            if (this.onGround) {
                if (this.aV()) {
                    f4 = this.ay();
                } else {
                    f4 = this.aG;
                }

                f4 *= f3;
            } else {
                f4 = this.aH;
            }

            this.a(f, f1, f4);
            f2 = 0.91F;
            if (this.onGround) {
                f2 = 0.54600006F;
                int j = this.worldObj.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.posZ));

                if (j > 0) {
                    f2 = Block.blocksList[j].frictionFactor * 0.91F;
                }
            }

            if (this.f_()) {
                float f5 = 0.15F;

                if (this.motionX < (double) (-f5)) {
                    this.motionX = (double) (-f5);
                }

                if (this.motionX > (double) f5) {
                    this.motionX = (double) f5;
                }

                if (this.motionZ < (double) (-f5)) {
                    this.motionZ = (double) (-f5);
                }

                if (this.motionZ > (double) f5) {
                    this.motionZ = (double) f5;
                }

                this.fallDistance = 0.0F;
                if (this.motionY < -0.15D) {
                    this.motionY = -0.15D;
                }

                boolean flag = this.isSneaking() && this instanceof net.minecraft.src.EntityPlayer;

                if (flag && this.motionY < 0.0D) {
                    this.motionY = 0.0D;
                }
            }

            this.move(this.motionX, this.motionY, this.motionZ);
            if (this.positionChanged && this.f_()) {
                this.motionY = 0.2D;
            }

            this.motionY -= 0.08D;
            this.motionY *= 0.9800000190734863D;
            this.motionX *= (double) f2;
            this.motionZ *= (double) f2;
        }

        this.aY = this.aZ;
        d0 = this.posX - this.lastX;
        double d1 = this.posZ - this.lastZ;
        float f6 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f6 > 1.0F) {
            f6 = 1.0F;
        }

        this.aZ += (f6 - this.aZ) * 0.4F;
        this.ba += this.aZ;
    }

    public boolean f_() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.posZ);
        int l = this.worldObj.getBlockId(i, j, k);

        return l == Block.LADDER.blockID || l == Block.VINE.blockID;
    }

    public void b(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) this.health);
        nbttagcompound.setShort("HurtTime", (short) this.hurtTicks);
        nbttagcompound.setShort("DeathTime", (short) this.deathTicks);
        nbttagcompound.setShort("AttackTime", (short) this.attackTicks);
        if (!this.activePotionsMap.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.activePotionsMap.values().iterator();

            while (iterator.hasNext()) {
                MobEffect mobeffect = (MobEffect) iterator.next();
                net.minecraft.src.NBTTagCompound nbttagcompound1 = new net.minecraft.src.NBTTagCompound();

                nbttagcompound1.setByte("Id", (byte) mobeffect.getEffectId());
                nbttagcompound1.setByte("Amplifier", (byte) mobeffect.getAmplifier());
                nbttagcompound1.setInteger("Duration", mobeffect.getDuration());
                nbttaglist.add(nbttagcompound1);
            }

            nbttagcompound.set("ActiveEffects", nbttaglist);
        }
    }

    public void a(net.minecraft.src.NBTTagCompound nbttagcompound) {
        if (this.health < -32768) {
            this.health = -32768;
        }

        this.health = nbttagcompound.getShort("Health");
        if (!nbttagcompound.hasKey("Health")) {
            this.health = this.getMaxHealth();
        }

        this.hurtTicks = nbttagcompound.getShort("HurtTime");
        this.deathTicks = nbttagcompound.getShort("DeathTime");
        this.attackTicks = nbttagcompound.getShort("AttackTime");
        if (nbttagcompound.hasKey("ActiveEffects")) {
            NBTTagList nbttaglist = nbttagcompound.getList("ActiveEffects");

            for (int i = 0; i < nbttaglist.size(); ++i) {
                net.minecraft.src.NBTTagCompound nbttagcompound1 = (net.minecraft.src.NBTTagCompound) nbttaglist.get(i);
                byte b0 = nbttagcompound1.getByte("Id");
                byte b1 = nbttagcompound1.getByte("Amplifier");
                int j = nbttagcompound1.getInteger("Duration");

                this.activePotionsMap.put(Integer.valueOf(b0), new MobEffect(b0, j, b1));
            }
        }
    }

    public boolean isEntityAlive() {
        return !this.isDead && this.health > 0;
    }

    public boolean aU() {
        return false;
    }

    public void f(float f) {
        this.bs = f;
    }

    public void d(boolean flag) {
        this.bu = flag;
    }

    public void d() {
        if (this.bE > 0) {
            --this.bE;
        }

        if (this.bi > 0) {
            double d0 = this.posX + (this.bj - this.posX) / (double) this.bi;
            double d1 = this.posY + (this.bk - this.posY) / (double) this.bi;
            double d2 = this.posZ + (this.bl - this.posZ) / (double) this.bi;
            double d3 = MathHelper.g(this.bm - (double) this.rotationYaw);

            this.rotationYaw = (float) ((double) this.rotationYaw + d3 / (double) this.bi);
            this.rotationPitch = (float) ((double) this.rotationPitch + (this.bn - (double) this.rotationPitch) / (double) this.bi);
            --this.bi;
            this.setPosition(d0, d1, d2);
            this.b(this.rotationYaw, this.rotationPitch);
        }

        if (Math.abs(this.motionX) < 0.005D) {
            this.motionX = 0.0D;
        }

        if (Math.abs(this.motionY) < 0.005D) {
            this.motionY = 0.0D;
        }

        if (Math.abs(this.motionZ) < 0.005D) {
            this.motionZ = 0.0D;
        }

        // this.world.methodProfiler.a("ai"); // CraftBukkit - not in production code
        if (this.aX()) {
            this.bu = false;
            this.br = 0.0F;
            this.bs = 0.0F;
            this.bt = 0.0F;
        } else if (this.aW()) {
            if (this.aV()) {
                // this.world.methodProfiler.a("newAi"); // CraftBukkit - not in production code
                this.bc();
                // this.world.methodProfiler.b(); // CraftBukkit - not in production code
            } else {
                // this.world.methodProfiler.a("oldAi"); // CraftBukkit - not in production code
                this.be();
                // this.world.methodProfiler.b(); // CraftBukkit - not in production code
                this.as = this.rotationYaw;
            }
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("jump"); // CraftBukkit - not in production code
        if (this.bu) {
            if (!this.H() && !this.J()) {
                if (this.onGround && this.bE == 0) {
                    this.aZ();
                    this.bE = 10;
                }
            } else {
                this.motionY += 0.03999999910593033D;
            }
        } else {
            this.bE = 0;
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("travel"); // CraftBukkit - not in production code
        this.br *= 0.98F;
        this.bs *= 0.98F;
        this.bt *= 0.9F;
        float f = this.aG;

        this.aG *= this.bs();
        this.e(this.br, this.bs);
        this.aG = f;
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("push"); // CraftBukkit - not in production code
        if (!this.worldObj.isStatic) {
            List list = this.worldObj.getEntities(this, this.boundingBox.grow(0.20000000298023224D, 0.0D, 0.20000000298023224D));

            if (list != null && !list.isEmpty()) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    if (entity.M()) {
                        entity.collide(this);
                    }
                }
            }
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
    }

    protected boolean aV() {
        return false;
    }

    protected boolean aW() {
        return !this.worldObj.isStatic;
    }

    protected boolean aX() {
        return this.health <= 0;
    }

    public boolean isBlocking() {
        return false;
    }

    protected void aZ() {
        this.motionY = 0.41999998688697815D;
        if (this.hasEffect(Potion.JUMP)) {
            this.motionY += (double) ((float) (this.getEffect(Potion.JUMP).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting()) {
            float f = this.rotationYaw * 0.017453292F;

            this.motionX -= (double) (MathHelper.sin(f) * 0.2F);
            this.motionZ += (double) (MathHelper.cos(f) * 0.2F);
        }

        this.al = true;
    }

    protected boolean ba() {
        return true;
    }

    protected void bb() {
        net.minecraft.src.EntityPlayer entityhuman = this.worldObj.findNearbyPlayer(this, -1.0D);

        if (entityhuman != null) {
            double d0 = entityhuman.posX - this.posX;
            double d1 = entityhuman.posY - this.posY;
            double d2 = entityhuman.posZ - this.posZ;
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;

            if (this.ba() && d3 > 16384.0D) {
                this.setDead();
            }

            if (this.bq > 600 && this.random.nextInt(800) == 0 && d3 > 1024.0D && this.ba()) {
                this.setDead();
            } else if (d3 < 1024.0D) {
                this.bq = 0;
            }
        }
    }

    protected void bc() {
        ++this.bq;
        // this.world.methodProfiler.a("checkDespawn"); // CraftBukkit - not in production code
        this.bb();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("sensing"); // CraftBukkit - not in production code
        this.bA.a();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("targetSelector"); // CraftBukkit - not in production code
        this.targetSelector.a();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("goalSelector"); // CraftBukkit - not in production code
        this.goalSelector.a();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("navigation"); // CraftBukkit - not in production code
        this.navigation.e();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("mob tick"); // CraftBukkit - not in production code
        this.bd();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("controls"); // CraftBukkit - not in production code
        // this.world.methodProfiler.a("move"); // CraftBukkit - not in production code
        this.moveController.c();
        // this.world.methodProfiler.c("look"); // CraftBukkit - not in production code
        this.lookController.a();
        // this.world.methodProfiler.c("jump"); // CraftBukkit - not in production code
        this.jumpController.b();
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
    }

    protected void bd() {}

    protected void be() {
        ++this.bq;
        this.bb();
        this.br = 0.0F;
        this.bs = 0.0F;
        float f = 8.0F;

        if (this.random.nextFloat() < 0.02F) {
            net.minecraft.src.EntityPlayer entityhuman = this.worldObj.findNearbyPlayer(this, (double) f);

            if (entityhuman != null) {
                this.bF = entityhuman;
                this.bx = 10 + this.random.nextInt(20);
            } else {
                this.bt = (this.random.nextFloat() - 0.5F) * 20.0F;
            }
        }

        if (this.bF != null) {
            this.a(this.bF, 10.0F, (float) this.bf());
            if (this.bx-- <= 0 || this.bF.isDead || this.bF.e((Entity) this) > (double) (f * f)) {
                this.bF = null;
            }
        } else {
            if (this.random.nextFloat() < 0.05F) {
                this.bt = (this.random.nextFloat() - 0.5F) * 20.0F;
            }

            this.rotationYaw += this.bt;
            this.rotationPitch = this.bv;
        }

        boolean flag = this.H();
        boolean flag1 = this.J();

        if (flag || flag1) {
            this.bu = this.random.nextFloat() < 0.8F;
        }
    }

    public int bf() {
        return 40;
    }

    public void a(Entity entity, float f, float f1) {
        double d0 = entity.posX - this.posX;
        double d1 = entity.posZ - this.posZ;
        double d2;

        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;

            d2 = this.posY + (double) this.getHeadHeight() - (entityliving.posY + (double) entityliving.getHeadHeight());
        } else {
            d2 = (entity.boundingBox.b + entity.boundingBox.e) / 2.0D - (this.posY + (double) this.getHeadHeight());
        }

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1);
        float f2 = (float) (Math.atan2(d1, d0) * 180.0D / 3.1415927410125732D) - 90.0F;
        float f3 = (float) (-(Math.atan2(d2, d3) * 180.0D / 3.1415927410125732D));

        this.rotationPitch = -this.b(this.rotationPitch, f3, f1);
        this.rotationYaw = this.b(this.rotationYaw, f2, f);
    }

    private float b(float f, float f1, float f2) {
        float f3 = MathHelper.g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public boolean canSpawn() {
        return this.worldObj.b(this.boundingBox) && this.worldObj.getCubes(this, this.boundingBox).isEmpty() && !this.worldObj.containsLiquid(this.boundingBox);
    }

    protected void C() {
        // CraftBukkit start
        EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(null, this.getBukkitEntity(), EntityDamageEvent.DamageCause.VOID, 4);
        this.worldObj.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled() || event.getDamage() == 0) {
            return;
        }

        event.getEntity().setLastDamageCause(event);
        this.damageEntity(DamageSource.OUT_OF_WORLD, event.getDamage());
        // CraftBukkit end
    }

    public Vec3 Z() {
        return this.i(1.0F);
    }

    public Vec3 i(float f) {
        float f1;
        float f2;
        float f3;
        float f4;

        if (f == 1.0F) {
            f1 = MathHelper.cos(-this.rotationYaw * 0.017453292F - 3.1415927F);
            f2 = MathHelper.sin(-this.rotationYaw * 0.017453292F - 3.1415927F);
            f3 = -MathHelper.cos(-this.rotationPitch * 0.017453292F);
            f4 = MathHelper.sin(-this.rotationPitch * 0.017453292F);
            return Vec3.a().create((double) (f2 * f3), (double) f4, (double) (f1 * f3));
        } else {
            f1 = this.lastPitch + (this.rotationPitch - this.lastPitch) * f;
            f2 = this.lastYaw + (this.rotationYaw - this.lastYaw) * f;
            f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
            f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);

            return Vec3.a().create((double) (f4 * f5), (double) f6, (double) (f3 * f5));
        }
    }

    public int bl() {
        return 4;
    }

    public boolean isSleeping() {
        return false;
    }

    protected void bo() {
        Iterator iterator = this.activePotionsMap.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.activePotionsMap.get(integer);

            if (!mobeffect.tick(this) && !this.worldObj.isStatic) {
                iterator.remove();
                this.c(mobeffect);
            }
        }

        int i;

        if (this.potionsNeedUpdate) {
            if (!this.worldObj.isStatic) {
                if (this.activePotionsMap.isEmpty()) {
                    this.datawatcher.watch(8, Integer.valueOf(0));
                } else {
                    i = PotionBrewer.a(this.activePotionsMap.values());
                    this.datawatcher.watch(8, Integer.valueOf(i));
                }
            }

            this.potionsNeedUpdate = false;
        }

        if (this.random.nextBoolean()) {
            i = this.datawatcher.getInt(8);
            if (i > 0) {
                double d0 = (double) (i >> 16 & 255) / 255.0D;
                double d1 = (double) (i >> 8 & 255) / 255.0D;
                double d2 = (double) (i >> 0 & 255) / 255.0D;

                this.worldObj.a("mobSpell", this.posX + (this.random.nextDouble() - 0.5D) * (double) this.width, this.posY + this.random.nextDouble() * (double) this.length - (double) this.height, this.posZ + (this.random.nextDouble() - 0.5D) * (double) this.width, d0, d1, d2);
            }
        }
    }

    public void bp() {
        Iterator iterator = this.activePotionsMap.keySet().iterator();

        while (iterator.hasNext()) {
            Integer integer = (Integer) iterator.next();
            MobEffect mobeffect = (MobEffect) this.activePotionsMap.get(integer);

            if (!this.worldObj.isStatic) {
                iterator.remove();
                this.c(mobeffect);
            }
        }
    }

    public Collection getEffects() {
        return this.activePotionsMap.values();
    }

    public boolean hasEffect(Potion mobeffectlist) {
        return this.activePotionsMap.containsKey(Integer.valueOf(mobeffectlist.id));
    }

    public MobEffect getEffect(Potion mobeffectlist) {
        return (MobEffect) this.activePotionsMap.get(Integer.valueOf(mobeffectlist.id));
    }

    public void addPotionEffect(MobEffect mobeffect) {
        if (this.e(mobeffect)) {
            if (this.activePotionsMap.containsKey(Integer.valueOf(mobeffect.getEffectId()))) {
                ((MobEffect) this.activePotionsMap.get(Integer.valueOf(mobeffect.getEffectId()))).a(mobeffect);
                this.b((MobEffect) this.activePotionsMap.get(Integer.valueOf(mobeffect.getEffectId())));
            } else {
                this.activePotionsMap.put(Integer.valueOf(mobeffect.getEffectId()), mobeffect);
                this.a(mobeffect);
            }
        }
    }

    public boolean e(MobEffect mobeffect) {
        if (this.getMonsterType() == EnumMonsterType.UNDEAD) {
            int i = mobeffect.getEffectId();

            if (i == Potion.REGENERATION.id || i == Potion.POISON.id) {
                return false;
            }
        }

        return true;
    }

    public boolean br() {
        return this.getMonsterType() == EnumMonsterType.UNDEAD;
    }

    protected void a(MobEffect mobeffect) {
        this.potionsNeedUpdate = true;
    }

    protected void b(MobEffect mobeffect) {
        this.potionsNeedUpdate = true;
    }

    protected void c(MobEffect mobeffect) {
        this.potionsNeedUpdate = true;
    }

    protected float bs() {
        float f = 1.0F;

        if (this.hasEffect(Potion.FASTER_MOVEMENT)) {
            f *= 1.0F + 0.2F * (float) (this.getEffect(Potion.FASTER_MOVEMENT).getAmplifier() + 1);
        }

        if (this.hasEffect(Potion.SLOWER_MOVEMENT)) {
            f *= 1.0F - 0.15F * (float) (this.getEffect(Potion.SLOWER_MOVEMENT).getAmplifier() + 1);
        }

        return f;
    }

    public void enderTeleportTo(double d0, double d1, double d2) {
        this.setPositionRotation(d0, d1, d2, this.rotationYaw, this.rotationPitch);
    }

    public boolean isBaby() {
        return false;
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEFINED;
    }

    public void a(ItemStack itemstack) {
        this.worldObj.makeSound(this, "random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);

        for (int i = 0; i < 5; ++i) {
            Vec3 vec3d = Vec3.a().create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

            vec3d.a(-this.rotationPitch * 3.1415927F / 180.0F);
            vec3d.b(-this.rotationYaw * 3.1415927F / 180.0F);
            Vec3 vec3d1 = Vec3.a().create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

            vec3d1.a(-this.rotationPitch * 3.1415927F / 180.0F);
            vec3d1.b(-this.rotationYaw * 3.1415927F / 180.0F);
            vec3d1 = vec3d1.add(this.posX, this.posY + (double) this.getHeadHeight(), this.posZ);
            this.worldObj.a("iconcrack_" + itemstack.getItem().id, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c);
        }
    }
}
