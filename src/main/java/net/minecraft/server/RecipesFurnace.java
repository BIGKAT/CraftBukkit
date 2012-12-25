package net.minecraft.server;

import java.util.HashMap;
import java.util.Map;
// Forge start
import java.util.Arrays;
import java.util.List;
// Forge end

public class RecipesFurnace {

    private static final RecipesFurnace a = new RecipesFurnace();
    public Map recipes = new HashMap(); // CraftBukkit - private -> public
    private Map c = new HashMap();
    private HashMap<List<Integer>, ItemStack> metaSmeltingList = new HashMap<List<Integer>, ItemStack>(); // Forge
    private HashMap<List<Integer>, Float> metaExperience = new HashMap<List<Integer>, Float>(); // Forge

    public static final RecipesFurnace getInstance() {
        return a;
    }

    public RecipesFurnace() { // CraftBukkit - private -> public
        this.registerRecipe(Block.IRON_ORE.id, new ItemStack(Item.IRON_INGOT), 0.7F);
        this.registerRecipe(Block.GOLD_ORE.id, new ItemStack(Item.GOLD_INGOT), 1.0F);
        this.registerRecipe(Block.DIAMOND_ORE.id, new ItemStack(Item.DIAMOND), 1.0F);
        this.registerRecipe(Block.SAND.id, new ItemStack(Block.GLASS), 0.1F);
        this.registerRecipe(Item.PORK.id, new ItemStack(Item.GRILLED_PORK), 0.35F);
        this.registerRecipe(Item.RAW_BEEF.id, new ItemStack(Item.COOKED_BEEF), 0.35F);
        this.registerRecipe(Item.RAW_CHICKEN.id, new ItemStack(Item.COOKED_CHICKEN), 0.35F);
        this.registerRecipe(Item.RAW_FISH.id, new ItemStack(Item.COOKED_FISH), 0.35F);
        this.registerRecipe(Block.COBBLESTONE.id, new ItemStack(Block.STONE), 0.1F);
        this.registerRecipe(Item.CLAY_BALL.id, new ItemStack(Item.CLAY_BRICK), 0.3F);
        this.registerRecipe(Block.CACTUS.id, new ItemStack(Item.INK_SACK, 1, 2), 0.2F);
        this.registerRecipe(Block.LOG.id, new ItemStack(Item.COAL, 1, 1), 0.15F);
        this.registerRecipe(Block.EMERALD_ORE.id, new ItemStack(Item.EMERALD), 1.0F);
        this.registerRecipe(Item.POTATO.id, new ItemStack(Item.POTATO_BAKED), 0.35F);
        this.registerRecipe(Block.COAL_ORE.id, new ItemStack(Item.COAL), 0.1F);
        this.registerRecipe(Block.REDSTONE_ORE.id, new ItemStack(Item.REDSTONE), 0.7F);
        this.registerRecipe(Block.LAPIS_ORE.id, new ItemStack(Item.INK_SACK, 1, 4), 0.2F);
    }

    public void registerRecipe(int i, ItemStack itemstack, float f) {
        this.recipes.put(Integer.valueOf(i), itemstack);
        this.c.put(Integer.valueOf(itemstack.id), Float.valueOf(f));
    }

    @Deprecated // Forge
    public ItemStack getResult(int i) {
        return (ItemStack) this.recipes.get(Integer.valueOf(i));
    }

    public Map getRecipes() {
        return this.recipes;
    }

    @Deprecated // Forge - In favor of ItemStack sensitive version
    public float c(int i) {
        return this.c.containsKey(Integer.valueOf(i)) ? ((Float) this.c.get(Integer.valueOf(i))).floatValue() : 0.0F;
    }
    
    // Forge start   
    /**
     * A metadata sensitive version of adding a furnace recipe.
     */
    public void addSmelting(int itemID, int metadata, ItemStack itemstack, float experience)
    {
        metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
        metaExperience.put(Arrays.asList(itemID, metadata), experience);
    }
     
    /**
     * Used to get the resulting ItemStack form a source ItemStack
     * @param item The Source ItemStack
     * @return The result ItemStack
     */
    public ItemStack getSmeltingResult(ItemStack item) 
    {
        if (item == null)
        {
            return null;
        }
        ItemStack ret = (ItemStack)metaSmeltingList.get(Arrays.asList(item.id, item.getData()));
        if (ret != null) 
        {
            return ret;
        }
        return (ItemStack)recipes.get(Integer.valueOf(item.id));
    }
     
    /**
     * Grabs the amount of base experience for this item to give when pulled from the furnace slot.
     */
    public float getExperience(ItemStack item)
    {
        if (item == null || item.getItem() == null)
        {
            return 0;
        }
        float ret = item.getItem().getSmeltingExperience(item);
        if (ret < 0 && metaExperience.containsKey(Arrays.asList(item.id, item.getData())))
        {
            ret = metaExperience.get(Arrays.asList(item.id, item.getData()));
        }
        if (ret < 0 && c.containsKey(item.id))
        {
            ret = ((Float)c.get(item.id)).floatValue();
        }
        return (ret < 0 ? 0 : ret);
   }
    // Forge end
}
