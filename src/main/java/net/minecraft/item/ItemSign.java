package net.minecraft.item;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemSign extends Item
{
    public ItemSign(int par1)
    {
        super(par1);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par7 == 0)
        {
            return false;
        }
        else if (!par3World.getBlockMaterial(par4, par5, par6).isSolid())
        {
            return false;
        }
        else
        {
            int var11 = par4, var12 = par5, clickedZ = par6; // CraftBukkit

            if (par7 == 1)
            {
                ++par5;
            }

            if (par7 == 2)
            {
                --par6;
            }

            if (par7 == 3)
            {
                ++par6;
            }

            if (par7 == 4)
            {
                --par4;
            }

            if (par7 == 5)
            {
                ++par4;
            }

            if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
            {
                return false;
            }
            else if (!Block.signPost.canPlaceBlockAt(par3World, par4, par5, par6))
            {
                return false;
            }
            else
            {
                CraftBlockState blockState = CraftBlockState.getBlockState(par3World, par4, par5, par6); // CraftBukkit

                if (par7 == 1)
                {
                    int i1 = MathHelper.floor_double((double)((par2EntityPlayer.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
                    // CraftBukkit start - sign
                    par3World.setBlockAndMetadata(par4, par5, par6, Block.signPost.blockID, i1);
                }
                else
                {
                    par3World.setBlockAndMetadata(par4, par5, par6, Block.signWall.blockID, par7);
                }

                org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(par3World, par2EntityPlayer, blockState, var11, var12, clickedZ);

                if (event.isCancelled() || !event.canBuild())
                {
                    event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
                    return false;
                }
                else
                {
                    if (par7 == 1)
                    {
                        par3World.notifyBlockChange(par4, par5, par6, Block.signPost.blockID);
                    }
                    else
                    {
                        par3World.notifyBlockChange(par4, par5, par6, Block.signWall.blockID);
                    }
                }

                // CraftBukkit end
                --par1ItemStack.stackSize;
                TileEntitySign tileentitysign = (TileEntitySign) par3World.getBlockTileEntity(par4, par5, par6);

                if (tileentitysign != null)
                {
                    par2EntityPlayer.displayGUIEditSign((TileEntity) tileentitysign);
                }

                return true;
            }
        }
    }
}
