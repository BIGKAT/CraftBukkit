package net.minecraft.src;

// CraftBukkit start
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryPlayer implements net.minecraft.src.IInventory {

    public net.minecraft.src.ItemStack[] items = new net.minecraft.src.ItemStack[36];
    public net.minecraft.src.ItemStack[] armor = new net.minecraft.src.ItemStack[4];
    public int itemInHandIndex = 0;
    public EntityPlayer player;
    private net.minecraft.src.ItemStack g;
    public boolean e = false;

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public net.minecraft.src.ItemStack[] getContents() {
        return this.items;
    }

    public net.minecraft.src.ItemStack[] getArmorContents() {
        return this.armor;
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

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return this.player.getBukkitEntity();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public InventoryPlayer(EntityPlayer entityhuman) {
        this.player = entityhuman;
    }

    public net.minecraft.src.ItemStack getItemInHand() {
        return this.itemInHandIndex < 9 && this.itemInHandIndex >= 0 ? this.items[this.itemInHandIndex] : null;
    }

    public static int getHotbarSize() {
        return 9;
    }

    private int h(int i) {
        for (int j = 0; j < this.items.length; ++j) {
            if (this.items[j] != null && this.items[j].id == i) {
                return j;
            }
        }

        return -1;
    }

    private int firstPartial(net.minecraft.src.ItemStack itemstack) {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null && this.items[i].id == itemstack.id && this.items[i].isStackable() && this.items[i].count < this.items[i].getMaxStackSize() && this.items[i].count < this.getMaxStackSize() && (!this.items[i].usesData() || this.items[i].getData() == itemstack.getData()) && net.minecraft.src.ItemStack.equals(this.items[i], itemstack)) {
                return i;
            }
        }

        return -1;
    }

    // CraftBukkit start - watch method above! :D
    public int canHold(net.minecraft.src.ItemStack itemstack) {
        int remains = itemstack.count;
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null) return itemstack.count;

            // Taken from firstPartial(ItemStack)
            if (this.items[i] != null && this.items[i].id == itemstack.id && this.items[i].isStackable() && this.items[i].count < this.items[i].getMaxStackSize() && this.items[i].count < this.getMaxStackSize() && (!this.items[i].usesData() || this.items[i].getData() == itemstack.getData())) {
                remains -= (this.items[i].getMaxStackSize() < this.getMaxStackSize() ? this.items[i].getMaxStackSize() : this.getMaxStackSize()) - this.items[i].count;
            }
            if (remains <= 0) return itemstack.count;
        }
        return itemstack.count - remains;
    }
    // CraftBukkit end

    public int i() {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private int e(net.minecraft.src.ItemStack itemstack) {
        int i = itemstack.id;
        int j = itemstack.count;
        int k;

        if (itemstack.getMaxStackSize() == 1) {
            k = this.i();
            if (k < 0) {
                return j;
            } else {
                if (this.items[k] == null) {
                    this.items[k] = net.minecraft.src.ItemStack.b(itemstack);
                }

                return 0;
            }
        } else {
            k = this.firstPartial(itemstack);
            if (k < 0) {
                k = this.i();
            }

            if (k < 0) {
                return j;
            } else {
                if (this.items[k] == null) {
                    this.items[k] = new net.minecraft.src.ItemStack(i, 0, itemstack.getData());
                    if (itemstack.hasTag()) {
                        this.items[k].setTag((net.minecraft.src.NBTTagCompound) itemstack.getTag().clone());
                    }
                }

                int l = j;

                if (j > this.items[k].getMaxStackSize() - this.items[k].count) {
                    l = this.items[k].getMaxStackSize() - this.items[k].count;
                }

                if (l > this.getMaxStackSize() - this.items[k].count) {
                    l = this.getMaxStackSize() - this.items[k].count;
                }

                if (l == 0) {
                    return j;
                } else {
                    j -= l;
                    this.items[k].count += l;
                    this.items[k].b = 5;
                    return j;
                }
            }
        }
    }

    public void k() {
        for (int i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                this.items[i].a(this.player.worldObj, this.player, i, this.itemInHandIndex == i);
            }
        }
    }

    public boolean d(int i) {
        int j = this.h(i);

        if (j < 0) {
            return false;
        } else {
            if (--this.items[j].count <= 0) {
                this.items[j] = null;
            }

            return true;
        }
    }

    public boolean e(int i) {
        int j = this.h(i);

        return j >= 0;
    }

    public boolean pickup(net.minecraft.src.ItemStack itemstack) {
        int i;

        if (itemstack.h()) {
            i = this.i();
            if (i >= 0) {
                this.items[i] = net.minecraft.src.ItemStack.b(itemstack);
                this.items[i].b = 5;
                itemstack.count = 0;
                return true;
            } else if (this.player.capabilities.canInstantlyBuild) {
                itemstack.count = 0;
                return true;
            } else {
                return false;
            }
        } else {
            do {
                i = itemstack.count;
                itemstack.count = this.e(itemstack);
            } while (itemstack.count > 0 && itemstack.count < i);

            if (itemstack.count == i && this.player.capabilities.canInstantlyBuild) {
                itemstack.count = 0;
                return true;
            } else {
                return itemstack.count < i;
            }
        }
    }

    public net.minecraft.src.ItemStack splitStack(int i, int j) {
        net.minecraft.src.ItemStack[] aitemstack = this.items;

        if (i >= this.items.length) {
            aitemstack = this.armor;
            i -= this.items.length;
        }

        if (aitemstack[i] != null) {
            net.minecraft.src.ItemStack itemstack;

            if (aitemstack[i].count <= j) {
                itemstack = aitemstack[i];
                aitemstack[i] = null;
                return itemstack;
            } else {
                itemstack = aitemstack[i].a(j);
                if (aitemstack[i].count == 0) {
                    aitemstack[i] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    public net.minecraft.src.ItemStack splitWithoutUpdate(int i) {
        net.minecraft.src.ItemStack[] aitemstack = this.items;

        if (i >= this.items.length) {
            aitemstack = this.armor;
            i -= this.items.length;
        }

        if (aitemstack[i] != null) {
            net.minecraft.src.ItemStack itemstack = aitemstack[i];

            aitemstack[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, net.minecraft.src.ItemStack itemstack) {
        net.minecraft.src.ItemStack[] aitemstack = this.items;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.armor;
        }

        aitemstack[i] = itemstack;
    }

    public float a(Block block) {
        float f = 1.0F;

        if (this.items[this.itemInHandIndex] != null) {
            f *= this.items[this.itemInHandIndex].a(block);
        }

        return f;
    }

    public NBTTagList a(NBTTagList nbttaglist) {
        int i;
        net.minecraft.src.NBTTagCompound nbttagcompound;

        for (i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                nbttagcompound = new net.minecraft.src.NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                this.items[i].save(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null) {
                nbttagcompound = new net.minecraft.src.NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) (i + 100));
                this.armor[i].save(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public void b(NBTTagList nbttaglist) {
        this.items = new net.minecraft.src.ItemStack[36];
        this.armor = new net.minecraft.src.ItemStack[4];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            net.minecraft.src.NBTTagCompound nbttagcompound = (net.minecraft.src.NBTTagCompound) nbttaglist.tagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;
            net.minecraft.src.ItemStack itemstack = net.minecraft.src.ItemStack.a(nbttagcompound);

            if (itemstack != null) {
                if (j >= 0 && j < this.items.length) {
                    this.items[j] = itemstack;
                }

                if (j >= 100 && j < this.armor.length + 100) {
                    this.armor[j - 100] = itemstack;
                }
            }
        }
    }

    public int getSize() {
        return this.items.length + 4;
    }

    public net.minecraft.src.ItemStack getItem(int i) {
        net.minecraft.src.ItemStack[] aitemstack = this.items;

        if (i >= aitemstack.length) {
            i -= aitemstack.length;
            aitemstack = this.armor;
        }

        return aitemstack[i];
    }

    public String getName() {
        return "container.inventory";
    }

    public int getMaxStackSize() {
        return maxStack;
    }

    public int a(Entity entity) {
        net.minecraft.src.ItemStack itemstack = this.getItem(this.itemInHandIndex);

        return itemstack != null ? itemstack.a(entity) : 1;
    }

    public boolean b(Block block) {
        if (block.blockMaterial.isHarvestable()) {
            return true;
        } else {
            net.minecraft.src.ItemStack itemstack = this.getItem(this.itemInHandIndex);

            return itemstack != null ? itemstack.b(block) : false;
        }
    }

    public int l() {
        int i = 0;
        net.minecraft.src.ItemStack[] aitemstack = this.armor;
        int j = aitemstack.length;

        for (int k = 0; k < j; ++k) {
            net.minecraft.src.ItemStack itemstack = aitemstack[k];

            if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
                int l = ((ItemArmor) itemstack.getItem()).b;

                i += l;
            }
        }

        return i;
    }

    public void g(int i) {
        i /= 4;
        if (i < 1) {
            i = 1;
        }

        for (int j = 0; j < this.armor.length; ++j) {
            if (this.armor[j] != null && this.armor[j].getItem() instanceof ItemArmor) {
                this.armor[j].damage(i, this.player);
                if (this.armor[j].count == 0) {
                    this.armor[j] = null;
                }
            }
        }
    }

    public void m() {
        int i;

        for (i = 0; i < this.items.length; ++i) {
            if (this.items[i] != null) {
                this.player.a(this.items[i], true);
                this.items[i] = null;
            }
        }

        for (i = 0; i < this.armor.length; ++i) {
            if (this.armor[i] != null) {
                this.player.a(this.armor[i], true);
                this.armor[i] = null;
            }
        }
    }

    public void update() {
        this.e = true;
    }

    public void setItemStack(net.minecraft.src.ItemStack itemstack) {
        this.g = itemstack;
    }

    public net.minecraft.src.ItemStack getItemStack() {
        // CraftBukkit start
        if (this.g != null && this.g.count == 0) {
            this.setItemStack(null);
        }
        // CraftBukkit end
        return this.g;
    }

    public boolean a(EntityPlayer entityhuman) {
        return this.player.isDead ? false : entityhuman.e(this.player) <= 64.0D;
    }

    public boolean c(net.minecraft.src.ItemStack itemstack) {
        net.minecraft.src.ItemStack[] aitemstack = this.armor;
        int i = aitemstack.length;

        int j;
        net.minecraft.src.ItemStack itemstack1;

        for (j = 0; j < i; ++j) {
            itemstack1 = aitemstack[j];
            if (itemstack1 != null && itemstack1.c(itemstack)) {
                return true;
            }
        }

        aitemstack = this.items;
        i = aitemstack.length;

        for (j = 0; j < i; ++j) {
            itemstack1 = aitemstack[j];
            if (itemstack1 != null && itemstack1.c(itemstack)) {
                return true;
            }
        }

        return false;
    }

    public void startOpen() {}

    public void f() {}

    public void b(InventoryPlayer playerinventory) {
        int i;

        for (i = 0; i < this.items.length; ++i) {
            this.items[i] = net.minecraft.src.ItemStack.b(playerinventory.items[i]);
        }

        for (i = 0; i < this.armor.length; ++i) {
            this.armor[i] = net.minecraft.server.ItemStack.b(playerinventory.armor[i]);
        }
    }
}
