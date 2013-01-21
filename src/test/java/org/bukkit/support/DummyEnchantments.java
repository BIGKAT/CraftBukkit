package org.bukkit.support;


public class DummyEnchantments {
    static {
        net.minecraft.enchantment.Enchantment/*was:Enchantment*/.enchantmentsList/*was:byId*/.getClass();
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
    }

    public static void setup() {}
}
