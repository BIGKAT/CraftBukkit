package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


import org.bukkit.craftbukkit.CraftArt;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ArtTest {
    private static final int UNIT_MULTIPLIER = 16;

    @Test
    public void verifyMapping() {
        List<Art> arts = Lists.newArrayList(Art.values());

        for (net.minecraft.util.EnumArt/*was:EnumArt*/ enumArt : net.minecraft.util.EnumArt/*was:EnumArt*/.values/*was:values*/()) {
            int id = enumArt.ordinal();
            String name = enumArt.title/*was:B*/;
            int width = enumArt.sizeX/*was:C*/ / UNIT_MULTIPLIER;
            int height = enumArt.sizeY/*was:D*/ / UNIT_MULTIPLIER;

            Art subject = Art.getById(id);

            String message = String.format("org.bukkit.Art is missing id: %d named: '%s'", id - Achievement.STATISTIC_OFFSET, name);
            assertNotNull(message, subject);

            assertThat(Art.getByName(name), is(subject));
            assertThat("Art." + subject + "'s width", subject.getBlockWidth(), is(width));
            assertThat("Art." + subject + "'s height", subject.getBlockHeight(), is(height));

            arts.remove(subject);
        }

        assertThat("org.bukkit.Art has too many arts", arts, is(Collections.EMPTY_LIST));
    }

    @Test
    public void testCraftArtToNotch() {
        Map<net.minecraft.util.EnumArt/*was:EnumArt*/, Art> cache = new EnumMap(net.minecraft.util.EnumArt/*was:EnumArt*/.class);
        for (Art art : Art.values()) {
            net.minecraft.util.EnumArt/*was:EnumArt*/ enumArt = CraftArt.BukkitToNotch(art);
            assertNotNull(art.name(), enumArt);
            assertThat(art.name(), cache.put(enumArt, art), is(nullValue()));
        }
    }

    @Test
    public void testCraftArtToBukkit() {
        Map<Art, net.minecraft.util.EnumArt/*was:EnumArt*/> cache = new EnumMap(Art.class);
        for (net.minecraft.util.EnumArt/*was:EnumArt*/ enumArt : net.minecraft.util.EnumArt/*was:EnumArt*/.values/*was:values*/()) {
            Art art = CraftArt.NotchToBukkit(enumArt);
            assertNotNull(enumArt.name(), art);
            assertThat(enumArt.name(), cache.put(art, enumArt), is(nullValue()));
        }
    }
}
