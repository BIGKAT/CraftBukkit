package net.minecraft.server;

import net.minecraft.server.Block;
import net.minecraft.server.Item;

public enum EnumToolMaterial {

    WOOD(0, 59, 2.0F, 0, 15),
    STONE(1, 131, 4.0F, 1, 5),
    IRON(2, 250, 6.0F, 2, 14),
    DIAMOND(3, 1561, 8.0F, 3, 10),
    GOLD(0, 32, 12.0F, 0, 22);
   private final int f;
   private final int g;
   private final float h;
   private final int i;
   private final int j;

   //Added by forge for custom Armor materials.
   public Item customCraftingMaterial = null;

   private EnumToolMaterial(int var2, int var3, float var4, int var5, int var6) {
      this.f = var2;
      this.g = var3;
      this.h = var4;
      this.i = var5;
      this.j = var6;
   }

   public int a() {
      return this.g;
   }

   public float b() {
      return this.h;
   }

   public int c() {
      return this.i;
   }

   public int d() {
      return this.f;
   }

   public int e() {
      return this.j;
   }

   public int f() {
	   // Forge start
	   switch (this)
	   {
	   	case WOOD:   return Block.WOOD.id;
	   	case STONE:   return Block.COBBLESTONE.id;
	   	case GOLD:    return Item.GOLD_INGOT.id;
	   	case IRON:    return Item.IRON_INGOT.id;
	   	case DIAMOND: return Item.DIAMOND.id;
	   	default:      return (customCraftingMaterial == null ? 0 : customCraftingMaterial.id);
	   }
	   // Forge end
   }

}
