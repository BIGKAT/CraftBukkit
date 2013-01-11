package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockFadeEvent;
// CraftBukkit end

public class BlockGrass extends Block
{
    protected BlockGrass(int par1)
    {
        super(par1, Material.grass);
        this.blockIndexInTexture = 3;
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return par1 == 1 ? 0 : (par1 == 0 ? 2 : 3);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            if (par1World.getBlockLightValue(par2, par3 + 1, par4) < 4 && Block.lightOpacity[par1World.getBlockId(par2, par3 + 1, par4)] > 2)
            {
                // CraftBukkit start
                org.bukkit.World bworld = par1World.getWorld();
                BlockState blockState = bworld.getBlockAt(par2, par3, par4).getState();
                blockState.setTypeId(Block.dirt.blockID);
                BlockFadeEvent event = new BlockFadeEvent(blockState.getBlock(), blockState);
                par1World.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled())
                {
                    blockState.update(true);
                }

                // CraftBukkit end
            }
            else if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9)
            {
                int var6 = Math.min(4, Math.max(20, (int)(4 * 100F / par1World.growthOdds)));  // Spigot

                for (int var7 = 0; var7 < var6; ++var7)   // Spigot
                {
                    int var8 = par2 + par5Random.nextInt(3) - 1;
                    int var9 = par3 + par5Random.nextInt(5) - 3;
                    int var10 = par4 + par5Random.nextInt(3) - 1;
                    int l1 = par1World.getBlockId(var8, var9 + 1, var10);

                    if (par1World.getBlockId(var8, var9, var10) == Block.dirt.blockID && par1World.getBlockLightValue(var8, var9 + 1, var10) >= 4 && Block.lightOpacity[l1] <= 2)
                    {
                        // CraftBukkit start
                        org.bukkit.World bworld = par1World.getWorld();
                        BlockState blockState = bworld.getBlockAt(var8, var9, var10).getState();
                        blockState.setTypeId(Block.grass.blockID);
                        BlockSpreadEvent event = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(par2, par3, par4), blockState);
                        par1World.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled())
                        {
                            blockState.update(true);
                        }

                        // CraftBukkit end
                    }
                }
            }
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.dirt.idDropped(0, par2Random, par3);
    }
}
