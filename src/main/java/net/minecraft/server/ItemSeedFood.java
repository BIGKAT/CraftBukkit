package net.minecraft.server;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit
// Forge start
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
// Forge end

public class ItemSeedFood extends ItemFood implements IPlantable { // Forge

    private int b;
    private int c;

    public ItemSeedFood(int i, int j, float f, int k, int l) {
        super(i, j, f, false);
        this.b = k;
        this.c = l;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (l != 1) {
            return false;
        } else if (entityhuman.a(i, j, k, l, itemstack) && entityhuman.a(i, j + 1, k, l, itemstack)) {
            int i1 = world.getTypeId(i, j, k);

            if (i1 == this.c && world.isEmpty(i, j + 1, k)) {
                CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j + 1, k); // CraftBukkit

                world.setTypeId(i, j + 1, k, this.b);

                // CraftBukkit start - seeds
                org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, i, j, k);

                if (event.isCancelled() || !event.canBuild()) {
                    event.getBlockPlaced().setTypeId(0);
                    return false;
                }
                // CraftBukkit end

                --itemstack.count;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    // Forge start
    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z)
    {
        return EnumPlantType.Crop;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z)
    {
        return b;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z)
    {
        return 0;
    }
    // Forge end
}
