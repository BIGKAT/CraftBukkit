package net.minecraft.src;

// CraftBukkit start
import java.util.List;

import net.minecraft.server.*;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryMerchant implements IInventory {

    private final IMerchant merchant;
    private net.minecraft.src.ItemStack[] itemsInSlots = new net.minecraft.src.ItemStack[3];
    private final EntityPlayer player;
    private MerchantRecipe recipe;
    private int e;

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public net.minecraft.src.ItemStack[] getContents() {
        return this.itemsInSlots;
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

    public void setMaxStackSize(int i) {
        maxStack = i;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return player.getBukkitEntity();
    }
    // CraftBukkit end

    public InventoryMerchant(EntityPlayer entityhuman, IMerchant imerchant) {
        this.player = entityhuman;
        this.merchant = imerchant;
    }

    public int getSize() {
        return this.itemsInSlots.length;
    }

    public net.minecraft.src.ItemStack getItem(int i) {
        return this.itemsInSlots[i];
    }

    public net.minecraft.src.ItemStack splitStack(int i, int j) {
        if (this.itemsInSlots[i] != null) {
            net.minecraft.src.ItemStack itemstack;

            if (i == 2) {
                itemstack = this.itemsInSlots[i];
                this.itemsInSlots[i] = null;
                return itemstack;
            } else if (this.itemsInSlots[i].count <= j) {
                itemstack = this.itemsInSlots[i];
                this.itemsInSlots[i] = null;
                if (this.d(i)) {
                    this.g();
                }

                return itemstack;
            } else {
                itemstack = this.itemsInSlots[i].a(j);
                if (this.itemsInSlots[i].count == 0) {
                    this.itemsInSlots[i] = null;
                }

                if (this.d(i)) {
                    this.g();
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    private boolean d(int i) {
        return i == 0 || i == 1;
    }

    public net.minecraft.src.ItemStack splitWithoutUpdate(int i) {
        if (this.itemsInSlots[i] != null) {
            net.minecraft.src.ItemStack itemstack = this.itemsInSlots[i];

            this.itemsInSlots[i] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    public void setItem(int i, net.minecraft.src.ItemStack itemstack) {
        this.itemsInSlots[i] = itemstack;
        if (itemstack != null && itemstack.count > this.getMaxStackSize()) {
            itemstack.count = this.getMaxStackSize();
        }

        if (this.d(i)) {
            this.g();
        }
    }

    public String getName() {
        return "mob.villager";
    }

    public int getMaxStackSize() {
        return maxStack; // CraftBukkit
    }

    public boolean a(EntityPlayer entityhuman) {
        return this.merchant.l_() == entityhuman;
    }

    public void startOpen() {}

    public void f() {}

    public void update() {
        this.g();
    }

    public void g() {
        this.recipe = null;
        net.minecraft.src.ItemStack itemstack = this.itemsInSlots[0];
        net.minecraft.src.ItemStack itemstack1 = this.itemsInSlots[1];

        if (itemstack == null) {
            itemstack = itemstack1;
            itemstack1 = null;
        }

        if (itemstack == null) {
            this.setItem(2, (net.minecraft.src.ItemStack) null);
        } else {
            MerchantRecipeList merchantrecipelist = this.merchant.getOffers(this.player);

            if (merchantrecipelist != null) {
                MerchantRecipe merchantrecipe = merchantrecipelist.a(itemstack, itemstack1, this.e);

                if (merchantrecipe != null) {
                    this.recipe = merchantrecipe;
                    this.setItem(2, merchantrecipe.getBuyItem3().cloneItemStack());
                } else if (itemstack1 != null) {
                    merchantrecipe = merchantrecipelist.a(itemstack1, itemstack, this.e);
                    if (merchantrecipe != null) {
                        this.recipe = merchantrecipe;
                        this.setItem(2, merchantrecipe.getBuyItem3().cloneItemStack());
                    } else {
                        this.setItem(2, (net.minecraft.src.ItemStack) null);
                    }
                } else {
                    this.setItem(2, (net.minecraft.src.ItemStack) null);
                }
            }
        }
    }

    public MerchantRecipe getRecipe() {
        return this.recipe;
    }

    public void c(int i) {
        this.e = i;
        this.g();
    }
}
