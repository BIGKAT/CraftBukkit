package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling.TreeGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenSwamp extends WorldGenerator implements net.minecraft.block.BlockSapling.TreeGenerator   // CraftBukkit add interface
{
    public WorldGenSwamp() {}

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        // CraftBukkit start - moved to generate
        return this.generate((BlockChangeDelegate) par1World, par2Random, par3, par4, par5);
    }

    public boolean generate(BlockChangeDelegate world, Random random, int i, int j, int k)
    {
        // CraftBukkit end
        int l;

        for (l = random.nextInt(4) + 5; world.getTypeId(i, j - 1, k) != 0 && Block.blocksList[world.getTypeId(i, j - 1, k)].blockMaterial == Material.water; --j)   // CraftBukkit - bypass world.getMaterial
        {
            ;
        }

        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 128)
        {
            int i1;
            int j1;
            int k1;
            int l1;

            for (i1 = j; i1 <= j + 1 + l; ++i1)
            {
                byte b0 = 1;

                if (i1 == j)
                {
                    b0 = 0;
                }

                if (i1 >= j + 1 + l - 2)
                {
                    b0 = 3;
                }

                for (j1 = i - b0; j1 <= i + b0 && flag; ++j1)
                {
                    for (k1 = k - b0; k1 <= k + b0 && flag; ++k1)
                    {
                        if (i1 >= 0 && i1 < 128)
                        {
                            l1 = world.getTypeId(j1, i1, k1);

                            if (l1 != 0 && l1 != Block.leaves.blockID)
                            {
                                if (l1 != Block.waterStill.blockID && l1 != Block.waterMoving.blockID)
                                {
                                    flag = false;
                                }
                                else if (i1 > j)
                                {
                                    flag = false;
                                }
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                i1 = world.getTypeId(i, j - 1, k);

                if ((i1 == Block.grass.blockID || i1 == Block.dirt.blockID) && j < 128 - l - 1)
                {
                    this.setType(world, i, j - 1, k, Block.dirt.blockID);
                    int i2;
                    int j2;

                    for (j2 = j - 3 + l; j2 <= j + l; ++j2)
                    {
                        j1 = j2 - (j + l);
                        k1 = 2 - j1 / 2;

                        for (l1 = i - k1; l1 <= i + k1; ++l1)
                        {
                            i2 = l1 - i;

                            for (int k2 = k - k1; k2 <= k + k1; ++k2)
                            {
                                int l2 = k2 - k;

                                if ((Math.abs(i2) != k1 || Math.abs(l2) != k1 || random.nextInt(2) != 0 && j1 != 0) && !Block.opaqueCubeLookup[world.getTypeId(l1, j2, k2)])
                                {
                                    this.setType(world, l1, j2, k2, Block.leaves.blockID);
                                }
                            }
                        }
                    }

                    for (j2 = 0; j2 < l; ++j2)
                    {
                        j1 = world.getTypeId(i, j + j2, k);

                        if (j1 == 0 || j1 == Block.leaves.blockID || j1 == Block.waterMoving.blockID || j1 == Block.waterStill.blockID)
                        {
                            this.setType(world, i, j + j2, k, Block.wood.blockID);
                        }
                    }

                    for (j2 = j - 3 + l; j2 <= j + l; ++j2)
                    {
                        j1 = j2 - (j + l);
                        k1 = 2 - j1 / 2;

                        for (l1 = i - k1; l1 <= i + k1; ++l1)
                        {
                            for (i2 = k - k1; i2 <= k + k1; ++i2)
                            {
                                if (world.getTypeId(l1, j2, i2) == Block.leaves.blockID)
                                {
                                    if (random.nextInt(4) == 0 && world.getTypeId(l1 - 1, j2, i2) == 0)
                                    {
                                        this.b(world, l1 - 1, j2, i2, 8);
                                    }

                                    if (random.nextInt(4) == 0 && world.getTypeId(l1 + 1, j2, i2) == 0)
                                    {
                                        this.b(world, l1 + 1, j2, i2, 2);
                                    }

                                    if (random.nextInt(4) == 0 && world.getTypeId(l1, j2, i2 - 1) == 0)
                                    {
                                        this.b(world, l1, j2, i2 - 1, 1);
                                    }

                                    if (random.nextInt(4) == 0 && world.getTypeId(l1, j2, i2 + 1) == 0)
                                    {
                                        this.b(world, l1, j2, i2 + 1, 4);
                                    }
                                }
                            }
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }

    // CraftBukkit - change signature
    private void b(BlockChangeDelegate world, int i, int j, int k, int l)
    {
        this.setTypeAndData(world, i, j, k, Block.vine.blockID, l);
        int i1 = 4;

        while (true)
        {
            --j;

            if (world.getTypeId(i, j, k) != 0 || i1 <= 0)
            {
                return;
            }

            this.setTypeAndData(world, i, j, k, Block.vine.blockID, l);
            --i1;
        }
    }
}