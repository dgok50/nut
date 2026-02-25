package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import javax.usb.UsbDevice;
import javax.usb.UsbEndpoint;
import javax.usb.UsbPipe;

/* JADX INFO: loaded from: ConUSB3.class */
class ConUSB3 extends Thread {
    public static int Input_Voltage;
    public static int Output_Voltage;
    public static int Load;
    public static int Input_Frequency;
    public static int Battery_Capacity;
    public static int Temperature;
    public static String temp;
    public static Calendar cal1;
    public static Calendar cal2;
    ConUSB conusbAPP;
    public static UsbEndpoint endpoint = null;
    public static UsbDevice upsUsbDevice = null;
    public static int Remaining_Time = 0;
    public static int bit7 = 0;
    public static int bit6 = 0;
    public static int bit5 = 0;
    public static int bit4 = 0;
    public static int bit3 = 0;
    public static int bit2 = 0;
    public static int bit1 = 0;
    public static int bit0 = 1;
    public static int controloff = 0;
    public static int delay = 2000;
    public static int a = 14;
    public static int timeINT = 0;
    public static int num = 0;
    public static boolean soundBO = false;
    public static boolean green_modeBO = false;
    public static boolean deeptestBO = false;
    public static boolean testflag = false;
    public static boolean battery_power = false;
    public static boolean battery_power_stop = false;
    public static boolean init_rebootBO = true;
    public static File ff = null;
    public static File f0 = new File(Label.fileConfigBatTest);
    public static File f1 = new File(Label.fileConfigUpsDown);
    public static File f2 = new File(Label.fileConfigOutlet1);
    public static File f3 = new File(Label.fileConfigOutlet2);
    public static File f4 = new File(Label.fileConfigUpsOff);
    public static File f5 = new File(Label.fileConfigSilence);
    public static File f_exit = new File(Label.fileConfigExit);
    public static String filelog = "";
    public static String Q1 = "";
    public static String c1 = "";
    public static String temp2 = null;
    public static BufferedReader br = null;
    public static FileOutputStream fo = null;
    public static Calendar cal3 = null;
    public static UsbPipe pipe = null;
    static int aa = 0;
    static int bb = 0;
    static byte[] data = {80, 67, 77, 82, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 69, 78, 68};

    public ConUSB3(UsbEndpoint usbEndpoint, UsbDevice usbDevice, ConUSB conUSB) {
        this.conusbAPP = null;
        Connect.model = 3;
        endpoint = usbEndpoint;
        upsUsbDevice = usbDevice;
        this.conusbAPP = conUSB;
        data[16] = 0;
        try {
            if (!f0.exists()) {
                temp2 = " ";
                fo = new FileOutputStream(f0, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
            ff = new File(Label.fileBatTestTimes);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Bat_test_times = Integer.parseInt(br.readLine());
                br.close();
            }
            ff = new File(Label.fileBatResult);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Battestresult = br.readLine();
                br.close();
            }
            ff = new File(Label.fileBatTimes);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Bat_times = Integer.parseInt(br.readLine());
                br.close();
            }
            ff = new File(Label.fileBatStart);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Batpowerstart = br.readLine();
                br.close();
            }
            ff = new File(Label.fileBatStop);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Batpowerend = br.readLine();
                br.close();
            }
            if (!f2.exists()) {
                temp = "0";
                fo = new FileOutputStream(f2, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            if (!f3.exists()) {
                temp = "0";
                fo = new FileOutputStream(f3, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            if (!f4.exists()) {
                temp = "0";
                fo = new FileOutputStream(f4, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            if (!f5.exists()) {
                temp2 = "0";
                fo = new FileOutputStream(f5, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
            ff = new File(Label.fileConfigMailTest);
            if (!ff.exists()) {
                temp = "0";
                fo = new FileOutputStream(ff, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            ff = new File(Label.fileStart);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.UpsStartTime = br.readLine();
                br.close();
            }
            ff = new File(Label.fileHost);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Hostname = br.readLine();
                br.close();
            }
            temp2 = "POWERCOM";
            fo = new FileOutputStream(Label.fileCompany, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
            temp2 = "0004";
            fo = new FileOutputStream(Label.fileUsbId, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
        } catch (Exception e) {
            System.err.println("Exception : Class ConUSB3 : ConUSB3() : e0 : " + e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x043c, code lost:
    
        defpackage.ConUSB3.a = -1;
        r0 = defpackage.UPSMON.RecordAPP;
        defpackage.Record.set(new java.lang.String("UPS_turn_off_after_1_minute"));
        java.lang.System.out.println();
        java.lang.System.out.println("UPSMON : UPS will turn off its power after 1 minute !!");
        java.lang.System.out.println("UPSMON : UPS will turn ON its power again after 1.5 minute !!");
        r0.setData(new byte[]{15, 9, 0});
        defpackage.ConUSB3.upsUsbDevice.syncSubmit(r0);
        r0.setComplete(false);
        sleep(5000);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x049a, code lost:
    
        java.lang.Runtime.getRuntime().exec("EXT" + defpackage.UPSMON.separaST + "Execute" + defpackage.UPSMON.separaST + "User_Cmd");
        sleep(5000);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x04d3, code lost:
    
        r62 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x04d5, code lost:
    
        java.lang.System.err.println("Exception : Class ConUSB3 : run() : e1 : " + r62);
     */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            Method dump skipped, instruction units count: 5817
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.ConUSB3.run():void");
    }

    public static void read() {
        try {
            if (f0.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f0)));
                while (true) {
                    String line = br.readLine();
                    temp2 = line;
                    if (line == null) {
                        break;
                    }
                    if (temp2.equals("1")) {
                        a = 1;
                    } else if (temp2.equals("2")) {
                        a = 2;
                    } else if (temp2.equals("0")) {
                        a = 3;
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f0, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
            }
            if (f1.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
                while (true) {
                    String line2 = br.readLine();
                    temp2 = line2;
                    if (line2 == null) {
                        break;
                    }
                    if (temp2.equals("1")) {
                        a = 4;
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f1, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
            }
            if (f4.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f4)));
                while (true) {
                    String line3 = br.readLine();
                    temp = line3;
                    if (line3 == null) {
                        break;
                    }
                    if (!temp.equals("0")) {
                        controloff = Integer.parseInt(temp);
                        a = 5;
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f4, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
            }
            if (f2.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));
                while (true) {
                    String line4 = br.readLine();
                    temp = line4;
                    if (line4 == null) {
                        break;
                    }
                    if (temp.equals("1")) {
                        a = 6;
                    } else if (temp.equals("2")) {
                        a = 7;
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f2, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
            }
            if (f3.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f3)));
                while (true) {
                    String line5 = br.readLine();
                    temp = line5;
                    if (line5 == null) {
                        break;
                    }
                    if (temp.equals("1")) {
                        a = 8;
                    } else if (temp.equals("2")) {
                        a = 9;
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f3, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
            }
            if (f5.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f5)));
                while (true) {
                    String line6 = br.readLine();
                    temp2 = line6;
                    if (line6 == null) {
                        break;
                    }
                    if (temp2.equals("2")) {
                        a = 10;
                    } else if (temp2.equals("1")) {
                        a = 11;
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f5, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
            }
            if (f_exit.exists()) {
                temp2 = "1";
                fo = new FileOutputStream(f_exit, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("Exception : Class ConUSB3 : read() : e : " + e);
        }
    }
}
