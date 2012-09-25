package net.minecraft.src;

import net.minecraft.server.Chunk;
import net.minecraft.server.DamageSource;
import net.minecraft.server.IMonster;
import net.minecraft.server.Item;
import net.minecraft.server.MathHelper;
import net.minecraft.server.WorldType;

import org.bukkit.event.entity.SlimeSplitEvent; // CraftBukkit

public class EntitySlime extends EntityLiving implements IMonster {

    public float a;
    public float b;
    public float c;
    private int jumpDelay = 0;

    public EntitySlime(net.minecraft.src.World world) {
        super(world);
        this.texture = "/mob/slime.png";
        int i = 1 << this.random.nextInt(3);

        this.yOffset = 0.0F;
        this.jumpDelay = this.random.nextInt(20) + 10;
        this.setSlimeSize(i);
    }

    protected void entityInit() {
        super.entityInit();
        this.datawatcher.a(16, new Byte((byte) 1));
    }

    public void setSlimeSize(int i) {
        this.datawatcher.watch(16, new Byte((byte) i));
        this.setSize(0.6F * (float) i, 0.6F * (float) i);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setHealth(this.getMaxHealth());
        this.aV = i;
    }

    public int getMaxHealth() {
        int i = this.getSlimeSize();

        return i * i;
    }

    public int getSlimeSize() {
        return this.datawatcher.getByte(16);
    }

    public void readEntityFromNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        nbttagcompound.setInteger("Size", this.getSlimeSize() - 1);
    }

    public void writeEntityToNBT(net.minecraft.src.NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        this.setSlimeSize(nbttagcompound.getInteger("Size") + 1);
    }

    protected String i() {
        return "slime";
    }

    protected String o() {
        return "mob.slime";
    }

    public void h_() {
        if (!this.worldObj.isStatic && this.worldObj.difficulty == 0 && this.getSlimeSize() > 0) {
            this.isDead = true;
        }

        this.b += (this.a - this.b) * 0.5F;
        this.c = this.b;
        boolean flag = this.onGround;

        super.h_();
        if (this.onGround && !flag) {
            int i = this.getSlimeSize();

            for (int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * 3.1415927F * 2.0F;
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;

                this.worldObj.a(this.i(), this.posX + (double) f2, this.boundingBox.b, this.posZ + (double) f3, 0.0D, 0.0D, 0.0D);
            }

            if (this.p()) {
                this.worldObj.makeSound(this, this.o(), this.aP(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.a = -0.5F;
        } else if (!this.onGround && flag) {
            this.a = 1.0F;
        }

        this.l();
    }

    protected void be() {
        this.bb();
        EntityPlayer entityhuman = this.worldObj.findNearbyVulnerablePlayer(this, 16.0D); // CraftBukkit TODO: EntityTargetEvent

        if (entityhuman != null) {
            this.a(entityhuman, 10.0F, 20.0F);
        }

        if (this.onGround && this.jumpDelay-- <= 0) {
            this.jumpDelay = this.k();
            if (entityhuman != null) {
                this.jumpDelay /= 3;
            }

            this.bu = true;
            if (this.r()) {
                this.worldObj.makeSound(this, this.o(), this.aP(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            this.br = 1.0F - this.random.nextFloat() * 2.0F;
            this.bs = (float) (1 * this.getSlimeSize());
        } else {
            this.bu = false;
            if (this.onGround) {
                this.br = this.bs = 0.0F;
            }
        }
    }

    protected void l() {
        this.a *= 0.6F;
    }

    protected int k() {
        return this.random.nextInt(20) + 10;
    }

    protected EntitySlime j() {
        return new EntitySlime(this.worldObj);
    }

    public void setDead() {
        int i = this.getSlimeSize();

        if (!this.worldObj.isStatic && i > 1 && this.getHealth() <= 0) {
            int j = 2 + this.random.nextInt(3);

            // CraftBukkit start
            SlimeSplitEvent event = new SlimeSplitEvent((org.bukkit.entity.Slime) this.getBukkitEntity(), j);
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled() && event.getCount() > 0) {
                j = event.getCount();
            } else {
                super.setDead();
                return;
            }
            // CraftBukkit end

            for (int k = 0; k < j; ++k) {
                float f = ((float) (k % 2) - 0.5F) * (float) i / 4.0F;
                float f1 = ((float) (k / 2) - 0.5F) * (float) i / 4.0F;
                EntitySlime entityslime = this.j();

                entityslime.setSlimeSize(i / 2);
                entityslime.setPositionRotation(this.posX + (double) f, this.posY + 0.5D, this.posZ + (double) f1, this.random.nextFloat() * 360.0F, 0.0F);
                this.worldObj.addEntity(entityslime, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SLIME_SPLIT); // CraftBukkit - SpawnReason
            }
        }

        super.setDead();
    }

    public void b_(EntityPlayer entityhuman) {
        if (this.m()) {
            int i = this.getSlimeSize();

            if (this.l(entityhuman) && this.e(entityhuman) < 0.6D * (double) i * 0.6D * (double) i && entityhuman.damageEntity(DamageSource.mobAttack(this), this.n())) {
                this.worldObj.makeSound(this, "mob.slimeattack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    protected boolean m() {
        return this.getSlimeSize() > 1;
    }

    protected int n() {
        return this.getSlimeSize();
    }

    protected String aR() {
        return "mob.slime";
    }

    protected String aS() {
        return "mob.slime";
    }

    protected int getLootId() {
        return this.getSlimeSize() == 1 ? Item.SLIME_BALL.id : 0;
    }

    public boolean canSpawn() {
        Chunk chunk = this.worldObj.getChunkAtWorldCoords(MathHelper.floor(this.posX), MathHelper.floor(this.posZ));

        return this.worldObj.getWorldData().getType() == WorldType.FLAT && this.random.nextInt(4) != 1 ? false : ((this.getSlimeSize() == 1 || this.worldObj.difficulty > 0) && this.random.nextInt(10) == 0 && chunk.a(987234911L).nextInt(10) == 0 && this.posY < 40.0D ? super.canSpawn() : false);
    }

    protected float aP() {
        return 0.4F * (float) this.getSlimeSize();
    }

    public int bf() {
        return 0;
    }

    protected boolean r() {
        return this.getSlimeSize() > 1;
    }

    protected boolean p() {
        return this.getSlimeSize() > 2;
    }
}
