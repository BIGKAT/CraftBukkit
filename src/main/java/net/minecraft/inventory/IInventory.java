package net.minecraft.inventory;

import org.bukkit.craftbukkit.entity.CraftHumanEntity; // CraftBukkit
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IInventory
{
    /**
     * Returns the number of slots in the inventory.
     */
    int getSizeInventory();

    ItemStack getStackInSlot(int i);

    ItemStack decrStackSize(int i, int j);

    ItemStack getStackInSlotOnClosing(int i);

    void setInventorySlotContents(int i, ItemStack itemstack);

    /**
     * Returns the name of the inventory.
     */
    String getInvName();

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended. *Isn't
     * this more of a set than a get?*
     */
    int getInventoryStackLimit();

    /**
     * Called when an the contents of an Inventory change, usually
     */
    void onInventoryChanged();

    boolean isUseableByPlayer(EntityPlayer entityhuman);

    void openChest();

    void closeChest();

    // CraftBukkit start
    ItemStack[] getContents();

    void onOpen(CraftHumanEntity who);

    void onClose(CraftHumanEntity who);

    java.util.List<org.bukkit.entity.HumanEntity> getViewers();

    org.bukkit.inventory.InventoryHolder getOwner();

    void setMaxStackSize(int size);

    int MAX_STACK = 64;
    // CraftBukkit end
}
