package forge.oredict;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.Recipe;

import net.minecraft.server.Block;
import net.minecraft.server.CraftingRecipe;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ShapedRecipes;

public class ShapedOreRecipe implements CraftingRecipe 
{
    //Added in for future ease of change, but hard coded for now.
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;
    
    private ItemStack output = null;
    private Object[] input = null;
    private int width = 0;
    private int height = 0;
    private boolean mirriored = true;

    public ShapedOreRecipe(Block     result, Object... recipe){ this(result, true, recipe);}
    public ShapedOreRecipe(Item      result, Object... recipe){ this(result, true, recipe); }
    public ShapedOreRecipe(ItemStack result, Object... recipe){ this(result, true, recipe); }
    public ShapedOreRecipe(Block     result, boolean mirrior, Object... recipe){ this(new ItemStack(result), mirrior, recipe);}
    public ShapedOreRecipe(Item      result, boolean mirrior, Object... recipe){ this(new ItemStack(result), mirrior, recipe); }
    
    public ShapedOreRecipe(ItemStack result, boolean mirrior, Object... recipe)
    {
        output = result.cloneItemStack();
        mirriored = mirrior;
        
        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof String[])
        {
            String[] parts = ((String[])recipe[idx++]);

            for (String s : parts)
            {
                width = s.length();
                shape += s;
            }
            
            height = parts.length;
        }
        else
        {
            while (recipe[idx] instanceof String)
            {
                String s = (String)recipe[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }
        
        if (width * height != shape.length())
        {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp :  recipe)
            {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

        for (; idx < recipe.length; idx += 2)
        {
            Character chr = (Character)recipe[idx];
            Object in = recipe[idx + 1];
            Object val = null;

            if (in instanceof ItemStack)
            {
                itemMap.put(chr, ((ItemStack)in).cloneItemStack());
            }
            else if (in instanceof Item)
            {
                itemMap.put(chr, new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                itemMap.put(chr, new ItemStack((Block)in, 1, -1));
            }
            else if (in instanceof String)
            {
                itemMap.put(chr, OreDictionary.getOres((String)in));
            }
            else
            {
                String ret = "Invalid shaped ore recipe: ";
                for (Object tmp :  recipe)
                {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }

        input = new Object[width * height];
        int x = 0;
        for (char chr : shape.toCharArray())
        {
            input[x++] = itemMap.get(chr);   
        }
    }

    @Override
    public ItemStack b(InventoryCrafting var1){ return output.cloneItemStack(); }

    @Override
    public int a(){ return input.length; }

    @Override
    public ItemStack b(){ return output; }

    @Override
    public boolean a(InventoryCrafting inv)
    {        
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
                if (checkMatch(inv, x, y, true))
                {
                    return true;
                }
    
                if (mirriored && checkMatch(inv, x, y, false))
                {
                    return true;
                }
            }
        }
    
        return false;
    }
    
    private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirrior)
    {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirrior)
                    {
                        target = input[width - subX - 1 + subY * width];
                    }
                    else
                    {
                        target = input[subX + subY * width];
                    }
                }

                ItemStack slot = inv.b(x, y);
                
                if (target instanceof ItemStack)
                {
                    if (!checkItemEquals((ItemStack)target, slot))
                    {
                        return false;
                    }
                }
                else if (target instanceof ArrayList)
                {
                    boolean matched = false;
                    
                    for (ItemStack item : (ArrayList<ItemStack>)target)
                    {
                        matched = matched || checkItemEquals(item, slot);
                    }
                    
                    if (!matched)
                    {
                        return false;
                    }
                }
                else if (target == null && slot != null)
                {
                    return false;
                }
            }
        }

        return true;
    }
    
    private boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        if (input == null && target != null || input != null && target == null)
        {
            return false;
        }
        return (target.id == input.id && (target.getData() == -1 || target.getData() == input.getData()));
    }
    
    public void setMirriored(boolean mirrior)
    {
        mirriored = mirrior;
    }

    @Override
	public Recipe toBukkitRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
