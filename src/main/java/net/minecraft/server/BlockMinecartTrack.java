package net.minecraft.server;

import java.util.Random;

import net.minecraft.server.EntityMinecart;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.World;
import net.minecraftforge.common.ForgeDirection;
import static net.minecraftforge.common.ForgeDirection.*;

public class BlockMinecartTrack extends Block
{
    /** Power related rails have this field at true. */
    private final boolean a;
    private int renderType = 9;

    public void setRenderType(int var1)
    {
        this.renderType = var1;
    }

    /**
     * Returns true if the block at the coordinates of world passed is a valid rail block (current is rail, powered or
     * detector).
     */
    public static final boolean e_(World var0, int var1, int var2, int var3)
    {
        int var4 = var0.getTypeId(var1, var2, var3);
        return d(var4); // Forge
    }

    /**
     * Return true if the parameter is a blockID for a valid rail block (current is rail, powered or detector).
     */
    public static final boolean d(int var0)
    {
        return Block.byId[var0] instanceof BlockMinecartTrack; // Forge
    }

    protected BlockMinecartTrack(int var1, int var2, boolean var3)
    {
        super(var1, var2, Material.ORIENTABLE);
        this.a = var3;
        this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.a(CreativeModeTab.e);
    }

    /**
     * Returns true if the block is power related rail.
     */
    public boolean p()
    {
        return this.a;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB e(World var1, int var2, int var3, int var4)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean c()
    {
        return false;
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition a(World var1, int var2, int var3, int var4, Vec3D var5, Vec3D var6)
    {
        this.updateShape(var1, var2, var3, var4);
        return super.a(var1, var2, var3, var4, var5, var6);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void updateShape(IBlockAccess var1, int var2, int var3, int var4)
    {
        int var5 = var1.getData(var2, var3, var4);

        if (var5 >= 2 && var5 <= 5)
        {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int a(int var1, int var2)
    {
        if (this.a)
        {
            if (this.id == Block.GOLDEN_RAIL.id && (var2 & 8) == 0)
            {
                return this.textureId - 16;
            }
        }
        else if (var2 >= 6)
        {
            return this.textureId - 16;
        }

        return this.textureId;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean b()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int d()
    {
        return this.renderType; // Forge
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int a(Random var1)
    {
        return 1;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlace(World var1, int var2, int var3, int var4)
    {
        return var1.isBlockSolidOnSide(var2, var3 - 1, var4, UP); // Forge
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onPlace(World var1, int var2, int var3, int var4)
    {
        if (!var1.isStatic)
        {
            this.a(var1, var2, var3, var4, true);

            if (this.id == Block.GOLDEN_RAIL.id)
            {
                this.doPhysics(var1, var2, var3, var4, this.id);
            }
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void doPhysics(World var1, int var2, int var3, int var4, int var5)
    {
        if (!var1.isStatic)
        {
            int var6 = var1.getData(var2, var3, var4);
            int var7 = var6;

            if (this.a)
            {
                var7 = var6 & 7;
            }

            boolean var8 = false;
            // Forge start
            if (!var1.isBlockSolidOnSide(var2, var3 - 1, var4, UP)) 
            {
                var8 = true;
            }

            if (var7 == 2 && !var1.isBlockSolidOnSide(var2 + 1, var3, var4, UP))
            {
                var8 = true;
            }

            if (var7 == 3 && !var1.isBlockSolidOnSide(var2 - 1, var3, var4, UP))
            {
                var8 = true;
            }

            if (var7 == 4 && !var1.isBlockSolidOnSide(var2, var3, var4 - 1, UP))
            {
                var8 = true;
            }

            if (var7 == 5 && !var1.isBlockSolidOnSide(var2, var3, var4 + 1, UP))
            {
                var8 = true;
            }
            // Forge end
            if (var8)
            {
                this.c(var1, var2, var3, var4, var1.getData(var2, var3, var4), 0);
                var1.setTypeId(var2, var3, var4, 0);
            }
            else if (this.id == Block.GOLDEN_RAIL.id)
            {
                boolean var9 = var1.isBlockIndirectlyPowered(var2, var3, var4);
                var9 = var9 || this.a(var1, var2, var3, var4, var6, true, 0) || this.a(var1, var2, var3, var4, var6, false, 0);
                boolean var10 = false;

                if (var9 && (var6 & 8) == 0)
                {
                    var1.setData(var2, var3, var4, var7 | 8);
                    var10 = true;
                }
                else if (!var9 && (var6 & 8) != 0)
                {
                    var1.setData(var2, var3, var4, var7);
                    var10 = true;
                }

                if (var10)
                {
                    var1.applyPhysics(var2, var3 - 1, var4, this.id);

                    if (var7 == 2 || var7 == 3 || var7 == 4 || var7 == 5)
                    {
                        var1.applyPhysics(var2, var3 + 1, var4, this.id);
                    }
                }
            }
            else if (var5 > 0 && Block.byId[var5].isPowerSource() && !this.a && MinecartTrackLogic.a(new MinecartTrackLogic(this, var1, var2, var3, var4)) == 3)
            {
                this.a(var1, var2, var3, var4, false);
            }
        }
    }

    /**
     * Completely recalculates the track shape based on neighboring tracks
     */
    private void a(World var1, int var2, int var3, int var4, boolean var5)
    {
        if (!var1.isStatic)
        {
            (new MinecartTrackLogic(this, var1, var2, var3, var4)).a(var1.isBlockIndirectlyPowered(var2, var3, var4), var5);
        }
    }

    /**
     * Powered minecart rail is conductive like wire, so check for powered neighbors
     */
    private boolean a(World var1, int var2, int var3, int var4, int var5, boolean var6, int var7)
    {
        if (var7 >= 8)
        {
            return false;
        }
        else
        {
            int var8 = var5 & 7;
            boolean var9 = true;

            switch (var8)
            {
                case 0:
                    if (var6)
                    {
                        ++var4;
                    }
                    else
                    {
                        --var4;
                    }

                    break;

                case 1:
                    if (var6)
                    {
                        --var2;
                    }
                    else
                    {
                        ++var2;
                    }

                    break;

                case 2:
                    if (var6)
                    {
                        --var2;
                    }
                    else
                    {
                        ++var2;
                        ++var3;
                        var9 = false;
                    }

                    var8 = 1;
                    break;

                case 3:
                    if (var6)
                    {
                        --var2;
                        ++var3;
                        var9 = false;
                    }
                    else
                    {
                        ++var2;
                    }

                    var8 = 1;
                    break;

                case 4:
                    if (var6)
                    {
                        ++var4;
                    }
                    else
                    {
                        --var4;
                        ++var3;
                        var9 = false;
                    }

                    var8 = 0;
                    break;

                case 5:
                    if (var6)
                    {
                        ++var4;
                        ++var3;
                        var9 = false;
                    }
                    else
                    {
                        --var4;
                    }

                    var8 = 0;
            }

            return this.a(var1, var2, var3, var4, var6, var7, var8) ? true : var9 && this.a(var1, var2, var3 - 1, var4, var6, var7, var8);
        }
    }

    /**
     * Returns true if the specified rail is passing power to its neighbor
     */
    private boolean a(World var1, int var2, int var3, int var4, boolean var5, int var6, int var7)
    {
        int var8 = var1.getTypeId(var2, var3, var4);

        if (var8 == Block.GOLDEN_RAIL.id)
        {
            int var9 = var1.getData(var2, var3, var4);
            int var10 = var9 & 7;

            if (var7 == 1 && (var10 == 0 || var10 == 4 || var10 == 5))
            {
                return false;
            }

            if (var7 == 0 && (var10 == 1 || var10 == 2 || var10 == 3))
            {
                return false;
            }

            if ((var9 & 8) != 0)
            {
                if (var1.isBlockIndirectlyPowered(var2, var3, var4))
                {
                    return true;
                }

                return this.a(var1, var2, var3, var4, var9, var5, var6 + 1);
            }
        }

        return false;
    }

    /**
     * Returns the mobility information of the block, 0 = free, 1 = can't push but can move over, 2 = total immobility
     * and stop pistons
     */
    public int q_()
    {
        return 0;
    }

    /**
     * This function is no longer called by Minecraft
     */
    @Deprecated
    static boolean a(BlockMinecartTrack blockrail)
    {
        return blockrail.a;
    }

    /**
     * Return true if the rail can make corners.
     * Used by placement logic.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the rail can make corners.
     */
    public boolean isFlexibleRail(World world, int y, int x, int z)
    {
        return !this.a;
    }

    /**
     * Returns true if the rail can make up and down slopes.
     * Used by placement logic.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the rail can make slopes.
     */
    public boolean canMakeSlopes(World world, int x, int y, int z)
    {
        return true;
    }

    /**
     * Return the rails metadata (without the power bit if the rail uses one).
     * Can be used to make the cart think the rail something other than it is,
     * for example when making diamond junctions or switches.
     * The cart parameter will often be null unless it it called from EntityMinecart.
     * 
     * Valid rail metadata is defined as follows:
     * 0x0: flat track going North-South
     * 0x1: flat track going West-East
     * 0x2: track ascending to the East
     * 0x3: track ascending to the West
     * 0x4: track ascending to the North
     * 0x5: track ascending to the South
     * 0x6: WestNorth corner (connecting East and South)
     * 0x7: EastNorth corner (connecting West and South)
     * 0x8: EastSouth corner (connecting West and North)
     * 0x9: WestSouth corner (connecting East and North)
     * 
     * All directions are Notch defined.
     * In MC Beta 1.8.3 the Sun rises in the North.
     * In MC 1.0.0 the Sun rises in the East.
     * 
     * @param world The world.
     * @param cart The cart asking for the metadata, null if it is not called by EntityMinecart.
     * @param y The rail X coordinate.
     * @param x The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return The metadata.
     */
    public int getBasicRailMetadata(IBlockAccess world, EntityMinecart cart, int x, int y, int z)
    {
        int meta = world.getData(x, y, z);
        if(this.a)
        {
            meta = meta & 7;
        }
        return meta;
    }

    /**
     * Returns the max speed of the rail at the specified position.
     * @param world The world.
     * @param cart The cart on the rail, may be null.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return The max speed of the current rail.
     */
    public float getRailMaxSpeed(World world, EntityMinecart cart, int y, int x, int z)
    {
        return 0.4f;
    }

    /**
     * This function is called by any minecart that passes over this rail.
     * It is called once per update tick that the minecart is on the rail.
     * @param world The world.
     * @param cart The cart on the rail.
     * @param y The rail X coordinate.
     * @param x The rail Y coordinate.
     * @param z The rail Z coordinate.
     */
    public void onMinecartPass(World world, EntityMinecart cart, int y, int x, int z)
    {
    }

    /**
     * Return true if this rail uses the 4th bit as a power bit.
     * Avoid using this function when getBasicRailMetadata() can be used instead.
     * The only reason to use this function is if you wish to change the rails metadata.
     * @param world The world.
     * @param x The rail X coordinate.
     * @param y The rail Y coordinate.
     * @param z The rail Z coordinate.
     * @return True if the 4th bit is a power bit.
     */
    public boolean hasPowerBit(World world, int x, int y, int z)
    {
        return this.a;
    }
}
