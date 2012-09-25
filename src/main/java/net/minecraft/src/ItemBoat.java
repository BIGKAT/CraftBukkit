package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityBoat;
import net.minecraft.server.EnumMovingObjectType;
import net.minecraft.server.Item;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MovingObjectPosition;

public class ItemBoat extends Item {

    public ItemBoat(int i) {
        super(i);
        this.maxStackSize = 1;
        this.a(CreativeModeTab.e);
    }

    public net.minecraft.src.ItemStack a(net.minecraft.src.ItemStack itemstack, net.minecraft.src.World world, EntityPlayer entityhuman) {
        float f = 1.0F;
        float f1 = entityhuman.lastPitch + (entityhuman.rotationPitch - entityhuman.lastPitch) * f;
        float f2 = entityhuman.lastYaw + (entityhuman.rotationYaw - entityhuman.lastYaw) * f;
        double d0 = entityhuman.lastX + (entityhuman.posX - entityhuman.lastX) * (double) f;
        double d1 = entityhuman.lastY + (entityhuman.posY - entityhuman.lastY) * (double) f + 1.62D - (double) entityhuman.height;
        double d2 = entityhuman.lastZ + (entityhuman.posZ - entityhuman.lastZ) * (double) f;
        Vec3 vec3d = Vec3.a().create(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3 vec3d1 = vec3d.add((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        MovingObjectPosition movingobjectposition = world.rayTrace(vec3d, vec3d1, true);

        if (movingobjectposition == null) {
            return itemstack;
        } else {
            Vec3 vec3d2 = entityhuman.i(f);
            boolean flag = false;
            float f9 = 1.0F;
            List list = world.getEntities(entityhuman, entityhuman.boundingBox.a(vec3d2.a * d3, vec3d2.b * d3, vec3d2.c * d3).grow((double) f9, (double) f9, (double) f9));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (entity.L()) {
                    float f10 = entity.Y();
                    AxisAlignedBB axisalignedbb = entity.boundingBox.grow((double) f10, (double) f10, (double) f10);

                    if (axisalignedbb.a(vec3d)) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                return itemstack;
            } else {
                if (movingobjectposition.type == EnumMovingObjectType.TILE) {
                    int i = movingobjectposition.b;
                    int j = movingobjectposition.c;
                    int k = movingobjectposition.d;

                    if (!world.isStatic) {
                        // CraftBukkit start - Boat placement
                        org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent(entityhuman, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, i, j, k, movingobjectposition.face, itemstack);

                        if (event.isCancelled()) {
                            return itemstack;
                        }
                        // CraftBukkit end

                        if (world.getBlockId(i, j, k) == Block.SNOW.blockID) {
                            --j;
                        }

                        world.addEntity(new EntityBoat(world, (double) ((float) i + 0.5F), (double) ((float) j + 1.0F), (double) ((float) k + 0.5F)));
                    }

                    if (!entityhuman.capabilities.canInstantlyBuild) {
                        --itemstack.count;
                    }
                }

                return itemstack;
            }
        }
    }
}
