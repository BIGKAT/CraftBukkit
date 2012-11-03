package net.minecraft.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FurnaceRecipes {

    private static final FurnaceRecipes a = new FurnaceRecipes();
    public Map recipies = new HashMap(); // CraftBukkit - private -> public
    private Map metaSmeltingList = new HashMap();

    public static final FurnaceRecipes getInstance() {
        return a;
    }

    public FurnaceRecipes() { // CraftBukkit - private -> public
        this.registerRecipe(Block.IRON_ORE.id, new ItemStack(Item.IRON_INGOT));
        this.registerRecipe(Block.GOLD_ORE.id, new ItemStack(Item.GOLD_INGOT));
        this.registerRecipe(Block.DIAMOND_ORE.id, new ItemStack(Item.DIAMOND));
        this.registerRecipe(Block.SAND.id, new ItemStack(Block.GLASS));
        this.registerRecipe(Item.PORK.id, new ItemStack(Item.GRILLED_PORK));
        this.registerRecipe(Item.RAW_BEEF.id, new ItemStack(Item.COOKED_BEEF));
        this.registerRecipe(Item.RAW_CHICKEN.id, new ItemStack(Item.COOKED_CHICKEN));
        this.registerRecipe(Item.RAW_FISH.id, new ItemStack(Item.COOKED_FISH));
        this.registerRecipe(Block.COBBLESTONE.id, new ItemStack(Block.STONE));
        this.registerRecipe(Item.CLAY_BALL.id, new ItemStack(Item.CLAY_BRICK));
        this.registerRecipe(Block.CACTUS.id, new ItemStack(Item.INK_SACK, 1, 2));
        this.registerRecipe(Block.LOG.id, new ItemStack(Item.COAL, 1, 1));
        this.registerRecipe(Block.COAL_ORE.id, new ItemStack(Item.COAL));
        this.registerRecipe(Block.REDSTONE_ORE.id, new ItemStack(Item.REDSTONE));
        this.registerRecipe(Block.LAPIS_ORE.id, new ItemStack(Item.INK_SACK, 1, 4));
    }

    public void registerRecipe(int i, ItemStack itemstack) {
        this.recipies.put(Integer.valueOf(i), itemstack);
    }

    @Deprecated
    public ItemStack getResult(int i) {
        return (ItemStack) this.recipies.get(Integer.valueOf(i));
    }

    public Map getRecipies() {
        return this.recipies;
    }

	/**
	 * Add a metadata-sensitive furnace recipe
	 * 
	 * @param itemID
	 *            The Item ID
	 * @param metadata
	 *            The Item Metadata
	 * @param itemstack
	 *            The ItemStack for the result
	 */
	public void addSmelting(int itemID, int metadata, ItemStack itemstack) {
		metaSmeltingList.put(Arrays.asList(itemID, metadata), itemstack);
	}

	/**
	 * Used to get the resulting ItemStack form a source ItemStack
	 * 
	 * @param item
	 *            The Source ItemStack
	 * @return The result ItemStack
	 */
	public ItemStack getSmeltingResult(ItemStack item) {
		if (item == null) {
			return null;
		}
		ItemStack ret = (ItemStack) metaSmeltingList.get(Arrays.asList(item.id, item.getData()));
		if (ret != null) {
			return ret;
		}
		return (ItemStack) recipies.get(Integer.valueOf(item.id));
    }
}
