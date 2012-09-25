package net.minecraft.src;

// CraftBukkit start

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryCraftResult implements IInventory {

    private net.minecraft.src.ItemStack[] items = new net.minecraft.src.ItemStack[1];

    // CraftBukkit start
    private int maxStack = MAX_STACK;

    public net.minecraft.src.ItemStack[] getContents() {
        return this.items;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return null; // Result slots don't get an owner
    }

    // Don't need a transaction; the InventoryCrafting keeps track of it for us
    public void onOpen(CraftHumanEntity who) {}
    public void onClose(CraftHumanEntity who) {}
    public java.util.List<HumanEntity> getViewers() {
        return new java.util.ArrayList<HumanEntity>();
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public InventoryCraftResult() {}

    public int getSize() {
        return 1;
    }

    public net.minecraft.src.ItemStack getItem(int i) {
        return this.items[i];
    }

    public String getName() {
        return "Result";
    }

    public net.minecraft.src.ItemStack splitStack(int i, int j) {
        if (this.items[i] != null) {
            net.minecraft.src.ItemStack itemstack = this.items[i];

            this.items[i] = null;
            return itemstack;
        } else {
            return null;
        }
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

    public void setItem(int i, net.minecraft.src.ItemStack itemstack) {
        this.items[i] = itemstack;
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
