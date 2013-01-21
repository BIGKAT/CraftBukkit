package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.List;


import org.bukkit.support.Util;
import org.junit.Test;

import com.google.common.collect.Lists;

public class AchievementTest {
    @Test
    @SuppressWarnings("unchecked")
    public void verifyMapping() throws Throwable {
        List<Achievement> achievements = Lists.newArrayList(Achievement.values());

        for (/*was:net.minecraft.server.*/net.minecraft.stats.Achievement/*was:Achievement*/ statistic : (List<net.minecraft.stats.Achievement/*was:Achievement*/>) net.minecraft.stats.AchievementList/*was:AchievementList*/.achievementList/*was:e*/) {
            int id = statistic.statId/*was:e*/;

            String name = Util.getInternalState(net.minecraft.stats.StatBase/*was:Statistic*/.class, statistic, org.bukkit.craftbukkit.CraftServer.isUsingMappingCB ? "a" : "statName"); // CBMCP
            String message = String.format("org.bukkit.Achievement is missing id: %d named: '%s'", id - Achievement.STATISTIC_OFFSET, name);

            Achievement subject = Achievement.getById(id);
            assertNotNull(message, subject);

            assertTrue(name, achievements.remove(subject));
        }

        assertThat("org.bukkit.Achievement has too many achievements", achievements, is(Collections.EMPTY_LIST));
    }
}
