package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: VMWare.class */
public class VMWare {
    public static String separaST = System.getProperty("file.separator");
    public static String enableST = "";
    public static String vmwareipST = "";
    public static String vmwareidST = "";
    public static String passwdST = "";
    public static String resetST = "";
    public static String temp = "";
    public static BufferedReader br = null;
    public static FileOutputStream fo = null;

    public static void main(String[] strArr) {
        new VMWare(strArr);
        System.exit(0);
    }

    private VMWare(String[] strArr) {
        portal(strArr);
    }

    public static String portal(String[] strArr) {
        boolean z = false;
        if (strArr.length != 0 && strArr[0].equals("default")) {
            z = true;
        }
        if (!z) {
            try {
                System.out.println();
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    System.out.print(" a. VMWare Server Shutdown                     (Y/n): ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    enableST = br.readLine();
                    System.out.flush();
                    if (!enableST.equals("0") && enableST.length() <= 2) {
                        if (enableST.equals("y") || enableST.equals("Y") || enableST.length() == 0) {
                            stringBuffer.append("[Enable]=true\n");
                        } else {
                            fo = new FileOutputStream(new File("EXT" + separaST + "SYSTEM" + separaST + "VMWare.txt"), false);
                            temp = "[Enable]=false\n";
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                            br.close();
                            System.exit(0);
                        }
                        do {
                            System.out.print(" b. VMWare Server IP                                : ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            vmwareipST = br.readLine();
                            System.out.flush();
                        } while (vmwareipST.length() < 9);
                        stringBuffer.append("[VMWare IP]=" + vmwareipST + "\n");
                        System.out.print(" d. VMWare Root Account             (default : root): ");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        vmwareidST = br.readLine();
                        System.out.flush();
                        if (vmwareidST.length() == 0) {
                            vmwareidST = "root";
                        }
                        stringBuffer.append("[VMWare ID]=" + vmwareidST + "\n");
                        do {
                            passwdST = new String(System.console().readPassword(" e. VMWare Root Password                            : ", new Object[0]));
                        } while (passwdST.length() == 0);
                        stringBuffer.append("[VMWare Password]=" + passwdST + "\n");
                        System.out.print(" i. Would you need to reset                    (N/y): ");
                        br = new BufferedReader(new InputStreamReader(System.in));
                        resetST = br.readLine();
                        System.out.flush();
                        System.out.println();
                        if (!resetST.equals("Y") && !resetST.equals("y")) {
                            break;
                        }
                        stringBuffer.delete(0, stringBuffer.length());
                    }
                }
                fo = new FileOutputStream(new File("EXT" + separaST + "SYSTEM" + separaST + "VMWare.txt"), false);
                temp = stringBuffer.toString();
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
                fo = new FileOutputStream(new File("EXT" + separaST + "Execute" + separaST + "VMWare"), false);
                temp = "vicfg-hostops --server " + vmwareipST + " --username " + vmwareidST + " --password " + passwdST + " --operation shutdown --force";
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
                br.close();
            } catch (Exception e) {
                System.err.println("Exception: Class VMWare: main(): e1: " + e);
            }
        }
        return enableST;
    }
}
