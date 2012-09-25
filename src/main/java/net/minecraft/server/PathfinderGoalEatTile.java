package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.Material;
// CraftBukkit end

public class PathfinderGoalEatTile extends PathfinderGoal {

    private EntityLiving b;
    private World c;
    int a = 0;

    public PathfinderGoalEatTile(EntityLiving entityliving) {
        this.b = entityliving;
        this.c = entityliving.worldObj;
        this.a(7);
    }

    public boolean a() {
        if (this.b.au().nextInt(this.b.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            int i = MathHelper.floor(this.b.posX);
            int j = MathHelper.floor(this.b.posY);
            int k = MathHelper.floor(this.b.posZ);

            return this.c.getBlockId(i, j, k) == Block.LONG_GRASS.blockID && this.c.getData(i, j, k) == 1 ? true : this.c.getBlockId(i, j - 1, k) == Block.GRASS.blockID;
        }
    }

    public void e() {
        this.a = 40;
        this.c.setEntityState(this.b, (byte) 10);
        this.b.getNavigation().g();
    }

    public void c() {
        this.a = 0;
    }

    public boolean b() {
        return this.a > 0;
    }

    public int f() {
        return this.a;
    }

    public void d() {
        this.a = Math.max(0, this.a - 1);
        if (this.a == 4) {
            int i = MathHelper.floor(this.b.posX);
            int j = MathHelper.floor(this.b.posY);
            int k = MathHelper.floor(this.b.posZ);

            if (this.c.getBlockId(i, j, k) == Block.LONG_GRASS.blockID) {
                // CraftBukkit start
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.b.getBukkitEntity(), this.b.worldObj.getWorld().getBlockAt(i, j, k), Material.AIR).isCancelled()) {
                    this.c.triggerEffect(2001, i, j, k, Block.LONG_GRASS.blockID + 4096);
                    this.c.setBlockWithNotify(i, j, k, 0);
                    this.b.aA();
                }
                // CraftBukkit end
            } else if (this.c.getBlockId(i, j - 1, k) == Block.GRASS.blockID) {
                // CraftBukkit start
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.b.getBukkitEntity(), this.b.worldObj.getWorld().getBlockAt(i, j - 1, k), Material.DIRT).isCancelled()) {
                    this.c.triggerEffect(2001, i, j - 1, k, Block.GRASS.blockID);
                    this.c.setBlockWithNotify(i, j - 1, k, Block.DIRT.blockID);
                    this.b.aA();
                }
                // CraftBukkit end
            }
        }
    }
}
