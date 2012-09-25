package net.minecraft.src;

import java.util.Iterator;

// CraftBukkit start

import net.minecraft.src.*;

import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerBrewingStand extends Container {

    private net.minecraft.src.TileEntityBrewingStand brewingStand;
    private final net.minecraft.src.Slot f;
    private int g = 0;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerBrewingStand(InventoryPlayer playerinventory, net.minecraft.src.TileEntityBrewingStand tileentitybrewingstand) {
        player = playerinventory; // CraftBukkit
        this.brewingStand = tileentitybrewingstand;
        this.a(new SlotPotionBottle(playerinventory.player, tileentitybrewingstand, 0, 56, 46));
        this.a(new SlotPotionBottle(playerinventory.player, tileentitybrewingstand, 1, 79, 53));
        this.a(new SlotPotionBottle(playerinventory.player, tileentitybrewingstand, 2, 102, 46));
        this.f = this.a(new SlotBrewing(this, tileentitybrewingstand, 3, 79, 17));

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.a(new net.minecraft.src.Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.a(new net.minecraft.src.Slot(playerinventory, i, 8 + i * 18, 142));
        }
    }

    public void addCraftingToCrafters(ICrafting icrafting) {
        super.addCraftingToCrafters(icrafting);
        icrafting.updateCraftingInventoryInfo(this, 0, this.brewingStand.t_());
    }

    public void b() {
        super.b();
        Iterator iterator = this.listeners.iterator();

        while (iterator.hasNext()) {
            ICrafting icrafting = (ICrafting) iterator.next();

            if (this.g != this.brewingStand.t_()) {
                icrafting.updateCraftingInventoryInfo(this, 0, this.brewingStand.t_());
            }
        }

        this.g = this.brewingStand.t_();
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.brewingStand.a(entityhuman);
    }

    public net.minecraft.src.ItemStack b(int i) {
        net.minecraft.src.ItemStack itemstack = null;
        net.minecraft.src.Slot slot = (net.minecraft.src.Slot) this.b.get(i);

        if (slot != null && slot.d()) {
            net.minecraft.src.ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if ((i < 0 || i > 2) && i != 3) {
                if (!this.f.d() && this.f.isAllowed(itemstack1)) {
                    if (!this.a(itemstack1, 3, 4, false)) {
                        return null;
                    }
                } else if (SlotPotionBottle.a_(itemstack)) {
                    if (!this.a(itemstack1, 0, 3, false)) {
                        return null;
                    }
                } else if (i >= 4 && i < 31) {
                    if (!this.a(itemstack1, 31, 40, false)) {
                        return null;
                    }
                } else if (i >= 31 && i < 40) {
                    if (!this.a(itemstack1, 4, 31, false)) {
                        return null;
                    }
                } else if (!this.a(itemstack1, 4, 40, false)) {
                    return null;
                }
            } else {
                if (!this.a(itemstack1, 4, 40, true)) {
                    return null;
                }

                slot.a(itemstack1, itemstack);
            }

            if (itemstack1.count == 0) {
                slot.set((net.minecraft.src.ItemStack) null);
            } else {
                slot.e();
            }

            if (itemstack1.count == itemstack.count) {
                return null;
            }

            slot.b(itemstack1);
        }

        return itemstack;
    }

    // CraftBukkit start
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory = new CraftInventory(this.brewingStand);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
