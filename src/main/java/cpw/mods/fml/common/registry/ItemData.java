package cpw.mods.fml.common.registry;

import java.util.Map;

import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.ItemBlock;
import net.minecraft.server.NBTTagCompound;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;

import cpw.mods.fml.common.ModContainer;

public class ItemData {

    private static Map<String, Multiset<String>> modOrdinals = Maps.newHashMap();
    public final String modId;
    public String itemType; // MCPC remove final
    public final int itemId;
    public int ordinal; // MCPC remove final

    public ItemData(Item item, ModContainer mc)
    {
        this.itemId = item.id;
        if (item.getClass().equals(ItemBlock.class))
        {
            this.itemType =  Block.byId[this.itemId].getClass().getName();
        }
        else
        {
            this.itemType = item.getClass().getName();
        }
        this.modId = mc.getModId();
        if (!modOrdinals.containsKey(mc.getModId()))
        {
            modOrdinals.put(mc.getModId(), HashMultiset.<String>create());
        }
        this.ordinal = modOrdinals.get(mc.getModId()).add(itemType, 1);
    }

    public ItemData(NBTTagCompound tag)
    {
        this.modId = tag.getString("ModId");
        this.itemType = tag.getString("ItemType");
        this.itemId = tag.getInt("ItemId");
        this.ordinal = tag.getInt("ordinal");
    }

    public NBTTagCompound toNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("ModId", modId);
        tag.setString("ItemType", itemType);
        tag.setInt("ItemId", itemId);
        tag.setInt("ordinal", ordinal);
        return tag;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(modId, itemType, itemId, ordinal);
    }

    @Override
    public boolean equals(Object obj)
    {
        try
        {
            ItemData other = (ItemData) obj;
            return Objects.equal(modId, other.modId) && Objects.equal(itemType, other.itemType) && Objects.equal(itemId, other.itemId) && Objects.equal(ordinal, other.ordinal);
        }
        catch (ClassCastException cce)
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return String.format("Item %d, Type %s, owned by %s, ordinal %d", itemId, itemType, modId, ordinal);
    }

    public boolean mayDifferByOrdinal(ItemData rightValue)
    {
        return Objects.equal(itemType, rightValue.itemType) && Objects.equal(modId, rightValue.modId);
    }
}
