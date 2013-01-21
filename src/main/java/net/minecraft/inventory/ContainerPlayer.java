package net.minecraft.inventory;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.packet.Packet103SetSlot;
// CraftBukkit end

public class ContainerPlayer extends Container
{
    /** The crafting matrix inventory. */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    public IInventory craftResult = new InventoryCraftResult();

    /** Determines if inventory manipulation should be handled. */
    public boolean isLocalWorld = false;
    private final EntityPlayer thePlayer;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private InventoryPlayer player;
    // CraftBukkit end

    public ContainerPlayer(InventoryPlayer par1InventoryPlayer, boolean par2, EntityPlayer par3EntityPlayer)
    {
        this.isLocalWorld = par2;
        this.thePlayer = par3EntityPlayer;
        this.craftResult = new InventoryCraftResult(); // CraftBukkit - moved to before InventoryCrafting construction
        this.craftMatrix = new InventoryCrafting(this, 2, 2, par1InventoryPlayer.player); // CraftBukkit - pass player
        this.craftMatrix.resultInventory = this.craftResult; // CraftBukkit - let InventoryCrafting know about its result slot
        this.player = par1InventoryPlayer; // CraftBukkit - save player
        this.addSlotToContainer((Slot)(new SlotCrafting(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 144, 36)));
        int var4;
        int var5;

        for (var4 = 0; var4 < 2; ++var4)
        {
            for (var5 = 0; var5 < 2; ++var5)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, var5 + var4 * 2, 88 + var5 * 18, 26 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 4; ++var4)
        {
            this.addSlotToContainer((Slot)(new SlotArmor(this, par1InventoryPlayer, par1InventoryPlayer.getSizeInventory() - 1 - var4, 8, 8 + var4 * 18, var4)));
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var4, 8 + var4 * 18, 142));
        }

        // this.a((IInventory) this.craftInventory); // CraftBukkit - unneeded since it just sets result slot to empty
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        // CraftBukkit start (Note: the following line would cause an error if called during construction)
        CraftingManager.getInstance().lastCraftView = getBukkitView();
        ItemStack craftResult = CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj);
        this.craftResult.setInventorySlotContents(0, craftResult);

        if (super.crafters.size() < 1)
        {
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) super.crafters.get(0); // TODO: Is this _always_ correct? Seems like it.
        player.playerNetServerHandler.sendPacketToPlayer(new Packet103SetSlot(player.openContainer.windowId, 0, craftResult));
        // CraftBukkit end
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);

        for (int var2 = 0; var2 < 4; ++var2)
        {
            ItemStack var3 = this.craftMatrix.getStackInSlotOnClosing(var2);

            if (var3 != null)
            {
                par1EntityPlayer.dropPlayerItem(var3);
            }
        }

        this.craftResult.setInventorySlotContents(0, (ItemStack)null);
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 == 0)
            {
                if (!this.mergeItemStack(var5, 9, 45, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (par2 >= 1 && par2 < 5)
            {
                if (!this.mergeItemStack(var5, 9, 45, false))
                {
                    return null;
                }
            }
            else if (par2 >= 5 && par2 < 9)
            {
                if (!this.mergeItemStack(var5, 9, 45, false))
                {
                    return null;
                }
            }
            else if (var3.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)var3.getItem()).armorType)).getHasStack())
            {
                int var6 = 5 + ((ItemArmor)var3.getItem()).armorType;

                if (!this.mergeItemStack(var5, var6, var6 + 1, false))
                {
                    return null;
                }
            }
            else if (par2 >= 9 && par2 < 36)
            {
                if (!this.mergeItemStack(var5, 36, 45, false))
                {
                    return null;
                }
            }
            else if (par2 >= 36 && par2 < 45)
            {
                if (!this.mergeItemStack(var5, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 9, 45, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(par1EntityPlayer, var5);
        }

        return var3;
    }

    // CraftBukkit start
    public CraftInventoryView getBukkitView()
    {
        if (bukkitEntity != null)
        {
            return bukkitEntity;
        }

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.craftMatrix, this.craftResult);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
