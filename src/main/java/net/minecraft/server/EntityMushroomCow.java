package net.minecraft.server;

import java.util.ArrayList;

import forge.IShearable;

public class EntityMushroomCow extends EntityCow implements IShearable {

    public EntityMushroomCow(World world) {
        super(world);
        this.texture = "/mob/redcow.png";
        this.b(0.9F, 1.3F);
    }

    public boolean b(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (itemstack != null && itemstack.id == Item.BOWL.id && this.getAge() >= 0) {
            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, new ItemStack(Item.MUSHROOM_SOUP));
            return true;
        } else {
            return super.b(entityhuman);
        }
    }

    public EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityMushroomCow(this.world);
    }

	public boolean isShearable(ItemStack item, World world, int x, int y, int z) {
		return getAge()>=0;
	}

	public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x, int y, int z, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        // CraftBukkit start
/*        org.bukkit.event.player.PlayerShearEntityEvent event = new org.bukkit.event.player.PlayerShearEntityEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), this.getBukkitEntity());
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return ret;
        }*/
        // CraftBukkit end

        this.die();
        EntityCow entitycow = new EntityCow(this.world);
        entitycow.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        entitycow.setHealth(this.getHealth());
        entitycow.V = this.V;
        this.world.addEntity(entitycow);
        this.world.a("largeexplode", this.locX, this.locY + (double) (this.length / 2.0F), this.locZ, 0.0D, 0.0D, 0.0D);
		for (int i = 0; i < 5; i++) {
			ret.add(new ItemStack(Block.RED_MUSHROOM));
		}
		return ret;
    }
}
