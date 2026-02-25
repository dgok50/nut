package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbEndpoint;
import javax.usb.UsbPipe;

/* JADX INFO: loaded from: ConUSB4.class */
class ConUSB4 extends Thread {
    public static int Input_Voltage;
    public static int Output_Voltage;
    public static int Load;
    public static int Input_Frequency;
    public static int Battery_Capacity;
    public static int Temperature;
    public static int a;
    public static String temp;
    public static Calendar cal1;
    public static Calendar cal2;
    public static UsbEndpoint endpoint = null;
    public static UsbDevice upsUsbDevice = null;
    public static int Remaining_Time = 0;
    public static int bit7 = 0;
    public static int bit6 = 0;
    public static int bit5 = 0;
    public static int bit4 = 0;
    public static int bit3 = 1;
    public static int bit2 = 0;
    public static int bit1 = 0;
    public static int bit0 = 0;
    public static int controloff = 0;
    public static int b = 0;
    public static int timeINT = 0;
    public static int delay = 2000;
    public static boolean soundBO = false;
    public static boolean green_modeBO = false;
    public static boolean battery_power = false;
    public static boolean battery_power_stop = false;
    public static boolean testflag = false;
    public static File f0 = new File(Label.fileConfigBatTest);
    public static File f1 = new File(Label.fileConfigUpsDown);
    public static File f4 = new File(Label.fileConfigUpsOff);
    public static File ff = null;
    public static String c1 = "";
    public static String temp2 = null;
    public static String datalog = "";
    public static String filelog = "";
    public static BufferedReader br = null;
    public static FileOutputStream fo = null;
    public static Calendar cal3 = null;
    public static boolean freq_BO = true;
    static byte[] data = {80, 67, 77, 82, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 69, 78, 68};

    public ConUSB4(UsbEndpoint usbEndpoint, UsbDevice usbDevice, ConUSB conUSB) {
        Connect.model = 4;
        endpoint = usbEndpoint;
        upsUsbDevice = usbDevice;
        data[16] = 1;
        try {
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
        } catch (Exception e) {
            System.err.println("Exception : Class ConUSB4 : e : " + e);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        try {
            UsbPipe usbPipe = endpoint.getUsbPipe();
            byte[] bArr = {-83, 1, 1, 0, 0, 0, 0, 0};
            UsbControlIrp usbControlIrpCreateUsbControlIrp = usbPipe.createUsbControlIrp((byte) 33, (byte) 9, (short) 941, (short) 0);
            usbPipe.createUsbControlIrp((byte) 33, (byte) 9, (short) 928, (short) 0);
            usbPipe.createUsbControlIrp((byte) 33, (byte) 9, (short) 932, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp2 = usbPipe.createUsbControlIrp((byte) 33, (byte) 9, (short) 783, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp3 = usbPipe.createUsbControlIrp((byte) 33, (byte) 9, (short) 784, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp4 = usbPipe.createUsbControlIrp((byte) 33, (byte) 9, (short) 789, (short) 0);
            byte[] bArr2 = {-96, 2, 5, 0, 0, 0, 0, 0};
            byte[] bArr3 = {-92, 0, 0, 0, 0, 0, 0, 0};
            byte[] bArr4 = {-96, 2, 71, 0, 0, 0, 0, 0};
            byte[] bArr5 = new byte[3];
            byte[] bArr6 = new byte[3];
            byte[] bArr7 = new byte[3];
            byte[] bArr8 = new byte[3];
            byte[] bArr9 = new byte[3];
            byte[] bArr10 = new byte[3];
            byte[] bArr11 = new byte[3];
            byte[] bArr12 = new byte[3];
            UsbControlIrp usbControlIrpCreateUsbControlIrp5 = usbPipe.createUsbControlIrp((byte) -95, (byte) 1, (short) 782, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp6 = usbPipe.createUsbControlIrp((byte) -95, (byte) 1, (short) 797, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp7 = usbPipe.createUsbControlIrp((byte) -95, (byte) 1, (short) 798, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp8 = usbPipe.createUsbControlIrp((byte) -95, (byte) 1, (short) 799, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp9 = usbPipe.createUsbControlIrp((byte) -95, (byte) 1, (short) 801, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp10 = usbPipe.createUsbControlIrp((byte) -95, (byte) 1, (short) 802, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp11 = usbPipe.createUsbControlIrp((byte) -95, (byte) 1, (short) 806, (short) 0);
            UsbControlIrp usbControlIrpCreateUsbControlIrp12 = usbPipe.createUsbControlIrp((byte) -95, (byte) 1, (short) 823, (short) 0);
            usbControlIrpCreateUsbControlIrp5.setData(bArr5);
            usbControlIrpCreateUsbControlIrp6.setData(bArr6);
            usbControlIrpCreateUsbControlIrp7.setData(bArr7);
            usbControlIrpCreateUsbControlIrp8.setData(bArr8);
            usbControlIrpCreateUsbControlIrp9.setData(bArr9);
            usbControlIrpCreateUsbControlIrp10.setData(bArr10);
            usbControlIrpCreateUsbControlIrp11.setData(bArr11);
            usbControlIrpCreateUsbControlIrp12.setData(bArr12);
            usbPipe.open();
            while (a >= 0) {
                read();
                if (a == 1) {
                    a = 0;
                    b = 40;
                    usbControlIrpCreateUsbControlIrp4.setData(new byte[]{21, 1});
                    upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp4);
                    usbControlIrpCreateUsbControlIrp4.setComplete(false);
                    sleep(1000L);
                } else if (a == 4) {
                    b = 30;
                    usbControlIrpCreateUsbControlIrp2.setData(new byte[]{15, (byte) (Label.upsdelay + 8), 0});
                    upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp2);
                    usbControlIrpCreateUsbControlIrp2.setComplete(false);
                    a = -1;
                    sleep(1000L);
                } else if (a == 5) {
                    a = 0;
                    usbControlIrpCreateUsbControlIrp3.setData(new byte[]{16, 1, 0});
                    upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp3);
                    usbControlIrpCreateUsbControlIrp3.setComplete(false);
                    sleep(3000L);
                    Record record = UPSMON.RecordAPP;
                    Record.set(new String("UPS_turn_off_after_" + controloff + "_minutes"));
                    System.out.println("UPSMON : UPS will turn off power after " + controloff + " minutes");
                    usbControlIrpCreateUsbControlIrp2.setData(new byte[]{15, 0, (byte) controloff});
                    upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp2);
                    usbControlIrpCreateUsbControlIrp2.setComplete(false);
                    sleep(3000L);
                    try {
                        String str7 = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                        System.out.println("UPSMON : Linux Shutdown !!");
                        Record record2 = UPSMON.RecordAPP;
                        Record.set(new String("Linux_Shutdown"));
                        Record record3 = UPSMON.RecordAPP;
                        Record.set2(new String("UPS_Shutdown"), String.valueOf(controloff));
                        sleep(1000L);
                        Runtime.getRuntime().exec(str7);
                    } catch (Exception e) {
                        System.err.println("Exception : Class ConUSB1 : run() : e : " + e);
                    }
                }
                if (b == 0) {
                    usbControlIrpCreateUsbControlIrp.setData(bArr);
                    upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp);
                    usbControlIrpCreateUsbControlIrp.setComplete(false);
                    sleep(12000L);
                }
                upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp5);
                upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp6);
                if (freq_BO) {
                    upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp7);
                }
                upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp8);
                upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp9);
                upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp10);
                upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp11);
                upsUsbDevice.syncSubmit(usbControlIrpCreateUsbControlIrp12);
                Input_Voltage = bArr6[1];
                if (Input_Voltage < 0) {
                    Input_Voltage += 256;
                }
                if (Input_Voltage >= 100) {
                    str = "(" + String.valueOf(Input_Voltage) + ".0 ";
                } else if (Input_Voltage > 9) {
                    str = "(0" + String.valueOf(Input_Voltage) + ".0 ";
                } else {
                    str = "(00" + String.valueOf(Input_Voltage) + ".0 ";
                }
                data[5] = (byte) (Input_Voltage / 256);
                data[6] = (byte) (Input_Voltage % 256);
                Label.InputVolt = Input_Voltage;
                if (Label.InputVoltMax < Label.InputVolt) {
                    Label.InputVoltMax = Label.InputVolt;
                }
                if (Label.InputVoltMini > Label.InputVolt) {
                    Label.InputVoltMini = Label.InputVolt;
                }
                String str8 = str + "000.0 ";
                Output_Voltage = bArr9[1];
                if (Output_Voltage < 0) {
                    Output_Voltage += 256;
                }
                if (bArr11[2] == 68) {
                    str2 = str8 + "000.0 ";
                } else if (Output_Voltage >= 100) {
                    str2 = str8 + String.valueOf(Output_Voltage) + ".0 ";
                } else if (Output_Voltage > 9) {
                    str2 = str8 + "0" + String.valueOf(Output_Voltage) + ".0 ";
                } else {
                    str2 = str8 + "00" + String.valueOf(Output_Voltage) + ".0 ";
                }
                data[7] = (byte) (Output_Voltage / 256);
                data[8] = (byte) (Output_Voltage % 256);
                Label.OutputVolt = Output_Voltage;
                Load = bArr8[1];
                if (Load < 0) {
                    Load += 256;
                }
                if (Load >= 100) {
                    Load = 100;
                    str3 = str2 + String.valueOf(Load) + " ";
                } else if (Load > 9) {
                    str3 = str2 + "0" + String.valueOf(Load) + " ";
                } else {
                    str3 = str2 + "00" + String.valueOf(Load) + " ";
                }
                data[11] = (byte) Load;
                Label.Load = Load;
                Label.InputFreq = bArr7[1];
                if (Label.InputFreq >= 40) {
                    freq_BO = false;
                }
                if (Label.InputFreq < 0) {
                    Label.InputFreq += 256;
                }
                if (Label.InputFreq >= 10) {
                    str4 = str3 + Label.InputFreq + ".0 ";
                } else {
                    str4 = str3 + "0" + String.valueOf(Input_Frequency) + ".0 ";
                }
                data[13] = (byte) Label.InputFreq;
                Battery_Capacity = bArr5[1];
                if (Battery_Capacity < 0) {
                    Battery_Capacity += 256;
                }
                if (Battery_Capacity >= 100) {
                    str5 = str4 + "0100 ";
                } else if (Battery_Capacity >= 10) {
                    str5 = str4 + "00" + String.valueOf(Battery_Capacity) + " ";
                } else {
                    str5 = str4 + "000" + String.valueOf(Battery_Capacity) + " ";
                }
                data[12] = (byte) Battery_Capacity;
                Label.Bat_level = Battery_Capacity;
                Temperature = bArr10[1];
                if (Temperature < 0) {
                    Temperature += 256;
                }
                data[14] = (byte) Temperature;
                if (bArr11[2] == 68) {
                    str6 = str5 + "00.0 ";
                } else if (Temperature >= 10) {
                    str6 = str5 + String.valueOf(Temperature) + ".0 ";
                } else {
                    str6 = str5 + "0" + String.valueOf(Temperature) + ".0 ";
                }
                Label.Temperature = Temperature;
                if (bArr11[1] == 10 || Input_Voltage <= 10) {
                    Input_Voltage = 0;
                    if (bit7 == 0) {
                        bit7 = 1;
                        b = 30;
                        Connect.readfile();
                        battery_power = true;
                        System.out.println("UPSMON : Power Failure");
                        Record record4 = UPSMON.RecordAPP;
                        Record.set(new String("Power_Failure"));
                        temp = "Battery Power";
                        Label.PowerStatus = temp;
                        soundBO = true;
                        green_modeBO = true;
                        Label.Bat_times++;
                        temp = String.valueOf(Label.Bat_times);
                        fo = new FileOutputStream(Label.fileBatTimes, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                        if (Integer.parseInt(UPSMON.shutdowntype) != 2) {
                            CountDOWN.usrcmd = true;
                            new CountDOWN().start();
                            System.out.println("UPSMON : Linux count down " + Label.osdelay + " seconds");
                        }
                    }
                    data[18] = 1;
                } else {
                    if (bit7 == 1) {
                        bit7 = 0;
                        CountDOWN.roopBO = false;
                        battery_power_stop = true;
                        temp = "AC Utility Power";
                        Label.PowerStatus = temp;
                        System.out.println("UPSMON : Power Restore");
                        Record record5 = UPSMON.RecordAPP;
                        Record.set(new String("Power_Restore"));
                    }
                    data[18] = 0;
                }
                String str9 = str6 + String.valueOf(bit7);
                if (Battery_Capacity <= 30) {
                    if (bit6 == 0) {
                        bit6 = 1;
                        System.out.println("UPSMON : Low Battery");
                        Record record6 = UPSMON.RecordAPP;
                        Record.set(new String("Low_Battery"));
                        Connect.lowbat = 1;
                        data[19] = 1;
                    }
                } else if (bit6 == 1) {
                    bit6 = 0;
                    System.out.println("UPSMON : Normal");
                    Record record7 = UPSMON.RecordAPP;
                    Record.set(new String("Normal"));
                    Connect.lowbat = 0;
                    data[19] = 0;
                }
                String str10 = str9 + String.valueOf(bit6);
                if (bArr11[2] == 16) {
                    if (bit5 == 0) {
                        bit5 = 1;
                        temp = "AVR Boost";
                        Label.PowerStatus = temp;
                        System.out.println("UPSMON : Boost");
                        Record record8 = UPSMON.RecordAPP;
                        Record.set(new String("Boost"));
                        data[17] = 2;
                    }
                } else if (bArr11[2] == 32) {
                    if (bit5 == 0) {
                        bit5 = 1;
                        temp = "AVR Buck";
                        Label.PowerStatus = temp;
                        System.out.println("UPSMON : Buck");
                        Record record9 = UPSMON.RecordAPP;
                        Record.set(new String("Buck"));
                        data[17] = 3;
                    }
                } else if (bit5 == 1) {
                    bit5 = 0;
                    System.out.println("UPSMON : Normal");
                    Record record10 = UPSMON.RecordAPP;
                    Record.set(new String("Normal"));
                    data[17] = 0;
                }
                String str11 = str10 + String.valueOf(bit5);
                if (bArr11[1] == -115) {
                    if (bit4 == 0) {
                        bit4 = 1;
                        System.out.println("UPSMON : UPS Failed");
                        Record record11 = UPSMON.RecordAPP;
                        Record.set(new String("UPS_Failed"));
                        data[21] = 1;
                    }
                } else if (bit4 == 1) {
                    bit4 = 0;
                    System.out.println("UPSMON : UPS Normal");
                    Record record12 = UPSMON.RecordAPP;
                    Record.set(new String("UPS_Normal"));
                    data[21] = 0;
                }
                String str12 = str11 + String.valueOf(bit4);
                if (bArr11[1] == 77) {
                    if (bit3 == 1) {
                        bit3 = 2;
                        System.out.println("UPSMON : Battery Failed");
                        Record record13 = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Failed"));
                        data[22] = 1;
                    }
                } else {
                    if (bit3 == 2) {
                        System.out.println("UPSMON : UPS Normal");
                        Record record14 = UPSMON.RecordAPP;
                        Record.set(new String("UPS_Normal"));
                        data[22] = 0;
                    }
                    bit3 = 1;
                }
                String str13 = str12 + String.valueOf(bit3);
                if ((bArr11[1] == 13 && bArr11[2] == 64) || (bArr11[1] == 10 && bArr11[2] == 64)) {
                    if (bit2 == 0) {
                        bit2 = 1;
                        soundBO = true;
                        green_modeBO = true;
                        testflag = true;
                        System.out.println("UPSMON : Battery Test");
                        Record record15 = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Test"));
                        data[23] = 1;
                    }
                } else if (bit2 == 1) {
                    bit2 = 0;
                    System.out.println("UPSMON : Battery Normal");
                    Record record16 = UPSMON.RecordAPP;
                    Record.set(new String("Battery_Normal"));
                    data[23] = 0;
                    Label.Battestresult = "OK";
                    temp2 = "Battery Normal";
                    fo = new FileOutputStream(Label.fileBatResult, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                String str14 = str13 + String.valueOf(bit2);
                if (bArr12[1] < 0) {
                    Remaining_Time = (((bArr12[2] * 256) + bArr12[1]) + 256) / 60;
                } else {
                    Remaining_Time = ((bArr12[2] * 256) + bArr12[1]) / 60;
                }
                Label.Bat_backup_time = Remaining_Time;
                data[9] = bArr12[2];
                data[10] = bArr12[1];
                String str15 = str14 + String.valueOf(bit1) + String.valueOf(bit0) + "\n";
                if (str15.length() >= 44) {
                    try {
                        byte[] bytes = str15.getBytes();
                        if (bytes.length != 0) {
                            fo = new FileOutputStream(Label.fileget, false);
                            fo.write(bytes, 0, bytes.length);
                            fo.close();
                            String strValueOf = String.valueOf(Label.Bat_backup_time);
                            fo = new FileOutputStream(Label.fileRt, false);
                            fo.write(strValueOf.getBytes(), 0, strValueOf.length());
                            fo.close();
                            cal3 = Calendar.getInstance();
                            timeINT = cal3.get(11);
                            if (timeINT <= 9) {
                                temp = "0" + timeINT + ":";
                            } else {
                                temp = timeINT + ":";
                            }
                            timeINT = cal3.get(12);
                            if (timeINT <= 9) {
                                temp += "0" + timeINT + ":";
                            } else {
                                temp += timeINT + ":";
                            }
                            timeINT = cal3.get(13);
                            if (timeINT <= 9) {
                                temp += "0" + timeINT;
                            } else {
                                temp += timeINT;
                            }
                            String str16 = temp + "," + Label.InputVolt + "," + Label.OutputVolt + "," + Label.Temperature + "," + Label.Load + "," + Label.Bat_level + "\n";
                            if (a <= 15) {
                                filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + String.valueOf(cal3.get(1)) + "_" + String.valueOf(cal3.get(2) + 1) + "_" + String.valueOf(cal3.get(5)) + ".csv";
                            } else if (cal3.get(11) == 0 && cal3.get(12) <= 3) {
                                filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + String.valueOf(cal3.get(1)) + "_" + String.valueOf(cal3.get(2) + 1) + "_" + String.valueOf(cal3.get(5)) + ".csv";
                            }
                            fo = new FileOutputStream(filelog, true);
                            fo.write(str16.getBytes(), 0, str16.length());
                            fo.close();
                            timeINT = cal3.get(2) + 1;
                            if (timeINT <= 9) {
                                temp += " " + cal3.get(1) + "/0" + timeINT;
                            } else {
                                temp += " " + cal3.get(1) + "/" + timeINT;
                            }
                            timeINT = cal3.get(5);
                            if (timeINT <= 9) {
                                temp += "/0" + timeINT;
                            } else {
                                temp += "/" + timeINT;
                            }
                            fo = new FileOutputStream(Label.fileUpdate, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                            if (battery_power) {
                                battery_power = false;
                                fo = new FileOutputStream(Label.fileBatStart, false);
                                fo.write(temp.getBytes(), 0, temp.length());
                                fo.close();
                                Label.Batpowerstart = temp;
                            }
                            if (battery_power_stop) {
                                battery_power_stop = false;
                                fo = new FileOutputStream(Label.fileBatStop, false);
                                fo.write(temp.getBytes(), 0, temp.length());
                                fo.close();
                                Label.Batpowerend = temp;
                            }
                            if (testflag) {
                                testflag = false;
                                fo = new FileOutputStream(Label.fileBatime, false);
                                fo.write(temp.getBytes(), 0, temp.length());
                                fo.close();
                            }
                        }
                    } catch (Exception e2) {
                        System.err.println("Exception : Class ConUSB4 : run : e1 : " + e2);
                    }
                }
                usbControlIrpCreateUsbControlIrp5.setComplete(false);
                usbControlIrpCreateUsbControlIrp6.setComplete(false);
                if (freq_BO) {
                    usbControlIrpCreateUsbControlIrp7.setComplete(false);
                }
                usbControlIrpCreateUsbControlIrp8.setComplete(false);
                usbControlIrpCreateUsbControlIrp9.setComplete(false);
                usbControlIrpCreateUsbControlIrp10.setComplete(false);
                usbControlIrpCreateUsbControlIrp11.setComplete(false);
                usbControlIrpCreateUsbControlIrp12.setComplete(false);
                b++;
                if (b == 10000) {
                    b = 90;
                }
                sleep(delay);
            }
        } catch (Exception e3) {
            System.err.println();
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
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f0, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
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
            }
            if (f4.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f4)));
                while (true) {
                    String line3 = br.readLine();
                    temp = line3;
                    if (line3 == null) {
                        break;
                    }
                    if (!temp.equals("0")) {
                        controloff = Integer.parseInt(temp);
                        a = 5;
                    }
                    temp2 = "";
                    fo = new FileOutputStream(f4, false);
                    fo.write(temp2.getBytes(), 0, temp2.length());
                    fo.close();
                }
                br.close();
            }
        } catch (Exception e) {
            System.err.println("Exception : Class ConUSB4 : read() : e : " + e);
        }
    }
}
