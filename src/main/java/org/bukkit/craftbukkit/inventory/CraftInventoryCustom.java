package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class CraftInventoryCustom extends CraftInventory {
    public CraftInventoryCustom(InventoryHolder owner, InventoryType type) {
        super(new MinecraftInventory(owner, type));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size) {
        super(new MinecraftInventory(owner, size));
    }

    public CraftInventoryCustom(InventoryHolder owner, int size, String title) {
        super(new MinecraftInventory(owner, size, title));
    }

    static class MinecraftInventory implements IInventory {
        private ItemStack[] items;
        private int maxStack = MAX_STACK;
        private List<HumanEntity> viewers;
        private String title;
        private InventoryType type;
        private InventoryHolder owner;

        public MinecraftInventory(InventoryHolder owner, InventoryType type) {
            this(owner, type.getDefaultSize(), type.getDefaultTitle());
            this.type = type;
        }

        public MinecraftInventory(InventoryHolder owner, int size) {
            this(owner, size, "Chest");
        }

        public MinecraftInventory(InventoryHolder owner, int size, String title) {
            this.items = new ItemStack[size];
            this.title = title;
            this.viewers = new ArrayList<HumanEntity>();
            this.owner = owner;
            this.type = InventoryType.CHEST;
        }

        public int getSize() {
            return items.length;
        }

        public ItemStack getItem(int i) {
            return items[i];
        }

        public ItemStack splitStack(int i, int j) {
            ItemStack stack = this.getItem(i);
            ItemStack result;
            if (stack == null) return null;
            if (stack.stackSize <= j) {
                this.setItem(i, null);
                result = stack;
            } else {
                result = new ItemStack(stack.itemID, j, stack.getItemDamage(), stack.getEnchantmentTagList());
                stack.stackSize -= j;
            }
            this.update();
            return result;
        }

        public ItemStack splitWithoutUpdate(int i) {
            ItemStack stack = this.getItem(i);
            ItemStack result;
            if (stack == null) return null;
            if (stack.stackSize <= 1) {
                this.setItem(i, null);
                result = stack;
            } else {
                result = new ItemStack(stack.itemID, 1, stack.getItemDamage(), stack.getEnchantmentTagList());
                stack.stackSize -= 1;
            }
            return result;
        }

        public void setItem(int i, ItemStack itemstack) {
            items[i] = itemstack;
            if (itemstack != null && this.getMaxStackSize() > 0 && itemstack.stackSize > this.getMaxStackSize()) {
                itemstack.stackSize = this.getMaxStackSize();
            }
        }

        public String getName() {
            return title;
        }

        public int getMaxStackSize() {
            return maxStack;
        }

        public void setMaxStackSize(int size) {
            maxStack = size;
        }

        public void update() {}

        public boolean a(EntityPlayer entityhuman) {
            return true;
        }

        public ItemStack[] getContents() {
            return items;
        }

        public void onOpen(CraftHumanEntity who) {
            viewers.add(who);
        }

        public void onClose(CraftHumanEntity who) {
            viewers.remove(who);
        }

        public List<HumanEntity> getViewers() {
            return viewers;
        }

        public InventoryType getType() {
            return type;
        }

        public void f() {}

        public void g() {}

        public InventoryHolder getOwner() {
            return owner;
        }

        public void startOpen() {}
    }
}
