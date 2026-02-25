package defpackage;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

/* JADX INFO: compiled from: ConCOM1.java */
/* JADX INFO: loaded from: ConCOM1Get.class */
class ConCOM1Get extends Thread {
    InputStream in;
    ConCOM1 comApp;
    byte iUPS;
    byte SDMode;
    int BatLow;
    public static double Involtage;
    public static double Batt;
    public static int Input_Voltage;
    public static int Output_Voltage;
    public static int Load;
    public static int Input_Frequency;
    public static int Battery_Capacity;
    public static int Output_Frequency = 0;
    public static int bit7 = 0;
    public static int bit6 = 0;
    public static int bit5 = 0;
    public static int bit4 = 0;
    public static int bit3 = 1;
    public static int bit2 = 0;
    public static int bit1 = 0;
    public static int bit0 = 0;
    public static int overload = 0;
    public static int timeINT = 0;
    public static int b = 0;
    public static int[] Byte_9 = {0, 0, 0, 0, 0, 0, 0, 0};
    public static int[] Byte_10 = {0, 0, 0, 0, 0, 0, 0, 0};
    public static String temp = "(";
    public static String c1 = "";
    public static String data1 = "";
    public static String filelog = "";
    public static String timeST = "";
    public static Calendar cal3 = null;
    public static boolean testflag = false;
    public static boolean countdownthreadflag = false;
    public static boolean selftestflag = true;
    public static boolean batbadflag = true;
    public static boolean upsfailflag = true;
    public static boolean battery_power = false;
    public static boolean battery_power_stop = false;
    static boolean upsoffBO = false;
    public static FileOutputStream fo = null;
    static byte[] data = {80, 67, 77, 82, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 69, 78, 68};

    public ConCOM1Get(InputStream inputStream, ConCOM1 conCOM1) {
        this.in = inputStream;
        this.comApp = conCOM1;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int i;
        BufferedInputStream bufferedInputStream = null;
        try {
            int[] iArr = new int[16];
            iArr[0] = 0;
            iArr[1] = 0;
            iArr[2] = 0;
            iArr[3] = 0;
            iArr[4] = 0;
            iArr[5] = 0;
            iArr[6] = 0;
            iArr[7] = 0;
            iArr[8] = 0;
            iArr[9] = 0;
            iArr[10] = 0;
            iArr[11] = 0;
            iArr[12] = 0;
            iArr[13] = 0;
            iArr[14] = 0;
            iArr[15] = 0;
            bufferedInputStream = new BufferedInputStream(this.in);
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            byte[] bArr = new byte[64];
            while (ConCOM1Set.roopINT >= 0 && (i = bufferedInputStream.read(bArr)) != -1) {
                for (int i5 = 0; i5 < i; i5++) {
                    try {
                        int i6 = bArr[i5];
                        if (i6 < 0) {
                            i6 += 256;
                        }
                        iArr[i2] = i6;
                        if (i2 == 9) {
                            i3 = i6;
                        } else if (i2 == 10) {
                            i4 = i6;
                        }
                        i2++;
                        if (i2 == 16) {
                            i2 = 0;
                        }
                    } catch (Exception e) {
                        System.err.println("Exception : Class ConCOM1Get : run() : e1 :" + e);
                    }
                }
                byte_9tobit(i3);
                byte_10tobit(i4);
                caculate(iArr);
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (Exception e2) {
                    System.err.println("Exception : Class ConCOM1Get : run() : e3 :" + e2);
                }
            }
        } catch (Exception e3) {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (Exception e4) {
                    System.err.println("Exception : Class ConCOM1Get : run() : e3 :" + e4);
                }
            }
        } catch (Throwable th) {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (Exception e5) {
                    System.err.println("Exception : Class ConCOM1Get : run() : e3 :" + e5);
                    throw th;
                }
            }
            throw th;
        }
    }

    public void byte_10tobit(int i) {
        Byte_10[7] = i / 128;
        int i2 = i % 128;
        Byte_10[6] = i2 / 64;
        int i3 = i2 % 64;
        Byte_10[5] = i3 / 32;
        int i4 = i3 % 32;
        Byte_10[4] = i4 / 16;
        int i5 = i4 % 16;
        Byte_10[3] = i5 / 8;
        int i6 = i5 % 8;
        Byte_10[2] = i6 / 4;
        int i7 = i6 % 4;
        Byte_10[1] = i7 / 2;
        Byte_10[0] = i7 % 2;
    }

    public void byte_9tobit(int i) {
        Byte_9[7] = i / 128;
        int i2 = i % 128;
        Byte_9[6] = i2 / 64;
        int i3 = i2 % 64;
        Byte_9[5] = i3 / 32;
        int i4 = i3 % 32;
        Byte_9[4] = i4 / 16;
        int i5 = i4 % 16;
        Byte_9[3] = i5 / 8;
        int i6 = i5 % 8;
        Byte_9[2] = i6 / 4;
        int i7 = i6 % 4;
        Byte_9[1] = i7 / 2;
        Byte_9[0] = i7 % 2;
    }

    public void caculate(int[] iArr) {
        if (b == 0) {
            try {
                temp = "POWERCOM";
                fo = new FileOutputStream(Label.fileCompany, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            } catch (Exception e) {
                System.err.println("Exception : Class ConCOM1 : caculate() : e : " + e);
            }
        }
        temp = "(";
        if (iArr[5] >= 240) {
            this.iUPS = (byte) 15;
        }
        Input_Voltage = cacuinvoltage(iArr);
        Label.InputVolt = Input_Voltage;
        if (Input_Voltage == 0) {
            temp += "000.0";
        } else if (Input_Voltage < 10) {
            temp += "00" + Input_Voltage + ".0";
        } else if (Input_Voltage < 100) {
            temp += "0" + Input_Voltage + ".0";
        } else {
            temp += Input_Voltage + ".0";
        }
        data[5] = (byte) (Input_Voltage / 256);
        data[6] = (byte) (Input_Voltage % 256);
        if (Label.InputVoltMax < Label.InputVolt) {
            Label.InputVoltMax = Label.InputVolt;
        }
        if (Label.InputVoltMini > Label.InputVolt) {
            Label.InputVoltMini = Label.InputVolt;
        }
        temp += " 000.0 ";
        Output_Voltage = cacuonvoltage(iArr);
        Label.OutputVolt = Output_Voltage;
        if (Output_Voltage == 0) {
            temp += "000.0";
        } else if (Output_Voltage < 10) {
            temp += "00" + Output_Voltage + ".0";
        } else if (Output_Voltage < 100) {
            temp += "0" + Output_Voltage + ".0";
        } else {
            temp += Output_Voltage + ".0";
        }
        data[7] = (byte) (Output_Voltage / 256);
        data[8] = (byte) (Output_Voltage % 256);
        Load = cacuload(iArr);
        Label.Load = Load;
        if (Load < 10) {
            temp += " 00" + Load;
        } else {
            temp += " 0" + Load;
        }
        data[11] = (byte) Load;
        Input_Frequency = ipfrequency(iArr);
        if (Input_Frequency < 10) {
            temp += " 0" + Input_Frequency + ".0";
        } else {
            temp += " " + Input_Frequency + ".0";
        }
        data[13] = (byte) Input_Frequency;
        Battery_Capacity = battery(iArr);
        Label.Bat_level = Battery_Capacity;
        if (Battery_Capacity >= 99) {
            temp += " 0100";
        } else if (Battery_Capacity >= 10) {
            temp += " 00" + Battery_Capacity;
        } else if (Battery_Capacity >= 3) {
            temp += " 000" + Battery_Capacity;
        } else {
            temp += " 0000";
        }
        data[12] = (byte) Battery_Capacity;
        Output_Frequency = opfrequency(iArr);
        Label.InputFreq = Output_Frequency;
        if (Output_Frequency < 10) {
            temp += " 0" + Output_Frequency + ".0";
        } else {
            temp += " " + Output_Frequency + ".0";
        }
        data[14] = (byte) Output_Frequency;
        data[15] = -1;
        data[16] = 1;
        if (Byte_9[0] == 1) {
            if (Byte_10[2] != 1) {
                temp += " 1";
                if (bit7 == 0) {
                    bit7 = 1;
                    battery_power = true;
                    System.out.println("UPSMON : Power Failure");
                    Record record = UPSMON.RecordAPP;
                    Record.set(new String("Power_Failure"));
                    Label.PowerStatus = "Battery Power";
                    ConCOM1Set.soundBO = true;
                    ConCOM1Set.green_modeBO = true;
                    try {
                        Label.Bat_times++;
                        temp = String.valueOf(Label.Bat_times);
                        fo = new FileOutputStream(Label.fileBatTimes, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    } catch (Exception e2) {
                        System.err.println("Exception : Class ConCOM1 : caculate() : ee : " + e2);
                    }
                    countdownthreadflag = true;
                    CountDOWN.usrcmd = true;
                    new CountDOWN().start();
                    System.out.println("UPSMON : Linux count down " + Label.osdelay + " seconds");
                }
                data[18] = 1;
            }
        } else {
            temp += " 0";
            if (bit7 == 1) {
                bit7 = 0;
                battery_power_stop = true;
                System.out.println("UPSMON : Power Restore");
                Record record2 = UPSMON.RecordAPP;
                Record.set(new String("Power_Restore"));
                countdownthreadflag = false;
                CountDOWN.roopBO = false;
            }
            data[18] = 0;
            Label.PowerStatus = "Normal";
        }
        if (Byte_9[1] == 1) {
            temp += "1";
            if (bit6 == 0) {
                bit6 = 1;
                System.out.println("UPSMON : Low Battery");
                Record record3 = UPSMON.RecordAPP;
                Record.set(new String("Low_Battery"));
                Label.PowerStatus = "Battery Low";
                Connect.lowbat = 1;
            }
            data[19] = 1;
        } else {
            temp += "0";
            if (bit6 == 1) {
                bit6 = 0;
                Connect.lowbat = 0;
            }
            data[19] = 0;
        }
        if (Byte_9[5] == 1) {
            if (overload == 0) {
                overload = 1;
                System.out.println("UPSMON : Overload");
                Record record4 = UPSMON.RecordAPP;
                Record.set(new String("Overload"));
                Label.PowerStatus = "Overload";
            }
            data[20] = 1;
        } else {
            if (overload == 1) {
                overload = 0;
                System.out.println("UPSMON : Normal");
                Record record5 = UPSMON.RecordAPP;
                Record.set(new String("Normal"));
            }
            data[20] = 0;
        }
        if (Byte_9[3] == 1) {
            temp += "1";
            if (bit5 == 0) {
                bit5 = 1;
                if (Input_Voltage > Output_Voltage) {
                    System.out.println("UPSMON : Buck");
                    Record record6 = UPSMON.RecordAPP;
                    Record.set(new String("Buck"));
                    Label.PowerStatus = "AVR Buck";
                    data[17] = 3;
                } else if (Output_Voltage > Input_Voltage) {
                    System.out.println("UPSMON : Boost");
                    Record record7 = UPSMON.RecordAPP;
                    Record.set(new String("Boost"));
                    Label.PowerStatus = "AVR Boost";
                    data[17] = 2;
                }
            }
        } else {
            temp += "0";
            if (bit5 == 1) {
                bit5 = 0;
                System.out.println("UPSMON : Normal");
                Record record8 = UPSMON.RecordAPP;
                Record.set(new String("Normal"));
            }
            data[17] = 0;
        }
        if (Byte_10[0] == 1) {
            temp += "1";
            if (bit4 == 0) {
                bit4 = 1;
                System.out.println("UPSMON : UPS Failed");
                Record record9 = UPSMON.RecordAPP;
                Record.set(new String("UPS_Failed"));
            }
            data[21] = 1;
        } else {
            temp += "0";
            if (bit4 == 1 || !upsfailflag) {
                bit4 = 0;
                batbadflag = true;
                System.out.println("UPSMON : Normal");
                Record record10 = UPSMON.RecordAPP;
                Record.set(new String("Normal"));
            }
            data[21] = 0;
        }
        if (Byte_10[1] == 1) {
            temp += "3";
            if (batbadflag) {
                bit3 = 2;
                batbadflag = false;
                System.out.println("UPSMON : Battery Failed");
                Record record11 = UPSMON.RecordAPP;
                Record.set(new String("Battery_Failed"));
                try {
                    Label.Battestresult = "Battery Failed";
                    temp = "Battery Failed";
                    fo = new FileOutputStream(Label.fileBatResult, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                } catch (Exception e3) {
                    System.err.println("Exception : Class ConCOM1Get : caculate() : e3 : " + e3);
                }
            }
            data[22] = 1;
        } else {
            temp += "1";
            if (bit3 == 2 || !batbadflag) {
                bit3 = 0;
                batbadflag = true;
                System.out.println("UPSMON : Battery Normal");
                Record record12 = UPSMON.RecordAPP;
                Record.set(new String("Battery_Normal"));
            }
            data[22] = 0;
        }
        if (Byte_10[2] == 1) {
            temp += "1";
            if (selftestflag) {
                selftestflag = false;
                try {
                    ConCOM1Set.soundBO = true;
                    System.out.println("UPSMON : UPS Battery Test");
                    Record record13 = UPSMON.RecordAPP;
                    Record.set(new String("UPS_Battery_Test"));
                    Label.Bat_test_times++;
                    temp = String.valueOf(Label.Bat_test_times);
                    fo = new FileOutputStream(Label.fileBatTestTimes, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    testflag = true;
                    fo.close();
                } catch (Exception e4) {
                    System.err.println("Exception : Class ConCOM1Get : caculate() : e1 : " + e4);
                }
            }
            data[23] = 1;
        } else {
            temp += "0";
            if (!selftestflag) {
                selftestflag = true;
                data[23] = 0;
                System.out.println("UPSMON : Battery Normal");
                Record record14 = UPSMON.RecordAPP;
                Record.set(new String("Battery_Normal"));
                try {
                    Label.Battestresult = "OK";
                    temp = "Battery Normal";
                    fo = new FileOutputStream(Label.fileBatResult, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                } catch (Exception e5) {
                    System.err.println("Exception : Class ConCOM1Get : caculate() : e2 : " + e5);
                }
            }
        }
        if (Byte_10[4] == 1) {
            if (!upsoffBO) {
                upsoffBO = true;
                bit1 = 1;
            }
            temp += "0";
        } else {
            if (upsoffBO) {
                Record record15 = UPSMON.RecordAPP;
                Record.set(new String("UPS_OFF"));
                System.out.println("UPSMON : UPS OFF");
                Record record16 = UPSMON.RecordAPP;
                Record.set(new String("UPS_ON"));
                System.out.println("UPSMON : UPS ON");
                temp += "1";
            } else {
                temp += "0";
            }
            upsoffBO = false;
            bit1 = 0;
        }
        b++;
        if (b == 1000) {
            b = 10;
        }
        if (Byte_10[3] == 0) {
            temp += "1";
        } else {
            temp += "0";
        }
        if (temp.length() >= 44) {
            if (!ConCOM1.BO1200) {
                ConCOM1.BO1200 = true;
            }
            try {
                byte[] bytes = temp.getBytes();
                int length = bytes.length;
                if (length != 0) {
                    fo = new FileOutputStream(Label.fileget, false);
                    fo.write(bytes, 0, length);
                    fo.close();
                }
            } catch (Exception e6) {
                System.err.println("Exception : Class ConCOM1Get : caculate : e2 : " + e6);
            }
            try {
                cal3 = Calendar.getInstance();
                timeINT = cal3.get(11);
                if (timeINT <= 9) {
                    data1 = "0" + timeINT + ":";
                } else {
                    data1 = timeINT + ":";
                }
                timeINT = cal3.get(12);
                if (timeINT <= 9) {
                    data1 += "0" + timeINT + ":";
                } else {
                    data1 += timeINT + ":";
                }
                timeINT = cal3.get(13);
                if (timeINT <= 9) {
                    data1 += "0" + timeINT + ",";
                } else {
                    data1 += timeINT + ",";
                }
                data1 += Input_Voltage + "," + Output_Voltage + "," + Input_Frequency + "," + Load + "," + Battery_Capacity + "\n";
                if (b <= 3) {
                    filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + String.valueOf(cal3.get(1)) + "_" + String.valueOf(cal3.get(2) + 1) + "_" + String.valueOf(cal3.get(5)) + ".csv";
                } else if (cal3.get(11) == 0 && cal3.get(12) <= 3) {
                    filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + String.valueOf(cal3.get(1)) + "_" + String.valueOf(cal3.get(2) + 1) + "_" + String.valueOf(cal3.get(5)) + ".csv";
                }
                if (!timeST.equals(data1)) {
                    fo = new FileOutputStream(filelog, true);
                    fo.write(data1.getBytes(), 0, data1.length());
                    fo.close();
                    timeST = data1;
                }
                timeINT = cal3.get(11);
                if (timeINT <= 9) {
                    data1 = "0" + timeINT + ":";
                } else {
                    data1 = timeINT + ":";
                }
                timeINT = cal3.get(12);
                if (timeINT <= 9) {
                    data1 += "0" + timeINT + ":";
                } else {
                    data1 += timeINT + ":";
                }
                timeINT = cal3.get(13);
                if (timeINT <= 9) {
                    data1 += "0" + timeINT;
                } else {
                    data1 += timeINT;
                }
                timeINT = cal3.get(2) + 1;
                if (timeINT <= 9) {
                    data1 += " " + cal3.get(1) + "/0" + timeINT;
                } else {
                    data1 += " " + cal3.get(1) + "/" + timeINT;
                }
                timeINT = cal3.get(5);
                if (timeINT <= 9) {
                    data1 += "/0" + timeINT;
                } else {
                    data1 += "/" + timeINT;
                }
                fo = new FileOutputStream(Label.fileUpdate, false);
                fo.write(data1.getBytes(), 0, data1.length());
                fo.close();
                if (battery_power) {
                    battery_power = false;
                    fo = new FileOutputStream(Label.fileBatStart, false);
                    fo.write(data1.getBytes(), 0, data1.length());
                    Label.Batpowerstart = data1;
                    fo.close();
                }
                if (battery_power_stop) {
                    battery_power_stop = false;
                    fo = new FileOutputStream(Label.fileBatStop, false);
                    fo.write(data1.getBytes(), 0, data1.length());
                    Label.Batpowerend = data1;
                    fo.close();
                }
                if (testflag) {
                    testflag = false;
                    fo = new FileOutputStream(Label.fileBatime, false);
                    fo.write(data1.getBytes(), 0, data1.length());
                    fo.close();
                }
            } catch (Exception e7) {
                System.err.println("Exception : Class ConCOM1Get : caculate : e3 : " + e7);
            }
        }
    }

    public int battery(int[] iArr) {
        if (this.iUPS == 15) {
            Batt = iArr[1];
        }
        if ((this.BatLow == 1 && Batt > 29.0d) || (this.BatLow == 0 && Batt < 29.0d)) {
            Batt = 29.0d;
        }
        if (Batt < 0.0d) {
            Batt = 0.0d;
        }
        if (Batt > 100.0d) {
            Batt = 100.0d;
        }
        Battery_Capacity = (int) Math.round(Batt);
        return Battery_Capacity;
    }

    public int opfrequency(int[] iArr) {
        int i = 0;
        if (iArr[6] != 0 && this.SDMode != 1) {
            i = 4807 / iArr[6];
            if (this.iUPS == 15) {
                i = iArr[6];
            }
        }
        if (i > 90) {
            i = 0;
        }
        int iRound = Math.round(i);
        String.valueOf(iRound);
        return iRound;
    }

    public int cacuinvoltage(int[] iArr) {
        if (this.iUPS == 15) {
            Involtage = iArr[2] * 2;
        }
        if (Involtage < 25.0d) {
            Involtage = 0.0d;
        }
        Input_Voltage = (int) Math.round(Involtage);
        return Input_Voltage;
    }

    public int cacuonvoltage(int[] iArr) {
        if (this.iUPS == 15) {
            Output_Voltage = iArr[3] * 2;
        }
        Output_Voltage = Math.round(Output_Voltage);
        return Output_Voltage;
    }

    public int cacuload(int[] iArr) {
        if (this.iUPS == 15) {
            Load = iArr[0];
        }
        Load = Math.round(Load);
        return Load;
    }

    public int ipfrequency(int[] iArr) {
        if (iArr[4] != 0) {
            Input_Frequency = 4807 / iArr[4];
        } else {
            Input_Frequency = 0;
        }
        if (this.iUPS == 15) {
            Input_Frequency = iArr[4];
        }
        if (Input_Voltage <= 20) {
            Input_Frequency = 0;
        }
        Input_Frequency = Math.round(Input_Frequency);
        return Input_Frequency;
    }
}
