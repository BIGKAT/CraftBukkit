package net.minecraft.server;

import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnumArmorMaterial;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.NBTTagCompound;

public class ItemArmor extends Item {

   private static final int[] cl = new int[]{11, 16, 15, 13};
   public final int a;
   public final int b;
   public final int c;
   private final EnumArmorMaterial cm;


   public ItemArmor(int var1, EnumArmorMaterial var2, int var3, int var4) {
      super(var1);
      this.cm = var2;
      this.a = var4;
      this.c = var3;
      this.b = var2.b(var4);
      this.setMaxDurability(var2.a(var4));
      this.maxStackSize = 1;
      this.a(CreativeModeTab.j);
   }

   public int c() {
      return this.cm.a();
   }

   public EnumArmorMaterial d() {
      return this.cm;
   }

   public boolean b_(ItemStack var1) {
      return this.cm != EnumArmorMaterial.CLOTH?false:(!var1.hasTag()?false:(!var1.getTag().hasKey("display")?false:var1.getTag().getCompound("display").hasKey("color")));
   }

   public int b(ItemStack var1) {
      if(this.cm != EnumArmorMaterial.CLOTH) {
         return -1;
      } else {
         NBTTagCompound var2 = var1.getTag();
         if(var2 == null) {
            return 10511680;
         } else {
            NBTTagCompound var3 = var2.getCompound("display");
            return var3 == null?10511680:(var3.hasKey("color")?var3.getInt("color"):10511680);
         }
      }
   }

   public void c(ItemStack var1) {
      if(this.cm == EnumArmorMaterial.CLOTH) {
         NBTTagCompound var2 = var1.getTag();
         if(var2 != null) {
            NBTTagCompound var3 = var2.getCompound("display");
            if(var3.hasKey("color")) {
               var3.o("color");
            }

         }
      }
   }

   public void b(ItemStack var1, int var2) {
      if(this.cm != EnumArmorMaterial.CLOTH) {
         throw new UnsupportedOperationException("Can\'t dye non-leather!");
      } else {
         NBTTagCompound var3 = var1.getTag();
         if(var3 == null) {
            var3 = new NBTTagCompound();
            var1.setTag(var3);
         }

         NBTTagCompound var4 = var3.getCompound("display");
         if(!var3.hasKey("display")) {
            var3.setCompound("display", var4);
         }

         var4.setInt("color", var2);
      }
   }

   public boolean a(ItemStack var1, ItemStack var2) {
      return this.cm.b() == var2.id?true:super.a(var1, var2);
   }

   static int[] e() {
      return cl;
   }

}
