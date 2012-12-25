package net.minecraft.server;

import java.util.Random;

// Forge start
import net.minecraftforge.common.ChestGenHooks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.registry.GameData;
// Forge end

public class Item
{
    private CreativeModeTab a = null;

    /** The RNG used by the Item subclasses. */
    protected static Random d = new Random();

    /** A 32000 elements Item array. */
    public static Item[] byId = new Item[32000];
    public static Item IRON_SPADE = (new ItemSpade(0, EnumToolMaterial.IRON)).b(2, 5).b("shovelIron");
    public static Item IRON_PICKAXE = (new ItemPickaxe(1, EnumToolMaterial.IRON)).b(2, 6).b("pickaxeIron");
    public static Item IRON_AXE = (new ItemAxe(2, EnumToolMaterial.IRON)).b(2, 7).b("hatchetIron");
    public static Item FLINT_AND_STEEL = (new ItemFlintAndSteel(3)).b(5, 0).b("flintAndSteel");
    public static Item APPLE = (new ItemFood(4, 4, 0.3F, false)).b(10, 0).b("apple");
    public static Item BOW = (new ItemBow(5)).b(5, 1).b("bow");
    public static Item ARROW = (new Item(6)).b(5, 2).b("arrow").a(CreativeModeTab.j);
    public static Item COAL = (new ItemCoal(7)).b(7, 0).b("coal");
    public static Item DIAMOND = (new Item(8)).b(7, 3).b("diamond").a(CreativeModeTab.l);
    public static Item IRON_INGOT = (new Item(9)).b(7, 1).b("ingotIron").a(CreativeModeTab.l);
    public static Item GOLD_INGOT = (new Item(10)).b(7, 2).b("ingotGold").a(CreativeModeTab.l);
    public static Item IRON_SWORD = (new ItemSword(11, EnumToolMaterial.IRON)).b(2, 4).b("swordIron");
    public static Item WOOD_SWORD = (new ItemSword(12, EnumToolMaterial.WOOD)).b(0, 4).b("swordWood");
    public static Item WOOD_SPADE = (new ItemSpade(13, EnumToolMaterial.WOOD)).b(0, 5).b("shovelWood");
    public static Item WOOD_PICKAXE = (new ItemPickaxe(14, EnumToolMaterial.WOOD)).b(0, 6).b("pickaxeWood");
    public static Item WOOD_AXE = (new ItemAxe(15, EnumToolMaterial.WOOD)).b(0, 7).b("hatchetWood");
    public static Item STONE_SWORD = (new ItemSword(16, EnumToolMaterial.STONE)).b(1, 4).b("swordStone");
    public static Item STONE_SPADE = (new ItemSpade(17, EnumToolMaterial.STONE)).b(1, 5).b("shovelStone");
    public static Item STONE_PICKAXE = (new ItemPickaxe(18, EnumToolMaterial.STONE)).b(1, 6).b("pickaxeStone");
    public static Item STONE_AXE = (new ItemAxe(19, EnumToolMaterial.STONE)).b(1, 7).b("hatchetStone");
    public static Item DIAMOND_SWORD = (new ItemSword(20, EnumToolMaterial.DIAMOND)).b(3, 4).b("swordDiamond");
    public static Item DIAMOND_SPADE = (new ItemSpade(21, EnumToolMaterial.DIAMOND)).b(3, 5).b("shovelDiamond");
    public static Item DIAMOND_PICKAXE = (new ItemPickaxe(22, EnumToolMaterial.DIAMOND)).b(3, 6).b("pickaxeDiamond");
    public static Item DIAMOND_AXE = (new ItemAxe(23, EnumToolMaterial.DIAMOND)).b(3, 7).b("hatchetDiamond");
    public static Item STICK = (new Item(24)).b(5, 3).o().b("stick").a(CreativeModeTab.l);
    public static Item BOWL = (new Item(25)).b(7, 4).b("bowl").a(CreativeModeTab.l);
    public static Item MUSHROOM_SOUP = (new ItemSoup(26, 6)).b(8, 4).b("mushroomStew");
    public static Item GOLD_SWORD = (new ItemSword(27, EnumToolMaterial.GOLD)).b(4, 4).b("swordGold");
    public static Item GOLD_SPADE = (new ItemSpade(28, EnumToolMaterial.GOLD)).b(4, 5).b("shovelGold");
    public static Item GOLD_PICKAXE = (new ItemPickaxe(29, EnumToolMaterial.GOLD)).b(4, 6).b("pickaxeGold");
    public static Item GOLD_AXE = (new ItemAxe(30, EnumToolMaterial.GOLD)).b(4, 7).b("hatchetGold");
    public static Item STRING = (new ItemReed(31, Block.TRIPWIRE)).b(8, 0).b("string").a(CreativeModeTab.l);
    public static Item FEATHER = (new Item(32)).b(8, 1).b("feather").a(CreativeModeTab.l);
    public static Item SULPHUR = (new Item(33)).b(8, 2).b("sulphur").c(PotionBrewer.k).a(CreativeModeTab.l);
    public static Item WOOD_HOE = (new ItemHoe(34, EnumToolMaterial.WOOD)).b(0, 8).b("hoeWood");
    public static Item STONE_HOE = (new ItemHoe(35, EnumToolMaterial.STONE)).b(1, 8).b("hoeStone");
    public static Item IRON_HOE = (new ItemHoe(36, EnumToolMaterial.IRON)).b(2, 8).b("hoeIron");
    public static Item DIAMOND_HOE = (new ItemHoe(37, EnumToolMaterial.DIAMOND)).b(3, 8).b("hoeDiamond");
    public static Item GOLD_HOE = (new ItemHoe(38, EnumToolMaterial.GOLD)).b(4, 8).b("hoeGold");
    public static Item SEEDS = (new ItemSeeds(39, Block.CROPS.id, Block.SOIL.id)).b(9, 0).b("seeds");
    public static Item WHEAT = (new Item(40)).b(9, 1).b("wheat").a(CreativeModeTab.l);
    public static Item BREAD = (new ItemFood(41, 5, 0.6F, false)).b(9, 2).b("bread");
    public static Item LEATHER_HELMET = (new ItemArmor(42, EnumArmorMaterial.CLOTH, 0, 0)).b(0, 0).b("helmetCloth");
    public static Item LEATHER_CHESTPLATE = (new ItemArmor(43, EnumArmorMaterial.CLOTH, 0, 1)).b(0, 1).b("chestplateCloth");
    public static Item LEATHER_LEGGINGS = (new ItemArmor(44, EnumArmorMaterial.CLOTH, 0, 2)).b(0, 2).b("leggingsCloth");
    public static Item LEATHER_BOOTS = (new ItemArmor(45, EnumArmorMaterial.CLOTH, 0, 3)).b(0, 3).b("bootsCloth");
    public static Item CHAINMAIL_HELMET = (new ItemArmor(46, EnumArmorMaterial.CHAIN, 1, 0)).b(1, 0).b("helmetChain");
    public static Item CHAINMAIL_CHESTPLATE = (new ItemArmor(47, EnumArmorMaterial.CHAIN, 1, 1)).b(1, 1).b("chestplateChain");
    public static Item CHAINMAIL_LEGGINGS = (new ItemArmor(48, EnumArmorMaterial.CHAIN, 1, 2)).b(1, 2).b("leggingsChain");
    public static Item CHAINMAIL_BOOTS = (new ItemArmor(49, EnumArmorMaterial.CHAIN, 1, 3)).b(1, 3).b("bootsChain");
    public static Item IRON_HELMET = (new ItemArmor(50, EnumArmorMaterial.IRON, 2, 0)).b(2, 0).b("helmetIron");
    public static Item IRON_CHESTPLATE = (new ItemArmor(51, EnumArmorMaterial.IRON, 2, 1)).b(2, 1).b("chestplateIron");
    public static Item IRON_LEGGINGS = (new ItemArmor(52, EnumArmorMaterial.IRON, 2, 2)).b(2, 2).b("leggingsIron");
    public static Item IRON_BOOTS = (new ItemArmor(53, EnumArmorMaterial.IRON, 2, 3)).b(2, 3).b("bootsIron");
    public static Item DIAMOND_HELMET = (new ItemArmor(54, EnumArmorMaterial.DIAMOND, 3, 0)).b(3, 0).b("helmetDiamond");
    public static Item DIAMOND_CHESTPLATE = (new ItemArmor(55, EnumArmorMaterial.DIAMOND, 3, 1)).b(3, 1).b("chestplateDiamond");
    public static Item DIAMOND_LEGGINGS = (new ItemArmor(56, EnumArmorMaterial.DIAMOND, 3, 2)).b(3, 2).b("leggingsDiamond");
    public static Item DIAMOND_BOOTS = (new ItemArmor(57, EnumArmorMaterial.DIAMOND, 3, 3)).b(3, 3).b("bootsDiamond");
    public static Item GOLD_HELMET = (new ItemArmor(58, EnumArmorMaterial.GOLD, 4, 0)).b(4, 0).b("helmetGold");
    public static Item GOLD_CHESTPLATE = (new ItemArmor(59, EnumArmorMaterial.GOLD, 4, 1)).b(4, 1).b("chestplateGold");
    public static Item GOLD_LEGGINGS = (new ItemArmor(60, EnumArmorMaterial.GOLD, 4, 2)).b(4, 2).b("leggingsGold");
    public static Item GOLD_BOOTS = (new ItemArmor(61, EnumArmorMaterial.GOLD, 4, 3)).b(4, 3).b("bootsGold");
    public static Item FLINT = (new Item(62)).b(6, 0).b("flint").a(CreativeModeTab.l);
    public static Item PORK = (new ItemFood(63, 3, 0.3F, true)).b(7, 5).b("porkchopRaw");
    public static Item GRILLED_PORK = (new ItemFood(64, 8, 0.8F, true)).b(8, 5).b("porkchopCooked");
    public static Item PAINTING = (new ItemHanging(65, EntityPainting.class)).b(10, 1).b("painting");
    public static Item GOLDEN_APPLE = (new ItemGoldenApple(66, 4, 1.2F, false)).j().a(MobEffectList.REGENERATION.id, 5, 0, 1.0F).b(11, 0).b("appleGold");
    public static Item SIGN = (new ItemSign(67)).b(10, 2).b("sign");
    public static Item WOOD_DOOR = (new ItemDoor(68, Material.WOOD)).b(11, 2).b("doorWood");
    public static Item BUCKET = (new ItemBucket(69, 0)).b(10, 4).b("bucket").d(16);
    public static Item WATER_BUCKET = (new ItemBucket(70, Block.WATER.id)).b(11, 4).b("bucketWater").a(BUCKET);
    public static Item LAVA_BUCKET = (new ItemBucket(71, Block.LAVA.id)).b(12, 4).b("bucketLava").a(BUCKET);
    public static Item MINECART = (new ItemMinecart(72, 0)).b(7, 8).b("minecart");
    public static Item SADDLE = (new ItemSaddle(73)).b(8, 6).b("saddle");
    public static Item IRON_DOOR = (new ItemDoor(74, Material.ORE)).b(12, 2).b("doorIron");
    public static Item REDSTONE = (new ItemRedstone(75)).b(8, 3).b("redstone").c(PotionBrewer.i);
    public static Item SNOW_BALL = (new ItemSnowball(76)).b(14, 0).b("snowball");
    public static Item BOAT = (new ItemBoat(77)).b(8, 8).b("boat");
    public static Item LEATHER = (new Item(78)).b(7, 6).b("leather").a(CreativeModeTab.l);
    public static Item MILK_BUCKET = (new ItemMilkBucket(79)).b(13, 4).b("milk").a(BUCKET);
    public static Item CLAY_BRICK = (new Item(80)).b(6, 1).b("brick").a(CreativeModeTab.l);
    public static Item CLAY_BALL = (new Item(81)).b(9, 3).b("clay").a(CreativeModeTab.l);
    public static Item SUGAR_CANE = (new ItemReed(82, Block.SUGAR_CANE_BLOCK)).b(11, 1).b("reeds").a(CreativeModeTab.l);
    public static Item PAPER = (new Item(83)).b(10, 3).b("paper").a(CreativeModeTab.f);
    public static Item BOOK = (new ItemBook(84)).b(11, 3).b("book").a(CreativeModeTab.f);
    public static Item SLIME_BALL = (new Item(85)).b(14, 1).b("slimeball").a(CreativeModeTab.f);
    public static Item STORAGE_MINECART = (new ItemMinecart(86, 1)).b(7, 9).b("minecartChest");
    public static Item POWERED_MINECART = (new ItemMinecart(87, 2)).b(7, 10).b("minecartFurnace");
    public static Item EGG = (new ItemEgg(88)).b(12, 0).b("egg");
    public static Item COMPASS = (new Item(89)).b(6, 3).b("compass").a(CreativeModeTab.i);
    public static Item FISHING_ROD = (new ItemFishingRod(90)).b(5, 4).b("fishingRod");
    public static Item WATCH = (new Item(91)).b(6, 4).b("clock").a(CreativeModeTab.i);
    public static Item GLOWSTONE_DUST = (new Item(92)).b(9, 4).b("yellowDust").c(PotionBrewer.j).a(CreativeModeTab.l);
    public static Item RAW_FISH = (new ItemFood(93, 2, 0.3F, false)).b(9, 5).b("fishRaw");
    public static Item COOKED_FISH = (new ItemFood(94, 5, 0.6F, false)).b(10, 5).b("fishCooked");
    public static Item INK_SACK = (new ItemDye(95)).b(14, 4).b("dyePowder");
    public static Item BONE = (new Item(96)).b(12, 1).b("bone").o().a(CreativeModeTab.f);
    public static Item SUGAR = (new Item(97)).b(13, 0).b("sugar").c(PotionBrewer.b).a(CreativeModeTab.l);
    public static Item CAKE = (new ItemReed(98, Block.CAKE_BLOCK)).d(1).b(13, 1).b("cake").a(CreativeModeTab.h);
    public static Item BED = (new ItemBed(99)).d(1).b(13, 2).b("bed");
    public static Item DIODE = (new ItemReed(100, Block.DIODE_OFF)).b(6, 5).b("diode").a(CreativeModeTab.d);
    public static Item COOKIE = (new ItemFood(101, 2, 0.1F, false)).b(12, 5).b("cookie");
    public static ItemWorldMap MAP = (ItemWorldMap)(new ItemWorldMap(102)).b(12, 3).b("map");

    /**
     * Item introduced on 1.7 version, is a shear to cut leaves (you can keep the block) or get wool from sheeps.
     */
    public static ItemShears SHEARS = (ItemShears)(new ItemShears(103)).b(13, 5).b("shears");
    public static Item MELON = (new ItemFood(104, 2, 0.3F, false)).b(13, 6).b("melon");
    public static Item PUMPKIN_SEEDS = (new ItemSeeds(105, Block.PUMPKIN_STEM.id, Block.SOIL.id)).b(13, 3).b("seeds_pumpkin");
    public static Item MELON_SEEDS = (new ItemSeeds(106, Block.MELON_STEM.id, Block.SOIL.id)).b(14, 3).b("seeds_melon");
    public static Item RAW_BEEF = (new ItemFood(107, 3, 0.3F, true)).b(9, 6).b("beefRaw");
    public static Item COOKED_BEEF = (new ItemFood(108, 8, 0.8F, true)).b(10, 6).b("beefCooked");
    public static Item RAW_CHICKEN = (new ItemFood(109, 2, 0.3F, true)).a(MobEffectList.HUNGER.id, 30, 0, 0.3F).b(9, 7).b("chickenRaw");
    public static Item COOKED_CHICKEN = (new ItemFood(110, 6, 0.6F, true)).b(10, 7).b("chickenCooked");
    public static Item ROTTEN_FLESH = (new ItemFood(111, 4, 0.1F, true)).a(MobEffectList.HUNGER.id, 30, 0, 0.8F).b(11, 5).b("rottenFlesh");
    public static Item ENDER_PEARL = (new ItemEnderPearl(112)).b(11, 6).b("enderPearl");
    public static Item BLAZE_ROD = (new Item(113)).b(12, 6).b("blazeRod").a(CreativeModeTab.l);
    public static Item GHAST_TEAR = (new Item(114)).b(11, 7).b("ghastTear").c(PotionBrewer.c).a(CreativeModeTab.k);
    public static Item GOLD_NUGGET = (new Item(115)).b(12, 7).b("goldNugget").a(CreativeModeTab.l);
    public static Item NETHER_STALK = (new ItemSeeds(116, Block.NETHER_WART.id, Block.SOUL_SAND.id)).b(13, 7).b("netherStalkSeeds").c("+4");
    public static ItemPotion POTION = (ItemPotion)(new ItemPotion(117)).b(13, 8).b("potion");
    public static Item GLASS_BOTTLE = (new ItemGlassBottle(118)).b(12, 8).b("glassBottle");
    public static Item SPIDER_EYE = (new ItemFood(119, 2, 0.8F, false)).a(MobEffectList.POISON.id, 5, 0, 1.0F).b(11, 8).b("spiderEye").c(PotionBrewer.d);
    public static Item FERMENTED_SPIDER_EYE = (new Item(120)).b(10, 8).b("fermentedSpiderEye").c(PotionBrewer.e).a(CreativeModeTab.k);
    public static Item BLAZE_POWDER = (new Item(121)).b(13, 9).b("blazePowder").c(PotionBrewer.g).a(CreativeModeTab.k);
    public static Item MAGMA_CREAM = (new Item(122)).b(13, 10).b("magmaCream").c(PotionBrewer.h).a(CreativeModeTab.k);
    public static Item BREWING_STAND = (new ItemReed(123, Block.BREWING_STAND)).b(12, 10).b("brewingStand").a(CreativeModeTab.k);
    public static Item CAULDRON = (new ItemReed(124, Block.CAULDRON)).b(12, 9).b("cauldron").a(CreativeModeTab.k);
    public static Item EYE_OF_ENDER = (new ItemEnderEye(125)).b(11, 9).b("eyeOfEnder");
    public static Item SPECKLED_MELON = (new Item(126)).b(9, 8).b("speckledMelon").c(PotionBrewer.f).a(CreativeModeTab.k);
    public static Item MONSTER_EGG = (new ItemMonsterEgg(127)).b(9, 9).b("monsterPlacer");

    /**
     * Bottle o' Enchanting. Drops between 1 and 3 experience orbs when thrown.
     */
    public static Item EXP_BOTTLE = (new ItemExpBottle(128)).b(11, 10).b("expBottle");

    /**
     * Fire Charge. When used in a dispenser it fires a fireball similiar to a Ghast's.
     */
    public static Item FIREBALL = (new ItemFireball(129)).b(14, 2).b("fireball");
    public static Item BOOK_AND_QUILL = (new ItemBookAndQuill(130)).b(11, 11).b("writingBook").a(CreativeModeTab.f);
    public static Item WRITTEN_BOOK = (new ItemWrittenBook(131)).b(12, 11).b("writtenBook");
    public static Item EMERALD = (new Item(132)).b(10, 11).b("emerald").a(CreativeModeTab.l);
    public static Item ITEM_FRAME = (new ItemHanging(133, EntityItemFrame.class)).b(14, 12).b("frame");
    public static Item FLOWER_POT = (new ItemReed(134, Block.FLOWER_POT)).b(13, 11).b("flowerPot").a(CreativeModeTab.c);
    public static Item CARROT = (new ItemSeedFood(135, 4, 0.6F, Block.CARROTS.id, Block.SOIL.id)).b(8, 7).b("carrots");
    public static Item POTATO = (new ItemSeedFood(136, 1, 0.3F, Block.POTATOES.id, Block.SOIL.id)).b(7, 7).b("potato");
    public static Item POTATO_BAKED = (new ItemFood(137, 6, 0.6F, false)).b(6, 7).b("potatoBaked");
    public static Item POTATO_POISON = (new ItemFood(138, 2, 0.3F, false)).a(MobEffectList.POISON.id, 5, 0, 0.6F).b(6, 8).b("potatoPoisonous");
    public static ItemMapEmpty MAP_EMPTY = (ItemMapEmpty)(new ItemMapEmpty(139)).b(13, 12).b("emptyMap");
    public static Item CARROT_GOLDEN = (new ItemFood(140, 6, 1.2F, false)).b(6, 9).b("carrotGolden").c(PotionBrewer.l);
    public static Item SKULL = (new ItemSkull(141)).b("skull");
    public static Item CARROT_STICK = (new ItemCarrotStick(142)).b(6, 6).b("carrotOnAStick");
    public static Item NETHER_STAR = (new ItemNetherStar(143)).b(9, 11).b("netherStar").a(CreativeModeTab.l);
    public static Item PUMPKIN_PIE = (new ItemFood(144, 8, 0.3F, false)).b(8, 9).b("pumpkinPie").a(CreativeModeTab.h);
    public static Item FIREWORKS = (new ItemFireworks(145)).b(9, 12).b("fireworks");
    public static Item FIREWORKS_CHARGE = (new ItemFireworksCharge(146)).b(10, 12).b("fireworksCharge").a(CreativeModeTab.f);
    public static ItemEnchantedBook ENCHANTED_BOOK = (ItemEnchantedBook)(new ItemEnchantedBook(147)).b(15, 12).d(1).b("enchantedBook");
    public static Item RECORD_1 = (new ItemRecord(2000, "13")).b(0, 15).b("record");
    public static Item RECORD_2 = (new ItemRecord(2001, "cat")).b(1, 15).b("record");
    public static Item RECORD_3 = (new ItemRecord(2002, "blocks")).b(2, 15).b("record");
    public static Item RECORD_4 = (new ItemRecord(2003, "chirp")).b(3, 15).b("record");
    public static Item RECORD_5 = (new ItemRecord(2004, "far")).b(4, 15).b("record");
    public static Item RECORD_6 = (new ItemRecord(2005, "mall")).b(5, 15).b("record");
    public static Item RECORD_7 = (new ItemRecord(2006, "mellohi")).b(6, 15).b("record");
    public static Item RECORD_8 = (new ItemRecord(2007, "stal")).b(7, 15).b("record");
    public static Item RECORD_9 = (new ItemRecord(2008, "strad")).b(8, 15).b("record");
    public static Item RECORD_10 = (new ItemRecord(2009, "ward")).b(9, 15).b("record");
    public static Item RECORD_11 = (new ItemRecord(2010, "11")).b(10, 15).b("record");
    public static Item RECORD_12 = (new ItemRecord(2011, "wait")).b(11, 15).b("record");

    /** The ID of this item. */
    public final int id;

    /** Maximum size of the stack. */
    protected int maxStackSize = 64;

    /** Maximum damage an item can handle. */
    private int durability = 0;

    /** Icon index in the icons table. */
    protected int textureId;

    /** If true, render the object in full 3D, like weapons and tools. */
    protected boolean cm = false;

    /**
     * Some items (like dyes) have multiple subtypes on same item, this is field define this behavior
     */
    protected boolean cn = false;
    private Item craftingResult = null;

    /**
     * The string representing this item's effect on a potion when used as an ingredient.
     */
    private String co = null;

    /** full name of item from language file */
    private String name;

   /** FORGE: To disable repair recipes. */
   protected boolean canRepair = true;
   public boolean isDefaultTexture = true;
   private String currentTexture = "/gui/items.png";

    protected Item(int par1)
    {
        this.id = 256 + par1;

        if (byId[256 + par1] != null)
        {
            System.out.println("CONFLICT @ " + par1 + " item slot already occupied by " + byId[256 + par1] + " while adding " + this); // Forge
        }

        byId[256 + par1] = this;
        GameData.newItemAdded(this); // Forge

        // MCPC
        /* 
         * This is required for mods that use custom materials or else plugins such as ModifyWorld will receive NPE's 
         * when looking up the custom material name.
         */
        org.bukkit.Material.addMaterial(256 + par1);
        // Forge start
        if (!(this instanceof ItemBlock))
        {
            this.isDefaultTexture = "/gui/items.png".equals(this.getTextureFile());
        }
        // Forge end
    }

    /**
     * Sets the icon index for this item. Returns the item.
     */
    public Item c(int par1)
    {
        this.textureId = par1;
        return this;
    }

    public Item d(int par1)
    {
        this.maxStackSize = par1;
        return this;
    }

    public Item b(int par1, int par2)
    {
        this.textureId = par1 + par2 * 16;
        return this;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean interactWith(ItemStack par1ItemStack, EntityHuman par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        return false;
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    public float getDestroySpeed(ItemStack par1ItemStack, Block par2Block)
    {
        return 1.0F;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack a(ItemStack par1ItemStack, World par2World, EntityHuman par3EntityPlayer)
    {
        return par1ItemStack;
    }

    public ItemStack b(ItemStack par1ItemStack, World par2World, EntityHuman par3EntityPlayer)
    {
        return par1ItemStack;
    }

    /**
     * Returns the maximum size of the stack for a specific item. *Isn't this more a Set than a Get?*
     */
    public int getMaxStackSize()
    {
        return this.maxStackSize;
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int filterData(int par1)
    {
        return 0;
    }

    public boolean l()
    {
        return this.cn;
    }

    protected Item a(boolean par1)
    {
        this.cn = par1;
        return this;
    }

    /**
     * Returns the maximum damage an item can take.
     */
    public int getMaxDurability()
    {
        return this.durability;
    }

    /**
     * set max damage of an Item
     */
    public Item setMaxDurability(int par1)  // MCPC
    {
        this.durability = par1;
        return this;
    }

    public boolean n()
    {
        return this.durability > 0 && !this.cn;
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean a(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving)
    {
        return false;
    }

    public boolean a(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving)
    {
        return false;
    }

    /**
     * Returns the damage against a given entity.
     */
    public int a(Entity par1Entity)
    {
        return 1;
    }

    /**
     * Returns if the item (tool) can harvest results from the block type.
     */
    public boolean canDestroySpecialBlock(Block par1Block)
    {
        return false;
    }

    /**
     * Called when a player right clicks a entity with a item.
     */
    public boolean a(ItemStack par1ItemStack, EntityLiving par2EntityLiving)
    {
        return false;
    }

    /**
     * Sets bFull3D to True and return the object.
     */
    public Item o()
    {
        this.cm = true;
        return this;
    }

    /**
     * set name of item from language file
     */
    public Item b(String par1Str)
    {
        this.name = "item." + par1Str;
        return this;
    }

    public String i(ItemStack par1ItemStack)
    {
        String var2 = this.d(par1ItemStack);
        return var2 == null ? "" : LocaleI18n.get(var2);
    }

    public String getName()
    {
        return this.name;
    }

    public String d(ItemStack par1ItemStack)
    {
        return this.name;
    }

    public Item a(Item par1Item)
    {
        this.craftingResult = par1Item;
        return this;
    }

    /**
     * If this returns true, after a recipe involving this item is crafted the container item will be added to the
     * player's inventory instead of remaining in the crafting grid.
     */
    public boolean j(ItemStack par1ItemStack)
    {
        return true;
    }

    /**
     * If this function returns true (or the item is damageable), the ItemStack's NBT tag will be sent to the client.
     */
    public boolean q()
    {
        return true;
    }

    public Item r()
    {
        return this.craftingResult;
    }

    /**
     * True if this Item has a container item (a.k.a. crafting result)
     */
    public boolean s()
    {
        return this.craftingResult != null;
    }

    public String t()
    {
        return LocaleI18n.get(this.getName() + ".name");
    }

    public String k(ItemStack par1ItemStack)
    {
        return LocaleI18n.get(this.d(par1ItemStack) + ".name");
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void a(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {}

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     */
    public void d(ItemStack par1ItemStack, World par2World, EntityHuman par3EntityPlayer) {}

    /**
     * false for all Items except sub-classes of ItemMapBase
     */
    public boolean f()
    {
        return false;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAnimation b_(ItemStack par1ItemStack)
    {
        return EnumAnimation.a;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int c_(ItemStack par1ItemStack)
    {
        return 0;
    }

    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    public void a(ItemStack par1ItemStack, World par2World, EntityHuman par3EntityPlayer, int par4) {}

    /**
     * Sets the string representing this item's effect on a potion when used as an ingredient.
     */
    protected Item c(String par1Str)
    {
        this.co = par1Str;
        return this;
    }

    /**
     * Returns a string representing what this item does to a potion.
     */
    public String u()
    {
        return this.co;
    }

    /**
     * Returns true if this item serves as a potion ingredient (its ingredient information is not null).
     */
    public boolean v()
    {
        return this.co != null;
    }

    public String l(ItemStack par1ItemStack)
    {
        return ("" + LocaleLanguage.a().c(this.i(par1ItemStack))).trim();
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean d_(ItemStack par1ItemStack)
    {
        return this.getMaxStackSize() == 1 && this.n();
    }

    protected MovingObjectPosition a(World par1World, EntityHuman par2EntityPlayer, boolean par3)
    {
        float var4 = 1.0F;
        float var5 = par2EntityPlayer.lastPitch + (par2EntityPlayer.pitch - par2EntityPlayer.lastPitch) * var4;
        float var6 = par2EntityPlayer.lastYaw + (par2EntityPlayer.yaw - par2EntityPlayer.lastYaw) * var4;
        double var7 = par2EntityPlayer.lastX + (par2EntityPlayer.locX - par2EntityPlayer.lastX) * (double)var4;
        double var9 = par2EntityPlayer.lastY + (par2EntityPlayer.locY - par2EntityPlayer.lastY) * (double)var4 + 1.62D - (double)par2EntityPlayer.height;
        double var11 = par2EntityPlayer.lastZ + (par2EntityPlayer.locZ - par2EntityPlayer.lastZ) * (double)var4;
        Vec3D var13 = par1World.getVec3DPool().create(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float) Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float) Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5.0D;
        // Forge start
        if (par2EntityPlayer instanceof EntityPlayer)
        {
            var21 = ((EntityPlayer)par2EntityPlayer).playerInteractManager.getBlockReachDistance();
        }
        // Forge end
        Vec3D var23 = var13.add((double) var18 * var21, (double) var17 * var21, (double) var20 * var21);
        return par1World.rayTrace(var13, var23, par3, !par3);
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int c()
    {
        return 0;
    }

    /**
     * returns this;
     */
    public Item a(CreativeModeTab par1CreativeTabs)
    {
        this.a = par1CreativeTabs;
        return this;
    }

    public boolean x()
    {
        return true;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    public boolean a(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        return false;
    }

    /* =========================================================== FORGE START ===============================================================*/
    /**
    * Called when a player drops the item into the world,
    * returning false from this will prevent the item from
    * being removed from the players inventory and spawning
    * in the world
    *
    * @param player The player that dropped the item
    * @param item The item stack, before the item is removed.
    */
   public boolean onDroppedByPlayer(ItemStack item, EntityHuman player)
   {
       return true;
   }

   /**
    * This is called when the item is used, before the block is activated.
    * @param stack The Item Stack
    * @param player The Player that used the item
    * @param world The Current World
    * @param x Target X Position
    * @param y Target Y Position
    * @param z Target Z Position
    * @param side The side of the target hit
    * @return Return true to prevent any further processing.
    */
   public boolean onItemUseFirst(ItemStack stack, EntityHuman player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
   {
       return false;
   }

   /**
    * Metadata-sensitive version of getStrVsBlock
    * @param itemstack The Item Stack
    * @param block The block the item is trying to break
    * @param metadata The items current metadata
    * @return The damage strength
    */
   public float getStrVsBlock(ItemStack itemstack, Block block, int metadata)
   {
       return getDestroySpeed(itemstack, block);
   }

   /**
    * Called by CraftingManager to determine if an item is reparable.
    * @return True if reparable
    */
   public boolean isRepairable()
   {
       return canRepair && this.n();
   }

   /**
    * Call to disable repair recipes.
    * @return The current Item instance
    */
   public Item setNoRepair()
   {
       canRepair = false;
       return this;
   }

   /**
    * Called before a block is broken.  Return true to prevent default block harvesting.
    *
    * Note: In SMP, this is called on both client and server sides!
    *
    * @param itemstack The current ItemStack
    * @param X The X Position
    * @param Y The X Position
    * @param Z The X Position
    * @param player The Player that is wielding the item
    * @return True to prevent harvesting, false to continue as normal
    */
   public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityHuman player)
   {
       return false;
   }

   /**
    * Called each tick while using an item.
    * @param stack The Item being used
    * @param player The Player using the item
    * @param count The amount of time in tick the item has been used for continuously
    */
   public void onUsingItemTick(ItemStack stack, EntityHuman player, int count)
   {
   }

   /**
    * Called when the player Left Clicks (attacks) an entity.
    * Processed before damage is done, if return value is true further processing is canceled
    * and the entity is not attacked.
    *
    * @param stack The Item being used
    * @param player The player that is attacking
    * @param entity The entity being attacked
    * @return True to cancel the rest of the interaction.
    */
   public boolean onLeftClickEntity(ItemStack stack, EntityHuman player, Entity entity)
   {
       return false;
   }

   /**
    * Grabs the current texture file used for this block
    */
   public String getTextureFile()
   {
       if (this instanceof ItemBlock)
       {
           return Block.byId[((ItemBlock)this).g()].getTextureFile();
       }
       return currentTexture;
   }

   /**
    * Sets the current texture file for this item, used when rendering.
    * Default is "/gui/items.png"
    *
    * @param texture The texture file
    */
   public Item setTextureFile(String texture)
   {
       currentTexture = texture;
       isDefaultTexture = false;
       return this;
   }

   /**
    * ItemStack sensitive version of getContainerItem.
    * Returns a full ItemStack instance of the result.
    *
    * @param itemStack The current ItemStack
    * @return The resulting ItemStack
    */
   public ItemStack getContainerItemStack(ItemStack itemStack)
   {
       if (!this.s())
       {
           return null;
       }
       return new ItemStack(this.r());
   }

   /**
    * Retrieves the normal 'lifespan' of this item when it is dropped on the ground as a EntityItem.
    * This is in ticks, standard result is 6000, or 5 mins.
    *
    * @param itemStack The current ItemStack
    * @param world The world the entity is in
    * @return The normal lifespan in ticks.
    */
   public int getEntityLifespan(ItemStack itemStack, World world)
   {
       return 6000;
   }

   /**
    * Determines if this Item has a special entity for when they are in the world.
    * Is called when a EntityItem is spawned in the world, if true and Item#createCustomEntity
    * returns non null, the EntityItem will be destroyed and the new Entity will be added to the world.
    *
    * @param stack The current item stack
    * @return True of the item has a custom entity, If true, Item#createCustomEntity will be called
    */
   public boolean hasCustomEntity(ItemStack stack)
   {
       return false;
   }

   /**
    * This function should return a new entity to replace the dropped item.
    * Returning null here will not kill the EntityItem and will leave it to function normally.
    * Called when the item it placed in a world.
    *
    * @param world The world object
    * @param location The EntityItem object, useful for getting the position of the entity
    * @param itemstack The current item stack
    * @return A new Entity object to spawn or null
    */
   public Entity createEntity(World world, Entity location, ItemStack itemstack)
   {
       return null;
   }

   /**
    * Determines the base experience for a player when they remove this item from a furnace slot.
    * This number must be between 0 and 1 for it to be valid.
    * This number will be multiplied by the stack size to get the total experience.
    *
    * @param item The item stack the player is picking up.
    * @return The amount to award for each item.
    */
   public float getSmeltingExperience(ItemStack item)
   {
       return -1; //-1 will default to the old lookups.
   }

   /**
    * Generates the base Random item for a specific instance of the chest gen,
    * Enchanted books use this to pick a random enchantment.
    *
    * @param chest The chest category to generate for
    * @param rnd World RNG
    * @param original Original result registered with the chest gen hooks.
    * @return New values to use as the random item, typically this will be original
    */
   public StructurePieceTreasure getChestGenBase(ChestGenHooks chest, Random rnd, StructurePieceTreasure original)
   {
       if (this instanceof ItemEnchantedBook)
       {
           return ((ItemEnchantedBook)this).a(rnd,
                   original.d,
                   original.e, original.a);
       }
       return original;
   }

   /**
    *
    * Should this item, when held, allow sneak-clicks to pass through to the underlying block?
    *
    * @param par2World
    * @param par4
    * @param par5
    * @param par6
    * @return
    */
   public boolean shouldPassSneakingClickToBlock(World par2World, int par4, int par5, int par6)
   {
       return false;
   }
   /* =========================================================== FORGE END ===============================================================*/    
    
    static
    {
        StatisticList.c();
    }
}
