/**
 * This software is provided under the terms of the Minecraft Forge Public
 * License v1.0.
 */

package forge;

import net.minecraft.server.ItemStack;
import net.minecraft.server.EntityHuman;

public interface IDestroyToolHandler
{
    /** Called when the user's currently equipped item is destroyed.
     */
    public void onDestroyCurrentItem(EntityHuman player, ItemStack orig);
}

