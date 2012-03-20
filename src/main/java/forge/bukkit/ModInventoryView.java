package forge.bukkit;

import net.minecraft.server.Container;
import net.minecraft.server.EntityPlayer;

import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class ModInventoryView extends InventoryView {

	private Container container;
	private EntityPlayer player;

	public ModInventoryView(Container container, EntityPlayer player) {
		this.container=container;
		this.player=player;
	}

	@Override
	public Inventory getTopInventory() {
		return new CraftInventory(container.getInventory());
	}

	@Override
	public Inventory getBottomInventory() {
		return player.getBukkitEntity().getInventory();
	}

	@Override
	public HumanEntity getPlayer() {
		return player.getBukkitEntity();
	}

	@Override
	public InventoryType getType() {
		return InventoryType.MOD;
	}

}
