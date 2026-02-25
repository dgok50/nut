package defpackage;

import java.io.InputStream;
import java.io.OutputStream;
import javax.comm.CommPort;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

/* JADX INFO: loaded from: ConCOM1.class */
public class ConCOM1 {
    CommPortIdentifier thecom;
    InputStream in;
    OutputStream out;
    static boolean BO1200 = false;
    static CommPort com = null;

    public ConCOM1() {
        this.thecom = null;
        Connect.readfile();
        try {
            this.thecom = CommPortIdentifier.getPortIdentifier(UPSMON.rs232);
            com = this.thecom.open("UPSMON1200", 10);
        } catch (Exception e) {
            System.err.println("Exception : Class ConCOM1 : ConCOM1() : e3 : " + e);
        } catch (NoSuchPortException e2) {
            System.err.println("Exception : Class ConCOM1 : ConCOM1() : There is no serial port name " + UPSMON.rs232 + " : e2 : " + e2);
        } catch (PortInUseException e3) {
            System.err.println("Exception : Class ConCOM1 : ConCOM1() : This port has been occupied by the other program : e1 : " + e3);
        }
        try {
            com.disableReceiveThreshold();
            com.disableReceiveTimeout();
            com.disableReceiveFraming();
            com.setInputBufferSize(64);
            com.setOutputBufferSize(64);
            SerialPort serialPort = com;
            serialPort.setSerialPortParams(1200, 8, 1, 0);
            serialPort.setFlowControlMode(0);
            serialPort.setDTR(true);
            this.out = com.getOutputStream();
            new ConCOM1Set(this.out, this).start();
            this.in = com.getInputStream();
            new ConCOM1Get(this.in, this).start();
            new Switch(this).start();
        } catch (Exception e4) {
            com.close();
            System.err.println("Exception : Class ConCOM1 : ConCOM1() : e2 : " + e4);
        } catch (UnsupportedCommOperationException e5) {
            com.close();
            System.err.println("Exception : Class ConCOM1 : ConCOM1() : e1 : " + e5);
        }
    }

    public void switcher() {
        new ConCOM2(this);
    }

    public void closeStream() {
        try {
            this.in.close();
            this.out.close();
        } catch (Exception e) {
            System.err.println("Exception : Class StartCOM1 : closeStream() : e1 :" + e);
        }
    }
}
