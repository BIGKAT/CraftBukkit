package forge.oredict;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.inventory.Recipe;

import net.minecraft.server.Block;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.CraftingRecipe;
import net.minecraft.server.InventoryCrafting;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ShapelessRecipes;

public class ShapelessOreRecipe implements CraftingRecipe 
{
    private ItemStack output = null;
    private ArrayList input = new ArrayList();    

    public ShapelessOreRecipe(Block result, Object... recipe){ this(new ItemStack(result), recipe); }
    public ShapelessOreRecipe(Item  result, Object... recipe){ this(new ItemStack(result), recipe); }
    
    public ShapelessOreRecipe(ItemStack result, Object... recipe)
    {
        output = result.cloneItemStack();
        for (Object in : recipe)
        {
            if (in instanceof ItemStack)
            {
                input.add(((ItemStack)in).cloneItemStack());
            }
            else if (in instanceof Item)
            {
                input.add(new ItemStack((Item)in));
            }
            else if (in instanceof Block)
            {
                input.add(new ItemStack((Block)in));
            }
            else if (in instanceof String)
            {
                input.add(OreDictionary.getOres((String)in));
            }
            else
            {
                String ret = "Invalid shapeless ore recipe: ";
                for (Object tmp :  recipe)
                {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }
    }

    @Override
    public int a(){ return input.size(); }

    @Override
    public ItemStack b(){ return output; }
    
    @Override
    public ItemStack b(InventoryCrafting var1){ return output.cloneItemStack(); }
    
    @Override
    public boolean a(InventoryCrafting var1) 
    {
        ArrayList required = new ArrayList(input);

        for (int x = 0; x < var1.getSize(); x++)
        {
            ItemStack slot = var1.getItem(x);

            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;
                    
                    Object next = req.next();
                    
                    if (next instanceof ItemStack)
                    {
                        match = checkItemEquals((ItemStack)next, slot);
                    }
                    else if (next instanceof ArrayList)
                    {
                        for (ItemStack item : (ArrayList<ItemStack>)next)
                        {
                            match = match || checkItemEquals(item, slot);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }
    
    private boolean checkItemEquals(ItemStack target, ItemStack input)
    {
        return (target.id == input.id && (target.getData() == -1 || target.getData() == input.getData()));
    }
    
	@Override
	public Recipe toBukkitRecipe() {
		// TODO Auto-generated method stub
		return null;
	}
}
