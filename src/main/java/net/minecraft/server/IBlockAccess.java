package net.minecraft.server;

public interface IBlockAccess {

   int getTypeId(int var1, int var2, int var3);

   TileEntity getTileEntity(int var1, int var2, int var3);

   int getData(int var1, int var2, int var3);

   Material getMaterial(int var1, int var2, int var3);

   boolean t(int var1, int var2, int var3);

   Vec3DPool getVec3DPool();

   boolean isBlockFacePowered(int var1, int var2, int var3, int var4);

  public abstract BiomeBase getBiome(int i, int j); // MCPC
}
