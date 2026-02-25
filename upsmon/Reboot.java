package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: Reboot.class */
public class Reboot {
    public static String separaST = System.getProperty("file.separator");
    public static String minuteST = "";
    public static String resetST = "";
    public static BufferedReader br = null;
    public static FileOutputStream fo = null;

    public static void main(String[] strArr) {
        new Reboot(strArr);
        System.exit(0);
    }

    private Reboot(String[] strArr) {
        portal(strArr);
    }

    public static String portal(String[] strArr) {
        try {
            System.out.println();
            new StringBuffer();
            minuteST = "1";
            System.out.println(" a. Ups reboot after minutes                        : 1");
            System.out.print(" b. Commit ups reboot                          (Y/n): ");
            br = new BufferedReader(new InputStreamReader(System.in));
            resetST = br.readLine();
            System.out.flush();
            System.out.println();
            if (!resetST.equals("N") && !resetST.equals("n")) {
                fo = new FileOutputStream(new File("EXT" + separaST + "CONFIG" + separaST + "UPS_OFF.txt"), false);
                fo.write(minuteST.getBytes(), 0, minuteST.length());
                fo.close();
                br.close();
            }
        } catch (Exception e) {
            System.err.println("Exception: Class Reboot: main(): e1: " + e);
        }
        return minuteST;
    }
}
