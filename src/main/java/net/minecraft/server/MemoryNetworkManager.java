package net.minecraft.server;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.server.INetworkManager;
import net.minecraft.server.NetHandler;
import net.minecraft.server.Packet;
import cpw.mods.fml.common.network.FMLNetworkHandler;

public class MemoryNetworkManager implements INetworkManager {

   private static final SocketAddress a = new InetSocketAddress("127.0.0.1", 0);
   private final List b = Collections.synchronizedList(new ArrayList());
   private MemoryNetworkManager c;
   private NetHandler d;
   private boolean e = false;
   private String f = "";
   private Object[] g;
   private boolean h = false;


   public MemoryNetworkManager(NetHandler var1) {
      this.d = var1;
   }

   public void a(NetHandler var1) {
      this.d = var1;
   }

   public void queue(Packet var1) {
      if(!this.e) {
         this.c.b(var1);
      }
   }

   public void a() {}

   public void b() {
      int var1 = 2500;

      while(var1-- >= 0 && !this.b.isEmpty()) {
         Packet var2 = (Packet)this.b.remove(0);
         var2.handle(this.d);
      }

      if(this.b.size() > var1) {
         System.out.println("Memory connection overburdened; after processing 2500 packets, we still have " + this.b.size() + " to go!");
      }

      if(this.e && this.b.isEmpty()) {
         this.d.a(this.f, this.g);
         FMLNetworkHandler.onConnectionClosed(this, this.d.getPlayerH());
      }

   }

   public SocketAddress getSocketAddress() {
      return a;
   }

   public void d() {
      this.e = true;
   }

   public void a(String var1, Object ... var2) {
      this.e = true;
      this.f = var1;
      this.g = var2;
   }

   public int e() {
      return 0;
   }

   public void b(Packet var1) {
      String var2 = this.d.a()?">":"<";
      if(var1.a_() && this.d.b()) {
         var1.handle(this.d);
      } else {
         this.b.add(var1);
      }

   }

}
