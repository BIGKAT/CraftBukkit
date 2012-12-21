package net.minecraft.server;

import java.util.ArrayList;
import java.util.Random;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;

public class BlockLongGrass extends BlockFlower implements IShearable // Forge
{
    protected BlockLongGrass(int var1, int var2)
    {
        super(var1, var2, Material.REPLACEABLE_PLANT);
        float var3 = 0.4F;
        this.a(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, 0.8F, 0.5F + var3);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int a(int var1, int var2)
    {
        return var2 == 1 ? this.textureId : (var2 == 2 ? this.textureId + 16 + 1 : (var2 == 0 ? this.textureId + 16 : this.textureId));
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int getDropType(int var1, Random var2, int var3)
    {
        return -1; // Forge
    }

    /**
     * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i' (inclusive).
     */
    public int getDropCount(int var1, Random var2)
    {
        return 1 + var2.nextInt(var1 * 2 + 1);
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
   public void a(World world, EntityHuman entityplayer, int var3, int var4, int var5, int var6) {
	   super.a(world, entityplayer, var3, var4, var5, var6); // Forge
   }


   public int getDropData(World var1, int var2, int var3, int var4) {
      return var1.getData(var2, var3, var4);
   }

   // Forge start
   @Override
   public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int meta, int fortune)
   {
	   ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

       if (world.random.nextInt(8) != 0)
       {
           return ret;
       }
       else
       {
           ItemStack item = ForgeHooks.getGrassSeed(world);

           if (item != null)
           {
               ret.add(item);
           }

           return ret;
       }
   }

   public boolean isShearable(ItemStack item, World world, int x, int y, int z) 
   {
       return true;
   }

   public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune) 
   {
	   ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
       ret.add(new ItemStack(this, 1, world.getData(x, y, z)));
       return ret;
   }
   // Forge end
}
