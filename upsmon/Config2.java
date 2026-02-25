package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: Config2.class */
public class Config2 {
    public static String separaST = System.getProperty("file.separator");
    public static String connect = "0";
    public static String rs232 = "";
    public static String snmp = "";
    public static String shutdowntype = "";
    public static String osdelay = "";
    public static String oscapacity = "";
    public static String osminutes = "";
    public static String lowbatdown = "";
    public static String upsdelay = "";
    public static String usercmd = "0";
    public static String usercmdsec = "10";
    public static String pollinterval = "";
    public static File f1 = null;
    public static FileOutputStream fo2 = null;
    public static BufferedReader br = null;
    public static String fileConnect = "EXT/STATUS/UPS_Connect.txt";

    public static void main(String[] strArr) {
        new Config2(strArr);
        try {
            fo2 = new FileOutputStream(fileConnect, false);
            fo2.write("0".getBytes(), 0, 1);
            fo2.close();
        } catch (Exception e) {
            System.err.println("Exception : Class Config : main() : e2 : " + e);
        }
        System.exit(0);
    }

    private Config2(String[] strArr) {
        portal(strArr);
    }

    public static String portal(String[] strArr) {
        String line;
        String line2;
        String line3;
        boolean z = false;
        if (strArr.length != 0 && strArr[0].equals("default")) {
            z = true;
            Connect.readfile();
        }
        if (!z) {
            try {
                System.out.println();
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    int i = 0;
                    while (true) {
                        System.out.print(" a. UPS connection: 1.RS232  2.USB  3.SNMP-Card  4.UPSMON-PRO : ");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        connect = br.readLine();
                        System.out.flush();
                        if (connect.length() == 0) {
                            connect = "0";
                        } else {
                            i = Integer.parseInt(connect);
                        }
                        if (!connect.equals("0") && i <= 4) {
                            break;
                        }
                    }
                    stringBuffer.append("[CONNECT]=" + connect + "\n");
                    Label.connect = Integer.parseInt(connect);
                    if (connect.equals("3")) {
                        do {
                            System.out.print(" b. SNMP-Card IP address                                      : ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            line3 = br.readLine();
                            System.out.flush();
                            if (line3.length() == 0 || !Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b").matcher(line3).matches()) {
                                line3 = "0";
                            }
                        } while (line3.equals("0"));
                        stringBuffer.append("[IP]=" + line3 + "\n");
                    } else if (connect.equals("4")) {
                        do {
                            System.out.print(" b. UPSMON-PRO IP address                                     : ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            line2 = br.readLine();
                            System.out.flush();
                            if (line2.length() == 0 || !Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b").matcher(line2).matches()) {
                                line2 = "0";
                            }
                        } while (line2.equals("0"));
                        stringBuffer.append("[IP]=" + line2 + "\n");
                    } else if (connect.equals("1")) {
                        stringBuffer.append("[IP]=\n");
                        do {
                            System.out.print(" b. Name and path of serial port              (ex:/dev/ttyS0) : ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            line = br.readLine();
                            System.out.flush();
                            if (line.length() == 0) {
                                line = "0";
                            }
                        } while (line.equals("0"));
                        stringBuffer.append("[RS232]=" + line + "\n");
                    } else if (connect.equals("2")) {
                        stringBuffer.append("[IP]=\n");
                    }
                    stringBuffer.append("[Shutdown Type]=0\n");
                    if (connect.equals("2")) {
                        System.out.print(" b. Linux shutdown delay seconds              (default : 120) : ");
                    } else {
                        System.out.print(" c. Linux shutdown delay seconds              (default : 120) : ");
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));
                    osdelay = br.readLine();
                    System.out.flush();
                    if (osdelay.length() == 0) {
                        osdelay = "120";
                    }
                    stringBuffer.append("[OS Delay]=" + osdelay + "\n");
                    Label.osdelay = Integer.parseInt(osdelay);
                    if (connect.equals("2")) {
                        System.out.print(" c. Linux shutdown if battery capacity %      (default : 30 ) : ");
                    } else {
                        System.out.print(" d. Linux shutdown if battery capacity %      (default : 30 ) : ");
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));
                    oscapacity = br.readLine();
                    System.out.flush();
                    if (oscapacity.length() == 0) {
                        oscapacity = "30";
                    }
                    Label.oscapacity = Integer.parseInt(oscapacity);
                    stringBuffer.append("[OS Shutdown Battery Capacity]=" + oscapacity + "\n");
                    if (connect.equals("2")) {
                        System.out.print(" d. Linux shutdown if battery backup minutes  (default : 5  ) : ");
                    } else {
                        System.out.print(" e. Linux shutdown if battery backup minutes  (default : 5  ) : ");
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));
                    osminutes = br.readLine();
                    System.out.flush();
                    if (osminutes.length() == 0) {
                        osminutes = "5";
                    }
                    stringBuffer.append("[OS Shutdown Battery Backup Time]=" + osminutes + "\n");
                    Label.osminutes = Integer.parseInt(osminutes);
                    stringBuffer.append("[Low Battery Shutdown]=0\n");
                    stringBuffer.append("[User Command]=0\n");
                    if (connect.equals("4")) {
                        stringBuffer.append("[UPS Delay]=0\n");
                    } else {
                        if (connect.equals("2")) {
                            System.out.print(" e. Ups shutdown delay minutes                (default : 3  ) : ");
                        } else {
                            System.out.print(" f. Ups shutdown delay minutes                (default : 3  ) : ");
                        }
                        br = new BufferedReader(new InputStreamReader(System.in));
                        upsdelay = br.readLine();
                        System.out.flush();
                        if (upsdelay.length() == 0) {
                            upsdelay = "3";
                        }
                        stringBuffer.append("[UPS Delay]=" + upsdelay + "\n");
                    }
                    if (connect.equals("2")) {
                        System.out.print(" f. Ups polling interval seconds              (default : 2  ) : ");
                    } else if (connect.equals("4")) {
                        System.out.print(" e. Ups polling interval seconds              (default : 2  ) : ");
                    } else {
                        System.out.print(" g. Ups polling interval seconds              (default : 2  ) : ");
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));
                    pollinterval = br.readLine();
                    System.out.flush();
                    if (pollinterval.length() == 0) {
                        pollinterval = "2";
                    }
                    if (connect.equals("2")) {
                        System.out.print(" g. Execute user command: 0.No 1.Yes          (default : 0  ) : ");
                    } else if (connect.equals("4")) {
                        System.out.print(" f. Execute user command: 0.No 1.Yes          (default : 0  ) : ");
                    } else {
                        System.out.print(" h. Execute user command: 0.No 1.Yes          (default : 0  ) : ");
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));
                    usercmd = br.readLine();
                    System.out.flush();
                    if (usercmd.length() == 0) {
                        usercmd = "0";
                    }
                    stringBuffer.append("[User Command]=" + usercmd + "\n");
                    if (usercmd.equals("0")) {
                        usercmdsec = "10";
                        stringBuffer.append("[User Command Seconds]=" + usercmdsec + "\n");
                    } else {
                        if (connect.equals("2")) {
                            System.out.print(" h. Execute user command seconds              (default : 10 ) : ");
                        } else if (connect.equals("4")) {
                            System.out.print(" g. Execute user command seconds              (default : 10 ) : ");
                        } else {
                            System.out.print(" i. Execute user command seconds              (default : 10 ) : ");
                        }
                        br = new BufferedReader(new InputStreamReader(System.in));
                        usercmdsec = br.readLine();
                        System.out.flush();
                        if (usercmdsec.length() == 0) {
                            usercmdsec = "10";
                        }
                        stringBuffer.append("[User Command Seconds]=" + usercmdsec + "\n");
                    }
                    if (usercmd.equals("0")) {
                        if (connect.equals("2")) {
                            System.out.print(" h. Save configuration: 0.Yes 1.No            (default : 0  ) : ");
                        } else if (connect.equals("4")) {
                            System.out.print(" g. Save configuration: 0.Yes 1.No            (default : 0  ) : ");
                        } else {
                            System.out.print(" i. Save configuration: 0.Yes 1.No            (default : 0  ) : ");
                        }
                    } else if (connect.equals("2")) {
                        System.out.print(" i. Save configuration: 0.Yes 1.No            (default : 0  ) : ");
                    } else if (connect.equals("4")) {
                        System.out.print(" h. Save configuration: 0.Yes 1.No            (default : 0  ) : ");
                    } else {
                        System.out.print(" j. Save configuration: 0.Yes 1.No            (default : 0  ) : ");
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String line4 = br.readLine();
                    System.out.flush();
                    if (line4.length() == 0) {
                        line4 = "0";
                    }
                    System.out.println();
                    if (line4.equals("1")) {
                        stringBuffer.delete(0, stringBuffer.length());
                    } else {
                        String string = stringBuffer.toString();
                        fo2 = new FileOutputStream(Label.fileInit, false);
                        fo2.write(string.getBytes(), 0, string.length());
                        String str = pollinterval;
                        fo2 = new FileOutputStream(Label.fileConfigInterval, false);
                        fo2.write(str.getBytes(), 0, str.length());
                        fo2.close();
                        br.close();
                        return connect;
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception: Class Config: main(): e1: " + e);
            }
        }
        return connect;
    }
}
