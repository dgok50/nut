package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;

/* JADX INFO: loaded from: Connect.class */
public class Connect {
    static DatagramSocket server = null;
    public static int model = 0;
    public static int lowbat = 0;
    public static String temp = "";
    public static BufferedReader br = null;
    public static FileOutputStream fo2 = null;

    public static void main(String[] strArr) {
        readfile();
        begin(Label.connect);
    }

    public static void readfile() {
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("EXT" + UPSMON.separaST + "SYSTEM" + UPSMON.separaST + "UPS_Init.txt")));
            while (true) {
                String line = br.readLine();
                temp = line;
                if (line != null) {
                    if (temp.startsWith("[CON")) {
                        Label.connect = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    } else if (temp.startsWith("[RS232")) {
                        UPSMON.rs232 = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[IP")) {
                        UPSMON.snmp = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[Shutdown")) {
                        UPSMON.shutdowntype = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[OS D")) {
                        Label.osdelay = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    } else if (temp.startsWith("[OS Shutdown Battery C")) {
                        Label.oscapacity = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    } else if (temp.startsWith("[OS Shutdown Battery B")) {
                        Label.osminutes = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    } else if (temp.startsWith("[Low")) {
                        UPSMON.lowbatdown = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[User Command]")) {
                        UPSMON.usercmd = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[User Command Sec")) {
                        UPSMON.usercmdsec = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[UPS OutletOFF Delay")) {
                        Label.outletoffsec = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[UPS")) {
                        Label.upsdelay = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    }
                } else {
                    br.close();
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Exception : Class Connect : readfile : e1 : " + e);
        }
    }

    public static void begin(int i) {
        try {
            byte[] bytes = "".getBytes();
            fo2 = new FileOutputStream(Label.fileget, false);
            fo2.write(bytes, 0, 0);
            fo2 = new FileOutputStream(Label.fileRt, false);
            fo2.write(bytes, 0, 0);
            fo2 = new FileOutputStream(Label.fileCountDown, false);
            fo2.write(bytes, 0, 0);
            fo2.close();
        } catch (Exception e) {
            System.err.println("Exception : Class Connect : begin() : e2 : " + e);
        }
        if (i == 1) {
            try {
                new ConCOM1();
                model = 6;
            } catch (Exception e2) {
                System.err.println("Exception : Class Connect : begin() : e1 : " + e2);
            }
        } else if (i == 2) {
            try {
                new ConUSB();
            } catch (Exception e3) {
                System.err.println("Exception : Class Connect : begin() : e2 : " + e3);
            }
        } else if (i == 3) {
            try {
                server = new DatagramSocket(162);
                new ConSNMPSet(server).start();
                new ConSNMPGet(server).start();
                model = 5;
            } catch (Exception e4) {
                System.err.println("Exception : Class Connect : begin() : e3 : " + e4);
            }
        } else if (i == 4) {
            try {
                server = new DatagramSocket(2600);
                new ConPROSet(server).start();
                new ConPROGet(server).start();
                model = 8;
            } catch (Exception e5) {
                System.err.println("Exception : Class Connect : begin() : e4 : " + e5);
            }
        }
        new PROServer().start();
        try {
            File file = new File(Label.fileWeb);
            if (file.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                int i2 = 0;
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    if (i2 == 0 && line.startsWith("[Enable]=tru")) {
                        try {
                            Runtime.getRuntime().exec("EXT" + UPSMON.separaST + "apache" + UPSMON.separaST + "bin" + UPSMON.separaST + "startup.sh");
                        } catch (Exception e6) {
                            System.err.println("Exception : Class Connect : begin() : ef :" + e6);
                        }
                    }
                    i2++;
                }
            }
        } catch (Exception e7) {
            System.err.println("Exception : Class Connect : begin() : ee : " + e7);
        }
    }
}
