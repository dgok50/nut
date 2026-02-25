package defpackage;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Calendar;

/* JADX INFO: compiled from: ConPro.java */
/* JADX INFO: loaded from: ConPROGet.class */
class ConPROGet extends Thread {
    DatagramSocket server;
    public static Calendar cal1;
    public static Calendar cal2;
    public static File f1 = new File("EXT" + UPSMON.separaST + "JSystem" + UPSMON.separaST + "Rt.bak");
    public static String c = "EXT" + UPSMON.separaST + "JSystem" + UPSMON.separaST + "get.bak";
    public static String temp = "";
    public static String fileLog = "";
    public static String data1 = "";
    public static int output_frequency = 0;
    public static int temperature = 0;
    public static int on_line = 0;
    public static int avr_bypass = 0;
    public static int inv = 0;
    public static int bat_low = 0;
    public static int over_load = 0;
    public static int ups_fail = 0;
    public static int bad_bat = 0;
    public static int self_test = 0;
    public static int connect = 0;
    public static int schedule_off = 0;
    public static int timeINT = 0;
    public static boolean countdownthreadflag = false;
    public static boolean testflag = false;
    public static boolean battery_power = false;
    public static boolean battery_power_stop = false;
    public static FileOutputStream fo = null;
    public static int b = 0;
    public static int b7 = 0;
    public static int b6 = 0;
    public static int b5 = 0;
    public static int b4 = 0;
    public static int b3 = 0;
    public static int b2 = 0;
    public static int b1 = 0;
    public static int b0 = 0;
    public static int b8 = 0;
    public static int b9 = 0;
    public static Calendar cal3 = null;

    public ConPROGet(DatagramSocket datagramSocket) {
        this.server = null;
        this.server = datagramSocket;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        byte[] bArr = new byte[512];
        byte[] bArr2 = new byte[30];
        int[] iArr = new int[30];
        try {
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            while (true) {
                try {
                    this.server.receive(datagramPacket);
                    int length = datagramPacket.getLength();
                    for (int i = 0; i < length; i++) {
                        int i2 = bArr[i];
                        if (i2 < 0) {
                            i2 += 256;
                        }
                        iArr[i] = i2;
                    }
                } catch (Exception e) {
                    System.err.println("Exception : Class ConPROGet : run : ea : " + e);
                }
                calculate(iArr);
            }
        } catch (Exception e2) {
            System.err.println("Exception : Class ConPROGet : run : e1 : " + e2);
        }
    }

    public static void calculate(int[] iArr) {
        if (iArr[0] == 80 && iArr[1] == 67 && iArr[2] == 77 && iArr[3] == 82 && iArr[4] == 1) {
            StringBuffer stringBuffer = new StringBuffer("(");
            Label.InputVolt = (iArr[5] * 256) + iArr[6];
            if (Label.InputVolt == 0) {
                stringBuffer.append("000.0 000.0 ");
            } else if (Label.InputVolt < 10) {
                stringBuffer.append("00" + Label.InputVolt + ".0 000.0 ");
            } else if (Label.InputVolt < 100) {
                stringBuffer.append("0" + Label.InputVolt + ".0 000.0 ");
            } else {
                stringBuffer.append(Label.InputVolt + ".0 000.0 ");
            }
            if (Label.InputVoltMax < Label.InputVolt) {
                Label.InputVoltMax = Label.InputVolt;
            }
            if (Label.InputVoltMini > Label.InputVolt) {
                Label.InputVoltMini = Label.InputVolt;
            }
            Label.OutputVolt = (iArr[7] * 256) + iArr[8];
            if (Label.OutputVolt == 0) {
                stringBuffer.append("000.0 ");
            } else if (Label.OutputVolt < 10) {
                stringBuffer.append("00" + Label.OutputVolt + ".0 ");
            } else if (Label.OutputVolt < 100) {
                stringBuffer.append("0" + Label.OutputVolt + ".0 ");
            } else {
                stringBuffer.append(Label.OutputVolt + ".0 ");
            }
            try {
                Label.Bat_backup_time = ((iArr[9] * 256) + iArr[10]) / 60;
                temp = String.valueOf(Label.Bat_backup_time);
                fo = new FileOutputStream(Label.fileRt, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
            } catch (Exception e) {
                System.err.println("Exception ConPROGet : calculate() : " + e);
            }
            Label.Load = iArr[11];
            if (Label.Load == 0) {
                stringBuffer.append("000 ");
            } else if (Label.Load < 10) {
                stringBuffer.append("00" + Label.Load + " ");
            } else if (Label.Load < 100) {
                stringBuffer.append("0" + Label.Load + " ");
            } else {
                stringBuffer.append(Label.Load + " ");
            }
            Label.InputFreq = iArr[13];
            if (Label.InputFreq == 0) {
                stringBuffer.append("00.0 ");
            } else if (Label.InputFreq < 10) {
                stringBuffer.append("0" + Label.InputFreq + ".0 ");
            } else {
                stringBuffer.append(Label.InputFreq + ".0 ");
            }
            Label.Bat_level = iArr[12];
            if (Label.Bat_level == 0) {
                stringBuffer.append("000 ");
            } else if (Label.Bat_level < 10) {
                stringBuffer.append("00" + Label.Bat_level + " ");
            } else if (Label.Bat_level < 100) {
                stringBuffer.append("0" + Label.Bat_level + " ");
            } else if (Label.Bat_level >= 100) {
                stringBuffer.append("100 ");
            }
            Label.Temperature = iArr[15];
            if (Label.Temperature == -1) {
                output_frequency = iArr[14];
                stringBuffer.append(output_frequency + ".0 ");
            } else {
                stringBuffer.append(Label.Temperature + ".0 ");
            }
            inv = iArr[18];
            if (inv == 0) {
                if (b7 == 1) {
                    System.out.println("UPSMON : Power Restore");
                    Record record = UPSMON.RecordAPP;
                    Record.set(new String("Power_Restore"));
                    battery_power_stop = true;
                    CountDOWN.roopBO = false;
                }
                stringBuffer.append("0");
                b7 = 0;
                Label.PowerStatus = "AC Utility Power";
            } else {
                if (b7 == 0) {
                    System.out.println("UPSMON : Power Failure");
                    Record record2 = UPSMON.RecordAPP;
                    Record.set(new String("Power_Failure"));
                    Label.PowerStatus = "Battery Power";
                    battery_power = true;
                    try {
                        Label.Bat_times++;
                        temp = String.valueOf(Label.Bat_times);
                        fo = new FileOutputStream(Label.fileBatTimes, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    } catch (Exception e2) {
                        System.err.println("Exception : Class ConPROGET : run() : e3 : " + e2);
                    }
                    if (Integer.parseInt(UPSMON.shutdowntype) != 2) {
                        CountDOWN.usrcmd = true;
                        new CountDOWN().start();
                        System.out.println("UPSMON : Linux count down " + Label.osdelay + " seconds");
                    }
                }
                stringBuffer.append("1");
                b7 = 1;
            }
            bat_low = iArr[19];
            if (bat_low == 0) {
                stringBuffer.append("0");
                b6 = 0;
                Connect.lowbat = 0;
            } else {
                if (b6 == 0) {
                    System.out.println("UPSMON : Low Battery");
                    Record record3 = UPSMON.RecordAPP;
                    Record.set(new String("Low_Battery"));
                    Connect.lowbat = 1;
                    if (b7 == 1) {
                        Label.PowerStatus = "Low Battery";
                    }
                }
                stringBuffer.append("1");
                b6 = 1;
            }
            avr_bypass = iArr[17];
            if (avr_bypass == 0) {
                if (b5 != 0) {
                    System.out.println("UPSMON : UPS Normal");
                    Record record4 = UPSMON.RecordAPP;
                    Record.set(new String("UPS_Normal"));
                }
                stringBuffer.append("0");
                b5 = 0;
            } else if (avr_bypass == 1) {
                if (b5 == 0) {
                    System.out.println("UPSMON : Bypass");
                    Record record5 = UPSMON.RecordAPP;
                    Record.set(new String("Bypass"));
                    if (b7 == 0) {
                        Label.PowerStatus = "Bypass";
                    }
                }
                stringBuffer.append("1");
                b5 = 1;
            } else if (avr_bypass == 2) {
                if (b5 == 0) {
                    System.out.println("UPSMON : AVR Boost");
                    Record record6 = UPSMON.RecordAPP;
                    Record.set(new String("AVR_Boost"));
                    if (b7 == 0) {
                        Label.PowerStatus = "AVR Boost";
                    }
                }
                stringBuffer.append("1");
                b5 = 1;
            } else if (avr_bypass == 3) {
                if (b5 == 0) {
                    System.out.println("UPSMON : AVR Buck");
                    Record record7 = UPSMON.RecordAPP;
                    Record.set(new String("AVR_Buck"));
                    if (b7 == 0) {
                        Label.PowerStatus = "AVR Buck";
                    }
                }
                stringBuffer.append("1");
                b5 = 1;
            }
            ups_fail = iArr[21];
            if (ups_fail == 0) {
                if (b4 == 1) {
                    System.out.println("UPSMON : Normal");
                    Record record8 = UPSMON.RecordAPP;
                    Record.set(new String("Normal"));
                }
                stringBuffer.append("0");
                b4 = 0;
            } else {
                if (b4 == 0) {
                    System.out.println("UPSMON : UPS Fail");
                    Record record9 = UPSMON.RecordAPP;
                    Record.set(new String("UPS_Fail"));
                    Label.PowerStatus = "UPS Fail";
                }
                stringBuffer.append("1");
                b4 = 1;
            }
            bad_bat = iArr[22];
            on_line = iArr[16];
            if (bad_bat == 1) {
                if (iArr[16] == 0) {
                    if (b3 != 2) {
                        System.out.println("UPSMON : Battery Failed");
                        Record record10 = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Failed"));
                        try {
                            temp = "Battery Normal";
                            fo = new FileOutputStream(Label.fileBatResult, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e3) {
                            System.err.println("Exception : Class ConPROGET : run() : e0 : " + e3);
                        }
                    }
                    stringBuffer.append("2");
                    b3 = 2;
                } else {
                    if (b3 != 2) {
                        System.out.println("UPSMON : Battery Failed");
                        Record record11 = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Failed"));
                        try {
                            temp = "Battery Normal";
                            fo = new FileOutputStream(Label.fileBatResult, false);
                            fo.write(temp.getBytes(), 0, temp.length());
                            fo.close();
                        } catch (Exception e4) {
                            System.err.println("Exception : Class ConPROGET : run() : e1 : " + e4);
                        }
                    }
                    stringBuffer.append("3");
                    b3 = 3;
                }
            } else if (on_line == 1) {
                stringBuffer.append("1");
                b3 = 1;
            } else if (on_line == 0) {
                stringBuffer.append("0");
                b3 = 0;
            }
            self_test = iArr[23];
            if (self_test == 0) {
                stringBuffer.append("000");
                if (b2 == 1 && bad_bat == 0) {
                    System.out.println("UPSMON : Battery Normal");
                    Record record12 = UPSMON.RecordAPP;
                    Record.set(new String("Battery_Normal"));
                    Label.Battestresult = "OK";
                    try {
                        temp = "Battery Normal";
                        fo = new FileOutputStream(Label.fileBatResult, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    } catch (Exception e5) {
                        System.err.println("Exception : Class ConPROGET : run() : e2 : " + e5);
                    }
                }
                b2 = 0;
            } else {
                stringBuffer.append("100");
                if (b2 == 0) {
                    System.out.println("UPSMON : UPS Battery Test");
                    Record record13 = UPSMON.RecordAPP;
                    Record.set(new String("UPS_Battery_Test"));
                    Label.Bat_test_times++;
                    testflag = true;
                    try {
                        temp = String.valueOf(Label.Bat_test_times);
                        fo = new FileOutputStream(Label.fileBatTestTimes, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    } catch (Exception e6) {
                        System.err.println("Exception : Class ConPROGet : caculate() : ee :" + e6);
                    }
                }
                b2 = 1;
            }
            over_load = iArr[20];
            if (over_load == 0) {
                if (b8 == 1) {
                    System.out.println("UPSMON : Normal");
                    Record record14 = UPSMON.RecordAPP;
                    Record.set(new String("Normal"));
                }
                b8 = 0;
            } else {
                if (b8 == 0) {
                    System.out.println("UPSMON : Over Load");
                    Record record15 = UPSMON.RecordAPP;
                    Record.set(new String("Over_Load"));
                }
                b8 = 1;
            }
            connect = iArr[24];
            if (connect == 0) {
                if (b9 == 1) {
                    System.out.println("UPSMON : UPS Connection");
                    Record record16 = UPSMON.RecordAPP;
                    Record.set(new String("UPS_Connection"));
                }
                b9 = 0;
            } else {
                if (b9 == 0) {
                    System.out.println("UPSMON : UPS Disconnect");
                    Record record17 = UPSMON.RecordAPP;
                    Record.set(new String("UPS_Disconnect"));
                }
                b9 = 1;
            }
            schedule_off = iArr[25];
            if (schedule_off == 1) {
                try {
                    String str = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                    System.out.println("UPSMON : Schedule OS Shutdown !!");
                    Record record18 = UPSMON.RecordAPP;
                    Record.set(new String("Schedule_OS_Shutdown"));
                    sleep(1000L);
                    Runtime.getRuntime().exec(str);
                    sleep(1000L);
                    System.exit(0);
                } catch (Exception e7) {
                    System.err.println("Exception : Class CountDOWN : closeOS1() : e : " + e7);
                }
            }
            temp = stringBuffer.toString();
            if (temp.length() >= 44) {
                try {
                    byte[] bytes = temp.getBytes();
                    int length = bytes.length;
                    if (length != 0) {
                        fo = new FileOutputStream(Label.fileget, false);
                        fo.write(bytes, 0, length);
                        fo.close();
                    }
                    cal1 = Calendar.getInstance();
                    timeINT = cal1.get(11);
                    if (timeINT <= 9) {
                        data1 = "0" + timeINT + ":";
                    } else {
                        data1 = timeINT + ":";
                    }
                    timeINT = cal1.get(12);
                    if (timeINT <= 9) {
                        data1 += "0" + timeINT + ":";
                    } else {
                        data1 += timeINT + ":";
                    }
                    timeINT = cal1.get(13);
                    if (timeINT <= 9) {
                        data1 += "0" + timeINT;
                    } else {
                        data1 += timeINT;
                    }
                    temp = data1 + "," + Label.InputVolt + "," + Label.OutputVolt + "," + Label.Temperature + "," + Label.Load + "," + Label.Bat_level + "\n";
                    if (b <= 3) {
                        fileLog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + String.valueOf(cal1.get(1)) + "_" + String.valueOf(cal1.get(2) + 1) + "_" + String.valueOf(cal1.get(5)) + ".csv";
                    } else if (cal1.get(11) == 0 && cal1.get(12) <= 3) {
                        fileLog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + String.valueOf(cal1.get(1)) + "_" + String.valueOf(cal1.get(2) + 1) + "_" + String.valueOf(cal1.get(5)) + ".csv";
                    }
                    fo = new FileOutputStream(fileLog, true);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                    timeINT = cal1.get(2) + 1;
                    if (timeINT <= 9) {
                        data1 += " " + cal1.get(1) + "/0" + timeINT;
                    } else {
                        data1 += " " + cal1.get(1) + "/" + timeINT;
                    }
                    timeINT = cal1.get(5);
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
                    temp = "DATE      : " + data1 + "\n";
                    if (Label.Hostname.length() != 0) {
                        temp += "HOSTNAME  : " + Label.Hostname + "\n";
                    }
                    temp += "VERSION   : UPSMON PRO V1.22\n";
                    temp += "INTERFACE : UPSMON PRO Server\n";
                    temp += "STARTTIME : " + Label.UpsStartTime + " \n";
                    temp += "STATUS    : " + Label.PowerStatus + " \n";
                    temp += "LINEV     : " + Label.InputVolt + " Volts \n";
                    temp += "LOADPCT   : " + Label.Load + " Percent Load Capacity \n";
                    temp += "BCHARGE   : " + Label.Bat_level + " Percent \n";
                    if (Label.Bat_backup_time > 1) {
                        temp += "TIMELEFT  : " + Label.Bat_backup_time + " Minutes \n";
                    }
                    temp += "MBATTCHG  : " + Label.oscapacity + " Percent \n";
                    temp += "MINTIMEL  : " + Label.osminutes + " Minutes \n";
                    temp += "MAXTIME   : " + Label.osdelay + " Seconds\n";
                    temp += "MAXLINEV  : " + Label.InputVoltMax + " Volts \n";
                    temp += "MINLINEV  : " + Label.InputVoltMini + " Volts \n";
                    temp += "OUTPUTV   : " + Label.OutputVolt + " Volts \n";
                    temp += "SENSE     : High \n";
                    if (Label.Temperature != -1) {
                        temp += "ITEMP     : " + Label.Temperature + " C Internal \n";
                    }
                    temp += "LINEFREQ  : " + Label.InputFreq + " Hz \n";
                    if (Label.Batpowerstart.length() > 0) {
                        temp += "XONBATT   : " + Label.Batpowerstart + " \n";
                    }
                    if (Label.Batpowerend.length() > 0) {
                        temp += "XOFFBATT  : " + Label.Batpowerend + " \n";
                    }
                    if (Label.Battestresult.length() > 0) {
                        temp += "SELFTEST  : " + Label.Battestresult + " \n";
                    }
                    fo = new FileOutputStream(Label.fileStatus, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                } catch (Exception e8) {
                    System.err.println("Exception : Class ConPROGet : caculate() : e2 :" + e8);
                }
            }
            b++;
            if (b == 1000) {
                b = 10;
            }
        }
    }
}
