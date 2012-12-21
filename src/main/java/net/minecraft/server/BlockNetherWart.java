package net.minecraft.server;

import java.util.ArrayList;
import java.util.Random;

import net.minecraftforge.common.ForgeDirection;

public class BlockNetherWart extends BlockFlower {

    protected BlockNetherWart(int i) {
        super(i, 226);
        this.b(true);
        float f = 0.5F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.a((CreativeModeTab) null);
    }

    protected boolean d_(int i) {
        return i == Block.SOUL_SAND.id;
    }

    public boolean d(World world, int i, int j, int k) {
    	// Forge start
        Block block = Block.byId[world.getTypeId(i, j - 1, k)];
        return (block != null && block.canSustainPlant(world, i, j - 1, k, ForgeDirection.UP, this));
        // Forge end
    }

    public void b(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);

        if (l < 3 && random.nextInt(10) == 0) {
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j, k, this.id, ++l); // CraftBukkit
        }

        super.b(world, i, j, k, random);
    }

    public int a(int i, int j) {
        return j >= 3 ? this.textureId + 2 : (j > 0 ? this.textureId + 1 : this.textureId);
    }

    public int d() {
        return 6;
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
    	super.dropNaturally(world, i, j, k, l, f, i1);
    }

    public int getDropType(int i, Random random, int j) {
        return 0;
    }

    public int a(Random random) {
        return 0;
    }
    
    // Forge start
    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
    	ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    	int count = 1;
    
    	if (metadata >= 3)
    	{
    		count = 2 + world.random.nextInt(3) + (fortune > 0 ? world.random.nextInt(fortune + 1) : 0);
    	}
    
    	for (int i = 0; i < count; i++)
    	{
    		ret.add(new ItemStack(Item.NETHER_STALK));
    	}
    
    	return ret;
    }
    // Forge end
}