package net.minecraft.server;

public class ItemArmor extends Item
{
    /** Holds the 'base' maxDamage that each armorType have. */
    private static final int[] co = new int[] {11, 16, 15, 13};

    /**
     * Stores the armor type: 0 is helmet, 1 is plate, 2 is legs and 3 is boots
     */
    public final int a;

    /** Holds the amount of damage that the armor reduces at full durability. */
    public final int b;

    /**
     * Used on RenderPlayer to select the correspondent armor to be rendered on the player: 0 is cloth, 1 is chain, 2 is
     * iron, 3 is diamond and 4 is gold.
     */
    public final int c;

    /** The EnumArmorMaterial used for this ItemArmor */
    private final EnumArmorMaterial cp;

    public ItemArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4)
    {
        super(par1);
        this.cp = par2EnumArmorMaterial;
        this.a = par4;
        this.c = par3;
        this.b = par2EnumArmorMaterial.b(par4);
        this.setMaxDurability(par2EnumArmorMaterial.a(par4));
        this.maxStackSize = 1;
        this.a(CreativeModeTab.j);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int c()
    {
        return this.cp.a();
    }

    /**
     * Return the armor material for this armor item.
     */
    public EnumArmorMaterial d()
    {
        return this.cp;
    }

    /**
     * Return whether the specified armor ItemStack has a color.
     */
    public boolean a(ItemStack par1ItemStack)
    {
        return this.cp != EnumArmorMaterial.CLOTH ? false : (!par1ItemStack.hasTag() ? false : (!par1ItemStack.getTag().hasKey("display") ? false : par1ItemStack.getTag().getCompound("display").hasKey("color")));
    }

    /**
     * Return the color for the specified armor ItemStack.
     */
    public int b(ItemStack par1ItemStack)
    {
        if (this.cp != EnumArmorMaterial.CLOTH)
        {
            return -1;
        }
        else
        {
            NBTTagCompound var2 = par1ItemStack.getTag();

            if (var2 == null)
            {
                return 10511680;
            }
            else
            {
                NBTTagCompound var3 = var2.getCompound("display");
                return var3 == null ? 10511680 : (var3.hasKey("color") ? var3.getInt("color") : 10511680);
            }
        }
    }

    /**
     * Remove the color from the specified armor ItemStack.
     */
    public void c(ItemStack par1ItemStack)
    {
        if (this.cp == EnumArmorMaterial.CLOTH)
        {
            NBTTagCompound var2 = par1ItemStack.getTag();

            if (var2 != null)
            {
                NBTTagCompound var3 = var2.getCompound("display");

                if (var3.hasKey("color"))
                {
                    var3.o("color");
                }
            }
        }
    }

    public void b(ItemStack par1ItemStack, int par2)
    {
        if (this.cp != EnumArmorMaterial.CLOTH)
        {
            throw new UnsupportedOperationException("Can\'t dye non-leather!");
        }
        else
        {
            NBTTagCompound var3 = par1ItemStack.getTag();

            if (var3 == null)
            {
                var3 = new NBTTagCompound();
                par1ItemStack.setTag(var3);
            }

            NBTTagCompound var4 = var3.getCompound("display");

            if (!var3.hasKey("display"))
            {
                var3.setCompound("display", var4);
            }

            var4.setInt("color", par2);
        }
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean a(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return this.cp.b() == par2ItemStack.id ? true : super.a(par1ItemStack, par2ItemStack);
    }

    /**
     * Returns the 'max damage' factor array for the armor, each piece of armor have a durability factor (that gets
     * multiplied by armor material factor)
     */
    static int[] e()
    {
        return co;
    }
}
