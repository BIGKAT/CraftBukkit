package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTeleportEvent;
// CraftBukkit end

public class EntityEnderman extends EntityMonster {

    private static boolean[] d = new boolean[256];
    private int e = 0;
    private int g = 0;

    public EntityEnderman(World world) {
        super(world);
        this.texture = "/mob/enderman.png";
        this.bw = 0.2F;
        this.damage = 7;
        this.a(0.6F, 2.9F);
        this.W = 1.0F;
    }

    public int getMaxHealth() {
        return 40;
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 0));
        this.datawatcher.a(17, new Byte((byte) 0));
        this.datawatcher.a(18, new Byte((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setShort("carried", (short) this.getCarried());
        nbttagcompound.setShort("carriedData", (short) this.getCarryingData());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setCarried(nbttagcompound.getShort("carried"));
        this.setCarryingData(nbttagcompound.getShort("carriedData"));
    }

    protected Entity findTarget() {
        EntityHuman entityhuman = this.worldObj.findNearbyVulnerablePlayer(this, 64.0D);

        if (entityhuman != null) {
            if (this.d(entityhuman)) {
                if (this.g++ == 5) {
                    this.g = 0;
                    this.e(true);
                    return entityhuman;
                }
            } else {
                this.g = 0;
            }
        }

        return null;
    }

    private boolean d(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.armor[3];

        if (itemstack != null && itemstack.id == Block.PUMPKIN.blockID) {
            return false;
        } else {
            Vec3D vec3d = entityhuman.i(1.0F).b();
            Vec3D vec3d1 = Vec3D.a().create(this.posX - entityhuman.posX, this.boundingBox.b + (double) (this.length / 2.0F) - (entityhuman.posY + (double) entityhuman.getHeadHeight()), this.posZ - entityhuman.posZ);
            double d0 = vec3d1.c();

            vec3d1 = vec3d1.b();
            double d1 = vec3d.b(vec3d1);

            return d1 > 1.0D - 0.025D / d0 ? entityhuman.l(this) : false;
        }
    }

    public void d() {
        if (this.G()) {
            this.damageEntity(DamageSource.DROWN, 1);
        }

        this.bw = this.entityToAttack != null ? 6.5F : 0.3F;
        int i;

        if (!this.worldObj.isStatic) {
            int j;
            int k;
            int l;

            if (this.getCarried() == 0) {
                if (this.random.nextInt(20) == 0) {
                    i = MathHelper.floor(this.posX - 2.0D + this.random.nextDouble() * 4.0D);
                    j = MathHelper.floor(this.posY + this.random.nextDouble() * 3.0D);
                    k = MathHelper.floor(this.posZ - 2.0D + this.random.nextDouble() * 4.0D);
                    l = this.worldObj.getBlockId(i, j, k);
                    if (d[l]) {
                        // CraftBukkit start - pickup event
                        if (!CraftEventFactory.callEntityChangeBlockEvent(this, this.worldObj.getWorld().getBlockAt(i, j, k), org.bukkit.Material.AIR).isCancelled()) {
                            this.setCarried(this.worldObj.getBlockId(i, j, k));
                            this.setCarryingData(this.worldObj.getData(i, j, k));
                            this.worldObj.setBlockWithNotify(i, j, k, 0);
                        }
                        // CraftBukkit end
                    }
                }
            } else if (this.random.nextInt(2000) == 0) {
                i = MathHelper.floor(this.posX - 1.0D + this.random.nextDouble() * 2.0D);
                j = MathHelper.floor(this.posY + this.random.nextDouble() * 2.0D);
                k = MathHelper.floor(this.posZ - 1.0D + this.random.nextDouble() * 2.0D);
                l = this.worldObj.getBlockId(i, j, k);
                int i1 = this.worldObj.getBlockId(i, j - 1, k);

                if (l == 0 && i1 > 0 && Block.blocksList[i1].c()) {
                    // CraftBukkit start - place event
                    org.bukkit.block.Block bblock = this.worldObj.getWorld().getBlockAt(i, j, k);

                    if (!CraftEventFactory.callEntityChangeBlockEvent(this, bblock, bblock.getType()).isCancelled()) {
                        this.worldObj.setBlockAndMetadataWithNotify(i, j, k, this.getCarried(), this.getCarryingData());
                        this.setCarried(0);
                    }
                    // CraftBukkit end
                }
            }
        }

        for (i = 0; i < 2; ++i) {
            this.worldObj.a("portal", this.posX + (this.random.nextDouble() - 0.5D) * (double) this.width, this.posY + this.random.nextDouble() * (double) this.length - 0.25D, this.posZ + (this.random.nextDouble() - 0.5D) * (double) this.width, (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
        }

        if (this.worldObj.s() && !this.worldObj.isStatic) {
            float f = this.c(1.0F);

            if (f > 0.5F && this.worldObj.j(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
                this.entityToAttack = null;
                this.e(false);
                this.n();
            }
        }

        if (this.G()) {
            this.entityToAttack = null;
            this.e(false);
            this.n();
        }

        this.bu = false;
        if (this.entityToAttack != null) {
            this.a(this.entityToAttack, 100.0F, 100.0F);
        }

        if (!this.worldObj.isStatic && this.isEntityAlive()) {
            if (this.entityToAttack != null) {
                if (this.entityToAttack instanceof EntityHuman && this.d((EntityHuman) this.entityToAttack)) {
                    this.br = this.bs = 0.0F;
                    this.bw = 0.0F;
                    if (this.entityToAttack.e((Entity) this) < 16.0D) {
                        this.n();
                    }

                    this.e = 0;
                } else if (this.entityToAttack.e((Entity) this) > 256.0D && this.e++ >= 30 && this.c(this.entityToAttack)) {
                    this.e = 0;
                }
            } else {
                this.e(false);
                this.e = 0;
            }
        }

        super.d();
    }

    protected boolean n() {
        double d0 = this.posX + (this.random.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double) (this.random.nextInt(64) - 32);
        double d2 = this.posZ + (this.random.nextDouble() - 0.5D) * 64.0D;

        return this.j(d0, d1, d2);
    }

    protected boolean c(Entity entity) {
        Vec3D vec3d = Vec3D.a().create(this.posX - entity.posX, this.boundingBox.b + (double) (this.length / 2.0F) - entity.posY + (double) entity.getHeadHeight(), this.posZ - entity.posZ);

        vec3d = vec3d.b();
        double d0 = 16.0D;
        double d1 = this.posX + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.a * d0;
        double d2 = this.posY + (double) (this.random.nextInt(16) - 8) - vec3d.b * d0;
        double d3 = this.posZ + (this.random.nextDouble() - 0.5D) * 8.0D - vec3d.c * d0;

        return this.j(d1, d2, d3);
    }

    protected boolean j(double d0, double d1, double d2) {
        double d3 = this.posX;
        double d4 = this.posY;
        double d5 = this.posZ;

        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        boolean flag = false;
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.posY);
        int k = MathHelper.floor(this.posZ);
        int l;

        if (this.worldObj.isLoaded(i, j, k)) {
            boolean flag1 = false;

            while (!flag1 && j > 0) {
                l = this.worldObj.getBlockId(i, j - 1, k);
                if (l != 0 && Block.blocksList[l].blockMaterial.isSolid()) {
                    flag1 = true;
                } else {
                    --this.posY;
                    --j;
                }
            }

            if (flag1) {
                // CraftBukkit start - teleport event
                EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), new Location(this.worldObj.getWorld(), d3, d4, d5), new Location(this.worldObj.getWorld(), this.posX, this.posY, this.posZ));
                this.worldObj.getServer().getPluginManager().callEvent(teleport);
                if (teleport.isCancelled()) {
                    return false;
                }

                Location to = teleport.getTo();
                this.setPosition(to.getX(), to.getY(), to.getZ());
                // CraftBukkit end

                if (this.worldObj.getCubes(this, this.boundingBox).isEmpty() && !this.worldObj.containsLiquid(this.boundingBox)) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPosition(d3, d4, d5);
            return false;
        } else {
            short short1 = 128;

            for (l = 0; l < short1; ++l) {
                double d6 = (double) l / ((double) short1 - 1.0D);
                float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (this.posX - d3) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;
                double d8 = d4 + (this.posY - d4) * d6 + this.random.nextDouble() * (double) this.length;
                double d9 = d5 + (this.posZ - d5) * d6 + (this.random.nextDouble() - 0.5D) * (double) this.width * 2.0D;

                this.worldObj.a("portal", d7, d8, d9, (double) f, (double) f1, (double) f2);
            }

            this.worldObj.makeSound(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            this.worldObj.makeSound(this, "mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    protected String aQ() {
        return "mob.endermen.idle";
    }

    protected String aR() {
        return "mob.endermen.hit";
    }

    protected String aS() {
        return "mob.endermen.death";
    }

    protected int getLootId() {
        return Item.ENDER_PEARL.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        int j = this.getLootId();

        if (j > 0) {
            // CraftBukkit start - whole method
            java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
            int count = this.random.nextInt(2 + i);

            if ((j > 0) && (count > 0)) {
                loot.add(new org.bukkit.inventory.ItemStack(j, count));
            }

            CraftEventFactory.callEntityDeathEvent(this, loot);
            // CraftBukkit end
        }
    }

    public void setCarried(int i) {
        this.datawatcher.watch(16, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarried() {
        return this.datawatcher.getByte(16);
    }

    public void setCarryingData(int i) {
        this.datawatcher.watch(17, Byte.valueOf((byte) (i & 255)));
    }

    public int getCarryingData() {
        return this.datawatcher.getByte(17);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (damagesource instanceof EntityDamageSourceIndirect) {
            for (int j = 0; j < 64; ++j) {
                if (this.n()) {
                    return true;
                }
            }

            return false;
        } else {
            if (damagesource.getEntity() instanceof EntityHuman) {
                this.e(true);
            }

            return super.damageEntity(damagesource, i);
        }
    }

    public void e(boolean flag) {
        this.datawatcher.watch(18, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    static {
        d[Block.GRASS.blockID] = true;
        d[Block.DIRT.blockID] = true;
        d[Block.SAND.blockID] = true;
        d[Block.GRAVEL.blockID] = true;
        d[Block.YELLOW_FLOWER.blockID] = true;
        d[Block.RED_ROSE.blockID] = true;
        d[Block.BROWN_MUSHROOM.blockID] = true;
        d[Block.RED_MUSHROOM.blockID] = true;
        d[Block.TNT.blockID] = true;
        d[Block.CACTUS.blockID] = true;
        d[Block.CLAY.blockID] = true;
        d[Block.PUMPKIN.blockID] = true;
        d[Block.MELON.blockID] = true;
        d[Block.MYCEL.blockID] = true;
    }
}
