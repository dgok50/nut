package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: UPSMON.class */
public class UPSMON {
    public static String separaST = System.getProperty("file.separator");
    public static String connect = "0";
    public static String rs232 = "";
    public static String snmp = "";
    public static String shutdowntype = "";
    public static String osdelay = "";
    public static String oscapacity = "";
    public static String osminutes = "";
    public static String lowbatdown = "";
    public static String usercmd = "0";
    public static String usercmdsec = "10";
    public static String upsdelay = "";
    public static File f1 = null;
    public static FileOutputStream fo2 = null;
    public static BufferedReader br = null;
    public static Record RecordAPP = new Record();
    public static Connect ConnectAPP = null;

    public static void main(String[] strArr) {
        new UPSMON(strArr);
        try {
            fo2 = new FileOutputStream(Label.fileConnect, false);
            fo2.write("0".getBytes(), 0, 1);
            fo2.close();
        } catch (Exception e) {
            System.err.println("Exception : Class UPSMON : main() : e2 : " + e);
        }
        System.exit(0);
    }

    private UPSMON(String[] strArr) {
        makedirectory();
        judge(portal(strArr));
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
                    stringBuffer.append("[User Command Seconds]=10\n");
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
                    if (connect.equals("2") || connect.equals("4")) {
                        System.out.print(" f. Would you need to reset                   (default : N  ) : ");
                    } else {
                        System.out.print(" g. Would you need to reset                   (default : N  ) : ");
                    }
                    br = new BufferedReader(new InputStreamReader(System.in));
                    String line4 = br.readLine();
                    System.out.flush();
                    if (line4.length() == 0) {
                        line4 = "N";
                    }
                    System.out.println();
                    if (line4.equals("Y") || line4.equals("y")) {
                        stringBuffer.delete(0, stringBuffer.length());
                    } else {
                        String string = stringBuffer.toString();
                        fo2 = new FileOutputStream(Label.fileInit, false);
                        fo2.write(string.getBytes(), 0, string.length());
                        fo2.close();
                        br.close();
                        return connect;
                    }
                }
            } catch (Exception e) {
                System.err.println("Exception: Class UPSMON: main(): e1: " + e);
            }
        }
        return connect;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0038, code lost:
    
        defpackage.UPSMON.connect = r0.substring(r0.indexOf("=") + 1, r0.length());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void judge(java.lang.String r8) {
        /*
            r0 = r8
            java.lang.String r1 = "0"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L6d
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.lang.Exception -> L53
            r1 = r0
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Exception -> L53
            r3 = r2
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Exception -> L53
            r5 = r4
            java.lang.String r6 = defpackage.Label.fileInit     // Catch: java.lang.Exception -> L53
            r5.<init>(r6)     // Catch: java.lang.Exception -> L53
            r3.<init>(r4)     // Catch: java.lang.Exception -> L53
            r1.<init>(r2)     // Catch: java.lang.Exception -> L53
            defpackage.UPSMON.br = r0     // Catch: java.lang.Exception -> L53
            java.lang.String r0 = ""
            r9 = r0
            r0 = 0
            r10 = r0
        L29:
            java.io.BufferedReader r0 = defpackage.UPSMON.br     // Catch: java.lang.Exception -> L53
            java.lang.String r0 = r0.readLine()     // Catch: java.lang.Exception -> L53
            r1 = r0
            r9 = r1
            if (r0 == 0) goto L50
            r0 = r10
            if (r0 != 0) goto L29
            r0 = r9
            java.lang.String r1 = "="
            int r0 = r0.indexOf(r1)     // Catch: java.lang.Exception -> L53
            r10 = r0
            r0 = r9
            r1 = r10
            r2 = 1
            int r1 = r1 + r2
            r2 = r9
            int r2 = r2.length()     // Catch: java.lang.Exception -> L53
            java.lang.String r0 = r0.substring(r1, r2)     // Catch: java.lang.Exception -> L53
            defpackage.UPSMON.connect = r0     // Catch: java.lang.Exception -> L53
            goto L50
        L50:
            goto L6d
        L53:
            r9 = move-exception
            java.io.PrintStream r0 = java.lang.System.err
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r2 = r1
            r2.<init>()
            java.lang.String r2 = "Exception : Class UPSMON : judge() : e : "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r9
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.println(r1)
        L6d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.UPSMON.judge(java.lang.String):void");
    }

    public static void makedirectory() {
        String str;
        byte[] bytes = "".getBytes();
        f1 = new File("EXT" + separaST + "SYSTEM");
        if (!f1.exists()) {
            f1.mkdir();
        }
        f1 = new File("EXT" + separaST + "CONFIG");
        if (!f1.exists()) {
            f1.mkdir();
            Calendar calendar = Calendar.getInstance();
            int i = calendar.get(11);
            if (i < 10) {
                str = "0" + String.valueOf(i) + ":";
            } else {
                str = String.valueOf(i) + ":";
            }
            int i2 = calendar.get(12);
            if (i2 < 10) {
                str = str + "0";
            }
            String str2 = str + String.valueOf(i2) + ":";
            int i3 = calendar.get(13);
            if (i3 < 10) {
                str2 = str2 + "0";
            }
            String str3 = (str2 + String.valueOf(i3) + " ") + String.valueOf(calendar.get(1)) + "/";
            int i4 = calendar.get(2) + 1;
            if (i4 < 10) {
                str3 = str3 + "0";
            }
            String str4 = str3 + String.valueOf(i4) + "/";
            int i5 = calendar.get(5);
            if (i5 < 10) {
                str4 = str4 + "0";
            }
            String str5 = str4 + String.valueOf(i5);
            try {
                fo2 = new FileOutputStream(Label.fileBatLastReplace, false);
                fo2.write(str5.getBytes(), 0, str5.length());
                fo2 = new FileOutputStream(Label.fileStart, false);
                fo2.write(str5.getBytes(), 0, str5.length());
            } catch (Exception e) {
                System.err.println("Exception : Class UPSMON : makedirectory () : ee : " + e);
            }
        }
        f1 = new File("EXT" + separaST + "EVENT");
        if (!f1.exists()) {
            f1.mkdir();
        }
        f1 = new File("EXT" + separaST + "LOG");
        if (!f1.exists()) {
            f1.mkdir();
        }
        f1 = new File("EXT" + separaST + "STATUS");
        if (!f1.exists()) {
            f1.mkdir();
        }
        try {
            fo2 = new FileOutputStream(Label.fileConnect, false);
            fo2.write("-1".getBytes(), 0, 2);
            fo2.close();
            fo2 = new FileOutputStream(Label.fileInit, false);
            fo2.write(bytes, 0, 0);
            fo2.close();
        } catch (Exception e2) {
            System.err.println("Exception : Class UPSMON : makedirectory() : e2 : " + e2);
        }
    }
}
