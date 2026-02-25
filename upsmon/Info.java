package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: Info.class */
public class Info {
    public static int a;
    public static int b;
    public static int c;
    public static BufferedReader br = null;
    public static String temp = "";
    public static String poll_interval = "";
    public static String community = "";
    public static String firmware = "";
    public static String usbid = "";
    public static String separator = System.getProperty("file.separator");
    public static String[] line = new String[12];
    public static File f1 = new File("EXT" + separator + "STATUS" + separator + "UPS_Company.txt");
    public static File f2 = new File("EXT" + separator + "STATUS" + separator + "UPS_Model.txt");
    public static File f3 = new File("EXT" + separator + "STATUS" + separator + "UPS_Firmware.txt");
    public static File f4 = new File("EXT" + separator + "CONFIG" + separator + "Rating_Input_Voltage.txt");
    public static File f5 = new File("EXT" + separator + "CONFIG" + separator + "Rating_Output_Voltage.txt");
    public static File f6 = new File("EXT" + separator + "SYSTEM" + separator + "UPS_Init.txt");
    public static File f7 = new File("EXT" + separator + "CONFIG" + separator + "UPS_Polling_Interval.txt");
    public static File f8 = new File("EXT" + separator + "CONFIG" + separator + "UPS_Community.txt");
    public static File f9 = new File("EXT" + separator + "STATUS" + separator + "SNMP_Firmware.txt");
    public static File fa = new File("EXT" + separator + "CONFIG" + separator + "Rating_Battery_Voltage.txt");
    public static File fb = new File("EXT" + separator + "CONFIG" + separator + "Rating_Battery_Frequency.txt");
    public static File fc = new File("EXT" + separator + "STATUS" + separator + "Usb_VID.txt");
    static String rs232 = "";
    static String snmp = "";
    static String shutdowntype = "";
    static String lowbatdown = "";
    static String usercmd = "";
    static String usercmdsec = "";
    static String outletoffsec = "0";
    static int connect = 0;
    static int osdelay = 0;
    static int oscapacity = 0;
    static int osminutes = 0;
    static int upsdelay = 0;
    public static int d = 0;

    public static void main(String[] strArr) {
        try {
            System.out.println();
            if (f1.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
                temp = br.readLine();
                if (temp != null && temp.length() > 1) {
                    System.out.println("Ups Company                              : " + temp);
                }
            } else {
                System.out.println("Ups Company                              : Powercome");
            }
            if (f2.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));
                temp = br.readLine();
                if (temp != null && temp.length() > 1) {
                    System.out.println("Ups Model                                : " + temp);
                }
            }
            if (f3.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f3)));
                temp = br.readLine();
                if (temp != null && temp.length() > 1) {
                    System.out.println("Ups Firmware                             : " + temp);
                }
            }
            if (f4.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f4)));
                temp = br.readLine();
                if (temp != null && temp.length() > 1) {
                    System.out.println("Rating Input Voltage                     : " + temp + " Volts");
                }
            }
            if (f5.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f5)));
                temp = br.readLine();
                if (temp != null && temp.length() > 1) {
                    System.out.println("Rating Output Voltage                    : " + temp + " Volts");
                }
            }
            if (fa.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fa)));
                temp = br.readLine();
                if (temp != null && temp.length() > 1) {
                    System.out.println("Rating Battery Voltage                   : " + temp + " Volts");
                }
            }
            if (fb.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fb)));
                temp = br.readLine();
                if (temp != null && temp.length() > 1) {
                    System.out.println("Rating Frequency                         : " + temp + " Hz");
                }
            }
            if (f7.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f7)));
                temp = br.readLine();
                if (temp != null && temp.length() >= 1) {
                    poll_interval = temp;
                }
            }
            if (fc.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fc)));
                temp = br.readLine();
                if (temp != null && temp.length() > 1) {
                    usbid = temp;
                }
            }
            if (f6.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f6)));
                while (true) {
                    String line2 = br.readLine();
                    temp = line2;
                    if (line2 == null) {
                        break;
                    }
                    if (temp.startsWith("[CON")) {
                        connect = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                        if (connect == 1) {
                            System.out.println("Upsmon Pro Connect                       : RS232");
                            c = 1;
                        } else if (connect == 2) {
                            System.out.println("Upsmon Pro Connect                       : Usb Port");
                            c = 2;
                        } else if (connect == 3) {
                            System.out.println("Upsmon Pro Connect                       : SNMP-Card");
                            c = 3;
                        } else if (connect == 4) {
                            System.out.println("Upsmon Pro Connect                       : Upsmon Pro Master");
                            c = 4;
                        }
                    } else if (temp.startsWith("[RS232")) {
                        rs232 = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[IP")) {
                        snmp = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[Shutdown")) {
                        shutdowntype = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[OS D")) {
                        osdelay = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    } else if (temp.startsWith("[OS Shutdown Battery C")) {
                        oscapacity = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    } else if (temp.startsWith("[OS Shutdown Battery B")) {
                        osminutes = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    } else if (temp.startsWith("[Low")) {
                        lowbatdown = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[User Command]")) {
                        usercmd = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[User Command Sec")) {
                        usercmdsec = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[UPS OutletOFF Delay")) {
                        outletoffsec = temp.substring(temp.indexOf("=") + 1, temp.length());
                    } else if (temp.startsWith("[UPS")) {
                        upsdelay = Integer.parseInt(temp.substring(temp.indexOf("=") + 1, temp.length()));
                    }
                }
                br.close();
            }
            if (c == 1) {
                System.out.println("Upsmon Pro RS232 Port Name and Path      : " + rs232);
                System.out.println("Upsmon Pro Polling Interval              : " + poll_interval + " Seconds");
                System.out.println("Linux shutdown delay seconds             : " + osdelay + " Seconds");
                System.out.println("Linux shutdown if battery capacity %     : " + oscapacity + " Percent");
                System.out.println("Linux shutdown if battery backup minutes : " + osminutes + " Minutes");
                System.out.println("Ups shutdown Delay                       : " + upsdelay + " Minutes");
                if (usercmd.equals("1")) {
                    System.out.println("Execute user command seconds             : " + usercmdsec + " Seconds");
                }
            } else if (c == 2) {
                System.out.println("Upsmon Pro Polling Interval              : " + poll_interval + " Seconds");
                System.out.println("Upsmon Pro Usb vendor & product ID       : 0d9f & " + usbid);
                System.out.println("Upsmon Pro Usb Type                      : usbhid-ups");
                System.out.println("Linux shutdown delay seconds             : " + osdelay + " Seconds");
                System.out.println("Linux shutdown if battery capacity %     : " + oscapacity + " Percent");
                System.out.println("Linux shutdown if battery backup minutes : " + osminutes + " Minutes");
                System.out.println("Ups shutdown Delay                       : " + upsdelay + " Minutes");
                if (usercmd.equals("1")) {
                    System.out.println("Execute user command seconds             : " + usercmdsec + " Seconds");
                }
            } else if (c == 3) {
                if (f8.exists()) {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(f8)));
                    temp = br.readLine();
                    if (temp != null) {
                        community = temp;
                    }
                }
                if (f9.exists()) {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(f9)));
                    temp = br.readLine();
                    if (temp != null) {
                        firmware = temp;
                    }
                }
                System.out.println("SNMP-Card IP                             : " + snmp);
                System.out.println("SNMP-Card firmware                       : " + firmware);
                System.out.println("SNMP-Card Community                      : " + community);
                System.out.println("Linux shutdown delay seconds             : " + osdelay + " Seconds");
                System.out.println("Linux shutdown if battery capacity %     : " + oscapacity + " Percent");
                System.out.println("Linux shutdown if battery backup minutes : " + osminutes + " Minutes");
                System.out.println("Ups shutdown Delay                       : " + upsdelay + " Minutes");
                System.out.println("Ups outlet off Delay                     : " + outletoffsec + " Seconds");
                if (usercmd.equals("1")) {
                    System.out.println("Execute user command seconds             : " + usercmdsec + " Seconds");
                }
            } else if (c == 4) {
                System.out.println("Upsmon Pro Master IP                     : " + snmp);
                System.out.println("Polling Interval                         : " + poll_interval + " Seconds");
                System.out.println("Linux shutdown delay seconds             : " + osdelay + " Seconds");
                System.out.println("Linux shutdown if battery capacity %     : " + oscapacity + " Percent");
                System.out.println("Linux shutdown if battery backup minutes : " + osminutes + " Minutes");
            }
        } catch (Exception e) {
            System.err.println("Exception : Class Info : run() : e2 : " + e);
        }
        System.out.println();
    }
}
