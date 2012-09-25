package net.minecraft.src;

import java.util.Iterator;

import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Block;
import net.minecraft.server.EntityWeather;
import net.minecraft.server.MathHelper;

import org.bukkit.event.block.BlockIgniteEvent; // CraftBukkit

public class EntityLightningBolt extends EntityWeather {

    private int lifeTicks;
    public long a = 0L;
    private int c;

    // CraftBukkit start
    private org.bukkit.craftbukkit.CraftWorld cworld;
    public boolean isEffect = false;

    public EntityLightningBolt(net.minecraft.src.World world, double d0, double d1, double d2) {
        this(world, d0, d1, d2, false);
    }

    public EntityLightningBolt(net.minecraft.src.World world, double d0, double d1, double d2, boolean isEffect) {
        // CraftBukkit end

        super(world);

        // CraftBukkit start
        this.isEffect = isEffect;
        this.cworld = world.getWorld();
        // CraftBukkit end

        this.setPositionRotation(d0, d1, d2, 0.0F, 0.0F);
        this.lifeTicks = 2;
        this.a = this.random.nextLong();
        this.c = this.random.nextInt(3) + 1;

        // CraftBukkit
        if (!isEffect && world.difficulty >= 2 && world.areChunksLoaded(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2), 10)) {
            int i = MathHelper.floor(d0);
            int j = MathHelper.floor(d1);
            int k = MathHelper.floor(d2);

            if (world.getBlockId(i, j, k) == 0 && Block.FIRE.canPlace(world, i, j, k)) {
                // CraftBukkit start
                BlockIgniteEvent event = new BlockIgniteEvent(this.cworld.getBlockAt(i, j, k), BlockIgniteEvent.IgniteCause.LIGHTNING, null);
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    world.setBlockWithNotify(i, j, k, Block.FIRE.blockID);
                }
                // CraftBukkit end
            }

            for (i = 0; i < 4; ++i) {
                j = MathHelper.floor(d0) + this.random.nextInt(3) - 1;
                k = MathHelper.floor(d1) + this.random.nextInt(3) - 1;
                int l = MathHelper.floor(d2) + this.random.nextInt(3) - 1;

                if (world.getBlockId(j, k, l) == 0 && Block.FIRE.canPlace(world, j, k, l)) {
                    // CraftBukkit start
                    BlockIgniteEvent event = new BlockIgniteEvent(this.cworld.getBlockAt(j, k, l), BlockIgniteEvent.IgniteCause.LIGHTNING, null);
                    world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        world.setBlockWithNotify(j, k, l, Block.FIRE.blockID);
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    public void h_() {
        super.h_();
        if (this.lifeTicks == 2) {
            this.worldObj.makeSound(this.posX, this.posY, this.posZ, "ambient.weather.thunder", 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            this.worldObj.makeSound(this.posX, this.posY, this.posZ, "random.explode", 2.0F, 0.5F + this.random.nextFloat() * 0.2F);
        }

        --this.lifeTicks;
        if (this.lifeTicks < 0) {
            if (this.c == 0) {
                this.setDead();
            } else if (this.lifeTicks < -this.random.nextInt(10)) {
                --this.c;
                this.lifeTicks = 1;
                this.a = this.random.nextLong();
                // CraftBukkit
                if (!this.isEffect && this.worldObj.areChunksLoaded(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ), 10)) {
                    int i = MathHelper.floor(this.posX);
                    int j = MathHelper.floor(this.posY);
                    int k = MathHelper.floor(this.posZ);

                    if (this.worldObj.getTypeId(i, j, k) == 0 && Block.FIRE.canPlace(this.worldObj, i, j, k)) {
                        // CraftBukkit start
                        BlockIgniteEvent event = new BlockIgniteEvent(this.cworld.getBlockAt(i, j, k), BlockIgniteEvent.IgniteCause.LIGHTNING, null);
                        this.worldObj.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            this.worldObj.setBlockWithNotify(i, j, k, Block.FIRE.blockID);
                        }
                        // CraftBukkit end
                    }
                }
            }
        }

        if (this.lifeTicks >= 0 && !this.isEffect) { // CraftBukkit
            double d0 = 3.0D;
            // CraftBukkit start - switch to array copy of list to avoid CMEs
            Object[] array = this.worldObj.getEntities(this, AxisAlignedBB.a().a(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0D + d0, this.posZ + d0)).toArray();
            Iterator iterator = com.google.common.collect.Iterators.forArray(array);
            // CraftBukkit end

            while (iterator.hasNext()) {
                net.minecraft.src.Entity entity = (net.minecraft.src.Entity) iterator.next();

                entity.a(this);
            }

            this.worldObj.s = 2;
        }
    }

    protected void a() {}

    protected void a(net.minecraft.src.NBTTagCompound nbttagcompound) {}

    protected void b(net.minecraft.src.NBTTagCompound nbttagcompound) {}
}
