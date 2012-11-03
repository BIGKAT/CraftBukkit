package forge.bukkit;

import net.minecraft.server.World;

import org.bukkit.BlockChangeDelegate;

public class BukkitForgeHooks {

	public static World unwrapBlockChangeDelegate(BlockChangeDelegate world) {
		if (world instanceof ForgeBlockChangeDelegate)
		{
			return ((ForgeBlockChangeDelegate)world).unwrap();
		}
		else
		{
			throw new IllegalArgumentException("Attempted to access a forge grow method with an incompatible BlockChangeDelegate type");
		}
	}

	public interface ForgeBlockChangeDelegate extends BlockChangeDelegate {
		World unwrap();
	}
}
