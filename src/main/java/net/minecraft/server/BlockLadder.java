package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.Material;
import net.minecraft.server.World;

import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.*;

public class BlockLadder extends Block {

   protected BlockLadder(int var1, int var2) {
      super(var1, var2, Material.ORIENTABLE);
      this.a(CreativeModeTab.c);
   }

   public AxisAlignedBB e(World var1, int var2, int var3, int var4) {
      this.updateShape(var1, var2, var3, var4);
      return super.e(var1, var2, var3, var4);
   }

   public void updateShape(IBlockAccess var1, int var2, int var3, int var4) {
      this.d(var1.getData(var2, var3, var4));
   }

   public void d(int var1) {
      float var3 = 0.125F;
      if(var1 == 2) {
         this.a(0.0F, 0.0F, 1.0F - var3, 1.0F, 1.0F, 1.0F);
      }

      if(var1 == 3) {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var3);
      }

      if(var1 == 4) {
         this.a(1.0F - var3, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

      if(var1 == 5) {
         this.a(0.0F, 0.0F, 0.0F, var3, 1.0F, 1.0F);
      }

   }

   public boolean c() {
      return false;
   }

   public boolean b() {
      return false;
   }

   public int d() {
      return 8;
   }

   // Forge start
   public boolean canPlace(World world, int var2, int var3, int var4) {
	   return world.isBlockSolidOnSide(var2 - 1, var3, var4, EAST ) ||
		      world.isBlockSolidOnSide(var2 + 1, var3, var4, WEST ) ||
	          world.isBlockSolidOnSide(var2, var3, var4 - 1, SOUTH) ||
	          world.isBlockSolidOnSide(var2, var3, var4 + 1, NORTH);   
   }
   // Forge end

   public int getPlacedData(World world, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9) {
      int var10 = var9;
      // Forge start
      if ((var10 == 0 || var5 == 2) && world.isBlockSolidOnSide(var2, var3, var4 + 1, NORTH)) {
         var10 = 2;
      }

      if((var10 == 0 || var5 == 3) && world.isBlockSolidOnSide(var2, var3, var4 - 1, SOUTH)) {
         var10 = 3;
      }

      if((var10 == 0 || var5 == 4) && world.isBlockSolidOnSide(var2 + 1, var3, var4, WEST)) {
         var10 = 4;
      }

      if((var10 == 0 || var5 == 5) && world.isBlockSolidOnSide(var2 - 1, var3, var4, EAST)) {
         var10 = 5;
      }
      // Forge end

      return var10;
   }

   public void doPhysics(World world, int var2, int var3, int var4, int var5) {
      int var6 = world.getData(var2, var3, var4);
      boolean var7 = false;
      // Forge start
      if(var6 == 2 && world.isBlockSolidOnSide(var2, var3, var4 + 1, NORTH)) {
         var7 = true;
      }

      if(var6 == 3 && world.isBlockSolidOnSide(var2, var3, var4 - 1, SOUTH)) {
         var7 = true;
      }

      if(var6 == 4 && world.isBlockSolidOnSide(var2 + 1, var3, var4, WEST)) {
         var7 = true;
      }

      if(var6 == 5 && world.isBlockSolidOnSide(var2 - 1, var3, var4, EAST)) {
         var7 = true;
      }
      // Forge end
      if(!var7) {
         this.c(world, var2, var3, var4, var6, 0);
         world.setTypeId(var2, var3, var4, 0);
      }

      super.doPhysics(world, var2, var3, var4, var5);
   }

   public int a(Random var1) {
      return 1;
   }
   
   // Forge start
   @Override
   public boolean isLadder(World world, int x, int y, int z)
   {
	   return true;
   }
   // Forge end
}
