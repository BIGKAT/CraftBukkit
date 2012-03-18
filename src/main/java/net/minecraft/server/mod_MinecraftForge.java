package net.minecraft.server;

import forge.ForgeHooks;
import forge.MinecraftForge;
import forge.NetworkMod;

/**
 * This class is just here to make the Forge version show up nicely in the ModLoader logs/Crash Screen
 */
public class mod_MinecraftForge extends NetworkMod
{
    @Override
    public String getVersion()
    {
        return String.format("%d.%d.%d.%d",
                ForgeHooks.majorVersion,    ForgeHooks.minorVersion,
                ForgeHooks.revisionVersion, ForgeHooks.buildVersion);
    }

    @Override
    public void load()
    {
        MinecraftForge.getDungeonLootTries(); //Random thing to make things Initialize
    }

	@Override
	public boolean clientSideRequired() 
	{
		return false;
	}

	@Override
	public boolean serverSideRequired() 
	{
		return false;
	}
}
