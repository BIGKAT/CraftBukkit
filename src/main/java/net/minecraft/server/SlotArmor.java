package net.minecraft.server;

class SlotArmor extends Slot {

   final int a;
   final ContainerPlayer b;


   SlotArmor(ContainerPlayer var1, IInventory var2, int var3, int var4, int var5, int var6) {
      super(var2, var3, var4, var5);
      this.b = var1;
      this.a = var6;
   }

   public int a() {
      return 1;
   }

   public boolean isAllowed(ItemStack var1) {
      return var1 == null?false:(var1.getItem() instanceof ItemArmor?((ItemArmor)var1.getItem()).a == this.a:(var1.getItem().id != Block.PUMPKIN.id && var1.getItem().id != Item.SKULL.id?false:this.a == 0));
   }
}
