package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/* JADX INFO: loaded from: Mail.class */
public class Mail {
    public static String separaST = System.getProperty("file.separator");
    public static String enableST = "";
    public static String mailserverST = "";
    public static String mailportST = "";
    public static String idST = "";
    public static String passwdST = "";
    public static String authenST = "";
    public static String senderST = "";
    public static String receivST = "";
    public static String resetST = "";
    public static String temp = "";
    public static BufferedReader br = null;
    public static FileOutputStream fo = null;

    public static void main(String[] strArr) {
        new Mail(strArr);
        System.exit(0);
    }

    private Mail(String[] strArr) {
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
                    System.out.print(" a. E-Mail Server Enable                       (Y/n): ");
                    br = new BufferedReader(new InputStreamReader(System.in));
                    enableST = br.readLine();
                    System.out.flush();
                    if (!enableST.equals("0") && enableST.length() <= 2) {
                        if (enableST.equals("y") || enableST.equals("Y") || enableST.length() == 0) {
                            stringBuffer.append("[Enable]=true\n");
                        } else {
                            fo = new FileOutputStream(new File("EXT" + separaST + "SYSTEM" + separaST + "Mail.txt"), false);
                            temp = "[Enable]=false\n";
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                            br.close();
                            System.exit(0);
                        }
                        do {
                            System.out.print(" b. E-Mail Server Name or IP                        : ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            mailserverST = br.readLine();
                            System.out.flush();
                        } while (mailserverST.length() < 9);
                        stringBuffer.append("[E-Mail Server]=" + mailserverST + "\n");
                        do {
                            System.out.print(" c. E-Mail Server Port                (default : 25): ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            mailportST = br.readLine();
                            System.out.flush();
                        } while (mailportST.length() > 5);
                        if (mailportST.length() == 0) {
                            stringBuffer.append("[E-Mail Server Port]=25\n");
                        } else {
                            stringBuffer.append("[E-Mail Server Port]=" + mailportST + "\n");
                        }
                        do {
                            System.out.print(" d. E-Mail Account                                  : ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            idST = br.readLine();
                            System.out.flush();
                        } while (idST.length() == 0);
                        stringBuffer.append("[E-Mail Account]=" + idST + "\n");
                        temp = new String(System.console().readPassword(" e. E-Mail Password                                 : ", new Object[0]));
                        stringBuffer.append("[E-Mail Account Password]=" + temp + "\n");
                        do {
                            System.out.print(" f. SSL Requires                               (Y/n): ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            authenST = br.readLine();
                            System.out.flush();
                        } while (authenST.length() > 3);
                        if (authenST.length() == 0 || authenST.equals("y") || authenST.equals("Y")) {
                            stringBuffer.append("[SSL]=true\n");
                        } else {
                            stringBuffer.append("[SSL]=false\n");
                        }
                        while (true) {
                            System.out.print(" g. E-Mail Sender                                   : ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            senderST = br.readLine();
                            System.out.flush();
                            if (senderST.length() != 0 && senderST.indexOf("@") != -1) {
                                break;
                            }
                        }
                        stringBuffer.append("[E-Mail Sender]=" + senderST + "\n");
                        while (true) {
                            System.out.print(" h. E-Mail Recipient                                : ");
                            br = new BufferedReader(new InputStreamReader(System.in));
                            receivST = br.readLine();
                            System.out.flush();
                            if (receivST.length() != 0 && receivST.indexOf("@") != -1) {
                                break;
                            }
                        }
                        stringBuffer.append("[E-Mail Recipient]=" + receivST + "\n");
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
                fo = new FileOutputStream(new File("EXT" + separaST + "SYSTEM" + separaST + "Mail.txt"), false);
                temp = stringBuffer.toString();
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
                br.close();
            } catch (Exception e) {
                System.err.println("Exception: Class Mail: main(): e1: " + e);
            }
        }
        return enableST;
    }
}
