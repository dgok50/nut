package defpackage;

import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

/* JADX INFO: loaded from: ConCOM2.class */
public class ConCOM2 {
    ConCOM1 comport1App;
    CommPortIdentifier thecom;
    CommPort com;
    InputStream in;
    OutputStream out;

    public ConCOM2(ConCOM1 conCOM1) {
        this.thecom = null;
        this.com = null;
        Connect.model = 7;
        this.comport1App = conCOM1;
        try {
            this.thecom = CommPortIdentifier.getPortIdentifier(UPSMON.rs232);
            this.com = this.thecom.open("UPSMON2400", 10);
        } catch (PortInUseException e) {
            System.err.println("Exception : Class ConCOM2 : ConCOM2() : e1 : This Port had been occupied by the other program:" + e);
            this.com.close();
        } catch (NoSuchPortException e2) {
            System.err.println("Exception : Class ConCOM2 : ConCOM2() : e2 : CommPortIdentifier can't detect " + UPSMON.rs232 + " this port : " + e2);
            this.com.close();
        } catch (Exception e3) {
            System.err.println("Exception : Class ConCOM2 : ConCOM2() : e3 : Other Exceptions : " + e3);
        }
        try {
            this.com.disableReceiveThreshold();
            this.com.disableReceiveTimeout();
            this.com.disableReceiveFraming();
            this.com.setInputBufferSize(64);
            this.com.setOutputBufferSize(64);
            SerialPort serialPort = this.com;
            serialPort.setSerialPortParams(2400, 8, 1, 0);
            serialPort.setFlowControlMode(0);
            serialPort.setDTR(true);
            this.out = this.com.getOutputStream();
            new ConCOM2Set(this.out).start();
            this.in = this.com.getInputStream();
            new ConCOM2Get(this.in).start();
        } catch (UnsupportedCommOperationException e4) {
            this.com.close();
            System.err.println("Exception : Class ConCOM2 : ConCOM2() : e4 : UnsupportedCommOperationException :" + e4);
        } catch (Exception e5) {
            this.com.close();
            System.err.println("Exception : Class ConCOM2 : ConCOM2() : e5 : " + e5);
        }
    }
}
