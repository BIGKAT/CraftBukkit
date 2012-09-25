package net.minecraft.src;

import net.minecraft.server.Container;
import net.minecraft.server.ICrafting;
import net.minecraft.server.IMerchant;
import net.minecraft.server.SlotMerchantResult;
import net.minecraft.src.*;

import org.bukkit.craftbukkit.inventory.CraftInventoryView; // CraftBukkit

public class ContainerMerchant extends Container {

    private IMerchant merchant;
    private net.minecraft.src.InventoryMerchant f;
    private final net.minecraft.src.World g;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity == null) {
            bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), new org.bukkit.craftbukkit.inventory.CraftInventoryMerchant(this.getMerchantInventory()), this);
        }
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerMerchant(InventoryPlayer playerinventory, IMerchant imerchant, net.minecraft.src.World world) {
        this.merchant = imerchant;
        this.g = world;
        this.f = new net.minecraft.src.InventoryMerchant(playerinventory.player, imerchant);
        this.a(new net.minecraft.src.Slot(this.f, 0, 36, 53));
        this.a(new net.minecraft.src.Slot(this.f, 1, 62, 53));
        this.a((net.minecraft.src.Slot) (new SlotMerchantResult(playerinventory.player, imerchant, this.f, 2, 120, 53)));
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

    public net.minecraft.server.InventoryMerchant getMerchantInventory() {
        return this.f;
    }

    public void addCraftingToCrafters(ICrafting icrafting) {
        super.addCraftingToCrafters(icrafting);
    }

    public void b() {
        super.b();
    }

    public void a(net.minecraft.src.IInventory iinventory) {
        this.f.g();
        super.a(iinventory);
    }

    public void c(int i) {
        this.f.c(i);
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        return this.merchant.l_() == entityhuman;
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
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 30) {
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

    public void a(net.minecraft.src.EntityPlayer entityhuman) {
        super.a(entityhuman);
        this.merchant.a_((net.minecraft.src.EntityPlayer) null);
        super.a(entityhuman);
        if (!this.g.isStatic) {
            net.minecraft.src.ItemStack itemstack = this.f.splitWithoutUpdate(0);

            if (itemstack != null) {
                entityhuman.drop(itemstack);
            }

            itemstack = this.f.splitWithoutUpdate(1);
            if (itemstack != null) {
                entityhuman.drop(itemstack);
            }
        }
    }
}
