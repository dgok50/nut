package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/* JADX INFO: compiled from: ConCOM1.java */
/* JADX INFO: loaded from: ConCOM1Set.class */
class ConCOM1Set extends Thread {
    ConCOM1 comApp;
    OutputStream out;
    public static int roopINT = 0;
    public static int a = 0;
    public static boolean soundBO = false;
    public static boolean green_modeBO = false;
    public static FileOutputStream fo = null;
    public static BufferedReader br = null;
    public static File ff = null;
    public static File f0 = new File(Label.fileConfigBatTest);
    public static File f1 = new File(Label.fileConfigUpsDown);
    public static File f4 = new File(Label.fileConfigUpsOff);
    public static File f5 = new File(Label.fileConfigSilence);
    public static File f_exit = new File(Label.fileConfigExit);
    public static String temp2 = "";
    public static String controloff = "";

    public ConCOM1Set(OutputStream outputStream, ConCOM1 conCOM1) {
        this.out = outputStream;
        this.comApp = conCOM1;
        try {
            ff = new File(Label.fileConfigInterval);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                temp2 = br.readLine();
                Label.ConfigIntervall = Integer.parseInt(temp2) * 1000;
                br.close();
            } else {
                temp2 = "2";
                fo = new FileOutputStream(ff, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
            ff = new File(Label.fileBatStart);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Batpowerstart = br.readLine();
                br.close();
            }
            ff = new File(Label.fileHost);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Hostname = br.readLine();
                br.close();
            }
            ff = new File(Label.fileBatResult);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Battestresult = br.readLine();
                br.close();
            }
            ff = new File(Label.fileBatStop);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Batpowerend = br.readLine();
                br.close();
            }
            ff = new File(Label.fileBatTimes);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Bat_times = Integer.parseInt(br.readLine());
                br.close();
            }
            ff = new File(Label.fileBatTestTimes);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Bat_test_times = Integer.parseInt(br.readLine());
                br.close();
            }
            ff = new File(Label.fileStart);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.UpsStartTime = br.readLine();
                br.close();
            }
            if (!f0.exists()) {
                temp2 = " ";
                fo = new FileOutputStream(f0, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
            if (!f4.exists()) {
                temp2 = "0";
                fo = new FileOutputStream(f4, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
        } catch (Exception e) {
            System.err.println("Exception : Class ConCOM1Set : ConCOM1Set()\t: e : " + e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:49:0x02f6, code lost:
    
        defpackage.ConCOM1Set.roopINT = -1;
     */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            Method dump skipped, instruction units count: 830
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.ConCOM1Set.run():void");
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
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f0, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
                f0.delete();
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
                        a = 2;
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f1, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
                f1.delete();
            }
            if (f4.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f4)));
                while (true) {
                    String line3 = br.readLine();
                    temp2 = line3;
                    if (line3 == null) {
                        break;
                    } else if (!temp2.equals("0")) {
                        controloff = temp2;
                        a = 5;
                    }
                }
                br.close();
                f4.delete();
            }
            if (ConCOM1Get.bit7 == 1) {
                if (f5.exists()) {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(f5)));
                    while (true) {
                        String line4 = br.readLine();
                        temp2 = line4;
                        if (line4 == null) {
                            break;
                        }
                        if (temp2.equals("2")) {
                            a = 4;
                        } else if (temp2.equals("1")) {
                            a = 3;
                        }
                    }
                    br.close();
                }
                f5.delete();
            }
            if (f_exit.exists()) {
                temp2 = "1";
                fo = new FileOutputStream(f_exit, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("Exception : Class ConCOM1 : read() : e : " + e);
        }
    }
}
