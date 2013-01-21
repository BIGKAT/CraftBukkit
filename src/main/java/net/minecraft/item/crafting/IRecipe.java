package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
public interface IRecipe
{

    boolean matches(InventoryCrafting inventorycrafting, World world);

    ItemStack getCraftingResult(InventoryCrafting inventorycrafting);

    /**
     * Returns the size of the recipe area
     */
    int getRecipeSize();

    ItemStack getRecipeOutput();

    org.bukkit.inventory.Recipe toBukkitRecipe(); // CraftBukkit
}
