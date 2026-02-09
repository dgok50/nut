/* Decompiler 200ms, total 526ms, lines 175 */
import ConUSB.1;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbServices;

public class ConUSB {
   public static UsbConfiguration config = null;
   public static UsbInterface theInterface = null;
   public static Thread USBAPP = null;
   public static String temp = null;
   public static FileOutputStream fo = null;

   public ConUSB() {
      try {
         this.conusb();
      } catch (Exception var2) {
      }

   }

   public void conusb() throws UsbException, InterruptedException, IOException {
      UsbDevice var1 = findUPS();
      if (var1 == null) {
         System.out.println("UPSMON : There is no any UPS USBDevice attached!");
         System.out.println("UPSMON : Exit");
      } else {
         config = var1.getActiveUsbConfiguration();
         theInterface = config.getUsbInterface((byte)0);
         theInterface.claim(new 1(this));
         List var2 = theInterface.getUsbEndpoints();
         Iterator var3 = var2.iterator();
         UsbEndpoint var4 = null;

         while(var3.hasNext()) {
            var4 = (UsbEndpoint)var3.next();
            byte var5 = var4.getDirection();
            if (var5 != 0) {
               if (Connect.model == 3) {
                  File var6 = new File(Label.fileupsmodel);
                  if (var6.exists()) {
                     USBAPP = new ConUSB4(var4, var1, this);
                     USBAPP.setPriority(10);
                  } else {
                     USBAPP = new ConUSB3(var4, var1, this);
                     USBAPP.setPriority(10);
                  }
               }

               USBAPP.start();
            }
         }
      }

   }

   private static UsbDevice findUPS() throws UsbException, InterruptedException, IOException {
      UsbServices var0 = UsbHostManager.getUsbServices();
      UsbHub var1 = var0.getRootUsbHub();
      return listDevices(var1);
   }

   private static UsbDevice listDevices(UsbHub var0) throws UnsupportedEncodingException, UsbException {
      short var1 = 3487;
      short var2 = 162;
      short var3 = 164;
      short var4 = 166;
      short var5 = 167;
      short var6 = 163;
      byte var7 = 4;
      List var8 = var0.getAttachedUsbDevices();
      Iterator var9 = var8.iterator();

      while(var9.hasNext()) {
         UsbDevice var10 = (UsbDevice)var9.next();
         UsbDeviceDescriptor var11 = var10.getUsbDeviceDescriptor();
         short var12 = var11.idVendor();
         if (var12 == var1) {
            short var13 = var11.idProduct();
            if (var13 == var2) {
               try {
                  temp = "0xa2";
                  fo = new FileOutputStream(Label.fileUsbId, false);
                  fo.write(temp.getBytes(), 0, temp.length());
                  temp = "UPS-IMP/IMD";
                  fo = new FileOutputStream(Label.fileModel, false);
                  fo.write(temp.getBytes(), 0, temp.length());
               } catch (Exception var15) {
                  System.err.println(var15);
               }

               Connect.model = 1;
               return var10;
            }

            if (var13 == var3) {
               try {
                  temp = "0xa4";
                  fo = new FileOutputStream(Label.fileUsbId, false);
                  fo.write(temp.getBytes(), 0, temp.length());
                  temp = "UPS-WOW";
                  fo = new FileOutputStream(Label.fileModel, false);
                  fo.write(temp.getBytes(), 0, temp.length());
               } catch (Exception var16) {
                  System.err.println(var16);
               }

               Connect.model = 1;
               return var10;
            }

            if (var13 == var4) {
               try {
                  temp = "0xa6";
                  fo = new FileOutputStream(Label.fileUsbId, false);
                  fo.write(temp.getBytes(), 0, temp.length());
                  temp = "UPS-BNT";
                  fo = new FileOutputStream(Label.fileModel, false);
                  fo.write(temp.getBytes(), 0, temp.length());
               } catch (Exception var17) {
                  System.err.println(var17);
               }

               Connect.model = 1;
               return var10;
            }

            if (var13 == var5) {
               try {
                  temp = "0xa7";
                  fo = new FileOutputStream(Label.fileUsbId, false);
                  fo.write(temp.getBytes(), 0, temp.length());
                  temp = "UPS-King";
                  fo = new FileOutputStream(Label.fileModel, false);
                  fo.write(temp.getBytes(), 0, temp.length());
               } catch (Exception var18) {
                  System.err.println(var18);
               }

               Connect.model = 1;
               return var10;
            }

            if (var13 == var6) {
               Connect.model = 2;
               return var10;
            }

            if (var13 == var7) {
               Connect.model = 3;
               return var10;
            }
         } else if (var10.isUsbHub()) {
            UsbDevice var19 = listDevices((UsbHub)var10);
            if (var19 != null) {
               return var19;
            }
         }
      }

      return null;
   }
}
