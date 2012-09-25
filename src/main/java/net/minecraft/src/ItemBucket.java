package net.minecraft.src;

// CraftBukkit start

import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.EnumMovingObjectType;
import net.minecraft.server.Item;
import net.minecraft.server.Material;
import net.minecraft.server.MovingObjectPosition;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
// CraftBukkit end

public class ItemBucket extends Item {

    private int a;

    public ItemBucket(int i, int j) {
        super(i);
        this.maxStackSize = 1;
        this.a = j;
        this.a(CreativeModeTab.f);
    }

    public net.minecraft.src.ItemStack a(net.minecraft.src.ItemStack itemstack, net.minecraft.src.World world, EntityPlayer entityhuman) {
        float f = 1.0F;
        double d0 = entityhuman.lastX + (entityhuman.posX - entityhuman.lastX) * (double) f;
        double d1 = entityhuman.lastY + (entityhuman.posY - entityhuman.lastY) * (double) f + 1.62D - (double) entityhuman.yOffset;
        double d2 = entityhuman.lastZ + (entityhuman.posZ - entityhuman.lastZ) * (double) f;
        boolean flag = this.a == 0;
        MovingObjectPosition movingobjectposition = this.a(world, entityhuman, flag);

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

                if (this.a == 0) {
                    if (!entityhuman.e(i, j, k)) {
                        return itemstack;
                    }

                    if (world.getMaterial(i, j, k) == Material.WATER && world.getData(i, j, k) == 0) {
                        // CraftBukkit start
                        PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, i, j, k, -1, itemstack, Item.WATER_BUCKET);

                        if (event.isCancelled()) {
                            return itemstack;
                        }
                        // CraftBukkit end
                        world.setBlockWithNotify(i, j, k, 0);
                        if (entityhuman.capabilities.canInstantlyBuild) {
                            return itemstack;
                        }

                        net.minecraft.src.ItemStack result = CraftItemStack.createNMSItemStack(event.getItemStack()); // CraftBukkit - TODO: Check this stuff later... Not sure how this behavior should work
                        if (--itemstack.count <= 0) {
                            return result; // CraftBukkit
                        }

                        if (!entityhuman.inventory.pickup(result)) { // CraftBukkit
                            entityhuman.drop(CraftItemStack.createNMSItemStack(event.getItemStack())); // CraftBukkit
                        }

                        return itemstack;
                    }

                    if (world.getMaterial(i, j, k) == Material.LAVA && world.getData(i, j, k) == 0) {
                        // CraftBukkit start
                        PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, i, j, k, -1, itemstack, Item.LAVA_BUCKET);

                        if (event.isCancelled()) {
                            return itemstack;
                        }
                        // CraftBukkit end
                        world.setBlockWithNotify(i, j, k, 0);
                        if (entityhuman.capabilities.canInstantlyBuild) {
                            return itemstack;
                        }

                        net.minecraft.src.ItemStack result = CraftItemStack.createNMSItemStack(event.getItemStack()); // CraftBukkit - TODO: Check this stuff later... Not sure how this behavior should work
                        if (--itemstack.count <= 0) {
                            return result; // CraftBukkit
                        }

                        if (!entityhuman.inventory.pickup(result)) { // CraftBukkit
                            entityhuman.drop(CraftItemStack.createNMSItemStack(event.getItemStack())); // CraftBukkit
                        }

                        return itemstack;
                    }
                } else {
                    if (this.a < 0) {
                        // CraftBukkit start
                        PlayerBucketEmptyEvent event = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, i, j, k, movingobjectposition.face, itemstack);

                        if (event.isCancelled()) {
                            return itemstack;
                        }

                        return CraftItemStack.createNMSItemStack(event.getItemStack());
                    }

                    int clickedX = i, clickedY = j, clickedZ = k;
                    // CraftBukkit end

                    if (movingobjectposition.face == 0) {
                        --j;
                    }

                    if (movingobjectposition.face == 1) {
                        ++j;
                    }

                    if (movingobjectposition.face == 2) {
                        --k;
                    }

                    if (movingobjectposition.face == 3) {
                        ++k;
                    }

                    if (movingobjectposition.face == 4) {
                        --i;
                    }

                    if (movingobjectposition.face == 5) {
                        ++i;
                    }

                    if (!entityhuman.e(i, j, k)) {
                        return itemstack;
                    }

                    // CraftBukkit start
                    PlayerBucketEmptyEvent event = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, clickedX, clickedY, clickedZ, movingobjectposition.face, itemstack);

                    if (event.isCancelled()) {
                        return itemstack;
                    }
                    // CraftBukkit end

                    if (this.a(world, d0, d1, d2, i, j, k) && !entityhuman.capabilities.canInstantlyBuild) {
                        return CraftItemStack.createNMSItemStack(event.getItemStack()); // CraftBukkit
                    }
                }
            } else if (this.a == 0 && movingobjectposition.entity instanceof EntityCow) {
                // CraftBukkit start - This codepath seems to be *NEVER* called
                org.bukkit.Location loc = movingobjectposition.entity.getBukkitEntity().getLocation();
                PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), -1, itemstack, Item.MILK_BUCKET);

                if (event.isCancelled()) {
                    return itemstack;
                }

                return CraftItemStack.createNMSItemStack(event.getItemStack());
                // CraftBukkit end
            }

            return itemstack;
        }
    }

    public boolean a(net.minecraft.src.World world, double d0, double d1, double d2, int i, int j, int k) {
        if (this.a <= 0) {
            return false;
        } else if (!world.isEmpty(i, j, k) && world.getMaterial(i, j, k).isBuildable()) {
            return false;
        } else {
            if (world.worldProvider.d && this.a == Block.WATER.blockID) {
                world.makeSound(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                for (int l = 0; l < 8; ++l) {
                    world.a("largesmoke", (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else {
                world.setBlockAndMetadataWithNotify(i, j, k, this.a, 0);
            }

            return true;
        }
    }
}
