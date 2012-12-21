package net.minecraft.server;

import org.bukkit.event.player.PlayerShearEntityEvent; // CraftBukkit
import java.util.ArrayList;
import net.minecraftforge.common.IShearable;

public class EntityMushroomCow extends EntityCow implements IShearable { // Forge

	EntityHuman shearer = null;
	
    public EntityMushroomCow(World world) {
        super(world);
        this.texture = "/mob/redcow.png";
        this.a(0.9F, 1.3F);
    }

    public boolean a(EntityHuman entityhuman) {
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

        shearer = entityhuman; // for bukkit down there
        return super.a(entityhuman); // Forge

    }

    public EntityMushroomCow c(EntityAgeable entityageable) {
        return new EntityMushroomCow(this.world);
    }
    
    @Override
    public boolean isShearable(ItemStack var1, World var2, int var3, int var4, int var5)
    {
        return this.getAge() >= 0;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, World world, int X, int Y, int Z, int fortune) 
    {
    	 ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
    	
        // CraftBukkit start
        PlayerShearEntityEvent event = new PlayerShearEntityEvent((org.bukkit.entity.Player) shearer.getBukkitEntity(), this.getBukkitEntity());
        this.world.getServer().getPluginManager().callEvent(event);
        shearer = null;

        if (event.isCancelled()) {
            return ret;
        }
        // CraftBukkit end
        // Forge start
        this.die();
        EntityCow entitycow = new EntityCow(this.world);
        entitycow.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        entitycow.setHealth(this.getHealth());
        entitycow.aw = this.aw;
        this.world.addEntity(entitycow);
        this.world.addParticle("largeexplode", this.locX, this.locY + (double)(this.length / 2.0F), this.locZ, 0.0D, 0.0D, 0.0D);
        
        for (int var9 = 0; var9 < 5; ++var9)
        {
            ret.add(new ItemStack(Block.RED_MUSHROOM));
        }

        return ret;
        // Forge end
    }

    public EntityCow b(EntityAgeable entityageable) {
        return this.c(entityageable);
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.c(entityageable);
    }
}
