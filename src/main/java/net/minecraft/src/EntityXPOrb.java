package net.minecraft.src;

// CraftBukkit start

import net.minecraft.server.Block;
import net.minecraft.server.DamageSource;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.src.*;

import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityXPOrb extends net.minecraft.src.Entity {

    public int a;
    public int b = 0;
    public int c;
    private int d = 5;
    public int xpValue; // CraftBukkit - private -> public
    private net.minecraft.src.EntityPlayer targetPlayer;
    private int targetTime;

    public EntityXPOrb(net.minecraft.src.World world, double d0, double d1, double d2, int i) {
        super(world);
        this.a(0.5F, 0.5F);
        this.height = this.length / 2.0F;
        this.setPosition(d0, d1, d2);
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.motionY = (double) ((float) (Math.random() * 0.2D) * 2.0F);
        this.motionZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.xpValue = i;
    }

    protected boolean e_() {
        return false;
    }

    public EntityXPOrb(net.minecraft.src.World world) {
        super(world);
        this.a(0.25F, 0.25F);
        this.height = this.length / 2.0F;
    }

    protected void a() {}

    public void h_() {
        super.h_();
        if (this.c > 0) {
            --this.c;
        }

        this.lastX = this.posX;
        this.lastY = this.posY;
        this.lastZ = this.posZ;
        this.motionY -= 0.029999999329447746D;
        if (this.worldObj.getMaterial(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ)) == Material.LAVA) {
            this.motionY = 0.20000000298023224D;
            this.motionX = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            this.motionZ = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            this.worldObj.makeSound(this, "random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }

        this.i(this.posX, (this.boundingBox.b + this.boundingBox.e) / 2.0D, this.posZ);
        double d0 = 8.0D;

        if (this.targetTime < this.a - 20 + this.entityId % 100) {
            if (this.targetPlayer == null || this.targetPlayer.e(this) > d0 * d0) {
                this.targetPlayer = this.worldObj.findNearbyPlayer(this, d0);
            }

            this.targetTime = this.a;
        }

        if (this.targetPlayer != null) {
            // CraftBukkit start
            EntityTargetEvent event = CraftEventFactory.callEntityTargetEvent(this, targetPlayer, EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
            net.minecraft.src.Entity target = event.getTarget() == null ? null : ((org.bukkit.craftbukkit.entity.CraftEntity) event.getTarget()).getHandle();

            if (!event.isCancelled() && target != null) {
                double d1 = (target.posX - this.posX) / d0;
                double d2 = (target.posY + (double) target.getHeadHeight() - this.posY) / d0;
                double d3 = (target.posZ - this.posZ) / d0;
                double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
                double d5 = 1.0D - d4;
                if (d5 > 0.0D) {
                    d5 *= d5;
                    this.motionX += d1 / d4 * d5 * 0.1D;
                    this.motionY += d2 / d4 * d5 * 0.1D;
                    this.motionZ += d3 / d4 * d5 * 0.1D;
                }
                // CraftBukkit end
            }
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        float f = 0.98F;

        if (this.onGround) {
            f = 0.58800006F;
            int i = this.worldObj.getBlockId(MathHelper.floor(this.posX), MathHelper.floor(this.boundingBox.b) - 1, MathHelper.floor(this.posZ));

            if (i > 0) {
                f = Block.blocksList[i].frictionFactor * 0.98F;
            }
        }

        this.motionX *= (double) f;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double) f;
        if (this.onGround) {
            this.motionY *= -0.8999999761581421D;
        }

        ++this.a;
        ++this.b;
        if (this.b >= 6000) {
            this.setDead();
        }
    }

    public boolean I() {
        return this.worldObj.a(this.boundingBox, Material.WATER, this);
    }

    protected void burn(int i) {
        this.damageEntity(DamageSource.FIRE, i);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        this.K();
        this.d -= i;
        if (this.d <= 0) {
            this.setDead();
        }

        return false;
    }

    public void b(net.minecraft.src.NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) ((byte) this.d));
        nbttagcompound.setShort("Age", (short) this.b);
        nbttagcompound.setShort("Value", (short) this.xpValue);
    }

    public void a(net.minecraft.src.NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getShort("Health") & 255;
        this.b = nbttagcompound.getShort("Age");
        this.xpValue = nbttagcompound.getShort("Value");
    }

    public void b_(net.minecraft.src.EntityPlayer entityhuman) {
        if (!this.worldObj.isStatic) {
            if (this.c == 0 && entityhuman.bL == 0) {
                entityhuman.bL = 2;
                this.worldObj.makeSound(this, "random.orb", 0.1F, 0.5F * ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.8F));
                entityhuman.receive(this, 1);
                entityhuman.giveExp(CraftEventFactory.callPlayerExpChangeEvent(entityhuman, this.xpValue).getAmount()); // CraftBukkit - this.value to event.getAmount()
                this.setDead();
            }
        }
    }

    public int d() {
        return this.xpValue;
    }

    public static int getOrbValue(int i) {
        // CraftBukkit start
        if (i > 162670129) return i - 100000;
        if (i > 81335063) return 81335063;
        if (i > 40667527) return 40667527;
        if (i > 20333759) return 20333759;
        if (i > 10166857) return 10166857;
        if (i > 5083423) return 5083423;
        if (i > 2541701) return 2541701;
        if (i > 1270849) return 1270849;
        if (i > 635413) return 635413;
        if (i > 317701) return 317701;
        if (i > 158849) return 158849;
        if (i > 79423) return 79423;
        if (i > 39709) return 39709;
        if (i > 19853) return 19853;
        if (i > 9923) return 9923;
        if (i > 4957) return 4957;
        // CraftBukkit end

        return i >= 2477 ? 2477 : (i >= 1237 ? 1237 : (i >= 617 ? 617 : (i >= 307 ? 307 : (i >= 149 ? 149 : (i >= 73 ? 73 : (i >= 37 ? 37 : (i >= 17 ? 17 : (i >= 7 ? 7 : (i >= 3 ? 3 : 1)))))))));
    }

    public boolean an() {
        return false;
    }
}
