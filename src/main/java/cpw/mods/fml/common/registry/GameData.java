package cpw.mods.fml.common.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemWithAuxData;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

import mcpc.com.google.common.base.Function;
import mcpc.com.google.common.collect.MapDifference;
import mcpc.com.google.common.collect.Maps;
import mcpc.com.google.common.collect.Sets;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;

public class GameData {
    private static Map<Integer, ItemData> idMap = Maps.newHashMap();
    private static CountDownLatch serverValidationLatch;
    private static CountDownLatch clientValidationLatch;
    private static MapDifference<Integer, ItemData> difference;
    private static boolean shouldContinue = true;
    private static boolean isSaveValid = true;

    // MCPC - bukkit to vanilla obfuscated mappings
    // used with ModIdMapPacket
    private static final Map<String, String> bukkitToVanilla = new HashMap<String, String>();
    static {
    	// 1.4.5 mappings
    	// blocks
    	bukkitToVanilla.put("BlockAnvil", "ais");
    	bukkitToVanilla.put("BlockStone", "ame");
    	bukkitToVanilla.put("BlockGrass", "akc");
    	bukkitToVanilla.put("BlockDirt", "ajo");
    	bukkitToVanilla.put("Block", "amj");
    	bukkitToVanilla.put("BlockFlowing", "akr");
    	bukkitToVanilla.put("BlockStationary", "aks");
    	bukkitToVanilla.put("BlockSand", "akg");
    	bukkitToVanilla.put("BlockGravel", "akd");
    	bukkitToVanilla.put("BlockOre", "ale");
    	bukkitToVanilla.put("BlockSponge", "aly");
    	bukkitToVanilla.put("BlockGlass", "akb");
    	bukkitToVanilla.put("BlockDispenser", "ajp");
    	bukkitToVanilla.put("BlockNote", "aky");
    	bukkitToVanilla.put("BlockMinecartTrack", "alk");
    	bukkitToVanilla.put("BlockBed", "aiu");
    	bukkitToVanilla.put("BlockMinecartDetector", "ajl");
    	bukkitToVanilla.put("BlockWeb", "amz");
    	bukkitToVanilla.put("BlockPistonExtension", "ant");
    	bukkitToVanilla.put("BlockDeadBush", "ajk");
    	bukkitToVanilla.put("BlockFlower", "aix");
    	bukkitToVanilla.put("BlockMushroom", "akx");
    	bukkitToVanilla.put("BlockPistonMoving", "anu");
    	bukkitToVanilla.put("BlockOreBlock", "akv");
    	bukkitToVanilla.put("BlockTNT", "amo");
    	bukkitToVanilla.put("BlockBookshelf", "aiv");
    	bukkitToVanilla.put("BlockFire", "ajy");
    	bukkitToVanilla.put("BlockTorch", "amq");
    	bukkitToVanilla.put("BlockObsidian", "ald");
    	bukkitToVanilla.put("BlockRedstoneWire", "alo");
    	bukkitToVanilla.put("BlockChest", "ajd");
    	bukkitToVanilla.put("BlockStairs", "ama");
    	bukkitToVanilla.put("BlockMobSpawner", "akw");
    	bukkitToVanilla.put("BlockCrops", "ajj");
    	bukkitToVanilla.put("BlockWorkbench", "anc");
    	bukkitToVanilla.put("BlockSign", "alu");
    	bukkitToVanilla.put("BlockFurnace", "aka");
    	bukkitToVanilla.put("BlockSoil", "ajv");
    	bukkitToVanilla.put("BlockLever", "ako");
    	bukkitToVanilla.put("BlockPressurePlate", "alh");
    	bukkitToVanilla.put("BlockDoor", "ajq");
    	bukkitToVanilla.put("BlockLadder", "akl");
    	bukkitToVanilla.put("BlockRedstoneTorch", "alb");
    	bukkitToVanilla.put("BlockButton", "aiy");
    	bukkitToVanilla.put("BlockSnow", "amp");
    	bukkitToVanilla.put("BlockIce", "akk");
    	bukkitToVanilla.put("BlockRedstoneOre", "alp");
    	bukkitToVanilla.put("BlockFence", "ajx");
    	bukkitToVanilla.put("BlockJukeBox", "alm");
    	bukkitToVanilla.put("BlockBloodStone", "aki");
    	bukkitToVanilla.put("BlockPumpkin", "alj");
    	bukkitToVanilla.put("BlockCactus", "aiz");
    	bukkitToVanilla.put("BlockSnowBlock", "alx");
    	bukkitToVanilla.put("BlockReed", "alr");
    	bukkitToVanilla.put("BlockClay", "aje");
    	bukkitToVanilla.put("BlockDiode", "ajm");
    	bukkitToVanilla.put("BlockCake", "aja");
    	bukkitToVanilla.put("BlockLockedChest", "akt");
    	bukkitToVanilla.put("BlockLightStone", "akp");
    	bukkitToVanilla.put("BlockSlowSand", "akh");
    	bukkitToVanilla.put("BlockThinFence", "ami");
    	bukkitToVanilla.put("BlockMelon", "aku");
    	bukkitToVanilla.put("BlockHugeMushroom", "akj");
    	bukkitToVanilla.put("BlockTrapdoor", "ams");
    	bukkitToVanilla.put("BlockMycel", "akz");
    	bukkitToVanilla.put("BlockFenceGate", "ajw");
    	bukkitToVanilla.put("BlockStem", "amb");
    	bukkitToVanilla.put("BlockEnderPortal", "amg");
    	bukkitToVanilla.put("BlockCauldron", "ajc");
    	bukkitToVanilla.put("BlockBrewingStand", "aiw");
    	bukkitToVanilla.put("BlockEnchantmentTable", "ajs");
    	bukkitToVanilla.put("BlockNetherWart", "ala");
    	bukkitToVanilla.put("BlockCocoa", "ajg");
    	bukkitToVanilla.put("BlockRedstoneLamp", "alq");
    	bukkitToVanilla.put("BlockDragonEgg", "ajr");
    	bukkitToVanilla.put("BlockEnderPortalFrame", "amh");
    	bukkitToVanilla.put("BlockCommand", "ajh");
    	bukkitToVanilla.put("BlockBeacon", "ait");
    	bukkitToVanilla.put("BlockCarrots", "ajb");
    	bukkitToVanilla.put("BlockFlowerPot", "ajz");
    	bukkitToVanilla.put("BlockPotatoes", "alg");
    	bukkitToVanilla.put("BlockTripwireHook", "amu");
    	bukkitToVanilla.put("BlockEnderChest", "ajt");
    	bukkitToVanilla.put("BlockTripwire", "");
    	bukkitToVanilla.put("BlockSkull", "alv");
    	bukkitToVanilla.put("BlockCloth", "ajf");
    	bukkitToVanilla.put("BlockDirectional", "ajn");
    	bukkitToVanilla.put("BlockContainer", "aju");
    	bukkitToVanilla.put("BlockStepAbstract", "ake");
    	bukkitToVanilla.put("BlockHalfTransparant", "akf");
    	bukkitToVanilla.put("BlockLeaves", "akm");
    	bukkitToVanilla.put("BlockFluids", "akq");
    	bukkitToVanilla.put("BlockPortal", "alf");
    	bukkitToVanilla.put("BlockSapling", "alt");
    	bukkitToVanilla.put("BlockSmoothBrick", "alw");
    	bukkitToVanilla.put("BlockMonsterEggs", "amc");
    	bukkitToVanilla.put("BlockLongGrass", "amf");
    	bukkitToVanilla.put("BlockLog", "amt");
    	bukkitToVanilla.put("BlockVine", "amw");
    	bukkitToVanilla.put("BlockCobbleWall", "amx");
    	bukkitToVanilla.put("BlockWoodStep", "ana");
    	bukkitToVanilla.put("BlockTripwire", "amv");
    	
    	// items
    	bukkitToVanilla.put("Item", "uk");
    	bukkitToVanilla.put("ItemAnvil", "ss");
    	bukkitToVanilla.put("ItemArmor", "st");
    	bukkitToVanilla.put("ItemAxe", "ui");
    	bukkitToVanilla.put("ItemBed", "sw");
    	bukkitToVanilla.put("ItemBlock", "vl");
    	bukkitToVanilla.put("ItemBoat", "sx");
    	bukkitToVanilla.put("ItemBookAndQuill", "vq");
    	bukkitToVanilla.put("ItemBow", "sz");
    	bukkitToVanilla.put("ItemBucket", "tb");
    	bukkitToVanilla.put("ItemCarrotStick", "tc");
    	bukkitToVanilla.put("ItemCoal", "te");
    	bukkitToVanilla.put("ItemCloth", "td");
    	bukkitToVanilla.put("ItemDoor", "tv");
    	bukkitToVanilla.put("ItemDye", "tw");
    	bukkitToVanilla.put("ItemEgg", "tx");
    	bukkitToVanilla.put("ItemEnderEye", "tz");
    	bukkitToVanilla.put("ItemEnderPearl", "ua");
    	bukkitToVanilla.put("ItemExpBottle", "ub");
    	bukkitToVanilla.put("ItemFireball", "uc");
    	bukkitToVanilla.put("ItemFishingRod", "ud");
    	bukkitToVanilla.put("ItemFlintAndSteel", "ue");
    	bukkitToVanilla.put("ItemFood", "uf");
    	bukkitToVanilla.put("ItemGlassBottle", "sy");
    	bukkitToVanilla.put("ItemGoldenApple", "ug");
    	bukkitToVanilla.put("ItemHanging", "uh");
    	bukkitToVanilla.put("ItemHoe", "uj");
    	bukkitToVanilla.put("ItemLeaves", "un");
    	bukkitToVanilla.put("ItemMapEmpty", "ty");
    	bukkitToVanilla.put("ItemMilkBucket", "up");
    	bukkitToVanilla.put("ItemMinecart", "uq");
    	bukkitToVanilla.put("ItemMonsterEgg", "ur");
    	bukkitToVanilla.put("ItemMultiTexture", "us");
    	bukkitToVanilla.put("ItemNetherStar", "vg");
    	bukkitToVanilla.put("ItemPickaxe", "ut");
    	bukkitToVanilla.put("ItemPiston", "uu");
    	bukkitToVanilla.put("ItemPotion", "uv");
    	bukkitToVanilla.put("ItemRecord", "ux");
    	bukkitToVanilla.put("ItemRedstone", "uy");
    	bukkitToVanilla.put("ItemReed", "vm");
    	bukkitToVanilla.put("ItemSaddle", "uz");
    	bukkitToVanilla.put("ItemSeedFood", "vb");
    	bukkitToVanilla.put("ItemSeeds", "vc");
    	bukkitToVanilla.put("ItemShears", "vd");
    	bukkitToVanilla.put("ItemSign", "vf");
    	bukkitToVanilla.put("ItemSnowball", "vi");
    	bukkitToVanilla.put("ItemSkull", "vh");
    	bukkitToVanilla.put("ItemSoup", "ta");
    	bukkitToVanilla.put("ItemSpade", "ve");
    	bukkitToVanilla.put("ItemStep", "vk");
    	bukkitToVanilla.put("ItemSword", "vp");
    	bukkitToVanilla.put("ItemWaterLily", "vo");
    	bukkitToVanilla.put("ItemWorldMap", "uo");
    	bukkitToVanilla.put("ItemWrittenBook", "vr");
    	bukkitToVanilla.put("ItemWithAuxData", "tf");
    }

    
    public static void newItemAdded(Item item)
    {
        ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null)
        {
            mc = Loader.instance().getMinecraftModContainer();
            if (Loader.instance().hasReachedState(LoaderState.AVAILABLE))
            {
                FMLLog.severe("It appears something has tried to allocate an Item outside of the initialization phase of Minecraft, this could be very bad for your network connectivity.");
            }
        }
        String itemType = item.getClass().getName();
        ItemData itemData = new ItemData(item, mc);
        if (idMap.containsKey(item.id))
        {
            ItemData id = idMap.get(item.id);
            FMLLog.warning("[ItemTracker] The mod %s is attempting to overwrite existing item at %d (%s from %s) with %s", mc.getModId(), id.itemId, id.itemType, id.modId, itemType);
        }
        idMap.put(item.id, itemData);
        FMLLog.fine("[ItemTracker] Adding item %s(%d) owned by %s", item.getClass().getName(), item.id, mc.getModId());
    }

    public static void validateWorldSave(Set<ItemData> worldSaveItems)
    {
        isSaveValid = true;
        shouldContinue = true;
        // allow ourselves to continue if there's no saved data
        if (worldSaveItems == null)
        {
            serverValidationLatch.countDown();
            try
            {
                clientValidationLatch.await();
            }
            catch (InterruptedException e)
            {
            }
            return;
        }

        Function<? super ItemData, Integer> idMapFunction = new Function<ItemData, Integer>() {
            public Integer apply(ItemData input) {
                return input.itemId;
            };
        };

        Map<Integer,ItemData> worldMap = Maps.uniqueIndex(worldSaveItems,idMapFunction);
        difference = Maps.difference(worldMap, idMap);
        FMLLog.fine("The difference set is %s", difference);
        if (!difference.entriesDiffering().isEmpty() || !difference.entriesOnlyOnLeft().isEmpty())
        {
            FMLLog.severe("FML has detected item discrepancies");
            FMLLog.severe("Missing items : %s", difference.entriesOnlyOnLeft());
            FMLLog.severe("Mismatched items : %s", difference.entriesDiffering());
            isSaveValid = false;
            serverValidationLatch.countDown();
        }
        else
        {
            isSaveValid = true;
            serverValidationLatch.countDown();
        }
        try
        {
            clientValidationLatch.await();
            if (!shouldContinue)
            {
                throw new RuntimeException("This server instance is going to stop abnormally because of a fatal ID mismatch");
            }
        }
        catch (InterruptedException e)
        {
        }
    }

    public static void writeItemData(NBTTagList itemList)
    {
        for (ItemData dat : idMap.values())
        {
	        // MCPC - some classes are used more then once and need to be handled
	        if (dat.itemType.equals("net.minecraft.server.ItemWithAuxData"))
	        {
	        	switch(dat.itemId)
	        	{
	        		case 52: dat.itemType = "net.minecraft.server.BlockMobSpawner";
	        				 dat.ordinal = 0;
	        				 break;
	        		case 99: dat.itemType = "net.minecraft.server.BlockHugeMushroom";
	        				 dat.ordinal = 0;
	        				 break;
	        		case 100: dat.itemType = "net.minecraft.server.BlockHugeMushroom";
	        				  dat.ordinal = 1;
	        				  break;
	        		case 31:
	        		case 106: // ItemWithAuxData
	        		default:
	        	}
	        }

	        // MCPC - strip out NMS so we can search id map
	        String[] replace = dat.itemType.split("net\\.minecraft\\.server\\.");
	        
	        // MCPC - if not a custom class, proceed
	        if (replace.length > 1) // vanilla
	        {
	        	// MCPC - for any NMS match, convert to obfuscated vanilla mapping
		        for(Map.Entry<String, String> entry : bukkitToVanilla.entrySet()){
		        	if (entry.getKey().equals(replace[1]))
		        	{
		        		dat.itemType = replace[0] + entry.getValue();
		        		break;
		        	}
		        }
	        }
            itemList.add(dat.toNBT());
        }
        
    }

    /**
     * Initialize the server gate
     * @param gateCount the countdown amount. If it's 2 we're on the client and the client and server
     * will wait at the latch. 1 is a server and the server will proceed
     */
    public static void initializeServerGate(int gateCount)
    {
        serverValidationLatch = new CountDownLatch(gateCount - 1);
        clientValidationLatch = new CountDownLatch(gateCount - 1);
    }

    public static MapDifference<Integer, ItemData> gateWorldLoadingForValidation()
    {
        try
        {
            serverValidationLatch.await();
            if (!isSaveValid)
            {
                return difference;
            }
        }
        catch (InterruptedException e)
        {
        }
        difference = null;
        return null;
    }


    public static void releaseGate(boolean carryOn)
    {
        shouldContinue = carryOn;
        clientValidationLatch.countDown();
    }

    public static Set<ItemData> buildWorldItemData(NBTTagList modList)
    {
        Set<ItemData> worldSaveItems = Sets.newHashSet();
        for (int i = 0; i < modList.size(); i++)
        {
            NBTTagCompound mod = (NBTTagCompound) modList.get(i);
            ItemData dat = new ItemData(mod);
            worldSaveItems.add(dat);
        }
        return worldSaveItems;
    }



}
