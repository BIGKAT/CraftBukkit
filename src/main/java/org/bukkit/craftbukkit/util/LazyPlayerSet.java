package org.bukkit.craftbukkit.util;

import java.util.HashSet;
import java.util.List;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

public class LazyPlayerSet extends LazyHashSet<Player> {

    @Override
    HashSet<Player> makeReference() {
        if (reference != null) {
            throw new IllegalStateException("Reference already created!");
        }
        List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
        HashSet<Player> reference = new HashSet<Player>(players.size());
        for (EntityPlayerMP player : players) {
            reference.add((Player) CraftServer.getBukkitEntity(player));
        }
        return reference;
    }

}
