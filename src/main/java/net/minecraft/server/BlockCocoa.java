package net.minecraft.server;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;

public class BlockCocoa extends BlockDirectional {

   public BlockCocoa(int var1) {
      super(var1, 168, Material.PLANT);
      this.b(true);
   }

   public void b(World var1, int var2, int var3, int var4, Random var5) {
      if(!this.d(var1, var2, var3, var4)) {
         this.c(var1, var2, var3, var4, var1.getData(var2, var3, var4), 0);
         var1.setTypeId(var2, var3, var4, 0);
      } else if(var1.random.nextInt(5) == 0) {
         int var6 = var1.getData(var2, var3, var4);
         int var7 = c(var6);
         if(var7 < 2) {
            ++var7;
            var1.setData(var2, var3, var4, var7 << 2 | e(var6));
         }
      }

   }

   public boolean d(World var1, int var2, int var3, int var4) {
      int var5 = e(var1.getData(var2, var3, var4));
      var2 += Direction.a[var5];
      var4 += Direction.b[var5];
      int var6 = var1.getTypeId(var2, var3, var4);
      return var6 == Block.LOG.id && BlockLog.e(var1.getData(var2, var3, var4)) == 3;
   }

   public int d() {
      return 28;
   }

   public boolean b() {
      return false;
   }

   public boolean c() {
      return false;
   }

   public AxisAlignedBB e(World var1, int var2, int var3, int var4) {
      this.updateShape(var1, var2, var3, var4);
      return super.e(var1, var2, var3, var4);
   }

   public void updateShape(IBlockAccess var1, int var2, int var3, int var4) {
      int var5 = var1.getData(var2, var3, var4);
      int var6 = e(var5);
      int var7 = c(var5);
      int var8 = 4 + var7 * 2;
      int var9 = 5 + var7 * 2;
      float var10 = (float)var8 / 2.0F;
      switch(var6) {
      case 0:
         this.a((8.0F - var10) / 16.0F, (12.0F - (float)var9) / 16.0F, (15.0F - (float)var8) / 16.0F, (8.0F + var10) / 16.0F, 0.75F, 0.9375F);
         break;
      case 1:
         this.a(0.0625F, (12.0F - (float)var9) / 16.0F, (8.0F - var10) / 16.0F, (1.0F + (float)var8) / 16.0F, 0.75F, (8.0F + var10) / 16.0F);
         break;
      case 2:
         this.a((8.0F - var10) / 16.0F, (12.0F - (float)var9) / 16.0F, 0.0625F, (8.0F + var10) / 16.0F, 0.75F, (1.0F + (float)var8) / 16.0F);
         break;
      case 3:
         this.a((15.0F - (float)var8) / 16.0F, (12.0F - (float)var9) / 16.0F, (8.0F - var10) / 16.0F, 0.9375F, 0.75F, (8.0F + var10) / 16.0F);
      }

   }

   public void postPlace(World var1, int var2, int var3, int var4, EntityLiving var5) {
      int var6 = ((MathHelper.floor((double)(var5.yaw * 4.0F / 360.0F) + 0.5D) & 3) + 0) % 4;
      var1.setData(var2, var3, var4, var6);
   }

   public int getPlacedData(World var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9) {
      if(var5 == 1 || var5 == 0) {
         var5 = 2;
      }

      return Direction.f[Direction.e[var5]];
   }

   public void doPhysics(World var1, int var2, int var3, int var4, int var5) {
      if(!this.d(var1, var2, var3, var4)) {
         this.c(var1, var2, var3, var4, var1.getData(var2, var3, var4), 0);
         var1.setTypeId(var2, var3, var4, 0);
      }

   }

   public static int c(int var0) {
      return (var0 & 12) >> 2;
   }

   // Forge start
   /**
    * Drops the block items with a specified chance of dropping the specified items
    */
   public void dropdropNaturally(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
   {
       super.dropNaturally(par1World, par2, par3, par4, par5, par6, 0);
   }

   @Override
   public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
   {
       ArrayList<ItemStack> dropped = super.getBlockDropped(world, x, y, z, metadata, fortune);
       int var8 = c(metadata);
       byte var9 = 1;

       if (var8 >= 2)
       {
           var9 = 3;
       }

       for (int var10 = 0; var10 < var9; ++var10)
       {
           dropped.add(new ItemStack(Item.INK_SACK, 1, 3));
       }
       return dropped;
   }
   
   public int getDropData(World var1, int var2, int var3, int var4) {
      return 3;
   }
   
   @Override
   public int getDropType(int par1, Random par2Random, int par3)
   {
       return 0;
   }
   // Forge end
}
