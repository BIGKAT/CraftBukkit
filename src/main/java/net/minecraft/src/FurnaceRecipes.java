package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.Block;
import net.minecraft.server.Item;

public class FurnaceRecipes {

    private static final FurnaceRecipes a = new FurnaceRecipes();
    public Map recipes = new HashMap(); // CraftBukkit - private -> public
    private Map c = new HashMap();

    public static final FurnaceRecipes getInstance() {
        return a;
    }

    public FurnaceRecipes() { // CraftBukkit - private -> public
        this.registerRecipe(Block.IRON_ORE.blockID, new net.minecraft.src.ItemStack(Item.IRON_INGOT), 0.7F);
        this.registerRecipe(Block.GOLD_ORE.blockID, new net.minecraft.src.ItemStack(Item.GOLD_INGOT), 1.0F);
        this.registerRecipe(Block.DIAMOND_ORE.blockID, new net.minecraft.src.ItemStack(Item.DIAMOND), 1.0F);
        this.registerRecipe(Block.SAND.blockID, new net.minecraft.src.ItemStack(Block.GLASS), 0.1F);
        this.registerRecipe(Item.PORK.id, new net.minecraft.src.ItemStack(Item.GRILLED_PORK), 0.3F);
        this.registerRecipe(Item.RAW_BEEF.id, new net.minecraft.src.ItemStack(Item.COOKED_BEEF), 0.3F);
        this.registerRecipe(Item.RAW_CHICKEN.id, new net.minecraft.src.ItemStack(Item.COOKED_CHICKEN), 0.3F);
        this.registerRecipe(Item.RAW_FISH.id, new net.minecraft.src.ItemStack(Item.COOKED_FISH), 0.3F);
        this.registerRecipe(Block.COBBLESTONE.blockID, new net.minecraft.src.ItemStack(Block.STONE), 0.1F);
        this.registerRecipe(Item.CLAY_BALL.id, new net.minecraft.src.ItemStack(Item.CLAY_BRICK), 0.2F);
        this.registerRecipe(Block.CACTUS.blockID, new net.minecraft.src.ItemStack(Item.INK_SACK, 1, 2), 0.2F);
        this.registerRecipe(Block.LOG.blockID, new net.minecraft.src.ItemStack(Item.COAL, 1, 1), 0.1F);
        this.registerRecipe(Block.EMERALD_ORE.blockID, new net.minecraft.src.ItemStack(Item.EMERALD), 1.0F);
        this.registerRecipe(Block.COAL_ORE.blockID, new net.minecraft.src.ItemStack(Item.COAL), 0.1F);
        this.registerRecipe(Block.REDSTONE_ORE.blockID, new net.minecraft.src.ItemStack(Item.REDSTONE), 0.7F);
        this.registerRecipe(Block.LAPIS_ORE.blockID, new net.minecraft.src.ItemStack(Item.INK_SACK, 1, 4), 0.2F);
    }

    public void registerRecipe(int i, net.minecraft.src.ItemStack itemstack, float f) {
        this.recipes.put(Integer.valueOf(i), itemstack);
        this.c.put(Integer.valueOf(itemstack.id), Float.valueOf(f));
    }

    public net.minecraft.src.ItemStack getResult(int i) {
        return (net.minecraft.src.ItemStack) this.recipes.get(Integer.valueOf(i));
    }

    public Map getRecipes() {
        return this.recipes;
    }

    public float c(int i) {
        return this.c.containsKey(Integer.valueOf(i)) ? ((Float) this.c.get(Integer.valueOf(i))).floatValue() : 0.0F;
    }
}
