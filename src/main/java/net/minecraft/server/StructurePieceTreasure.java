package net.minecraft.server;

import java.util.Random;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntityChest;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.WeightedRandom;
import net.minecraft.server.WeightedRandomChoice;
import net.minecraftforge.common.ChestGenHooks;

public class StructurePieceTreasure extends WeightedRandomChoice {

   private int b;
   private int c;
   private int d;
   private int e;

   public final ItemStack itemStack; // Forge

   public StructurePieceTreasure(int var1, int var2, int var3, int var4, int var5) {
      super(var5);
      this.b = var1;
      this.c = var2;
      this.d = var3;
      this.e = var4;
      itemStack = new ItemStack(var1, 1, var2);
   }

   public StructurePieceTreasure(ItemStack stack, int min, int max, int weight)
   {
      super(weight);
      this.itemStack = stack;
      this.d = min;
      this.e = max;
   }   

   public static void a(Random var0, StructurePieceTreasure[] var1, TileEntityChest var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         StructurePieceTreasure var5 = (StructurePieceTreasure)WeightedRandom.a(var0, (WeightedRandomChoice[])var1);
         // Forge start
         ItemStack[] stacks = ChestGenHooks.generateStacks(var0, var5.itemStack, var5.d, var5.e);
         for (ItemStack item : stacks)
         {
               var2.setItem(var0.nextInt(var2.getSize()), item);
         }
         // Forge end
      }

   }

   public static void a(Random var0, StructurePieceTreasure[] var1, TileEntityDispenser var2, int var3) {
      for(int var4 = 0; var4 < var3; ++var4) {
         StructurePieceTreasure var5 = (StructurePieceTreasure)WeightedRandom.a(var0, (WeightedRandomChoice[])var1);
         // Forge start
         ItemStack[] stacks = ChestGenHooks.generateStacks(var0, var5.itemStack, var5.d, var5.e);
         for (ItemStack item : stacks)
         {
               var2.setItem(var0.nextInt(var2.getSize()), item);
         }
         // Forge end
      }
   }
}
