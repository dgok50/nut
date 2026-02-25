package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/* JADX INFO: compiled from: ConPro.java */
/* JADX INFO: loaded from: ConPROSet.class */
class ConPROSet extends Thread {
    public static InetAddress ProIP = null;
    public static DatagramSocket server = null;
    public static DatagramPacket theOutput = null;
    public static BufferedReader br = null;
    public static File f0 = new File(Label.fileConfigBatTest);
    public static File ff = null;
    public static FileOutputStream fo = null;
    public static String temp = "";
    public static int a = 0;
    public static int roopINT = 0;

    public ConPROSet(DatagramSocket datagramSocket) {
        server = datagramSocket;
        try {
            ff = new File(Label.fileConfigInterval);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                temp = br.readLine();
                Label.ConfigIntervall = Integer.parseInt(temp) * 1000;
                br.close();
            } else {
                temp = "2";
                fo = new FileOutputStream(ff, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
                temp = "POWERCOM";
                fo = new FileOutputStream(Label.fileCompany, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            if (!f0.exists()) {
                temp = "0";
                fo = new FileOutputStream(f0, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            ff = new File(Label.fileBatTestTimes);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Bat_test_times = Integer.parseInt(br.readLine());
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
            ff = new File(Label.fileStart);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.UpsStartTime = br.readLine();
                br.close();
            }
            ff = new File(Label.fileBatStop);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Batpowerend = br.readLine();
                br.close();
            }
        } catch (Exception e) {
            System.err.println("Exception : Class ConPROSet : e : " + e);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (roopINT >= 0) {
            read();
            try {
                byte[] bArr = {80, 67, 77, 71, 1, 69, 78, 68};
                theOutput = new DatagramPacket(bArr, bArr.length, ProIP, 2601);
                server.send(theOutput);
                sleep(Label.ConfigIntervall);
            } catch (Exception e) {
                System.err.println("Exceptino : Class ConPROSet : run() : e : " + e);
            }
            if (a == 1) {
                try {
                    byte[] bArr2 = {80, 67, 77, 83, 1, 69, 78, 68};
                    theOutput = new DatagramPacket(bArr2, bArr2.length, ProIP, 2601);
                    server.send(theOutput);
                    sleep(5000L);
                    temp = "";
                    fo = new FileOutputStream(f0, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                } catch (Exception e2) {
                    System.err.println("Exceptino : Class ConPROSet : run() : e1 : " + e2);
                }
                a = 0;
            }
            if (roopINT % 20 == 0) {
                Connect.readfile();
            } else if (roopINT == 99) {
                roopINT = 11;
            }
        }
    }

    public static void read() {
        try {
            ProIP = InetAddress.getByName(UPSMON.snmp);
            if (f0.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f0)));
                while (true) {
                    String line = br.readLine();
                    temp = line;
                    if (line == null) {
                        break;
                    } else if (temp.equals("1")) {
                        a = 1;
                    }
                }
                br.close();
                f0.delete();
            }
        } catch (Exception e) {
            System.err.println("Exception : Class ConPROSet : read() : e0 : " + e);
        }
    }
}
