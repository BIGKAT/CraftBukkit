package net.minecraft.src;

public abstract class EntityAgeable extends EntityCreature {
    public boolean ageLocked = false; // CraftBukkit

    public EntityAgeable(net.minecraft.src.World world) {
        super(world);
    }

    protected void a() {
        super.a();
        this.datawatcher.a(12, new Integer(0));
    }

    public int getGrowingAge() {
        return this.datawatcher.getInt(12);
    }

    public void setGrowingAge(int i) {
        this.datawatcher.watch(12, Integer.valueOf(i));
    }

    public void b(net.minecraft.src.NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInteger("Age", this.getGrowingAge());
        nbttagcompound.setBoolean("AgeLocked", this.ageLocked); // CraftBukkit
    }

    public void a(net.minecraft.src.NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setGrowingAge(nbttagcompound.getInteger("Age"));
        this.ageLocked = nbttagcompound.getBoolean("AgeLocked"); // CraftBukkit
    }

    public void d() {
        super.d();
        int i = this.getGrowingAge();

        if (ageLocked) return; // CraftBukkit
        if (i < 0) {
            ++i;
            this.setGrowingAge(i);
        } else if (i > 0) {
            --i;
            this.setGrowingAge(i);
        }
    }

    public boolean isBaby() {
        return this.getGrowingAge() < 0;
    }
}
