package net.minecraft.server;

import java.util.ArrayList;

import forge.IShearable;

public class ItemShears extends Item {

    public ItemShears(int i) {
        super(i);
        this.e(1);
        this.setMaxDurability(238);
    }

    public boolean a(ItemStack itemstack, int i, int j, int k, int l, EntityLiving entityliving) {
        if (i != Block.LEAVES.id && i != Block.WEB.id && i != Block.LONG_GRASS.id && i != Block.VINE.id && !(Block.byId[i] instanceof IShearable)) {
            return super.a(itemstack, i, j, k, l, entityliving);
        } else {
            return true;
        }
    }

    public boolean canDestroySpecialBlock(Block block) {
        return block.id == Block.WEB.id;
    }

    public float getDestroySpeed(ItemStack itemstack, Block block) {
        return block.id != Block.WEB.id && block.id != Block.LEAVES.id ? (block.id == Block.WOOL.id ? 5.0F : super.getDestroySpeed(itemstack, block)) : 15.0F;
    }
    @Override
    public void a(ItemStack itemstack, EntityLiving entity)
    {
        if (entity.world.isStatic)
        {
            return;
        }
        if (entity instanceof IShearable)
        {
            IShearable target = (IShearable)entity;
            if (target.isShearable(itemstack, entity.world, (int)entity.locX, (int)entity.locY, (int)entity.locZ))
            {
                ArrayList<ItemStack> drops = target.onSheared(itemstack, entity.world, (int)entity.locX, (int)entity.locY, (int)entity.locZ,
                        EnchantmentManager.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS.id, itemstack));
                for(ItemStack stack : drops)
                {
                    EntityItem ent = entity.a(stack, 1.0F);
                    ent.motY += entity.random.nextFloat() * 0.05F;
                    ent.motX += (entity.random.nextFloat() - entity.random.nextFloat()) * 0.1F;
                    ent.motZ += (entity.random.nextFloat() - entity.random.nextFloat()) * 0.1F;
                }
                itemstack.damage(1, entity);
            }
        }
    }
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int X, int Y, int Z, EntityHuman player) 
    {
        if (player.world.isStatic)
        {
            return false;
        }
        int id = player.world.getTypeId(X, Y, Z);
        if (Block.byId[id] != null && Block.byId[id] instanceof IShearable)
        {
            IShearable target = (IShearable)Block.byId[id];
            if (target.isShearable(itemstack, player.world, X, Y, Z))
            {
                ArrayList<ItemStack> drops = target.onSheared(itemstack, player.world, X, Y, Z,
                        EnchantmentManager.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS.id, itemstack));
                for(ItemStack stack : drops)
                {
                    float f = 0.7F;
                    double d  = (double)(player.random.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(player.random.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(player.random.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.world, (double)X + d, (double)Y + d1, (double)Z + d2, stack);
                    entityitem.pickupDelay = 10;
                    player.world.addEntity(entityitem);
                }
                itemstack.damage(1, player);
                player.a(StatisticList.C[id], 1);
            }
        }
        return false;
    }
}
