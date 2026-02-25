package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/* JADX INFO: compiled from: Status.java */
/* JADX INFO: loaded from: StatusAPP.class */
class StatusAPP extends Thread {
    static int meter_ipvoltage = 0;
    static int meter_opvoltage = 0;
    static int meter_loadlevel = 0;
    static int meter_ipfrequency = 0;
    static int meter_opfrequency = 0;
    static int meter_temperature = 0;
    static int meter_batterylevel = 0;
    static int meter_remainingtime = 0;
    static String b7 = "0";
    static String b6 = "0";
    static String b5 = "0";
    static String b4 = "0";
    static String b3 = "0";
    static String b2 = "0";
    static String b1 = "0";
    static String b0 = "0";
    static String separator = System.getProperty("file.separator");
    static String[] text = new String[9];
    static String temp = "";
    public static File f1 = new File("EXT" + separator + "SYSTEM" + separator + "UPS_get.txt");
    public static File f2 = new File("EXT" + separator + "STATUS" + separator + "Battery_Backup_Time.txt");
    public static File f3 = new File("EXT" + separator + "STATUS" + separator + "UPS_Update_Time.txt");
    public static File f4 = new File("EXT" + separator + "STATUS" + separator + "Battery_Voltage.txt");
    public static File f5 = new File("EXT" + separator + "STATUS" + separator + "UPS_Connect.txt");
    public static File f6 = new File("EXT" + separator + "STATUS" + separator + "UPS_Output_Power.txt");
    public static File f7 = new File("EXT" + separator + "STATUS" + separator + "Battery_Test_Result.txt");
    public static File f8 = new File("EXT" + separator + "STATUS" + separator + "Battery_Last_Test.txt");
    public static File f9 = new File("EXT" + separator + "STATUS" + separator + "Battery_Power_Times.txt");
    public static File fa = new File("EXT" + separator + "STATUS" + separator + "Last_Battery_Power_Start.txt");
    public static File fb = new File("EXT" + separator + "STATUS" + separator + "Last_Battery_Power_Stop.txt");
    public static File fc = new File("EXT" + separator + "STATUS" + separator + "Linux_Count_Down.txt");
    public static File fd = new File("EXT" + separator + "STATUS" + separator + "Env_Temperature.txt");
    public static File fe = new File("EXT" + separator + "STATUS" + separator + "Env_Humidity.txt");
    public static File ff = new File("EXT" + separator + "CONFIG" + separator + "Battery_Last_Replace.txt");
    public static File fg = new File("EXT" + separator + "STATUS" + separator + "Battery_Test_Times.txt");
    public static BufferedReader br = null;

    StatusAPP() {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                if (f1.exists()) {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
                    temp = br.readLine();
                    if (temp != null && temp.length() >= 44) {
                        caculate(temp);
                    }
                    br.close();
                }
                sleep(1500L);
            } catch (Exception e) {
                System.out.println("Exception : Class StatusAPP : run() : e : " + e);
                return;
            }
        }
    }

    public static void caculate(String str) {
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(str);
            int i = 0;
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                if (strNextToken.indexOf("(") != -1) {
                    i = 0;
                }
                text[i] = strNextToken;
                i++;
                if (i >= 8) {
                    break;
                }
            }
            if (text[7].length() == 8) {
                b7 = text[7].substring(0, 1);
                b6 = text[7].substring(1, 2);
                b5 = text[7].substring(2, 3);
                b4 = text[7].substring(3, 4);
                b3 = text[7].substring(4, 5);
                b2 = text[7].substring(5, 6);
                b1 = text[7].substring(6, 7);
                b0 = text[7].substring(7, 8);
            }
            if (text[4].indexOf(":") > 0) {
                text[4] = text[4].replace(':', '0');
            }
            meter_ipfrequency = (int) Double.parseDouble(text[4]);
            if (text[6].indexOf(":") > 0) {
                text[6] = text[6].replace(':', '0');
            }
            meter_temperature = (int) Double.parseDouble(text[6]);
            if (text[3].indexOf(":") > 0) {
                text[3] = text[3].replace(':', '0');
            }
            meter_loadlevel = (int) Double.parseDouble(text[3]);
            if (text[2].indexOf(":") > 0) {
                text[2] = text[2].replace(':', '0');
            }
            meter_opvoltage = (int) Double.parseDouble(text[2]);
            text[0] = text[0].substring(1, text[0].length());
            if (text[0].indexOf(":") > 0) {
                text[0] = text[0].replace(':', '0');
            }
            meter_ipvoltage = (int) Double.parseDouble(text[0]);
            if (text[5].indexOf(".") == -1) {
                meter_batterylevel = (int) Double.parseDouble(text[5]);
            }
            System.out.println();
            if (b7.equals("1")) {
                System.out.println("Power Status          : Battery Power");
            } else {
                System.out.println("Power Status          : AC Utility Power");
            }
            System.out.println("Input Voltage         : " + meter_ipvoltage + " Volts");
            System.out.println("Input Frequency       : " + meter_ipfrequency + " Hz");
            System.out.println("Output Voltage        : " + meter_opvoltage + " Volts");
            try {
                if (f6.exists()) {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(f6)));
                    String line = br.readLine();
                    if (line != null && line.length() >= 1) {
                        System.out.println("Output Power          : " + line + " Watt");
                    }
                    br.close();
                }
            } catch (Exception e) {
                System.err.println("Exception : Class Status : run() : e2 : " + e);
            }
            if (b5.equals("1")) {
                if (b3.equals("0")) {
                    System.out.println("Ups Status            : Bypass");
                } else if (meter_opvoltage > meter_ipvoltage) {
                    System.out.println("Ups Status            : AVR Boost");
                } else {
                    System.out.println("Ups Status            : AVR Buck");
                }
            } else if (b4.equals("1")) {
                System.out.println("Ups Status            : UPS Failed");
            } else {
                System.out.println("Ups Status            : Normal");
            }
            System.out.println("Ups Load              : " + meter_loadlevel + " Percent");
            if (meter_temperature < 45) {
                System.out.println("Ups Temperature       : " + meter_temperature + " C");
            }
            if (b0.equals("0")) {
                System.out.println("Ups Beeper            : OFF");
            } else {
                System.out.println("Ups Beeper            : ON");
            }
            if (b6.equals("1")) {
                System.out.println("Battery Status        : Low Battery");
            } else if (b3.equals("2")) {
                System.out.println("Battery Status        : Battery Fail");
            } else if (b2.equals("1")) {
                System.out.println("Battery Status        : Discharge (Battery Test)");
            } else if (b7.equals("1")) {
                System.out.println("Battery Status        : Discharge");
            } else if (meter_batterylevel >= 98) {
                System.out.println("Battery Status        : Normal");
            } else {
                System.out.println("Battery Status        : Charge");
            }
            if (meter_batterylevel > 3) {
                System.out.println("Battery Capacity      : " + meter_batterylevel + " Percent");
            }
            try {
                if (f4.exists()) {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(f4)));
                    String line2 = br.readLine();
                    if (line2 != null && line2.length() > 1) {
                        System.out.println("Battery Voltage       : " + line2 + " Volts");
                    }
                    br.close();
                }
            } catch (Exception e2) {
                System.err.println("Exception : Class Status : run() : e2 : " + e2);
            }
            if (fg.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fg)));
                String line3 = br.readLine();
                if (line3 != null) {
                    if (Integer.parseInt(line3) <= 1) {
                        System.out.println("Battery Test Times    : " + line3 + " Time");
                    } else {
                        System.out.println("Battery Test Times    : " + line3 + " Times");
                    }
                }
                br.close();
            }
            if (f8.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f8)));
                String line4 = br.readLine();
                if (line4 != null && line4.length() > 1) {
                    System.out.println("Battery Last Test     : " + line4);
                }
                br.close();
            }
            if (f7.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f7)));
                String line5 = br.readLine();
                if (line5 != null && line5.length() > 1) {
                    System.out.println("Battery Test Result   : " + line5);
                }
                br.close();
            }
            if (f9.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f9)));
                String line6 = br.readLine();
                if (line6 != null && line6.length() >= 1) {
                    if (Integer.parseInt(line6) <= 1) {
                        System.out.println("Battery Power Times   : " + line6 + " Time");
                    } else {
                        System.out.println("Battery Power Times   : " + line6 + " Times");
                    }
                }
                br.close();
            }
            if (fa.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fa)));
                String line7 = br.readLine();
                if (line7 != null && line7.length() >= 1) {
                    System.out.println("Battery Power Start   : " + line7);
                }
                br.close();
            }
            if (fb.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fb)));
                String line8 = br.readLine();
                if (line8 != null && line8.length() >= 1) {
                    System.out.println("Battery Power End     : " + line8);
                }
                br.close();
            }
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                String line9 = br.readLine();
                if (line9 != null && line9.length() >= 1) {
                    int i2 = Integer.parseInt(line9.substring(9, 13));
                    int i3 = Integer.parseInt(line9.substring(14, 16));
                    int i4 = Integer.parseInt(line9.substring(17, 19));
                    int i5 = ((Status.year - i2) * 12) + (Status.month - i3);
                    if (i5 >= 0) {
                        if (i5 == 0) {
                            int i6 = Status.Date - i4;
                            if (i6 >= 0) {
                                if (i6 <= 1) {
                                    System.out.println("Battery Age           : 1 Day");
                                } else {
                                    System.out.println("Battery Age           : " + (Status.Date - i4) + " Days");
                                }
                            }
                        } else if (i5 == 1) {
                            System.out.println("Battery Age           : 1 Month");
                        } else {
                            System.out.println("Battery Age           : " + i5 + " Months");
                        }
                    }
                }
                br.close();
            }
            if (fd.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fd)));
                String line10 = br.readLine();
                if (line10 != null && line10.length() >= 1) {
                    System.out.println("Ambient Temperature   : " + line10 + " C");
                }
                br.close();
            }
            if (fe.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fe)));
                String line11 = br.readLine();
                if (line11 != null && line11.length() >= 1) {
                    System.out.println("Ambient Humidity      : " + line11 + " Percent");
                }
                br.close();
            }
            System.out.println("Upsmon Pro Linux      : V1.43");
            if (f5.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f5)));
                String line12 = br.readLine();
                if (line12 != null) {
                    if (line12.equals("1")) {
                        System.out.println("Upsmon Pro Status     : Monitoring");
                    } else {
                        System.out.println("Upsmon Pro Connect    : Disconnect");
                    }
                }
                br.close();
            }
            if (f3.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f3)));
                String line13 = br.readLine();
                if (line13 != null && line13.length() > 1) {
                    System.out.println("Upsmon Pro Update     : " + line13);
                }
                br.close();
            }
            if (fc.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fc)));
                String line14 = br.readLine();
                if (line14 != null && line14.length() >= 1) {
                    System.out.println("Upsmon Pro is going to shutdown Linux after " + line14 + " seconds");
                }
                br.close();
            }
            System.out.println();
        } catch (Exception e3) {
        }
    }
}
