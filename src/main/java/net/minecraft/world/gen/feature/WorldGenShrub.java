package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling.TreeGenerator;
import net.minecraft.world.World;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenShrub extends WorldGenerator implements net.minecraft.block.BlockSapling.TreeGenerator   // CraftBukkit add interface
{
    private int field_76527_a;
    private int field_76526_b;

    public WorldGenShrub(int par1, int par2)
    {
        this.field_76526_b = par1;
        this.field_76527_a = par2;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        // CraftBukkit start - moved to generate
        return this.generate((BlockChangeDelegate) par1World, par2Random, par3, par4, par5);
    }

    public boolean generate(BlockChangeDelegate world, Random random, int i, int j, int k)
    {
        // CraftBukkit end
        int l;

        for (boolean flag = false; ((l = world.getTypeId(i, j, k)) == 0 || l == Block.leaves.blockID) && j > 0; --j)
        {
            ;
        }

        int i1 = world.getTypeId(i, j, k);

        if (i1 == Block.dirt.blockID || i1 == Block.grass.blockID)
        {
            ++j;
            this.setTypeAndData(world, i, j, k, Block.wood.blockID, this.field_76526_b);

            for (int j1 = j; j1 <= j + 2; ++j1)
            {
                int k1 = j1 - j;
                int l1 = 2 - k1;

                for (int i2 = i - l1; i2 <= i + l1; ++i2)
                {
                    int j2 = i2 - i;

                    for (int k2 = k - l1; k2 <= k + l1; ++k2)
                    {
                        int l2 = k2 - k;

                        if ((Math.abs(j2) != l1 || Math.abs(l2) != l1 || random.nextInt(2) != 0) && !Block.opaqueCubeLookup[world.getTypeId(i2, j1, k2)])
                        {
                            this.setTypeAndData(world, i2, j1, k2, Block.leaves.blockID, this.field_76527_a);
                        }
                    }
                }
            }

            // CraftBukkit start - return false if gen was unsuccessful
        }
        else
        {
            return false;
        }

        // CraftBukkit end
        return true;
    }
}
