package net.minecraft.src;

// CraftBukkit start

import net.minecraft.src.*;

import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerChest extends Container {

    public net.minecraft.src.IInventory container; // CraftBukkit - private->public
    private int f;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory;
        if (this.container instanceof InventoryPlayer) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryPlayer((InventoryPlayer) this.container);
        } else if (this.container instanceof net.minecraft.src.InventoryLargeChest) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) this.container);
        } else {
            inventory = new CraftInventory(this.container);
        }

        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerChest(net.minecraft.src.IInventory iinventory, net.minecraft.src.IInventory iinventory1) {
        this.container = iinventory1;
        this.f = iinventory1.getSize() / 9;
        iinventory1.startOpen();
        int i = (this.f - 4) * 18;
        // CraftBukkit start - save player
        // TODO: Should we check to make sure it really is an InventoryPlayer?
        this.player = (InventoryPlayer)iinventory;
        // CraftBukkit end

        int j;
        int k;

        for (j = 0; j < this.f; ++j) {
            for (k = 0; k < 9; ++k) {
                this.a(new net.minecraft.src.Slot(iinventory1, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.a(new net.minecraft.src.Slot(iinventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new net.minecraft.src.Slot(iinventory, j, 8 + j * 18, 161 + i));
        }
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.container.a(entityhuman);
    }

    public net.minecraft.src.ItemStack b(int i) {
        net.minecraft.src.ItemStack itemstack = null;
        net.minecraft.src.Slot slot = (net.minecraft.src.Slot) this.b.get(i);

        if (slot != null && slot.d()) {
            net.minecraft.src.ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i < this.f * 9) {
                if (!this.a(itemstack1, this.f * 9, this.b.size(), true)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 0, this.f * 9, false)) {
                return null;
            }

            if (itemstack1.count == 0) {
                slot.set((net.minecraft.src.ItemStack) null);
            } else {
                slot.e();
            }
        }

        return itemstack;
    }

    public void a(net.minecraft.src.EntityPlayer entityhuman) {
        super.a(entityhuman);
        this.container.f();
    }
}
