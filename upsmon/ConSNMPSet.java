package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/* JADX INFO: compiled from: ConSNMP.java */
/* JADX INFO: loaded from: ConSNMPSet.class */
class ConSNMPSet extends Thread {
    public static String bo1;
    public static String bo2;
    public static String bo3;
    public static InetAddress SNMPCardIP = null;
    public static DatagramPacket theOutput = null;
    public static DatagramSocket server = null;
    public static String[] text = new String[7];
    public static String temp = "SNMPCard";
    public static byte[] data_E = {48, 43, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 30, 2, 4, 54, -8, 11, 68, 2, 1, 0, 2, 1, 0, 48, 16, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 2, 1, 0, 5, 0};
    public static byte[] data_D = {48, -127, -14, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, -127, -28, 2, 1, 4, 2, 1, 0, 2, 1, 1, 48, -127, -40, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 1, 2, 0, 5, 0, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 2, 4, 0, 5, 0, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 2, 7, 0, 5, 0, 48, 16, 6, 12, 43, 6, 1, 2, 1, 33, 1, 3, 3, 1, 2, 1, 5, 0, 48, 16, 6, 12, 43, 6, 1, 2, 1, 33, 1, 3, 3, 1, 3, 1, 5, 0, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 4, 1, 0, 5, 0, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 4, 2, 0, 5, 0, 48, 16, 6, 12, 43, 6, 1, 2, 1, 33, 1, 4, 4, 1, 2, 1, 5, 0, 48, 16, 6, 12, 43, 6, 1, 2, 1, 33, 1, 4, 4, 1, 5, 1, 5, 0, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 6, 1, 0, 5, 0, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 9, 1, 0, 5, 0, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 2, 3, 0, 5, 0, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 2, 5, 0, 5, 0};
    public static byte[] data_C = {48, 45, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 32, 2, 4, 88, -95, 102, 68, 2, 1, 0, 2, 1, 0, 48, 18, 48, 16, 6, 12, 43, 6, 1, 2, 1, 33, 1, 4, 4, 1, 4, 1, 5, 0};
    public static byte[] data_B = {48, 47, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 34, 2, 4, 13, -3, 0, -106, 2, 1, 0, 2, 1, 0, 48, 20, 48, 18, 6, 14, 43, 6, 1, 4, 1, -121, 39, 1, 1, 1, 9, 1, 1, 0, 5, 0};
    public static byte[] data_A = {48, 47, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 34, 2, 4, 98, -71, 5, 53, 2, 1, 0, 2, 1, 0, 48, 20, 48, 18, 6, 14, 43, 6, 1, 4, 1, -121, 39, 1, 1, 1, 9, 1, 2, 0, 5, 0};
    public static int a = 0;
    public static int controloff = 0;
    public static int IBO = 0;
    public static boolean powerwatt = false;
    public static boolean envitemp = false;
    public static boolean envihumi = false;
    public static boolean megatecBO = false;
    public static boolean synsoBO = false;
    public static boolean envitempBO = true;
    public static boolean envihumiBO = true;
    public static boolean powerwatBO = true;
    public static boolean deeptestBO = false;
    public static boolean battBO = false;
    public static File ff = null;
    public static File f0 = new File(Label.fileConfigBatTest);
    public static File f1 = new File(Label.fileConfigUpsDown);
    public static File f2 = new File(Label.fileConfigOutlet1);
    public static File f3 = new File(Label.fileConfigOutlet2);
    public static File f4 = new File(Label.fileConfigUpsOff);
    public static File f_exit = new File(Label.fileConfigExit);
    public static FileOutputStream fo = null;
    public static BufferedReader br = null;

    public ConSNMPSet(DatagramSocket datagramSocket) {
        server = datagramSocket;
        try {
            fo = new FileOutputStream(Label.fileInterface, false);
            fo.write(temp.getBytes(), 0, temp.length());
            fo.close();
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
            }
            if (!f0.exists()) {
                temp = "0";
                fo = new FileOutputStream(f0, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            if (!f2.exists()) {
                temp = "0";
                fo = new FileOutputStream(f2, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            if (!f3.exists()) {
                temp = "0";
                fo = new FileOutputStream(f3, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            if (!f4.exists()) {
                temp = "0";
                fo = new FileOutputStream(f4, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            ff = new File(Label.fileConfigMailTest);
            if (!ff.exists()) {
                temp = "0";
                fo = new FileOutputStream(ff, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            }
            ff = new File(Label.fileConfigcomunity);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Community = br.readLine();
                br.close();
            } else {
                temp = "public";
                fo = new FileOutputStream(ff, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
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
            ff = new File(Label.fileHost);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Hostname = br.readLine();
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
            ff = new File(Label.fileBatResult);
            if (ff.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
                Label.Battestresult = br.readLine();
                br.close();
            }
        } catch (Exception e) {
            System.err.println("Exception : Class ConSNMPSet : e : " + e);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        byte b;
        byte b2;
        Connect.readfile();
        while (a >= 0) {
            read();
            try {
                try {
                    theOutput = new DatagramPacket(data_D, data_D.length, SNMPCardIP, 161);
                    server.send(theOutput);
                    ConSNMPGet.powerflag = true;
                    sleep(Label.ConfigIntervall);
                    if (Label.b7 == 0) {
                        if (ConSNMPGet.b % 10 == 0) {
                            Connect.readfile();
                            if (synsoBO && powerwatBO) {
                                powerwatt = true;
                                ConSNMPGet.powerflag = false;
                                theOutput = new DatagramPacket(data_C, data_C.length, SNMPCardIP, 161);
                                server.send(theOutput);
                                sleep(1000L);
                            }
                        } else if (ConSNMPGet.b % 12 == 0) {
                            if (synsoBO) {
                                battBO = true;
                                theOutput = new DatagramPacket(data_E, data_E.length, SNMPCardIP, 161);
                                server.send(theOutput);
                                sleep(1000L);
                            }
                        } else if (ConSNMPGet.b % 14 == 0) {
                            if (megatecBO && envitempBO) {
                                envitemp = true;
                                ConSNMPGet.powerflag = false;
                                theOutput = new DatagramPacket(data_B, data_B.length, SNMPCardIP, 161);
                                server.send(theOutput);
                                sleep(1000L);
                            }
                        } else if (ConSNMPGet.b % 16 == 0 && megatecBO && envihumiBO) {
                            envihumi = true;
                            theOutput = new DatagramPacket(data_A, data_A.length, SNMPCardIP, 161);
                            server.send(theOutput);
                            sleep(1000L);
                        }
                    }
                    if (ConSNMPGet.b == 5) {
                        try {
                            ConSNMPGet.powerflag = false;
                            byte[] bArr = {48, 43, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 30, 2, 4, 28, -6, -73, 7, 2, 1, 0, 2, 1, 0, 48, 16, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 1, 1, 0, 5, 0};
                            theOutput = new DatagramPacket(bArr, bArr.length, SNMPCardIP, 161);
                            server.send(theOutput);
                            IBO = 1;
                            sleep(2000L);
                        } catch (Exception e) {
                            System.err.println("Exception : Class ConSNMPSet : run() : ee3 :" + e);
                        }
                        try {
                            ConSNMPGet.powerflag = false;
                            byte[] bArr2 = {48, 43, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 30, 2, 4, 28, -6, -73, 9, 2, 1, 0, 2, 1, 0, 48, 16, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 1, 2, 0, 5, 0};
                            theOutput = new DatagramPacket(bArr2, bArr2.length, SNMPCardIP, 161);
                            server.send(theOutput);
                            IBO = 2;
                            sleep(2000L);
                        } catch (Exception e2) {
                            System.err.println("Exception : Class ConSNMPSet : run() : ee3 :" + e2);
                        }
                        try {
                            ConSNMPGet.powerflag = false;
                            byte[] bArr3 = {48, 43, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 30, 2, 4, 28, -6, -73, 11, 2, 1, 0, 2, 1, 0, 48, 16, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 1, 3, 0, 5, 0};
                            theOutput = new DatagramPacket(bArr3, bArr3.length, SNMPCardIP, 161);
                            server.send(theOutput);
                            IBO = 3;
                            sleep(2000L);
                        } catch (Exception e3) {
                            System.err.println("Exception : Class ConSNMPSet : run() : ee3 :" + e3);
                        }
                        try {
                            ConSNMPGet.powerflag = false;
                            byte[] bArr4 = {48, 43, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 30, 2, 4, 98, -71, 4, 76, 2, 1, 0, 2, 1, 0, 48, 16, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 1, 4, 0, 5, 0};
                            theOutput = new DatagramPacket(bArr4, bArr4.length, SNMPCardIP, 161);
                            server.send(theOutput);
                            IBO = 4;
                            sleep(2000L);
                        } catch (Exception e4) {
                            System.err.println("Exception : Class ConSNMPSet : run() : ee0 :" + e4);
                        }
                        try {
                            ConSNMPGet.powerflag = false;
                            byte[] bArr5 = {48, 43, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -96, 30, 2, 4, 118, 19, 110, 70, 2, 1, 0, 2, 1, 0, 48, 16, 48, 14, 6, 10, 43, 6, 1, 2, 1, 33, 1, 9, 3, 0, 5, 0};
                            theOutput = new DatagramPacket(bArr5, bArr5.length, SNMPCardIP, 161);
                            server.send(theOutput);
                            IBO = 5;
                            sleep(2000L);
                        } catch (Exception e5) {
                            System.err.println("Exception : Class ConSNMPSet : run() : ee4 :" + e5);
                        }
                    }
                    if (a == 1) {
                        deeptestBO = false;
                        try {
                            byte[] bArr6 = {48, 53, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 40, 2, 4, 110, 40, 79, -98, 2, 1, 0, 2, 1, 0, 48, 26, 48, 24, 6, 10, 43, 6, 1, 2, 1, 33, 1, 7, 1, 0, 6, 10, 43, 6, 1, 2, 1, 33, 1, 7, 7, 3};
                            new DatagramSocket().send(new DatagramPacket(bArr6, bArr6.length, SNMPCardIP, 161));
                        } catch (Exception e6) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e3 :" + e6);
                        }
                        try {
                            temp = "";
                            fo = new FileOutputStream(f0, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e7) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e4 : " + e7);
                        }
                        a = 0;
                    } else if (a == 2) {
                        deeptestBO = true;
                        try {
                            byte[] bArr7 = {48, 53, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 40, 2, 4, 110, 40, 79, -98, 2, 1, 0, 2, 1, 0, 48, 26, 48, 24, 6, 10, 43, 6, 1, 2, 1, 33, 1, 7, 1, 0, 6, 10, 43, 6, 1, 2, 1, 33, 1, 7, 7, 5};
                            new DatagramSocket().send(new DatagramPacket(bArr7, bArr7.length, SNMPCardIP, 161));
                        } catch (Exception e8) {
                            System.err.println("Exception : Class ConSNMPSet : run() : ee :" + e8);
                        }
                        try {
                            temp = "";
                            fo = new FileOutputStream(f0, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e9) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e4 : " + e9);
                        }
                        a = 0;
                    } else if (a == 3) {
                        try {
                            byte[] bArr8 = {48, 53, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 40, 2, 4, 110, 40, 79, -98, 2, 1, 0, 2, 1, 0, 48, 26, 48, 24, 6, 10, 43, 6, 1, 2, 1, 33, 1, 7, 1, 0, 6, 10, 43, 6, 1, 2, 1, 33, 1, 7, 7, 2};
                            new DatagramSocket().send(new DatagramPacket(bArr8, bArr8.length, SNMPCardIP, 161));
                        } catch (Exception e10) {
                            System.err.println("Exception : Class ConSNMPSet : run() : ee :" + e10);
                        }
                        try {
                            temp = "";
                            fo = new FileOutputStream(f0, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e11) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e4 : " + e11);
                        }
                        a = 0;
                    } else if (a == 4) {
                        try {
                            temp = "";
                            fo = new FileOutputStream(f1, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                            byte[] bArr9 = {48, 44, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 31, 2, 4, 101, -71, -7, -99, 2, 1, 0, 2, 1, 0, 48, 17, 48, 15, 6, 10, 43, 6, 1, 2, 1, 33, 1, 8, 3, 0, 2, 1, -1};
                            DatagramSocket datagramSocket = new DatagramSocket();
                            datagramSocket.send(new DatagramPacket(bArr9, bArr9.length, SNMPCardIP, 161));
                            int i = Label.upsdelay * 60;
                            int i2 = i / 256;
                            int i3 = i % 256;
                            if (i2 > 128) {
                                b = (byte) (i2 - 256);
                            } else {
                                b = (byte) i2;
                            }
                            if (i3 > 128) {
                                b2 = (byte) (i3 - 256);
                            } else {
                                b2 = (byte) i3;
                            }
                            if (i > 128) {
                                byte[] bArr10 = {48, 45, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 32, 2, 4, 101, -71, -7, -89, 2, 1, 0, 2, 1, 0, 48, 18, 48, 16, 6, 10, 43, 6, 1, 2, 1, 33, 1, 8, 2, 0, 2, 2, b, b2};
                                datagramSocket.send(new DatagramPacket(bArr10, bArr10.length, SNMPCardIP, 161));
                            } else {
                                byte[] bArr11 = {48, 44, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 31, 2, 4, 101, -71, -7, -97, 2, 1, 0, 2, 1, 0, 48, 17, 48, 15, 6, 10, 43, 6, 1, 2, 1, 33, 1, 8, 2, 0, 2, 1, b2};
                                datagramSocket.send(new DatagramPacket(bArr11, bArr11.length, SNMPCardIP, 161));
                            }
                        } catch (Exception e12) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e5 : " + e12);
                        }
                        a = -1;
                    } else if (a == 5) {
                        Record record = UPSMON.RecordAPP;
                        Record.set(new String("UPS_reboot_power_after_" + controloff + "_minutes"));
                        System.out.println("UPSMON : UPS will reboot power after " + controloff + " minutes");
                        int i4 = controloff * 60;
                        int i5 = i4 / 256;
                        int i6 = i4 % 256;
                        if (controloff <= 2) {
                            try {
                                byte[] bArr12 = {48, 44, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 31, 2, 4, 53, 101, 74, -57, 2, 1, 0, 2, 1, 0, 48, 17, 48, 15, 6, 10, 43, 6, 1, 2, 1, 33, 1, 8, 2, 0, 2, 1, (byte) i6};
                                new DatagramSocket().send(new DatagramPacket(bArr12, bArr12.length, SNMPCardIP, 161));
                            } catch (Exception e13) {
                                System.err.println("Exception : Class ConSNMPSet : run() : e7 :" + e13);
                            }
                        } else {
                            try {
                                byte[] bArr13 = {48, 45, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 32, 2, 4, 53, 101, 74, -53, 2, 1, 0, 2, 1, 0, 48, 18, 48, 16, 6, 10, 43, 6, 1, 2, 1, 33, 1, 8, 2, 0, 2, 2, (byte) i5, (byte) i6};
                                new DatagramSocket().send(new DatagramPacket(bArr13, bArr13.length, SNMPCardIP, 161));
                            } catch (Exception e14) {
                                System.err.println("Exception : Class ConSNMPSet : run() : e8 :" + e14);
                            }
                        }
                        sleep(3000L);
                        try {
                            String str = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                            System.out.println("UPSMON : Linux Shutdown !!");
                            Record record2 = UPSMON.RecordAPP;
                            Record.set(new String("Linux_Shutdown"));
                            Record record3 = UPSMON.RecordAPP;
                            Record.set2(new String("UPS_Shutdown"), String.valueOf(Label.upsdelay));
                            sleep(1000L);
                            Runtime.getRuntime().exec(str);
                        } catch (Exception e15) {
                            System.err.println("Exception : Class ConCOM2Set : run() : e : " + e15);
                        }
                        try {
                            temp = "0";
                            fo = new FileOutputStream(f4, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e16) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e9 : " + e16);
                        }
                        a = 0;
                    } else if (a == 6) {
                        try {
                            byte[] bArr14 = {48, 48, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 35, 2, 4, 82, -4, 116, -80, 2, 1, 0, 2, 1, 0, 48, 21, 48, 19, 6, 14, 43, 6, 1, 4, 1, -121, 39, 1, 1, 1, 6, 2, 7, 0, 2, 1, 2};
                            new DatagramSocket().send(new DatagramPacket(bArr14, bArr14.length, SNMPCardIP, 161));
                            System.out.println("UPSMON : Outlet Group1 ON");
                            Record record4 = UPSMON.RecordAPP;
                            Record.set(new String("Outlet_Group1_ON"));
                        } catch (Exception e17) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e7 :" + e17);
                        }
                        try {
                            temp = "0";
                            fo = new FileOutputStream(f2, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e18) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + e18);
                        }
                        a = 0;
                    } else if (a == 7) {
                        try {
                            byte[] bArr15 = {48, 48, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 35, 2, 4, 82, -4, 116, -90, 2, 1, 0, 2, 1, 0, 48, 21, 48, 19, 6, 14, 43, 6, 1, 4, 1, -121, 39, 1, 1, 1, 6, 2, 8, 0, 2, 1, 2};
                            new DatagramSocket().send(new DatagramPacket(bArr15, bArr15.length, SNMPCardIP, 161));
                            System.out.println("UPSMON : Outlet Group1 OFF");
                            Record record5 = UPSMON.RecordAPP;
                            Record.set(new String("Outlet_Group1_OFF"));
                        } catch (Exception e19) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e9 :" + e19);
                        }
                        try {
                            temp = "0";
                            fo = new FileOutputStream(f2, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e20) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e4 : " + e20);
                        }
                        a = 0;
                    } else if (a == 8) {
                        try {
                            byte[] bArr16 = {48, 48, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 35, 2, 4, 82, -4, 116, -75, 2, 1, 0, 2, 1, 0, 48, 21, 48, 19, 6, 14, 43, 6, 1, 4, 1, -121, 39, 1, 1, 1, 6, 2, 9, 0, 2, 1, 2};
                            new DatagramSocket().send(new DatagramPacket(bArr16, bArr16.length, SNMPCardIP, 161));
                            System.out.println("UPSMON : Outlet Group2 ON");
                            Record record6 = UPSMON.RecordAPP;
                            Record.set(new String("Outlet_Group2_ON"));
                        } catch (Exception e21) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e7 :" + e21);
                        }
                        try {
                            temp = "0";
                            fo = new FileOutputStream(f3, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e22) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + e22);
                        }
                        a = 0;
                    } else if (a == 9) {
                        try {
                            byte[] bArr17 = {48, 48, 2, 1, 0, 4, 6, 112, 117, 98, 108, 105, 99, -93, 35, 2, 4, 82, -4, 116, -85, 2, 1, 0, 2, 1, 0, 48, 21, 48, 19, 6, 14, 43, 6, 1, 4, 1, -121, 39, 1, 1, 1, 6, 2, 10, 0, 2, 1, 2};
                            new DatagramSocket().send(new DatagramPacket(bArr17, bArr17.length, SNMPCardIP, 161));
                            System.out.println("UPSMON : Outlet Group2 OFF");
                            Record record7 = UPSMON.RecordAPP;
                            Record.set(new String("Outlet_Group2_OFF"));
                        } catch (Exception e23) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e8 :" + e23);
                        }
                        try {
                            temp = "0";
                            fo = new FileOutputStream(f3, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e24) {
                            System.err.println("Exception : Class ConSNMPSet : run() : e4 : " + e24);
                        }
                        a = 0;
                    }
                } catch (Exception e25) {
                    System.err.println("Exception : Class ConSNMPSet : run() : e2 :" + e25);
                }
            } catch (Exception e26) {
                System.err.println("Exception : Class ConSNMPSet : run() : e3 :" + e26);
            }
        }
    }

    public static void read() {
        try {
            SNMPCardIP = InetAddress.getByName(UPSMON.snmp);
            if (f0.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f0)));
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.equals("1")) {
                        a = 1;
                    } else if (line.equals("2")) {
                        a = 2;
                    } else if (line.equals("0")) {
                        a = 3;
                    }
                }
                br.close();
            }
            if (f1.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));
                while (true) {
                    String line2 = br.readLine();
                    if (line2 == null) {
                        break;
                    } else if (line2.equals("1")) {
                        a = 4;
                    }
                }
                br.close();
                f1.delete();
            }
            if (f4.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f4)));
                while (true) {
                    String line3 = br.readLine();
                    if (line3 == null) {
                        break;
                    } else if (!line3.equals("0")) {
                        controloff = Integer.parseInt(line3);
                        a = 5;
                    }
                }
                br.close();
                f4.delete();
            }
            if (f2.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));
                while (true) {
                    String line4 = br.readLine();
                    if (line4 == null) {
                        break;
                    }
                    if (line4.equals("1")) {
                        a = 6;
                    } else if (line4.equals("2")) {
                        a = 7;
                    }
                }
                br.close();
                f2.delete();
            }
            if (f3.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f3)));
                while (true) {
                    String line5 = br.readLine();
                    if (line5 == null) {
                        break;
                    }
                    if (line5.equals("1")) {
                        a = 8;
                    } else if (line5.equals("2")) {
                        a = 9;
                    }
                }
                br.close();
                f2.delete();
            }
            if (f_exit.exists()) {
                fo = new FileOutputStream(f_exit, false);
                fo.write("1".getBytes(), 0, "1".length());
                fo.close();
                System.exit(0);
            }
        } catch (Exception e) {
            System.err.println("Exception : Class ConSNMPSet : read() : e : " + e);
        }
    }
}
