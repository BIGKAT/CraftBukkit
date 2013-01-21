package net.minecraft.world.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

// CraftBukkit start
import java.util.UUID;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
// CraftBukkit end

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public byte scale;

    /** colours */
    public byte[] colors = new byte[16384];

    /**
     * Holds a reference to the MapInfo of the players who own a copy of the map
     */
    public List playersArrayList = new ArrayList();

    /**
     * Holds a reference to the players who own a copy of the map and a reference to their MapInfo
     */
    private Map playersHashMap = new HashMap();
    public Map playersVisibleOnMap = new LinkedHashMap();

    // CraftBukkit start
    public final CraftMapView mapView;
    private CraftServer server;
    private UUID uniqueId = null;
    // CraftBukkit end

    public MapData(String par1Str)
    {
        super(par1Str);
        // CraftBukkit start
        mapView = new CraftMapView(this);
        server = (CraftServer) org.bukkit.Bukkit.getServer();
        // CraftBukkit end
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        // CraftBukkit start
        byte dimension = par1NBTTagCompound.getByte("dimension");

        if (dimension >= 10)
        {
            long least = par1NBTTagCompound.getLong("UUIDLeast");
            long most = par1NBTTagCompound.getLong("UUIDMost");

            if (least != 0L && most != 0L)
            {
                this.uniqueId = new UUID(most, least);
                CraftWorld world = (CraftWorld) server.getWorld(this.uniqueId);

                // Check if the stored world details are correct.
                if (world == null)
                {
                    /* All Maps which do not have their valid world loaded are set to a dimension which hopefully won't be reached.
                       This is to prevent them being corrupted with the wrong map data. */
                    dimension = 127;
                }
                else
                {
                    dimension = (byte) world.getHandle().dimension;
                }
            }
        }

        this.dimension = dimension;
        // CraftBukkit end
        this.xCenter = par1NBTTagCompound.getInteger("xCenter");
        this.zCenter = par1NBTTagCompound.getInteger("zCenter");
        this.scale = par1NBTTagCompound.getByte("scale");

        if (this.scale < 0)
        {
            this.scale = 0;
        }

        if (this.scale > 4)
        {
            this.scale = 4;
        }

        short var2 = par1NBTTagCompound.getShort("width");
        short var3 = par1NBTTagCompound.getShort("height");

        if (var2 == 128 && var3 == 128)
        {
            this.colors = par1NBTTagCompound.getByteArray("colors");
        }
        else
        {
            byte[] var4 = par1NBTTagCompound.getByteArray("colors");
            this.colors = new byte[16384];
            int var5 = (128 - var2) / 2;
            int var6 = (128 - var3) / 2;

            for (int var7 = 0; var7 < var3; ++var7)
            {
                int var8 = var7 + var6;

                if (var8 >= 0 || var8 < 128)
                {
                    for (int var9 = 0; var9 < var2; ++var9)
                    {
                        int var10 = var9 + var5;

                        if (var10 >= 0 || var10 < 128)
                        {
                            this.colors[var10 + var8 * 128] = var4[var9 + var7 * var2];
                        }
                    }
                }
            }
        }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        // CraftBukkit start
        if (this.dimension >= 10)
        {
            if (this.uniqueId == null)
            {
                for (org.bukkit.World world : server.getWorlds())
                {
                    CraftWorld cWorld = (CraftWorld) world;

                    if (cWorld.getHandle().dimension == this.dimension)
                    {
                        this.uniqueId = cWorld.getUID();
                        break;
                    }
                }
            }

            /* Perform a second check to see if a matching world was found, this is a necessary
               change incase Maps are forcefully unlinked from a World and lack a UID.*/
            if (this.uniqueId != null)
            {
                par1NBTTagCompound.setLong("UUIDLeast", this.uniqueId.getLeastSignificantBits());
                par1NBTTagCompound.setLong("UUIDMost", this.uniqueId.getMostSignificantBits());
            }
        }

        // CraftBukkit end
        par1NBTTagCompound.setByte("dimension", this.dimension);
        par1NBTTagCompound.setInteger("xCenter", this.xCenter);
        par1NBTTagCompound.setInteger("zCenter", this.zCenter);
        par1NBTTagCompound.setByte("scale", this.scale);
        par1NBTTagCompound.setShort("width", (short)128);
        par1NBTTagCompound.setShort("height", (short)128);
        par1NBTTagCompound.setByteArray("colors", this.colors);
    }

    /**
     * Adds the player passed to the list of visible players and checks to see which players are visible
     */
    public void updateVisiblePlayers(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
    {
        if (!this.playersHashMap.containsKey(par1EntityPlayer))
        {
            MapInfo var3 = new MapInfo(this, par1EntityPlayer);
            this.playersHashMap.put(par1EntityPlayer, var3);
            this.playersArrayList.add(var3);
        }

        if (!par1EntityPlayer.inventory.hasItemStack(par2ItemStack))
        {
            this.playersVisibleOnMap.remove(par1EntityPlayer.getCommandSenderName());
        }

        for (int var5 = 0; var5 < this.playersArrayList.size(); ++var5)
        {
            MapInfo var4 = (MapInfo)this.playersArrayList.get(var5);

            if (!var4.entityplayerObj.isDead && (var4.entityplayerObj.inventory.hasItemStack(par2ItemStack) || par2ItemStack.isOnItemFrame()))
            {
                if (!par2ItemStack.isOnItemFrame() && var4.entityplayerObj.dimension == this.dimension)
                {
                    this.func_82567_a(0, var4.entityplayerObj.worldObj, var4.entityplayerObj.getCommandSenderName(), var4.entityplayerObj.posX, var4.entityplayerObj.posZ, (double)var4.entityplayerObj.rotationYaw);
                }
            }
            else
            {
                this.playersHashMap.remove(var4.entityplayerObj);
                this.playersArrayList.remove(var4);
            }
        }

        if (par2ItemStack.isOnItemFrame())
        {
            this.func_82567_a(1, par1EntityPlayer.worldObj, "frame-" + par2ItemStack.getItemFrame().entityId, (double)par2ItemStack.getItemFrame().xPosition, (double)par2ItemStack.getItemFrame().zPosition, (double)(par2ItemStack.getItemFrame().hangingDirection * 90));
        }
    }

    private void func_82567_a(int par1, World par2World, String par3Str, double par4, double par6, double par8)
    {
        int var10 = 1 << this.scale;
        float var11 = (float)(par4 - (double)this.xCenter) / (float)var10;
        float var12 = (float)(par6 - (double)this.zCenter) / (float)var10;
        byte var13 = (byte)((int)((double)(var11 * 2.0F) + 0.5D));
        byte var14 = (byte)((int)((double)(var12 * 2.0F) + 0.5D));
        byte var16 = 63;
        byte var15;

        if (var11 >= (float)(-var16) && var12 >= (float)(-var16) && var11 <= (float)var16 && var12 <= (float)var16)
        {
            par8 += par8 < 0.0D ? -8.0D : 8.0D;
            var15 = (byte)((int)(par8 * 16.0D / 360.0D));

            if (this.dimension < 0)
            {
                int var17 = (int)(par2World.getWorldInfo().getWorldTime() / 10L);
                var15 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 15);
            }
        }
        else
        {
            if (Math.abs(var11) >= 320.0F || Math.abs(var12) >= 320.0F)
            {
                this.playersVisibleOnMap.remove(par3Str);
                return;
            }

            par1 = 6;
            var15 = 0;

            if (var11 <= (float)(-var16))
            {
                var13 = (byte)((int)((double)(var16 * 2) + 2.5D));
            }

            if (var12 <= (float)(-var16))
            {
                var14 = (byte)((int)((double)(var16 * 2) + 2.5D));
            }

            if (var11 >= (float)var16)
            {
                var13 = (byte)(var16 * 2 + 1);
            }

            if (var12 >= (float)var16)
            {
                var14 = (byte)(var16 * 2 + 1);
            }
        }

        this.playersVisibleOnMap.put(par3Str, new MapCoord(this, (byte)par1, var13, var14, var15));
    }

    /**
     * Get byte array of packet data to send to players on map for updating map data
     */
    public byte[] getUpdatePacketData(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        MapInfo var4 = (MapInfo)this.playersHashMap.get(par3EntityPlayer);
        return var4 == null ? null : var4.getPlayersOnMap(par1ItemStack);
    }

    /**
     * Marks a vertical range of pixels as being modified so they will be resent to clients. Parameters: X, lowest Y,
     * highest Y
     */
    public void setColumnDirty(int par1, int par2, int par3)
    {
        super.markDirty();

        for (int var4 = 0; var4 < this.playersArrayList.size(); ++var4)
        {
            MapInfo var5 = (MapInfo)this.playersArrayList.get(var4);

            if (var5.field_76209_b[par1] < 0 || var5.field_76209_b[par1] > par2)
            {
                var5.field_76209_b[par1] = par2;
            }

            if (var5.field_76210_c[par1] < 0 || var5.field_76210_c[par1] < par3)
            {
                var5.field_76210_c[par1] = par3;
            }
        }
    }

    public MapInfo func_82568_a(EntityPlayer par1EntityPlayer)
    {
        MapInfo var2 = (MapInfo)this.playersHashMap.get(par1EntityPlayer);

        if (var2 == null)
        {
            var2 = new MapInfo(this, par1EntityPlayer);
            this.playersHashMap.put(par1EntityPlayer, var2);
            this.playersArrayList.add(var2);
        }

        return var2;
    }
}
