package net.minecraft.server;

import java.util.Iterator;

import org.bukkit.event.player.PlayerPickupItemEvent; // CraftBukkit
// Forge start
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
// Forge end

public class EntityItem extends Entity {

    public int age;
    public int pickupDelay;
    private int d;
    public float c;
    private int lastTick = (int) (System.currentTimeMillis() / 50); // CraftBukkit
    /**
     * The maximum age of this EntityItem.  The item is expired once this is reached.
     */
    public int lifespan = 6000; // Forge

    public EntityItem(World world, double d0, double d1, double d2) {
        super(world);
        this.age = 0;
        this.d = 5;
        this.c = (float) (Math.random() * 3.141592653589793D * 2.0D);
        this.a(0.25F, 0.25F);
        this.height = this.length / 2.0F;
        this.setPosition(d0, d1, d2);
        this.yaw = (float) (Math.random() * 360.0D);
        this.motX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.motY = 0.20000000298023224D;
        this.motZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
    }

    public EntityItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
        this(world, d0, d1, d2);
        // CraftBukkit start - Can't set null items in the datawatcher
        if (itemstack == null || itemstack.getItem() == null) {
            return;
        }
        // CraftBukkit end
        this.setItemStack(itemstack);
        this.lifespan = itemstack.getItem() == null ? 6000 : itemstack.getItem().getEntityLifespan(itemstack, world); // Forge
    }

    protected boolean f_() {
        return false;
    }

    public EntityItem(World world) {
        super(world);
        this.age = 0;
        this.d = 5;
        this.c = (float) (Math.random() * 3.141592653589793D * 2.0D);
        this.a(0.25F, 0.25F);
        this.height = this.length / 2.0F;
    }

    protected void a() {
        this.getDataWatcher().a(10, 5);
    }

    public void j_() {
        super.j_();
        // CraftBukkit start
        int currentTick = (int) (System.currentTimeMillis() / 50);
        this.pickupDelay -= (currentTick - this.lastTick);
        this.lastTick = currentTick;
        // CraftBukkit end

        if (lastTick % 2 == 0) { // Spigot
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.motY -= 0.03999999910593033D;
        this.Y = this.i(this.locX, (this.boundingBox.b + this.boundingBox.e) / 2.0D, this.locZ);
        this.move(this.motX, this.motY, this.motZ);
        boolean flag = (int) this.lastX != (int) this.locX || (int) this.lastY != (int) this.locY || (int) this.lastZ != (int) this.locZ;

        if (flag || this.ticksLived % 25 == 0) {
            if (this.world.getMaterial(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) == Material.LAVA) {
                this.motY = 0.20000000298023224D;
                this.motX = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                this.motZ = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                this.makeSound("random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
            }

            if (!this.world.isStatic) {
                this.g();
            }
        }

        float f = 0.98F;

        if (this.onGround) {
            f = 0.58800006F;
            int i = this.world.getTypeId(MathHelper.floor(this.locX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.locZ));

            if (i > 0) {
                f = Block.byId[i].frictionFactor * 0.98F;
            }
        }

        this.motX *= (double) f;
        this.motY *= 0.9800000190734863D;
        this.motZ *= (double) f;
        if (this.onGround) {
            this.motY *= -0.5D;
        }
        } // Spigot
        ++this.age;
        // Forge start
        ItemStack item = getDataWatcher().f(10);
        if (!this.world.isStatic && this.age >= this.lifespan) {
            if (item != null) {
                ItemExpireEvent event = new ItemExpireEvent(this, this.getItemStack().getItem() == null ? 6000 : this.getItemStack().getItem().getEntityLifespan(this.getItemStack(), this.world));
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    this.lifespan += event.extraLife;
                } else {
                    // CraftBukkit start
                    if (org.bukkit.craftbukkit.event.CraftEventFactory.callItemDespawnEvent(this).isCancelled()) {
                        this.age = 0;
                        return;
                    }
                    // CraftBukkit end
                    else this.die();
                }
            }
            else {
                this.die();
            }
            // Forge end
        }

        if (this.getItemStack() != null && this.getItemStack().count <= 0) // Forge
            this.die();
    }

    private void g() {
        Iterator iterator = this.world.a(EntityItem.class, this.boundingBox.grow(0.5D, 0.0D, 0.5D)).iterator();

        while (iterator.hasNext()) {
            EntityItem entityitem = (EntityItem) iterator.next();

            this.a(entityitem);
        }
    }

    public boolean a(EntityItem entityitem) {
        if (entityitem == this) {
            return false;
        } else if (entityitem.isAlive() && this.isAlive()) {
            ItemStack itemstack = this.getItemStack();
            ItemStack itemstack1 = entityitem.getItemStack();

            if (itemstack1.getItem() != itemstack.getItem()) {
                return false;
            } else if (itemstack1.hasTag() ^ itemstack.hasTag()) {
                return false;
            } else if (itemstack1.hasTag() && !itemstack1.getTag().equals(itemstack.getTag())) {
                return false;
            } else if (itemstack1.getItem().l() && itemstack1.getData() != itemstack.getData()) {
                return false;
            } else if (itemstack1.count < itemstack.count) {
                return entityitem.a(this);
            } else if (itemstack1.count + itemstack.count > itemstack1.getMaxStackSize()) {
                return false;
            } else {
                itemstack1.count += itemstack.count;
                entityitem.pickupDelay = Math.max(entityitem.pickupDelay, this.pickupDelay);
                entityitem.age = Math.min(entityitem.age, this.age);
                entityitem.setItemStack(itemstack1);
                this.die();
                return true;
            }
        } else {
            return false;
        }
    }

    public void c() {
        this.age = 4800;
    }

    public boolean I() {
        return this.world.a(this.boundingBox, Material.WATER, (Entity) this);
    }

    protected void burn(int i) {
        this.damageEntity(DamageSource.FIRE, i);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.isInvulnerable()) {
            return false;
        } else if (this.getItemStack() != null && this.getItemStack().id == Item.NETHER_STAR.id && damagesource == DamageSource.EXPLOSION) {
            return false;
        } else {
            this.K();
            this.d -= i;
            if (this.d <= 0) {
                this.die();
            }

            return false;
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) ((byte) this.d));
        nbttagcompound.setShort("Age", (short) this.age);
        nbttagcompound.setInt("Lifespan", this.lifespan); // Forge
        if (this.getItemStack() != null) {
            nbttagcompound.setCompound("Item", this.getItemStack().save(new NBTTagCompound()));
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getShort("Health") & 255;
        this.age = nbttagcompound.getShort("Age");
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Item");

        // CraftBukkit start
        if (nbttagcompound1 != null) {
            ItemStack itemstack = ItemStack.a(nbttagcompound1);
            if (itemstack != null) {
                this.setItemStack(itemstack);
            } else {
                this.die();
            }
        } else {
            this.die();
        }
        // CraftBukkit end
        // Forge start
        ItemStack item = getDataWatcher().f(10);
        if (this.getItemStack() == null || this.getItemStack().count <= 0) {
            // Forge end
            this.die();
        }
        // Forge start
        if (nbttagcompound1.hasKey("Lifespan")) {
            lifespan = nbttagcompound1.getInt("Lifespan");
        }
        // Forge end
    }

    public void c_(EntityHuman entityhuman) {
        if (!this.world.isStatic) {
            ItemStack itemstack = this.getItemStack();
            int i = itemstack.count;
            // Forge start
            if (this.pickupDelay > 0)
                return;

            EntityItemPickupEvent var2 = new EntityItemPickupEvent(entityhuman, this);
            if (MinecraftForge.EVENT_BUS.post(var2))
                return;
            // Forge end

            // CraftBukkit start
            int canHold = entityhuman.inventory.canHold(itemstack);
            int remaining = itemstack.count - canHold;

            if (this.pickupDelay <= 0 && canHold > 0) {
                itemstack.count = canHold;
                PlayerPickupItemEvent event = new PlayerPickupItemEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), (org.bukkit.entity.Item) this.getBukkitEntity(), remaining);
                event.setCancelled(!entityhuman.canPickUpLoot);
                this.world.getServer().getPluginManager().callEvent(event);
                itemstack.count = canHold + remaining;

                if (event.isCancelled()) {
                    return;
                }

                // Possibly < 0; fix here so we do not have to modify code below
                this.pickupDelay = 0;
            }
            // CraftBukkit end

            if (this.pickupDelay <= 0 && (var2.getResult() == Result.ALLOW || i <= 0 || entityhuman.inventory.pickup(this.getItemStack()))) { // Forge
                if (itemstack.id == Block.LOG.id) {
                    entityhuman.a((Statistic) AchievementList.g);
                }

                if (itemstack.id == Item.LEATHER.id) {
                    entityhuman.a((Statistic) AchievementList.t);
                }

                if (itemstack.id == Item.DIAMOND.id) {
                    entityhuman.a((Statistic) AchievementList.w);
                }

                if (itemstack.id == Item.BLAZE_ROD.id) {
                    entityhuman.a((Statistic) AchievementList.z);
                }

                GameRegistry.onPickupNotification(entityhuman, this); // Forge
                this.makeSound("random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityhuman.receive(this, i);
                if (itemstack.count <= 0) {
                    this.die();
                }
            }
        }
    }

    public String getLocalizedName() {
        return LocaleI18n.get("item." + this.getItemStack().a());
    }

    public boolean aq() {
        return false;
    }

    public void b(int i) {
        super.b(i);
        if (!this.world.isStatic) {
            this.g();
        }
    }

    public ItemStack getItemStack() {
        ItemStack itemstack = this.getDataWatcher().f(10);

        if (itemstack == null) {
            System.out.println("Item entity " + this.id + " has no item?!");
            return new ItemStack(Block.STONE);
        } else {
            return itemstack;
        }
    }

    public void setItemStack(ItemStack itemstack) {
        this.getDataWatcher().watch(10, itemstack);
        this.getDataWatcher().h(10);
    }
}