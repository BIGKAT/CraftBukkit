package net.minecraft.server;

import net.minecraft.server.AchievementList;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.IInventory;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Slot;
import net.minecraft.server.Statistic;

public class SlotResult2 extends Slot {

   private EntityHuman a;
   private int f;


   public SlotResult2(EntityHuman var1, IInventory var2, int var3, int var4, int var5) {
      super(var2, var3, var4, var5);
      this.a = var1;
   }

   public boolean isAllowed(ItemStack var1) {
      return false;
   }

   public ItemStack a(int var1) {
      if(this.c()) {
         this.f += Math.min(var1, this.getItem().count);
      }

      return super.a(var1);
   }

   public void c(ItemStack var1) {
      this.b(var1);
      super.c(var1);
   }

   protected void a(ItemStack var1, int var2) {
      this.f += var2;
      this.b(var1);
   }

   protected void b(ItemStack var1) {
      var1.a(this.a.world, this.a, this.f);
      this.f = 0;
      if(var1.id == Item.IRON_INGOT.id) {
         this.a.a((Statistic)AchievementList.k, 1);
      }

      if(var1.id == Item.COOKED_FISH.id) {
         this.a.a((Statistic)AchievementList.p, 1);
      }

      ModLoader.takenFromFurnace(this.a, var1);
   }
}
