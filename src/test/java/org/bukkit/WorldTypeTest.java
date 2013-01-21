package org.bukkit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class WorldTypeTest {
    @Test
    public void testTypes() {
        for (net.minecraft.world.WorldType/*was:WorldType*/ type : net.minecraft.world.WorldType/*was:WorldType*/.worldTypes/*was:types*/) {
            if (type == null) continue;

            assertThat(type.getWorldTypeName/*was:name*/() + " has no Bukkit world", org.bukkit.WorldType.getByName(type.getWorldTypeName/*was:name*/()), is(not(nullValue())));
        }
    }
}
