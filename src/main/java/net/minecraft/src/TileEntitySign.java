package net.minecraft.src;

import net.minecraft.server.*;

public class TileEntitySign extends TileEntity {

    public String[] signText = new String[] { "", "", "", ""};
    public int b = -1;
    public boolean isEditable = true; // CraftBukkit - privite -> public

    public TileEntitySign() {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setString("Text1", this.signText[0]);
        nbttagcompound.setString("Text2", this.signText[1]);
        nbttagcompound.setString("Text3", this.signText[2]);
        nbttagcompound.setString("Text4", this.signText[3]);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.isEditable = false;
        super.a(nbttagcompound);

        for (int i = 0; i < 4; ++i) {
            this.signText[i] = nbttagcompound.getString("Text" + (i + 1));
            if (this.signText[i].length() > 15) {
                this.signText[i] = this.signText[i].substring(0, 15);
            }
        }
    }

    public Packet e() {
        String[] astring = new String[4];

        // CraftBukkit start - limit sign text to 15 chars per line
        for (int i = 0; i < 4; ++i) {
            astring[i] = this.signText[i];

            if (this.signText[i].length() > 15) {
                astring[i] = this.signText[i].substring(0, 15);
            }
        }
        // CraftBukkit end

        return new Packet130UpdateSign(this.x, this.y, this.z, astring);
    }

    public boolean a() {
        return this.isEditable;
    }
}
