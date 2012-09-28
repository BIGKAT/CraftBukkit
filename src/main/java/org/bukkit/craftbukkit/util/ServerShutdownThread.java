package org.bukkit.craftbukkit.util;

import net.minecraft.src.ExceptionWorldConflict;
import net.minecraft.server.MinecraftServer;

public class ServerShutdownThread extends Thread {
    private final MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.stopServer();
//        } catch (ExceptionWorldConflict ex) {
//            ex.printStackTrace();
        } finally {
            try {
				// TODO: CB added code in MinecraftServer
                // server.reader.getTerminal().restore();
            } catch (Exception e) {
            }
        }
    }
}
