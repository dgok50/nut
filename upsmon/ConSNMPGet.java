package defpackage;

import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Calendar;

/* JADX INFO: compiled from: ConSNMP.java */
/* JADX INFO: loaded from: ConSNMPGet.class */
class ConSNMPGet extends Thread {
    DatagramSocket server;
    public static Calendar cal1;
    public static Calendar cal2;
    public static int[] buffer2 = new int[512];
    public static int timeINT = 0;
    public static int receive_length = 0;
    public static int b = 0;
    public static int certified_length = 0;
    public static int headlength = 0;
    public static int outputlevel1 = 0;
    public static int outputlevel2 = 0;
    public static int batt_judge = 0;
    public static boolean normalBO = false;
    public static boolean powerflag = false;
    public static boolean testflag = false;
    public static boolean battery_power = false;
    public static boolean battery_power_stop = false;
    public static String temp = "";
    public static String battery = "";
    public static String fileLog = "";
    public static String data1 = "";
    public static String packetST = "";
    public static String MMM = "";
    public static String NNN = "000.0";
    public static String PPP = "";
    public static String QQQ = "";
    public static String RR = "";
    public static String TT = "";
    public static StringBuffer sb = new StringBuffer();
    public static FileOutputStream fo = null;
    public static Calendar cal3 = null;

    public ConSNMPGet(DatagramSocket datagramSocket) {
        this.server = null;
        this.server = datagramSocket;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        byte[] bArr = new byte[512];
        try {
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            while (true) {
                try {
                    this.server.receive(datagramPacket);
                    receive_length = datagramPacket.getLength();
                    if (receive_length != 0) {
                        packetST = new String(datagramPacket.getData(), 0, receive_length);
                        if (packetST.startsWith("0")) {
                            snmpcard(receive_length, bArr);
                        }
                    }
                    b++;
                    if (b == 1000) {
                        b = 10;
                    }
                } catch (Exception e) {
                    System.err.println("Exception : Class ConSNMPGet : run() : e1 : " + e);
                }
            }
        } catch (Exception e2) {
            System.err.println("Exception : Class ConSNMPGet : run() : e2 : " + e2);
        }
    }

    public void snmpcard(int i, byte[] bArr) {
        for (int i2 = 0; i2 < i; i2++) {
            try {
                buffer2[i2] = bArr[i2];
                if (bArr[i2] < 0) {
                    buffer2[i2] = buffer2[i2] + 256;
                }
                if (buffer2[1] == 130) {
                    if (ConSNMPSet.IBO == 1) {
                        ConSNMPSet.synsoBO = true;
                        ConSNMPSet.megatecBO = false;
                        if (i2 >= 53) {
                            sb.append((char) buffer2[i2]);
                        }
                    } else if (ConSNMPSet.IBO == 2) {
                        if (i2 > 52) {
                            sb.append((char) buffer2[i2]);
                        }
                    } else if (ConSNMPSet.IBO == 3) {
                        if (i2 > 52) {
                            sb.append((char) buffer2[i2]);
                        }
                    } else if (ConSNMPSet.IBO == 4) {
                        if (i2 > 52) {
                            sb.append((char) buffer2[i2]);
                        }
                    } else if (ConSNMPSet.IBO == 5 && i2 > 51) {
                        if (buffer2[52] == 1) {
                            Label.ConfigOutputVoltage = buffer2[53];
                        } else if (buffer2[52] == 2) {
                            Label.ConfigOutputVoltage = buffer2[54];
                        }
                        temp = String.valueOf(Label.ConfigOutputVoltage);
                        fo = new FileOutputStream(Label.fileConfigOutputVolt, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    }
                } else if (ConSNMPSet.IBO == 1) {
                    ConSNMPSet.synsoBO = false;
                    ConSNMPSet.megatecBO = true;
                    if (i2 > 44) {
                        sb.append((char) buffer2[i2]);
                    }
                } else if (ConSNMPSet.IBO == 2) {
                    if (i2 > 44) {
                        sb.append((char) buffer2[i2]);
                    }
                } else if (ConSNMPSet.IBO == 3) {
                    if (i2 > 44) {
                        sb.append((char) buffer2[i2]);
                    }
                } else if (ConSNMPSet.IBO == 4) {
                    if (i2 > 44) {
                        sb.append((char) buffer2[i2]);
                    }
                } else if (ConSNMPSet.IBO == 5 && i2 > 43) {
                    if (buffer2[44] == 2) {
                        Label.ConfigOutputVoltage = (buffer2[45] * 256) + buffer2[46];
                        if (Label.ConfigOutputVoltage > 1000.0d) {
                            Label.ConfigOutputVoltage /= 10.0d;
                        }
                    } else {
                        Label.ConfigOutputVoltage = buffer2[45];
                    }
                    temp = String.valueOf(Label.ConfigOutputVoltage);
                    fo = new FileOutputStream(Label.fileConfigOutputVolt, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
            } catch (Exception e) {
                return;
            }
        }
        if (ConSNMPSet.IBO == 5) {
            ConSNMPSet.IBO = 6;
        }
        if (ConSNMPSet.IBO == 1) {
            temp = sb.toString();
            fo = new FileOutputStream(Label.fileCompany, false);
            fo.write(temp.getBytes(), 0, temp.length());
            fo.close();
            sb.delete(0, temp.length());
        } else if (ConSNMPSet.IBO == 2) {
            temp = sb.toString();
            Label.UpsModel = temp;
            fo = new FileOutputStream(Label.fileModel, false);
            fo.write(temp.getBytes(), 0, temp.length());
            fo.close();
            sb.delete(0, sb.length());
        } else if (ConSNMPSet.IBO == 3) {
            temp = sb.toString();
            Label.UpsFirmware = temp;
            fo = new FileOutputStream(Label.fileFirmware, false);
            fo.write(temp.getBytes(), 0, temp.length());
            fo.close();
            sb.delete(0, temp.length());
        } else if (ConSNMPSet.IBO == 4) {
            temp = sb.toString();
            fo = new FileOutputStream(Label.fileSnmpFirm, false);
            fo.write(temp.getBytes(), 0, temp.length());
            fo.close();
            sb.delete(0, temp.length());
        } else if (ConSNMPSet.battBO) {
            if (ConSNMPSet.synsoBO) {
                ConSNMPSet.battBO = false;
                if (bArr[53] == 4) {
                    if (Label.b3 == 0) {
                        System.out.println("UPSMON : Battery Fail");
                        Record record = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Fail"));
                        temp = "Battery Fail";
                        fo = new FileOutputStream(Label.fileBatResult, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    }
                    Label.b3 = 2;
                } else {
                    if (Label.b3 == 2) {
                        System.out.println("UPSMON : Battery Normal");
                        Record record2 = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Normal"));
                        temp = "Battery Normal";
                        fo = new FileOutputStream(Label.fileBatResult, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    }
                    Label.b3 = 0;
                }
            }
        } else if (ConSNMPSet.powerwatt) {
            if (ConSNMPSet.synsoBO && ConSNMPSet.powerwatBO) {
                if (bArr[55] == 48) {
                    ConSNMPSet.powerwatBO = false;
                } else {
                    ConSNMPSet.powerwatt = false;
                    temp = String.valueOf((int) bArr[55]);
                    fo = new FileOutputStream(Label.fileOutputPower, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                    powerflag = false;
                }
            }
        } else if (ConSNMPSet.envitemp) {
            if (ConSNMPSet.megatecBO && ConSNMPSet.envitempBO) {
                if (bArr[49] == 0) {
                    ConSNMPSet.envitempBO = false;
                } else {
                    ConSNMPSet.envitemp = false;
                    Label.envtemp = ((bArr[49] * 256) + bArr[50]) / 10;
                    temp = String.valueOf(Label.envtemp);
                    fo = new FileOutputStream(Label.fileEnvtemp, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                    powerflag = false;
                }
            }
        } else if (ConSNMPSet.envihumi && ConSNMPSet.megatecBO && ConSNMPSet.envihumiBO) {
            if (bArr[49] == 0) {
                ConSNMPSet.envihumiBO = false;
            } else {
                ConSNMPSet.envihumi = false;
                Label.envhumi = bArr[49];
                temp = String.valueOf(Label.envhumi);
                fo = new FileOutputStream(Label.fileEnvHumi, false);
                fo.write(temp.getBytes(), 0, temp.length());
                fo.close();
                powerflag = false;
            }
        }
        certified_length = 0;
        headlength = 0;
        if (buffer2[1] == 129) {
            certified_length = buffer2[2];
            headlength = 3;
        } else if (buffer2[1] == 130) {
            certified_length = (buffer2[2] * 256) + bArr[3];
            if (certified_length < 0) {
                certified_length += 256;
            }
            headlength = 4;
        }
        if (powerflag || certified_length == i - headlength) {
            headlength += 4;
            int i3 = buffer2[headlength];
            headlength++;
            headlength += i3;
            if (buffer2[headlength] != 160 && buffer2[headlength] != 161 && buffer2[headlength] != 163 && buffer2[headlength] != 164 && buffer2[headlength] == 162) {
                headlength++;
                if (buffer2[headlength] == 129) {
                    headlength += 12;
                } else if (buffer2[headlength] == 130) {
                    headlength += 13;
                }
                if (buffer2[headlength] == 129) {
                    headlength += 3;
                } else if (buffer2[headlength] == 130) {
                    headlength += 4;
                }
                if (buffer2[headlength] == 129) {
                    headlength++;
                } else if (buffer2[headlength] == 130) {
                    headlength += 2;
                }
                headlength += 2;
                headlength += 12;
                headlength += 1 + buffer2[headlength];
                headlength++;
                if (buffer2[headlength] == 129) {
                    headlength += 16;
                } else if (buffer2[headlength] == 130) {
                    headlength += 17;
                } else {
                    headlength += 15;
                }
                Label.Bat_level = buffer2[headlength];
                if (Label.Bat_level == 99) {
                    Label.Bat_level = 100;
                }
                battery = String.valueOf(Label.Bat_level);
                if (b % 5 == 1) {
                    temp = battery + " %";
                    fo = new FileOutputStream(Label.filebatcap, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 16;
                } else if (buffer2[headlength] == 130) {
                    headlength += 17;
                } else {
                    headlength += 15;
                }
                Label.Temperature = buffer2[headlength];
                if (b % 10 == 1) {
                    temp = String.valueOf(Label.Temperature) + " C";
                    fo = new FileOutputStream(Label.filetemp, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 17;
                } else if (buffer2[headlength] == 130) {
                    headlength += 18;
                } else {
                    headlength += 16;
                }
                if (buffer2[headlength] == 2) {
                    Label.InputFreq = ((buffer2[headlength + 1] * 256) + buffer2[headlength + 2]) / 10;
                    headlength += 2;
                } else if (buffer2[headlength] == 1) {
                    if (ConSNMPSet.synsoBO) {
                        Label.InputFreq = buffer2[headlength + 1] / 10;
                    } else {
                        Label.InputFreq = buffer2[headlength + 1];
                    }
                    headlength++;
                }
                if (Label.InputFreq < 5) {
                    Label.InputFreq = 0;
                }
                if (b % 10 == 1) {
                    temp = String.valueOf(Label.InputFreq) + " Hertz";
                    fo = new FileOutputStream(Label.filefreq, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 17;
                } else if (buffer2[headlength] == 130) {
                    headlength += 18;
                } else {
                    headlength += 16;
                }
                if (buffer2[headlength] == 1) {
                    Label.InputVolt = buffer2[headlength + 1];
                    headlength++;
                } else if (buffer2[headlength] == 2) {
                    Label.InputVolt = (buffer2[headlength + 1] * 256) + buffer2[headlength + 2];
                    headlength += 2;
                }
                if (Label.InputVolt > 600) {
                    Label.InputVolt /= 10;
                }
                if (Label.InputVoltMax < Label.InputVolt) {
                    Label.InputVoltMax = Label.InputVolt;
                }
                if (Label.InputVoltMini > Label.InputVolt) {
                    Label.InputVoltMini = Label.InputVolt;
                }
                if (b % 2 == 1) {
                    temp = String.valueOf(Label.InputVolt) + " Voltage";
                    fo = new FileOutputStream(Label.fileInputVolt, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 16;
                } else if (buffer2[headlength] == 130) {
                    headlength += 17;
                } else {
                    headlength += 15;
                }
                int i4 = buffer2[headlength];
                if (i4 == 4) {
                    if (Label.b5 == 0) {
                        Label.b5 = 1;
                        System.out.println("UPSMON : Bypass");
                        Record record3 = UPSMON.RecordAPP;
                        Record.set(new String("Bypass"));
                    }
                } else if (i4 == 6 || i4 == 7) {
                    Label.b5 = 1;
                } else {
                    if (Label.b5 == 1 && normalBO) {
                        System.out.println("UPSMON : Normal");
                        Record record4 = UPSMON.RecordAPP;
                        Record.set(new String("Normal"));
                        normalBO = false;
                    }
                    Label.b5 = 0;
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 15;
                } else if (buffer2[headlength] == 130) {
                    headlength += 16;
                } else {
                    headlength += 14;
                }
                if (buffer2[headlength] == 1) {
                    headlength++;
                } else if (buffer2[headlength] == 2) {
                    headlength += 2;
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 17;
                } else if (buffer2[headlength] == 130) {
                    headlength += 18;
                } else {
                    headlength += 16;
                }
                if (buffer2[headlength] == 1) {
                    Label.OutputVolt = buffer2[headlength + 1];
                    headlength++;
                    if (b == 0) {
                        outputlevel1 = 1;
                    }
                    outputlevel2 = 1;
                } else if (buffer2[headlength] == 2) {
                    Label.OutputVolt = (buffer2[headlength + 1] * 256) + buffer2[headlength + 2];
                    headlength += 2;
                    if (b == 0) {
                        outputlevel1 = 2;
                    }
                    outputlevel2 = 2;
                }
                if (Label.OutputVolt > 600) {
                    Label.OutputVolt /= 10;
                }
                if (b % 3 == 1) {
                    temp = String.valueOf(Label.OutputVolt) + " Voltage";
                    fo = new FileOutputStream(Label.fileOutputVolt, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
                if (Label.b5 == 1 && Label.b3 == 1) {
                    if (Label.InputVolt >= Label.OutputVolt) {
                        if (!normalBO) {
                            System.out.println("UPSMON : AVR Buck");
                            Record record5 = UPSMON.RecordAPP;
                            Record.set(new String("AVR_Buck"));
                            normalBO = true;
                        }
                    } else if (!normalBO) {
                        System.out.println("UPSMON : AVR Boost");
                        Record record6 = UPSMON.RecordAPP;
                        Record.set(new String("AVR_Boost"));
                        normalBO = true;
                    }
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 17;
                } else if (buffer2[headlength] == 130) {
                    headlength += 18;
                } else {
                    headlength += 16;
                }
                if (buffer2[headlength] == 1) {
                    Label.Load = buffer2[headlength + 1];
                    headlength++;
                } else if (buffer2[headlength] == 2) {
                    Label.Load = (buffer2[headlength + 1] * 256) + buffer2[headlength + 2];
                    headlength += 2;
                }
                if (b % 5 == 1) {
                    temp = String.valueOf(Label.Load) + " %";
                    fo = new FileOutputStream(Label.fileload, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 16;
                } else if (buffer2[headlength] == 130) {
                    headlength += 17;
                } else {
                    headlength += 15;
                }
                int i5 = buffer2[headlength];
                if (i5 == 0) {
                    if (Label.b7 == 1) {
                        Label.b7 = 0;
                        System.out.println("UPSMON : Power Restore");
                        Record record7 = UPSMON.RecordAPP;
                        Record.set(new String("Power_Restore"));
                        CountDOWN.roopBO = false;
                        battery_power_stop = true;
                        if (CountDOWN.outletoffBO == 1) {
                            CountDOWN.outletoffBO = 0;
                            ConSNMPSet.a = 8;
                        }
                    } else if (Label.b6 == 1) {
                        Label.b6 = 0;
                        Connect.lowbat = 0;
                        System.out.println("UPSMON : Normal");
                        Record record8 = UPSMON.RecordAPP;
                        Record.set(new String("Normal"));
                    } else if (Label.b2 == 1) {
                        Label.b2 = 0;
                        if (ConSNMPSet.deeptestBO) {
                            cal3 = Calendar.getInstance();
                            int timeInMillis = (int) ((cal3.getTimeInMillis() - cal2.getTimeInMillis()) / 1000);
                            if (timeInMillis < 60) {
                                System.out.println("UPSMON : Battery Test Time (" + timeInMillis + " secs)");
                                Record record9 = UPSMON.RecordAPP;
                                Record.set(new String("Battery_Test_Time_(" + timeInMillis + "_secs)"));
                            } else {
                                int i6 = timeInMillis / 60;
                                int i7 = timeInMillis % 60;
                                System.out.println("UPSMON : Battery Test Time (" + i6 + " mins " + i7 + " secs)");
                                Record record10 = UPSMON.RecordAPP;
                                Record.set(new String("Battery_Test_Time_(" + i6 + "_mins_" + i7 + "_secs)"));
                            }
                        }
                        System.out.println("UPSMON : Battery Normal");
                        Record record11 = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Normal"));
                        Label.Battestresult = "OK";
                        temp = "Battery Normal";
                        fo = new FileOutputStream(Label.fileBatResult, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    }
                } else if (i5 == 1) {
                    if (Label.b7 == 1) {
                        Label.b7 = 0;
                        System.out.println("UPSMON : Power Restore");
                        Record record12 = UPSMON.RecordAPP;
                        Record.set(new String("Power_Restore"));
                        CountDOWN.roopBO = false;
                        battery_power_stop = true;
                    } else if (Label.b5 != 1) {
                        if (Label.b2 == 0 && Label.Bat_level >= 30) {
                            if (ConSNMPSet.deeptestBO) {
                                Label.b2 = 1;
                                System.out.println("UPSMON : Battery Deep Test");
                                Record record13 = UPSMON.RecordAPP;
                                Record.set(new String("Battery_Deep_Test"));
                                testflag = true;
                                Label.Bat_test_times++;
                                temp = String.valueOf(Label.Bat_test_times);
                                fo = new FileOutputStream(Label.fileBatTestTimes, false);
                                fo.write(temp.getBytes(), 0, temp.length());
                                fo.close();
                                cal2 = Calendar.getInstance();
                            } else {
                                Label.b2 = 1;
                                System.out.println("UPSMON : Battery Test");
                                Record record14 = UPSMON.RecordAPP;
                                Record.set(new String("Battery_Test"));
                                testflag = true;
                                Label.Bat_test_times++;
                                temp = String.valueOf(Label.Bat_test_times);
                                fo = new FileOutputStream(Label.fileBatTestTimes, false);
                                fo.write(temp.getBytes(), 0, temp.length());
                                fo.close();
                            }
                        } else if (Label.Bat_level < 30 && Label.b6 == 0) {
                            Label.b6 = 1;
                            System.out.println("UPSMON : Low Battery");
                            Record record15 = UPSMON.RecordAPP;
                            Record.set(new String("Low_Battery"));
                            Connect.lowbat = 1;
                        }
                    }
                } else if (i5 == 2) {
                    if (Label.InputVolt <= 10 && Label.b7 == 0) {
                        Label.b7 = 1;
                        battery_power = true;
                        System.out.println("UPSMON : Power Failure");
                        Record record16 = UPSMON.RecordAPP;
                        Record.set(new String("Power_Failure"));
                        int i8 = Integer.parseInt(UPSMON.shutdowntype);
                        Label.Bat_times++;
                        temp = String.valueOf(Label.Bat_times);
                        fo = new FileOutputStream(Label.fileBatTimes, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                        if (i8 != 2) {
                            CountDOWN.usrcmd = true;
                            new CountDOWN().start();
                            System.out.println("UPSMON : Linux count down " + Label.osdelay + " seconds");
                        }
                    }
                } else if (i5 == 3) {
                    if (Label.InputVolt <= 10 && Label.b7 == 0) {
                        Label.b7 = 1;
                        battery_power = true;
                        System.out.println("UPSMON : Power Failure");
                        Record record17 = UPSMON.RecordAPP;
                        Record.set(new String("Power_Failure"));
                        int i9 = Integer.parseInt(UPSMON.shutdowntype);
                        Label.Bat_times++;
                        temp = String.valueOf(Label.Bat_times);
                        fo = new FileOutputStream(Label.fileBatTimes, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                        if (i9 != 2) {
                            CountDOWN.usrcmd = true;
                            new CountDOWN().start();
                            System.out.println("UPSMON : count down " + Label.osdelay + " seconds");
                        }
                    }
                    if (Label.b6 == 0) {
                        Label.b6 = 1;
                        System.out.println("UPSMON : Low Battery");
                        Record record18 = UPSMON.RecordAPP;
                        Record.set(new String("Low_Battery"));
                        Connect.lowbat = 1;
                    }
                } else if (i5 == 4) {
                    Label.b1 = 1;
                }
                headlength += 2;
                if (buffer2[headlength] == 129) {
                    headlength += 15;
                } else if (buffer2[headlength] == 130) {
                    headlength += 16;
                } else {
                    headlength += 14;
                }
                if (buffer2[headlength] == 1) {
                    Label.ConfigInputVoltage = buffer2[headlength + 1];
                    headlength++;
                } else if (buffer2[headlength] == 2) {
                    Label.ConfigInputVoltage = (buffer2[headlength + 1] * 256) + buffer2[headlength + 2];
                    if (Label.ConfigInputVoltage > 1000) {
                        Label.ConfigInputVoltage /= 10;
                    }
                    headlength += 2;
                }
                temp = String.valueOf(Label.ConfigInputVoltage);
                if (b <= 10) {
                    fo = new FileOutputStream(Label.fileConfigInputVolt, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
                headlength += 2;
                if (buffer2[headlength] == 130) {
                    headlength += 16;
                    if (buffer2[headlength] == 1) {
                        Label.Bat_backup_time = buffer2[headlength + 1];
                        headlength++;
                    } else if (buffer2[headlength] == 2) {
                        Label.Bat_backup_time = (buffer2[headlength + 1] * 256) + buffer2[headlength + 2];
                        headlength += 2;
                    }
                } else if (buffer2[headlength] == 15) {
                    headlength += 15;
                    if (b == 0 && buffer2[headlength] != 0) {
                        batt_judge = 1;
                    }
                    if (batt_judge == 1) {
                        Label.Bat_backup_time = buffer2[headlength];
                    }
                } else if (buffer2[headlength] == 16) {
                    headlength += 15;
                    if (b == 0 && (buffer2[headlength] * 256) + buffer2[headlength + 1] != 0) {
                        batt_judge = 1;
                    }
                    if (batt_judge == 1) {
                        Label.Bat_backup_time = (buffer2[headlength] * 256) + buffer2[headlength + 1];
                    }
                }
                try {
                    if (batt_judge == 1) {
                        temp = String.valueOf(Label.Bat_backup_time);
                        fo = new FileOutputStream(Label.fileRt, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    }
                } catch (Exception e2) {
                    System.err.println("Exception : Class ReceivePacketAPP : snmpcard() : ea :" + e2);
                }
                headlength += 2;
                if (buffer2[headlength] == 130) {
                    headlength += 17;
                    if (b % 5 == 1) {
                        Label.Bat_volt = ((buffer2[headlength] * 256) + buffer2[headlength + 1]) / 10;
                        temp = String.valueOf(Label.Bat_volt);
                        fo = new FileOutputStream(Label.fileBatVolt, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    }
                } else if (buffer2[headlength] == 15) {
                    headlength += 15;
                    if (b % 5 == 1) {
                        Label.Bat_volt = buffer2[headlength];
                        temp = String.valueOf(Label.Bat_volt);
                        fo = new FileOutputStream(Label.fileBatVolt, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    }
                } else if (buffer2[headlength] == 16) {
                    headlength += 15;
                    if (b % 5 == 1) {
                        Label.Bat_volt = ((buffer2[headlength] * 256) + buffer2[headlength + 1]) / 10;
                        temp = String.valueOf(Label.Bat_volt);
                        fo = new FileOutputStream(Label.fileBatVolt, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        fo.close();
                    }
                }
                if (Label.OutputVolt <= 1) {
                    if (Label.b1 == 0) {
                        Label.b1 = 1;
                        System.out.println("UPSMON : UPS OFF");
                        Record record19 = UPSMON.RecordAPP;
                        Record.set(new String("UPS_OFF"));
                    }
                } else if (Label.b1 == 1) {
                    Label.b1 = 0;
                    System.out.println("UPSMON : UPS ON");
                    Record record20 = UPSMON.RecordAPP;
                    Record.set(new String("UPS_ON"));
                }
                if (b % 5 == 1) {
                    if (Label.b7 == 1) {
                        temp = "Battery Power";
                        Label.PowerStatus = temp;
                    } else if (Label.b5 == 1) {
                        temp = "Bypass";
                        Label.PowerStatus = temp;
                    } else if (i4 == 6) {
                        temp = "AVR Boost";
                        Label.PowerStatus = temp;
                    } else if (i4 == 7) {
                        temp = "AVR Buck";
                        Label.PowerStatus = temp;
                    } else {
                        temp = "AC Utility Power";
                        Label.PowerStatus = "On-Line";
                    }
                    fo = new FileOutputStream(Label.fileUpsStatus, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                    if (Label.b6 == 1) {
                        temp = "Battery Low";
                    } else if (Label.b7 == 1) {
                        temp = "Discharge";
                    } else if (Label.b2 == 1) {
                        temp = "Battery Test";
                    } else if (Label.b3 == 2) {
                        temp = "Battery Fail";
                    } else if (Label.Bat_level < 98) {
                        temp = "Charge";
                    } else {
                        temp = "Normal";
                    }
                    fo = new FileOutputStream(Label.fileBatStatus, false);
                    fo.write(temp.getBytes(), 0, temp.length());
                    fo.close();
                }
                try {
                    MMM = String.valueOf(Label.InputVolt) + ".0";
                    PPP = String.valueOf(Label.OutputVolt) + ".0";
                    RR = String.valueOf(Label.InputFreq) + ".0";
                    TT = String.valueOf(Label.Temperature) + ".0";
                    QQQ = String.valueOf(Label.Load);
                    if (Label.InputVolt < 10) {
                        MMM = "00" + MMM;
                    } else if (Label.InputVolt < 100) {
                        MMM = "0" + MMM;
                    }
                    if (Label.OutputVolt < 10) {
                        PPP = "00" + PPP;
                    } else if (Label.OutputVolt < 100) {
                        PPP = "0" + PPP;
                    }
                    if (Label.Load < 10) {
                        QQQ = "00" + QQQ;
                    } else if (Label.Load < 100) {
                        QQQ = "0" + QQQ;
                    }
                    if (Label.InputFreq < 10) {
                        RR = "0" + RR;
                    }
                    if (Label.Bat_level < 100) {
                        battery = "0" + battery;
                    } else if (Label.Bat_level < 10) {
                        battery = "00" + battery;
                    }
                    temp = "(" + MMM + " " + NNN + " " + PPP + " " + QQQ + " " + RR + " " + battery + " " + TT + " " + Label.b7 + Label.b6 + Label.b5 + Label.b4 + Label.b3 + Label.b2 + Label.b1 + Label.b1 + "\n";
                    if (b % 10 != 0 && b % 12 != 0 && b % 15 != 0 && ((b < 5 || b > 10) && temp.length() >= 45)) {
                        try {
                            byte[] bytes = temp.getBytes();
                            int length = bytes.length;
                            if (length != 0) {
                                fo = new FileOutputStream(Label.fileget, false);
                                fo.write(bytes, 0, length);
                                fo.close();
                            }
                        } catch (Exception e3) {
                            System.err.println("Exception : Class ReceivePacketAPP : snmpcard() : e2 :" + e3);
                        }
                        try {
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
                            if (b <= 3) {
                                fileLog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + String.valueOf(cal1.get(1)) + "_" + String.valueOf(cal1.get(2) + 1) + "_" + String.valueOf(cal1.get(5)) + ".csv";
                            } else if (cal1.get(11) == 0 && cal1.get(12) <= 3) {
                                fileLog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + String.valueOf(cal1.get(1)) + "_" + String.valueOf(cal1.get(2) + 1) + "_" + String.valueOf(cal1.get(5)) + ".csv";
                            }
                            if (b % 10 != 0 && b % 6 != 0 && b % 12 != 0 && b % 15 != 0 && ((b < 5 || b > 10) && outputlevel1 == outputlevel2)) {
                                temp = data1 + "," + Label.InputVolt + "," + Label.OutputVolt + "," + Label.Temperature + "," + Label.Load + "," + battery + "\n";
                                fo = new FileOutputStream(fileLog, true);
                                fo.write(temp.getBytes(), 0, temp.length());
                                fo.close();
                            }
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
                                fo.close();
                                Label.Batpowerstart = data1;
                            }
                            if (battery_power_stop) {
                                battery_power_stop = false;
                                fo = new FileOutputStream(Label.fileBatStop, false);
                                fo.write(data1.getBytes(), 0, data1.length());
                                fo.close();
                                Label.Batpowerend = data1;
                            }
                            if (testflag) {
                                testflag = false;
                                fo = new FileOutputStream(Label.fileBatime, false);
                                fo.write(data1.getBytes(), 0, data1.length());
                                fo.close();
                            }
                        } catch (Exception e4) {
                            System.err.println("Exception : Class ReceivePacketAPP : snmpcard() : e4 :" + e4);
                        }
                    }
                } catch (Exception e5) {
                    System.err.println("Exception : Class ReceivePacketAPP : snmpcard () : e3 :" + e5);
                }
            }
        }
    }
}
