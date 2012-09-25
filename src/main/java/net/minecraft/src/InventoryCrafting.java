package net.minecraft.src;

// CraftBukkit start
import java.util.List;

import net.minecraft.server.*;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
// CraftBukkit end

public class InventoryCrafting implements IInventory {

    private net.minecraft.src.ItemStack[] items;
    private int b;
    private Container c;

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    public IRecipe currentRecipe;
    public IInventory resultInventory;
    private EntityPlayer owner;
    private int maxStack = MAX_STACK;

    public net.minecraft.src.ItemStack[] getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public InventoryType getInvType() {
        return items.length == 4 ? InventoryType.CRAFTING : InventoryType.WORKBENCH;
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return owner.getBukkitEntity();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
        resultInventory.setMaxStackSize(size);
    }

    public InventoryCrafting(Container container, int i, int j, EntityPlayer player) {
        this(container, i, j);
        this.owner = player;
    }
    // CraftBukkit end

    public InventoryCrafting(Container container, int i, int j) {
        int k = i * j;

        this.items = new net.minecraft.src.ItemStack[k];
        this.c = container;
        this.b = i;
    }

    public int getSize() {
        return this.items.length;
    }

    public net.minecraft.src.ItemStack getItem(int i) {
        return i >= this.getSize() ? null : this.items[i];
    }

    public net.minecraft.src.ItemStack b(int i, int j) {
        if (i >= 0 && i < this.b) {
            int k = i + j * this.b;

            return this.getItem(k);
        } else {
            return null;
        }
    }

    public String getName() {
        return "container.crafting";
    }

    public net.minecraft.src.ItemStack splitWithoutUpdate(int i) {
        if (this.items[i] != null) {
            net.minecraft.src.ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public net.minecraft.src.ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            net.minecraft.src.ItemStack itemstack;

            if (this.items[i].count <= j) {
                itemstack = this.items[i];
                this.items[i] = null;
                this.c.a((IInventory) this);
                return itemstack;
            } else {
                itemstack = this.items[i].a(j);
                if (this.items[i].count == 0) {
                    this.items[i] = null;
                }

                this.c.a((IInventory) this);
                return itemstack;
            }
        } else {
            return null;
        }
    }

    public void setItem(int i, net.minecraft.src.ItemStack itemstack) {
        this.items[i] = itemstack;
        this.c.a((IInventory) this);
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public void update() {}

    public boolean a(EntityPlayer entityhuman) {
        return true;
    }

    public void startOpen() {}

    public void f() {}
}
