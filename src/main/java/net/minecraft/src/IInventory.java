package net.minecraft.src;

import org.bukkit.craftbukkit.entity.CraftHumanEntity; // CraftBukkit

public interface IInventory {

    int getSize();

    net.minecraft.src.ItemStack getItem(int i);

    net.minecraft.src.ItemStack splitStack(int i, int j);

    net.minecraft.src.ItemStack splitWithoutUpdate(int i);

    void setItem(int i, net.minecraft.src.ItemStack itemstack);

    String getName();

    int getMaxStackSize();

    void update();

    boolean a(EntityPlayer entityhuman);

    void startOpen();

    void f();

    // CraftBukkit start
    net.minecraft.src.ItemStack[] getContents();

    void onOpen(CraftHumanEntity who);

    void onClose(CraftHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int size);

    int MAX_STACK = 64;
    // CraftBukkit end
}
