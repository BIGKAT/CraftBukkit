package net.minecraft.src;

// CraftBukkit start
import java.util.List;

import net.minecraft.server.NBTTagList;
import net.minecraft.server.TileEntityEnderChest;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryEnderChest extends InventoryBasic {

    private TileEntityEnderChest a;

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    public org.bukkit.entity.Player player;
    private int maxStack = MAX_STACK;

    public net.minecraft.src.ItemStack[] getContents() {
        return this.items;
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
        return this.player;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    public int getMaxStackSize() {
        return maxStack;
    }
    // CraftBukkit end

    public InventoryEnderChest() {
        super("container.enderchest", 27);
    }

    public void a(TileEntityEnderChest tileentityenderchest) {
        this.a = tileentityenderchest;
    }

    public void a(NBTTagList nbttaglist) {
        int i;

        for (i = 0; i < this.getSize(); ++i) {
            this.setItem(i, (net.minecraft.src.ItemStack) null);
        }

        for (i = 0; i < nbttaglist.size(); ++i) {
            net.minecraft.src.NBTTagCompound nbttagcompound = (net.minecraft.src.NBTTagCompound) nbttaglist.get(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSize()) {
                this.setItem(j, net.minecraft.src.ItemStack.a(nbttagcompound));
            }
        }
    }

    public NBTTagList g() {
        NBTTagList nbttaglist = new NBTTagList("EnderItems");

        for (int i = 0; i < this.getSize(); ++i) {
            ItemStack itemstack = this.getItem(i);

            if (itemstack != null) {
                net.minecraft.src.NBTTagCompound nbttagcompound = new net.minecraft.src.NBTTagCompound();

                nbttagcompound.setByte("Slot", (byte) i);
                itemstack.save(nbttagcompound);
                nbttaglist.add(nbttagcompound);
            }
        }

        return nbttaglist;
    }

    public boolean a(EntityPlayer entityhuman) {
        return this.a != null && !this.a.a(entityhuman) ? false : super.a(entityhuman);
    }

    public void startOpen() {
        if (this.a != null) {
            this.a.a();
        }

        super.startOpen();
    }

    public void f() {
        if (this.a != null) {
            this.a.b();
        }

        super.f();
        this.a = null;
    }
}
