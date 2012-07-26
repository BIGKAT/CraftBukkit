package net.minecraft.server;

import forge.ForgeHooks;
import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit

public class ItemHoe extends Item {

    public ItemHoe(int i, EnumToolMaterial enumtoolmaterial) {
        super(i);
        this.maxStackSize = 1;
        this.setMaxDurability(enumtoolmaterial.a());
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if (!entityhuman.d(i, j, k)) {
            return false;
        } else {
            if(ForgeHooks.onUseHoe(itemstack, entityhuman, world, i, j, k)) 
            {
                itemstack.damage(1, entityhuman);
                return true;
            }
            int i1 = world.getTypeId(i, j, k);
            int j1 = world.getTypeId(i, j + 1, k);

            if ((l == 0 || j1 != 0 || i1 != Block.GRASS.id) && i1 != Block.DIRT.id) {
                return false;
            } else {
                Block block = Block.SOIL;

                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getName(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                if (world.isStatic) {
                    return true;
                } else {
                    CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit

                    world.setTypeId(i, j, k, block.id);

                    // CraftBukkit start - Hoes - blockface -1 for 'SELF'
                    org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, i, j, k);

                    if (event.isCancelled() || !event.canBuild()) {
                        event.getBlockPlaced().setTypeId(blockState.getTypeId());
                        return false;
                    }
                    // CraftBukkit end

                    itemstack.damage(1, entityhuman);
                    return true;
                }
            }
        }
    }
}
