package forge.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.server.DataWatcher;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MathHelper;
import forge.ISpawnHandler;
import forge.IThrowableEntity;
import forge.MinecraftForge;
import forge.NetworkMod;

public class PacketEntitySpawn extends ForgePacket
{
    public int modID;
    public int entityID;
    public int typeID;
    public int posX;
    public int posY;
    public int posZ;
    public byte yaw;
    public byte pitch;
    public byte yawHead;
    public int throwerID;
    public int speedX;
    public int speedY;
    public int speedZ;
    public Object metadata;
    private ISpawnHandler handler;

    public PacketEntitySpawn(){}
    public PacketEntitySpawn(Entity ent, NetworkMod mod, int type)
    {
        entityID = ent.id;

        posX = MathHelper.floor(ent.locX * 32D);
        posY = MathHelper.floor(ent.locY * 32D);
        posZ = MathHelper.floor(ent.locZ * 32D);

        typeID = type;
        modID = MinecraftForge.getModID(mod);

        yaw      = (byte)(ent.yaw * 256.0F / 360.0F);
        pitch    = (byte)(ent.pitch * 256.0F / 360.0F);
        yawHead  = (byte)(ent instanceof EntityLiving ? ((EntityLiving)ent).X * 256.0F / 360.0F : 0);
        metadata = ent.getDataWatcher();

        if (ent instanceof IThrowableEntity)
        {
            Entity owner = ((IThrowableEntity)ent).getThrower();
            throwerID = (owner == null ? ent.id : owner.id);
            double maxVel = 3.9D;
            double mX = ent.motX;
            double mY = ent.motY;
            double mZ = ent.motZ;
            if (mX < -maxVel) mX = -maxVel;
            if (mY < -maxVel) mY = -maxVel;
            if (mZ < -maxVel) mZ = -maxVel;
            if (mX >  maxVel) mX =  maxVel;
            if (mY >  maxVel) mY =  maxVel;
            if (mZ >  maxVel) mZ =  maxVel;
            speedX = (int)(mX * 8000D);
            speedY = (int)(mY * 8000D);
            speedZ = (int)(mZ * 8000D);
        }
        if (ent instanceof ISpawnHandler)
        {
            handler = (ISpawnHandler)ent;
        }
    }
    public void writeData(DataOutputStream data) throws IOException
    {
        data.writeInt(modID);
        data.writeInt(entityID);
        data.writeByte(typeID);
        data.writeInt(posX);
        data.writeInt(posY);
        data.writeInt(posZ);
        data.writeByte(yaw);
        data.writeByte(pitch);
        data.writeByte(yawHead);
        ((DataWatcher)metadata).a(data);
        data.writeInt(throwerID);
        if (throwerID != 0)
        {
            data.writeShort(speedX);
            data.writeShort(speedY);
            data.writeShort(speedZ);
        }
        if (handler != null)
        {
            handler.writeSpawnData(data);
        }
    }

    public void readData(DataInputStream data) throws IOException
    {
        modID     = data.readInt();
        entityID  = data.readInt();
        typeID    = data.readByte();
        posX      = data.readInt();
        posY      = data.readInt();
        posZ      = data.readInt();
        yaw       = data.readByte();
        pitch     = data.readByte();
        yawHead   = data.readByte();
        metadata  = DataWatcher.a(data);
        throwerID = data.readInt();
        if (throwerID != 0)
        {
            speedX = data.readShort();
            speedY = data.readShort();
            speedZ = data.readShort();
        }
    }
    @Override
    public int getID()
    {
        return ForgePacket.SPAWN;
    }
}
