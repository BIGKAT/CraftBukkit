package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.*;
import net.minecraft.src.*;
import net.minecraft.src.IRecipe;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class CraftingManager {

    private static final CraftingManager a = new CraftingManager();
    public List recipes = new ArrayList(); // CraftBukkit - private -> public
    // CraftBukkit start
    public net.minecraft.src.IRecipe lastRecipe;
    public org.bukkit.inventory.InventoryView lastCraftView;
    // CraftBukkit end

    public static final CraftingManager getInstance() {
        return a;
    }

    public CraftingManager() { // CraftBukkit - private -> public
        (new RecipesTools()).a(this);
        (new RecipesWeapons()).a(this);
        (new RecipeIngots()).a(this);
        (new RecipesFood()).a(this);
        (new RecipesCrafting()).a(this);
        (new RecipesArmor()).a(this);
        (new RecipesDyes()).a(this);
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.PAPER, 3), new Object[] { "###", Character.valueOf('#'), Item.SUGAR_CANE});
        this.registerShapelessRecipe(new net.minecraft.src.ItemStack(Item.BOOK, 1), new Object[] { Item.PAPER, Item.PAPER, Item.PAPER, Item.LEATHER});
        this.registerShapelessRecipe(new net.minecraft.src.ItemStack(Item.BOOK_AND_QUILL, 1), new Object[] { Item.BOOK, new net.minecraft.src.ItemStack(Item.INK_SACK, 1, 0), Item.FEATHER});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.FENCE, 2), new Object[] { "###", "###", Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.NETHER_FENCE, 6), new Object[] { "###", "###", Character.valueOf('#'), Block.NETHER_BRICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.FENCE_GATE, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Item.STICK, Character.valueOf('W'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.jukebox, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.DIAMOND});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.NOTE_BLOCK, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.REDSTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.BOOKSHELF, 1), new Object[] { "###", "XXX", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.BOOK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.SNOW_BLOCK, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.SNOW_BALL});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.CLAY, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.CLAY_BALL});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.BRICK, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.CLAY_BRICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.GLOWSTONE, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.GLOWSTONE_DUST});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.cloth, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.STRING});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.TNT, 1), new Object[] { "X#X", "#X#", "X#X", Character.valueOf('X'), Item.SULPHUR, Character.valueOf('#'), Block.SAND});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.STEP, 6, 3), new Object[] { "###", Character.valueOf('#'), Block.COBBLESTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.STEP, 6, 0), new Object[] { "###", Character.valueOf('#'), Block.STONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.STEP, 6, 1), new Object[] { "###", Character.valueOf('#'), Block.SANDSTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.STEP, 6, 4), new Object[] { "###", Character.valueOf('#'), Block.BRICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.STEP, 6, 5), new Object[] { "###", Character.valueOf('#'), Block.SMOOTH_BRICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD_STEP, 6, 0), new Object[] { "###", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.WOOD, 1, 0)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD_STEP, 6, 2), new Object[] { "###", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.WOOD, 1, 2)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD_STEP, 6, 1), new Object[] { "###", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.WOOD, 1, 1)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD_STEP, 6, 3), new Object[] { "###", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.WOOD, 1, 3)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.LADDER, 3), new Object[] { "# #", "###", "# #", Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.WOOD_DOOR, 1), new Object[] { "##", "##", "##", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.TRAP_DOOR, 2), new Object[] { "###", "###", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.IRON_DOOR, 1), new Object[] { "##", "##", "##", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.SIGN, 3), new Object[] { "###", "###", " X ", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.STICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.CAKE, 1), new Object[] { "AAA", "BEB", "CCC", Character.valueOf('A'), Item.MILK_BUCKET, Character.valueOf('B'), Item.SUGAR, Character.valueOf('C'), Item.WHEAT, Character.valueOf('E'), Item.EGG});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.SUGAR, 1), new Object[] { "#", Character.valueOf('#'), Item.SUGAR_CANE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD, 4, 0), new Object[] { "#", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.LOG, 1, 0)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD, 4, 1), new Object[] { "#", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.LOG, 1, 1)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD, 4, 2), new Object[] { "#", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.LOG, 1, 2)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD, 4, 3), new Object[] { "#", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.LOG, 1, 3)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.STICK, 4), new Object[] { "#", "#", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.TORCH, 4), new Object[] { "X", "#", Character.valueOf('X'), Item.COAL, Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.TORCH, 4), new Object[] { "X", "#", Character.valueOf('X'), new net.minecraft.src.ItemStack(Item.COAL, 1, 1), Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.BOWL, 4), new Object[] { "# #", " # ", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.GLASS_BOTTLE, 3), new Object[] { "# #", " # ", Character.valueOf('#'), Block.GLASS});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.RAILS, 16), new Object[] { "X X", "X#X", "X X", Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.GOLDEN_RAIL, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Item.GOLD_INGOT, Character.valueOf('R'), Item.REDSTONE, Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.DETECTOR_RAIL, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('R'), Item.REDSTONE, Character.valueOf('#'), Block.STONE_PLATE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.MINECART, 1), new Object[] { "# #", "###", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.CAULDRON, 1), new Object[] { "# #", "# #", "###", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.BREWING_STAND, 1), new Object[] { " B ", "###", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('B'), Item.BLAZE_ROD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.JACK_O_LANTERN, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.PUMPKIN, Character.valueOf('B'), Block.TORCH});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.STORAGE_MINECART, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.CHEST, Character.valueOf('B'), Item.MINECART});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.POWERED_MINECART, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.FURNACE, Character.valueOf('B'), Item.MINECART});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.BOAT, 1), new Object[] { "# #", "###", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.BUCKET, 1), new Object[] { "# #", " # ", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.FLINT_AND_STEEL, 1), new Object[] { "A ", " B", Character.valueOf('A'), Item.IRON_INGOT, Character.valueOf('B'), Item.FLINT});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.BREAD, 1), new Object[] { "###", Character.valueOf('#'), Item.WHEAT});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.WOOD, 1, 0)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.BIRCH_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.WOOD, 1, 2)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.SPRUCE_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.WOOD, 1, 1)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.JUNGLE_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new net.minecraft.src.ItemStack(Block.WOOD, 1, 3)});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.FISHING_ROD, 1), new Object[] { "  #", " #X", "# X", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Item.STRING});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.COBBLESTONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.COBBLESTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.BRICK_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.BRICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.STONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.SMOOTH_BRICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.NETHER_BRICK_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.NETHER_BRICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.SANDSTONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.SANDSTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.PAINTING, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Block.cloth});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.GOLDEN_APPLE, 1, 0), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.GOLD_NUGGET, Character.valueOf('X'), Item.APPLE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.GOLDEN_APPLE, 1, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.GOLD_BLOCK, Character.valueOf('X'), Item.APPLE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.LEVER, 1), new Object[] { "X", "#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('X'), Item.STICK});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.TRIPWIRE_SOURCE, 2), new Object[] { "I", "S", "#", Character.valueOf('#'), Block.WOOD, Character.valueOf('S'), Item.STICK, Character.valueOf('I'), Item.IRON_INGOT});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.REDSTONE_TORCH_ON, 1), new Object[] { "X", "#", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Item.REDSTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.DIODE, 1), new Object[] { "#X#", "III", Character.valueOf('#'), Block.REDSTONE_TORCH_ON, Character.valueOf('X'), Item.REDSTONE, Character.valueOf('I'), Block.STONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.WATCH, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Item.GOLD_INGOT, Character.valueOf('X'), Item.REDSTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.COMPASS, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Item.IRON_INGOT, Character.valueOf('X'), Item.REDSTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.MAP, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.PAPER, Character.valueOf('X'), Item.COMPASS});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.STONE_BUTTON, 1), new Object[] { "#", "#", Character.valueOf('#'), Block.STONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.STONE_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Block.STONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.WOOD_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.dispenser, 1), new Object[] { "###", "#X#", "#R#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('X'), Item.BOW, Character.valueOf('R'), Item.REDSTONE});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.PISTON, 1), new Object[] { "TTT", "#X#", "#R#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('R'), Item.REDSTONE, Character.valueOf('T'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.PISTON_STICKY, 1), new Object[] { "S", "P", Character.valueOf('S'), Item.SLIME_BALL, Character.valueOf('P'), Block.PISTON});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Item.BED, 1), new Object[] { "###", "XXX", Character.valueOf('#'), Block.cloth, Character.valueOf('X'), Block.WOOD});
        this.registerShapedRecipe(new net.minecraft.src.ItemStack(Block.ENCHANTMENT_TABLE, 1), new Object[] { " B ", "D#D", "###", Character.valueOf('#'), Block.obsidian, Character.valueOf('B'), Item.BOOK, Character.valueOf('D'), Item.DIAMOND});
        this.registerShapelessRecipe(new net.minecraft.src.ItemStack(Item.EYE_OF_ENDER, 1), new Object[] { Item.ENDER_PEARL, Item.BLAZE_POWDER});
        this.registerShapelessRecipe(new net.minecraft.src.ItemStack(Item.FIREBALL, 3), new Object[] { Item.SULPHUR, Item.BLAZE_POWDER, Item.COAL});
        this.registerShapelessRecipe(new net.minecraft.src.ItemStack(Item.FIREBALL, 3), new Object[] { Item.SULPHUR, Item.BLAZE_POWDER, new net.minecraft.src.ItemStack(Item.COAL, 1, 1)});
        //Collections.sort(this.b, new RecipeSorter(this)); // CraftBukkit - removed; see below
        this.sort(); // CraftBukkit - moved sort to a separate method
        System.out.println(this.recipes.size() + " recipes");
    }

    // CraftBukkit start
    public void sort() {
        Collections.sort(this.recipes, new RecipeSorter(this));
    }
    // CraftBukkit end

    public void registerShapedRecipe(net.minecraft.src.ItemStack itemstack, Object... aobject) { // CraftBukkit - default -> public
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;
        int l;

        if (aobject[i] instanceof String[]) {
            String[] astring = (String[]) ((String[]) aobject[i++]);
            String[] astring1 = astring;

            l = astring.length;

            for (int i1 = 0; i1 < l; ++i1) {
                String s1 = astring1[i1];

                ++k;
                j = s1.length();
                s = s + s1;
            }
        } else {
            while (aobject[i] instanceof String) {
                String s2 = (String) aobject[i++];

                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < aobject.length; i += 2) {
            Character character = (Character) aobject[i];
            net.minecraft.src.ItemStack itemstack1 = null;

            if (aobject[i + 1] instanceof Item) {
                itemstack1 = new net.minecraft.src.ItemStack((Item) aobject[i + 1]);
            } else if (aobject[i + 1] instanceof Block) {
                itemstack1 = new net.minecraft.src.ItemStack((Block) aobject[i + 1], 1, -1);
            } else if (aobject[i + 1] instanceof net.minecraft.src.ItemStack) {
                itemstack1 = (net.minecraft.src.ItemStack) aobject[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        net.minecraft.src.ItemStack[] aitemstack = new net.minecraft.src.ItemStack[j * k];

        for (l = 0; l < j * k; ++l) {
            char c0 = s.charAt(l);

            if (hashmap.containsKey(Character.valueOf(c0))) {
                aitemstack[l] = ((net.minecraft.src.ItemStack) hashmap.get(Character.valueOf(c0))).cloneItemStack();
            } else {
                aitemstack[l] = null;
            }
        }

        this.recipes.add(new net.minecraft.server.ShapedRecipes(j, k, aitemstack, itemstack));
    }

    public void registerShapelessRecipe(net.minecraft.src.ItemStack itemstack, Object... aobject) { // CraftBukkit - default -> public
        ArrayList arraylist = new ArrayList();
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object instanceof net.minecraft.src.ItemStack) {
                arraylist.add(((net.minecraft.src.ItemStack) object).cloneItemStack());
            } else if (object instanceof Item) {
                arraylist.add(new net.minecraft.src.ItemStack((Item) object));
            } else {
                if (!(object instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                arraylist.add(new net.minecraft.src.ItemStack((Block) object));
            }
        }

        this.recipes.add(new net.minecraft.src.ShapelessRecipes(itemstack, arraylist));
    }

    public net.minecraft.src.ItemStack craft(net.minecraft.src.InventoryCrafting inventorycrafting) {
        int i = 0;
        net.minecraft.src.ItemStack itemstack = null;
        net.minecraft.src.ItemStack itemstack1 = null;

        for (int j = 0; j < inventorycrafting.getSize(); ++j) {
            net.minecraft.src.ItemStack itemstack2 = inventorycrafting.getItem(j);

            if (itemstack2 != null) {
                if (i == 0) {
                    itemstack = itemstack2;
                }

                if (i == 1) {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.id == itemstack1.id && itemstack.count == 1 && itemstack1.count == 1 && Item.itemsList[itemstack.id].m()) {
            Item item = Item.itemsList[itemstack.id];
            int k = item.getMaxDurability() - itemstack.i();
            int l = item.getMaxDurability() - itemstack1.i();
            int i1 = k + l + item.getMaxDurability() * 10 / 100;
            int j1 = item.getMaxDurability() - i1;

            if (j1 < 0) {
                j1 = 0;
            }

            // CraftBukkit start - construct a dummy repair recipe
            net.minecraft.src.ItemStack result = new net.minecraft.src.ItemStack(itemstack.id, 1, j1);
            List<net.minecraft.src.ItemStack> ingredients = new ArrayList<net.minecraft.src.ItemStack>();
            ingredients.add(itemstack.cloneItemStack());
            ingredients.add(itemstack1.cloneItemStack());
            net.minecraft.src.ShapelessRecipes recipe = new net.minecraft.src.ShapelessRecipes(result.cloneItemStack(), ingredients);
            inventorycrafting.currentRecipe = recipe;
            result = CraftEventFactory.callPreCraftEvent(inventorycrafting, result, lastCraftView, true);
            return result;
            // CraftBukkit end
        } else {
            Iterator iterator = this.recipes.iterator();

            net.minecraft.src.IRecipe irecipe;

            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                irecipe = (IRecipe) iterator.next();
            } while (!irecipe.a(inventorycrafting));

            // CraftBukkit start - INVENTORY_PRE_CRAFT event
            inventorycrafting.currentRecipe = irecipe;
            net.minecraft.src.ItemStack result = irecipe.b(inventorycrafting);
            return CraftEventFactory.callPreCraftEvent(inventorycrafting, result, lastCraftView, false);
            // CraftBukkit end
        }
    }

    public List getRecipes() {
        return this.recipes;
    }
}
