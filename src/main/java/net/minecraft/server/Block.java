package net.minecraft.server;

import java.util.ArrayList;
import java.util.Random;

public class Block {

    public static final StepSound d = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound e = new StepSound("wood", 1.0F, 1.0F);
    public static final StepSound f = new StepSound("gravel", 1.0F, 1.0F);
    public static final StepSound g = new StepSound("grass", 1.0F, 1.0F);
    public static final StepSound h = new StepSound("stone", 1.0F, 1.0F);
    public static final StepSound i = new StepSound("stone", 1.0F, 1.5F);
    public static final StepSound j = new StepSoundStone("stone", 1.0F, 1.0F);
    public static final StepSound k = new StepSound("cloth", 1.0F, 1.0F);
    public static final StepSound l = new StepSoundSand("sand", 1.0F, 1.0F);
    public static final Block[] byId = new Block[4096];
    public static final boolean[] n = new boolean[4096];
    public static final int[] lightBlock = new int[4096];
    public static final boolean[] p = new boolean[4096];
    public static final int[] lightEmission = new int[4096];
    public static final boolean[] r = new boolean[4096];
    public static boolean[] s = new boolean[4096];
    public static final Block STONE = (new BlockStone(1, 1)).c(1.5F).b(10.0F).a(h).a("stone");
    public static final BlockGrass GRASS = (BlockGrass) (new BlockGrass(2)).c(0.6F).a(g).a("grass");
    public static final Block DIRT = (new BlockDirt(3, 2)).c(0.5F).a(f).a("dirt");
    public static final Block COBBLESTONE = (new Block(4, 16, Material.STONE)).c(2.0F).b(10.0F).a(h).a("stonebrick");
    public static final Block WOOD = (new BlockWood(5)).c(2.0F).b(5.0F).a(e).a("wood").j();
    public static final Block SAPLING = (new BlockSapling(6, 15)).c(0.0F).a(g).a("sapling").j();
    public static final Block BEDROCK = (new Block(7, 17, Material.STONE)).l().b(6000000.0F).a(h).a("bedrock").s();
    public static final Block WATER = (new BlockFlowing(8, Material.WATER)).c(100.0F).f(3).a("water").s().j();
    public static final Block STATIONARY_WATER = (new BlockStationary(9, Material.WATER)).c(100.0F).f(3).a("water").s().j();
    public static final Block LAVA = (new BlockFlowing(10, Material.LAVA)).c(0.0F).a(1.0F).f(255).a("lava").s().j();
    public static final Block STATIONARY_LAVA = (new BlockStationary(11, Material.LAVA)).c(100.0F).a(1.0F).f(255).a("lava").s().j();
    public static final Block SAND = (new BlockSand(12, 18)).c(0.5F).a(l).a("sand");
    public static final Block GRAVEL = (new BlockGravel(13, 19)).c(0.6F).a(f).a("gravel");
    public static final Block GOLD_ORE = (new BlockOre(14, 32)).c(3.0F).b(5.0F).a(h).a("oreGold");
    public static final Block IRON_ORE = (new BlockOre(15, 33)).c(3.0F).b(5.0F).a(h).a("oreIron");
    public static final Block COAL_ORE = (new BlockOre(16, 34)).c(3.0F).b(5.0F).a(h).a("oreCoal");
    public static final Block LOG = (new BlockLog(17)).c(2.0F).a(e).a("log").j();
    public static final BlockLeaves LEAVES = (BlockLeaves) (new BlockLeaves(18, 52)).c(0.2F).f(1).a(g).a("leaves").j();
    public static final Block SPONGE = (new BlockSponge(19)).c(0.6F).a(g).a("sponge");
    public static final Block GLASS = (new BlockGlass(20, 49, Material.SHATTERABLE, false)).c(0.3F).a(j).a("glass");
    public static final Block LAPIS_ORE = (new BlockOre(21, 160)).c(3.0F).b(5.0F).a(h).a("oreLapis");
    public static final Block LAPIS_BLOCK = (new Block(22, 144, Material.STONE)).c(3.0F).b(5.0F).a(h).a("blockLapis");
    public static final Block DISPENSER = (new BlockDispenser(23)).c(3.5F).a(h).a("dispenser").j();
    public static final Block SANDSTONE = (new BlockSandStone(24)).a(h).c(0.8F).a("sandStone").j();
    public static final Block NOTE_BLOCK = (new BlockNote(25)).c(0.8F).a("musicBlock").j();
    public static final Block BED = (new BlockBed(26)).c(0.2F).a("bed").s().j();
    public static final Block GOLDEN_RAIL = (new BlockMinecartTrack(27, 179, true)).c(0.7F).a(i).a("goldenRail").j();
    public static final Block DETECTOR_RAIL = (new BlockMinecartDetector(28, 195)).c(0.7F).a(i).a("detectorRail").j();
    public static final Block PISTON_STICKY = (new BlockPiston(29, 106, true)).a("pistonStickyBase").j();
    public static final Block WEB = (new BlockWeb(30, 11)).f(1).c(4.0F).a("web");
    public static final BlockLongGrass LONG_GRASS = (BlockLongGrass) (new BlockLongGrass(31, 39)).c(0.0F).a(g).a("tallgrass");
    public static final BlockDeadBush DEAD_BUSH = (BlockDeadBush) (new BlockDeadBush(32, 55)).c(0.0F).a(g).a("deadbush");
    public static final Block PISTON = (new BlockPiston(33, 107, false)).a("pistonBase").j();
    public static final BlockPistonExtension PISTON_EXTENSION = (BlockPistonExtension) (new BlockPistonExtension(34, 107)).j();
    public static final Block WOOL = (new BlockCloth()).c(0.8F).a(k).a("cloth").j();
    public static final BlockPistonMoving PISTON_MOVING = new BlockPistonMoving(36);
    public static final BlockFlower YELLOW_FLOWER = (BlockFlower) (new BlockFlower(37, 13)).c(0.0F).a(g).a("flower");
    public static final BlockFlower RED_ROSE = (BlockFlower) (new BlockFlower(38, 12)).c(0.0F).a(g).a("rose");
    public static final BlockFlower BROWN_MUSHROOM = (BlockFlower) (new BlockMushroom(39, 29)).c(0.0F).a(g).a(0.125F).a("mushroom");
    public static final BlockFlower RED_MUSHROOM = (BlockFlower) (new BlockMushroom(40, 28)).c(0.0F).a(g).a("mushroom");
    public static final Block GOLD_BLOCK = (new BlockOreBlock(41, 23)).c(3.0F).b(10.0F).a(i).a("blockGold");
    public static final Block IRON_BLOCK = (new BlockOreBlock(42, 22)).c(5.0F).b(10.0F).a(i).a("blockIron");
    public static final Block DOUBLE_STEP = (new BlockStep(43, true)).c(2.0F).b(10.0F).a(h).a("stoneSlab");
    public static final Block STEP = (new BlockStep(44, false)).c(2.0F).b(10.0F).a(h).a("stoneSlab");
    public static final Block BRICK = (new Block(45, 7, Material.STONE)).c(2.0F).b(10.0F).a(h).a("brick");
    public static final Block TNT = (new BlockTNT(46, 8)).c(0.0F).a(g).a("tnt");
    public static final Block BOOKSHELF = (new BlockBookshelf(47, 35)).c(1.5F).a(e).a("bookshelf");
    public static final Block MOSSY_COBBLESTONE = (new Block(48, 36, Material.STONE)).c(2.0F).b(10.0F).a(h).a("stoneMoss");
    public static final Block OBSIDIAN = (new BlockObsidian(49, 37)).c(50.0F).b(2000.0F).a(h).a("obsidian");
    public static final Block TORCH = (new BlockTorch(50, 80)).c(0.0F).a(0.9375F).a(e).a("torch").j();
    public static final BlockFire FIRE = (BlockFire) (new BlockFire(51, 31)).c(0.0F).a(1.0F).a(e).a("fire").s();
    public static final Block MOB_SPAWNER = (new BlockMobSpawner(52, 65)).c(5.0F).a(i).a("mobSpawner").s();
    public static final Block WOOD_STAIRS = (new BlockStairs(53, WOOD)).a("stairsWood").j();
    public static final Block CHEST = (new BlockChest(54)).c(2.5F).a(e).a("chest").j();
    public static final Block REDSTONE_WIRE = (new BlockRedstoneWire(55, 164)).c(0.0F).a(d).a("redstoneDust").s().j();
    public static final Block DIAMOND_ORE = (new BlockOre(56, 50)).c(3.0F).b(5.0F).a(h).a("oreDiamond");
    public static final Block DIAMOND_BLOCK = (new BlockOreBlock(57, 24)).c(5.0F).b(10.0F).a(i).a("blockDiamond");
    public static final Block WORKBENCH = (new BlockWorkbench(58)).c(2.5F).a(e).a("workbench");
    public static final Block CROPS = (new BlockCrops(59, 88)).c(0.0F).a(g).a("crops").s().j();
    public static final Block SOIL = (new BlockSoil(60)).c(0.6F).a(f).a("farmland").j();
    public static final Block FURNACE = (new BlockFurnace(61, false)).c(3.5F).a(h).a("furnace").j();
    public static final Block BURNING_FURNACE = (new BlockFurnace(62, true)).c(3.5F).a(h).a(0.875F).a("furnace").j();
    public static final Block SIGN_POST = (new BlockSign(63, TileEntitySign.class, true)).c(1.0F).a(e).a("sign").s().j();
    public static final Block WOODEN_DOOR = (new BlockDoor(64, Material.WOOD)).c(3.0F).a(e).a("doorWood").s().j();
    public static final Block LADDER = (new BlockLadder(65, 83)).c(0.4F).a(e).a("ladder").j();
    public static final Block RAILS = (new BlockMinecartTrack(66, 128, false)).c(0.7F).a(i).a("rail").j();
    public static final Block COBBLESTONE_STAIRS = (new BlockStairs(67, COBBLESTONE)).a("stairsStone").j();
    public static final Block WALL_SIGN = (new BlockSign(68, TileEntitySign.class, false)).c(1.0F).a(e).a("sign").s().j();
    public static final Block LEVER = (new BlockLever(69, 96)).c(0.5F).a(e).a("lever").j();
    public static final Block STONE_PLATE = (new BlockPressurePlate(70, STONE.textureId, EnumMobType.MOBS, Material.STONE)).c(0.5F).a(h).a("pressurePlate").j();
    public static final Block IRON_DOOR_BLOCK = (new BlockDoor(71, Material.ORE)).c(5.0F).a(i).a("doorIron").s().j();
    public static final Block WOOD_PLATE = (new BlockPressurePlate(72, WOOD.textureId, EnumMobType.EVERYTHING, Material.WOOD)).c(0.5F).a(e).a("pressurePlate").j();
    public static final Block REDSTONE_ORE = (new BlockRedstoneOre(73, 51, false)).c(3.0F).b(5.0F).a(h).a("oreRedstone").j();
    public static final Block GLOWING_REDSTONE_ORE = (new BlockRedstoneOre(74, 51, true)).a(0.625F).c(3.0F).b(5.0F).a(h).a("oreRedstone").j();
    public static final Block REDSTONE_TORCH_OFF = (new BlockRedstoneTorch(75, 115, false)).c(0.0F).a(e).a("notGate").j();
    public static final Block REDSTONE_TORCH_ON = (new BlockRedstoneTorch(76, 99, true)).c(0.0F).a(0.5F).a(e).a("notGate").j();
    public static final Block STONE_BUTTON = (new BlockButton(77, STONE.textureId)).c(0.5F).a(h).a("button").j();
    public static final Block SNOW = (new BlockSnow(78, 66)).c(0.1F).a(k).a("snow").f(0);
    public static final Block ICE = (new BlockIce(79, 67)).c(0.5F).f(3).a(j).a("ice");
    public static final Block SNOW_BLOCK = (new BlockSnowBlock(80, 66)).c(0.2F).a(k).a("snow");
    public static final Block CACTUS = (new BlockCactus(81, 70)).c(0.4F).a(k).a("cactus");
    public static final Block CLAY = (new BlockClay(82, 72)).c(0.6F).a(f).a("clay");
    public static final Block SUGAR_CANE_BLOCK = (new BlockReed(83, 73)).c(0.0F).a(g).a("reeds").s();
    public static final Block JUKEBOX = (new BlockJukeBox(84, 74)).c(2.0F).b(10.0F).a(h).a("jukebox").j();
    public static final Block FENCE = (new BlockFence(85, 4)).c(2.0F).b(5.0F).a(e).a("fence");
    public static final Block PUMPKIN = (new BlockPumpkin(86, 102, false)).c(1.0F).a(e).a("pumpkin").j();
    public static final Block NETHERRACK = (new BlockBloodStone(87, 103)).c(0.4F).a(h).a("hellrock");
    public static final Block SOUL_SAND = (new BlockSlowSand(88, 104)).c(0.5F).a(l).a("hellsand");
    public static final Block GLOWSTONE = (new BlockLightStone(89, 105, Material.SHATTERABLE)).c(0.3F).a(j).a(1.0F).a("lightgem");
    public static final BlockPortal PORTAL = (BlockPortal) (new BlockPortal(90, 14)).c(-1.0F).a(j).a(0.75F).a("portal");
    public static final Block JACK_O_LANTERN = (new BlockPumpkin(91, 102, true)).c(1.0F).a(e).a(1.0F).a("litpumpkin").j();
    public static final Block CAKE_BLOCK = (new BlockCake(92, 121)).c(0.5F).a(k).a("cake").s().j();
    public static final Block DIODE_OFF = (new BlockDiode(93, false)).c(0.0F).a(e).a("diode").s().j();
    public static final Block DIODE_ON = (new BlockDiode(94, true)).c(0.0F).a(0.625F).a(e).a("diode").s().j();
    public static final Block LOCKED_CHEST = (new BlockLockedChest(95)).c(0.0F).a(1.0F).a(e).a("lockedchest").a(true).j();
    public static final Block TRAP_DOOR = (new BlockTrapdoor(96, Material.WOOD)).c(3.0F).a(e).a("trapdoor").s().j();
    public static final Block MONSTER_EGGS = (new BlockMonsterEggs(97)).c(0.75F);
    public static final Block SMOOTH_BRICK = (new BlockSmoothBrick(98)).c(1.5F).b(10.0F).a(h).a("stonebricksmooth");
    public static final Block BIG_MUSHROOM_1 = (new BlockHugeMushroom(99, Material.WOOD, 142, 0)).c(0.2F).a(e).a("mushroom").j();
    public static final Block BIG_MUSHROOM_2 = (new BlockHugeMushroom(100, Material.WOOD, 142, 1)).c(0.2F).a(e).a("mushroom").j();
    public static final Block IRON_FENCE = (new BlockThinFence(101, 85, 85, Material.ORE, true)).c(5.0F).b(10.0F).a(i).a("fenceIron");
    public static final Block THIN_GLASS = (new BlockThinFence(102, 49, 148, Material.SHATTERABLE, false)).c(0.3F).a(j).a("thinGlass");
    public static final Block MELON = (new BlockMelon(103)).c(1.0F).a(e).a("melon");
    public static final Block PUMPKIN_STEM = (new BlockStem(104, PUMPKIN)).c(0.0F).a(e).a("pumpkinStem").j();
    public static final Block MELON_STEM = (new BlockStem(105, MELON)).c(0.0F).a(e).a("pumpkinStem").j();
    public static final Block VINE = (new BlockVine(106)).c(0.2F).a(g).a("vine").j();
    public static final Block FENCE_GATE = (new BlockFenceGate(107, 4)).c(2.0F).b(5.0F).a(e).a("fenceGate").j();
    public static final Block BRICK_STAIRS = (new BlockStairs(108, BRICK)).a("stairsBrick").j();
    public static final Block STONE_STAIRS = (new BlockStairs(109, SMOOTH_BRICK)).a("stairsStoneBrickSmooth").j();
    public static final BlockMycel MYCEL = (BlockMycel) (new BlockMycel(110)).c(0.6F).a(g).a("mycel");
    public static final Block WATER_LILY = (new BlockWaterLily(111, 76)).c(0.0F).a(g).a("waterlily");
    public static final Block NETHER_BRICK = (new Block(112, 224, Material.STONE)).c(2.0F).b(10.0F).a(h).a("netherBrick");
    public static final Block NETHER_FENCE = (new BlockFence(113, 224, Material.STONE)).c(2.0F).b(10.0F).a(h).a("netherFence");
    public static final Block NETHER_BRICK_STAIRS = (new BlockStairs(114, NETHER_BRICK)).a("stairsNetherBrick").j();
    public static final Block NETHER_WART = (new BlockNetherWart(115)).a("netherStalk").j();
    public static final Block ENCHANTMENT_TABLE = (new BlockEnchantmentTable(116)).c(5.0F).b(2000.0F).a("enchantmentTable");
    public static final Block BREWING_STAND = (new BlockBrewingStand(117)).c(0.5F).a(0.125F).a("brewingStand").j();
    public static final Block CAULDRON = (new BlockCauldron(118)).c(2.0F).a("cauldron").j();
    public static final Block ENDER_PORTAL = (new BlockEnderPortal(119, Material.PORTAL)).c(-1.0F).b(6000000.0F);
    public static final Block ENDER_PORTAL_FRAME = (new BlockEnderPortalFrame(120)).a(j).a(0.125F).c(-1.0F).a("endPortalFrame").j().b(6000000.0F);
    public static final Block WHITESTONE = (new Block(121, 175, Material.STONE)).c(3.0F).b(15.0F).a(h).a("whiteStone");
    public static final Block DRAGON_EGG = (new BlockDragonEgg(122, 167)).c(3.0F).b(15.0F).a(h).a(0.125F).a("dragonEgg");
    public static final Block REDSTONE_LAMP_OFF = (new BlockRedstoneLamp(123, false)).c(0.3F).a(j).a("redstoneLight");
    public static final Block REDSTONE_LAMP_ON = (new BlockRedstoneLamp(124, true)).c(0.3F).a(j).a("redstoneLight");
    public int textureId;
    public final int id;
    protected float strength;
    protected float durability;
    protected boolean bR;
    protected boolean bS;
    protected boolean bT;
    protected boolean isTileEntity;
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    public StepSound stepSound;
    public float cc;
    public final Material material;
    public float frictionFactor;
    private String name;
    protected ArrayList<ItemStack> dropList = new ArrayList<ItemStack>(); // CraftBukkit

    protected Block(int i, Material material) {
        this.bR = true;
        this.bS = true;
        this.stepSound = d;
        this.cc = 1.0F;
        this.frictionFactor = 0.6F;
        if (byId[i] != null) {
            throw new IllegalArgumentException("Slot " + i + " is already occupied by " + byId[i] + " when adding " + this);
        } else {
            this.material = material;
            byId[i] = this;
            this.id = i;
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            n[i] = this.a();
            lightBlock[i] = this.a() ? 255 : 0;
            p[i] = !material.blocksLight();
        }
    }

    protected Block j() {
        r[this.id] = true;
        return this;
    }

    protected void k() {}

    protected Block(int i, int j, Material material) {
        this(i, material);
        this.textureId = j;
    }

    protected Block a(StepSound stepsound) {
        this.stepSound = stepsound;
        return this;
    }

    protected Block f(int i) {
        lightBlock[this.id] = i;
        return this;
    }

    protected Block a(float f) {
        lightEmission[this.id] = (int) (15.0F * f);
        return this;
    }

    protected Block b(float f) {
        this.durability = f * 3.0F;
        return this;
    }

    public static boolean g(int i) {
        Block block = byId[i];

        return block == null ? false : block.material.j() && block.b();
    }

    public boolean b() {
        return true;
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        return !this.material.isSolid();
    }

    public int c() {
        return 0;
    }

    protected Block c(float f) {
        this.strength = f;
        if (this.durability < f * 5.0F) {
            this.durability = f * 5.0F;
        }

        return this;
    }

    protected Block l() {
        this.c(-1.0F);
        return this;
    }

    public float m() {
        return this.strength;
    }

    protected Block a(boolean flag) {
        this.bT = flag;
        return this;
    }

    public boolean n() {
        return this.bT;
    }

    public boolean o() {
        //return this.isTileEntity;
        return hasTileEntity(0);
    }

    public void a(float f, float f1, float f2, float f3, float f4, float f5) {
        this.minX = (double) f;
        this.minY = (double) f1;
        this.minZ = (double) f2;
        this.maxX = (double) f3;
        this.maxY = (double) f4;
        this.maxZ = (double) f5;
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return iblockaccess.getMaterial(i, j, k).isBuildable();
    }

    public int a(int i, int j) {
        return this.a(i);
    }

    public int a(int i) {
        return this.textureId;
    }

    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
        AxisAlignedBB axisalignedbb1 = this.e(world, i, j, k);

        if (axisalignedbb1 != null && axisalignedbb.a(axisalignedbb1)) {
            arraylist.add(axisalignedbb1);
        }
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        return AxisAlignedBB.b((double) i + this.minX, (double) j + this.minY, (double) k + this.minZ, (double) i + this.maxX, (double) j + this.maxY, (double) k + this.maxZ);
    }

    public boolean a() {
        return true;
    }

    public boolean a(int i, boolean flag) {
        return this.E_();
    }

    public boolean E_() {
        return true;
    }

    public void a(World world, int i, int j, int k, Random random) {}

    public void postBreak(World world, int i, int j, int k, int l) {}

    public void doPhysics(World world, int i, int j, int k, int l) {}

    public int d() {
        return 10;
    }

    public void onPlace(World world, int i, int j, int k) {}

    public void remove(World world, int i, int j, int k) {}

    public int a(Random random) {
        return 1;
    }

    public int getDropType(int i, Random random, int j) {
        return this.id;
    }

    @Deprecated
    public float getDamage(EntityHuman entityhuman) {
        //return this.strength < 0.0F ? 0.0F : (!entityhuman.b(this) ? 1.0F / this.strength / 100.0F : entityhuman.a(this) / this.strength / 30.0F);
        return blockStrength(entityhuman, 0);
    }

    public final void b(World world, int i, int j, int k, int l, int i1) {
        this.dropNaturally(world, i, j, k, l, 1.0F, i1);
        this.doActualDrop(world, i, j, k); // CraftBukkit
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (!world.isStatic) {
            int j1 = this.getDropCount(i1, world.random);

            for (int k1 = 0; k1 < j1; ++k1) {
                // CraftBukkit - <= to < to allow for plugins to completely disable block drops from explosions
                if (world.random.nextFloat() < f) {
                    int l1 = this.getDropType(l, world.random, i1);

                    if (l1 > 0) {
                        this.a(world, i, j, k, new ItemStack(l1, 1, this.getDropData(l)));
                    }
                }
            }
        }
    }

    protected void a(World world, int i, int j, int k, ItemStack itemstack) {
        // CraftBukkit start - the logic of this function is moved into finishDrop
        // This is such a hackish change it's rediculous.
        this.dropList.add(itemstack);
    }

    public final void finishDrop(World world, int i, int j, int k, ItemStack itemstack) {
        // CraftBukkit end
        if (!world.isStatic) {
            float f = 0.7F;
            double d0 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d1 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            double d2 = (double) (world.random.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(world, (double) i + d0, (double) j + d1, (double) k + d2, itemstack);

            entityitem.pickupDelay = 10;
            world.addEntity(entityitem);
        }
    }

    protected int getDropData(int i) {
        return 0;
    }

    public float a(Entity entity) {
        return this.durability / 5.0F;
    }

    public MovingObjectPosition a(World world, int i, int j, int k, Vec3D vec3d, Vec3D vec3d1) {
        this.updateShape(world, i, j, k);
        vec3d = vec3d.add((double) (-i), (double) (-j), (double) (-k));
        vec3d1 = vec3d1.add((double) (-i), (double) (-j), (double) (-k));
        Vec3D vec3d2 = vec3d.a(vec3d1, this.minX);
        Vec3D vec3d3 = vec3d.a(vec3d1, this.maxX);
        Vec3D vec3d4 = vec3d.b(vec3d1, this.minY);
        Vec3D vec3d5 = vec3d.b(vec3d1, this.maxY);
        Vec3D vec3d6 = vec3d.c(vec3d1, this.minZ);
        Vec3D vec3d7 = vec3d.c(vec3d1, this.maxZ);

        if (!this.a(vec3d2)) {
            vec3d2 = null;
        }

        if (!this.a(vec3d3)) {
            vec3d3 = null;
        }

        if (!this.b(vec3d4)) {
            vec3d4 = null;
        }

        if (!this.b(vec3d5)) {
            vec3d5 = null;
        }

        if (!this.c(vec3d6)) {
            vec3d6 = null;
        }

        if (!this.c(vec3d7)) {
            vec3d7 = null;
        }

        Vec3D vec3d8 = null;

        if (vec3d2 != null && (vec3d8 == null || vec3d.b(vec3d2) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d2;
        }

        if (vec3d3 != null && (vec3d8 == null || vec3d.b(vec3d3) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d3;
        }

        if (vec3d4 != null && (vec3d8 == null || vec3d.b(vec3d4) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d4;
        }

        if (vec3d5 != null && (vec3d8 == null || vec3d.b(vec3d5) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d5;
        }

        if (vec3d6 != null && (vec3d8 == null || vec3d.b(vec3d6) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d6;
        }

        if (vec3d7 != null && (vec3d8 == null || vec3d.b(vec3d7) < vec3d.b(vec3d8))) {
            vec3d8 = vec3d7;
        }

        if (vec3d8 == null) {
            return null;
        } else {
            byte b0 = -1;

            if (vec3d8 == vec3d2) {
                b0 = 4;
            }

            if (vec3d8 == vec3d3) {
                b0 = 5;
            }

            if (vec3d8 == vec3d4) {
                b0 = 0;
            }

            if (vec3d8 == vec3d5) {
                b0 = 1;
            }

            if (vec3d8 == vec3d6) {
                b0 = 2;
            }

            if (vec3d8 == vec3d7) {
                b0 = 3;
            }

            return new MovingObjectPosition(i, j, k, b0, vec3d8.add((double) i, (double) j, (double) k));
        }
    }

    private boolean a(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.b >= this.minY && vec3d.b <= this.maxY && vec3d.c >= this.minZ && vec3d.c <= this.maxZ;
    }

    private boolean b(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.a >= this.minX && vec3d.a <= this.maxX && vec3d.c >= this.minZ && vec3d.c <= this.maxZ;
    }

    private boolean c(Vec3D vec3d) {
        return vec3d == null ? false : vec3d.a >= this.minX && vec3d.a <= this.maxX && vec3d.b >= this.minY && vec3d.b <= this.maxY;
    }

    public void wasExploded(World world, int i, int j, int k) {}

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return this.canPlace(world, i, j, k);
    }

    public boolean canPlace(World world, int i, int j, int k) {
        int l = world.getTypeId(i, j, k);

        return l == 0 || byId[l].material.isReplacable();
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        return false;
    }

    public void b(World world, int i, int j, int k, Entity entity) {}

    public void postPlace(World world, int i, int j, int k, int l) {}

    public void attack(World world, int i, int j, int k, EntityHuman entityhuman) {}

    public void a(World world, int i, int j, int k, Entity entity, Vec3D vec3d) {}

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {}

    public boolean a(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return false;
    }

    public boolean isPowerSource() {
        return false;
    }

    public void a(World world, int i, int j, int k, Entity entity) {}

    public boolean d(World world, int i, int j, int k, int l) {
        return false;
    }

    public void f() {}

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        entityhuman.a(StatisticList.C[this.id], 1);
        entityhuman.c(0.025F);
        // CraftBukkit start - A way to separate statistics from the logic of determining what to drop
        this.doActualDrop(world, i, j, k);
    }

    public void doActualDrop(World world, int i, int j, int k) {
        for (ItemStack stack : this.dropList) {
            finishDrop(world, i, j, k, stack);
        }
        this.dropList.clear();
    }

    public void setDrops(ArrayList<ItemStack> drops) {
        this.dropList = drops;
    }

    public ArrayList<ItemStack> calculateDrops(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        // CraftBukkit end
        if (this.b() && !this.isTileEntity && EnchantmentManager.hasSilkTouchEnchantment(entityhuman.inventory)) {
            ItemStack itemstack = this.a_(l);

            if (itemstack != null) {
                this.a(world, i, j, k, itemstack);
            }
        } else {
            int i1 = EnchantmentManager.getBonusBlockLootEnchantmentLevel(entityhuman.inventory);

            this.dropNaturally(world, i, j, k, l, 1.0F, i1); // CraftBukkit
        }
        return this.dropList; // CraftBukkit
    }

    protected boolean h() {
        return this.b() && !this.isTileEntity;
    }

    protected ItemStack a_(int i) {
        int j = 0;

        if (this.id >= 0 && this.id < Item.byId.length && Item.byId[this.id].e()) {
            j = i;
        }

        return new ItemStack(this.id, 1, j);
    }

    public int getDropCount(int i, Random random) {
        return this.a(random);
    }

    public boolean f(World world, int i, int j, int k) {
        return true;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving) {}

    public Block a(String s) {
        this.name = "tile." + s;
        return this;
    }

    public String getName() {
        return LocaleI18n.get(this.q() + ".name");
    }

    public String q() {
        return this.name;
    }

    public void a(World world, int i, int j, int k, int l, int i1) {}

    public boolean r() {
        return this.bS;
    }

    protected Block s() {
        this.bS = false;
        return this;
    }

    public int g() {
        return this.material.getPushReaction();
    }

    public void a(World world, int i, int j, int k, Entity entity, float f) {}

    /* =================================================== FORGE START =====================================*/
    /**
     * Get a light value for this block, normal ranges are between 0 and 15
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return The light value
     */
    public int getLightValue(IBlockAccess world, int x, int y, int z) 
    {
        return lightEmission[id];
    }

    /**
     * Checks if a player or entity can use this block to 'climb' like a ladder.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block should act like a ladder
     */
    public boolean isLadder(World world, int x, int y, int z) 
    {
        return false;
    }
    
    /**
     * Return true if the block is a normal, solid cube.  This
     * determines indirect power state, entity ejection from blocks, and a few
     * others.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is a full cube
     */
    public boolean isBlockNormalCube(World world, int x, int y, int z) 
    {
        return material.j() && b();
    }

    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param size The side to check
     * @return True if the block is solid on the specified side.
     */
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, int side) 
    {
        int meta = world.getData(x, y, z);
        if (this instanceof BlockStep)
        {
            return (((meta & 8) == 8 && (side == 1)) || b());
        }
        else if (this instanceof BlockSoil)
        {
            return (side != 1 && side != 0);
        }
        else if (this instanceof BlockStairs)
        {
            boolean flipped = ((meta & 4) != 0);
            return ((meta & 3) + side == 5) || (side == 1 && flipped);
        }
        return isBlockNormalCube(world, x, y, z);
    }

    /**
     * Determines if a new block can be replace the space occupied by this one,
     * Used in the player's placement code to make the block act like water, and lava.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is replaceable by another block
     */
    public boolean isBlockReplaceable(World world, int x, int y, int z) 
    {
        return false;
    }

    /**
     * Determines if this block should set fire and deal fire damage
     * to entities coming into contact with it.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block should deal damage
     */
    public boolean isBlockBurning(World world, int x, int y, int z) 
    {
        return false;
    }
    
    /**
     * Determines this block should be treated as an air block
     * by the rest of the code. This method is primarily
     * useful for creating pure logic-blocks that will be invisible 
     * to the player and otherwise interact as air would.
     * 
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block considered air
     */
    public boolean isAirBlock(World world, int x, int y, int z) 
    {
        return false;
    }

    /**
     * Metadata sensitive version of the default getHardness function.
     * 
     * @param meta The block's current metatdata
     * @return Block hardness
     */
    public float getHardness(int meta) 
    {
        return strength;
    }

    /**
     * Determines this block's strength against being destroyed by a player.
     * 
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return A value 
     */
    public float blockStrength(World world, EntityHuman player, int x, int y, int z) 
    {
        return blockStrength(player, world.getData(x, y, z));
    }

    /**
     * Determines this block's strength against being destroyed by a player.
     * 
     * @param player The player damaging the block, may be null
     * @param meta The block's current metadata
     * @return The larger the value, the faster it breaks.
     */
    public float blockStrength(EntityHuman player, int meta) 
    {
        return ForgeHooks.blockStrength(this, player, meta);
    }

    /**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     * 
     * @param player The player damaging the block, may be null
     * @param meta The block's current metadata
     * @return True tospawn the drops
     */
    public boolean canHarvestBlock(EntityHuman player, int meta) 
    {
        return ForgeHooks.canHarvestBlock(this, player, meta);
    }

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.  
     * 
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     * 
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is actually destroyed.
     */
    public boolean removeBlockByPlayer(World world, EntityHuman player, int x, int y, int z) 
    {
        return world.setTypeId(x, y, z, 0);
    }

    /**
     * Called when a new CreativeContainer is opened, populate the list 
     * with all of the items for this block you want a player in creative mode
     * to have access to.
     * 
     * @param itemList The list of items to display on the creative inventory.
     */
    public void addCreativeItems(ArrayList itemList)
    {       
    }
    
    protected static int blockFireSpreadSpeed[] = new int[256];
    protected static int blockFlammability[] = new int[256];
    
    /**
     * Chance that fire will spread and consume this block.
     * 300 being a 100% chance, 0, being a 0% chance.
     * 
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param face The face that the fire is coming from
     * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
     */
    public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, int face)
    {
        return blockFlammability[id];
    }
    
    /**
     * Called when fire is updating, checks if a block face can catch fire.
     * 
     * 
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param face The face that the fire is coming from
     * @return True if the face can be on fire, false otherwise.
     */
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, int face)
    {
        return getFlammability(world, x, y, z, metadata, face) > 0;
    }
    
    /**
     * Called when fire is updating on a neighbor block.
     * The higher the number returned, the faster fire will spread around this block.
     * 
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param face The face that the fire is coming from
     * @return A number that is used to determine the speed of fire growth around the block
     */
    public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, int face)
    {
        return blockFireSpreadSpeed[id];
    }
    
    /**
     * Currently only called by fire when it is on top of this block.
     * Returning true will prevent the fire from naturally dying during updating.
     * Also prevents firing from dying from rain.
     * 
     * @param world The current world
     * @param x The blocks X position
     * @param y The blocks Y position
     * @param z The blocks Z position
     * @param metadata The blocks current metadata
     * @param face The face that the fire is coming from
     * @return 
     */
    public boolean isFireSource(World world, int x, int y, int z, int metadata, int face)
    {
        if (id == Block.NETHERRACK.id && face == 0)
        {
            return true;
        }
        if ((world.worldProvider instanceof WorldProviderTheEnd) && id == Block.BEDROCK.id && face == 0)
        {
            return true;
        }
        return false;
    }

    /**
     * Called by BlockFire to setup the burn values of vanilla blocks.
     * @param id The block id
     * @param encouragement How much the block encourages fire to spread
     * @param flammability how easy a block is to catch fire
     */
    public static void setBurnProperties(int id, int encouragement, int flammability)
    {
        blockFireSpreadSpeed[id] = encouragement;
        blockFlammability[id] = flammability;
    }

    /**
     * Called throughout the code as a replacement for block instanceof BlockContainer
     * Moving this to the Block base class allows for mods that wish to extend vinella 
     * blocks, and also want to have a tile entity on that block, may.
     * 
     * Return true from this function to specify this block has a tile entity.
     * 
     * @param metadata Metadata of the current block
     * @return True if block has a tile entity, false otherwise
     */
    public boolean hasTileEntity(int metadata)
    {
        return isTileEntity;
    }
    
    /**
     * Called throughout the code as a replacement for BlockContainer.getBlockEntity
     * Return the same thing you would from that function.
     * This will fall back to BlockContainer.getBlockEntity if this block is a BlockContainer.
     * 
     * @param metadata The Metadata of the current block
     * @return A instance of a class extending TileEntity
     */
    public TileEntity getTileEntity(int metadata)
    {
        if (this instanceof BlockContainer)
        {
            return ((BlockContainer)this).getBlockEntity(metadata);
        }
        return null;
    }    
    
    /**
     * Metadata and fortune sensitive version, this replaces the old (int meta, Random rand)
     * version in 1.1. 
     * 
     * @param meta Blocks Metadata
     * @param fortune Current item fortune level
     * @param random Random number generator
     * @return The number of items to drop
     */
    public int quantityDropped(int meta, int fortune, Random random)
    {
        return getDropCount(fortune, random);
    }
    
    /**
     * This returns a complete list of items dropped from this block.
     * 
     * @param world The current world
     * @param x X Position
     * @param Y Y Position
     * @param Z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        
        int count = quantityDropped(metadata, fortune, world.random);
        for(int i = 0; i < count; i++)
        {
            int id = getDropType(metadata, world.random, 0);
            if (id > 0)
            {
                ret.add(new ItemStack(id, 1, getDropData(metadata)));
            }
        }
        return ret;
    }

    static {
        Item.byId[WOOL.id] = (new ItemCloth(WOOL.id - 256)).a("cloth");
        Item.byId[LOG.id] = (new ItemWithAuxData(LOG.id - 256, LOG)).a("log");
        Item.byId[WOOD.id] = (new ItemWithAuxData(WOOD.id - 256, WOOD)).a("wood");
        Item.byId[SMOOTH_BRICK.id] = (new ItemWithAuxData(SMOOTH_BRICK.id - 256, SMOOTH_BRICK)).a("stonebricksmooth");
        Item.byId[SANDSTONE.id] = (new ItemWithAuxData(SANDSTONE.id - 256, SANDSTONE)).a("sandStone");
        Item.byId[STEP.id] = (new ItemStep(STEP.id - 256)).a("stoneSlab");
        Item.byId[SAPLING.id] = (new ItemSapling(SAPLING.id - 256)).a("sapling");
        Item.byId[LEAVES.id] = (new ItemLeaves(LEAVES.id - 256)).a("leaves");
        Item.byId[VINE.id] = new ItemColoredBlock(VINE.id - 256, false);
        Item.byId[LONG_GRASS.id] = (new ItemColoredBlock(LONG_GRASS.id - 256, true)).a(new String[] { "shrub", "grass", "fern"});
        Item.byId[WATER_LILY.id] = new ItemWaterLily(WATER_LILY.id - 256);
        Item.byId[PISTON.id] = new ItemPiston(PISTON.id - 256);
        Item.byId[PISTON_STICKY.id] = new ItemPiston(PISTON_STICKY.id - 256);
        Item.byId[BIG_MUSHROOM_1.id] = new ItemWithAuxData(BIG_MUSHROOM_1.id - 256, BIG_MUSHROOM_1); // CraftBukkit
        Item.byId[BIG_MUSHROOM_2.id] = new ItemWithAuxData(BIG_MUSHROOM_2.id - 256, BIG_MUSHROOM_2); // CraftBukkit
        Item.byId[MOB_SPAWNER.id] = new ItemWithAuxData(MOB_SPAWNER.id - 256, MOB_SPAWNER); // CraftBukkit

        for (int i = 0; i < 256; ++i) {
            if (byId[i] != null) {
                if (Item.byId[i] == null) {
                    Item.byId[i] = new ItemBlock(i - 256);
                    byId[i].k();
                }

                boolean flag = false;

                if (i > 0 && byId[i].c() == 10) {
                    flag = true;
                }

                if (i > 0 && byId[i] instanceof BlockStep) {
                    flag = true;
                }

                if (i == SOIL.id) {
                    flag = true;
                }

                if (p[i]) {
                    flag = true;
                }

                s[i] = flag;
            }
        }

        p[0] = true;
        StatisticList.b();
    }

    // CraftBukkit start - getDropData(int) is protected
    public static int getDropData(Block block, int data) {
        return block.getDropData(data);
    }
    // CraftBukkit end
}
