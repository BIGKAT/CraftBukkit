package net.minecraft.src;

import net.minecraft.server.Block;
import net.minecraft.server.CreativeModeTab;
import net.minecraft.src.Entity;
import net.minecraft.server.EntityTypes;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.Facing;
import net.minecraft.server.Item;

public class ItemMonsterPlacer extends Item {

    public ItemMonsterPlacer(int i) {
        super(i);
        this.a(true);
        this.a(CreativeModeTab.f);
    }

    public boolean interactWith(net.minecraft.src.ItemStack itemstack, EntityPlayer entityhuman, net.minecraft.src.World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (world.isStatic || itemstack.getData() == 48 || itemstack.getData() == 49 || itemstack.getData() == 63) { // CraftBukkit
            return true;
        } else {
            int i1 = world.getBlockId(i, j, k);

            i += Facing.b[l];
            j += Facing.c[l];
            k += Facing.d[l];
            double d0 = 0.0D;

            if (l == 1 && i1 == Block.FENCE.blockID || i1 == Block.NETHER_FENCE.blockID) {
                d0 = 0.5D;
            }

            if (a(world, itemstack.getData(), (double) i + 0.5D, (double) j + d0, (double) k + 0.5D) && !entityhuman.capabilities.canInstantlyBuild) {
                --itemstack.count;
            }

            return true;
        }
    }

    public static boolean a(net.minecraft.src.World world, int i, double d0, double d1, double d2) {
        if (!EntityTypes.a.containsKey(Integer.valueOf(i))) {
            return false;
        } else {
            Entity entity = EntityTypes.a(i, world);

            if (entity != null && entity instanceof EntityLiving) { // CraftBukkit
                entity.setPositionRotation(d0, d1, d2, world.rand.nextFloat() * 360.0F, 0.0F);
                if (entity instanceof EntityVillager) {
                    EntityVillager entityvillager = (EntityVillager) entity;

                    entityvillager.setProfession(entityvillager.au().nextInt(5));
                    world.addEntity(entityvillager);
                    return true;
                }

                world.addEntity(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG); // CraftBukkit
                ((EntityLiving) entity).aH();
            }

            return entity != null;
        }
    }
}
