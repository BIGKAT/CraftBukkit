package net.minecraft.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import net.minecraft.network.packet.Packet255KickDisconnect;
import net.minecraft.util.CryptManager;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
// CraftBukkit end

class ThreadLoginVerifier extends Thread
{
    /** The login handler that spawned this thread. */
    final NetLoginHandler loginHandler;

    // CraftBukkit start
    CraftServer server;

    ThreadLoginVerifier(NetLoginHandler pendingconnection, CraftServer server)
    {
        this.server = server;
        // CraftBukkit end
        this.loginHandler = pendingconnection;
    }

    public void run()
    {
        try
        {
            // Spigot start
            if (((CraftServer) org.bukkit.Bukkit.getServer()).ipFilter)
            {
                try
                {
                    String ip = this.loginHandler.getSocket().getInetAddress().getHostAddress();
                    String[] split = ip.split("\\.");
                    StringBuilder lookup = new StringBuilder();

                    for (int i = split.length - 1; i >= 0; i--)
                    {
                        lookup.append(split[i]);
                        lookup.append(".");
                    }

                    if (!ip.contains("127.0.0.1"))
                    {
                        lookup.append("xbl.spamhaus.org.");

                        if (java.net.InetAddress.getByName(lookup.toString()) != null)
                        {
                            this.loginHandler.myTCPConnection.addToSendQueue(new Packet255KickDisconnect("Your IP address (" + ip + ") is flagged as unsafe by spamhaus.org/xbl"));
                            this.loginHandler.myTCPConnection.serverShutdown();
                            this.loginHandler.connectionComplete = true;
                            return;
                        }
                    }
                }
                catch (Exception ex)
                {
                }
            }

            // Spigot end
            String var1 = (new BigInteger(CryptManager.getServerIdHash(NetLoginHandler.getServerId(this.loginHandler), NetLoginHandler.getLoginMinecraftServer(this.loginHandler).getKeyPair().getPublic(), NetLoginHandler.getSharedKey(this.loginHandler)))).toString(16);
            URL var2 = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(NetLoginHandler.getClientUsername(this.loginHandler), "UTF-8") + "&serverId=" + URLEncoder.encode(var1, "UTF-8"));
            BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));
            String var4 = var3.readLine();
            var3.close();

            if (!"YES".equals(var4))
            {
                this.loginHandler.raiseErrorAndDisconnect("Failed to verify username!");
                return;
            }

            // CraftBukkit start
            if (this.loginHandler.getSocket() == null)
            {
                return;
            }

            AsyncPlayerPreLoginEvent asyncEvent = new AsyncPlayerPreLoginEvent(NetLoginHandler.getClientUsername(this.loginHandler), this.loginHandler.getSocket().getInetAddress());
            this.server.getPluginManager().callEvent(asyncEvent);

            if (PlayerPreLoginEvent.getHandlerList().getRegisteredListeners().length != 0)
            {
                final PlayerPreLoginEvent event = new PlayerPreLoginEvent(NetLoginHandler.getClientUsername(this.loginHandler), this.loginHandler.getSocket().getInetAddress());

                if (asyncEvent.getResult() != PlayerPreLoginEvent.Result.ALLOWED)
                {
                    event.disallow(asyncEvent.getResult(), asyncEvent.getKickMessage());
                }

                Waitable<PlayerPreLoginEvent.Result> waitable = new Waitable<PlayerPreLoginEvent.Result>()
                {
                    @Override
                    protected PlayerPreLoginEvent.Result evaluate()
                    {
                        ThreadLoginVerifier.this.server.getPluginManager().callEvent(event);
                        return event.getResult();
                    }
                };
                NetLoginHandler.getLoginMinecraftServer(this.loginHandler).processQueue.add(waitable);

                if (waitable.get() != PlayerPreLoginEvent.Result.ALLOWED)
                {
                    this.loginHandler.raiseErrorAndDisconnect(event.getKickMessage());
                    return;
                }
            }
            else
            {
                if (asyncEvent.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
                {
                    this.loginHandler.raiseErrorAndDisconnect(asyncEvent.getKickMessage());
                    return;
                }
            }

            // CraftBukkit end
            NetLoginHandler.func_72531_a(this.loginHandler, true);
            // CraftBukkit start
        }
        catch (java.io.IOException exception)
        {
            this.loginHandler.raiseErrorAndDisconnect("Failed to verify username, session authentication server unavailable!");
        }
        catch (Exception exception)
        {
            this.loginHandler.raiseErrorAndDisconnect("Failed to verify username!");
            server.getLogger().log(java.util.logging.Level.WARNING, "Exception verifying " + NetLoginHandler.getClientUsername(this.loginHandler), exception);
            // CraftBukkit end
        }
    }
}
