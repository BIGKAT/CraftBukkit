package org.bukkit.potion;

import static org.junit.Assert.*;

import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.junit.BeforeClass;
import org.junit.Test;

public class PotionTest {
    @BeforeClass
    public static void setUp() {
        org.bukkit.potion.Potion.setPotionBrewer(new CraftPotionBrewer());
        net.minecraft.src.Potion.BLINDNESS.getClass();
        PotionEffectType.stopAcceptingRegistrations();
    }

    @Test
    public void getEffects() {
        for (PotionType type : PotionType.values()) {
            for (PotionEffect effect : new org.bukkit.potion.Potion(type).getEffects()) {
                assertTrue(effect.getType() == PotionEffectType.getById(effect.getType().getId()));
            }
        }
    }
}
