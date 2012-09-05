package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;

public abstract class ServerConnection {

    public static Logger a = Logger.getLogger("Minecraft");
    private final MinecraftServer c;
    private final List d = Collections.synchronizedList(new ArrayList());
    public volatile boolean b = false;

    public ServerConnection(MinecraftServer minecraftserver) {
        this.c = minecraftserver;
        this.b = true;
    }

    public void a(NetServerHandler netserverhandler) {
        this.d.add(netserverhandler);
    }

    public void a() {
        this.b = false;
    }

    public void b() {
        for (int i = 0; i < this.d.size(); ++i) {
            NetServerHandler netserverhandler = (NetServerHandler) this.d.get(i);

            try {
                netserverhandler.d();
            } catch (Exception exception) {
            	FMLLog.log(Level.SEVERE, exception, "A critical server error occured handling a packet, kicking %s", netserverhandler);
                a.log(Level.WARNING, "Failed to handle packet: " + exception, exception);
                netserverhandler.disconnect("Internal server error");
            }

            if (netserverhandler.disconnected) {
                this.d.remove(i--);
            }

            netserverhandler.networkManager.a();
        }
    }

    public MinecraftServer d() {
        return this.c;
    }
}
