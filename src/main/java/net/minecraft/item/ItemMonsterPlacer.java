package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
public class ItemMonsterPlacer extends Item
{
    public ItemMonsterPlacer(int par1)
    {
        super(par1);
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    public String i(ItemStack itemstack)
    {
        String s = ("" + StatCollector.translateToLocal(this.getItemName() + ".name")).trim();
        String s1 = EntityList.getStringFromID(itemstack.getItemDamage());

        if (s1 != null)
        {
            s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        if (par3World.isRemote || par1ItemStack.getItemDamage() == 48 || par1ItemStack.getItemDamage() == 49 || par1ItemStack.getItemDamage() == 63 || par1ItemStack.getItemDamage() == 64)   // CraftBukkit
        {
            return true;
        }
        else
        {
            int var11 = par3World.getBlockId(par4, par5, par6);
            par4 += Facing.offsetsXForSide[par7];
            par5 += Facing.offsetsYForSide[par7];
            par6 += Facing.offsetsZForSide[par7];
            double var12 = 0.0D;

            if (par7 == 1 && Block.blocksList[var11] != null && Block.blocksList[var11].getRenderType() == 11)
            {
                var12 = 0.5D;
            }

            if (spawnCreature(par3World, par1ItemStack.getItemDamage(), (double)par4 + 0.5D, (double)par5 + var12, (double)par6 + 0.5D) != null && !par2EntityPlayer.capabilities.isCreativeMode)
            {
                --par1ItemStack.stackSize;
            }

            return true;
        }
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
     * Parameters: world, entityID, x, y, z.
     */
    public static Entity spawnCreature(World par0World, int par1, double par2, double par4, double par6)
    {
        if (!EntityList.entityEggs.containsKey(Integer.valueOf(par1)))
        {
            return null;
        }
        else
        {
            Entity var8 = null;

            for (int var9 = 0; var9 < 1; ++var9)
            {
                var8 = EntityList.createEntityByID(par1, par0World);

                if (var8 != null && var8 instanceof EntityLiving)
                {
                    EntityLiving var10 = (EntityLiving)var8;
                    var8.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
                    var10.rotationYawHead = var10.rotationYaw;
                    var10.renderYawOffset = var10.rotationYaw;
                    var10.initCreature();
                    par0World.addEntity(var8, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG); // CraftBukkit
                    var10.playLivingSound();
                }
            }

            return var8;
        }
    }
}
