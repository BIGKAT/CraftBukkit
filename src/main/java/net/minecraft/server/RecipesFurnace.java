package net.minecraft.server;

import java.util.HashMap;
import java.util.Map;

public class RecipesFurnace {

    private static final RecipesFurnace a = new RecipesFurnace();
    public Map recipes = new HashMap(); // CraftBukkit - private -> public
    private Map c = new HashMap();

    public static final RecipesFurnace getInstance() {
        return a;
    }

    public RecipesFurnace() { // CraftBukkit - private -> public
        this.registerRecipe(Block.IRON_ORE.blockID, new ItemStack(Item.IRON_INGOT), 0.7F);
        this.registerRecipe(Block.GOLD_ORE.blockID, new ItemStack(Item.GOLD_INGOT), 1.0F);
        this.registerRecipe(Block.DIAMOND_ORE.blockID, new ItemStack(Item.DIAMOND), 1.0F);
        this.registerRecipe(Block.SAND.blockID, new ItemStack(Block.GLASS), 0.1F);
        this.registerRecipe(Item.PORK.id, new ItemStack(Item.GRILLED_PORK), 0.3F);
        this.registerRecipe(Item.RAW_BEEF.id, new ItemStack(Item.COOKED_BEEF), 0.3F);
        this.registerRecipe(Item.RAW_CHICKEN.id, new ItemStack(Item.COOKED_CHICKEN), 0.3F);
        this.registerRecipe(Item.RAW_FISH.id, new ItemStack(Item.COOKED_FISH), 0.3F);
        this.registerRecipe(Block.COBBLESTONE.blockID, new ItemStack(Block.STONE), 0.1F);
        this.registerRecipe(Item.CLAY_BALL.id, new ItemStack(Item.CLAY_BRICK), 0.2F);
        this.registerRecipe(Block.CACTUS.blockID, new ItemStack(Item.INK_SACK, 1, 2), 0.2F);
        this.registerRecipe(Block.LOG.blockID, new ItemStack(Item.COAL, 1, 1), 0.1F);
        this.registerRecipe(Block.EMERALD_ORE.blockID, new ItemStack(Item.EMERALD), 1.0F);
        this.registerRecipe(Block.COAL_ORE.blockID, new ItemStack(Item.COAL), 0.1F);
        this.registerRecipe(Block.REDSTONE_ORE.blockID, new ItemStack(Item.REDSTONE), 0.7F);
        this.registerRecipe(Block.LAPIS_ORE.blockID, new ItemStack(Item.INK_SACK, 1, 4), 0.2F);
    }

    public void registerRecipe(int i, ItemStack itemstack, float f) {
        this.recipes.put(Integer.valueOf(i), itemstack);
        this.c.put(Integer.valueOf(itemstack.id), Float.valueOf(f));
    }

    public ItemStack getResult(int i) {
        return (ItemStack) this.recipes.get(Integer.valueOf(i));
    }

    public Map getRecipes() {
        return this.recipes;
    }

    public float c(int i) {
        return this.c.containsKey(Integer.valueOf(i)) ? ((Float) this.c.get(Integer.valueOf(i))).floatValue() : 0.0F;
    }
}
