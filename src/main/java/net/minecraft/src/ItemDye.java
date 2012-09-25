package net.minecraft.src;

// CraftBukkit start

import net.minecraft.server.Block;
import net.minecraft.server.BlockCloth;
import net.minecraft.server.BlockCrops;
import net.minecraft.server.BlockDirectional;
import net.minecraft.server.BlockLog;
import net.minecraft.server.BlockMushroom;
import net.minecraft.server.BlockSapling;
import net.minecraft.server.BlockStem;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.server.Item;
import net.minecraft.server.MathHelper;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.SheepDyeWoolEvent;
// CraftBukkit end

public class ItemDye extends Item {

    public static final String[] a = new String[] { "black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lightBlue", "magenta", "orange", "white"};
    public static final int[] b = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 2651799, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};

    public ItemDye(int i) {
        super(i);
        this.a(true);
        this.setMaxDurability(0);
        this.a(CreativeModeTab.l);
    }

    public String c(net.minecraft.src.ItemStack itemstack) {
        int i = MathHelper.a(itemstack.getData(), 0, 15);

        return super.getName() + "." + a[i];
    }

    public boolean interactWith(net.minecraft.src.ItemStack itemstack, EntityPlayer entityhuman, net.minecraft.src.World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (!entityhuman.e(i, j, k)) {
            return false;
        } else {
            int i1;
            int j1;

            if (itemstack.getData() == 15) {
                i1 = world.getBlockId(i, j, k);
                if (i1 == Block.SAPLING.blockID) {
                    if (!world.isStatic) {
                        // CraftBukkit start
                        Player player = (entityhuman instanceof EntityPlayerMP) ? (Player) entityhuman.getBukkitEntity() : null;
                        ((BlockSapling) Block.SAPLING).grow(world, i, j, k, world.rand, true, player, itemstack);
                        //--itemstack.count; - called later if the bonemeal attempt was succesful
                        // CraftBukkit end
                    }

                    return true;
                }

                if (i1 == Block.BROWN_MUSHROOM.blockID || i1 == Block.RED_MUSHROOM.blockID) {
                    // CraftBukkit start
                    if (!world.isStatic) {
                        Player player = (entityhuman instanceof EntityPlayerMP) ? (Player) entityhuman.getBukkitEntity() : null;
                        ((BlockMushroom) Block.blocksList[i1]).grow(world, i, j, k, world.rand, true, player, itemstack);
                        //--itemstack.count; - called later if the bonemeal attempt was succesful
                        // CraftBukkit end
                    }

                    return true;
                }

                if (i1 == Block.MELON_STEM.blockID || i1 == Block.PUMPKIN_STEM.blockID) {
                    if (world.getData(i, j, k) == 7) {
                        return false;
                    }

                    if (!world.isStatic) {
                        ((BlockStem) Block.blocksList[i1]).l(world, i, j, k);
                        --itemstack.count;
                    }

                    return true;
                }

                if (i1 == Block.CROPS.blockID) {
                    if (world.getData(i, j, k) == 7) {
                        return false;
                    }

                    if (!world.isStatic) {
                        ((BlockCrops) Block.CROPS).c_(world, i, j, k);
                        --itemstack.count;
                    }

                    return true;
                }

                if (i1 == Block.COCOA.blockID) {
                    if (!world.isStatic) {
                        world.setBlockMetadataWithNotify(i, j, k, 8 | BlockDirectional.d(world.getData(i, j, k)));
                        --itemstack.count;
                    }

                    return true;
                }

                if (i1 == Block.GRASS.blockID) {
                    if (!world.isStatic) {
                        --itemstack.count;

                        label135:
                        for (j1 = 0; j1 < 128; ++j1) {
                            int k1 = i;
                            int l1 = j + 1;
                            int i2 = k;

                            for (int j2 = 0; j2 < j1 / 16; ++j2) {
                                k1 += d.nextInt(3) - 1;
                                l1 += (d.nextInt(3) - 1) * d.nextInt(3) / 2;
                                i2 += d.nextInt(3) - 1;
                                if (world.getBlockId(k1, l1 - 1, i2) != Block.GRASS.blockID || world.s(k1, l1, i2)) {
                                    continue label135;
                                }
                            }

                            if (world.getBlockId(k1, l1, i2) == 0) {
                                if (d.nextInt(10) != 0) {
                                    if (Block.LONG_GRASS.d(world, k1, l1, i2)) {
                                        world.setBlockAndMetadataWithNotify(k1, l1, i2, Block.LONG_GRASS.blockID, 1);
                                    }
                                } else if (d.nextInt(3) != 0) {
                                    if (Block.YELLOW_FLOWER.d(world, k1, l1, i2)) {
                                        world.setBlockWithNotify(k1, l1, i2, Block.YELLOW_FLOWER.blockID);
                                    }
                                } else if (Block.RED_ROSE.d(world, k1, l1, i2)) {
                                    world.setBlockWithNotify(k1, l1, i2, Block.RED_ROSE.blockID);
                                }
                            }
                        }
                    }

                    return true;
                }
            } else if (itemstack.getData() == 3) {
                i1 = world.getBlockId(i, j, k);
                j1 = world.getData(i, j, k);
                if (i1 == Block.LOG.blockID && BlockLog.e(j1) == 3) {
                    if (l == 0) {
                        return false;
                    }

                    if (l == 1) {
                        return false;
                    }

                    if (l == 2) {
                        --k;
                    }

                    if (l == 3) {
                        ++k;
                    }

                    if (l == 4) {
                        --i;
                    }

                    if (l == 5) {
                        ++i;
                    }

                    if (world.isEmpty(i, j, k)) {
                        world.setBlockWithNotify(i, j, k, Block.COCOA.blockID);
                        if (world.getBlockId(i, j, k) == Block.COCOA.blockID) {
                            Block.blocksList[Block.COCOA.blockID].postPlace(world, i, j, k, l, f, f1, f2);
                        }

                        if (!entityhuman.capabilities.canInstantlyBuild) {
                            --itemstack.count;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public boolean a(net.minecraft.src.ItemStack itemstack, EntityLiving entityliving) {
        if (entityliving instanceof EntitySheep) {
            EntitySheep entitysheep = (EntitySheep) entityliving;
            int i = BlockCloth.e_(itemstack.getData());

            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != i) {
                // CraftBukkit start
                byte bColor = (byte) i;
                SheepDyeWoolEvent event = new SheepDyeWoolEvent((org.bukkit.entity.Sheep) entitysheep.getBukkitEntity(), org.bukkit.DyeColor.getByData(bColor));
                entitysheep.worldObj.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return false;
                }

                i = (byte) event.getColor().getData();
                // CraftBukkit end

                entitysheep.setColor(i);
                --itemstack.count;
            }

            return true;
        } else {
            return false;
        }
    }
}
