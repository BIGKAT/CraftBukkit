package net.minecraft.src;

// CraftBukkit start

import net.minecraft.server.*;
import net.minecraft.src.*;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public abstract class EntityCreature extends net.minecraft.src.EntityLiving {

    public PathEntity pathToEntity; // CraftBukkit - public
    public Entity entityToAttack; // CraftBukkit - public
    protected boolean b = false;
    protected int c = 0;

    public EntityCreature(net.minecraft.src.World world) {
        super(world);
    }

    protected boolean i() {
        return false;
    }

    protected void be() {
        // this.world.methodProfiler.a("ai"); // CraftBukkit - not in production code
        if (this.c > 0) {
            --this.c;
        }

        this.b = this.i();
        float f = 16.0F;

        if (this.entityToAttack == null) {
            // CraftBukkit start
            Entity target = this.findTarget();
            if (target != null) {
                EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), target.getBukkitEntity(), EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                this.worldObj.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    if (event.getTarget() == null) {
                        this.entityToAttack = null;
                    } else {
                        this.entityToAttack = ((CraftEntity) event.getTarget()).getHandle();
                    }
                }
            }
            // CraftBukkit end

            if (this.entityToAttack != null) {
                this.pathToEntity = this.worldObj.getPathEntityToEntity(this, this.entityToAttack, f, true, false, false, true);
            }
        } else if (this.entityToAttack.isEntityAlive()) {
            float f1 = this.entityToAttack.d((Entity) this);

            if (this.l(this.entityToAttack)) {
                this.a(this.entityToAttack, f1);
            }
        } else {
            // CraftBukkit start
            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null, EntityTargetEvent.TargetReason.TARGET_DIED);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.entityToAttack = null;
                } else {
                    this.entityToAttack = ((CraftEntity) event.getTarget()).getHandle();
                }
            }
            // CraftBukkit end
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        if (!this.b && this.entityToAttack != null && (this.pathToEntity == null || this.random.nextInt(20) == 0)) {
            this.pathToEntity = this.worldObj.getPathEntityToEntity(this, this.entityToAttack, f, true, false, false, true);
        } else if (!this.b && (this.pathToEntity == null && this.random.nextInt(180) == 0 || this.random.nextInt(120) == 0 || this.c > 0) && this.bq < 100) {
            this.j();
        }

        int i = MathHelper.floor(this.boundingBox.b + 0.5D);
        boolean flag = this.H();
        boolean flag1 = this.J();

        this.rotationPitch = 0.0F;
        if (this.pathToEntity != null && this.random.nextInt(100) != 0) {
            // this.world.methodProfiler.a("followpath"); // CraftBukkit - not in production code
            Vec3 vec3d = this.pathToEntity.a((Entity) this);
            double d0 = (double) (this.width * 2.0F);

            while (vec3d != null && vec3d.d(this.posX, vec3d.b, this.posZ) < d0 * d0) {
                this.pathToEntity.a();
                if (this.pathToEntity.b()) {
                    vec3d = null;
                    this.pathToEntity = null;
                } else {
                    vec3d = this.pathToEntity.a((Entity) this);
                }
            }

            this.bu = false;
            if (vec3d != null) {
                double d1 = vec3d.a - this.posX;
                double d2 = vec3d.c - this.posZ;
                double d3 = vec3d.b - (double) i;
                // CraftBukkit - Math -> TrigMath
                float f2 = (float) (org.bukkit.craftbukkit.TrigMath.atan2(d2, d1) * 180.0D / 3.1415927410125732D) - 90.0F;
                float f3 = MathHelper.g(f2 - this.rotationYaw);

                this.bs = this.bw;
                if (f3 > 30.0F) {
                    f3 = 30.0F;
                }

                if (f3 < -30.0F) {
                    f3 = -30.0F;
                }

                this.rotationYaw += f3;
                if (this.b && this.entityToAttack != null) {
                    double d4 = this.entityToAttack.posX - this.posX;
                    double d5 = this.entityToAttack.posZ - this.posZ;
                    float f4 = this.rotationYaw;

                    this.rotationYaw = (float) (Math.atan2(d5, d4) * 180.0D / 3.1415927410125732D) - 90.0F;
                    f3 = (f4 - this.rotationYaw + 90.0F) * 3.1415927F / 180.0F;
                    this.br = -MathHelper.sin(f3) * this.bs * 1.0F;
                    this.bs = MathHelper.cos(f3) * this.bs * 1.0F;
                }

                if (d3 > 0.0D) {
                    this.bu = true;
                }
            }

            if (this.entityToAttack != null) {
                this.a(this.entityToAttack, 30.0F, 30.0F);
            }

            if (this.positionChanged && !this.l()) {
                this.bu = true;
            }

            if (this.random.nextFloat() < 0.8F && (flag || flag1)) {
                this.bu = true;
            }

            // this.world.methodProfiler.b(); // CraftBukkit - not in production code
        } else {
            super.be();
            this.pathToEntity = null;
        }
    }

    protected void j() {
        // this.world.methodProfiler.a("stroll"); // CraftBukkit - not in production code
        boolean flag = false;
        int i = -1;
        int j = -1;
        int k = -1;
        float f = -99999.0F;

        for (int l = 0; l < 10; ++l) {
            int i1 = MathHelper.floor(this.posX + (double) this.random.nextInt(13) - 6.0D);
            int j1 = MathHelper.floor(this.posY + (double) this.random.nextInt(7) - 3.0D);
            int k1 = MathHelper.floor(this.posZ + (double) this.random.nextInt(13) - 6.0D);
            float f1 = this.a(i1, j1, k1);

            if (f1 > f) {
                f = f1;
                i = i1;
                j = j1;
                k = k1;
                flag = true;
            }
        }

        if (flag) {
            this.pathToEntity = this.worldObj.a(this, i, j, k, 10.0F, true, false, false, true);
        }

        // this.world.methodProfiler.b(); // CraftBukkit - not in production code
    }

    protected void a(Entity entity, float f) {}

    public float a(int i, int j, int k) {
        return 0.0F;
    }

    protected Entity findTarget() {
        return null;
    }

    public boolean canSpawn() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.posZ);

        return super.canSpawn() && this.a(i, j, k) >= 0.0F;
    }

    public boolean l() {
        return this.pathToEntity != null;
    }

    public void setPathEntity(PathEntity pathentity) {
        this.pathToEntity = pathentity;
    }

    public Entity m() {
        return this.entityToAttack;
    }

    public void setTarget(Entity entity) {
        this.entityToAttack = entity;
    }

    protected float bs() {
        if (this.aV()) {
            return 1.0F;
        } else {
            float f = super.bs();

            if (this.c > 0) {
                f *= 2.0F;
            }

            return f;
        }
    }
}
