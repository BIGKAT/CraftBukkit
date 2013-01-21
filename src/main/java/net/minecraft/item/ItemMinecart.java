package net.minecraft.item;

import net.minecraft.block.BlockRail;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
public class ItemMinecart extends Item
{
    public int minecartType;

    public ItemMinecart(int par1, int par2)
    {
        super(par1);
        this.maxStackSize = 1;
        this.minecartType = par2;
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int var11 = par3World.getBlockId(par4, par5, par6);

        if (BlockRail.isRailBlock(var11))
        {
            if (!par3World.isRemote)
            {
                // CraftBukkit start - Minecarts
                org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent(par2EntityPlayer, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, par4, par5, par6, par7, par1ItemStack);

                if (event.isCancelled())
                {
                    return false;
                }

                // CraftBukkit end
                par3World.spawnEntityInWorld(new EntityMinecart(par3World, (double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), this.minecartType));
            }

            --par1ItemStack.stackSize;
            return true;
        }
        else
        {
            return false;
        }
    }
}
