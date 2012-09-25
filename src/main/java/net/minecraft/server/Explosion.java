package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.Location;
// CraftBukkit end

public class Explosion {

    public boolean a = false;
    private int h = 16;
    private Random i = new Random();
    private World world;
    public double posX;
    public double posY;
    public double posZ;
    public Entity source;
    public float size;
    public List blocks = new ArrayList();
    private Map k = new HashMap();
    public boolean wasCanceled = false; // CraftBukkit

    public Explosion(World world, Entity entity, double d0, double d1, double d2, float f) {
        this.world = world;
        this.source = entity;
        this.size = (float) Math.max(f, 0.0); // CraftBukkit - clamp bad values
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
    }

    public void a() {
        // CraftBukkit start
        if (this.size < 0.1F) {
            return;
        }
        // CraftBukkit end

        float f = this.size;
        HashSet hashset = new HashSet();

        int i;
        int j;
        int k;
        double d0;
        double d1;
        double d2;

        for (i = 0; i < this.h; ++i) {
            for (j = 0; j < this.h; ++j) {
                for (k = 0; k < this.h; ++k) {
                    if (i == 0 || i == this.h - 1 || j == 0 || j == this.h - 1 || k == 0 || k == this.h - 1) {
                        double d3 = (double) ((float) i / ((float) this.h - 1.0F) * 2.0F - 1.0F);
                        double d4 = (double) ((float) j / ((float) this.h - 1.0F) * 2.0F - 1.0F);
                        double d5 = (double) ((float) k / ((float) this.h - 1.0F) * 2.0F - 1.0F);
                        double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                        d3 /= d6;
                        d4 /= d6;
                        d5 /= d6;
                        float f1 = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);

                        d0 = this.posX;
                        d1 = this.posY;
                        d2 = this.posZ;

                        for (float f2 = 0.3F; f1 > 0.0F; f1 -= f2 * 0.75F) {
                            int l = MathHelper.floor(d0);
                            int i1 = MathHelper.floor(d1);
                            int j1 = MathHelper.floor(d2);
                            int k1 = this.world.getBlockId(l, i1, j1);

                            if (k1 > 0) {
                                f1 -= (Block.blocksList[k1].a(this.source) + 0.3F) * f2;
                            }

                            if (f1 > 0.0F && i1 < 256 && i1 >= 0) { // CraftBukkit - don't wrap explosions
                                hashset.add(new ChunkPosition(l, i1, j1));
                            }

                            d0 += d3 * (double) f2;
                            d1 += d4 * (double) f2;
                            d2 += d5 * (double) f2;
                        }
                    }
                }
            }
        }

        this.blocks.addAll(hashset);
        this.size *= 2.0F;
        i = MathHelper.floor(this.posX - (double) this.size - 1.0D);
        j = MathHelper.floor(this.posX + (double) this.size + 1.0D);
        k = MathHelper.floor(this.posY - (double) this.size - 1.0D);
        int l1 = MathHelper.floor(this.posY + (double) this.size + 1.0D);
        int i2 = MathHelper.floor(this.posZ - (double) this.size - 1.0D);
        int j2 = MathHelper.floor(this.posZ + (double) this.size + 1.0D);
        List list = this.world.getEntities(this.source, AxisAlignedBB.a().a((double) i, (double) k, (double) i2, (double) j, (double) l1, (double) j2));
        Vec3D vec3d = Vec3D.a().create(this.posX, this.posY, this.posZ);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = (Entity) list.get(k2);
            double d7 = entity.f(this.posX, this.posY, this.posZ) / (double) this.size;

            if (d7 <= 1.0D) {
                d0 = entity.posX - this.posX;
                d1 = entity.posY + (double) entity.getHeadHeight() - this.posY;
                d2 = entity.posZ - this.posZ;
                double d8 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                if (d8 != 0.0D) {
                    d0 /= d8;
                    d1 /= d8;
                    d2 /= d8;
                    double d9 = (double) this.world.a(vec3d, entity.boundingBox);
                    double d10 = (1.0D - d7) * d9;

                    // CraftBukkit start - explosion damage hook
                    org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                    int damageDone = (int) ((d10 * d10 + d10) / 2.0D * 8.0D * (double) this.size + 1.0D);

                    if (damagee == null) {
                        // nothing was hurt
                    } else if (this.source == null) { // Block explosion (without an entity source; bed etc.)
                        EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(null, damagee, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION, damageDone);
                        Bukkit.getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            damagee.setLastDamageCause(event);
                            entity.damageEntity(DamageSource.EXPLOSION, event.getDamage());

                            entity.motionX += d0 * d10;
                            entity.motionY += d1 * d10;
                            entity.motionZ += d2 * d10;
                            if (entity instanceof EntityHuman) {
                                this.k.put((EntityHuman) entity, Vec3D.a().create(d0 * d10, d1 * d10, d2 * d10));
                            }
                        }
                    } else {
                        final org.bukkit.entity.Entity damager = this.source.getBukkitEntity();
                        final EntityDamageEvent.DamageCause damageCause;

                        if (damager instanceof org.bukkit.entity.TNTPrimed) {
                            damageCause = EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
                        } else {
                            damageCause = EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
                        }

                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageCause, damageDone);
                        Bukkit.getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            entity.getBukkitEntity().setLastDamageCause(event);
                            entity.damageEntity(DamageSource.EXPLOSION, event.getDamage());

                            entity.motionX += d0 * d10;
                            entity.motionY += d1 * d10;
                            entity.motionZ += d2 * d10;
                            if (entity instanceof EntityHuman) {
                                this.k.put((EntityHuman) entity, Vec3D.a().create(d0 * d10, d1 * d10, d2 * d10));
                            }
                        }
                    }
                    // CraftBukkit end
                }
            }
        }

        this.size = f;
    }

    public void a(boolean flag) {
        this.world.makeSound(this.posX, this.posY, this.posZ, "random.explode", 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        this.world.a("hugeexplosion", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);

        // CraftBukkit start
        org.bukkit.World bworld = this.world.getWorld();
        org.bukkit.entity.Entity explode = this.source == null ? null : this.source.getBukkitEntity();
        Location location = new Location(bworld, this.posX, this.posY, this.posZ);

        List<org.bukkit.block.Block> blockList = new ArrayList<org.bukkit.block.Block>();
        for (int j = blocks.size() - 1; j >= 0; j--) {
            ChunkPosition cpos = (ChunkPosition) blocks.get(j);
            org.bukkit.block.Block block = bworld.getBlockAt(cpos.x, cpos.y, cpos.z);
            if (block.getType() != org.bukkit.Material.AIR) {
                blockList.add(block);
            }
        }

        EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList, 0.3F);
        this.world.getServer().getPluginManager().callEvent(event);

        blocks.clear();

        for (org.bukkit.block.Block block : event.blockList()) {
            ChunkPosition coords = new ChunkPosition(block.getX(), block.getY(), block.getZ());
            blocks.add(coords);
        }

        if (event.isCancelled()) {
            this.wasCanceled = true;
            return;
        }

        Iterator iterator = this.blocks.iterator();
        // CraftBukkit end

        ChunkPosition chunkposition;
        int i;
        int j;
        int k;
        int l;

        while (iterator.hasNext()) {
            chunkposition = (ChunkPosition) iterator.next();
            i = chunkposition.x;
            j = chunkposition.y;
            k = chunkposition.z;
            l = this.world.getBlockId(i, j, k);
            if (flag) {
                double d0 = (double) ((float) i + this.world.rand.nextFloat());
                double d1 = (double) ((float) j + this.world.rand.nextFloat());
                double d2 = (double) ((float) k + this.world.rand.nextFloat());
                double d3 = d0 - this.posX;
                double d4 = d1 - this.posY;
                double d5 = d2 - this.posZ;
                double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                d3 /= d6;
                d4 /= d6;
                d5 /= d6;
                double d7 = 0.5D / (d6 / (double) this.size + 0.1D);

                d7 *= (double) (this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                d3 *= d7;
                d4 *= d7;
                d5 *= d7;
                this.world.a("explode", (d0 + this.posX * 1.0D) / 2.0D, (d1 + this.posY * 1.0D) / 2.0D, (d2 + this.posZ * 1.0D) / 2.0D, d3, d4, d5);
                this.world.a("smoke", d0, d1, d2, d3, d4, d5);
            }

            // CraftBukkit - stop explosions from putting out fire
            if (l > 0 && l != Block.FIRE.blockID) {
                // CraftBukkit
                Block.blocksList[l].dropBlockAsItemWithChance(this.world, i, j, k, this.world.getData(i, j, k), event.getYield(), 0);
                if (this.world.setRawTypeIdAndData(i, j, k, 0, 0, this.world.isStatic)) {
                    this.world.applyPhysics(i, j, k, 0);
                }

                Block.blocksList[l].onBlockDestroyedByExplosion(this.world, i, j, k);
            }
        }

        if (this.a) {
            iterator = this.blocks.iterator();

            while (iterator.hasNext()) {
                chunkposition = (ChunkPosition) iterator.next();
                i = chunkposition.x;
                j = chunkposition.y;
                k = chunkposition.z;
                l = this.world.getBlockId(i, j, k);
                int i1 = this.world.getBlockId(i, j - 1, k);

                if (l == 0 && Block.n[i1] && this.i.nextInt(3) == 0) {
                    this.world.setBlockWithNotify(i, j, k, Block.FIRE.blockID);
                }
            }
        }
    }

    public Map b() {
        return this.k;
    }
}
