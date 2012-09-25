package net.minecraft.src;

import java.util.Iterator;

import net.minecraft.server.*;

import org.bukkit.event.player.PlayerPickupItemEvent; // CraftBukkit

public class EntityItem extends Entity {

    public net.minecraft.src.ItemStack item;
    public int age = 0;
    public int delayBeforeCanPickup;
    private int e = 5;
    public float d = (float) (Math.random() * 3.141592653589793D * 2.0D);
    private int lastTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit

    public EntityItem(net.minecraft.src.World world, double d0, double d1, double d2, net.minecraft.src.ItemStack itemstack) {
        super(world);
        this.a(0.25F, 0.25F);
        this.height = this.length / 2.0F;
        this.setPosition(d0, d1, d2);
        this.item = itemstack;
        // CraftBukkit start - infinite item fix & nullcheck
        if (this.item == null) {
            throw new IllegalArgumentException("Can't create an EntityItem for a null item");
        }
        if (this.item.count <= -1) {
            this.item.count = 1;
        }
        // CraftBukkit end
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
    }

    protected boolean e_() {
        return false;
    }

    public EntityItem(net.minecraft.src.World world) {
        super(world);
        this.a(0.25F, 0.25F);
        this.height = this.length / 2.0F;
    }

    protected void a() {}

    public void h_() {
        super.h_();
        // CraftBukkit start
        int currentTick = (int) (System.currentTimeMillis() / 50);
        this.delayBeforeCanPickup -= (currentTick - this.lastTick);
        this.lastTick = currentTick;
        // CraftBukkit end

        this.lastX = this.posX;
        this.lastY = this.posY;
        this.lastZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.i(this.posX, (this.boundingBox.b + this.boundingBox.e) / 2.0D, this.posZ);
        this.move(this.motionX, this.motionY, this.motionZ);
        boolean flag = (int) this.lastX != (int) this.posX || (int) this.lastY != (int) this.posY || (int) this.lastZ != (int) this.posZ;

        if (flag) {
            if (this.worldObj.getMaterial(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) == Material.LAVA) {
                this.motionY = 0.20000000298023224D;
                this.motionX = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                this.motionZ = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                this.worldObj.makeSound(this, "random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
            }

            if (!this.worldObj.isStatic) {
                Iterator iterator = this.worldObj.a(EntityItem.class, this.boundingBox.grow(0.5D, 0.0D, 0.5D)).iterator();

                while (iterator.hasNext()) {
                    EntityItem entityitem = (EntityItem) iterator.next();

                    this.a(entityitem);
                }
            }
        }

        float f = 0.98F;

        if (this.onGround) {
            f = 0.58800006F;
            int i = this.worldObj.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.posZ));

            if (i > 0) {
                f = Block.blocksList[i].frictionFactor * 0.98F;
            }
        }

        this.motionX *= (double) f;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double) f;
        if (this.onGround) {
            this.motionY *= -0.5D;
        }

        ++this.age;
        if (this.age >= 6000) {
            // CraftBukkit start
            if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                this.age = 0;
                return;
            }
            // CraftBukkit end
            this.setDead();
        }
    }

    public boolean a(EntityItem entityitem) {
        if (entityitem == this) {
            return false;
        } else if (entityitem.isEntityAlive() && this.isEntityAlive()) {
            if (entityitem.item.getItem() != this.item.getItem()) {
                return false;
            } else if (entityitem.item.getItem().k() && entityitem.item.getData() != this.item.getData()) {
                return false;
            } else if (entityitem.item.count < this.item.count) {
                return entityitem.a(this);
            } else if (entityitem.item.count + this.item.count > entityitem.item.getMaxStackSize()) {
                return false;
            // CraftBukkit start - don't merge items with enchantments
            } else if (entityitem.item.hasEnchantments() || this.item.hasEnchantments()) {
                return false;
                // CraftBukkit end
            } else {
                entityitem.item.count += this.item.count;
                entityitem.delayBeforeCanPickup = Math.max(entityitem.delayBeforeCanPickup, this.delayBeforeCanPickup);
                entityitem.age = Math.min(entityitem.age, this.age);
                this.setDead();
                return true;
            }
        } else {
            return false;
        }
    }

    public void d() {
        this.age = 4800;
    }

    public boolean I() {
        return this.worldObj.a(this.boundingBox, Material.WATER, this);
    }

    protected void burn(int i) {
        this.damageEntity(DamageSource.FIRE, i);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.K();
        this.e -= i;
        if (this.e <= 0) {
            this.setDead();
        }

        return false;
    }

    public void b(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) ((byte) this.e));
        nbttagcompound.setShort("Age", (short) this.age);
        if (this.item != null) {
            nbttagcompound.setCompoundTag("Item", this.item.save(new net.minecraft.src.NBTTagCompound()));
        }
    }

    public void a(net.minecraft.src.NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getShort("Health") & 255;
        this.age = nbttagcompound.getShort("Age");
        net.minecraft.src.NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");

        this.item = ItemStack.a(nbttagcompound1);
        if (this.item == null) {
            this.setDead();
        }
    }

    public void b_(EntityPlayer entityhuman) {
        if ((!this.worldObj.isStatic) && (this.item != null)) { // CraftBukkit - nullcheck
            int i = this.item.count;

            // CraftBukkit start
            int canHold = entityhuman.inventory.canHold(this.item);
            int remaining = this.item.count - canHold;

            if (this.delayBeforeCanPickup <= 0 && canHold > 0) {
                this.item.count = canHold;
                PlayerPickupItemEvent event = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                this.worldObj.getServer().getPluginManager().callEvent(event);
                this.item.count = canHold + remaining;

                if (event.isCancelled()) {
                    return;
                }

                // Possibly < 0; fix here so we do not have to modify code below
                this.delayBeforeCanPickup = 0;
            }
            // CraftBukkit end

            if (this.delayBeforeCanPickup == 0 && entityhuman.inventory.pickup(this.item)) {
                if (this.item.id == Block.LOG.blockID) {
                    entityhuman.a((Statistic) AchievementList.g);
                }

                if (this.item.id == Item.LEATHER.id) {
                    entityhuman.a((Statistic) AchievementList.t);
                }

                if (this.item.id == Item.DIAMOND.id) {
                    entityhuman.a((Statistic) AchievementList.w);
                }

                if (this.item.id == Item.BLAZE_ROD.id) {
                    entityhuman.a((Statistic) AchievementList.z);
                }

                this.worldObj.makeSound(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityhuman.receive(this, i);
                if (this.item.count <= 0) {
                    this.setDead();
                }
            }
        }
    }

    public String getLocalizedName() {
        if (this.item == null) return LocaleI18n.get("item.unknown"); // CraftBukkit - nullcheck
        return LocaleI18n.get("item." + this.item.a());
    }

    public boolean an() {
        return false;
    }
}
