package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
public class ItemBlock extends Item
{
    /** The block ID of the Block associated with this ItemBlock */
    private int blockID;

    public ItemBlock(int par1)
    {
        super(par1);
        this.blockID = par1 + 256;
        this.setIconIndex(Block.blocksList[par1 + 256].getBlockTextureFromSide(2));
    }

    /**
     * Returns the blockID for this Item
     */
    public int getBlockID()
    {
        return this.blockID;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int var11 = par3World.getBlockId(par4, par5, par6);

        if (var11 == Block.snow.blockID)
        {
            par7 = 1;
        }
        else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID)
        {
            if (par7 == 0)
            {
                --par5;
            }

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
        }

        if (par1ItemStack.stackSize == 0)
        {
            return false;
        }
        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
        {
            return false;
        }
        else if (par5 == 255 && Block.blocksList[this.blockID].blockMaterial.isSolid())
        {
            return false;
        }
        else if (par3World.canPlaceEntityOnSide(this.blockID, par4, par5, par6, false, par7, par2EntityPlayer))
        {
            Block var12 = Block.blocksList[this.blockID];
            int var13 = this.getMetadata(par1ItemStack.getItemDamage());
            int var14 = Block.blocksList[this.blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, var13);
            // CraftBukkit start - redirect to common function handler
            /*
            if (world.setTypeIdAndData(i, j, k, this.id, k1)) {
                if (world.getTypeId(i, j, k) == this.id) {
                    Block.byId[this.id].postPlace(world, i, j, k, entityhuman);
                    Block.byId[this.id].postPlace(world, i, j, k, k1);
                }

                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                --itemstack.count;
            }
            */
            return processBlockPlace(par3World, par2EntityPlayer, par1ItemStack, par4, par5, par6, this.blockID, var14);
            // CraftBukkit end
        }
        else
        {
            return false;
        }
    }

    // CraftBukkit start - add method to process block placement
    static boolean processBlockPlace(final World world, final EntityPlayer entityhuman, final ItemStack itemstack, final int x, final int y, final int z, final int id, final int data)
    {
        org.bukkit.block.BlockState blockstate = org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(world, x, y, z);
        world.editingBlocks = true;
        world.setBlockAndMetadata(x, y, z, id, data);
        org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockstate, x, y, z);

        if (event.isCancelled() || !event.canBuild())
        {
            blockstate.update(true);
            world.editingBlocks = false;
            return false;
        }

        world.editingBlocks = false;
        int newId = world.getBlockId(x, y, z);
        int newData = world.getBlockMetadata(x, y, z);
        Block block = Block.blocksList[newId];

        if (block != null)
        {
            block.onBlockAdded(world, x, y, z);
        }

        world.notifyBlockChange(x, y, z, newId);

        if (block != null)
        {
            block.onBlockPlacedBy(world, x, y, z, entityhuman);
            block.onPostBlockPlaced(world, x, y, z, newData);
            world.playSoundEffect((double)((float) x + 0.5F), (double)((float) y + 0.5F), (double)((float) z + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
        }

        if (itemstack != null)
        {
            --itemstack.stackSize;
        }

        return true;
    }
    // CraftBukkit end

    public String getItemNameIS(ItemStack par1ItemStack)
    {
        return Block.blocksList[this.blockID].getBlockName();
    }

    public String getItemName()
    {
        return Block.blocksList[this.blockID].getBlockName();
    }
}
