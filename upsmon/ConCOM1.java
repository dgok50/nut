/* Decompiler 13ms, total 1090ms, lines 73 */
import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

public class ConCOM1 {
   CommPortIdentifier thecom = null;
   InputStream in;
   OutputStream out;
   static boolean BO1200 = false;
   static CommPort com = null;

   public ConCOM1() {
      Connect.readfile();

      try {
         this.thecom = CommPortIdentifier.getPortIdentifier(UPSMON.rs232);
         com = this.thecom.open("UPSMON1200", 10);
      } catch (PortInUseException var7) {
         System.err.println("Exception : Class ConCOM1 : ConCOM1() : This port has been occupied by the other program : e1 : " + var7);
      } catch (NoSuchPortException var8) {
         System.err.println("Exception : Class ConCOM1 : ConCOM1() : There is no serial port name " + UPSMON.rs232 + " : e2 : " + var8);
      } catch (Exception var9) {
         System.err.println("Exception : Class ConCOM1 : ConCOM1() : e3 : " + var9);
      }

      try {
         com.disableReceiveThreshold();
         com.disableReceiveTimeout();
         com.disableReceiveFraming();
         com.setInputBufferSize(64);
         com.setOutputBufferSize(64);
         SerialPort var1 = (SerialPort)com;
         var1.setSerialPortParams(1200, 8, 1, 0);
         var1.setFlowControlMode(0);
         var1.setDTR(true);
         this.out = com.getOutputStream();
         ConCOM1Set var2 = new ConCOM1Set(this.out, this);
         var2.start();
         this.in = com.getInputStream();
         ConCOM1Get var3 = new ConCOM1Get(this.in, this);
         var3.start();
         Switch var4 = new Switch(this);
         var4.start();
      } catch (UnsupportedCommOperationException var5) {
         com.close();
         System.err.println("Exception : Class ConCOM1 : ConCOM1() : e1 : " + var5);
      } catch (Exception var6) {
         com.close();
         System.err.println("Exception : Class ConCOM1 : ConCOM1() : e2 : " + var6);
      }

   }

   public void switcher() {
      new ConCOM2(this);
   }

   public void closeStream() {
      try {
         this.in.close();
         this.out.close();
      } catch (Exception var2) {
         System.err.println("Exception : Class StartCOM1 : closeStream() : e1 :" + var2);
      }

   }
}
