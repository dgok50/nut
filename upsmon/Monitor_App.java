package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/* JADX INFO: compiled from: Monitor.java */
/* JADX INFO: loaded from: Monitor_App.class */
class Monitor_App extends Thread {
    public static FileOutputStream fo2;
    public static int sleepINT = 3000;
    public static long[] conrecord = {0, 0, 0};
    public static File f = new File("EXT" + UPSMON.separaST + "SYSTEM" + UPSMON.separaST + "UPS_get.txt");
    public static File f1 = new File("EXT" + UPSMON.separaST + "CONFIG" + UPSMON.separaST + "Mail_Test.txt");
    public static File f2 = new File("EXT" + UPSMON.separaST + "CONFIG" + UPSMON.separaST + "Exit.txt");
    public static File f3 = new File("EXT" + UPSMON.separaST + "STATUS" + UPSMON.separaST + "UPS_Connect.txt");
    public static BufferedReader br = null;
    public static String temp = "-1";
    public static String separaST = System.getProperty("file.separator");
    public static boolean a = true;
    public static boolean flag = false;
    public static boolean skipBO = false;

    Monitor_App() {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (a) {
            try {
                monitor();
                sleep(sleepINT);
            } catch (Exception e) {
                System.err.println("Exception : Class Monitor : run() : e : " + e);
                return;
            }
        }
    }

    public void restart() {
        try {
            Runtime.getRuntime().exec("EXT" + separaST + "Execute" + separaST + "RESTART");
            sleep(3000L);
            System.out.println("");
            System.out.println("");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void monitor() {
        try {
            if (f.exists()) {
                conrecord[1] = f.lastModified();
                if (conrecord[0] == 0) {
                    conrecord[0] = conrecord[1];
                    conrecord[2] = 0;
                } else if (conrecord[0] == conrecord[1]) {
                    long[] jArr = conrecord;
                    jArr[2] = jArr[2] + 1;
                    if (conrecord[2] >= 120) {
                        try {
                            fo2 = new FileOutputStream(f3, false);
                            temp = "2";
                            fo2.write(temp.getBytes(), 0, 1);
                            fo2.close();
                        } catch (Exception e) {
                            System.err.println("Exception : Class Monitor : monitor() : e3 : " + e);
                        }
                        if (flag) {
                            System.out.println("UPSMON : Lost Connection");
                            Record record = UPSMON.RecordAPP;
                            Record.set(new String("Lost_Connection"));
                            flag = false;
                            ConCOM2Get.data[24] = 1;
                            ConCOM1Get.data[24] = 1;
                            ConUSB3.data[24] = 1;
                            ConUSB4.data[24] = 1;
                        }
                    } else if (conrecord[2] % 11 == 10) {
                        restart();
                        sleep(70000L);
                    }
                } else {
                    conrecord[0] = conrecord[1];
                    conrecord[2] = 0;
                    skipBO = false;
                    try {
                        sleepINT = 3000;
                        fo2 = new FileOutputStream(f3, false);
                        temp = "1";
                        fo2.write(temp.getBytes(), 0, 1);
                        fo2.close();
                    } catch (Exception e2) {
                    }
                    if (!flag) {
                        flag = true;
                    }
                    ConCOM2Get.data[24] = 0;
                    ConCOM1Get.data[24] = 0;
                    ConUSB3.data[24] = 0;
                    ConUSB4.data[24] = 0;
                }
            }
            if (f1.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
                temp = br.readLine();
                if (temp.equals("1")) {
                    temp = "mailtest";
                    Record.write(temp, temp, temp);
                    temp = "0";
                    fo2 = new FileOutputStream(f1, false);
                    fo2.write(temp.getBytes(), 0, 1);
                    fo2.close();
                }
                sleep(1000L);
            }
            if (f2.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));
                temp = br.readLine();
                if (temp.equals("1")) {
                    f2.delete();
                    System.out.println("UPSMON : Exit");
                    Record record2 = UPSMON.RecordAPP;
                    Record.set(new String("UPSMON_Exit"));
                    System.exit(0);
                }
            }
        } catch (Exception e3) {
            System.err.println("Exception : Class Monitor : monitor() : e : " + e3);
        }
    }
}
