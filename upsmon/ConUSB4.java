/* Decompiler 1584ms, total 2032ms, lines 702 */
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

class ConUSB4 extends Thread {
   public static UsbEndpoint endpoint = null;
   public static UsbDevice upsUsbDevice = null;
   public static int Input_Voltage;
   public static int Output_Voltage;
   public static int Load;
   public static int Input_Frequency;
   public static int Battery_Capacity;
   public static int Temperature;
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
   public static int a;
   public static int b = 0;
   public static int timeINT = 0;
   public static int delay = 2000;
   public static boolean soundBO = false;
   public static boolean green_modeBO = false;
   public static boolean battery_power = false;
   public static boolean battery_power_stop = false;
   public static boolean testflag = false;
   public static File f0;
   public static File f1;
   public static File f4;
   public static File ff;
   public static String c1;
   public static String temp;
   public static String temp2;
   public static String datalog;
   public static String filelog;
   public static BufferedReader br;
   public static FileOutputStream fo;
   public static Calendar cal1;
   public static Calendar cal2;
   public static Calendar cal3;
   public static boolean freq_BO;
   static byte[] data;

   public ConUSB4(UsbEndpoint var1, UsbDevice var2, ConUSB var3) {
      Connect.model = 4;
      endpoint = var1;
      upsUsbDevice = var2;
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
      } catch (Exception var5) {
         System.err.println("Exception : Class ConUSB4 : e : " + var5);
      }

   }

   public void run() {
      try {
         UsbPipe var1 = endpoint.getUsbPipe();
         byte var2 = 33;
         byte var3 = 9;
         short var4 = 783;
         short var5 = 784;
         short var6 = 789;
         boolean var7 = true;
         short var8 = 928;
         short var9 = 932;
         short var10 = 941;
         byte[] var11 = new byte[]{-83, 1, 1, 0, 0, 0, 0, 0};
         UsbControlIrp var12 = var1.createUsbControlIrp(var2, var3, var10, (short)0);
         var1.createUsbControlIrp(var2, var3, var8, (short)0);
         var1.createUsbControlIrp(var2, var3, var9, (short)0);
         UsbControlIrp var15 = var1.createUsbControlIrp(var2, var3, var4, (short)0);
         UsbControlIrp var16 = var1.createUsbControlIrp(var2, var3, var5, (short)0);
         UsbControlIrp var17 = var1.createUsbControlIrp(var2, var3, var6, (short)0);
         byte[] var10000 = new byte[]{-96, 2, 5, 0, 0, 0, 0, 0};
         var10000 = new byte[]{-92, 0, 0, 0, 0, 0, 0, 0};
         var10000 = new byte[]{-96, 2, 71, 0, 0, 0, 0, 0};
         byte[] var21 = new byte[3];
         byte[] var22 = new byte[3];
         byte[] var23 = new byte[3];
         byte[] var24 = new byte[3];
         byte[] var25 = new byte[3];
         byte[] var26 = new byte[3];
         byte[] var27 = new byte[3];
         byte[] var28 = new byte[3];
         byte var29 = -95;
         byte var30 = 1;
         short var31 = 782;
         short var32 = 797;
         short var33 = 798;
         short var34 = 799;
         short var35 = 801;
         short var36 = 802;
         short var37 = 806;
         short var38 = 823;
         UsbControlIrp var39 = var1.createUsbControlIrp(var29, var30, var31, (short)0);
         UsbControlIrp var40 = var1.createUsbControlIrp(var29, var30, var32, (short)0);
         UsbControlIrp var41 = var1.createUsbControlIrp(var29, var30, var33, (short)0);
         UsbControlIrp var42 = var1.createUsbControlIrp(var29, var30, var34, (short)0);
         UsbControlIrp var43 = var1.createUsbControlIrp(var29, var30, var35, (short)0);
         UsbControlIrp var44 = var1.createUsbControlIrp(var29, var30, var36, (short)0);
         UsbControlIrp var45 = var1.createUsbControlIrp(var29, var30, var37, (short)0);
         UsbControlIrp var46 = var1.createUsbControlIrp(var29, var30, var38, (short)0);
         var39.setData(var21);
         var40.setData(var22);
         var41.setData(var23);
         var42.setData(var24);
         var43.setData(var25);
         var44.setData(var26);
         var45.setData(var27);
         var46.setData(var28);
         var1.open();

         for(String var47 = ""; a >= 0; sleep((long)delay)) {
            read();
            byte[] var48;
            Record var56;
            if (a == 1) {
               a = 0;
               b = 40;
               var48 = new byte[]{21, 1};
               var17.setData(var48);
               upsUsbDevice.syncSubmit(var17);
               var17.setComplete(false);
               sleep(1000L);
            } else if (a == 4) {
               b = 30;
               var48 = new byte[]{15, (byte)(Label.upsdelay + 8), 0};
               var15.setData(var48);
               upsUsbDevice.syncSubmit(var15);
               var15.setComplete(false);
               a = -1;
               sleep(1000L);
            } else if (a == 5) {
               a = 0;
               var48 = new byte[]{16, 1, 0};
               var16.setData(var48);
               upsUsbDevice.syncSubmit(var16);
               var16.setComplete(false);
               sleep(3000L);
               var56 = UPSMON.RecordAPP;
               Record.set(new String("UPS_turn_off_after_" + controloff + "_minutes"));
               System.out.println("UPSMON : UPS will turn off power after " + controloff + " minutes");
               byte[] var49 = new byte[]{15, 0, (byte)controloff};
               var15.setData(var49);
               upsUsbDevice.syncSubmit(var15);
               var15.setComplete(false);
               sleep(3000L);

               try {
                  String var50 = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                  System.out.println("UPSMON : Linux Shutdown !!");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("Linux_Shutdown"));
                  var56 = UPSMON.RecordAPP;
                  Record.set2(new String("UPS_Shutdown"), String.valueOf(controloff));
                  sleep(1000L);
                  Runtime.getRuntime().exec(var50);
               } catch (Exception var52) {
                  System.err.println("Exception : Class ConUSB1 : run() : e : " + var52);
               }
            }

            if (b == 0) {
               var12.setData(var11);
               upsUsbDevice.syncSubmit(var12);
               var12.setComplete(false);
               sleep(12000L);
            }

            upsUsbDevice.syncSubmit(var39);
            upsUsbDevice.syncSubmit(var40);
            if (freq_BO) {
               upsUsbDevice.syncSubmit(var41);
            }

            upsUsbDevice.syncSubmit(var42);
            upsUsbDevice.syncSubmit(var43);
            upsUsbDevice.syncSubmit(var44);
            upsUsbDevice.syncSubmit(var45);
            upsUsbDevice.syncSubmit(var46);
            Input_Voltage = var22[1];
            if (Input_Voltage < 0) {
               Input_Voltage += 256;
            }

            if (Input_Voltage >= 100) {
               var47 = "(" + String.valueOf(Input_Voltage) + ".0 ";
            } else if (Input_Voltage > 9) {
               var47 = "(0" + String.valueOf(Input_Voltage) + ".0 ";
            } else {
               var47 = "(00" + String.valueOf(Input_Voltage) + ".0 ";
            }

            data[5] = (byte)(Input_Voltage / 256);
            data[6] = (byte)(Input_Voltage % 256);
            Label.InputVolt = Input_Voltage;
            if (Label.InputVoltMax < Label.InputVolt) {
               Label.InputVoltMax = Label.InputVolt;
            }

            if (Label.InputVoltMini > Label.InputVolt) {
               Label.InputVoltMini = Label.InputVolt;
            }

            var47 = var47 + "000.0 ";
            Output_Voltage = var25[1];
            if (Output_Voltage < 0) {
               Output_Voltage += 256;
            }

            if (var27[2] == 68) {
               var47 = var47 + "000.0 ";
            } else if (Output_Voltage >= 100) {
               var47 = var47 + String.valueOf(Output_Voltage) + ".0 ";
            } else if (Output_Voltage > 9) {
               var47 = var47 + "0" + Output_Voltage + ".0 ";
            } else {
               var47 = var47 + "00" + Output_Voltage + ".0 ";
            }

            data[7] = (byte)(Output_Voltage / 256);
            data[8] = (byte)(Output_Voltage % 256);
            Label.OutputVolt = Output_Voltage;
            Load = var24[1];
            if (Load < 0) {
               Load += 256;
            }

            if (Load >= 100) {
               Load = 100;
               var47 = var47 + String.valueOf(Load) + " ";
            } else if (Load > 9) {
               var47 = var47 + "0" + Load + " ";
            } else {
               var47 = var47 + "00" + Load + " ";
            }

            data[11] = (byte)Load;
            Label.Load = Load;
            Label.InputFreq = var23[1];
            if (Label.InputFreq >= 40) {
               freq_BO = false;
            }

            if (Label.InputFreq < 0) {
               Label.InputFreq += 256;
            }

            if (Label.InputFreq >= 10) {
               var47 = var47 + Label.InputFreq + ".0 ";
            } else {
               var47 = var47 + "0" + Input_Frequency + ".0 ";
            }

            data[13] = (byte)Label.InputFreq;
            Battery_Capacity = var21[1];
            if (Battery_Capacity < 0) {
               Battery_Capacity += 256;
            }

            if (Battery_Capacity >= 100) {
               var47 = var47 + "0100 ";
            } else if (Battery_Capacity >= 10) {
               var47 = var47 + "00" + Battery_Capacity + " ";
            } else {
               var47 = var47 + "000" + Battery_Capacity + " ";
            }

            data[12] = (byte)Battery_Capacity;
            Label.Bat_level = Battery_Capacity;
            Temperature = var26[1];
            if (Temperature < 0) {
               Temperature += 256;
            }

            data[14] = (byte)Temperature;
            if (var27[2] == 68) {
               var47 = var47 + "00.0 ";
            } else if (Temperature >= 10) {
               var47 = var47 + String.valueOf(Temperature) + ".0 ";
            } else {
               var47 = var47 + "0" + Temperature + ".0 ";
            }

            Label.Temperature = Temperature;
            if (var27[1] != 10 && Input_Voltage > 10) {
               if (bit7 == 1) {
                  bit7 = 0;
                  CountDOWN.roopBO = false;
                  battery_power_stop = true;
                  temp = "AC Utility Power";
                  Label.PowerStatus = temp;
                  System.out.println("UPSMON : Power Restore");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("Power_Restore"));
               }

               data[18] = 0;
            } else {
               Input_Voltage = 0;
               if (bit7 == 0) {
                  bit7 = 1;
                  b = 30;
                  Connect.readfile();
                  battery_power = true;
                  System.out.println("UPSMON : Power Failure");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("Power_Failure"));
                  temp = "Battery Power";
                  Label.PowerStatus = temp;
                  soundBO = true;
                  green_modeBO = true;
                  ++Label.Bat_times;
                  temp = String.valueOf(Label.Bat_times);
                  fo = new FileOutputStream(Label.fileBatTimes, false);
                  fo.write(temp.getBytes(), 0, temp.length());
                  fo.close();
                  int var54 = Integer.parseInt(UPSMON.shutdowntype);
                  if (var54 != 2) {
                     CountDOWN.usrcmd = true;
                     CountDOWN var55 = new CountDOWN();
                     var55.start();
                     System.out.println("UPSMON : Linux count down " + Label.osdelay + " seconds");
                  }
               }

               data[18] = 1;
            }

            var47 = var47 + String.valueOf(bit7);
            if (Battery_Capacity <= 30) {
               if (bit6 == 0) {
                  bit6 = 1;
                  System.out.println("UPSMON : Low Battery");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("Low_Battery"));
                  Connect.lowbat = 1;
                  data[19] = 1;
               }
            } else if (bit6 == 1) {
               bit6 = 0;
               System.out.println("UPSMON : Normal");
               var56 = UPSMON.RecordAPP;
               Record.set(new String("Normal"));
               Connect.lowbat = 0;
               data[19] = 0;
            }

            var47 = var47 + String.valueOf(bit6);
            if (var27[2] == 16) {
               if (bit5 == 0) {
                  bit5 = 1;
                  temp = "AVR Boost";
                  Label.PowerStatus = temp;
                  System.out.println("UPSMON : Boost");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("Boost"));
                  data[17] = 2;
               }
            } else if (var27[2] == 32) {
               if (bit5 == 0) {
                  bit5 = 1;
                  temp = "AVR Buck";
                  Label.PowerStatus = temp;
                  System.out.println("UPSMON : Buck");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("Buck"));
                  data[17] = 3;
               }
            } else if (bit5 == 1) {
               bit5 = 0;
               System.out.println("UPSMON : Normal");
               var56 = UPSMON.RecordAPP;
               Record.set(new String("Normal"));
               data[17] = 0;
            }

            var47 = var47 + String.valueOf(bit5);
            if (var27[1] == -115) {
               if (bit4 == 0) {
                  bit4 = 1;
                  System.out.println("UPSMON : UPS Failed");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_Failed"));
                  data[21] = 1;
               }
            } else if (bit4 == 1) {
               bit4 = 0;
               System.out.println("UPSMON : UPS Normal");
               var56 = UPSMON.RecordAPP;
               Record.set(new String("UPS_Normal"));
               data[21] = 0;
            }

            var47 = var47 + String.valueOf(bit4);
            if (var27[1] == 77) {
               if (bit3 == 1) {
                  bit3 = 2;
                  System.out.println("UPSMON : Battery Failed");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("Battery_Failed"));
                  data[22] = 1;
               }
            } else {
               if (bit3 == 2) {
                  System.out.println("UPSMON : UPS Normal");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_Normal"));
                  data[22] = 0;
               }

               bit3 = 1;
            }

            var47 = var47 + String.valueOf(bit3);
            if (var27[1] == 13 && var27[2] == 64 || var27[1] == 10 && var27[2] == 64) {
               if (bit2 == 0) {
                  bit2 = 1;
                  soundBO = true;
                  green_modeBO = true;
                  testflag = true;
                  System.out.println("UPSMON : Battery Test");
                  var56 = UPSMON.RecordAPP;
                  Record.set(new String("Battery_Test"));
                  data[23] = 1;
               }
            } else if (bit2 == 1) {
               bit2 = 0;
               System.out.println("UPSMON : Battery Normal");
               var56 = UPSMON.RecordAPP;
               Record.set(new String("Battery_Normal"));
               data[23] = 0;
               Label.Battestresult = "OK";
               temp2 = "Battery Normal";
               fo = new FileOutputStream(Label.fileBatResult, false);
               fo.write(temp2.getBytes(), 0, temp2.length());
               fo.close();
            }

            var47 = var47 + String.valueOf(bit2);
            if (var28[1] < 0) {
               Remaining_Time = (var28[2] * 256 + var28[1] + 256) / 60;
            } else {
               Remaining_Time = (var28[2] * 256 + var28[1]) / 60;
            }

            Label.Bat_backup_time = Remaining_Time;
            data[9] = var28[2];
            data[10] = var28[1];
            var47 = var47 + String.valueOf(bit1) + bit0 + "\n";
            if (var47.length() >= 44) {
               try {
                  var48 = var47.getBytes();
                  if (var48.length != 0) {
                     fo = new FileOutputStream(Label.fileget, false);
                     fo.write(var48, 0, var48.length);
                     fo.close();
                     var47 = String.valueOf(Label.Bat_backup_time);
                     fo = new FileOutputStream(Label.fileRt, false);
                     fo.write(var47.getBytes(), 0, var47.length());
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
                        temp = temp + "0" + timeINT + ":";
                     } else {
                        temp = temp + timeINT + ":";
                     }

                     timeINT = cal3.get(13);
                     if (timeINT <= 9) {
                        temp = temp + "0" + timeINT;
                     } else {
                        temp = temp + timeINT;
                     }

                     var47 = temp + "," + Label.InputVolt + "," + Label.OutputVolt + "," + Label.Temperature + "," + Label.Load + "," + Label.Bat_level + "\n";
                     if (a <= 15) {
                        filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + cal3.get(1) + "_" + (cal3.get(2) + 1) + "_" + cal3.get(5) + ".csv";
                     } else if (cal3.get(11) == 0 && cal3.get(12) <= 3) {
                        filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + cal3.get(1) + "_" + (cal3.get(2) + 1) + "_" + cal3.get(5) + ".csv";
                     }

                     fo = new FileOutputStream(filelog, true);
                     fo.write(var47.getBytes(), 0, var47.length());
                     fo.close();
                     timeINT = cal3.get(2) + 1;
                     if (timeINT <= 9) {
                        temp = temp + " " + cal3.get(1) + "/0" + timeINT;
                     } else {
                        temp = temp + " " + cal3.get(1) + "/" + timeINT;
                     }

                     timeINT = cal3.get(5);
                     if (timeINT <= 9) {
                        temp = temp + "/0" + timeINT;
                     } else {
                        temp = temp + "/" + timeINT;
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
               } catch (Exception var51) {
                  System.err.println("Exception : Class ConUSB4 : run : e1 : " + var51);
               }
            }

            var39.setComplete(false);
            var40.setComplete(false);
            if (freq_BO) {
               var41.setComplete(false);
            }

            var42.setComplete(false);
            var43.setComplete(false);
            var44.setComplete(false);
            var45.setComplete(false);
            var46.setComplete(false);
            ++b;
            if (b == 10000) {
               b = 90;
            }
         }
      } catch (Exception var53) {
         System.err.println();
      }

   }

   public static void read() {
      try {
         if (f0.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f0)));

            while(true) {
               if ((temp2 = br.readLine()) == null) {
                  br.close();
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
         }

         if (f1.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f1)));

            while((temp2 = br.readLine()) != null) {
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

            while((temp = br.readLine()) != null) {
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
      } catch (Exception var1) {
         System.err.println("Exception : Class ConUSB4 : read() : e : " + var1);
      }

   }

   static {
      f0 = new File(Label.fileConfigBatTest);
      f1 = new File(Label.fileConfigUpsDown);
      f4 = new File(Label.fileConfigUpsOff);
      ff = null;
      c1 = "";
      temp2 = null;
      datalog = "";
      filelog = "";
      br = null;
      fo = null;
      cal3 = null;
      freq_BO = true;
      data = new byte[]{80, 67, 77, 82, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 69, 78, 68};
   }
}
