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

		if (itemstack != null && itemstack.id == Item.BOWL.id
				&& this.getAge() >= 0) {
			if (itemstack.count == 1) {
				entityhuman.inventory.setItem(
						entityhuman.inventory.itemInHandIndex, new ItemStack(
								Item.MUSHROOM_SOUP));
				return true;
			}

			if (entityhuman.inventory.pickup(new ItemStack(Item.MUSHROOM_SOUP))
					&& !entityhuman.abilities.canInstantlyBuild) {
				entityhuman.inventory.splitStack(
						entityhuman.inventory.itemInHandIndex, 1);
				return true;
			}
		}
		return super.b(entityhuman);
	}

	public EntityAnimal createChild(EntityAnimal entityanimal) {
		return new EntityMushroomCow(this.world);
	}

	@Override
	public boolean isShearable(ItemStack item, World world, int x, int y, int z) {
		return getAge()>=0;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, World world, int x,
			int y, int z, int fortune) {
		die();
        this.die();
        this.world.a("largeexplode", this.locX, this.locY + (double) (this.length / 2.0F), this.locZ, 0.0D, 0.0D, 0.0D);
        EntityCow entitycow = new EntityCow(this.world);
        entitycow.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        entitycow.setHealth(this.getHealth());
        entitycow.V = this.V;
        this.world.addEntity(entitycow);
        ArrayList<ItemStack> ret=new ArrayList<ItemStack>();
        for (int i = 0; i < 5; ++i) {
        	ret.add(new ItemStack(Block.RED_MUSHROOM));
		}
        return ret;
	}
}
