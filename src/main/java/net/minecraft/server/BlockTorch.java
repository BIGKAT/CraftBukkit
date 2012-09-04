package net.minecraft.server;

import java.util.Random;

import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.*;

public class BlockTorch extends Block {

	protected BlockTorch(int i, int j) {
		super(i, j, Material.ORIENTABLE);
		this.b(true);
		this.a(CreativeModeTab.c);
	}

	public AxisAlignedBB e(World world, int i, int j, int k) {
		return null;
	}

	public boolean d() {
		return false;
	}

	public boolean c() {
		return false;
	}

	public int b() {
		return 2;
	}

	private boolean l(World world, int i, int j, int k) {
		if (world.t(i, j, k)) {
			return true;
		} else {
			int l = world.getTypeId(i, j, k);

			return (Block.byId[l] != null && Block.byId[l].canPlaceTorchOnTop(
					world, i, j, k));
		}
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	public boolean canPlace(World par1World, int par2, int par3, int par4) {
		return par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true)
				|| par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST,
						true)
				|| par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH,
						true)
				|| par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH,
						true)
				|| l(par1World, par2, par3 - 1, par4);
	}

	/**
     * called before onBlockPlacedBy by ItemBlock and ItemReed
     */
    public void postPlace(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8)
    {
        int var9 = par1World.getData(par2, par3, par4);

        if (par5 == 1 && this.l(par1World, par2, par3 - 1, par4))
        {
            var9 = 5;
        }

        if (par5 == 2 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true))
        {
            var9 = 4;
        }

        if (par5 == 3 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true))
        {
            var9 = 3;
        }

        if (par5 == 4 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true))
        {
            var9 = 2;
        }

        if (par5 == 5 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true))
        {
            var9 = 1;
        }

        par1World.setData(par2, par3, par4, var9);
    }

	public void b(World world, int i, int j, int k, Random random) {
		super.b(world, i, j, k, random);
		if (world.getData(i, j, k) == 0) {
			this.onPlace(world, i, j, k);
		}
	}
	
	/**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onPlace(World par1World, int par2, int par3, int par4)
    {
        if (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true))
        {
            par1World.setData(par2, par3, par4, 1);
        }
        else if (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true))
        {
            par1World.setData(par2, par3, par4, 2);
        }
        else if (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true))
        {
            par1World.setData(par2, par3, par4, 3);
        }
        else if (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true))
        {
            par1World.setData(par2, par3, par4, 4);
        }
        else if (this.l(par1World, par2, par3 - 1, par4))
        {
            par1World.setData(par2, par3, par4, 5);
        }

        this.n(par1World, par2, par3, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World par1World, int par2, int par3, int par4, int par5)
    {
        if (this.n(par1World, par2, par3, par4))
        {
            int var6 = par1World.getData(par2, par3, par4);
            boolean var7 = false;

            if (!par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST, true) && var6 == 1)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST, true) && var6 == 2)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH, true) && var6 == 3)
            {
                var7 = true;
            }

            if (!par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH, true) && var6 == 4)
            {
                var7 = true;
            }

            if (!this.l(par1World, par2, par3 - 1, par4) && var6 == 5)
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
			if (world.getTypeId(i, j, k) == this.id) {
				this.c(world, i, j, k, world.getData(i, j, k), 0);
				world.setTypeId(i, j, k, 0);
			}

			return false;
		} else {
			return true;
		}
	}

	public MovingObjectPosition a(World world, int i, int j, int k,
			Vec3D vec3d, Vec3D vec3d1) {
		int l = world.getData(i, j, k) & 7;
		float f = 0.15F;

		if (l == 1) {
			this.a(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
		} else if (l == 2) {
			this.a(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
		} else if (l == 3) {
			this.a(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
		} else if (l == 4) {
			this.a(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
		} else {
			f = 0.1F;
			this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
		}

		return super.a(world, i, j, k, vec3d, vec3d1);
	}
}
