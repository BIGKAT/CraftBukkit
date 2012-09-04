package net.minecraft.server;

import org.bukkit.event.player.PlayerShearEntityEvent; // CraftBukkit

import java.util.ArrayList;

import net.minecraftforge.common.IShearable;

public class EntityMushroomCow extends EntityCow implements IShearable

    public EntityMushroomCow(World world) {
        super(world);
        this.texture = "/mob/redcow.png";
        this.a(0.9F, 1.3F);
    }

    public boolean c(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.BOWL.id && this.getAge() >= 0) {
            if (itemstack.count == 1) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, new ItemStack(Item.MUSHROOM_SOUP));
                return true;
            }

            if (entityhuman.inventory.pickup(new ItemStack(Item.MUSHROOM_SOUP)) && !entityhuman.abilities.canInstantlyBuild) {
                entityhuman.inventory.splitStack(entityhuman.inventory.itemInHandIndex, 1);
                return true;
            }
        }

//        if (itemstack != null && itemstack.id == Item.SHEARS.id && this.getAge() >= 0) {
//            // CraftBukkit start
//            PlayerShearEntityEvent event = new PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity());
//            this.world.getServer().getPluginManager().callEvent(event);
//
//            if (event.isCancelled()) {
//                return false;
//            }
//            // CraftBukkit end
//
//            this.die();
//            this.world.a("largeexplode", this.locX, this.locY + (double) (this.length / 2.0F), this.locZ, 0.0D, 0.0D, 0.0D);
//            if (!this.world.isStatic) {
//                EntityCow entitycow = new EntityCow(this.world);
//
//                entitycow.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
//                entitycow.setHealth(this.getHealth());
//                entitycow.aq = this.aq;
//                this.world.addEntity(entitycow);
//
//                for (int i = 0; i < 5; ++i) {
//                    this.world.addEntity(new EntityItem(this.world, this.locX, this.locY + (double) this.length, this.locZ, new ItemStack(Block.RED_MUSHROOM)));
//                }
//            }
//
//            return true;
//        } else {
            return super.c(entityhuman);
//        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityMushroomCow(this.world);
    }
    
    @Override
    public boolean isShearable(ItemStack item, World world, int x, int y, int z)
    {
      return getAge() >= 0;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune)
    {
      die();
      die();
      this.world.a("largeexplode", this.locX, this.locY + this.length / 2.0F, this.locZ, 0.0D, 0.0D, 0.0D);
      EntityCow entitycow = new EntityCow(this.world);
      entitycow.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
      entitycow.setHealth(getHealth());
      entitycow.V = this.V;
      this.world.addEntity(entitycow);
      ArrayList ret = new ArrayList();
      for (int i = 0; i < 5; i++) {
        ret.add(new ItemStack(Block.RED_MUSHROOM));
      }
      return ret;
    }
}
