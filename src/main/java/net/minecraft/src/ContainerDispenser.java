package net.minecraft.src;

// CraftBukkit start

import net.minecraft.server.Container;
import net.minecraft.src.*;

import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerDispenser extends Container {

    public net.minecraft.src.TileEntityDispenser items; // CraftBukkit - private -> public
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerDispenser(net.minecraft.src.IInventory iinventory, net.minecraft.src.TileEntityDispenser tileentitydispenser) {
        this.items = tileentitydispenser;
        // CraftBukkit start - save player
        // TODO: Should we check to make sure it really is an InventoryPlayer?
        this.player = (InventoryPlayer)iinventory;
        // CraftBukkit end

        int i;
        int j;

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 3; ++j) {
                this.a(new net.minecraft.src.Slot(tileentitydispenser, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.a(new net.minecraft.src.Slot(iinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.a(new net.minecraft.src.Slot(iinventory, i, 8 + i * 18, 142));
        }
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.items.a(entityhuman);
    }

    public net.minecraft.src.ItemStack b(int i) {
        net.minecraft.src.ItemStack itemstack = null;
        net.minecraft.src.Slot slot = (net.minecraft.src.Slot) this.b.get(i);

        if (slot != null && slot.d()) {
            net.minecraft.src.ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i < 9) {
                if (!this.a(itemstack1, 9, 45, true)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 0, 9, false)) {
                return null;
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

        CraftInventory inventory = new CraftInventory(this.items);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
