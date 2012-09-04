package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

import net.minecraftforge.common.ForgeDirection;

import static net.minecraftforge.common.ForgeDirection.*;

public class BlockButton extends Block {

    protected BlockButton(int i, int j) {
        super(i, j, Material.ORIENTABLE);
        this.b(true);
        this.a(CreativeModeTab.d);
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return null;
    }

    public int p_() {
        return 20;
    }

    public boolean d() {
        return false;
    }

    public boolean c() {
        return false;
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlace(World par1World, int par2, int par3, int par4, int par5)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(par5);
        return (dir == NORTH && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) ||
               (dir == SOUTH && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) ||
               (dir == WEST  && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) ||
               (dir == EAST  && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST));
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlace(World par1World, int par2, int par3, int par4)
    {
        return (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST)) ||
               (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) ||
               (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) ||
               (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH));
    }

    /**
     * called before onBlockPlacedBy by ItemBlock and ItemReed
     */
    public void postPlace(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8)
    {
        int var9 = par1World.getBlockMetadata(par2, par3, par4);
        int var10 = var9 & 8;
        var9 &= 7;
        
        ForgeDirection dir = ForgeDirection.getOrientation(par5);

        if (dir == NORTH && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            var9 = 4;
        }
        else if (dir == SOUTH && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            var9 = 3;
        }
        else if (dir == WEST && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            var9 = 2;
        }
        else if (dir == EAST && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            var9 = 1;
        }
        else
        {
            var9 = this.l(par1World, par2, par3, par4);
        }

        par1World.setData(par2, par3, par4, var9 + var10);
    }

    /**
     * Get side which this button is facing.
     */
    private int l(World par1World, int par2, int par3, int par4)
    {
        if (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST)) return 1; 
        if (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST)) return 2; 
        if (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH)) return 3; 
        if (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH)) return 4;
        return 1;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World par1World, int par2, int par3, int par4, int par5)
    {
        if (this.n(par1World, par2, par3, par4))
        {
            int var6 = par1World.getData(par2, par3, par4) & 7;
            boolean var7 = false;

            if (!par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST) && var6 == 1)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST) && var6 == 2)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH) && var6 == 3)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH) && var6 == 4)
            {
                var7 = true;
            }

            if (var7)
            {
                this.c(par1World, par2, par3, par4, par1World.getData(par2, par3, par4), 0);
                par1World.setTypeId(par2, par3, par4, 0);
            }
        }
    }

    private boolean n(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setTypeId(i, j, k, 0);
            return false;
        } else {
            return true;
        }
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k);
        int i1 = l & 7;
        boolean flag = (l & 8) > 0;
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.1875F;
        float f3 = 0.125F;

        if (flag) {
            f3 = 0.0625F;
        }

        if (i1 == 1) {
            this.a(0.0F, f, 0.5F - f2, f3, f1, 0.5F + f2);
        } else if (i1 == 2) {
            this.a(1.0F - f3, f, 0.5F - f2, 1.0F, f1, 0.5F + f2);
        } else if (i1 == 3) {
            this.a(0.5F - f2, f, 0.0F, 0.5F + f2, f1, f3);
        } else if (i1 == 4) {
            this.a(0.5F - f2, f, 1.0F - f3, 0.5F + f2, f1, 1.0F);
        }
    }

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.interact(world, i, j, k, entityhuman, 0, 0.0F, 0.0F, 0.0F);
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 7;
        int k1 = 8 - (i1 & 8);

        if (k1 == 0) {
            return true;
        } else {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int old = (k1 != 8) ? 1 : 0;
            int current = (k1 == 8) ? 1 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (k1 == 8)) {
                return true;
            }
            // CraftBukkit end

            world.setData(i, j, k, j1 + k1);
            world.d(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
            world.applyPhysics(i, j, k, this.id);
            if (j1 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            } else if (j1 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            } else if (j1 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            } else if (j1 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            } else {
                world.applyPhysics(i, j - 1, k, this.id);
            }

            world.a(i, j, k, this.id, this.p_());
            return true;
        }
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        if ((i1 & 8) > 0) {
            world.applyPhysics(i, j, k, this.id);
            int j1 = i1 & 7;

            if (j1 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            } else if (j1 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            } else if (j1 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            } else if (j1 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            } else {
                world.applyPhysics(i, j - 1, k, this.id);
            }
        }

        super.remove(world, i, j, k, l, i1);
    }

    public boolean a(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) > 0;
    }

    public boolean c(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);

        if ((i1 & 8) == 0) {
            return false;
        } else {
            int j1 = i1 & 7;

            return j1 == 5 && l == 1 ? true : (j1 == 4 && l == 2 ? true : (j1 == 3 && l == 3 ? true : (j1 == 2 && l == 4 ? true : j1 == 1 && l == 5)));
        }
    }

    public boolean isPowerSource() {
        return true;
    }

    public void b(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) != 0) {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, 1, 0);
                world.getServer().getPluginManager().callEvent(eventRedstone);

                if (eventRedstone.getNewCurrent() > 0) {
                    return;
                }
                // CraftBukkit end

                world.setData(i, j, k, l & 7);
                world.applyPhysics(i, j, k, this.id);
                int i1 = l & 7;

                if (i1 == 1) {
                    world.applyPhysics(i - 1, j, k, this.id);
                } else if (i1 == 2) {
                    world.applyPhysics(i + 1, j, k, this.id);
                } else if (i1 == 3) {
                    world.applyPhysics(i, j, k - 1, this.id);
                } else if (i1 == 4) {
                    world.applyPhysics(i, j, k + 1, this.id);
                } else {
                    world.applyPhysics(i, j - 1, k, this.id);
                }

                world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
                world.d(i, j, k, i, j, k);
            }
        }
    }

    public void f() {
        float f = 0.1875F;
        float f1 = 0.125F;
        float f2 = 0.125F;

        this.a(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
    }
}
