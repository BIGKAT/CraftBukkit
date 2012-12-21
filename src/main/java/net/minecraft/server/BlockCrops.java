package net.minecraft.server;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.BlockFlower;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;

import net.minecraftforge.common.ForgeDirection;

public class BlockCrops extends BlockFlower {

   protected BlockCrops(int i, int j) {
      super(i, j);
      this.textureId = j;
      this.b(true);
      float f = 0.5F;
      this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
      this.a((CreativeModeTab)null);
      this.c(0.0F);
      this.a(g);
      this.D();
      this.r();
   }

   protected boolean d_(int i) {
      return i == Block.SOIL.id;
   }

   public void b(World world, int i, int j, int k, Random random) {
      super.b(world, i, j, k, random);
      if(world.getLightLevel(i, j + 1, k) >= 9) {
         int l = world.getData(i, j, k);

         if(l < 7) {
            float f = this.l(world, i, j, k);

                if (random.nextInt((int) ((world.growthOdds * 100 / world.getWorld().wheatGrowthModifier / 25.0F) / f) + 1) == 0) { // Spigot
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j, k, this.id, ++l); // CraftBukkit
            }
         }
      }
   }

   public void c_(World world, int i, int j, int k) {
      world.setData(i, j, k, 7);
   }

   private float l(World world, int i, int j, int k) {
      float f = 1.0F;
      int l = world.getTypeId(i, j, k - 1);
      int i1 = world.getTypeId(i, j, k + 1);
      int j1 = world.getTypeId(i - 1, j, k);
      int k1 = world.getTypeId(i + 1, j, k);
      int l1 = world.getTypeId(i - 1, j, k - 1);
      int i2 = world.getTypeId(i + 1, j, k - 1);
      int j2 = world.getTypeId(i + 1, j, k + 1);
      int k2 = world.getTypeId(i - 1, j, k + 1);
      boolean flag = j1 == this.id || k1 == this.id;
      boolean flag1 = l == this.id || i1 == this.id;
      boolean flag2 = l1 == this.id || i2 == this.id || j2 == this.id || k2 == this.id;

      for(int l2 = i - 1; l2 <= i + 1; ++l2) {
         for(int i3 = k - 1; i3 <= k + 1; ++i3) {
            int j3 = world.getTypeId(l2, j - 1, i3);
            float f1 = 0.0F;
            if (byId[j3] != null && byId[j3].canSustainPlant(world, l2, j - 1, i3, ForgeDirection.UP, this)) { // Forge
               f1 = 1.0F;
               if (byId[j3].isFertile(world, l2, j - 1, i3)) { // Forge
                  f1 = 3.0F;
               }
            }

            if(l2 != i || i3 != k) {
               f1 /= 4.0F;
            }

            f += f1;
         }
      }

      if(flag2 || flag && flag1) {
         f /= 2.0F;
      }

      return f;
   }

   public int a(int i, int j) {
      if(j < 0) {
         j = 7;
      }

      return this.textureId + j;
   }

   public int d() {
      return 6;
   }

   protected int h() {
      return Item.SEEDS.id;
   }

   protected int j() {
      return Item.WHEAT.id;
   }

   // Forge start
   public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
      super.dropNaturally(world, i, j, k, l, f, 0);
   }
   
   public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
   {
	   ArrayList<ItemStack> ret = super.getBlockDropped(world, x, y, z, metadata, fortune);
	   if (metadata >= 7)
	   {
		   for (int n = 0; n < 3 + fortune; n++)
		   {
			   if (world.random.nextInt(15) <= metadata)
			   {
				   ret.add(new ItemStack(this.h(), 1, 0));
			   }
		   }
	   }
	   return ret;
   }
   // Forge end

   public int getDropType(int i, Random random, int j) {
      return i == 7?this.j():this.h();
   }

   public int a(Random random) {
      return 1;
   }
}