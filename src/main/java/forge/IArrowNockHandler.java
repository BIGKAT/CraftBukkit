/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */
package forge;

import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import net.minecraft.server.EntityHuman;

public interface IArrowNockHandler
{

    /**
     * This is called before a player tries to load an arrow. If it returns
     * a non-null result, then the normal arrow will not be loaded and the
     * bow will be changed to the returned value.
     *
     * @param itemstack The ItemStack for the bow doing the firing
     * @param world The current world
     * @param player The player that is using the bow
     * @return The new bow item, or null to continue normally.
     */
    public ItemStack onArrowNock(ItemStack itemstack, World world, EntityHuman player);

}