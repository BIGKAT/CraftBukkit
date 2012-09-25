package net.minecraft.src;

import net.minecraft.server.*;

public interface IRecipe {

    boolean a(InventoryCrafting inventorycrafting);

    net.minecraft.src.ItemStack b(InventoryCrafting inventorycrafting);

    int a();

    net.minecraft.src.ItemStack b();

    org.bukkit.inventory.Recipe toBukkitRecipe(); // CraftBukkit
}
