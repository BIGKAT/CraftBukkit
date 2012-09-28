package org.bukkit.craftbukkit.entity;

import net.minecraft.src.EntityPainting;
import net.minecraft.src.EnumArt;
import net.minecraft.src.WorldServer;

import org.bukkit.Art;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;

public class CraftPainting extends CraftEntity implements Painting {

    public CraftPainting(CraftServer server, EntityPainting entity) {
        super(server, entity);
    }

    public Art getArt() {
        EnumArt art = getHandle().art;
        return CraftArt.NotchToBukkit(art);
    }

    public boolean setArt(Art art) {
        return setArt(art, false);
    }

    public boolean setArt(Art art, boolean force) {
        EntityPainting painting = this.getHandle();
        EnumArt oldArt = painting.art;
        EnumArt newArt = CraftArt.BukkitToNotch(art);
        painting.art = newArt;
        painting.setDirection(painting.direction);
        if (!force && !painting.onValidSurface()) {
            // Revert painting since it doesn't fit
            painting.art = oldArt;
            painting.setDirection(painting.direction);
            return false;
        }
        this.update();
        return true;
    }

    public BlockFace getAttachedFace() {
        return getFacing().getOppositeFace();
    }

    public void setFacingDirection(BlockFace face) {
        setFacingDirection(face, false);
    }

    public boolean setFacingDirection(BlockFace face, boolean force) {
        Block block = getLocation().getBlock().getRelative(getAttachedFace()).getRelative(face.getOppositeFace()).getRelative(getFacing());
        EntityPainting painting = getHandle();
        int x = painting.xPosition, y = painting.yPosition, z = painting.zPosition, dir = painting.direction;
        painting.xPosition = block.getX();
        painting.yPosition = block.getY();
        painting.zPosition = block.getZ();
        switch (face) {
        case EAST:
        default:
            getHandle().setDirection(0);
            break;
        case NORTH:
            getHandle().setDirection(1);
            break;
        case WEST:
            getHandle().setDirection(2);
            break;
        case SOUTH:
            getHandle().setDirection(3);
            break;
        }
        if (!force && !painting.onValidSurface()) {
            // Revert painting since it doesn't fit
            painting.xPosition = x;
            painting.yPosition = y;
            painting.zPosition = z;
            painting.setDirection(dir);
            return false;
        }
        this.update();
        return true;
    }

    public BlockFace getFacing() {
        switch (this.getHandle().direction) {
        case 0:
        default:
            return BlockFace.EAST;
        case 1:
            return BlockFace.NORTH;
        case 2:
            return BlockFace.WEST;
        case 3:
            return BlockFace.SOUTH;
        }
    }

    private void update() {
        WorldServer world = ((CraftWorld) getWorld()).getHandle();
        EntityPainting painting = new EntityPainting(world);
        painting.xPosition = getHandle().xPosition;
        painting.yPosition = getHandle().yPosition;
        painting.zPosition = getHandle().zPosition;
        painting.art = getHandle().art;
        painting.setDirection(getHandle().direction);
        getHandle().setDead();
        getHandle().velocityChanged = true; // because this occurs when the painting is broken, so it might be important
        world.spawnEntityInWorld(painting);
        this.entity = painting;
    }

    @Override
    public EntityPainting getHandle() {
        return (EntityPainting) entity;
    }

    @Override
    public String toString() {
        return "CraftPainting{art=" + getArt() + "}";
    }

    public EntityType getType() {
        return EntityType.PAINTING;
    }
}
