package net.minecraft.src;

import net.minecraft.server.MathHelper;
import net.minecraft.server.PathEntity;
import net.minecraft.server.PathfinderGoal;

import org.bukkit.event.entity.EntityTargetEvent; // CraftBukkit

public class EntityAIAttackOnCollide extends PathfinderGoal {

    net.minecraft.src.World a;
    net.minecraft.src.EntityLiving b;
    net.minecraft.src.EntityLiving c;
    int d;
    float e;
    boolean f;
    PathEntity g;
    Class h;
    private int i;

    public EntityAIAttackOnCollide(net.minecraft.src.EntityLiving entityliving, Class oclass, float f, boolean flag) {
        this(entityliving, f, flag);
        this.h = oclass;
    }

    public EntityAIAttackOnCollide(net.minecraft.src.EntityLiving entityliving, float f, boolean flag) {
        this.d = 0;
        this.b = entityliving;
        this.a = entityliving.worldObj;
        this.e = f;
        this.f = flag;
        this.a(3);
    }

    public boolean a() {
        net.minecraft.src.EntityLiving entityliving = this.b.az();

        if (entityliving == null) {
            return false;
        } else if (this.h != null && !this.h.isAssignableFrom(entityliving.getClass())) {
            return false;
        } else {
            this.c = entityliving;
            this.g = this.b.getNavigation().a(this.c);
            return this.g != null;
        }
    }

    public boolean b() {
        net.minecraft.src.EntityLiving entityliving = this.b.az();

        return entityliving == null ? false : (!this.c.isEntityAlive() ? false : (!this.f ? !this.b.getNavigation().f() : this.b.d(MathHelper.floor(this.c.posX), MathHelper.floor(this.c.posY), MathHelper.floor(this.c.posZ))));
    }

    public void e() {
        this.b.getNavigation().a(this.g, this.e);
        this.i = 0;
    }

    public void c() {
        // CraftBukkit start
        EntityTargetEvent.TargetReason reason = this.c.isEntityAlive() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTargetEvent(b, null, reason);
        // CraftBukkit end

        this.c = null;
        this.b.getNavigation().g();
    }

    public void d() {
        this.b.getControllerLook().a(this.c, 30.0F, 30.0F);
        if ((this.f || this.b.getEntitySenses().canSee(this.c)) && --this.i <= 0) {
            this.i = 4 + this.b.au().nextInt(7);
            this.b.getNavigation().a(this.c, this.e);
        }

        this.d = Math.max(this.d - 1, 0);
        double d0 = (double) (this.b.width * 2.0F * this.b.width * 2.0F);

        if (this.b.e(this.c.posX, this.c.boundingBox.b, this.c.posZ) <= d0) {
            if (this.d <= 0) {
                this.d = 20;
                this.b.k(this.c);
            }
        }
    }
}
