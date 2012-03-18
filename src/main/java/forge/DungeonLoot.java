package forge;

import java.util.Random;
import net.minecraft.server.ItemStack;

/**
 *
 * Used to hold a list of all items that can be spawned in a world dungeon
 *
 */
public class DungeonLoot
{
    private ItemStack item;
    private int minCount = 1;
    private int maxCount = 1;

    /**
     * @param item A item stack
     * @param min Minimum stack size when randomly generating
     * @param max Maximum stack size when randomly generating
     */
    public DungeonLoot(ItemStack item, int min, int max)
    {
        this.item = item;
        minCount = min;
        maxCount = max;
    }

    /**
     * Grabs a ItemStack ready to be added to the dungeon chest,
     * the stack size will be between minCount and maxCount
     * @param rand World gen random number generator
     * @return The ItemStack to be added to the chest
     */
    public ItemStack generateStack(Random rand)
    {
        ItemStack ret = this.item.cloneItemStack();
        ret.count = minCount + (rand.nextInt(maxCount - minCount + 1));
        return ret;
    }

    public boolean equals(ItemStack item, int min, int max)
    {
        return (min == minCount && max == maxCount && item.c(this.item));
    }

    public boolean equals(ItemStack item)
    {
        return item.c(this.item);
    }
}
