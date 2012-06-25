package forge;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.server.FMLBukkitHandler;

import net.minecraft.server.*;
import forge.packets.*;

public class PacketHandlerServer extends PacketHandlerBase
{
    public static boolean DEBUG = false;
    @Override
    public void onPacketData(NetworkManager network, String channel, byte[] bytes)
    {
        NetServerHandler net = (NetServerHandler)network.getNetHandler();
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(bytes));
        ForgePacket pkt = null;

        try
        {
            int packetID = data.read();
            switch (packetID)
            {
                case ForgePacket.MODLIST:
                    pkt = new PacketModList(true);
                    pkt.readData(data);
                    onModListResponse(net, (PacketModList)pkt);
                    break;
            }
        }
        catch (IOException e)
        {
            ModLoader.getLogger().log(Level.SEVERE, "Exception in PacketHandlerServer.onPacketData", e);
            e.printStackTrace();
        }
    }

    private void onModListResponse(NetServerHandler net, PacketModList pkt) throws IOException
    {
        if (DEBUG)
        {
            System.out.println("C->S: " + pkt.toString(true));
        }
        if (pkt.Length < 0)
        {
            net.disconnect("Invalid mod list response, Size: " + pkt.Length);
            return;
        }
        if (!pkt.has4096)
        {
            net.disconnect("Must have Forge build #136+ (4096 fix) to connect to this server");
        }
        if (pkt.Mods.length == 0)
        {
            ModLoader.getLogger().log(Level.INFO, net.getName() + " joined with no mods");
        }
        else
        {
            ModLoader.getLogger().log(Level.INFO, net.getName() + " joined with: " + Arrays.toString(pkt.Mods).replaceAll("mod_", ""));
        }

        //TODO: Write a 'banned mods' system and do the checks here

        NetworkMod[] serverMods = MinecraftForge.getNetworkMods();
        ArrayList<NetworkMod> missing = new ArrayList<NetworkMod>();
        for (NetworkMod mod : serverMods)
        {
            if (!mod.clientSideRequired())
            {
                continue;
            }
            boolean found = false;
            for (String modName : pkt.Mods)
            {
                if (modName.equals(mod.toString()))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                missing.add(mod);
            }
        }
        if (missing.size() > 0)
        {
            doMissingMods(net, missing);
        }
        else
        {
            finishLogin(net);
        }
    }

    /**
     * Sends the user a list of mods they are missing and then disconnects them
     * @param net The network handler
     */
    private void doMissingMods(NetServerHandler net, ArrayList<NetworkMod> list)
    {
        PacketMissingMods pkt = new PacketMissingMods(true);
        pkt.Mods = new String[list.size()];
        int x = 0;
        for (NetworkMod mod : list)
        {
            pkt.Mods[x++] = mod.toString();
        }
        if (DEBUG)
        {
            System.out.println("S->C: " + pkt.toString(true));
        }
        net.sendPacket(pkt.getPacket());
        disconnectUser(net);
    }

    /**
     * Disconnects the player just like kicking them, just without the kick message.
     * @param net The network handler
     */
    private void disconnectUser(NetServerHandler net)
    {
        MinecraftServer mc = ModLoader.getMinecraftServerInstance();
        net.player.I();
        net.networkManager.d();
        mc.serverConfigurationManager.sendAll(new Packet3Chat("\247e" + net.getName() + " left the game."));
        mc.serverConfigurationManager.disconnect(net.player);
        net.disconnected = true;
    }
    private void finishLogin(NetServerHandler netserverhandler)
    {
    	EntityPlayer entityplayer=netserverhandler.getPlayerEntity();
        WorldServer worldserver = (WorldServer) entityplayer.world; // CraftBukkit
        ChunkCoordinates chunkcoordinates = worldserver.getSpawn();
        int maxPlayers = netserverhandler.minecraftServer.serverConfigurationManager.getMaxPlayers();
        if (maxPlayers > 60) {
            maxPlayers = 60;
        }
        netserverhandler.sendPacket(new Packet1Login("", entityplayer.id, worldserver.getWorldData().getType(), entityplayer.itemInWorldManager.getGameMode(), worldserver.worldProvider.dimension, (byte) worldserver.difficulty, (byte) worldserver.getHeight(), (byte) maxPlayers));
        entityplayer.getBukkitEntity().sendSupportedChannels();
        // CraftBukkit end

        netserverhandler.sendPacket(new Packet6SpawnPosition(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z));
        netserverhandler.sendPacket(new Packet202Abilities(entityplayer.abilities));
        netserverhandler.minecraftServer.serverConfigurationManager.a(entityplayer, worldserver);
        // this.server.serverConfigurationManager.sendAll(new Packet3Chat("\u00A7e" + entityplayer.name + " joined the game.")); // CraftBukkit - message moved to join event
        netserverhandler.minecraftServer.serverConfigurationManager.c(entityplayer);
        netserverhandler.a(entityplayer.locX, entityplayer.locY, entityplayer.locZ, entityplayer.yaw, entityplayer.pitch);
        netserverhandler.sendPacket(new Packet4UpdateTime(entityplayer.getPlayerTime())); // CraftBukkit - add support for player specific time
        Iterator iterator = entityplayer.getEffects().iterator();

        while (iterator.hasNext()) {
            MobEffect mobeffect = (MobEffect) iterator.next();

            netserverhandler.sendPacket(new Packet41MobEffect(entityplayer.id, mobeffect));
        }

        entityplayer.syncInventory();
        FMLBukkitHandler.instance().announceLogin(entityplayer);
    }

    @Override
    public void sendPacket(NetworkManager network, Packet packet) 
    {
        NetServerHandler net = (NetServerHandler)network.getNetHandler();
        net.sendPacket(packet);
    }
}
