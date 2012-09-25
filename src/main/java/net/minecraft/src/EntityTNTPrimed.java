package net.minecraft.src;

import net.minecraft.src.Entity;

import org.bukkit.event.entity.ExplosionPrimeEvent; // CraftBukkit

public class EntityTNTPrimed extends Entity {

    public int fuse;
    public float yield = 4; // CraftBukkit
    public boolean isIncendiary = false; // CraftBukkit

    public EntityTNTPrimed(net.minecraft.src.World world) {
        super(world);
        this.fuse = 0;
        this.m = true;
        this.a(0.98F, 0.98F);
        this.height = this.length / 2.0F;
    }

    public EntityTNTPrimed(net.minecraft.src.World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1, d2);
        float f = (float) (Math.random() * 3.1415927410125732D * 2.0D);

        this.motionX = (double) (-((float) Math.sin((double) f)) * 0.02F);
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double) (-((float) Math.cos((double) f)) * 0.02F);
        this.fuse = 80;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    protected void a() {}

    protected boolean e_() {
        return false;
    }

    public boolean L() {
        return !this.isDead;
    }

    public void h_() {
        this.lastX = this.posX;
        this.lastY = this.posY;
        this.lastZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;
        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
            this.motionY *= -0.5D;
        }

        if (this.fuse-- <= 0) {
            // CraftBukkit start - Need to reverse the order of the explosion and the entity death so we have a location for the event
            if (!this.worldObj.isStatic) {
                this.explode();
            }
            this.setDead();
            // CraftBukkit end
        } else {
            this.worldObj.a("smoke", this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        // CraftBukkit start
        // float f = 4.0F;

        org.bukkit.craftbukkit.CraftServer server = this.worldObj.getServer();

        ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(server, this));
        server.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            // give 'this' instead of (Entity) null so we know what causes the damage
            this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, event.getRadius(), event.getFire());
        }
        // CraftBukkit end
    }

    protected void b(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Fuse", (byte) this.fuse);
    }

    protected void a(net.minecraft.src.NBTTagCompound nbttagcompound) {
        this.fuse = nbttagcompound.getByte("Fuse");
    }
}
