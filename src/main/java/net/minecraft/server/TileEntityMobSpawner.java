package net.minecraft.server;

import java.util.Iterator;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason; // CraftBukkit

public class TileEntityMobSpawner extends TileEntity {

    public int delay = -1;
    public String mobID = "Pig"; // CraftBukkit - private -> public
    private NBTTagCompound spawnData = null;
    public double b;
    public double c = 0.0D;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;

    public TileEntityMobSpawner() {
        this.delay = 20;
    }

    public void a(String s) {
        this.mobID = s;
    }

    public boolean b() {
        return this.world.findNearbyPlayer((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D, 16.0D) != null;
    }

    public void g() {
        if (this.b()) {
            if (this.world.isStatic) {
                double d0 = (double) ((float) this.x + this.world.rand.nextFloat());
                double d1 = (double) ((float) this.y + this.world.rand.nextFloat());
                double d2 = (double) ((float) this.z + this.world.rand.nextFloat());

                this.world.a("smoke", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                this.world.a("flame", d0, d1, d2, 0.0D, 0.0D, 0.0D);
                this.c = this.b % 360.0D;
                this.b += 4.545454502105713D;
            } else {
                if (this.delay == -1) {
                    this.f();
                }

                if (this.delay > 0) {
                    --this.delay;
                    return;
                }

                for (int i = 0; i < this.spawnCount; ++i) {
                    Entity entity = EntityTypes.createEntityByName(this.mobID, this.world);

                    if (entity == null) {
                        return;
                    }

                    int j = this.world.a(entity.getClass(), AxisAlignedBB.a().a((double) this.x, (double) this.y, (double) this.z, (double) (this.x + 1), (double) (this.y + 1), (double) (this.z + 1)).grow(8.0D, 4.0D, 8.0D)).size();

                    if (j >= 6) {
                        this.f();
                        return;
                    }

                    if (entity != null) {
                        double d3 = (double) this.x + (this.world.rand.nextDouble() - this.world.rand.nextDouble()) * 4.0D;
                        double d4 = (double) (this.y + this.world.rand.nextInt(3) - 1);
                        double d5 = (double) this.z + (this.world.rand.nextDouble() - this.world.rand.nextDouble()) * 4.0D;
                        EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;

                        entity.setPositionRotation(d3, d4, d5, this.world.rand.nextFloat() * 360.0F, 0.0F);
                        if (entityliving == null || entityliving.canSpawn()) {
                            this.a(entity);
                            this.world.addEntity(entity, SpawnReason.SPAWNER); // CraftBukkit
                            this.world.triggerEffect(2004, this.x, this.y, this.z, 0);
                            if (entityliving != null) {
                                entityliving.aK();
                            }

                            this.f();
                        }
                    }
                }
            }

            super.g();
        }
    }

    public void a(Entity entity) {
        if (this.spawnData != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            entity.c(nbttagcompound);
            Iterator iterator = this.spawnData.c().iterator();

            while (iterator.hasNext()) {
                NBTBase nbtbase = (NBTBase) iterator.next();

                nbttagcompound.set(nbtbase.getName(), nbtbase.clone());
            }

            entity.e(nbttagcompound);
        }
    }

    private void f() {
        this.delay = this.minSpawnDelay + this.world.rand.nextInt(this.maxSpawnDelay - this.minSpawnDelay);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.mobID = nbttagcompound.getString("EntityId");
        this.delay = nbttagcompound.getShort("Delay");
        if (nbttagcompound.hasKey("SpawnData")) {
            this.spawnData = nbttagcompound.getCompoundTag("SpawnData");
        } else {
            this.spawnData = null;
        }

        if (nbttagcompound.hasKey("MinSpawnDelay")) {
            this.minSpawnDelay = nbttagcompound.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbttagcompound.getShort("MaxSpawnDelay");
            this.spawnCount = nbttagcompound.getShort("SpawnCount");
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setString("EntityId", this.mobID);
        nbttagcompound.setShort("Delay", (short) this.delay);
        nbttagcompound.setShort("MinSpawnDelay", (short) this.minSpawnDelay);
        nbttagcompound.setShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
        nbttagcompound.setShort("SpawnCount", (short) this.spawnCount);
        if (this.spawnData != null) {
            nbttagcompound.setCompoundTag("SpawnData", this.spawnData);
        }
    }

    public Packet e() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.b(nbttagcompound);
        return new Packet132TileEntityData(this.x, this.y, this.z, 1, nbttagcompound);
    }
}
