package org.bukkit;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.minecraft.src.AchievementList;
import net.minecraft.src.StatBase;

import org.bukkit.support.Util;
import org.junit.Test;

import com.google.common.collect.Lists;

public class AchievementTest {
    @Test
    @SuppressWarnings("unchecked")
    public void verifyMapping() throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException {
        List<Achievement> achievements = Lists.newArrayList(Achievement.values());

        for (net.minecraft.src.Achievement statistic : (List<net.minecraft.src.Achievement>) AchievementList.e) {
            int id = statistic.e;

            String name = Util.getInternalState(StatBase.class, statistic, "a");
            String message = String.format("org.bukkit.Achievement is missing id: %d named: '%s'", id - Achievement.STATISTIC_OFFSET, name);

            Achievement subject = Achievement.getById(id);
            assertNotNull(message, subject);

            achievements.remove(subject);
        }

        assertThat("org.bukkit.Achievement has too many achievements", achievements, hasSize(0));
    }
}
