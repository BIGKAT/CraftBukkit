package net.minecraft.src;

public final class ItemStack {

    public int count;
    public int b;
    public int id;
    public NBTTagCompound tag;
    private int damage;

    public ItemStack(Block block) {
        this(block, 1);
    }

    public ItemStack(Block block, int i) {
        this(block.blockID, i, 0);
    }

    public ItemStack(Block block, int i, int j) {
        this(block.blockID, i, j);
    }

    public ItemStack(Item item) {
        this(item.shiftedIndex, 1, 0);
    }

    public ItemStack(Item item, int i) {
        this(item.shiftedIndex, i, 0);
    }

    public ItemStack(Item item, int i, int j) {
        this(item.shiftedIndex, i, j);
    }

    public ItemStack(int i, int j, int k) {
        this.count = 0;
        this.id = i;
        this.count = j;
        this.setData(k); // CraftBukkit
    }

    // CraftBukkit start - used to create a new ItemStack, specifying the enchantments at time of creation.
    public ItemStack(int id, int count, int data, NBTTagList enchantments) {
        this(id, count, data);
        // taken from .addEnchantment
        if (enchantments != null && Item.itemsList[this.id].getMaxStackSize() == 1) {
            if (this.tag == null) {
                this.setTag(new NBTTagCompound());
            }

            this.tag.set("ench", enchantments.copy()); // modify this part to use passed in enchantments list
            // TODO Books
        }
    }
    // CraftBukkit end

    public static ItemStack a(NBTTagCompound nbttagcompound) {
        ItemStack itemstack = new ItemStack();

        itemstack.c(nbttagcompound);
        return itemstack.getItem() != null ? itemstack : null;
    }

    private ItemStack() {
        this.count = 0;
    }

    public ItemStack a(int i) {
        ItemStack itemstack = new ItemStack(this.id, i, this.damage);

        if (this.tag != null) {
            itemstack.tag = (NBTTagCompound) this.tag.clone();
        }

        this.count -= i;
        return itemstack;
    }

    public Item getItem() {
        return Item.itemsList[this.id];
    }

    public boolean placeItem(EntityPlayer entityhuman, net.minecraft.src.World world, int i, int j, int k, int l, float f, float f1, float f2) {
        boolean flag = this.getItem().interactWith(this, entityhuman, world, i, j, k, l, f, f1, f2);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }

        return flag;
    }

    public float a(Block block) {
        return this.getItem().getDestroySpeed(this, block);
    }

    public ItemStack a(net.minecraft.src.World world, EntityPlayer entityhuman) {
        return this.getItem().a(this, world, entityhuman);
    }

    public ItemStack b(net.minecraft.src.World world, EntityPlayer entityhuman) {
        return this.getItem().b(this, world, entityhuman);
    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("id", (short) this.id);
        nbttagcompound.setByte("Count", (byte) this.count);
        nbttagcompound.setShort("Damage", (short) this.damage);
        if (this.tag != null) {
            nbttagcompound.set("tag", this.tag);
        }

        return nbttagcompound;
    }

    public void c(NBTTagCompound nbttagcompound) {
        this.id = nbttagcompound.getShort("id");
        this.count = nbttagcompound.getByte("Count");
        this.damage = nbttagcompound.getShort("Damage");
        if (nbttagcompound.hasKey("tag")) {
            this.tag = nbttagcompound.getCompoundTag("tag");
        }
    }

    public int getMaxStackSize() {
        return this.getItem().getMaxStackSize();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.f() || !this.h());
    }

    public boolean f() {
        return Item.itemsList[this.id].getMaxDurability() > 0;
    }

    public boolean usesData() {
        return Item.itemsList[this.id].k();
    }

    public boolean h() {
        return this.f() && this.damage > 0;
    }

    public int i() {
        return this.damage;
    }

    public int getData() {
        return this.damage;
    }

    public void setData(int i) {
        this.damage = (this.id > 0) && (this.id < 256) ? Item.itemsList[this.id].filterData(i) : i; // CraftBukkit
    }

    public int k() {
        return Item.itemsList[this.id].getMaxDurability();
    }

    public void damage(int i, EntityLiving entityliving) {
        if (this.f()) {
            if (i > 0 && entityliving instanceof EntityPlayer) {
                int j = EnchantmentManager.getDurabilityEnchantmentLevel(((EntityPlayer) entityliving).inventory);

                if (j > 0 && entityliving.worldObj.rand.nextInt(j + 1) > 0) {
                    return;
                }
            }

            if (!(entityliving instanceof EntityPlayer) || !((EntityPlayer) entityliving).capabilities.canInstantlyBuild) {
                this.damage += i;
            }

            if (this.damage > this.k()) {
                entityliving.a(this);
                if (entityliving instanceof EntityPlayer) {
                    ((EntityPlayer) entityliving).a(StatisticList.F[this.id], 1);
                }

                --this.count;
                if (this.count < 0) {
                    this.count = 0;
                }

                // CraftBukkit start - Check for item breaking
                if (this.count == 0 && entityliving instanceof EntityPlayer) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerItemBreakEvent((EntityPlayer) entityliving, this);
                }
                // CraftBukkit end

                this.damage = 0;
            }
        }
    }

    public void a(EntityLiving entityliving, EntityPlayer entityhuman) {
        boolean flag = Item.itemsList[this.id].a(this, entityliving, (EntityLiving) entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }
    }

    public void a(net.minecraft.src.World world, int i, int j, int k, int l, EntityPlayer entityhuman) {
        boolean flag = Item.itemsList[this.id].a(this, world, i, j, k, l, entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }
    }

    public int a(Entity entity) {
        return Item.itemsList[this.id].a(entity);
    }

    public boolean b(Block block) {
        return Item.itemsList[this.id].canHarvestBlock(block);
    }

    public boolean a(EntityLiving entityliving) {
        return Item.itemsList[this.id].a(this, entityliving);
    }

    public ItemStack cloneItemStack() {
        ItemStack itemstack = new ItemStack(this.id, this.count, this.damage);

        if (this.tag != null) {
            itemstack.tag = (NBTTagCompound) this.tag.clone();
        }

        return itemstack;
    }

    public static boolean equals(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? (itemstack.tag == null && itemstack1.tag != null ? false : itemstack.tag == null || itemstack.tag.equals(itemstack1.tag)) : false);
    }

    public static boolean matches(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? itemstack.d(itemstack1) : false);
    }

    private boolean d(ItemStack itemstack) {
        return this.count != itemstack.count ? false : (this.id != itemstack.id ? false : (this.damage != itemstack.damage ? false : (this.tag == null && itemstack.tag != null ? false : this.tag == null || this.tag.equals(itemstack.tag))));
    }

    public boolean doMaterialsMatch(ItemStack itemstack) {
        return this.id == itemstack.id && this.damage == itemstack.damage;
    }

    public String a() {
        return Item.itemsList[this.id].c(this);
    }

    public static ItemStack b(ItemStack itemstack) {
        return itemstack == null ? null : itemstack.cloneItemStack();
    }

    public String toString() {
        return this.count + "x" + Item.itemsList[this.id].getName() + "@" + this.damage;
    }

    public void a(net.minecraft.src.World world, Entity entity, int i, boolean flag) {
        if (this.b > 0) {
            --this.b;
        }

        Item.itemsList[this.id].a(this, world, entity, i, flag);
    }

    public void a(net.minecraft.src.World world, EntityPlayer entityhuman, int i) {
        entityhuman.a(StatisticList.D[this.id], i);
        Item.itemsList[this.id].d(this, world, entityhuman);
    }

    public boolean c(ItemStack itemstack) {
        return this.id == itemstack.id && this.count == itemstack.count && this.damage == itemstack.damage;
    }

    public int m() {
        return this.getItem().a(this);
    }

    public EnumAnimation n() {
        return this.getItem().b(this);
    }

    public void b(net.minecraft.src.World world, EntityPlayer entityhuman, int i) {
        this.getItem().a(this, world, entityhuman, i);
    }

    public boolean hasTag() {
        return this.tag != null;
    }

    public NBTTagCompound getTag() {
        return this.tag;
    }

    public NBTTagList getEnchantments() {
        return this.tag == null ? null : (NBTTagList) this.tag.get("ench");
    }

    public void setTag(NBTTagCompound nbttagcompound) {
        this.tag = nbttagcompound;
    }

    public boolean u() {
        return !this.getItem().k(this) ? false : !this.hasEnchantments();
    }

    public void addEnchantment(Enchantment enchantment, int i) {
        if (this.tag == null) {
            this.setTag(new NBTTagCompound());
        }

        if (!this.tag.hasKey("ench")) {
            this.tag.set("ench", new NBTTagList("ench"));
        }

        NBTTagList nbttaglist = (NBTTagList) this.tag.get("ench");
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setShort("id", (short) enchantment.effectId);
        nbttagcompound.setShort("lvl", (short) ((byte) i));
        nbttaglist.add(nbttagcompound);
    }

    public boolean hasEnchantments() {
        return this.tag != null && this.tag.hasKey("ench");
    }
}
