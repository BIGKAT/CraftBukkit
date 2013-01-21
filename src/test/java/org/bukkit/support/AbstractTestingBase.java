package org.bukkit.support;


import org.junit.BeforeClass;

/**
 *  If you are getting: java.lang.ExceptionInInitializerError
 *    at net.minecraft.server.StatisticList.<clinit>(SourceFile:58)
 *    at net.minecraft.server.Item.<clinit>(SourceFile:252)
 *    at Block.<clinit>(Block.java:577)
 *
 *  extend this class to solve it.
 */
public abstract class AbstractTestingBase {

    @BeforeClass
    public static void setup() {
        net.minecraft.stats.StatList/*was:StatisticList*/.nopInit/*was:a*/();
        DummyServer.setup();
        DummyPotions.setup();
        DummyEnchantments.setup();
    }
}