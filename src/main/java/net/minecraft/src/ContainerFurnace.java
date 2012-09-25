package net.minecraft.src;

import java.util.Iterator;

// CraftBukkit start

import net.minecraft.server.Container;
import net.minecraft.server.ICrafting;
import net.minecraft.server.SlotFurnaceResult;
import net.minecraft.src.*;

import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerFurnace extends Container {

    private net.minecraft.src.TileEntityFurnace furnace;
    private int f = 0;
    private int g = 0;
    private int h = 0;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryFurnace inventory = new CraftInventoryFurnace(this.furnace);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerFurnace(InventoryPlayer playerinventory, net.minecraft.src.TileEntityFurnace tileentityfurnace) {
        this.furnace = tileentityfurnace;
        this.a(new net.minecraft.src.Slot(tileentityfurnace, 0, 56, 17));
        this.a(new net.minecraft.src.Slot(tileentityfurnace, 1, 56, 53));
        this.a(new SlotFurnaceResult(playerinventory.player, tileentityfurnace, 2, 116, 35));
        this.player = playerinventory; // CraftBukkit - save player

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
        icrafting.updateCraftingInventoryInfo(this, 0, this.furnace.furnaceCookTime);
        icrafting.updateCraftingInventoryInfo(this, 1, this.furnace.furnaceBurnTime);
        icrafting.updateCraftingInventoryInfo(this, 2, this.furnace.ticksForCurrentFuel);
    }

    public void b() {
        super.b();
        Iterator iterator = this.listeners.iterator();

        while (iterator.hasNext()) {
            ICrafting icrafting = (ICrafting) iterator.next();

            if (this.f != this.furnace.furnaceCookTime) {
                icrafting.updateCraftingInventoryInfo(this, 0, this.furnace.furnaceCookTime);
            }

            if (this.g != this.furnace.furnaceBurnTime) {
                icrafting.updateCraftingInventoryInfo(this, 1, this.furnace.furnaceBurnTime);
            }

            if (this.h != this.furnace.ticksForCurrentFuel) {
                icrafting.updateCraftingInventoryInfo(this, 2, this.furnace.ticksForCurrentFuel);
            }
        }

        this.f = this.furnace.furnaceCookTime;
        this.g = this.furnace.furnaceBurnTime;
        this.h = this.furnace.ticksForCurrentFuel;
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.furnace.a(entityhuman);
    }

    public net.minecraft.src.ItemStack b(int i) {
        net.minecraft.src.ItemStack itemstack = null;
        net.minecraft.src.Slot slot = (net.minecraft.src.Slot) this.b.get(i);

        if (slot != null && slot.d()) {
            net.minecraft.src.ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 2) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.a(itemstack1, itemstack);
            } else if (i != 1 && i != 0) {
                if (FurnaceRecipes.getInstance().getResult(itemstack1.getItem().id) != null) {
                    if (!this.a(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (net.minecraft.src.TileEntityFurnace.isFuel(itemstack1)) {
                    if (!this.a(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (i >= 3 && i < 30) {
                    if (!this.a(itemstack1, 30, 39, false)) {
                        return null;
                    }
                } else if (i >= 30 && i < 39 && !this.a(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 3, 39, false)) {
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
}
