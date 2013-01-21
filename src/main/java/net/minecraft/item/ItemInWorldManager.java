package net.minecraft.item;

// CraftBukkit start
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet53BlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
// CraftBukkit end

public class ItemInWorldManager
{
    /** The world object that this object is connected to. */
    public World theWorld;

    /** The EntityPlayerMP object that this object is connected to. */
    public EntityPlayerMP thisPlayerMP;
    private EnumGameType gameType;

    /** True if the player is destroying a block */
    private boolean isDestroyingBlock;
    private int initialDamage;
    private int partiallyDestroyedBlockX;
    private int partiallyDestroyedBlockY;
    private int partiallyDestroyedBlockZ;
    private int curblockDamage;

    /**
     * Set to true when the "finished destroying block" packet is received but the block wasn't fully damaged yet. The
     * block will not be destroyed while this is false.
     */
    private boolean receivedFinishDiggingPacket;
    private int posX;
    private int posY;
    private int posZ;
    private int field_73093_n;
    private int durabilityRemainingOnBlock;

    public ItemInWorldManager(World par1World)
    {
        this.gameType = EnumGameType.NOT_SET;
        this.durabilityRemainingOnBlock = -1;
        this.theWorld = par1World;
    }

    // CraftBukkit start - keep this for backwards compatibility
    public ItemInWorldManager(WorldServer world)
    {
        this((World) world);
    }
    // CraftBukkit end

    public void setGameType(EnumGameType par1EnumGameType)
    {
        this.gameType = par1EnumGameType;
        par1EnumGameType.configurePlayerCapabilities(this.thisPlayerMP.capabilities);
        this.thisPlayerMP.sendPlayerAbilities();
    }

    public EnumGameType getGameType()
    {
        return this.gameType;
    }

    /**
     * Get if we are in creative game mode.
     */
    public boolean isCreative()
    {
        return this.gameType.isCreative();
    }

    /**
     * if the gameType is currently NOT_SET then change it to par1
     */
    public void initializeGameType(EnumGameType par1EnumGameType)
    {
        if (this.gameType == EnumGameType.NOT_SET)
        {
            this.gameType = par1EnumGameType;
        }

        this.setGameType(this.gameType);
    }

    public void updateBlockRemoving()
    {
        this.curblockDamage = (int)(System.currentTimeMillis() / 50);  // CraftBukkit
        int var1;
        float var4;
        int var5;

        if (this.receivedFinishDiggingPacket)
        {
            var1 = this.curblockDamage - this.field_73093_n;
            int var2 = this.theWorld.getBlockId(this.posX, this.posY, this.posZ);

            if (var2 == 0)
            {
                this.receivedFinishDiggingPacket = false;
            }
            else
            {
                Block var3 = Block.blocksList[var2];
                var4 = var3.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.posX, this.posY, this.posZ) * (float)(var1 + 1);
                var5 = (int)(var4 * 10.0F);

                if (var5 != this.durabilityRemainingOnBlock)
                {
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, this.posX, this.posY, this.posZ, var5);
                    this.durabilityRemainingOnBlock = var5;
                }

                if (var4 >= 1.0F)
                {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.posX, this.posY, this.posZ);
                }
            }
        }
        else if (this.isDestroyingBlock)
        {
            var1 = this.theWorld.getBlockId(this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ);
            Block var6 = Block.blocksList[var1];

            if (var6 == null)
            {
                this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            }
            else
            {
                int var7 = this.curblockDamage - this.initialDamage;
                var4 = var6.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ) * (float)(var7 + 1);
                var5 = (int)(var4 * 10.0F);

                if (var5 != this.durabilityRemainingOnBlock)
                {
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ, var5);
                    this.durabilityRemainingOnBlock = var5;
                }
            }
        }
    }

    /**
     * if not creative, it calls destroyBlockInWorldPartially untill the block is broken first. par4 is the specific
     * side. tryHarvestBlock can also be the result of this call
     */
    public void onBlockClicked(int par1, int par2, int par3, int par4)
    {
        // this.world.douseFire((EntityHuman) null, i, j, k, l); // CraftBukkit - moved down
        // CraftBukkit
        PlayerInteractEvent var5 = CraftEventFactory.callPlayerInteractEvent(this.thisPlayerMP, Action.LEFT_CLICK_BLOCK, par1, par2, par3, par4, this.thisPlayerMP.inventory.getCurrentItem());

        if (!this.gameType.isAdventure() || this.thisPlayerMP.canCurrentToolHarvestBlock(par1, par2, par3))
        {
            // CraftBukkit start
            if (var5.isCancelled())
            {
                // Let the client know the block still exists
                ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, this.theWorld));
                // Update any tile entity data for this block
                TileEntity tileentity = this.theWorld.getBlockTileEntity(par1, par2, par3);

                if (tileentity != null)
                {
                    this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(tileentity.getDescriptionPacket());
                }

                return;
            }

            // CraftBukkit end
            if (this.isCreative())
            {
                if (!this.theWorld.extinguishFire((EntityPlayer)null, par1, par2, par3, par4))
                {
                    this.tryHarvestBlock(par1, par2, par3);
                }
            }
            else
            {
                this.theWorld.extinguishFire(this.thisPlayerMP, par1, par2, par3, par4);
                this.initialDamage = this.curblockDamage;
                float var6 = 1.0F;
                int var7 = this.theWorld.getBlockId(par1, par2, par3);

                // CraftBukkit start - Swings at air do *NOT* exist.
                if (var5.useInteractedBlock() == Event.Result.DENY)
                {
                    // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                    if (var7 == Block.doorWood.blockID)
                    {
                        // For some reason *BOTH* the bottom/top part have to be marked updated.
                        boolean bottom = (this.theWorld.getBlockMetadata(par1, par2, par3) & 8) == 0;
                        ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, this.theWorld));
                        ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2 + (bottom ? 1 : -1), par3, this.theWorld));
                    }
                    else if (var7 == Block.trapdoor.blockID)
                    {
                        ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, this.theWorld));
                    }
                }
                else if (var7 > 0)
                {
                    Block.blocksList[var7].onBlockClicked(this.theWorld, par1, par2, par3, this.thisPlayerMP);
                    // Allow fire punching to be blocked
                    this.theWorld.extinguishFire((EntityPlayer) null, par1, par2, par3, par4);
                }

                // Handle hitting a block
                if (var7 > 0)
                {
                    var6 = Block.blocksList[var7].getPlayerRelativeBlockHardness(this.thisPlayerMP, this.theWorld, par1, par2, par3);
                }

                if (var5.useItemInHand() == Event.Result.DENY)
                {
                    // If we 'insta destroyed' then the client needs to be informed.
                    if (var6 > 1.0f)
                    {
                        ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, this.theWorld));
                    }

                    return;
                }

                org.bukkit.event.block.BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.thisPlayerMP, par1, par2, par3, this.thisPlayerMP.inventory.getCurrentItem(), var6 >= 1.0f);

                if (blockEvent.isCancelled())
                {
                    // Let the client know the block still exists
                    ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, this.theWorld));
                    return;
                }

                if (blockEvent.getInstaBreak())
                {
                    var6 = 2.0f;
                }

                // CraftBukkit end

                if (var7 > 0 && var6 >= 1.0F)
                {
                    this.tryHarvestBlock(par1, par2, par3);
                }
                else
                {
                    this.isDestroyingBlock = true;
                    this.partiallyDestroyedBlockX = par1;
                    this.partiallyDestroyedBlockY = par2;
                    this.partiallyDestroyedBlockZ = par3;
                    int j1 = (int)(var6 * 10.0F);
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, par1, par2, par3, j1);
                    this.durabilityRemainingOnBlock = j1;
                }
            }
        }
    }

    public void uncheckedTryHarvestBlock(int par1, int par2, int par3)
    {
        if (par1 == this.partiallyDestroyedBlockX && par2 == this.partiallyDestroyedBlockY && par3 == this.partiallyDestroyedBlockZ)
        {
            this.curblockDamage = (int)(System.currentTimeMillis() / 50);  // CraftBukkit
            int var4 = this.curblockDamage - this.initialDamage;
            int var5 = this.theWorld.getBlockId(par1, par2, par3);

            if (var5 != 0)
            {
                Block var6 = Block.blocksList[var5];
                float var7 = var6.getPlayerRelativeBlockHardness(this.thisPlayerMP, this.thisPlayerMP.worldObj, par1, par2, par3) * (float)(var4 + 1);

                if (var7 >= 0.7F)
                {
                    this.isDestroyingBlock = false;
                    this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, par1, par2, par3, -1);
                    this.tryHarvestBlock(par1, par2, par3);
                }
                else if (!this.receivedFinishDiggingPacket)
                {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.posX = par1;
                    this.posY = par2;
                    this.posZ = par3;
                    this.field_73093_n = this.initialDamage;
                }
            }

            // CraftBukkit start - force blockreset to client
        }
        else
        {
            ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, this.theWorld));
            // CraftBukkit end
        }
    }

    /**
     * note: this ignores the pars passed in and continues to destroy the onClickedBlock
     */
    public void cancelDestroyingBlock(int par1, int par2, int par3)
    {
        this.isDestroyingBlock = false;
        this.theWorld.destroyBlockInWorldPartially(this.thisPlayerMP.entityId, this.partiallyDestroyedBlockX, this.partiallyDestroyedBlockY, this.partiallyDestroyedBlockZ, -1);
    }

    /**
     * Removes a block and triggers the appropriate events
     */
    private boolean removeBlock(int par1, int par2, int par3)
    {
        Block var4 = Block.blocksList[this.theWorld.getBlockId(par1, par2, par3)];
        int var5 = this.theWorld.getBlockMetadata(par1, par2, par3);

        if (var4 != null)
        {
            var4.onBlockHarvested(this.theWorld, par1, par2, par3, var5, this.thisPlayerMP);
        }

        boolean var6 = this.theWorld.setBlockWithNotify(par1, par2, par3, 0);

        if (var4 != null && var6)
        {
            var4.onBlockDestroyedByPlayer(this.theWorld, par1, par2, par3, var5);
        }

        return var6;
    }

    /**
     * Attempts to harvest a block at the given coordinate
     */
    public boolean tryHarvestBlock(int par1, int par2, int par3)
    {
        // CraftBukkit start
        BlockBreakEvent event = null;

        if (this.thisPlayerMP instanceof EntityPlayerMP)
        {
            org.bukkit.block.Block block = this.theWorld.getWorld().getBlockAt(par1, par2, par3);

            // Tell client the block is gone immediately then process events
            if (theWorld.getBlockTileEntity(par1, par2, par3) == null)
            {
                Packet53BlockChange packet = new Packet53BlockChange(par1, par2, par3, this.theWorld);
                packet.type = 0;
                packet.metadata = 0;
                ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(packet);
            }

            event = new BlockBreakEvent(block, this.thisPlayerMP.getBukkitEntity());
            // Adventure mode pre-cancel
            event.setCancelled(this.gameType.isAdventure() && !this.thisPlayerMP.canCurrentToolHarvestBlock(par1, par2, par3));
            // Calculate default block experience
            Block nmsBlock = Block.blocksList[block.getTypeId()];

            if (nmsBlock != null && !event.isCancelled() && !this.isCreative() && this.thisPlayerMP.canHarvestBlock(nmsBlock))
            {
                // Copied from Block.a(world, entityhuman, int, int, int, int)
                if (!(Block.canSilkHarvest__Public_CB(nmsBlock) && EnchantmentHelper.getSilkTouchModifier(this.thisPlayerMP)))   // CBMCP - use public wrapper for s_()
                {
                    int data = block.getData();
                    int bonusLevel = EnchantmentHelper.getFortuneModifier(this.thisPlayerMP);
                    event.setExpToDrop(nmsBlock.getExpDrop(this.theWorld, data, bonusLevel));
                }
            }

            this.theWorld.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled())
            {
                // Let the client know the block still exists
                ((EntityPlayerMP) this.thisPlayerMP).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, this.theWorld));
                // Update any tile entity data for this block
                TileEntity tileentity = this.theWorld.getBlockTileEntity(par1, par2, par3);

                if (tileentity != null)
                {
                    this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(tileentity.getDescriptionPacket());
                }

                return false;
            }
        }

        if (false)   // Never trigger
        {
            // CraftBukkit end
            return false;
        }
        else
        {
            int var4 = this.theWorld.getBlockId(par1, par2, par3);

            if (Block.blocksList[var4] == null)
            {
                return false;    // CraftBukkit - a plugin set block to air without cancelling
            }

            int var5 = this.theWorld.getBlockMetadata(par1, par2, par3);

            // CraftBukkit start - special case skulls, their item data comes from a tile entity
            if (var4 == Block.skull.blockID && !this.isCreative())
            {
                Block.skull.dropBlockAsItemWithChance(theWorld, par1, par2, par3, var5, 1.0F, 0);
                return this.removeBlock(par1, par2, par3);
            }

            // CraftBukkit end
            this.theWorld.playAuxSFXAtEntity(this.thisPlayerMP, 2001, par1, par2, par3, var4 + (this.theWorld.getBlockMetadata(par1, par2, par3) << 12));
            boolean var6 = this.removeBlock(par1, par2, par3);

            if (this.isCreative())
            {
                this.thisPlayerMP.playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par1, par2, par3, this.theWorld));
            }
            else
            {
                ItemStack var7 = this.thisPlayerMP.getCurrentEquippedItem();
                boolean var8 = this.thisPlayerMP.canHarvestBlock(Block.blocksList[var4]);

                if (var7 != null)
                {
                    var7.onBlockDestroyed(this.theWorld, var4, par1, par2, par3, this.thisPlayerMP);

                    if (var7.stackSize == 0)
                    {
                        this.thisPlayerMP.destroyCurrentEquippedItem();
                    }
                }

                if (var6 && var8)
                {
                    Block.blocksList[var4].harvestBlock(this.theWorld, this.thisPlayerMP, par1, par2, par3, var5);
                }
            }

            // CraftBukkit start - drop event experience
            if (var6 && event != null)
            {
                Block.dropXpOnBlockBreak__Public_CB(Block.blocksList[var4], this.theWorld, par1, par2, par3, event.getExpToDrop()); // CBMCP - use public accessor for f()
            }

            // CraftBukkit end
            return var6;
        }
    }

    /**
     * Attempts to right-click use an item by the given EntityPlayer in the given World
     */
    public boolean tryUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack)
    {
        int var4 = par3ItemStack.stackSize;
        int var5 = par3ItemStack.getItemDamage();
        ItemStack var6 = par3ItemStack.useItemRightClick(par2World, par1EntityPlayer);

        if (var6 == par3ItemStack && (var6 == null || var6.stackSize == var4 && var6.getMaxItemUseDuration() <= 0 && var6.getItemDamage() == var5))
        {
            return false;
        }
        else
        {
            par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = var6;

            if (this.isCreative())
            {
                var6.stackSize = var4;

                if (var6.isItemStackDamageable())
                {
                    var6.setItemDamage(var5);
                }
            }

            if (var6.stackSize == 0)
            {
                par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = null;
            }

            if (!par1EntityPlayer.isUsingItem())
            {
                ((EntityPlayerMP)par1EntityPlayer).sendContainerToPlayer(par1EntityPlayer.inventoryContainer);
            }

            return true;
        }
    }

    /**
     * Activate the clicked on block, otherwise use the held item. Args: player, world, itemStack, x, y, z, side,
     * xOffset, yOffset, zOffset
     */
    public boolean activateBlockOrUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
        int var11 = par2World.getBlockId(par4, par5, par6);
        // CraftBukkit start - Interact
        boolean result = false;

        if (var11 > 0)
        {
            PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(par1EntityPlayer, Action.RIGHT_CLICK_BLOCK, par4, par5, par6, par7, par3ItemStack);

            if (event.useInteractedBlock() == Event.Result.DENY)
            {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                if (var11 == Block.doorWood.blockID)
                {
                    boolean bottom = (par2World.getBlockMetadata(par4, par5, par6) & 8) == 0;
                    ((EntityPlayerMP) par1EntityPlayer).playerNetServerHandler.sendPacketToPlayer(new Packet53BlockChange(par4, par5 + (bottom ? 1 : -1), par6, par2World));
                }

                result = (event.useItemInHand() != Event.Result.ALLOW);
            }
            else if (!par1EntityPlayer.isSneaking() || par3ItemStack == null)
            {
                result = Block.blocksList[var11].onBlockActivated(par2World, par4, par5, par6, par1EntityPlayer, par7, par8, par9, par10);
            }

            if (par3ItemStack != null && !result)
            {
                int j1 = par3ItemStack.getItemDamage();
                int k1 = par3ItemStack.stackSize;
                result = par3ItemStack.tryPlaceItemIntoWorld(par1EntityPlayer, par2World, par4, par5, par6, par7, par8, par9, par10);

                // The item count should not decrement in Creative mode.
                if (this.isCreative())
                {
                    par3ItemStack.setItemDamage(j1);
                    par3ItemStack.stackSize = k1;
                }
            }

            // If we have 'true' and no explicit deny *or* an explicit allow -- run the item part of the hook
            if (par3ItemStack != null && ((!result && event.useItemInHand() != Event.Result.DENY) || event.useItemInHand() == Event.Result.ALLOW))
            {
                this.tryUseItem(par1EntityPlayer, par2World, par3ItemStack);
            }
        }

        return result;
        // CraftBukkit end
    }

    /**
     * Sets the world instance.
     */
    public void setWorld(WorldServer par1WorldServer)
    {
        this.theWorld = par1WorldServer;
    }
}
