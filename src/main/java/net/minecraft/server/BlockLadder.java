package net.minecraft.server;

import java.util.Random;

import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.*;

public class BlockLadder extends Block {

    protected BlockLadder(int i, int j) {
        super(i, j, Material.ORIENTABLE);
        this.a(CreativeModeTab.c);
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);
        float f = 0.125F;

        if (l == 2) {
            this.a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (l == 3) {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }

        if (l == 4) {
            this.a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (l == 5) {
            this.a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }

        return super.e(world, i, j, k);
    }

    public boolean d() {
        return false;
    }

    public boolean c() {
        return false;
    }

    public int b() {
        return 8;
    }

    public boolean canPlace(World par1World, int par2, int par3, int par4)
    {
      return (par1World.isBlockSolidOnSide(par2 - 1, par3, par4, 5)) || (par1World.isBlockSolidOnSide(par2 + 1, par3, par4, 4)) || (par1World.isBlockSolidOnSide(par2, par3, par4 - 1, 3)) || (par1World.isBlockSolidOnSide(par2, par3, par4 + 1, 2));
    }

    /**
     * called before onBlockPlacedBy by ItemBlock and ItemReed
     */
    public void postPlace(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8)
    {
        int var9 = par1World.getData(par2, par3, par4);

        if ((var9 == 0 || par5 == 2) && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            var9 = 2;
        }

        if ((var9 == 0 || par5 == 3) && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            var9 = 3;
        }

        if ((var9 == 0 || par5 == 4) && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            var9 = 4;
        }

        if ((var9 == 0 || par5 == 5) && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            var9 = 5;
        }

        par1World.setData(par2, par3, par4, var9);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World par1World, int par2, int par3, int par4, int par5)
    {
        int var6 = par1World.getData(par2, par3, par4);
        boolean var7 = false;

        if (var6 == 2 && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, NORTH))
        {
            var7 = true;
        }

        if (var6 == 3 && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, SOUTH))
        {
            var7 = true;
        }

        if (var6 == 4 && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, WEST))
        {
            var7 = true;
        }

        if (var6 == 5 && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, EAST))
        {
            var7 = true;
        }

        if (!var7)
        {
            this.c(par1World, par2, par3, par4, var6, 0);
            par1World.setTypeId(par2, par3, par4, 0);
        }

        super.doPhysics(par1World, par2, par3, par4, par5);
    }

    public int a(Random random) {
        return 1;
    }
    
    @Override
    public boolean isLadder(World world, int x, int y, int z)
    {
        return true;
    }
}
