package net.minecraft.src;

import net.minecraft.server.Block;
import net.minecraft.server.EnumMovingObjectType;
import net.minecraft.server.ItemWithAuxData;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit

public class ItemLilyPad extends ItemWithAuxData {

    public ItemLilyPad(int i) {
        super(i, false);
    }

    public net.minecraft.src.ItemStack a(net.minecraft.src.ItemStack itemstack, net.minecraft.src.World world, EntityPlayer entityhuman) {
        MovingObjectPosition movingobjectposition = this.a(world, entityhuman, true);

        if (movingobjectposition == null) {
            return itemstack;
        } else {
            if (movingobjectposition.type == EnumMovingObjectType.TILE) {
                int i = movingobjectposition.b;
                int j = movingobjectposition.c;
                int k = movingobjectposition.d;

                if (!world.a(entityhuman, i, j, k)) {
                    return itemstack;
                }

                if (!entityhuman.e(i, j, k)) {
                    return itemstack;
                }

                if (world.getMaterial(i, j, k) == Material.WATER && world.getData(i, j, k) == 0 && world.isEmpty(i, j + 1, k)) {
                    CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j + 1, k); // CraftBukkit

                    world.setBlockWithNotify(i, j + 1, k, Block.WATER_LILY.blockID);

                    // CraftBukkit start - waterlily
                    org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, i, j, k);

                    if (event.isCancelled() || !event.canBuild()) {
                        event.getBlockPlaced().setTypeId(0);
                        return itemstack;
                    }
                    // CraftBukkit end

                    if (!entityhuman.capabilities.canInstantlyBuild) {
                        --itemstack.count;
                    }
                }
            }

            return itemstack;
        }
    }
}
