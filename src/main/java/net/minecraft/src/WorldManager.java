package net.minecraft.src;

import java.util.Iterator;

import net.minecraft.src.Entity;
import net.minecraft.server.IWorldAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Packet55BlockBreakAnimation;
import net.minecraft.server.Packet61WorldEvent;
import net.minecraft.server.Packet62NamedSoundEffect;

public class WorldManager implements IWorldAccess {

    private MinecraftServer server;
    public net.minecraft.src.WorldServer world; // CraftBukkit - private -> public

    public WorldManager(MinecraftServer minecraftserver, net.minecraft.src.WorldServer worldserver) {
        this.server = minecraftserver;
        this.world = worldserver;
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {}

    public void a(Entity entity) {
        this.world.getTracker().track(entity);
    }

    public void b(Entity entity) {
        this.world.getTracker().untrackEntity(entity);
    }

    public void a(String s, double d0, double d1, double d2, float f, float f1) {
        // CraftBukkit - this.world.dimension
        this.server.getServerConfigurationManager().sendPacketNearby(d0, d1, d2, f > 1.0F ? (double) (16.0F * f) : 16.0D, this.world.dimension, new Packet62NamedSoundEffect(s, d0, d1, d2, f, f1));
    }

    public void a(int i, int j, int k, int l, int i1, int j1) {}

    public void a(int i, int j, int k) {
        this.world.getPlayerManager().flagDirty(i, j, k);
    }

    public void b(int i, int j, int k) {}

    public void a(String s, int i, int j, int k) {}

    public void a(EntityPlayer entityhuman, int i, int j, int k, int l, int i1) {
        // CraftBukkit - this.world.dimension
        this.server.getServerConfigurationManager().sendPacketNearby(entityhuman, (double) j, (double) k, (double) l, 64.0D, this.world.dimension, new Packet61WorldEvent(i, j, k, l, i1));
    }

    public void a(int i, int j, int k, int l, int i1) {
        Iterator iterator = this.server.getServerConfigurationManager().playerEntityList.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            if (entityplayer != null && entityplayer.worldObj == this.world && entityplayer.entityId != i) {
                double d0 = (double) j - entityplayer.posX;
                double d1 = (double) k - entityplayer.posY;
                double d2 = (double) l - entityplayer.posZ;

                if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
                    entityplayer.serverForThisPlayer.sendPacketToPlayer(new Packet55BlockBreakAnimation(i, j, k, l, i1));
                }
            }
        }
    }
}
