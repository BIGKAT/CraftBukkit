/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */

package forge;

import net.minecraft.server.IInventory;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;

public interface ICraftingHandler
{
    /**
     * Called after an item is taken from crafting.
     */
    public void onTakenFromCrafting(EntityHuman player, ItemStack stack, IInventory craftMatrix);
}
