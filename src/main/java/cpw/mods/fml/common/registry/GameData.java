package cpw.mods.fml.common.registry;

import java.util.HashMap; // MCPC
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;

import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.ItemWithAuxData;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

import mcpc.com.google.common.base.Function;
import mcpc.com.google.common.base.Throwables;
import mcpc.com.google.common.collect.ImmutableMap;
import mcpc.com.google.common.collect.MapDifference;
import mcpc.com.google.common.collect.MapDifference.ValueDifference;
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
    private static Map<String,String> ignoredMods;

    // MCPC - bukkit to vanilla obfuscated mappings
    // used with ModIdMapPacket
    private static final Map<String, String> bukkitToVanilla = new HashMap<String, String>();
    static {
        // 1.4.6 mappings
        // blocks
        bukkitToVanilla.put("BlockAnvil", "aiz");
        bukkitToVanilla.put("BlockStone", "aml");
        bukkitToVanilla.put("BlockGrass", "akj");
        bukkitToVanilla.put("BlockDirt", "ajv");
        bukkitToVanilla.put("Block", "amq");
        bukkitToVanilla.put("BlockFlowing", "aky");
        bukkitToVanilla.put("BlockStationary", "akz");
        bukkitToVanilla.put("BlockSand", "akn");
        bukkitToVanilla.put("BlockGravel", "akk");
        bukkitToVanilla.put("BlockOre", "all");
        bukkitToVanilla.put("BlockSponge", "amf");
        bukkitToVanilla.put("BlockGlass", "aki");
        bukkitToVanilla.put("BlockDispenser", "ajw");
        bukkitToVanilla.put("BlockNote", "alf");
        bukkitToVanilla.put("BlockMinecartTrack", "alr");
        bukkitToVanilla.put("BlockBed", "ajb");
        bukkitToVanilla.put("BlockMinecartDetector", "ajs");
        bukkitToVanilla.put("BlockWeb", "ang");
        bukkitToVanilla.put("BlockPistonExtension", "aob");
        bukkitToVanilla.put("BlockDeadBush", "ajr");
        bukkitToVanilla.put("BlockFlower", "aje");
        bukkitToVanilla.put("BlockMushroom", "ale");
        bukkitToVanilla.put("BlockPistonMoving", "aoc");
        bukkitToVanilla.put("BlockOreBlock", "alc");
        bukkitToVanilla.put("BlockTNT", "amv");
        bukkitToVanilla.put("BlockBookshelf", "ajc");
        bukkitToVanilla.put("BlockFire", "akf");
        bukkitToVanilla.put("BlockTorch", "amx");
        bukkitToVanilla.put("BlockObsidian", "alk");
        bukkitToVanilla.put("BlockRedstoneWire", "alv");
        bukkitToVanilla.put("BlockChest", "ajk");
        bukkitToVanilla.put("BlockStairs", "amh");
        bukkitToVanilla.put("BlockMobSpawner", "ald");
        bukkitToVanilla.put("BlockCrops", "ajq");
        bukkitToVanilla.put("BlockWorkbench", "anj");
        bukkitToVanilla.put("BlockSign", "amb");
        bukkitToVanilla.put("BlockFurnace", "akh");
        bukkitToVanilla.put("BlockSoil", "akc");
        bukkitToVanilla.put("BlockLever", "akv");
        bukkitToVanilla.put("BlockPressurePlate", "alo");
        bukkitToVanilla.put("BlockDoor", "ajx");
        bukkitToVanilla.put("BlockLadder", "aks");
        bukkitToVanilla.put("BlockRedstoneTorch", "ali");
        bukkitToVanilla.put("BlockButton", "ajf");
        bukkitToVanilla.put("BlockSnow", "amw");
        bukkitToVanilla.put("BlockIce", "akr");
        bukkitToVanilla.put("BlockRedstoneOre", "alw");
        bukkitToVanilla.put("BlockFence", "ake");
        bukkitToVanilla.put("BlockJukeBox", "alt");
        bukkitToVanilla.put("BlockBloodStone", "akp");
        bukkitToVanilla.put("BlockPumpkin", "alq");
        bukkitToVanilla.put("BlockCactus", "ajg");
        bukkitToVanilla.put("BlockSnowBlock", "ame");
        bukkitToVanilla.put("BlockReed", "aly");
        bukkitToVanilla.put("BlockClay", "ajl");
        bukkitToVanilla.put("BlockDiode", "ajt");
        bukkitToVanilla.put("BlockCake", "ajh");
        bukkitToVanilla.put("BlockLockedChest", "ala");
        bukkitToVanilla.put("BlockLightStone", "akw");
        bukkitToVanilla.put("BlockSlowSand", "ako");
        bukkitToVanilla.put("BlockThinFence", "amp");
        bukkitToVanilla.put("BlockMelon", "alb");
        bukkitToVanilla.put("BlockHugeMushroom", "akq");
        bukkitToVanilla.put("BlockTrapdoor", "amz");
        bukkitToVanilla.put("BlockMycel", "alg");
        bukkitToVanilla.put("BlockFenceGate", "akd");
        bukkitToVanilla.put("BlockStem", "ami");
        bukkitToVanilla.put("BlockEnderPortal", "amn");
        bukkitToVanilla.put("BlockCauldron", "ajj");
        bukkitToVanilla.put("BlockBrewingStand", "ajd");
        bukkitToVanilla.put("BlockEnchantmentTable", "ajz");
        bukkitToVanilla.put("BlockNetherWart", "alh");
        bukkitToVanilla.put("BlockCocoa", "ajn");
        bukkitToVanilla.put("BlockRedstoneLamp", "alx");
        bukkitToVanilla.put("BlockDragonEgg", "ajy");
        bukkitToVanilla.put("BlockEnderPortalFrame", "amo");
        bukkitToVanilla.put("BlockCommand", "ajo");
        bukkitToVanilla.put("BlockBeacon", "aja");
        bukkitToVanilla.put("BlockCarrots", "aji");
        bukkitToVanilla.put("BlockFlowerPot", "akg");
        bukkitToVanilla.put("BlockPotatoes", "aln");
        bukkitToVanilla.put("BlockTripwireHook", "anb");
        bukkitToVanilla.put("BlockEnderChest", "aka");
        bukkitToVanilla.put("BlockTripwire", "");
        bukkitToVanilla.put("BlockSkull", "amc");
        bukkitToVanilla.put("BlockCloth", "ajm");
        bukkitToVanilla.put("BlockDirectional", "aju");
        bukkitToVanilla.put("BlockContainer", "akb");
        bukkitToVanilla.put("BlockStepAbstract", "akl");
        bukkitToVanilla.put("BlockHalfTransparant", "akm");
        bukkitToVanilla.put("BlockLeaves", "akt");
        bukkitToVanilla.put("BlockFluids", "akx");
        bukkitToVanilla.put("BlockPortal", "alm");
        bukkitToVanilla.put("BlockSapling", "ama");
        bukkitToVanilla.put("BlockSmoothBrick", "amd");
        bukkitToVanilla.put("BlockMonsterEggs", "amj");
        bukkitToVanilla.put("BlockLongGrass", "amm");
        bukkitToVanilla.put("BlockLog", "ana");
        bukkitToVanilla.put("BlockVine", "and");
        bukkitToVanilla.put("BlockCobbleWall", "ane");
        bukkitToVanilla.put("BlockWoodStep", "anh");
        bukkitToVanilla.put("BlockTripwire", "anc");
        
        // items
        bukkitToVanilla.put("Item", "up");
        bukkitToVanilla.put("ItemBook", "sz");
        bukkitToVanilla.put("ItemAnvil", "st");
        bukkitToVanilla.put("ItemArmor", "su");
        bukkitToVanilla.put("ItemAxe", "un");
        bukkitToVanilla.put("ItemBed", "sx");
        bukkitToVanilla.put("ItemBlock", "vq");
        bukkitToVanilla.put("ItemBoat", "sy");
        bukkitToVanilla.put("ItemBookAndQuill", "vv");
        bukkitToVanilla.put("ItemBow", "tb");
        bukkitToVanilla.put("ItemBucket", "td");
        bukkitToVanilla.put("ItemCarrotStick", "te");
        bukkitToVanilla.put("ItemCoal", "tg");
        bukkitToVanilla.put("ItemCloth", "tf");
        bukkitToVanilla.put("ItemDoor", "tx");
        bukkitToVanilla.put("ItemDye", "ty");
        bukkitToVanilla.put("ItemEgg", "tz");
        bukkitToVanilla.put("ItemEnchantedBook", "ub");
        bukkitToVanilla.put("ItemEnderEye", "uc");
        bukkitToVanilla.put("ItemEnderPearl", "ud");
        bukkitToVanilla.put("ItemExpBottle", "ue");
        bukkitToVanilla.put("ItemFireball", "uf");
        bukkitToVanilla.put("ItemFishingRod", "ui");
        bukkitToVanilla.put("ItemFireworks", "uh");
        bukkitToVanilla.put("ItemFireworksCharge", "ug");
        bukkitToVanilla.put("ItemFlintAndSteel", "uj");
        bukkitToVanilla.put("ItemFood", "uk");
        bukkitToVanilla.put("ItemGlassBottle", "ta");
        bukkitToVanilla.put("ItemGoldenApple", "ul");
        bukkitToVanilla.put("ItemHanging", "um");
        bukkitToVanilla.put("ItemHoe", "uo");
        bukkitToVanilla.put("ItemLeaves", "us");
        bukkitToVanilla.put("ItemMapEmpty", "ua");
        bukkitToVanilla.put("ItemMilkBucket", "uu");
        bukkitToVanilla.put("ItemMinecart", "uv");
        bukkitToVanilla.put("ItemMonsterEgg", "uw");
        bukkitToVanilla.put("ItemMultiTexture", "ux");
        bukkitToVanilla.put("ItemNetherStar", "vl");
        bukkitToVanilla.put("ItemPickaxe", "uy");
        bukkitToVanilla.put("ItemPiston", "uz");
        bukkitToVanilla.put("ItemPotion", "va");
        bukkitToVanilla.put("ItemRecord", "vc");
        bukkitToVanilla.put("ItemRedstone", "vd");
        bukkitToVanilla.put("ItemReed", "vr");
        bukkitToVanilla.put("ItemSaddle", "ve");
        bukkitToVanilla.put("ItemSeedFood", "vg");
        bukkitToVanilla.put("ItemSeeds", "vh");
        bukkitToVanilla.put("ItemShears", "vi");
        bukkitToVanilla.put("ItemSign", "vk");
        bukkitToVanilla.put("ItemSnowball", "vn");
        bukkitToVanilla.put("ItemSkull", "vm");
        bukkitToVanilla.put("ItemSoup", "tc");
        bukkitToVanilla.put("ItemSpade", "vj");
        bukkitToVanilla.put("ItemStep", "vp");
        bukkitToVanilla.put("ItemSword", "vu");
        bukkitToVanilla.put("ItemWaterLily", "vt");
        bukkitToVanilla.put("ItemWorldMap", "ut");
        bukkitToVanilla.put("ItemWrittenBook", "vw");
        bukkitToVanilla.put("ItemWithAuxData", "th");
    }
    // MCPC end

    private static boolean isModIgnoredForIdValidation(String modId)
    {
        if (ignoredMods == null)
        {
            File f = new File(Loader.instance().getConfigDir(),"fmlIDChecking.properties");
            if (f.exists())
            {
                Properties p = new Properties();
                try
                {
                    p.load(new FileInputStream(f));
                    ignoredMods = Maps.fromProperties(p);
                    if (ignoredMods.size()>0)
                    {
                        FMLLog.warning("Using non-empty ignored mods configuration file %s", ignoredMods.keySet());
                    }
                }
                catch (Exception e)
                {
                    Throwables.propagateIfPossible(e);
                    FMLLog.log(Level.SEVERE, e, "Failed to read ignored ID checker mods properties file");
                    ignoredMods = ImmutableMap.<String, String>of();
                }
            }
            else
            {
                ignoredMods = ImmutableMap.<String, String>of();
            }
        }
        return ignoredMods.containsKey(modId);
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
            boolean foundNonIgnored = false;
            for (ItemData diff : difference.entriesOnlyOnLeft().values())
            {
                if (!isModIgnoredForIdValidation(diff.getModId()))
                {
                    foundNonIgnored = true;
                }
            }
            for (ValueDifference<ItemData> diff : difference.entriesDiffering().values())
            {
                if (! ( isModIgnoredForIdValidation(diff.leftValue().getModId()) || isModIgnoredForIdValidation(diff.rightValue().getModId()) ) )
                {
                    foundNonIgnored = true;
                }
            }
            if (!foundNonIgnored)
            {
                FMLLog.severe("FML is ignoring these ID discrepancies because of configuration. YOUR GAME WILL NOW PROBABLY CRASH. HOPEFULLY YOU WON'T HAVE CORRUPTED YOUR WORLD. BLAME %s", ignoredMods.keySet());
            }
            isSaveValid = !foundNonIgnored;
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
            if (dat.itemType.equalsIgnoreCase("net.minecraft.server.ItemWithAuxData"))
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
            String[] replace = dat.itemType.split("net\\.minecraft\\.server\\.v1_4_6.");
            
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

    static void setName(Item item, String name, String modId)
    {
        int id = item.id;
        ItemData itemData = idMap.get(id);
        itemData.setName(name,modId);
    }
}
