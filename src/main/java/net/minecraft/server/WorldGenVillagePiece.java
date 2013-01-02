package net.minecraft.server;

import java.util.List;
import java.util.Random;
import net.minecraft.server.Block;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.StructureBoundingBox;
import net.minecraft.server.StructurePiece;
import net.minecraft.server.World;
import net.minecraft.server.WorldGenVillagePieces;
import net.minecraft.server.WorldGenVillageStartPiece;

public abstract class WorldGenVillagePiece extends StructurePiece {

   private int a;
   protected WorldGenVillageStartPiece k;


   protected WorldGenVillagePiece(WorldGenVillageStartPiece var1, int var2) {
      super(var2);
      this.k = var1;
   }

   protected StructurePiece a(WorldGenVillageStartPiece var1, List var2, Random var3, int var4, int var5) {
      switch(this.f) {
      case 0:
         return WorldGenVillagePieces.a(var1, var2, var3, this.e.a - 1, this.e.b + var4, this.e.c + var5, 1, this.c());
      case 1:
         return WorldGenVillagePieces.a(var1, var2, var3, this.e.a + var5, this.e.b + var4, this.e.c - 1, 2, this.c());
      case 2:
         return WorldGenVillagePieces.a(var1, var2, var3, this.e.a - 1, this.e.b + var4, this.e.c + var5, 1, this.c());
      case 3:
         return WorldGenVillagePieces.a(var1, var2, var3, this.e.a + var5, this.e.b + var4, this.e.c - 1, 2, this.c());
      default:
         return null;
      }
   }

   protected StructurePiece b(WorldGenVillageStartPiece var1, List var2, Random var3, int var4, int var5) {
      switch(this.f) {
      case 0:
         return WorldGenVillagePieces.a(var1, var2, var3, this.e.d + 1, this.e.b + var4, this.e.c + var5, 3, this.c());
      case 1:
         return WorldGenVillagePieces.a(var1, var2, var3, this.e.a + var5, this.e.b + var4, this.e.f + 1, 0, this.c());
      case 2:
         return WorldGenVillagePieces.a(var1, var2, var3, this.e.d + 1, this.e.b + var4, this.e.c + var5, 3, this.c());
      case 3:
         return WorldGenVillagePieces.a(var1, var2, var3, this.e.a + var5, this.e.b + var4, this.e.f + 1, 0, this.c());
      default:
         return null;
      }
   }

   protected int b(World var1, StructureBoundingBox var2) {
      int var3 = 0;
      int var4 = 0;

      for(int var5 = this.e.c; var5 <= this.e.f; ++var5) {
         for(int var6 = this.e.a; var6 <= this.e.d; ++var6) {
            if(var2.b(var6, 64, var5)) {
               var3 += Math.max(var1.i(var6, var5), var1.worldProvider.getSeaLevel());
               ++var4;
            }
         }
      }

      if(var4 == 0) {
         return -1;
      } else {
         return var3 / var4;
      }
   }

   protected static boolean a(StructureBoundingBox var0) {
      return var0 != null && var0.b > 10;
   }

   protected void a(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6) {
      if(this.a < var6) {
         for(int var7 = this.a; var7 < var6; ++var7) {
            int var8 = this.a(var3 + var7, var5);
            int var9 = this.a(var4);
            int var10 = this.b(var3 + var7, var5);
            if(!var2.b(var8, var9, var10)) {
               break;
            }

            ++this.a;
            EntityVillager var11 = new EntityVillager(var1, this.b(var7));
            var11.setPositionRotation((double)var8 + 0.5D, (double)var9, (double)var10 + 0.5D, 0.0F, 0.0F);
            var1.addEntity(var11);
         }

      }
   }

   protected int b(int var1) {
      return 0;
   }

   protected int d(int var1, int var2) {
      if(this.k.b) {
         if(var1 == Block.LOG.id) {
            return Block.SANDSTONE.id;
         }

         if(var1 == Block.COBBLESTONE.id) {
            return Block.SANDSTONE.id;
         }

         if(var1 == Block.WOOD.id) {
            return Block.SANDSTONE.id;
         }

         if(var1 == Block.WOOD_STAIRS.id) {
            return Block.SANDSTONE_STAIRS.id;
         }

         if(var1 == Block.COBBLESTONE_STAIRS.id) {
            return Block.SANDSTONE_STAIRS.id;
         }

         if(var1 == Block.GRAVEL.id) {
            return Block.SANDSTONE.id;
         }
      }

      return var1;
   }

   protected int e(int var1, int var2) {
      if(this.k.b) {
         if(var1 == Block.LOG.id) {
            return 0;
         }

         if(var1 == Block.COBBLESTONE.id) {
            return 0;
         }

         if(var1 == Block.WOOD.id) {
            return 2;
         }
      }

      return var2;
   }

   protected void a(World var1, int var2, int var3, int var4, int var5, int var6, StructureBoundingBox var7) {
      int var8 = this.d(var2, var3);
      int var9 = this.e(var2, var3);
      super.a(var1, var8, var9, var4, var5, var6, var7);
   }

   protected void a(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11) {
      int var12 = this.d(var9, 0);
      int var13 = this.e(var9, 0);
      int var14 = this.d(var10, 0);
      int var15 = this.e(var10, 0);
      super.a(var1, var2, var3, var4, var5, var6, var7, var8, var12, var13, var14, var15, var11);
   }

   protected void b(World var1, int var2, int var3, int var4, int var5, int var6, StructureBoundingBox var7) {
      int var8 = this.d(var2, var3);
      int var9 = this.e(var2, var3);
      super.b(var1, var8, var9, var4, var5, var6, var7);
   }
}
