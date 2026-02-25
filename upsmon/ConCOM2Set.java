package defpackage;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/* JADX INFO: compiled from: ConCOM2.java */
/* JADX INFO: loaded from: ConCOM2Set.class */
class ConCOM2Set extends Thread {
    public static int roopINT = 1;
    public static int a = 0;
    public static byte[] a1 = {68, 81, 49, 13};
    public static byte[] a2 = {81, 49, 13};
    public static byte[] F_command = {70, 13};
    public static byte[] OI = {79, 73, 13};
    public static byte[] Rt = {82, 116, 13};
    public static byte[] I = {73, 13};
    public static byte[] F = {70, 13};
    public static byte[] Yop = {89, 111, 112, 13};
    public static boolean a1BO = false;
    public static boolean a2BO = false;
    public static boolean soundBO = false;
    public static boolean green_modeBO = false;
    public static boolean rtBO = false;
    public static boolean YopBO = false;
    public static boolean OIBO = false;
    public static boolean Q1BO = false;
    public static boolean deeptestBO = false;
    public static boolean IBO = false;
    public static boolean FBO = false;
    public static BufferedReader br = null;
    public static File f0 = new File(Label.fileConfigBatTest);
    public static File f1 = new File(Label.fileConfigUpsDown);
    public static File f2 = new File(Label.fileConfigOutlet1);
    public static File f3 = new File(Label.fileConfigOutlet2);
    public static File f4 = new File(Label.fileConfigUpsOff);
    public static File f5 = new File(Label.fileConfigSilence);
    public static File f_exit = new File(Label.fileConfigExit);
    public static File ff = null;
    public static String controloff = "";
    public static String temp2 = "";
    public static FileOutputStream fo = null;
    OutputStream out;

    public ConCOM2Set(OutputStream outputStream) {
        this.out = outputStream;
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
            if (!f0.exists()) {
                temp2 = " ";
                fo = new FileOutputStream(f0, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
            if (!f2.exists()) {
                temp2 = "0";
                fo = new FileOutputStream(f2, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
            if (!f3.exists()) {
                temp2 = "0";
                fo = new FileOutputStream(f3, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
            if (!f4.exists()) {
                temp2 = "0";
                fo = new FileOutputStream(f4, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
            }
            if (!f5.exists()) {
                temp2 = "0";
                fo = new FileOutputStream(f5, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
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
        } catch (Exception e) {
            System.err.println("Exception : Class ConCOM2Set : ConCOM2Set : e0 : " + e);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(this.out);
            while (roopINT >= 0) {
                read();
                if (roopINT % 60 == 0) {
                    Connect.readfile();
                    sleep(1000L);
                }
                if (roopINT <= 2) {
                    IBO = true;
                    bufferedOutputStream.write(I);
                    bufferedOutputStream.flush();
                    sleep(2000L);
                } else if (roopINT <= 4) {
                    FBO = true;
                    bufferedOutputStream.write(F);
                    bufferedOutputStream.flush();
                    sleep(2000L);
                } else if (roopINT % 10 == 0) {
                    rtBO = true;
                    bufferedOutputStream.write(Rt);
                    bufferedOutputStream.flush();
                    sleep(2000L);
                } else if (roopINT % 15 == 0) {
                    YopBO = true;
                    bufferedOutputStream.write(Yop);
                    bufferedOutputStream.flush();
                    sleep(2000L);
                } else if (roopINT % 2 == 0) {
                    bufferedOutputStream.write(a1);
                    bufferedOutputStream.flush();
                    sleep(Label.ConfigIntervall);
                } else {
                    bufferedOutputStream.write(a2);
                    bufferedOutputStream.flush();
                    sleep(Label.ConfigIntervall);
                }
                roopINT++;
                if (roopINT == 1000) {
                    roopINT = 20;
                }
                if (a == 1) {
                    bufferedOutputStream.write(new byte[]{84, 13});
                    bufferedOutputStream.flush();
                    soundBO = true;
                    deeptestBO = false;
                    a = 0;
                    sleep(1000L);
                } else if (a == 2) {
                    bufferedOutputStream.write(new byte[]{84, 76, 13});
                    bufferedOutputStream.flush();
                    deeptestBO = true;
                    a = 0;
                    sleep(1000L);
                } else if (a == 3) {
                    bufferedOutputStream.write(new byte[]{67, 84, 13});
                    bufferedOutputStream.flush();
                    a = 0;
                    sleep(1000L);
                } else if (a == 4) {
                    byte[] bArr = {83, 48, 0, 13};
                    String strValueOf = String.valueOf(Label.upsdelay);
                    if (Label.upsdelay < 10) {
                        bArr[2] = (byte) strValueOf.charAt(0);
                    } else if (Label.upsdelay >= 10) {
                        bArr[1] = (byte) strValueOf.charAt(0);
                        bArr[2] = (byte) strValueOf.charAt(1);
                    }
                    bufferedOutputStream.write(bArr);
                    bufferedOutputStream.flush();
                    roopINT = -1;
                } else if (a == 5) {
                    byte[] bArr2 = {83, 48, 0, 13};
                    if (controloff.length() == 1) {
                        bArr2[2] = (byte) controloff.charAt(0);
                    } else if (controloff.length() == 2) {
                        bArr2[1] = (byte) controloff.charAt(0);
                        bArr2[2] = (byte) controloff.charAt(1);
                    }
                    Record record = UPSMON.RecordAPP;
                    Record.set(new String("UPS_turn_off_after_" + controloff + "_minutes"));
                    System.out.println("UPSMON : UPS will turn off power after " + controloff + " minutes");
                    bufferedOutputStream.write(bArr2);
                    bufferedOutputStream.flush();
                    sleep(3000L);
                    try {
                        String str = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                        System.out.println("UPSMON : Linux Shutdown !!");
                        Record record2 = UPSMON.RecordAPP;
                        Record.set(new String("Linux_Shutdown"));
                        Record record3 = UPSMON.RecordAPP;
                        Record.set2(new String("UPS_Shutdown"), controloff);
                        sleep(1000L);
                        Runtime.getRuntime().exec(str);
                    } catch (Exception e) {
                        System.err.println("Exception : Class ConCOM2Set : run() : e : " + e);
                    }
                    try {
                        fo = new FileOutputStream(f4, false);
                        fo.write("0".getBytes(), 0, "0".length());
                        fo.close();
                    } catch (Exception e2) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e9 : " + e2);
                    }
                    a = 0;
                } else if (a == 6) {
                    bufferedOutputStream.write(new byte[]{79, 48, 49, 79, 78, 13});
                    bufferedOutputStream.flush();
                    System.out.println("UPSMON : Outlet Group1 ON");
                    Record record4 = UPSMON.RecordAPP;
                    Record.set(new String("Outlet_Group1_ON"));
                    try {
                        temp2 = "0";
                        fo = new FileOutputStream(f2, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                    } catch (Exception e3) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + e3);
                    }
                    a = 0;
                    sleep(1000L);
                } else if (a == 7) {
                    bufferedOutputStream.write(new byte[]{79, 48, 49, 79, 70, 70, 13});
                    bufferedOutputStream.flush();
                    System.out.println("UPSMON : Outlet Group1 OFF");
                    Record record5 = UPSMON.RecordAPP;
                    Record.set(new String("Outlet_Group1_OFF"));
                    try {
                        temp2 = "0";
                        fo = new FileOutputStream(f2, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                    } catch (Exception e4) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + e4);
                    }
                    a = 0;
                    sleep(1000L);
                } else if (a == 8) {
                    bufferedOutputStream.write(new byte[]{79, 48, 50, 79, 78, 13});
                    bufferedOutputStream.flush();
                    System.out.println("UPSMON : Outlet Group2 ON");
                    Record record6 = UPSMON.RecordAPP;
                    Record.set(new String("Outlet_Group2_ON"));
                    try {
                        temp2 = "0";
                        fo = new FileOutputStream(f3, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                    } catch (Exception e5) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + e5);
                    }
                    a = 0;
                    sleep(1000L);
                } else if (a == 9) {
                    bufferedOutputStream.write(new byte[]{79, 48, 50, 79, 70, 70, 13});
                    bufferedOutputStream.flush();
                    System.out.println("UPSMON : Outlet Group2 OFF");
                    Record record7 = UPSMON.RecordAPP;
                    Record.set(new String("Outlet_Group2_OFF"));
                    try {
                        temp2 = "0";
                        fo = new FileOutputStream(f3, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                    } catch (Exception e6) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + e6);
                    }
                    a = 0;
                    sleep(1000L);
                } else if (a == 10) {
                    System.out.println("UPSMON : Beeper OFF");
                    if (ConCOM2Get.b0.equals("1")) {
                        bufferedOutputStream.write(new byte[]{81, 13});
                        bufferedOutputStream.flush();
                    }
                    try {
                        temp2 = "0";
                        fo = new FileOutputStream(f5, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                    } catch (Exception e7) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + e7);
                    }
                    a = 0;
                    sleep(1000L);
                } else if (a == 11) {
                    System.out.println("UPSMON : Beeper ON");
                    if (ConCOM2Get.b0.equals("0")) {
                        bufferedOutputStream.write(new byte[]{81, 13});
                        bufferedOutputStream.flush();
                    }
                    try {
                        temp2 = "0";
                        fo = new FileOutputStream(f5, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                    } catch (Exception e8) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + e8);
                    }
                    a = 0;
                    sleep(1000L);
                }
            }
        } catch (Exception e9) {
            System.err.println("Exception : Class ConCOM2Set : run() : e1 :" + e9);
        }
        try {
            this.out.close();
        } catch (IOException e10) {
            System.err.println("Exception : Class ConCOM2Set : run() : e2 :" + e10);
        }
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
                        a = 4;
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
            if (f5.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f5)));
                while (true) {
                    String line4 = br.readLine();
                    temp2 = line4;
                    if (line4 == null) {
                        break;
                    }
                    if (temp2.equals("2")) {
                        a = 10;
                    } else if (temp2.equals("1")) {
                        a = 11;
                    }
                }
                br.close();
                f5.delete();
            }
            if (f2.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));
                while (true) {
                    String line5 = br.readLine();
                    temp2 = line5;
                    if (line5 == null) {
                        break;
                    }
                    if (temp2.equals("0")) {
                        br = new BufferedReader(new InputStreamReader(new FileInputStream(f3)));
                        while (true) {
                            String line6 = br.readLine();
                            temp2 = line6;
                            if (line6 != null) {
                                if (temp2.equals("1")) {
                                    a = 8;
                                } else if (temp2.equals("2")) {
                                    a = 9;
                                }
                            }
                        }
                    } else if (temp2.equals("1")) {
                        a = 6;
                    } else if (temp2.equals("2")) {
                        a = 7;
                    }
                }
                br.close();
                f2.delete();
            }
            if (f_exit.exists()) {
                temp2 = "1";
                fo = new FileOutputStream(f_exit, false);
                fo.write(temp2.getBytes(), 0, temp2.length());
                fo.close();
                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("Exception Class ConCOM2Set : read() : ea : " + e);
        }
    }
}
