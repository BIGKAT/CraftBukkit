package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.material.MaterialData;
// CraftBukkit end

public class BlockFire extends Block {

	//FORGE
    private int[] a = Block.blockFireSpreadSpeed;
    private int[] b = Block.blockFlammability;

    protected BlockFire(int i, int j) {
        super(i, j, Material.FIRE);
        this.a(true);
    }

    public void j() {
        this.a(Block.WOOD.id, 5, 20);
        this.a(Block.FENCE.id, 5, 20);
        this.a(Block.WOOD_STAIRS.id, 5, 20);
        this.a(Block.LOG.id, 5, 5);
        this.a(Block.LEAVES.id, 30, 60);
        this.a(Block.BOOKSHELF.id, 30, 20);
        this.a(Block.TNT.id, 15, 100);
        this.a(Block.LONG_GRASS.id, 60, 100);
        this.a(Block.WOOL.id, 30, 60);
        this.a(Block.VINE.id, 15, 100);
    }

    private void a(int i, int j, int k) {
    	//FORGE
        Block.setBurnProperties(i, j, k);
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return null;
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int c() {
        return 3;
    }

    public int a(Random random) {
        return 0;
    }

    public int d() {
        return 30;
    }

    public void a(World world, int i, int j, int k, Random random) {
    	//FORGE
        Block base = Block.byId[world.getTypeId(i, j - 1, k)];
        boolean flag = (base != null && base.isFireSource(world, i, j - 1, k, world.getData(i, j - 1, k), 0));

        if (world.worldProvider instanceof WorldProviderTheEnd && world.getTypeId(i, j - 1, k) == Block.BEDROCK.id) {
            flag = true;
        }

        if (!this.canPlace(world, i, j, k)) {
            fireExtinguished(world, i, j, k);   // CraftBukkit - invalid place location
        }

        if (!flag && world.x() && (world.y(i, j, k) || world.y(i - 1, j, k) || world.y(i + 1, j, k) || world.y(i, j, k - 1) || world.y(i, j, k + 1))) {
            fireExtinguished(world, i, j, k);   // CraftBukkit - extinguished by rain
        } else {
            int l = world.getData(i, j, k);

            if (l < 15) {
                world.setRawData(i, j, k, l + random.nextInt(3) / 2);
            }

            world.c(i, j, k, this.id, this.d() + random.nextInt(10));
            if (!flag && !this.g(world, i, j, k)) {
                if (!world.isBlockSolidOnSide(i, j - 1, k, 1) || l > 3) {
                    fireExtinguished(world, i, j, k);   // CraftBukkit - burn out
                }
            } else if (!flag && !this.canBlockCatchFire(world, i, j - 1, k, 1) && l == 15 && random.nextInt(4) == 0) {
                fireExtinguished(world, i, j, k);   // CraftBukkit - burn out
            } else {
                boolean flag1 = world.z(i, j, k);
                byte b0 = 0;

                if (flag1) {
                    b0 = -50;
                }

                this.tryToCatchBlockOnFire(world, i + 1, j, k, 300 + b0, random, l, 4);
                this.tryToCatchBlockOnFire(world, i - 1, j, k, 300 + b0, random, l, 5);
                this.tryToCatchBlockOnFire(world, i, j - 1, k, 250 + b0, random, l, 1);
                this.tryToCatchBlockOnFire(world, i, j + 1, k, 250 + b0, random, l, 0);
                this.tryToCatchBlockOnFire(world, i, j, k - 1, 300 + b0, random, l, 3);
                this.tryToCatchBlockOnFire(world, i, j, k + 1, 300 + b0, random, l, 2);

                // CraftBukkit start - Call to stop spread of fire.
                org.bukkit.Server server = world.getServer();
                org.bukkit.World bworld = world.getWorld();

                IgniteCause igniteCause = BlockIgniteEvent.IgniteCause.SPREAD;
                org.bukkit.block.Block fromBlock = bworld.getBlockAt(i, j, k);
                // CraftBukkit end

                for (int i1 = i - 1; i1 <= i + 1; ++i1) {
                    for (int j1 = k - 1; j1 <= k + 1; ++j1) {
                        for (int k1 = j - 1; k1 <= j + 4; ++k1) {
                            if (i1 != i || k1 != j || j1 != k) {
                                int l1 = 100;

                                if (k1 > j + 1) {
                                    l1 += (k1 - (j + 1)) * 100;
                                }

                                int i2 = this.h(world, i1, k1, j1);

                                if (i2 > 0) {
                                    int j2 = (i2 + 40) / (l + 30);

                                    if (flag1) {
                                        j2 /= 2;
                                    }

                                    if (j2 > 0 && random.nextInt(l1) <= j2 && (!world.x() || !world.y(i1, k1, j1)) && !world.y(i1 - 1, k1, k) && !world.y(i1 + 1, k1, j1) && !world.y(i1, k1, j1 - 1) && !world.y(i1, k1, j1 + 1)) {
                                        int k2 = l + random.nextInt(5) / 4;

                                        if (k2 > 15) {
                                            k2 = 15;
                                        }
                                        // CraftBukkit start - Call to stop spread of fire.
                                        org.bukkit.block.Block block = bworld.getBlockAt(i1, k1, j1);

                                        if (block.getTypeId() != Block.FIRE.id) {
                                            BlockIgniteEvent event = new BlockIgniteEvent(block, igniteCause, null);
                                            server.getPluginManager().callEvent(event);

                                            if (event.isCancelled()) {
                                                continue;
                                            }

                                            org.bukkit.block.BlockState blockState = bworld.getBlockAt(i1, k1, j1).getState();
                                            blockState.setTypeId(this.id);
                                            blockState.setData(new MaterialData(this.id, (byte) k2));

                                            BlockSpreadEvent spreadEvent = new BlockSpreadEvent(blockState.getBlock(), fromBlock, blockState);
                                            server.getPluginManager().callEvent(spreadEvent);

                                            if (!spreadEvent.isCancelled()) {
                                                blockState.update(true);
                                            }
                                        }
                                        // CraftBukkit end
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Deprecated
    private void a(World world, int i, int j, int k, int l, Random random, int i1) {
        tryToCatchBlockOnFire(world, i, j, k, l, random, i1, 0);
    }
    
    private void tryToCatchBlockOnFire(World world, int i, int j, int k, int l, Random random, int i1, int face)
    {
        int j1 = 0;
        Block block = Block.byId[world.getTypeId(i, j, k)];
        if (block != null)
        {
            j1 = block.getFlammability(world, i, j, k, world.getData(i, j, k), face);
        }

        //int j1 = this.b[world.getTypeId(i, j, k)];

        if (random.nextInt(l) < j1) {
            boolean flag = world.getTypeId(i, j, k) == Block.TNT.id;
            // CraftBukkit start
            org.bukkit.block.Block theBlock = world.getWorld().getBlockAt(i, j, k);

            BlockBurnEvent event = new BlockBurnEvent(theBlock);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (random.nextInt(i1 + 10) < 5 && !world.y(i, j, k)) {
                int k1 = i1 + random.nextInt(5) / 4;

                if (k1 > 15) {
                    k1 = 15;
                }

                world.setTypeIdAndData(i, j, k, this.id, k1);
            } else {
                world.setTypeId(i, j, k, 0);
            }

            if (flag) {
                Block.TNT.postBreak(world, i, j, k, 1);
            }
        }
    }

    private boolean g(World par1World, int par2, int par3, int par4) {
        return this.canBlockCatchFire(par1World, par2 + 1, par3, par4, 4) ||
               this.canBlockCatchFire(par1World, par2 - 1, par3, par4, 5) ||
               this.canBlockCatchFire(par1World, par2, par3 - 1, par4, 1) ||
               this.canBlockCatchFire(par1World, par2, par3 + 1, par4, 0) ||
               this.canBlockCatchFire(par1World, par2, par3, par4 - 1, 3) ||
               this.canBlockCatchFire(par1World, par2, par3, par4 + 1, 2);
    }

    private int h(World world, int i, int j, int k) {
        byte b0 = 0;

        if (!world.isEmpty(i, j, k)) {
            return 0;
        } else {
            /*int l = this.f(world, i + 1, j, k, b0);

            l = this.f(world, i - 1, j, k, l);
            l = this.f(world, i, j - 1, k, l);
            l = this.f(world, i, j + 1, k, l);
            l = this.f(world, i, j, k - 1, l);
            l = this.f(world, i, j, k + 1, l);*/
            
            int var6 = this.getChanceToEncourageFire(world, i + 1, j, k, b0, 4);
            var6 = this.getChanceToEncourageFire(world, i - 1, j, k, var6, 5);
            var6 = this.getChanceToEncourageFire(world, i, j - 1, k, var6, 1);
            var6 = this.getChanceToEncourageFire(world, i, j + 1, k, var6, 0);
            var6 = this.getChanceToEncourageFire(world, i, j, k - 1, var6, 3);
            var6 = this.getChanceToEncourageFire(world, i, j, k + 1, var6, 2);

            return var6;
        }
    }

    public boolean F_() {
        return false;
    }

    @Deprecated
    public boolean c(IBlockAccess iblockaccess, int i, int j, int k) {
        //return this.a[iblockaccess.getTypeId(i, j, k)] > 0;
        return canBlockCatchFire(iblockaccess, i, j, k, 0);
    }

    @Deprecated
    public int f(World world, int i, int j, int k, int l) {
        /*int i1 = this.a[world.getTypeId(i, j, k)];

        return i1 > l ? i1 : l;*/
        return getChanceToEncourageFire(world, i, j, k, l, 0);
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.isBlockSolidOnSide(i, j - 1, k, 1) || this.g(world, i, j, k);
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isBlockSolidOnSide(i, j - 1, k, 1) && !this.g(world, i, j, k)) {
            fireExtinguished(world, i, j, k);   // CraftBukkit - fuel block gone
        }
    }

    public void onPlace(World world, int i, int j, int k) {
        if (world.worldProvider.dimension > 0 || world.getTypeId(i, j - 1, k) != Block.OBSIDIAN.id || !Block.PORTAL.b_(world, i, j, k)) {
            if (!world.isBlockSolidOnSide(i, j - 1, k, 1) && !this.g(world, i, j, k)) {
                fireExtinguished(world, i, j, k);   // CraftBukkit - fuel block broke
            } else {
                world.c(i, j, k, this.id, this.d() + world.random.nextInt(10));
            }
        }
    }
    // CraftBukkit start
    private void fireExtinguished(World world, int x, int y, int z) {
        if (CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(x, y, z), 0).isCancelled() == false) {
            world.setTypeId(x, y, z, 0);
        }
    }
    // CraftBukkit end

    /**
     * Side sensitive version that calls the block function.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param face The side the fire is coming from
     * @return True if the face can catch fire.
     */
    public boolean canBlockCatchFire(IBlockAccess world, int x, int y, int z, int face)
    {
        Block block = Block.byId[world.getTypeId(x, y, z)];
        if (block != null)
        {
            return block.isFlammable(world, x, y, z, world.getData(x, y, z), face);
        }
        return false;
    }
    
    /**
     * Side sensitive version that calls the block function.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param oldChance The previous maximum chance.
     * @param face The side the fire is coming from
     * @return The chance of the block catching fire, or oldChance if it is higher
     */
    public int getChanceToEncourageFire(World world, int x, int y, int z, int oldChance, int face)
    {
        int newChance = 0;
        Block block = Block.byId[world.getTypeId(x, y, z)];
        if (block != null)
        {
            newChance = block.getFireSpreadSpeed(world, x, y, z, world.getData(x, y, z), face);
        }
        return (newChance > oldChance ? newChance : oldChance);
    }  
}
