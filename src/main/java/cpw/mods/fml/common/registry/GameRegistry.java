package cpw.mods.fml.common.registry;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

import net.minecraft.server.BiomeBase;
import net.minecraft.server.CraftingManager;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Item;
import net.minecraft.server.RecipesFurnace;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IInventory;
import net.minecraft.server.IRecipe;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemStack;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.WorldType;

import mcpc.com.google.common.base.Function;
import mcpc.com.google.common.collect.ArrayListMultimap;
import mcpc.com.google.common.collect.Lists;
import mcpc.com.google.common.collect.MapDifference;
import mcpc.com.google.common.collect.Maps;
import mcpc.com.google.common.collect.Multimap;
import mcpc.com.google.common.collect.Multimaps;
import mcpc.com.google.common.collect.Sets;
import mcpc.com.google.common.collect.Sets.SetView;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.IDispenseHandler;
import cpw.mods.fml.common.IDispenserHandler;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.IPickupNotifier;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.Mod.Block;
import cpw.mods.fml.common.ModContainer;

public class GameRegistry
{
    private static Multimap<ModContainer, BlockProxy> blockRegistry = ArrayListMultimap.create();
    private static Set<IWorldGenerator> worldGenerators = Sets.newHashSet();
    private static List<IFuelHandler> fuelHandlers = Lists.newArrayList();
    private static List<ICraftingHandler> craftingHandlers = Lists.newArrayList();
    private static List<IPickupNotifier> pickupHandlers = Lists.newArrayList();
    private static List<IPlayerTracker> playerTrackers = Lists.newArrayList();

    /**
     * Register a world generator - something that inserts new block types into the world
     *
     * @param generator
     */
    public static void registerWorldGenerator(IWorldGenerator generator)
    {
        worldGenerators.add(generator);
    }

    /**
     * Callback hook for world gen - if your mod wishes to add extra mod related generation to the world
     * call this
     *
     * @param chunkX
     * @param chunkZ
     * @param world
     * @param chunkGenerator
     * @param chunkProvider
     */
    public static void generateWorld(int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        long worldSeed = world.getSeed();
        Random fmlRandom = new Random(worldSeed);
        long xSeed = fmlRandom.nextLong() >> 2 + 1L;
        long zSeed = fmlRandom.nextLong() >> 2 + 1L;
        fmlRandom.setSeed((xSeed * chunkX + zSeed * chunkZ) ^ worldSeed);

        for (IWorldGenerator generator : worldGenerators)
        {
            generator.generate(fmlRandom, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    /**
     * Deprecated without replacement. Use vanilla DispenserRegistry code
     *
     * @param handler
     */
    @Deprecated
    public static void registerDispenserHandler(IDispenserHandler handler)
    {
    }
    /**
     * Deprecated without replacement. Use vanilla DispenserRegistry code
     *
     * @param handler
     */
    @Deprecated
    public static void registerDispenserHandler(final IDispenseHandler handler)
    {
    }


    /**
     *
     * Deprecated without replacement, use vanilla DispenserRegistry code
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param xVelocity
     * @param zVelocity
     * @param item
     */
    @Deprecated
    public static int tryDispense(World world, int x, int y, int z, int xVelocity, int zVelocity, ItemStack item, Random random, double entX, double entY, double entZ)
    {
        return -1;
    }
    /**
     * Internal method for creating an @Block instance
     * @param container
     * @param type
     * @param annotation
     * @throws Exception
     */
    public static Object buildBlock(ModContainer container, Class<?> type, Block annotation) throws Exception
    {
        Object o = type.getConstructor(int.class).newInstance(findSpareBlockId());
        registerBlock((net.minecraft.server.Block) o);
        return o;
    }

    /**
     * Private and not yet working properly
     *
     * @return
     */
    private static int findSpareBlockId()
    {
        return BlockTracker.nextBlockId();
    }

    /**
     * Register an item with the item registry with a custom name : this allows for easier server->client resolution
     *
     * @param item The item to register
     * @param name The mod-unique name of the item
     */
    public static void registerItem(net.minecraft.server.Item item, String name)
    {
        registerItem(item, name, null);
    }

    /**
     * Register the specified Item with a mod specific name : overrides the standard type based name
     * @param item The item to register
     * @param name The mod-unique name to register it as - null will remove a custom name
     * @param modId An optional modId that will "own" this block - generally used by multi-mod systems
     * where one mod should "own" all the blocks of all the mods, null defaults to the active mod
     */
    public static void registerItem(net.minecraft.server.Item item, String name, String modId)
    {
        GameData.setName(item, name, modId);
    }

    /**
     * Register a block with the world
     *
     */
    @Deprecated
    public static void registerBlock(net.minecraft.server.Block block)
    {
        registerBlock(block, ItemBlock.class);
    }


    /**
     * Register a block with the specified mod specific name : overrides the standard type based name
     * @param block The block to register
     * @param name The mod-unique name to register it as
     */
    public static void registerBlock(net.minecraft.server.Block block, String name)
    {
        registerBlock(block, ItemBlock.class, name);
    }

    /**
     * Register a block with the world, with the specified item class
     *
     * Deprecated in favour of named versions
     *
     * @param block The block to register
     * @param itemclass The item type to register with it
     */
    @Deprecated
    public static void registerBlock(net.minecraft.server.Block block, Class<? extends ItemBlock> itemclass)
    {
        registerBlock(block, itemclass, null);
    }
    /**
     * Register a block with the world, with the specified item class and block name
     * @param block The block to register
     * @param itemclass The item type to register with it
     * @param name The mod-unique name to register it with
     */
    public static void registerBlock(net.minecraft.server.Block block, Class<? extends ItemBlock> itemclass, String name)
    {
        registerBlock(block, itemclass, name, null);
    }
    /**
     * Register a block with the world, with the specified item class, block name and owning modId
     * @param block The block to register
     * @param itemclass The iterm type to register with it
     * @param name The mod-unique name to register it with
     * @param modId The modId that will own the block name. null defaults to the active modId
     */
    public static void registerBlock(net.minecraft.server.Block block, Class<? extends ItemBlock> itemclass, String name, String modId)
    {
        if (Loader.instance().isInState(LoaderState.CONSTRUCTING))
        {
            FMLLog.warning("The mod %s is attempting to register a block whilst it it being constructed. This is bad modding practice - please use a proper mod lifecycle event.", Loader.instance().activeModContainer());
        }
        try
        {
            assert block != null : "registerBlock: block cannot be null";
            assert itemclass != null : "registerBlock: itemclass cannot be null";
            int blockItemId = block.id - 256;
            Item i = itemclass.getConstructor(int.class).newInstance(blockItemId);
            GameRegistry.registerItem(i,name, modId);
        }
        catch (Exception e)
        {
            FMLLog.log(Level.SEVERE, e, "Caught an exception during block registration");
            throw new LoaderException(e);
        }
        blockRegistry.put(Loader.instance().activeModContainer(), (BlockProxy) block);
    }

    public static void addRecipe(ItemStack output, Object... params)
    {
        CraftingManager.getInstance().registerShapedRecipe(output, params);
    }

    public static void addShapelessRecipe(ItemStack output, Object... params)
    {
        CraftingManager.getInstance().registerShapelessRecipe(output, params);
    }

    public static void addRecipe(IRecipe recipe)
    {
        CraftingManager.getInstance().getRecipes().add(recipe);
    }

    public static void addSmelting(int input, ItemStack output, float xp)
    {
    	RecipesFurnace.getInstance().registerRecipe(input, output, xp);
    }

    public static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id)
    {
        TileEntity.a(tileEntityClass, id);
    }

    public static void addBiome(BiomeBase biome)
    {
        WorldType.NORMAL.addNewBiome(biome);
    }

    public static void removeBiome(BiomeBase biome)
    {
        WorldType.NORMAL.removeBiome(biome);
    }

    public static void registerFuelHandler(IFuelHandler handler)
    {
        fuelHandlers.add(handler);
    }
    public static int getFuelValue(ItemStack itemStack)
    {
        int fuelValue = 0;
        for (IFuelHandler handler : fuelHandlers)
        {
            fuelValue = Math.max(fuelValue, handler.getBurnTime(itemStack));
        }
        return fuelValue;
    }

    public static void registerCraftingHandler(ICraftingHandler handler)
    {
        craftingHandlers.add(handler);
    }

    public static void onItemCrafted(EntityHuman player, ItemStack item, IInventory craftMatrix)
    {
        for (ICraftingHandler handler : craftingHandlers)
        {
            handler.onCrafting(player, item, craftMatrix);
        }
    }

    public static void onItemSmelted(EntityHuman player, ItemStack item)
    {
        for (ICraftingHandler handler : craftingHandlers)
        {
            handler.onSmelting(player, item);
        }
    }

    public static void registerPickupHandler(IPickupNotifier handler)
    {
        pickupHandlers.add(handler);
    }

    public static void onPickupNotification(EntityHuman player, EntityItem item)
    {
        for (IPickupNotifier notify : pickupHandlers)
        {
            notify.notifyPickup(item, player);
        }
    }

    public static void registerPlayerTracker(IPlayerTracker tracker)
	{
		playerTrackers.add(tracker);
	}

	public static void onPlayerLogin(EntityHuman player)
	{
		for(IPlayerTracker tracker : playerTrackers)
			tracker.onPlayerLogin(player);
	}

	public static void onPlayerLogout(EntityHuman player)
	{
		for(IPlayerTracker tracker : playerTrackers)
			tracker.onPlayerLogout(player);
	}

	public static void onPlayerChangedDimension(EntityHuman player)
	{
		for(IPlayerTracker tracker : playerTrackers)
			tracker.onPlayerChangedDimension(player);
	}

	public static void onPlayerRespawn(EntityHuman player)
	{
		for(IPlayerTracker tracker : playerTrackers)
			tracker.onPlayerRespawn(player);
	}

}
