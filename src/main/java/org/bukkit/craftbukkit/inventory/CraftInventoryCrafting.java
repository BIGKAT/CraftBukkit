package org.bukkit.craftbukkit.inventory;

import net.minecraft.src.IRecipe;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryCrafting;

import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.util.Java15Compat;

public class CraftInventoryCrafting extends CraftInventory implements CraftingInventory {
    private IInventory resultInventory;

    public CraftInventoryCrafting(InventoryCrafting inventory, IInventory resultInventory) {
        super(inventory);
        this.resultInventory = resultInventory;
    }

    public IInventory getResultInventory() {
        return resultInventory;
    }

    public IInventory getMatrixInventory() {
        return inventory;
    }

    @Override
    public int getSize() {
        return getResultInventory().getInventoryStackLimit() + getMatrixInventory().getInventoryStackLimit();
    }

    @Override
    public void setContents(ItemStack[] items) {
        int resultLen = getResultInventory().getContents().length;
        int len = getMatrixInventory().getContents().length + resultLen;
        if (len > items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + len + " or less");
        }
        setContents(items[0], Java15Compat.Arrays_copyOfRange(items, 1, items.length));
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] items = new ItemStack[getSize()];
        net.minecraft.src.ItemStack[] mcResultItems = getResultInventory().getContents();

        int i = 0;
        for (i = 0; i < mcResultItems.length; i++ ) {
            items[i] = new CraftItemStack(mcResultItems[i]);
        }

        net.minecraft.src.ItemStack[] mcItems = getMatrixInventory().getContents();

        for (int j = 0; j < mcItems.length; j++) {
            items[i + j] = new CraftItemStack(mcItems[j]);
        }

        return items;
    }

    public void setContents(ItemStack result, ItemStack[] contents) {
        setResult(result);
        setMatrix(contents);
    }

    @Override
    public CraftItemStack getItem(int index) {
        if (index < getResultInventory().getInventoryStackLimit()) {
            net.minecraft.src.ItemStack item = getResultInventory().getStackInSlot(index);
            return item == null ? null : new CraftItemStack(item);
        } else {
            net.minecraft.src.ItemStack item = getMatrixInventory().getStackInSlot(index - getResultInventory().getInventoryStackLimit());
            return item == null ? null : new CraftItemStack(item);
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        if (index < getResultInventory().getInventoryStackLimit()) {
            getResultInventory().setInventorySlotContents(index, (item == null ? null : CraftItemStack.createNMSItemStack(item)));
        } else {
            getMatrixInventory().setInventorySlotContents((index - getResultInventory().getInventoryStackLimit()), (item == null ? null : CraftItemStack.createNMSItemStack(item)));
        }
    }

    public ItemStack[] getMatrix() {
        ItemStack[] items = new ItemStack[getSize()];
        net.minecraft.src.ItemStack[] matrix = getMatrixInventory().getContents();

        for (int i = 0; i < matrix.length; i++ ) {
            items[i] = new CraftItemStack(matrix[i]);
        }

        return items;
    }

    public ItemStack getResult() {
        net.minecraft.src.ItemStack item = getResultInventory().getStackInSlot(0);
        if(item != null) return new CraftItemStack(item);
        return null;
    }

    public void setMatrix(ItemStack[] contents) {
        if (getMatrixInventory().getContents().length > contents.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getMatrixInventory().getContents().length + " or less");
        }

        net.minecraft.src.ItemStack[] mcItems = getMatrixInventory().getContents();

        for (int i = 0; i < mcItems.length; i++ ) {
            if (i < contents.length) {
                ItemStack item = contents[i];
                if (item == null || item.getTypeId() <= 0) {
                    mcItems[i] = null;
                } else {
                    mcItems[i] = CraftItemStack.createNMSItemStack(item);
                }
            } else {
                mcItems[i] = null;
            }
        }
    }

    public void setResult(ItemStack item) {
        net.minecraft.src.ItemStack[] contents = getResultInventory().getContents();
        if (item == null || item.getTypeId() <= 0) {
            contents[0] = null;
        } else {
            contents[0] = CraftItemStack.createNMSItemStack(item);
        }
    }

    public Recipe getRecipe() {
        IRecipe recipe = ((InventoryCrafting)getInventory()).currentRecipe;
        return recipe == null ? null : recipe.toBukkitRecipe();
    }
}