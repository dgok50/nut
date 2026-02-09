/* Decompiler 16ms, total 385ms, lines 16 */
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;

class ConUSB$1 implements UsbInterfacePolicy {
   // $FF: synthetic field
   final ConUSB this$0;

   ConUSB$1(ConUSB var1) {
      this.this$0 = var1;
   }

   public boolean forceClaim(UsbInterface var1) {
      return true;
   }
}
