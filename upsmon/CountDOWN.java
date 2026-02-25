package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: CountDOWN.class */
public class CountDOWN extends Thread {
    public static int osdelay;
    public static int upsdowntime;
    public static int outletoffsec;
    public static int a;
    public static int b;
    public boolean turnoffupsBO = true;
    public boolean shutdownOS = true;
    public boolean showtime = true;
    public boolean batdown = false;
    public static int outletoffBO = 0;
    public static String temp = "";
    public static String vmwareST = "";
    public static String shutdownfile = "";
    public static boolean onceHibernate = false;
    public static boolean usrcmd = true;
    public static boolean roopBO = true;
    public static File f = new File(Label.fileVMWare);
    public static BufferedReader br = null;

    public CountDOWN() {
        osdelay = Label.osdelay;
        outletoffsec = Integer.parseInt(Label.outletoffsec);
        roopBO = true;
        readfile();
    }

    public static void readfile() {
        try {
            if (f.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                int i = 0;
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    if (i == 0) {
                        vmwareST = line.substring(line.indexOf("=") + 1, line.length());
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            System.err.println("Exception CountDOWN: readfile(): e" + e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:123:0x06a9, code lost:
    
        r6 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x06aa, code lost:
    
        java.lang.System.err.println("Exception : Class CountDOWN : run() : e2 : " + r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x06c3, code lost:
    
        return;
     */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            Method dump skipped, instruction units count: 1732
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.CountDOWN.run():void");
    }

    public void closeOS() {
        try {
            if (UPSMON.shutdowntype.equals("0")) {
                try {
                    Runtime.getRuntime().exec("EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "Broadcast-Off");
                    sleep(1000L);
                } catch (Exception e) {
                    System.err.println("Exception : Class CountDOWN : run() : e1 : " + e);
                }
                String str = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                System.out.println("UPSMON : Shutdown Linux!!");
                Record record = UPSMON.RecordAPP;
                Record.set(new String("Linux_Shutdown"));
                if (Label.upsdelay != 0) {
                    Record record2 = UPSMON.RecordAPP;
                    Record.set2(new String("UPS_Shutdown"), String.valueOf(Label.upsdelay));
                }
                sleep(2000L);
                Runtime.getRuntime().exec(str);
                System.exit(0);
                return;
            }
            if (UPSMON.shutdowntype.equals("1")) {
                try {
                    String str2 = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN2";
                    System.out.println("UPSMON : Linux Hibernate !!");
                    onceHibernate = true;
                    Record record3 = UPSMON.RecordAPP;
                    Record.set(new String("Linux_Hibernate"));
                    if (!UPSMON.upsdelay.equals("0")) {
                        Record record4 = UPSMON.RecordAPP;
                        Record.set2(new String("UPS_Shutdown"), UPSMON.upsdelay);
                    }
                    sleep(2000L);
                    Runtime.getRuntime().exec(str2);
                } catch (Exception e2) {
                    System.err.println("Exception : Class CountDOWN : closeOS2() : e : " + e2);
                }
            }
        } catch (Exception e3) {
            System.err.println("Exception : Class CountDOWN : closeOS1() : e : " + e3);
        }
    }
}
