package net.minecraft.server;

import net.minecraft.server.Item;
import net.minecraft.server.ItemArmor;

public enum EnumArmorMaterial {

    CLOTH(5, new int[]{1, 3, 2, 1}, 15),
    CHAIN(15, new int[]{2, 5, 4, 1}, 12),
    IRON(15, new int[]{2, 6, 5, 2}, 9),
    GOLD(7, new int[]{2, 5, 3, 1}, 25),
    DIAMOND(33, new int[]{3, 8, 6, 3}, 10);
   private int f;
   private int[] g;
   private int h;
   
   //Added by forge for custom Armor materials.
   public Item customCraftingMaterial = null;

   private EnumArmorMaterial(int var2, int[] var4, int var5) {
      this.f = var2;
      this.g = var4;
      this.h = var5;
   }

   public int a(int var1) {
      return ItemArmor.e()[var1] * this.f;
   }

   public int b(int var1) {
      return this.g[var1];
   }

   public int a() {
      return this.h;
   }

   public int b() {
	   // Forge start
	   switch (this)
	   {
	   	case CLOTH:   return Item.LEATHER.id;
	   	case CHAIN:   return Item.IRON_INGOT.id;
	   	case GOLD:    return Item.GOLD_INGOT.id;
	   	case IRON:    return Item.IRON_INGOT.id;
	   	case DIAMOND: return Item.DIAMOND.id;
	   	default:      return (customCraftingMaterial == null ? 0 : customCraftingMaterial.id);
	   }
	   // Forge end
   }

}
