package net.minecraft.src;

import java.io.IOException;

class TcpWriterThread extends Thread {

    final TcpConnection a;

    TcpWriterThread(TcpConnection networkmanager, String s) {
        super(s);
        this.a = networkmanager;
    }

    public void run() {
        TcpConnection.b.getAndIncrement();

        try {
            while (TcpConnection.a(this.a)) {
                boolean flag;

                for (flag = false; TcpConnection.d(this.a); flag = true) {
                    ;
                }

                try {
                    if (flag && TcpConnection.e(this.a) != null) {
                        TcpConnection.e(this.a).flush();
                    }
                } catch (IOException ioexception) {
                    if (!TcpConnection.f(this.a)) {
                        TcpConnection.a(this.a, (Exception) ioexception);
                    }

                    // ioexception.printStackTrace(); // CraftBukkit - Don't spam console on unexpected disconnect
                }

                try {
                    sleep(2L);
                } catch (InterruptedException interruptedexception) {
                    ;
                }
            }
        } finally {
            TcpConnection.b.getAndDecrement();
        }
    }
}
