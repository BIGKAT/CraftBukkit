package forge;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.server.*;
import forge.packets.*;

public class PacketHandlerServer implements IPacketHandler
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
}