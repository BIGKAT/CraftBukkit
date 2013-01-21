package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;


import org.bukkit.inventory.ItemStack;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class NMSCraftItemStackTest extends AbstractTestingBase {

    @Test
    public void testCloneEnchantedItem() throws Exception {
        /*was:net.minecraft.server.*/net.minecraft.item.ItemStack/*was:ItemStack*/ nmsItemStack = new net.minecraft.item.ItemStack/*was:ItemStack*/(net.minecraft.item.Item/*was:Item*/.potion/*was:POTION*/);
        nmsItemStack.addEnchantment/*was:addEnchantment*/(net.minecraft.enchantment.Enchantment/*was:Enchantment*/.sharpness/*was:DAMAGE_ALL*/, 1);
        ItemStack itemStack = CraftItemStack.asCraftMirror(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assertThat(clone.getType(), is(itemStack.getType()));
        assertThat(clone.getAmount(), is(itemStack.getAmount()));
        assertThat(clone.getDurability(), is(itemStack.getDurability()));
        assertThat(clone.getEnchantments(), is(itemStack.getEnchantments()));
        assertThat(clone.getTypeId(), is(itemStack.getTypeId()));
        assertThat(clone.getData(), is(itemStack.getData()));
        assertThat(clone, is(itemStack));
    }

    @Test
    public void testCloneNullItem() throws Exception {
        /*was:net.minecraft.server.*/net.minecraft.item.ItemStack/*was:ItemStack*/ nmsItemStack = null;
        ItemStack itemStack = CraftItemStack.asCraftMirror(nmsItemStack);
        ItemStack clone = itemStack.clone();
        assertThat(clone, is(itemStack));
    }
}
