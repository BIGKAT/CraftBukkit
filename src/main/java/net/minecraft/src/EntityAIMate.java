package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.server.EntityAnimal;
import net.minecraft.server.PathfinderGoal;

public class EntityAIMate extends PathfinderGoal {

    private EntityAnimal d;
    net.minecraft.src.World a;
    private EntityAnimal e;
    int b = 0;
    float c;

    public EntityAIMate(EntityAnimal entityanimal, float f) {
        this.d = entityanimal;
        this.a = entityanimal.worldObj;
        this.c = f;
        this.a(3);
    }

    public boolean a() {
        if (!this.d.s()) {
            return false;
        } else {
            this.e = this.f();
            return this.e != null;
        }
    }

    public boolean b() {
        return this.e.isEntityAlive() && this.e.s() && this.b < 60;
    }

    public void c() {
        this.e = null;
        this.b = 0;
    }

    public void d() {
        this.d.getControllerLook().a(this.e, 10.0F, (float) this.d.bf());
        this.d.getNavigation().a((net.minecraft.src.EntityLiving) this.e, this.c);
        ++this.b;
        if (this.b == 60) {
            this.i();
        }
    }

    private EntityAnimal f() {
        float f = 8.0F;
        List list = this.a.a(this.d.getClass(), this.d.boundingBox.grow((double) f, (double) f, (double) f));
        Iterator iterator = list.iterator();

        EntityAnimal entityanimal;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entityanimal = (EntityAnimal) iterator.next();
        } while (!this.d.mate(entityanimal));

        return entityanimal;
    }

    private void i() {
        EntityAnimal entityanimal = this.d.createChild(this.e);

        if (entityanimal != null) {
            this.d.setGrowingAge(6000);
            this.e.setGrowingAge(6000);
            this.d.t();
            this.e.t();
            entityanimal.setGrowingAge(-24000);
            entityanimal.setPositionRotation(this.d.posX, this.d.posY, this.d.posZ, 0.0F, 0.0F);
            this.a.addEntity(entityanimal, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.BREEDING); // CraftBukkit - added SpawnReason
            Random random = this.d.au();

            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;

                this.a.a("heart", this.d.posX + (double) (random.nextFloat() * this.d.width * 2.0F) - (double) this.d.width, this.d.posY + 0.5D + (double) (random.nextFloat() * this.d.length), this.d.posZ + (double) (random.nextFloat() * this.d.width * 2.0F) - (double) this.d.width, d0, d1, d2);
            }
        }
    }
}
