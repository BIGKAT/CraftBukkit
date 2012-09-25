package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// CraftBukkit start

import org.bukkit.entity.Painting;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.event.painting.PaintingBreakEvent;
// CraftBukkit end

public class EntityPainting extends Entity {

    private int f;
    public int direction;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public EnumArt art;

    public EntityPainting(net.minecraft.src.World world) {
        super(world);
        this.f = 0;
        this.direction = 0;
        this.yOffset = 0.0F;
        this.setSize(0.5F, 0.5F);
        this.art = EnumArt.values()[this.random.nextInt(EnumArt.values().length)]; // CraftBukkit - generate a non-null painting
    }

    public EntityPainting(net.minecraft.src.World world, int i, int j, int k, int l) {
        this(world);
        this.xPosition = i;
        this.yPosition = j;
        this.zPosition = k;
        ArrayList arraylist = new ArrayList();
        EnumArt[] aenumart = EnumArt.values();
        int i1 = aenumart.length;

        for (int j1 = 0; j1 < i1; ++j1) {
            EnumArt enumart = aenumart[j1];

            this.art = enumart;
            this.setDirection(l);
            if (this.survives()) {
                arraylist.add(enumart);
            }
        }

        if (!arraylist.isEmpty()) {
            this.art = (EnumArt) arraylist.get(this.random.nextInt(arraylist.size()));
        }

        this.setDirection(l);
    }

    protected void entityInit() {}

    public void setDirection(int i) {
        this.direction = i;
        this.lastYaw = this.rotationYaw = (float) (i * 90);
        float f = (float) this.art.B;
        float f1 = (float) this.art.C;
        float f2 = (float) this.art.B;

        if (i != 0 && i != 2) {
            f = 0.5F;
        } else {
            f2 = 0.5F;
        }

        f /= 32.0F;
        f1 /= 32.0F;
        f2 /= 32.0F;
        float f3 = (float) this.xPosition + 0.5F;
        float f4 = (float) this.yPosition + 0.5F;
        float f5 = (float) this.zPosition + 0.5F;
        float f6 = 0.5625F;

        if (i == 0) {
            f5 -= f6;
        }

        if (i == 1) {
            f3 -= f6;
        }

        if (i == 2) {
            f5 += f6;
        }

        if (i == 3) {
            f3 += f6;
        }

        if (i == 0) {
            f3 -= this.b(this.art.B);
        }

        if (i == 1) {
            f5 += this.b(this.art.B);
        }

        if (i == 2) {
            f3 += this.b(this.art.B);
        }

        if (i == 3) {
            f5 -= this.b(this.art.B);
        }

        f4 += this.b(this.art.C);
        this.setPosition((double) f3, (double) f4, (double) f5);
        float f7 = -0.00625F;

        this.boundingBox.b((double) (f3 - f - f7), (double) (f4 - f1 - f7), (double) (f5 - f2 - f7), (double) (f3 + f + f7), (double) (f4 + f1 + f7), (double) (f5 + f2 + f7));
    }

    private float b(int i) {
        return i == 32 ? 0.5F : (i == 64 ? 0.5F : 0.0F);
    }

    public void h_() {
        if (this.f++ == 100 && !this.worldObj.isStatic) {
            this.f = 0;
            if (!this.isDead && !this.survives()) {
                // CraftBukkit start
                Material material = this.worldObj.getMaterial((int) this.posX, (int) this.posY, (int) this.posZ);
                RemoveCause cause;

                if (material.equals(Material.WATER)) {
                    cause = RemoveCause.WATER;
                } else if (!material.equals(Material.AIR)) {
                    // TODO: This feels insufficient to catch 100% of suffocation cases
                    cause = RemoveCause.OBSTRUCTION;
                } else {
                    cause = RemoveCause.PHYSICS;
                }

                PaintingBreakEvent event = new PaintingBreakEvent((Painting) this.getBukkitEntity(), cause);
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled() || isDead) {
                    return;
                }
                // CraftBukkit end

                this.setDead();
                this.worldObj.addEntity(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new net.minecraft.src.ItemStack(Item.PAINTING)));
            }
        }
    }

    public boolean survives() {
        if (!this.worldObj.getCubes(this, this.boundingBox).isEmpty()) {
            return false;
        } else {
            int i = this.art.B / 16;
            int j = this.art.C / 16;
            int k = this.xPosition;
            int l = this.yPosition;
            int i1 = this.zPosition;

            if (this.direction == 0) {
                k = MathHelper.floor(this.posX - (double) ((float) this.art.B / 32.0F));
            }

            if (this.direction == 1) {
                i1 = MathHelper.floor(this.posZ - (double) ((float) this.art.B / 32.0F));
            }

            if (this.direction == 2) {
                k = MathHelper.floor(this.posX - (double) ((float) this.art.B / 32.0F));
            }

            if (this.direction == 3) {
                i1 = MathHelper.floor(this.posZ - (double) ((float) this.art.B / 32.0F));
            }

            l = MathHelper.floor(this.posY - (double) ((float) this.art.C / 32.0F));

            for (int j1 = 0; j1 < i; ++j1) {
                for (int k1 = 0; k1 < j; ++k1) {
                    Material material;

                    if (this.direction != 0 && this.direction != 2) {
                        material = this.worldObj.getMaterial(this.xPosition, l + k1, i1 + j1);
                    } else {
                        material = this.worldObj.getMaterial(k + j1, l + k1, this.zPosition);
                    }

                    if (!material.isBuildable()) {
                        return false;
                    }
                }
            }

            List list = this.worldObj.getEntities(this, this.boundingBox);
            Iterator iterator = list.iterator();

            Entity entity;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                entity = (Entity) iterator.next();
            } while (!(entity instanceof EntityPainting));

            return false;
        }
    }

    public boolean L() {
        return true;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (!this.isDead && !this.worldObj.isStatic) {
            // CraftBukkit start
            PaintingBreakEvent event = null;
            if (damagesource.getEntity() != null) {
                event = new org.bukkit.event.painting.PaintingBreakByEntityEvent((Painting) this.getBukkitEntity(), damagesource.getEntity() == null ? null : damagesource.getEntity().getBukkitEntity());
            } else {
                if (damagesource == DamageSource.FIRE) {
                    event = new PaintingBreakEvent((Painting) this.getBukkitEntity(), RemoveCause.FIRE);
                }
                // TODO: Could put other stuff here?
            }

            if (event != null) {
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return true;
                }
            }

            if (this.isDead) {
                return true;
            }
            // CraftBukkit end

            this.setDead();
            this.K();
            EntityPlayer entityhuman = null;

            if (damagesource.getEntity() instanceof EntityPlayer) {
                entityhuman = (EntityPlayer) damagesource.getEntity();
            }

            if (entityhuman != null && entityhuman.capabilities.canInstantlyBuild) {
                return true;
            }

            this.worldObj.addEntity(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new net.minecraft.src.ItemStack(Item.PAINTING)));
        }

        return true;
    }

    public void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Dir", (byte) this.direction);
        nbttagcompound.setString("Motive", this.art.A);
        nbttagcompound.setInteger("TileX", this.xPosition);
        nbttagcompound.setInteger("TileY", this.yPosition);
        nbttagcompound.setInteger("TileZ", this.zPosition);
    }

    public void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        this.direction = nbttagcompound.getByte("Dir");
        this.xPosition = nbttagcompound.getInteger("TileX");
        this.yPosition = nbttagcompound.getInteger("TileY");
        this.zPosition = nbttagcompound.getInteger("TileZ");
        String s = nbttagcompound.getString("Motive");
        EnumArt[] aenumart = EnumArt.values();
        int i = aenumart.length;

        for (int j = 0; j < i; ++j) {
            EnumArt enumart = aenumart[j];

            if (enumart.A.equals(s)) {
                this.art = enumart;
            }
        }

        if (this.art == null) {
            this.art = EnumArt.KEBAB;
        }

        this.setDirection(this.direction);
    }

    public void move(double d0, double d1, double d2) {
        if (!this.worldObj.isStatic && !this.isDead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) {
            if (isDead) return; // CraftBukkit

            this.setDead();
            this.worldObj.addEntity(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new net.minecraft.src.ItemStack(Item.PAINTING)));
        }
    }

    public void g(double d0, double d1, double d2) {
        if (false && !this.worldObj.isStatic && !this.isDead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) { // CraftBukkit - not needed for paintings
            this.setDead();
            this.worldObj.addEntity(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.PAINTING)));
        }
    }
}
