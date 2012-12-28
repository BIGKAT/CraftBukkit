package net.minecraft.server;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit

public class ItemBlock extends Item {

    private int id;
    private int localId;

    public ItemBlock(int i) {
        super(i);
        this.id = i + 256;
        this.c(Block.byId[i + 256].a(2));
        isDefaultTexture = Block.byId[i + 256].isDefaultTexture; // Forge
    }

    public int g() {
        return this.id;
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        int i1 = world.getTypeId(i, j, k);

        if (i1 == Block.SNOW.id) {
            l = 1;
        } else if (i1 != Block.VINE.id && i1 != Block.LONG_GRASS.id && i1 != Block.DEAD_BUSH.id && (Block.byId[i1] == null || !Block.byId[i1].isBlockReplaceable(world, i, j, k))) { // Forge
            if (l == 0) {
                --j;
            }

            if (l == 1) {
                ++j;
            }

            if (l == 2) {
                --k;
            }

            if (l == 3) {
                ++k;
            }

            if (l == 4) {
                --i;
            }

            if (l == 5) {
                ++i;
            }
        }

        if (itemstack.count == 0) {
            return false;
        } else if (!entityhuman.a(i, j, k, l, itemstack)) {
            return false;
        } else if (j == 255 && Block.byId[this.id].material.isBuildable()) {
            return false;
            // CraftBukkit start
        }

        int id = this.id;
        if (l == -1 && itemstack.getItem() instanceof ItemStep) {
            if (this.id == Block.STEP.id) {
                id = Block.DOUBLE_STEP.id;
            } else if (this.id == Block.WOOD_STEP.id) {
                id = Block.WOOD_DOUBLE_STEP.id;
            }
        }

        if (id != this.id || world.mayPlace(this.id, i, j, k, false, l, entityhuman)) {
            Block block = Block.byId[id];
            int j1 = this.filterData(itemstack.getData());
            int k1 = Block.byId[this.id].getPlacedData(world, i, j, k, l, f, f1, f2, j1);

            CraftBlockState replacedBlockState = CraftBlockState.getBlockState(world, i, j, k);

            world.suppressPhysics = true;
            world.setTypeIdAndData(i, j, k, id, k1);
            org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, replacedBlockState, clickedX, clickedY, clickedZ);
            localId = world.getTypeId(i, j, k);
            int data = world.getData(i, j, k);
            replacedBlockState.update(true);
            world.suppressPhysics = false;

            if (event.isCancelled() || !event.canBuild()) {
                return true;
            }
                    // CraftBukkit end

            if(placeBlockAt(itemstack, entityhuman, world, i, j, k, l, f, f1, f2, data)) { // Forge
                world.makeSound((double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), block.stepSound.getPlaceSound(), (block.stepSound.getVolume1() + 1.0F) / 2.0F, block.stepSound.getVolume2() * 0.8F);
                --itemstack.count;
            }

            return true;
        } else {
            return false;
        }
    }

    public String d(ItemStack itemstack) {
        return Block.byId[this.id].a();
    }

    public String getName() {
        return Block.byId[this.id].a();
    }

    // Forge start
    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param stack The item stack that was used to place the block. This can be changed inside the method.
     * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side The side the player (or machine) right-clicked on.
     */
    public boolean placeBlockAt(ItemStack stack, EntityHuman player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
       if (world.setTypeIdAndData(x, y, z, localId, metadata)) {
           if (world.getTypeId(x, y, z) == localId) {
               Block.byId[localId].postPlace(world, x, y, z, player);
              Block.byId[this.id].postPlace(world, x, y, z, metadata);
           }
           return true;
       }

       return false;
    }
    // Forge end
}
