package net.minecraft.server;

import java.util.List;

public abstract class EntityAnimal extends EntityAgeable implements IAnimal {

   private int love;
   private int e = 0;


   public EntityAnimal(World var1) {
      super(var1);
   }

   protected void bm() {
      if(this.getAge() != 0) {
         this.love = 0;
      }

      super.bm();
   }

   public void c() {
      super.c();
      if(this.getAge() != 0) {
         this.love = 0;
      }

      if(this.love > 0) {
         --this.love;
         String var1 = "heart";
         if(this.love % 10 == 0) {
            double var2 = this.random.nextGaussian() * 0.02D;
            double var4 = this.random.nextGaussian() * 0.02D;
            double var6 = this.random.nextGaussian() * 0.02D;
            this.world.addParticle(var1, this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + 0.5D + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, var2, var4, var6);
         }
      } else {
         this.e = 0;
      }

   }

   protected void a(Entity var1, float var2) {
      if(var1 instanceof EntityHuman) {
         if(var2 < 3.0F) {
            double var3 = var1.locX - this.locX;
            double var5 = var1.locZ - this.locZ;
            this.yaw = (float)(Math.atan2(var5, var3) * 180.0D / 3.1415927410125732D) - 90.0F;
            this.b = true;
         }

         EntityHuman var7 = (EntityHuman)var1;
         if(var7.bT() == null || !this.c(var7.bT())) {
            this.target = null;
         }
      } else if(var1 instanceof EntityAnimal) {
         EntityAnimal var8 = (EntityAnimal)var1;
         if(this.getAge() > 0 && var8.getAge() < 0) {
            if((double)var2 < 2.5D) {
               this.b = true;
            }
         } else if(this.love > 0 && var8.love > 0) {
            if(var8.target == null) {
               var8.target = this;
            }

            if(var8.target == this && (double)var2 < 3.5D) {
               ++var8.love;
               ++this.love;
               ++this.e;
               if(this.e % 4 == 0) {
                  this.world.addParticle("heart", this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + 0.5D + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, 0.0D, 0.0D, 0.0D);
               }

               if(this.e == 60) {
                  this.b((EntityAnimal)var1);
               }
            } else {
               this.e = 0;
            }
         } else {
            this.e = 0;
            this.target = null;
         }
      }

   }

   private void b(EntityAnimal var1) {
      EntityAgeable var2 = this.createChild(var1);
      if(var2 != null) {
         this.setAge(6000);
         var1.setAge(6000);
         this.love = 0;
         this.e = 0;
         this.target = null;
         var1.target = null;
         var1.e = 0;
         var1.love = 0;
         var2.setAge(-24000);
         var2.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);

         for(int var3 = 0; var3 < 7; ++var3) {
            double var4 = this.random.nextGaussian() * 0.02D;
            double var6 = this.random.nextGaussian() * 0.02D;
            double var8 = this.random.nextGaussian() * 0.02D;
            this.world.addParticle("heart", this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + 0.5D + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, var4, var6, var8);
         }

         this.world.addEntity(var2);
      }

   }

   public boolean damageEntity(DamageSource var1, int var2) {
      if(this.isInvulnerable()) {
         return false;
      } else {
         this.c = 60;
         this.target = null;
         this.love = 0;
         return super.damageEntity(var1, var2);
      }
   }

   public float a(int var1, int var2, int var3) {
      return this.world.getTypeId(var1, var2 - 1, var3) == Block.GRASS.id?10.0F:this.world.p(var1, var2, var3) - 0.5F;
   }

   public void b(NBTTagCompound var1) {
      super.b(var1);
      var1.setInt("InLove", this.love);
   }

   public void a(NBTTagCompound var1) {
      super.a(var1);
      this.love = var1.getInt("InLove");
   }

   protected Entity findTarget() {
      if(this.c > 0) {
         return null;
      } else {
         float var1 = 8.0F;
         List var2;
         int var3;
         EntityAnimal var4;
         if(this.love > 0) {
            var2 = this.world.a(this.getClass(), this.boundingBox.grow((double)var1, (double)var1, (double)var1));

            for(var3 = 0; var3 < var2.size(); ++var3) {
               var4 = (EntityAnimal)var2.get(var3);
               if(var4 != this && var4.love > 0) {
                  return var4;
               }
            }
         } else if(this.getAge() == 0) {
            var2 = this.world.a(EntityHuman.class, this.boundingBox.grow((double)var1, (double)var1, (double)var1));

            for(var3 = 0; var3 < var2.size(); ++var3) {
               EntityHuman var5 = (EntityHuman)var2.get(var3);
               if(var5.bT() != null && this.c(var5.bT())) {
                  return var5;
               }
            }
         } else if(this.getAge() > 0) {
            var2 = this.world.a(this.getClass(), this.boundingBox.grow((double)var1, (double)var1, (double)var1));

            for(var3 = 0; var3 < var2.size(); ++var3) {
               var4 = (EntityAnimal)var2.get(var3);
               if(var4 != this && var4.getAge() < 0) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   public boolean canSpawn() {
      int var1 = MathHelper.floor(this.locX);
      int var2 = MathHelper.floor(this.boundingBox.b);
      int var3 = MathHelper.floor(this.locZ);
      return this.world.getTypeId(var1, var2 - 1, var3) == Block.GRASS.id && this.world.l(var1, var2, var3) > 8 && super.canSpawn();
   }

   public int aN() {
      return 120;
   }

   protected boolean bj() {
      return !isNearTorch(this, 12D, world);
   }

   protected int getExpValue(EntityHuman var1) {
      return 1 + this.world.random.nextInt(3);
   }

   public boolean c(ItemStack var1) {
      return var1.id == Item.WHEAT.id;
   }

   public boolean a(EntityHuman var1) {
      ItemStack var2 = var1.inventory.getItemInHand();
      if(var2 != null && this.c(var2) && this.getAge() == 0) {
         if(!var1.abilities.canInstantlyBuild) {
            --var2.count;
            if(var2.count <= 0) {
               var1.inventory.setItem(var1.inventory.itemInHandIndex, (ItemStack)null);
            }
         }

         this.love = 600;
         this.target = null;

         for(int var3 = 0; var3 < 7; ++var3) {
            double var4 = this.random.nextGaussian() * 0.02D;
            double var6 = this.random.nextGaussian() * 0.02D;
            double var8 = this.random.nextGaussian() * 0.02D;
            this.world.addParticle("heart", this.locX + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, this.locY + 0.5D + (double)(this.random.nextFloat() * this.length), this.locZ + (double)(this.random.nextFloat() * this.width * 2.0F) - (double)this.width, var4, var6, var8);
         }

         return true;
      } else {
         return super.a(var1);
      }
   }

   public boolean r() {
      return this.love > 0;
   }

   public void s() {
      this.love = 0;
   }

   public boolean mate(EntityAnimal var1) {
      return var1 == this?false:(var1.getClass() != this.getClass()?false:this.r() && var1.r());
   }
   
	public static boolean isNearTorch(Entity entity, Double dist, World worldObj)
	{
		AxisAlignedBB axisalignedbb = entity.boundingBox.grow(dist, dist / 2D, dist);
		int i = MathHelper.floor(axisalignedbb.a);
		int j = MathHelper.floor(axisalignedbb.d + 1.0D);
		int k = MathHelper.floor(axisalignedbb.b);
		int l = MathHelper.floor(axisalignedbb.e + 1.0D);
		int i1 = MathHelper.floor(axisalignedbb.c);
		int j1 = MathHelper.floor(axisalignedbb.f + 1.0D);
		for (int k1 = i; k1 < j; k1++)
		{
			for (int l1 = k; l1 < l; l1++)
			{
				for (int i2 = i1; i2 < j1; i2++)
				{
					int j2 = worldObj.getTypeId(k1, l1, i2);

					if (j2 != 0)
					{
						String nameToCheck = "";
						nameToCheck = Block.byId[j2].a();
						if (nameToCheck != null && nameToCheck != "")
						{
							if (nameToCheck.equals("tile.torch") || nameToCheck.equals("tile.lightgem") || nameToCheck.equals("tile.redstoneLight") || nameToCheck.equals("tile.litpumpkin")) { return true; }
						}

					}

				}

			}

		}

		return false;
	}
}
