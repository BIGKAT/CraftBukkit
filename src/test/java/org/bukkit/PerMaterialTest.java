package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;


import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.support.AbstractTestingBase;
import org.bukkit.support.Util;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
public class PerMaterialTest extends AbstractTestingBase {
    private static int[] fireValues;

    @BeforeClass
    public static void getFireValues() {
        fireValues = Util.getInternalState(net.minecraft.block.BlockFire/*was:BlockFire*/.class, net.minecraft.block.Block/*was:Block*/.fire/*was:FIRE*/, org.bukkit.craftbukkit.CraftServer.isUsingMappingCB ? "a" : "chanceToEncourageFire"); // CBMCP
    }

    @Parameters(name= "{index}: {0}")
    public static List<Object[]> data() {
        List<Object[]> list = Lists.newArrayList();
        for (Material material : Material.values()) {
            list.add(new Object[] {material});
        }
        return list;
    }

    @Parameter public Material material;

    @Test
    public void isSolid() {
        if (material == Material.AIR) {
            assertFalse(material.isSolid());
        } else if (material.isBlock()) {
            assertThat(material.isSolid(), is(net.minecraft.block.Block/*was:Block*/.blocksList/*was:byId*/[material.getId()].blockMaterial/*was:material*/.blocksMovement/*was:isSolid*/()));
        } else {
            assertFalse(material.isSolid());
        }
    }

    @Test
    public void isEdible() {
        assertThat(material.isEdible(), is(net.minecraft.item.Item/*was:Item*/.itemsList/*was:byId*/[material.getId()] instanceof net.minecraft.item.ItemFood/*was:ItemFood*/));
    }

    @Test
    public void isRecord() {
        assertThat(material.isRecord(), is(net.minecraft.item.Item/*was:Item*/.itemsList/*was:byId*/[material.getId()] instanceof net.minecraft.item.ItemRecord/*was:ItemRecord*/));
    }

    @Test
    public void maxDurability() {
        if (material == Material.AIR) {
            assertThat((int) material.getMaxDurability(), is(0));
        } else {
            assertThat((int) material.getMaxDurability(), is(net.minecraft.item.Item/*was:Item*/.itemsList/*was:byId*/[material.getId()].getMaxDamage/*was:getMaxDurability*/()));
        }
    }

    @Test
    public void maxStackSize() {
        final ItemStack bukkit = new ItemStack(material);
        final CraftItemStack craft = CraftItemStack.asCraftCopy(bukkit);
        if (material == Material.AIR) {
            final int MAX_AIR_STACK = 0 /* Why can't I hold all of these AIR? */;
            assertThat(material.getMaxStackSize(), is(MAX_AIR_STACK));
            assertThat(bukkit.getMaxStackSize(), is(MAX_AIR_STACK));
            assertThat(craft.getMaxStackSize(), is(MAX_AIR_STACK));
        } else {
            assertThat(material.getMaxStackSize(), is(net.minecraft.item.Item/*was:Item*/.itemsList/*was:byId*/[material.getId()].getItemStackLimit/*was:getMaxStackSize*/()));
            assertThat(bukkit.getMaxStackSize(), is(material.getMaxStackSize()));
            assertThat(craft.getMaxStackSize(), is(material.getMaxStackSize()));
        }
    }

    @Test
    public void isTransparent() {
        if (material == Material.AIR) {
            assertTrue(material.isTransparent());
        } else if (material.isBlock()) {
            assertThat(material.isTransparent(), is(not(net.minecraft.block.Block/*was:Block*/.blocksList/*was:byId*/[material.getId()].blockMaterial/*was:material*/.getCanBlockGrass/*was:blocksLight*/())));
        } else {
            assertFalse(material.isTransparent());
        }
    }

    @Test
    public void isFlammable() {
        if (material != Material.AIR && material.isBlock()) {
            assertThat(material.isFlammable(), is(net.minecraft.block.Block/*was:Block*/.blocksList/*was:byId*/[material.getId()].blockMaterial/*was:material*/.getCanBurn/*was:isBurnable*/()));
        } else {
            assertFalse(material.isFlammable());
        }
    }

    @Test
    public void isBurnable() {
        if (material.isBlock()) {
            assertThat(material.isBurnable(), is(fireValues[material.getId()] > 0));
        } else {
            assertFalse(material.isBurnable());
        }
    }

    @Test
    public void isOccluding() {
        if (material.isBlock()) {
            assertThat(material.isOccluding(), is(net.minecraft.block.Block/*was:Block*/.isNormalCube/*was:i*/(material.getId())));
        } else {
            assertFalse(material.isOccluding());
        }
    }
}
