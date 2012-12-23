package net.minecraft.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
// Forge start
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
// Forge end

import org.bukkit.inventory.InventoryHolder; // CraftBukkit

public class TileEntity {

    private static Map a = new HashMap();
    private static Map b = new HashMap();
    public World world; // CraftBukkit - protected -> public
    public int x;
    public int y;
    public int z;
    protected boolean o;
    public int p = -1;
    public Block q;

    public TileEntity() {}

    public static void a(Class oclass, String s) {
        if (a.containsKey(s)) {
            throw new IllegalArgumentException("Duplicate id: " + s);
        } else {
            a.put(s, oclass);
            b.put(oclass, s);
        }
    }

    // Forge start
    /**
     * Determines if this TileEntity requires update calls.
     * @return True if you want updateEntity() to be called, false if not
     */
    public boolean canUpdate()
    {
        return true;
    }

    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for 
     * sending the packet.
     * 
     * @param net The NetworkManager the packet originated from 
     * @param pkt The data packet
     */
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {}

    /**
     * Called when the chunk this TileEntity is on is Unloaded.
     */
    public void onChunkUnload() {}

    /**
     * Called from Chunk.setBlockIDWithMetadata, determines if this tile entity should be re-created when the ID, or Metadata changes.
     * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
     * 
     * @param oldID The old ID of the block
     * @param newID The new ID of the block (May be the same)
     * @param oldMeta The old metadata of the block
     * @param newMeta The new metadata of the block (May be the same)
     * @param world Current world 
     * @param x X Postion
     * @param y Y Position
     * @param z Z Position
     * @return True to remove the old tile entity, false to keep it in tact {and create a new one if the new values specify to}
     */
    public boolean shouldRefresh(int oldID, int newID, int oldMeta, int newMeta, World world, int x, int y, int z)
    {
        return true;
    }
    // Forge end
    
    public void b(World world) {
        this.world = world;
    }

    public boolean o() {
        return this.world != null;
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.x = nbttagcompound.getInt("x");
        this.y = nbttagcompound.getInt("y");
        this.z = nbttagcompound.getInt("z");
    }

    public void b(NBTTagCompound nbttagcompound) {
        String s = (String) b.get(this.getClass());

        if (s == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbttagcompound.setString("id", s);
            nbttagcompound.setInt("x", this.x);
            nbttagcompound.setInt("y", this.y);
            nbttagcompound.setInt("z", this.z);
        }
    }

    public void g() {}

    public static TileEntity c(NBTTagCompound nbttagcompound) {
        TileEntity tileentity = null;
        Class oclass = null; // Forge

        try {
            oclass = (Class) a.get(nbttagcompound.getString("id")); // Forge

            if (oclass != null) {
                tileentity = (TileEntity) oclass.newInstance();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (tileentity != null) {
            // Forge start
            try {
            tileentity.a(nbttagcompound);
            }
            catch (Exception e) {
                FMLLog.log(Level.SEVERE, e,
                        "A TileEntity %s(%s) has thrown an exception during loading, its state cannot be restored. Report this to the mod author",
                        nbttagcompound.getString("id"), oclass.getName());
                tileentity = null;
            }
            // Forge end
        } else {
            System.out.println("Skipping TileEntity with id " + nbttagcompound.getString("id"));
        }

        return tileentity;
    }

    public int p() {
        if (this.p == -1) {
            this.p = this.world.getData(this.x, this.y, this.z);
        }

        return this.p;
    }

    public void update() {
        if (this.world != null) {
            this.p = this.world.getData(this.x, this.y, this.z);
            this.world.b(this.x, this.y, this.z, this);
        }
    }

    public Block q() {
        if (this.q == null) {
            this.q = Block.byId[this.world.getTypeId(this.x, this.y, this.z)];
        }

        return this.q;
    }

    public Packet getUpdatePacket() {
        return null;
    }

    public boolean r() {
        return this.o;
    }

    public void w_() {
        this.o = true;
    }

    public void s() {
        this.o = false;
    }

    public void b(int i, int j) {}

    public void h() {
        this.q = null;
        this.p = -1;
    }

    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        crashreportsystemdetails.a("Name", (Callable) (new CrashReportTileEntityName(this)));
        CrashReportSystemDetails.a(crashreportsystemdetails, this.x, this.y, this.z, this.q != null ? this.q.id : 0, this.p); // Forge
    }

    static Map t() {
        return b;
    }

    static {
        a(TileEntityFurnace.class, "Furnace");
        a(TileEntityChest.class, "Chest");
        a(TileEntityEnderChest.class, "EnderChest");
        a(TileEntityRecordPlayer.class, "RecordPlayer");
        a(TileEntityDispenser.class, "Trap");
        a(TileEntitySign.class, "Sign");
        a(TileEntityMobSpawner.class, "MobSpawner");
        a(TileEntityNote.class, "Music");
        a(TileEntityPiston.class, "Piston");
        a(TileEntityBrewingStand.class, "Cauldron");
        a(TileEntityEnchantTable.class, "EnchantTable");
        a(TileEntityEnderPortal.class, "Airportal");
        a(TileEntityCommand.class, "Control");
        a(TileEntityBeacon.class, "Beacon");
        a(TileEntitySkull.class, "Skull");
    }

    // CraftBukkit start
    public InventoryHolder getOwner() {
        org.bukkit.block.BlockState state = world.getWorld().getBlockAt(x, y, z).getState();
        if (state instanceof InventoryHolder) return (InventoryHolder) state;
        return null;
    }
    // CraftBukkit end
}
