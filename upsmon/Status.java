package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/* JADX INFO: loaded from: Status.class */
public class Status {
    static int year;
    static int month;
    static int Date = 0;
    public static File f1 = null;
    public static FileOutputStream fo2 = null;

    public static void main(String[] strArr) {
        copypath();
        try {
            new StatusAPP().start();
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(1);
            month = calendar.get(2) + 1;
            Date = calendar.get(5);
        } catch (Exception e) {
            System.err.println("Exception : Class Status : e : " + e);
        }
    }

    public static void copypath() {
        try {
            f1 = new File("pwd");
            if (f1.exists()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
                String str = "cd " + bufferedReader.readLine() + "\n";
                f1 = new File("../upsmon-pro-start");
                String str2 = (((str + "./path \n") + "./jre/bin/java UPSMON \n") + "./jre/bin/java -noverify Connect & \n") + "./jre/bin/java Monitor & \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str2.getBytes(), 0, str2.length());
                fo2.close();
                f1 = new File("../upsmon-pro-service");
                String str3 = ((str + "./path \n") + "./jre/bin/java -noverify Connect & \n") + "./jre/bin/java Monitor & \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str3.getBytes(), 0, str3.length());
                fo2.close();
                f1 = new File("../upsmon-pro-status");
                String str4 = (((str + "sudo sh -c 'echo 1 > /proc/sys/vm/drop_caches' \n") + "sudo sh -c 'echo 2 > /proc/sys/vm/drop_caches' \n") + "sudo sh -c 'echo 3 > /proc/sys/vm/drop_caches' \n") + "./jre/bin/java Status \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str4.getBytes(), 0, str4.length());
                fo2.close();
                f1 = new File("../upsmon-pro-stop");
                String str5 = str + "pwd > ./EXT/CONFIG/Exit.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str5.getBytes(), 0, str5.length());
                fo2.close();
                f1 = new File("../upsmon-pro-ups-reboot");
                String str6 = str + "./jre/bin/java Reboot \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str6.getBytes(), 0, str6.length());
                fo2.close();
                f1 = new File("../upsmon-pro-test-deep");
                String str7 = str + "echo 2 > ./EXT/CONFIG/Battery_Test.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str7.getBytes(), 0, str7.length());
                fo2.close();
                f1 = new File("../upsmon-pro-test-cancel");
                String str8 = str + "echo 0 > ./EXT/CONFIG/Battery_Test.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str8.getBytes(), 0, str8.length());
                fo2.close();
                f1 = new File("../upsmon-pro-test");
                String str9 = str + "echo 1 > ./EXT/CONFIG/Battery_Test.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str9.getBytes(), 0, str9.length());
                fo2.close();
                f1 = new File("../upsmon-pro-start-config");
                String str10 = str + "./jre/bin/java Config \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str10.getBytes(), 0, str10.length());
                fo2.close();
                f1 = new File("../upsmon-pro-restart");
                String str11 = ((((("#!/bin/bash \n" + str) + "cd .. \n") + "./upsmon-pro-stop \n") + "sleep 5 \n") + "./upsmon-pro-service \n") + "exit 0";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str11.getBytes(), 0, str11.length());
                fo2.close();
                f1 = new File("EXT/Execute/RESTART");
                String str12 = (((("#!/bin/bash \n" + str) + "cd ..\n") + "sleep 60 \n") + "./upsmon-pro-service2 \n") + "exit 0";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str12.getBytes(), 0, str12.length());
                fo2.close();
                f1 = new File("../upsmon-pro-outlet2-on");
                String str13 = str + "echo 1 > ./EXT/CONFIG/UPS_Outlet2.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str13.getBytes(), 0, str13.length());
                fo2.close();
                f1 = new File("../upsmon-pro-outlet2-off");
                String str14 = str + "echo 2 > ./EXT/CONFIG/UPS_Outlet2.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str14.getBytes(), 0, str14.length());
                fo2.close();
                f1 = new File("../upsmon-pro-outlet1-on");
                String str15 = str + "echo 1 > ./EXT/CONFIG/UPS_Outlet1.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str15.getBytes(), 0, str15.length());
                fo2.close();
                f1 = new File("../upsmon-pro-outlet1-off");
                String str16 = str + "echo 2 > ./EXT/CONFIG/UPS_Outlet1.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str16.getBytes(), 0, str16.length());
                fo2.close();
                f1 = new File("../upsmon-pro-mail-test");
                String str17 = str + "echo 1 > ./EXT/CONFIG/Mail_Test.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str17.getBytes(), 0, str17.length());
                fo2.close();
                f1 = new File("../upsmon-pro-mail");
                String str18 = str + "./jre/bin/java Mail \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str18.getBytes(), 0, str18.length());
                fo2.close();
                f1 = new File("../upsmon-pro-vmware");
                String str19 = str + "./jre/bin/java VMWare \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str19.getBytes(), 0, str19.length());
                fo2.close();
                f1 = new File("../upsmon-pro-log");
                String str20 = str + "./jre/bin/java Log \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str20.getBytes(), 0, str20.length());
                fo2.close();
                f1 = new File("../upsmon-pro-log-erase");
                String str21 = str + "rm -rif ./EXT/LOG \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str21.getBytes(), 0, str21.length());
                fo2.close();
                f1 = new File("../upsmon-pro-info");
                String str22 = str + "./jre/bin/java Info \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str22.getBytes(), 0, str22.length());
                fo2.close();
                f1 = new File("../upsmon-pro-event");
                String str23 = str + "./jre/bin/java Event \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str23.getBytes(), 0, str23.length());
                fo2.close();
                f1 = new File("../upsmon-pro-event-erase");
                String str24 = str + "rm -rif ./EXT/EVENT \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str24.getBytes(), 0, str24.length());
                fo2.close();
                f1 = new File("../upsmon-pro-beep-switch");
                String str25 = str + "echo 1 > ./EXT/CONFIG/UPS_Beeper.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str25.getBytes(), 0, str25.length());
                fo2.close();
                f1 = new File("../upsmon-pro-ups-type");
                String str26 = str + "echo 2 > ./EXT/CONFIG/UPS_USB_1200.txt \n";
                fo2 = new FileOutputStream(f1, false);
                fo2.write(str26.getBytes(), 0, str26.length());
                fo2.close();
                bufferedReader.close();
            }
        } catch (Exception e) {
            System.err.println("Exception : Class Status : copypath() : e2 : " + e);
        }
    }
}
