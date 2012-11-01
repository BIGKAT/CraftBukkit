package org.bukkit.craftbukkit;

import net.minecraft.src.WorldServer;

import org.bukkit.BlockChangeDelegate;

public class BCDProxy implements BlockChangeDelegate {

	public BCDProxy(WorldServer world) {
		// TODO Auto-generated constructor stub
	}

	public boolean setRawTypeId(int x, int y, int z, int typeId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean setRawTypeIdAndData(int x, int y, int z, int typeId, int data) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean setTypeId(int x, int y, int z, int typeId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean setTypeIdAndData(int x, int y, int z, int typeId, int data) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getTypeId(int x, int y, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isEmpty(int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

}
