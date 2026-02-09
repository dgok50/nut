/* Decompiler 11ms, total 302ms, lines 60 */
import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

public class ConCOM2 {
   ConCOM1 comport1App;
   CommPortIdentifier thecom = null;
   CommPort com = null;
   InputStream in;
   OutputStream out;

   public ConCOM2(ConCOM1 var1) {
      Connect.model = 7;
      this.comport1App = var1;

      try {
         this.thecom = CommPortIdentifier.getPortIdentifier(UPSMON.rs232);
         this.com = this.thecom.open("UPSMON2400", 10);
      } catch (PortInUseException var7) {
         System.err.println("Exception : Class ConCOM2 : ConCOM2() : e1 : This Port had been occupied by the other program:" + var7);
         this.com.close();
      } catch (NoSuchPortException var8) {
         System.err.println("Exception : Class ConCOM2 : ConCOM2() : e2 : CommPortIdentifier can't detect " + UPSMON.rs232 + " this port : " + var8);
         this.com.close();
      } catch (Exception var9) {
         System.err.println("Exception : Class ConCOM2 : ConCOM2() : e3 : Other Exceptions : " + var9);
      }

      try {
         this.com.disableReceiveThreshold();
         this.com.disableReceiveTimeout();
         this.com.disableReceiveFraming();
         this.com.setInputBufferSize(64);
         this.com.setOutputBufferSize(64);
         SerialPort var2 = (SerialPort)this.com;
         var2.setSerialPortParams(2400, 8, 1, 0);
         var2.setFlowControlMode(0);
         var2.setDTR(true);
         this.out = this.com.getOutputStream();
         ConCOM2Set var3 = new ConCOM2Set(this.out);
         var3.start();
         this.in = this.com.getInputStream();
         ConCOM2Get var4 = new ConCOM2Get(this.in);
         var4.start();
      } catch (UnsupportedCommOperationException var5) {
         this.com.close();
         System.err.println("Exception : Class ConCOM2 : ConCOM2() : e4 : UnsupportedCommOperationException :" + var5);
      } catch (Exception var6) {
         this.com.close();
         System.err.println("Exception : Class ConCOM2 : ConCOM2() : e5 : " + var6);
      }

   }
}
