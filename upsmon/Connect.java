/* Decompiler 499ms, total 1530ms, lines 152 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;

public class Connect {
   static DatagramSocket server = null;
   public static int model = 0;
   public static int lowbat = 0;
   public static String temp = "";
   public static BufferedReader br = null;
   public static FileOutputStream fo2 = null;

   public static void main(String[] var0) {
      readfile();
      begin(Label.connect);
   }

   public static void readfile() {
      try {
         br = new BufferedReader(new InputStreamReader(new FileInputStream("EXT" + UPSMON.separaST + "SYSTEM" + UPSMON.separaST + "UPS_Init.txt")));
         boolean var0 = false;

         while((temp = br.readLine()) != null) {
            int var2;
            if (temp.startsWith("[CON")) {
               var2 = temp.indexOf("=");
               Label.connect = Integer.parseInt(temp.substring(var2 + 1, temp.length()));
            } else if (temp.startsWith("[RS232")) {
               var2 = temp.indexOf("=");
               UPSMON.rs232 = temp.substring(var2 + 1, temp.length());
            } else if (temp.startsWith("[IP")) {
               var2 = temp.indexOf("=");
               UPSMON.snmp = temp.substring(var2 + 1, temp.length());
            } else if (temp.startsWith("[Shutdown")) {
               var2 = temp.indexOf("=");
               UPSMON.shutdowntype = temp.substring(var2 + 1, temp.length());
            } else if (temp.startsWith("[OS D")) {
               var2 = temp.indexOf("=");
               Label.osdelay = Integer.parseInt(temp.substring(var2 + 1, temp.length()));
            } else if (temp.startsWith("[OS Shutdown Battery C")) {
               var2 = temp.indexOf("=");
               Label.oscapacity = Integer.parseInt(temp.substring(var2 + 1, temp.length()));
            } else if (temp.startsWith("[OS Shutdown Battery B")) {
               var2 = temp.indexOf("=");
               Label.osminutes = Integer.parseInt(temp.substring(var2 + 1, temp.length()));
            } else if (temp.startsWith("[Low")) {
               var2 = temp.indexOf("=");
               UPSMON.lowbatdown = temp.substring(var2 + 1, temp.length());
            } else if (temp.startsWith("[User Command]")) {
               var2 = temp.indexOf("=");
               UPSMON.usercmd = temp.substring(var2 + 1, temp.length());
            } else if (temp.startsWith("[User Command Sec")) {
               var2 = temp.indexOf("=");
               UPSMON.usercmdsec = temp.substring(var2 + 1, temp.length());
            } else if (temp.startsWith("[UPS OutletOFF Delay")) {
               var2 = temp.indexOf("=");
               Label.outletoffsec = temp.substring(var2 + 1, temp.length());
            } else if (temp.startsWith("[UPS")) {
               var2 = temp.indexOf("=");
               Label.upsdelay = Integer.parseInt(temp.substring(var2 + 1, temp.length()));
            }
         }

         br.close();
      } catch (Exception var1) {
         System.err.println("Exception : Class Connect : readfile : e1 : " + var1);
      }

   }

   public static void begin(int var0) {
      try {
         String var1 = "";
         byte[] var2 = var1.getBytes();
         fo2 = new FileOutputStream(Label.fileget, false);
         fo2.write(var2, 0, 0);
         fo2 = new FileOutputStream(Label.fileRt, false);
         fo2.write(var2, 0, 0);
         fo2 = new FileOutputStream(Label.fileCountDown, false);
         fo2.write(var2, 0, 0);
         fo2.close();
      } catch (Exception var11) {
         System.err.println("Exception : Class Connect : begin() : e2 : " + var11);
      }

      if (var0 == 1) {
         try {
            new ConCOM1();
            model = 6;
         } catch (Exception var10) {
            System.err.println("Exception : Class Connect : begin() : e1 : " + var10);
         }
      } else if (var0 == 2) {
         try {
            new ConUSB();
         } catch (Exception var9) {
            System.err.println("Exception : Class Connect : begin() : e2 : " + var9);
         }
      } else if (var0 == 3) {
         try {
            server = new DatagramSocket(162);
            ConSNMPSet var13 = new ConSNMPSet(server);
            var13.start();
            ConSNMPGet var16 = new ConSNMPGet(server);
            var16.start();
            model = 5;
         } catch (Exception var8) {
            System.err.println("Exception : Class Connect : begin() : e3 : " + var8);
         }
      } else if (var0 == 4) {
         try {
            server = new DatagramSocket(2600);
            ConPROSet var14 = new ConPROSet(server);
            var14.start();
            ConPROGet var17 = new ConPROGet(server);
            var17.start();
            model = 8;
         } catch (Exception var7) {
            System.err.println("Exception : Class Connect : begin() : e4 : " + var7);
         }
      }

      PROServer var15 = new PROServer();
      var15.start();

      try {
         File var18 = new File(Label.fileWeb);
         if (var18.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(var18)));
            String var3 = "";

            for(int var4 = 0; (var3 = br.readLine()) != null; ++var4) {
               if (var4 == 0 && var3.startsWith("[Enable]=tru")) {
                  try {
                     String var5 = "EXT" + UPSMON.separaST + "apache" + UPSMON.separaST + "bin" + UPSMON.separaST + "startup.sh";
                     Runtime.getRuntime().exec(var5);
                  } catch (Exception var6) {
                     System.err.println("Exception : Class Connect : begin() : ef :" + var6);
                  }
               }
            }
         }
      } catch (Exception var12) {
         System.err.println("Exception : Class Connect : begin() : ee : " + var12);
      }

   }
}
