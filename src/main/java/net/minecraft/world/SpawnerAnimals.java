package net.minecraft.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;

// CraftBukkit start
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.craftbukkit.util.LongObjectHashMap;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public final class SpawnerAnimals
{
    private static LongObjectHashMap<Boolean> eligibleChunksForSpawning = new LongObjectHashMap<Boolean>(); // CraftBukkit - HashMap -> LongObjectHashMap

    /** An array of entity classes that spawn at night. */
    protected static final Class[] nightSpawnEntities = new Class[] {EntitySpider.class, EntityZombie.class, EntitySkeleton.class};

    /**
     * Given a chunk, find a random position in it.
     */
    protected static ChunkPosition getRandomSpawningPointInChunk(World par0World, int par1, int par2)
    {
        Chunk var3 = par0World.getChunkFromChunkCoords(par1, par2);
        int var4 = par1 * 16 + par0World.rand.nextInt(16);
        int var5 = par2 * 16 + par0World.rand.nextInt(16);
        int var6 = par0World.rand.nextInt(var3 == null ? par0World.getActualHeight() : var3.getTopFilledSegment() + 16 - 1);
        return new ChunkPosition(var4, var6, var5);
    }

    /**
     * adds all chunks within the spawn radius of the players to eligibleChunksForSpawning. pars: the world,
     * hostileCreatures, passiveCreatures. returns number of eligible chunks.
     */
    public static final int findChunksForSpawning(WorldServer par0WorldServer, boolean par1, boolean par2, boolean par3)
    {
        if (!par1 && !par2)
        {
            return 0;
        }
        else
        {
            eligibleChunksForSpawning.clear();
            int var4;
            int var7;

            for (var4 = 0; var4 < par0WorldServer.playerEntities.size(); ++var4)
            {
                EntityPlayer var5 = (EntityPlayer)par0WorldServer.playerEntities.get(var4);
                int var6 = MathHelper.floor_double(var5.posX / 16.0D);
                var7 = MathHelper.floor_double(var5.posZ / 16.0D);
                byte var8 = 8;

                for (int var9 = -var8; var9 <= var8; ++var9)
                {
                    for (int var10 = -var8; var10 <= var8; ++var10)
                    {
                        boolean var11 = var9 == -var8 || var9 == var8 || var10 == -var8 || var10 == var8;
                        // CraftBukkit start
                        long chunkCoords = LongHash.toLong(var9 + var6, var10 + var7);

                        if (!var11)
                        {
                            eligibleChunksForSpawning.put(chunkCoords, false);
                        }
                        else if (!eligibleChunksForSpawning.containsKey(chunkCoords))
                        {
                            eligibleChunksForSpawning.put(chunkCoords, true);
                        }

                        // CraftBukkit end
                    }
                }
            }

            var4 = 0;
            ChunkCoordinates var12 = par0WorldServer.getSpawnPoint();
            EnumCreatureType[] var32 = EnumCreatureType.values();
            var7 = var32.length;

            for (int var33 = 0; var33 < var7; ++var33)
            {
                EnumCreatureType var34 = var32[var33];
                // CraftBukkit start - use per-world spawn limits
                int limit = var34.getMaxNumberOfCreature();

                switch (var34)
                {
                    case monster:
                        limit = par0WorldServer.getWorld().getMonsterSpawnLimit();
                        break;

                    case creature:
                        limit = par0WorldServer.getWorld().getAnimalSpawnLimit();
                        break;

                    case waterCreature:
                        limit = par0WorldServer.getWorld().getWaterAnimalSpawnLimit();
                        break;

                    case ambient:
                        limit = par0WorldServer.getWorld().getAmbientSpawnLimit();
                        break;
                }

                if (limit == 0)
                {
                    continue;
                }

                // CraftBukkit end

                if ((!var34.getPeacefulCreature() || par2) && (var34.getPeacefulCreature() || par1) && (!var34.getAnimal() || par3) && par0WorldServer.countEntities(var34.getCreatureClass()) <= limit * eligibleChunksForSpawning.size() / 256)   // CraftBukkit - use per-world limits
                {
                    Iterator var35 = eligibleChunksForSpawning.keySet().iterator();
                    label110:

                    while (var35.hasNext())
                    {
                        // CraftBukkit start
                        long key = ((Long) var35.next()).longValue();

                        if (!eligibleChunksForSpawning.get(key))
                        {
                            ChunkPosition chunkposition = getRandomSpawningPointInChunk(par0WorldServer, LongHash.msw(key), LongHash.lsw(key));
                            // CraftBukkit end
                            int var37 = chunkposition.x;
                            int var36 = chunkposition.y;
                            int var38 = chunkposition.z;

                            if (!par0WorldServer.isBlockNormalCube(var37, var36, var38) && par0WorldServer.getBlockMaterial(var37, var36, var38) == var34.getCreatureMaterial())
                            {
                                int var13 = 0;
                                int var14 = 0;

                                while (var14 < 3)
                                {
                                    int var15 = var37;
                                    int var16 = var36;
                                    int var17 = var38;
                                    byte var18 = 6;
                                    SpawnListEntry var19 = null;
                                    int var20 = 0;

                                    while (true)
                                    {
                                        if (var20 < 4)
                                        {
                                            label103:
                                            {
                                                var15 += par0WorldServer.rand.nextInt(var18) - par0WorldServer.rand.nextInt(var18);
                                                var16 += par0WorldServer.rand.nextInt(1) - par0WorldServer.rand.nextInt(1);
                                                var17 += par0WorldServer.rand.nextInt(var18) - par0WorldServer.rand.nextInt(var18);

                                                if (canCreatureTypeSpawnAtLocation(var34, par0WorldServer, var15, var16, var17))
                                                {
                                                    float var21 = (float) var15 + 0.5F;
                                                    float var22 = (float) var16;
                                                    float var23 = (float) var17 + 0.5F;

                                                    if (par0WorldServer.getClosestPlayer((double) var21, (double) var22, (double) var23, 24.0D) == null)
                                                    {
                                                        float var24 = var21 - (float) var12.posX;
                                                        float var25 = var22 - (float) var12.posY;
                                                        float var26 = var23 - (float) var12.posZ;
                                                        float var27 = var24 * var24 + var25 * var25 + var26 * var26;

                                                        if (var27 >= 576.0F)
                                                        {
                                                            if (var19 == null)
                                                            {
                                                                var19 = par0WorldServer.spawnRandomCreature(var34, var15, var16, var17);

                                                                if (var19 == null)
                                                                {
                                                                    break label103;
                                                                }
                                                            }

                                                            EntityLiving var28;

                                                            try
                                                            {
                                                                var28 = (EntityLiving) var19.entityClass.getConstructor(new Class[] { World.class}).newInstance(new Object[] { par0WorldServer});
                                                            }
                                                            catch (Exception var29)
                                                            {
                                                                var29.printStackTrace();
                                                                return var4;
                                                            }

                                                            var28.setLocationAndAngles((double) var21, (double) var22, (double) var23, par0WorldServer.rand.nextFloat() * 360.0F, 0.0F);

                                                            if (var28.getCanSpawnHere())
                                                            {
                                                                ++var13;
                                                                // CraftBukkit start - added a reason for spawning this creature, moved a(entityliving, world...) up
                                                                creatureSpecificInit(var28, par0WorldServer, var21, var22, var23);
                                                                par0WorldServer.addEntity(var28, SpawnReason.NATURAL);

                                                                // CraftBukkit end
                                                                if (var13 >= var28.getMaxSpawnedInChunk())
                                                                {
                                                                    continue label110;
                                                                }
                                                            }

                                                            var4 += var13;
                                                        }
                                                    }
                                                }

                                                ++var20;
                                                continue;
                                            }
                                        }

                                        ++var14;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return var4;
        }
    }

    /**
     * Returns whether or not the specified creature type can spawn at the specified location.
     */
    public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType par0EnumCreatureType, World par1World, int par2, int par3, int par4)
    {
        if (par0EnumCreatureType.getCreatureMaterial() == Material.water)
        {
            return par1World.getBlockMaterial(par2, par3, par4).isLiquid() && par1World.getBlockMaterial(par2, par3 - 1, par4).isLiquid() && !par1World.isBlockNormalCube(par2, par3 + 1, par4);
        }
        else if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4))
        {
            return false;
        }
        else
        {
            int var5 = par1World.getBlockId(par2, par3 - 1, par4);
            return var5 != Block.bedrock.blockID && !par1World.isBlockNormalCube(par2, par3, par4) && !par1World.getBlockMaterial(par2, par3, par4).isLiquid() && !par1World.isBlockNormalCube(par2, par3 + 1, par4);
        }
    }

    /**
     * determines if a skeleton spawns on a spider, and if a sheep is a different color
     */
    private static void creatureSpecificInit(EntityLiving par0EntityLiving, World par1World, float par2, float par3, float par4)
    {
        par0EntityLiving.initCreature();
    }

    /**
     * Called during chunk generation to spawn initial creatures.
     */
    public static void performWorldGenSpawning(World par0World, BiomeGenBase par1BiomeGenBase, int par2, int par3, int par4, int par5, Random par6Random)
    {
        List var7 = par1BiomeGenBase.getSpawnableList(EnumCreatureType.creature);

        if (!var7.isEmpty())
        {
            while (par6Random.nextFloat() < par1BiomeGenBase.getSpawningChance())
            {
                SpawnListEntry var8 = (SpawnListEntry) WeightedRandom.getRandomItem(par0World.rand, (Collection) var7);
                int var9 = var8.minGroupCount + par6Random.nextInt(1 + var8.maxGroupCount - var8.minGroupCount);
                int var10 = par2 + par6Random.nextInt(par4);
                int var11 = par3 + par6Random.nextInt(par5);
                int var12 = var10;
                int var13 = var11;

                for (int var14 = 0; var14 < var9; ++var14)
                {
                    boolean var15 = false;

                    for (int var16 = 0; !var15 && var16 < 4; ++var16)
                    {
                        int var17 = par0World.getTopSolidOrLiquidBlock(var10, var11);

                        if (canCreatureTypeSpawnAtLocation(EnumCreatureType.creature, par0World, var10, var17, var11))
                        {
                            float var18 = (float)var10 + 0.5F;
                            float var19 = (float)var17;
                            float var20 = (float)var11 + 0.5F;
                            EntityLiving var21;

                            try
                            {
                                var21 = (EntityLiving)var8.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {par0World});
                            }
                            catch (Exception var23)
                            {
                                var23.printStackTrace();
                                continue;
                            }

                            var21.setLocationAndAngles((double)var18, (double)var19, (double)var20, par6Random.nextFloat() * 360.0F, 0.0F);
                            // CraftBukkit start - added a reason for spawning this creature, moved a(entity, world...) up
                            creatureSpecificInit(var21, par0World, var18, var19, var20);
                            par0World.addEntity(var21, SpawnReason.CHUNK_GEN);
                            // CraftBukkit end
                            var15 = true;
                        }

                        var10 += par6Random.nextInt(5) - par6Random.nextInt(5);

                        for (var11 += par6Random.nextInt(5) - par6Random.nextInt(5); var10 < par2 || var10 >= par2 + par4 || var11 < par3 || var11 >= par3 + par4; var11 = var13 + par6Random.nextInt(5) - par6Random.nextInt(5))
                        {
                            var10 = var12 + par6Random.nextInt(5) - par6Random.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}
