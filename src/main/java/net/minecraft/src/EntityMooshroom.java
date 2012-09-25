package net.minecraft.src;

import org.bukkit.event.player.PlayerShearEntityEvent; // CraftBukkit

public class EntityMooshroom extends net.minecraft.src.EntityCow {

    public EntityMooshroom(net.minecraft.src.World world) {
        super(world);
        this.texture = "/mob/redcow.png";
        this.setSize(0.9F, 1.3F);
    }

    public boolean c(net.minecraft.src.EntityPlayer entityhuman) {
        net.minecraft.src.ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.BOWL.id && this.getGrowingAge() >= 0) {
            if (itemstack.count == 1) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, new net.minecraft.src.ItemStack(Item.MUSHROOM_SOUP));
                return true;
            }

            if (entityhuman.inventory.pickup(new net.minecraft.src.ItemStack(Item.MUSHROOM_SOUP)) && !entityhuman.capabilities.canInstantlyBuild) {
                entityhuman.inventory.splitStack(entityhuman.inventory.itemInHandIndex, 1);
                return true;
            }
        }

        if (itemstack != null && itemstack.id == Item.SHEARS.id && this.getGrowingAge() >= 0) {
            // CraftBukkit start
            PlayerShearEntityEvent event = new PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity());
            this.worldObj.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return false;
            }
            // CraftBukkit end

            this.setDead();
            this.worldObj.a("largeexplode", this.posX, this.posY + (double) (this.length / 2.0F), this.posZ, 0.0D, 0.0D, 0.0D);
            if (!this.worldObj.isStatic) {
                net.minecraft.src.EntityCow entitycow = new net.minecraft.src.EntityCow(this.worldObj);

                entitycow.setPositionRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
                entitycow.setHealth(this.getHealth());
                entitycow.aq = this.aq;
                this.worldObj.addEntity(entitycow);

                for (int i = 0; i < 5; ++i) {
                    this.worldObj.addEntity(new net.minecraft.server.EntityItem(this.worldObj, this.posX, this.posY + (double) this.length, this.posZ, new net.minecraft.src.ItemStack(Block.RED_MUSHROOM)));
                }
            }

            return true;
        } else {
            return super.c(entityhuman);
        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityMooshroom(this.worldObj);
    }
}
