package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnchantmentManager
{
    /** Is the random seed of enchantment effects. */
    private static final Random random = new Random();

    /**
     * Used to calculate the extra armor of enchantments on armors equipped on player.
     */
    private static final EnchantmentModifierProtection b = new EnchantmentModifierProtection((EmptyClass)null);

    /**
     * Used to calculate the (magic) extra damage done by enchantments on current equipped item of player.
     */
    private static final EnchantmentModifierDamage c = new EnchantmentModifierDamage((EmptyClass)null);

    /**
     * Returns the level of enchantment on the ItemStack passed.
     */
    public static int getEnchantmentLevel(int par0, ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return 0;
        }
        else
        {
            NBTTagList var2 = par1ItemStack.getEnchantments();

            if (var2 == null)
            {
                return 0;
            }
            else
            {
                for (int var3 = 0; var3 < var2.size(); ++var3)
                {
                    short var4 = ((NBTTagCompound)var2.get(var3)).getShort("id");
                    short var5 = ((NBTTagCompound)var2.get(var3)).getShort("lvl");

                    if (var4 == par0)
                    {
                        return var5;
                    }
                }

                return 0;
            }
        }
    }

    /**
     * Return the enchantments for the specified stack.
     */
    public static Map a(ItemStack par0ItemStack)
    {
        LinkedHashMap var1 = new LinkedHashMap();
        NBTTagList var2 = par0ItemStack.id == Item.ENCHANTED_BOOK.id ? Item.ENCHANTED_BOOK.g(par0ItemStack) : par0ItemStack.getEnchantments();

        if (var2 != null)
        {
            for (int var3 = 0; var3 < var2.size(); ++var3)
            {
                short var4 = ((NBTTagCompound)var2.get(var3)).getShort("id");
                short var5 = ((NBTTagCompound)var2.get(var3)).getShort("lvl");
                var1.put(Integer.valueOf(var4), Integer.valueOf(var5));
            }
        }

        return var1;
    }

    /**
     * Set the enchantments for the specified stack.
     */
    public static void a(Map par0Map, ItemStack par1ItemStack)
    {
        NBTTagList var2 = new NBTTagList();
        Iterator var3 = par0Map.keySet().iterator();

        while (var3.hasNext())
        {
            int var4 = ((Integer)var3.next()).intValue();
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setShort("id", (short) var4);
            var5.setShort("lvl", (short) ((Integer) par0Map.get(Integer.valueOf(var4))).intValue());
            var2.add(var5);

            if (par1ItemStack.id == Item.ENCHANTED_BOOK.id)
            {
                Item.ENCHANTED_BOOK.a(par1ItemStack, new EnchantmentInstance(var4, ((Integer) par0Map.get(Integer.valueOf(var4))).intValue()));
            }
        }

        if (var2.size() > 0)
        {
            if (par1ItemStack.id != Item.ENCHANTED_BOOK.id)
            {
                par1ItemStack.a("ench", var2);
            }
        }
        else if (par1ItemStack.hasTag())
        {
            par1ItemStack.getTag().o("ench");
        }
    }

    /**
     * Returns the biggest level of the enchantment on the array of ItemStack passed.
     */
    public static int getEnchantmentLevel(int par0, ItemStack[] par1ArrayOfItemStack)
    {
        if (par1ArrayOfItemStack == null)
        {
            return 0;
        }
        else
        {
            int var2 = 0;
            ItemStack[] var3 = par1ArrayOfItemStack;
            int var4 = par1ArrayOfItemStack.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                ItemStack var6 = var3[var5];
                int var7 = getEnchantmentLevel(par0, var6);

                if (var7 > var2)
                {
                    var2 = var7;
                }
            }

            return var2;
        }
    }

    /**
     * Executes the enchantment modifier on the ItemStack passed.
     */
    private static void a(EnchantmentModifier par0IEnchantmentModifier, ItemStack par1ItemStack)
    {
        if (par1ItemStack != null)
        {
            NBTTagList var2 = par1ItemStack.getEnchantments();

            if (var2 != null)
            {
                for (int var3 = 0; var3 < var2.size(); ++var3)
                {
                    short var4 = ((NBTTagCompound)var2.get(var3)).getShort("id");
                    short var5 = ((NBTTagCompound)var2.get(var3)).getShort("lvl");

                    if (Enchantment.byId[var4] != null)
                    {
                        par0IEnchantmentModifier.a(Enchantment.byId[var4], var5);
                    }
                }
            }
        }
    }

    /**
     * Executes the enchantment modifier on the array of ItemStack passed.
     */
    private static void a(EnchantmentModifier par0IEnchantmentModifier, ItemStack[] par1ArrayOfItemStack)
    {
        ItemStack[] var2 = par1ArrayOfItemStack;
        int var3 = par1ArrayOfItemStack.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ItemStack var5 = var2[var4];
            a(par0IEnchantmentModifier, var5);
        }
    }

    /**
     * Returns the modifier of protection enchantments on armors equipped on player.
     */
    public static int a(ItemStack[] par0ArrayOfItemStack, DamageSource par1DamageSource)
    {
        b.a = 0;
        b.b = par1DamageSource;
        a(b, par0ArrayOfItemStack);

        if (b.a > 25)
        {
            b.a = 25;
        }

        return (b.a + 1 >> 1) + random.nextInt((b.a >> 1) + 1);
    }

    /**
     * Return the (magic) extra damage of the enchantments on player equipped item.
     */
    public static int a(EntityLiving par0EntityLiving, EntityLiving par1EntityLiving)
    {
        c.a = 0;
        c.b = par1EntityLiving;
        a(c, par0EntityLiving.bD());
        return c.a > 0 ? 1 + random.nextInt(c.a) : 0;
    }

    /**
     * Returns the knockback value of enchantments on equipped player item.
     */
    public static int getKnockbackEnchantmentLevel(EntityLiving par0EntityLiving, EntityLiving par1EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.KNOCKBACK.id, par0EntityLiving.bD());
    }

    public static int getFireAspectEnchantmentLevel(EntityLiving par0EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.FIRE_ASPECT.id, par0EntityLiving.bD());
    }

    /**
     * Returns the 'Water Breathing' modifier of enchantments on player equipped armors.
     */
    public static int getOxygenEnchantmentLevel(EntityLiving par0EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.OXYGEN.id, par0EntityLiving.getEquipment());
    }

    /**
     * Return the extra efficiency of tools based on enchantments on equipped player item.
     */
    public static int getDigSpeedEnchantmentLevel(EntityLiving par0EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.DIG_SPEED.id, par0EntityLiving.bD());
    }

    /**
     * Returns the silk touch status of enchantments on current equipped item of player.
     */
    public static boolean hasSilkTouchEnchantment(EntityLiving par0EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.SILK_TOUCH.id, par0EntityLiving.bD()) > 0;
    }

    /**
     * Returns the fortune enchantment modifier of the current equipped item of player.
     */
    public static int getBonusBlockLootEnchantmentLevel(EntityLiving par0EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS.id, par0EntityLiving.bD());
    }

    /**
     * Returns the looting enchantment modifier of the current equipped item of player.
     */
    public static int getBonusMonsterLootEnchantmentLevel(EntityLiving par0EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS.id, par0EntityLiving.bD());
    }

    /**
     * Returns the aqua affinity status of enchantments on current equipped item of player.
     */
    public static boolean hasWaterWorkerEnchantment(EntityLiving par0EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.WATER_WORKER.id, par0EntityLiving.getEquipment()) > 0;
    }

    public static int getThornsEnchantmentLevel(EntityLiving par0EntityLiving)
    {
        return getEnchantmentLevel(Enchantment.THORNS.id, par0EntityLiving.getEquipment());
    }

    public static ItemStack a(Enchantment par0Enchantment, EntityLiving par1EntityLiving)
    {
        ItemStack[] var2 = par1EntityLiving.getEquipment();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ItemStack var5 = var2[var4];

            if (var5 != null && getEnchantmentLevel(par0Enchantment.id, var5) > 0)
            {
                return var5;
            }
        }

        return null;
    }

    /**
     * Returns the enchantability of itemstack, it's uses a singular formula for each index (2nd parameter: 0, 1 and 2),
     * cutting to the max enchantability power of the table (3rd parameter)
     */
    public static int a(Random par0Random, int par1, int par2, ItemStack par3ItemStack)
    {
        Item var4 = par3ItemStack.getItem();
        int var5 = var4.c();

        if (var5 <= 0)
        {
            return 0;
        }
        else
        {
            if (par2 > 15)
            {
                par2 = 15;
            }

            int var6 = par0Random.nextInt(8) + 1 + (par2 >> 1) + par0Random.nextInt(par2 + 1);
            return par1 == 0 ? Math.max(var6 / 3, 1) : (par1 == 1 ? var6 * 2 / 3 + 1 : Math.max(var6, par2 * 2));
        }
    }

    /**
     * Adds a random enchantment to the specified item. Args: random, itemStack, enchantabilityLevel
     */
    public static ItemStack a(Random par0Random, ItemStack par1ItemStack, int par2)
    {
        List var3 = b(par0Random, par1ItemStack, par2);
        boolean var4 = par1ItemStack.id == Item.BOOK.id;

        if (var4)
        {
            par1ItemStack.id = Item.ENCHANTED_BOOK.id;
        }

        if (var3 != null)
        {
            Iterator var5 = var3.iterator();

            while (var5.hasNext())
            {
                EnchantmentInstance var6 = (EnchantmentInstance)var5.next();

                if (var4)
                {
                    Item.ENCHANTED_BOOK.a(par1ItemStack, var6);
                }
                else
                {
                    par1ItemStack.addEnchantment(var6.enchantment, var6.level);
                }
            }
        }

        return par1ItemStack;
    }

    /**
     * Create a list of random EnchantmentData (enchantments) that can be added together to the ItemStack, the 3rd
     * parameter is the total enchantability level.
     */
    public static List b(Random par0Random, ItemStack par1ItemStack, int par2)
    {
        Item var3 = par1ItemStack.getItem();
        int var4 = var3.c();

        if (var4 <= 0)
        {
            return null;
        }
        else
        {
            var4 /= 2;
            var4 = 1 + par0Random.nextInt((var4 >> 1) + 1) + par0Random.nextInt((var4 >> 1) + 1);
            int var5 = var4 + par2;
            float var6 = (par0Random.nextFloat() + par0Random.nextFloat() - 1.0F) * 0.15F;
            int var7 = (int)((float)var5 * (1.0F + var6) + 0.5F);

            if (var7 < 1)
            {
                var7 = 1;
            }

            ArrayList var8 = null;
            Map var9 = b(var7, par1ItemStack);

            if (var9 != null && !var9.isEmpty())
            {
                EnchantmentInstance var10 = (EnchantmentInstance) WeightedRandom.a(par0Random, var9.values());

                if (var10 != null)
                {
                    var8 = new ArrayList();
                    var8.add(var10);

                    for (int var11 = var7; par0Random.nextInt(50) <= var11; var11 >>= 1)
                    {
                        Iterator var12 = var9.keySet().iterator();

                        while (var12.hasNext())
                        {
                            Integer var13 = (Integer)var12.next();
                            boolean var14 = true;
                            Iterator var15 = var8.iterator();

                            while (true)
                            {
                                if (var15.hasNext())
                                {
                                    EnchantmentInstance var16 = (EnchantmentInstance)var15.next();

                                    if (var16.enchantment.a(Enchantment.byId[var13.intValue()]))
                                    {
                                        continue;
                                    }

                                    var14 = false;
                                }

                                if (!var14)
                                {
                                    var12.remove();
                                }

                                break;
                            }
                        }

                        if (!var9.isEmpty())
                        {
                            EnchantmentInstance var17 = (EnchantmentInstance) WeightedRandom.a(par0Random, var9.values());
                            var8.add(var17);
                        }
                    }
                }
            }

            return var8;
        }
    }

    /**
     * Creates a 'Map' of EnchantmentData (enchantments) possible to add on the ItemStack and the enchantability level
     * passed.
     */
    public static Map b(int par0, ItemStack par1ItemStack)
    {
        Item var2 = par1ItemStack.getItem();
        HashMap var3 = null;
        boolean var4 = par1ItemStack.id == Item.BOOK.id;
        Enchantment[] var5 = Enchantment.byId;
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            Enchantment var8 = var5[var7];

            if (var8 != null && (var8.canApplyAtEnchantingTable(par1ItemStack) || var4)) // Forge
            {
                for (int var9 = var8.getStartLevel(); var9 <= var8.getMaxLevel(); ++var9)
                {
                    if (par0 >= var8.a(var9) && par0 <= var8.b(var9))
                    {
                        if (var3 == null)
                        {
                            var3 = new HashMap();
                        }

                        var3.put(Integer.valueOf(var8.id), new EnchantmentInstance(var8, var9));
                    }
                }
            }
        }

        return var3;
    }
}
