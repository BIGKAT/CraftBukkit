package org.bukkit.support;


import org.bukkit.craftbukkit.potion.CraftPotionBrewer;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

public class DummyPotions {
    static {
        Potion.setPotionBrewer(new CraftPotionBrewer());
        net.minecraft.potion.Potion/*was:MobEffectList*/.blindness/*was:BLINDNESS*/.getClass();
        PotionEffectType.stopAcceptingRegistrations();
    }

    public static void setup() {}
}
