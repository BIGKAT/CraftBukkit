package net.minecraft.src;

import net.minecraft.server.Block;
import net.minecraft.server.BlockMonsterEggs;
import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityDamageSource;
import net.minecraft.server.EnumMonsterType;
import net.minecraft.server.Facing;
import net.minecraft.server.MathHelper;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntitySilverfish extends EntityMob {

    private int d;

    public EntitySilverfish(net.minecraft.src.World world) {
        super(world);
        this.texture = "/mob/silverfish.png";
        this.setSize(0.3F, 0.7F);
        this.moveSpeed = 0.6F;
        this.damage = 1;
    }

    public int getMaxHealth() {
        return 8;
    }

    protected boolean e_() {
        return false;
    }

    protected Entity findTarget() {
        double d0 = 8.0D;

        return this.worldObj.findNearbyVulnerablePlayer(this, d0);
    }

    protected String aQ() {
        return "mob.silverfish.say";
    }

    protected String aR() {
        return "mob.silverfish.hit";
    }

    protected String aS() {
        return "mob.silverfish.kill";
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.d <= 0 && (damagesource instanceof EntityDamageSource || damagesource == DamageSource.MAGIC)) {
            this.d = 20;
        }

        return super.damageEntity(damagesource, i);
    }

    protected void a(Entity entity, float f) {
        if (this.attackTicks <= 0 && f < 1.2F && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
            this.attackTicks = 20;
            entity.damageEntity(DamageSource.mobAttack(this), this.damage);
        }
    }

    protected void a(int i, int j, int k, int l) {
        this.worldObj.makeSound(this, "mob.silverfish.step", 1.0F, 1.0F);
    }

    protected int getLootId() {
        return 0;
    }

    public void h_() {
        this.aq = this.rotationYaw;
        super.h_();
    }

    protected void be() {
        super.be();
        if (!this.worldObj.isStatic) {
            int i;
            int j;
            int k;
            int l;

            if (this.d > 0) {
                --this.d;
                if (this.d == 0) {
                    i = MathHelper.floor(this.posX);
                    j = MathHelper.floor(this.posY);
                    k = MathHelper.floor(this.posZ);
                    boolean flag = false;

                    for (l = 0; !flag && l <= 5 && l >= -5; l = l <= 0 ? 1 - l : 0 - l) {
                        for (int i1 = 0; !flag && i1 <= 10 && i1 >= -10; i1 = i1 <= 0 ? 1 - i1 : 0 - i1) {
                            for (int j1 = 0; !flag && j1 <= 10 && j1 >= -10; j1 = j1 <= 0 ? 1 - j1 : 0 - j1) {
                                int k1 = this.worldObj.getBlockId(i + i1, j + l, k + j1);

                                if (k1 == Block.MONSTER_EGGS.blockID) {
                                    // CraftBukkit start
                                    if (CraftEventFactory.callEntityChangeBlockEvent(this, i + i1, j + l, k + j1, 0).isCancelled()) {
                                        continue;
                                    }
                                    // CraftBukkit end

                                    this.worldObj.triggerEffect(2001, i + i1, j + l, k + j1, Block.MONSTER_EGGS.blockID + (this.worldObj.getData(i + i1, j + l, k + j1) << 12));
                                    this.worldObj.setBlockWithNotify(i + i1, j + l, k + j1, 0);
                                    Block.MONSTER_EGGS.postBreak(this.worldObj, i + i1, j + l, k + j1, 0);
                                    if (this.random.nextBoolean()) {
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (this.entityToAttack == null && !this.l()) {
                i = MathHelper.floor(this.posX);
                j = MathHelper.floor(this.posY + 0.5D);
                k = MathHelper.floor(this.posZ);
                int l1 = this.random.nextInt(6);

                l = this.worldObj.getBlockId(i + Facing.b[l1], j + Facing.c[l1], k + Facing.d[l1]);
                if (BlockMonsterEggs.e(l)) {
                    // CraftBukkit start
                    if (CraftEventFactory.callEntityChangeBlockEvent(this, i + Facing.b[l1], j + Facing.c[l1], k + Facing.d[l1], Block.MONSTER_EGGS.blockID).isCancelled()) {
                        return;
                    }
                    // CraftBukkit end

                    this.worldObj.setBlockAndMetadataWithNotify(i + Facing.b[l1], j + Facing.c[l1], k + Facing.d[l1], Block.MONSTER_EGGS.blockID, BlockMonsterEggs.f(l));
                    this.aK();
                    this.setDead();
                } else {
                    this.j();
                }
            } else if (this.entityToAttack != null && !this.l()) {
                this.entityToAttack = null;
            }
        }
    }

    public float a(int i, int j, int k) {
        return this.worldObj.getBlockId(i, j - 1, k) == Block.STONE.blockID ? 10.0F : super.a(i, j, k);
    }

    protected boolean o() {
        return true;
    }

    public boolean canSpawn() {
        if (super.canSpawn()) {
            EntityPlayer entityhuman = this.worldObj.findNearbyPlayer(this, 5.0D);

            return entityhuman == null;
        } else {
            return false;
        }
    }

    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.ARTHROPOD;
    }
}
