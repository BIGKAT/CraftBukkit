package net.minecraft.src;

// CraftBukkit start

import net.minecraft.server.*;
import net.minecraft.src.*;

import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerWorkbench extends Container {

    public net.minecraft.src.InventoryCrafting craftInventory; // CraftBukkit - move initialization into constructor
    public net.minecraft.src.IInventory resultInventory; // CraftBukkit - move initialization into constructor
    private net.minecraft.src.World g;
    private int h;
    private int i;
    private int j;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerWorkbench(InventoryPlayer playerinventory, net.minecraft.src.World world, int i, int j, int k) {
        // CraftBukkit start - switched order of IInventory construction and stored player
        this.resultInventory = new net.minecraft.server.InventoryCraftResult();
        this.craftInventory = new net.minecraft.server.InventoryCrafting(this, 3, 3, playerinventory.player); // CraftBukkit - pass player
        this.craftInventory.resultInventory = this.resultInventory;
        this.player = playerinventory;
        // CraftBukkit end
        this.g = world;
        this.h = i;
        this.i = j;
        this.j = k;
        this.a((net.minecraft.src.Slot) (new SlotResult(playerinventory.player, this.craftInventory, this.resultInventory, 0, 124, 35)));

        int l;
        int i1;

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 3; ++i1) {
                this.a(new net.minecraft.src.Slot(this.craftInventory, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
            }
        }

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 9; ++i1) {
                this.a(new net.minecraft.src.Slot(playerinventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.a(new net.minecraft.src.Slot(playerinventory, l, 8 + l * 18, 142));
        }

        this.a((net.minecraft.src.IInventory) this.craftInventory);
    }

    public void a(net.minecraft.src.IInventory iinventory) {
        // CraftBukkit start
        CraftingManager.getInstance().lastCraftView = getBukkitView();
        net.minecraft.src.ItemStack craftResult = CraftingManager.getInstance().craft(this.craftInventory);
        this.resultInventory.setItem(0, craftResult);
        if (super.listeners.size() < 1) {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) super.listeners.get(0); // TODO: Is this _always_ correct? Seems like it.
        player.serverForThisPlayer.sendPacketToPlayer(new Packet103SetSlot(player.craftingInventory.windowId, 0, craftResult));
        // CraftBukkit end
    }

    public void a(net.minecraft.src.EntityPlayer entityhuman) {
        super.a(entityhuman);
        if (!this.g.isStatic) {
            for (int i = 0; i < 9; ++i) {
                net.minecraft.src.ItemStack itemstack = this.craftInventory.splitWithoutUpdate(i);

                if (itemstack != null) {
                    entityhuman.drop(itemstack);
                }
            }
        }
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.g.getBlockId(this.h, this.i, this.j) != Block.WORKBENCH.blockID ? false : entityhuman.e((double) this.h + 0.5D, (double) this.i + 0.5D, (double) this.j + 0.5D) <= 64.0D;
    }

    public net.minecraft.src.ItemStack b(int i) {
        net.minecraft.src.ItemStack itemstack = null;
        net.minecraft.src.Slot slot = (net.minecraft.src.Slot) this.b.get(i);

        if (slot != null && slot.d()) {
            net.minecraft.src.ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 0) {
                if (!this.a(itemstack1, 10, 46, true)) {
                    return null;
                }

                slot.a(itemstack1, itemstack);
            } else if (i >= 10 && i < 37) {
                if (!this.a(itemstack1, 37, 46, false)) {
                    return null;
                }
            } else if (i >= 37 && i < 46) {
                if (!this.a(itemstack1, 10, 37, false)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 10, 46, false)) {
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

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.craftInventory, this.resultInventory);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
