/* Decompiler 2641ms, total 3157ms, lines 969 */
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

class ConUSB3 extends Thread {
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
   public static int bit3 = 0;
   public static int bit2 = 0;
   public static int bit1 = 0;
   public static int bit0 = 1;
   public static int controloff = 0;
   public static int delay = 2000;
   public static int a = 14;
   public static int timeINT = 0;
   public static int num = 0;
   public static boolean soundBO = false;
   public static boolean green_modeBO = false;
   public static boolean deeptestBO = false;
   public static boolean testflag = false;
   public static boolean battery_power = false;
   public static boolean battery_power_stop = false;
   public static boolean init_rebootBO = true;
   public static File ff = null;
   public static File f0;
   public static File f1;
   public static File f2;
   public static File f3;
   public static File f4;
   public static File f5;
   public static File f_exit;
   public static String filelog;
   public static String Q1;
   public static String c1;
   public static String temp;
   public static String temp2;
   public static BufferedReader br;
   public static FileOutputStream fo;
   public static Calendar cal1;
   public static Calendar cal2;
   public static Calendar cal3;
   public static UsbPipe pipe;
   ConUSB conusbAPP = null;
   static int aa;
   static int bb;
   static byte[] data;

   public ConUSB3(UsbEndpoint var1, UsbDevice var2, ConUSB var3) {
      Connect.model = 3;
      endpoint = var1;
      upsUsbDevice = var2;
      this.conusbAPP = var3;
      data[16] = 0;

      try {
         if (!f0.exists()) {
            temp2 = " ";
            fo = new FileOutputStream(f0, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }

         ff = new File(Label.fileBatTestTimes);
         if (ff.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
            Label.Bat_test_times = Integer.parseInt(br.readLine());
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

         ff = new File(Label.fileBatStop);
         if (ff.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
            Label.Batpowerend = br.readLine();
            br.close();
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

         if (!f5.exists()) {
            temp2 = "0";
            fo = new FileOutputStream(f5, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }

         ff = new File(Label.fileConfigMailTest);
         if (!ff.exists()) {
            temp = "0";
            fo = new FileOutputStream(ff, false);
            fo.write(temp.getBytes(), 0, temp.length());
            fo.close();
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

         temp2 = "POWERCOM";
         fo = new FileOutputStream(Label.fileCompany, false);
         fo.write(temp2.getBytes(), 0, temp2.length());
         fo.close();
         temp2 = "0004";
         fo = new FileOutputStream(Label.fileUsbId, false);
         fo.write(temp2.getBytes(), 0, temp2.length());
         fo.close();
      } catch (Exception var5) {
         System.err.println("Exception : Class ConUSB3 : ConUSB3() : e0 : " + var5);
      }

   }

   public void run() {
      try {
         pipe = endpoint.getUsbPipe();
         byte var1 = 33;
         byte var2 = 9;
         short var3 = 941;
         byte[] var4 = new byte[]{-83, 0, 1, 0, 0, 0, 0, 0};
         UsbControlIrp var5 = pipe.createUsbControlIrp(var1, var2, var3, (short)0);
         short var6 = 928;
         byte[] var7 = new byte[]{-96, 5, 68, 81, 49, 13, 0, 0};
         byte[] var10000 = new byte[]{-96, 3, 81, 13, 0, 0, 0, 0};
         var10000 = new byte[]{-96, 5, 83, 48, 49, 13, 0, 0};
         UsbControlIrp var10 = pipe.createUsbControlIrp(var1, var2, var6, (short)0);
         short var11 = 932;
         byte[] var12 = new byte[]{-92, 0, 0, 0, 0, 0, 0, 0};
         UsbControlIrp var13 = pipe.createUsbControlIrp(var1, var2, var11, (short)0);
         short var14 = 789;
         UsbControlIrp var15 = pipe.createUsbControlIrp(var1, var2, var14, (short)0);
         short var16 = 787;
         UsbControlIrp var17 = pipe.createUsbControlIrp(var1, var2, var16, (short)0);
         short var18 = 817;
         short var19 = 818;
         UsbControlIrp var20 = pipe.createUsbControlIrp(var1, var2, var18, (short)0);
         UsbControlIrp var21 = pipe.createUsbControlIrp(var1, var2, var19, (short)0);
         short var22 = 783;
         UsbControlIrp var23 = pipe.createUsbControlIrp(var1, var2, var22, (short)0);
         short var24 = 784;
         UsbControlIrp var25 = pipe.createUsbControlIrp(var1, var2, var24, (short)0);
         byte[] var26 = new byte[3];
         byte[] var27 = new byte[3];
         byte[] var28 = new byte[3];
         byte[] var29 = new byte[3];
         byte[] var30 = new byte[3];
         byte[] var31 = new byte[3];
         byte[] var32 = new byte[3];
         byte[] var33 = new byte[3];
         byte[] var34 = new byte[3];
         byte var35 = -95;
         byte var36 = 1;
         short var37 = 942;
         short var38 = 792;
         short var39 = 797;
         short var40 = 798;
         short var41 = 799;
         short var42 = 801;
         short var43 = 813;
         short var44 = 778;
         short var45 = 787;
         pipe.createUsbControlIrp(var35, var36, var37, (short)0);
         UsbControlIrp var47 = pipe.createUsbControlIrp(var35, var36, var38, (short)0);
         UsbControlIrp var48 = pipe.createUsbControlIrp(var35, var36, var39, (short)0);
         UsbControlIrp var49 = pipe.createUsbControlIrp(var35, var36, var40, (short)0);
         UsbControlIrp var50 = pipe.createUsbControlIrp(var35, var36, var41, (short)0);
         UsbControlIrp var51 = pipe.createUsbControlIrp(var35, var36, var42, (short)0);
         UsbControlIrp var52 = pipe.createUsbControlIrp(var35, var36, var43, (short)0);
         UsbControlIrp var53 = pipe.createUsbControlIrp(var35, var36, var44, (short)0);
         UsbControlIrp var54 = pipe.createUsbControlIrp(var35, var36, var45, (short)0);
         var47.setData(var27);
         var48.setData(var28);
         var49.setData(var29);
         var50.setData(var30);
         var51.setData(var31);
         var52.setData(var32);
         var53.setData(var33);
         var54.setData(var34);

         for(; a >= 0; sleep((long)delay)) {
            read();
            byte[] var55;
            Record var64;
            if (a == 1) {
               a = 14;
               var55 = new byte[]{21, 1};
               var15.setData(var55);
               upsUsbDevice.syncSubmit(var15);
               var15.setComplete(false);
               deeptestBO = false;
               sleep(5000L);
            } else if (a == 2) {
               a = 14;
               var55 = new byte[]{21, 2};
               var15.setData(var55);
               upsUsbDevice.syncSubmit(var15);
               var15.setComplete(false);
               deeptestBO = true;
               sleep(5000L);
            } else if (a == 3) {
               a = 14;
               var55 = new byte[]{21, 3};
               var15.setData(var55);
               upsUsbDevice.syncSubmit(var15);
               var15.setComplete(false);
               sleep(1000L);
            } else {
               if (a == 4) {
                  a = -1;
                  var55 = new byte[]{15, (byte)(Label.upsdelay + 8), 0};
                  var23.setData(var55);
                  upsUsbDevice.syncSubmit(var23);
                  var23.setComplete(false);
                  sleep(5000L);
                  break;
               }

               if (a != 10 && a != 11) {
                  if (a == 5) {
                     a = -1;
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("UPS_turn_off_after_1_minute"));
                     System.out.println();
                     System.out.println("UPSMON : UPS will turn off its power after 1 minute !!");
                     System.out.println("UPSMON : UPS will turn ON its power again after 1.5 minute !!");
                     var55 = new byte[]{15, 9, 0};
                     var23.setData(var55);
                     upsUsbDevice.syncSubmit(var23);
                     var23.setComplete(false);
                     sleep(5000L);

                     String var56;
                     try {
                        var56 = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "User_Cmd";
                        Runtime.getRuntime().exec(var56);
                        sleep(5000L);
                     } catch (Exception var58) {
                        System.err.println("Exception : Class ConUSB3 : run() : e1 : " + var58);
                     }

                     try {
                        var56 = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                        System.out.println("UPSMON : Linux Shutdown !!");
                        var64 = UPSMON.RecordAPP;
                        Record.set(new String("Linux_Shutdown"));
                        var64 = UPSMON.RecordAPP;
                        Record.set2(new String("UPS_Shutdown"), String.valueOf(1));
                        sleep(1000L);
                        Runtime.getRuntime().exec(var56);
                     } catch (Exception var57) {
                        System.err.println("Exception : Class ConUSB3 : run() : e : " + var57);
                     }
                     break;
                  }

                  if (a == 6) {
                     a = 14;
                     var55 = new byte[]{49, 1};
                     var20.setData(var55);
                     upsUsbDevice.syncSubmit(var20);
                     var20.setComplete(false);
                     System.out.println("UPSMON : Outlet Group1 ON");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Outlet_Group1_ON"));
                     sleep(1000L);
                  } else if (a == 7) {
                     a = 14;
                     var55 = new byte[]{49, 0};
                     var20.setData(var55);
                     upsUsbDevice.syncSubmit(var20);
                     var20.setComplete(false);
                     System.out.println("UPSMON : Outlet Group1 OFF");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Outlet_Group1_OFF"));
                     sleep(1000L);
                  } else if (a == 8) {
                     a = 14;
                     var55 = new byte[]{50, 1};
                     var21.setData(var55);
                     upsUsbDevice.syncSubmit(var21);
                     var21.setComplete(false);
                     System.out.println("UPSMON : Outlet Group2 ON");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Outlet_Group2_ON"));
                     sleep(1000L);
                  } else if (a == 9) {
                     a = 14;
                     var55 = new byte[]{50, 0};
                     var21.setData(var55);
                     upsUsbDevice.syncSubmit(var21);
                     var21.setComplete(false);
                     System.out.println("UPSMON : Outlet Group2 OFF");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Outlet_Group2_OFF"));
                     sleep(1000L);
                  }
               } else {
                  a = 14;
                  var55 = new byte[]{19, 1};
                  var17.setData(var55);
                  upsUsbDevice.syncSubmit(var17);
                  var17.setComplete(false);
                  sleep(1000L);
               }
            }

            if (a <= 15) {
               ++a;
               var5.setData(var4);
               upsUsbDevice.syncSubmit(var5);
               var5.setComplete(false);
               var10.setData(var7);
               upsUsbDevice.syncSubmit(var10);
               var10.setComplete(false);
               var13.setData(var12);
               upsUsbDevice.syncSubmit(var13);
               var13.setComplete(false);
               sleep(1000L);
               Connect.readfile();
               var55 = new byte[]{16, 1, 0, 0, 0, 0};
               var25.setData(var55);
               upsUsbDevice.syncSubmit(var25);
               var25.setComplete(false);
               sleep(500L);
            }

            upsUsbDevice.syncSubmit(var47);
            upsUsbDevice.syncSubmit(var48);
            upsUsbDevice.syncSubmit(var49);
            upsUsbDevice.syncSubmit(var50);
            upsUsbDevice.syncSubmit(var51);
            upsUsbDevice.syncSubmit(var52);
            upsUsbDevice.syncSubmit(var53);
            upsUsbDevice.syncSubmit(var54);
            Input_Voltage = var28[1];
            if (var28[2] == 0) {
               if (Input_Voltage < 0) {
                  Input_Voltage += 256;
               }

               if (Input_Voltage >= 100) {
                  Q1 = "(" + String.valueOf(Input_Voltage) + ".0 ";
               } else if (Input_Voltage > 9) {
                  Q1 = "(0" + String.valueOf(Input_Voltage) + ".0 ";
               } else {
                  Q1 = "(00" + String.valueOf(Input_Voltage) + ".0 ";
               }
            } else if (var28[2] >= 1) {
               Input_Voltage = var28[1] + var28[2] * 256;
               Q1 = "(" + String.valueOf(Input_Voltage) + ".0 ";
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

            Q1 = Q1 + "000.0 ";
            Output_Voltage = var31[1];
            if (Output_Voltage < 0) {
               Output_Voltage += 256;
            }

            if (Output_Voltage >= 100) {
               Q1 = Q1 + String.valueOf(Output_Voltage) + ".0 ";
            } else if (Output_Voltage > 9) {
               Q1 = Q1 + "0" + Output_Voltage + ".0 ";
            } else {
               Q1 = Q1 + "00" + Output_Voltage + ".0 ";
            }

            data[7] = (byte)(Output_Voltage / 256);
            data[8] = (byte)(Output_Voltage % 256);
            Label.OutputVolt = Output_Voltage;
            Load = var30[1];
            if (Load < 0) {
               Load += 256;
            }

            if (Load >= 100) {
               Load = 100;
               Q1 = Q1 + String.valueOf(Load) + " ";
            } else if (Load > 9) {
               Q1 = Q1 + "0" + Load + " ";
            } else {
               Q1 = Q1 + "00" + Load + " ";
            }

            data[11] = (byte)Load;
            Label.Load = Load;
            Input_Frequency = var29[1];
            if (Input_Frequency < 0) {
               Input_Frequency += 256;
            }

            Label.InputFreq = Input_Frequency;
            if (Input_Frequency >= 10) {
               Q1 = Q1 + String.valueOf(Input_Frequency) + ".0 ";
            } else {
               Q1 = Q1 + "0" + Input_Frequency + ".0 ";
            }

            data[13] = (byte)Label.InputFreq;
            Battery_Capacity = var27[1];
            if (Battery_Capacity < 0) {
               Battery_Capacity += 256;
            }

            if (Battery_Capacity >= 100) {
               Q1 = Q1 + "0100 ";
            } else if (Battery_Capacity >= 10) {
               Q1 = Q1 + "00" + Battery_Capacity + " ";
            } else {
               Q1 = Q1 + "000" + Battery_Capacity + " ";
            }

            data[12] = (byte)Battery_Capacity;
            Label.Bat_level = Battery_Capacity;
            Temperature = var32[1];
            if (Temperature < 0) {
               Temperature += 256;
            }

            if (Temperature >= 10) {
               Q1 = Q1 + String.valueOf(Temperature) + ".0 ";
            } else {
               Q1 = Q1 + "0" + Temperature + ".0 ";
            }

            data[15] = (byte)Temperature;
            Label.Temperature = Temperature;
            int var63;
            if (var33[1] == 10 && var33[2] == 0) {
               if (bit7 == 0 && num > 2) {
                  bit7 = 1;
                  Label.b7 = 1;
                  soundBO = true;
                  green_modeBO = true;
                  battery_power = true;
                  temp = "Battery Power";
                  Label.PowerStatus = temp;
                  a = 30;
                  System.out.println("UPSMON : Power Failure");
                  var64 = UPSMON.RecordAPP;
                  Record.set(new String("Power_Failure"));
                  var63 = Integer.parseInt(UPSMON.shutdowntype);
                  if (var63 != 2) {
                     CountDOWN.usrcmd = true;
                     CountDOWN var61 = new CountDOWN();
                     var61.start();
                     System.out.println("UPSMON : Linux count down " + Label.osdelay + " seconds");
                  }

                  ++Label.Bat_times;
                  temp = String.valueOf(Label.Bat_times);
                  fo = new FileOutputStream(Label.fileBatTimes, false);
                  fo.write(temp.getBytes(), 0, temp.length());
                  fo.close();
                  sleep(5000L);
               }

               data[18] = 1;
            } else {
               if (bit7 == 1) {
                  bit7 = 0;
                  Label.b7 = 0;
                  System.out.println("UPSMON : Power Restore");
                  var64 = UPSMON.RecordAPP;
                  Record.set(new String("Power_Restore"));
                  CountDOWN.roopBO = false;
                  battery_power_stop = true;
                  temp = "AC Utility Power";
                  Label.PowerStatus = temp;
                  sleep(5000L);
               }

               data[18] = 0;
            }

            Q1 = Q1 + String.valueOf(bit7);
            if (Battery_Capacity <= 15) {
               if (bit6 == 0 && num > 2) {
                  bit6 = 1;
                  System.out.println("UPSMON : UPS Battery Low");
                  var64 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_Battery_Low"));
                  Connect.lowbat = 1;
                  data[19] = 1;
               }
            } else if (bit6 == 1) {
               bit6 = 0;
               Connect.lowbat = 0;
               data[19] = 0;
            }

            Q1 = Q1 + String.valueOf(bit6);
            if (var33[2] == 16) {
               if (bit5 == 0) {
                  bit5 = 1;
                  bit3 = 1;
                  System.out.println("UPSMON : AVR Boost");
                  var64 = UPSMON.RecordAPP;
                  Record.set(new String("AVR_Boost"));
                  temp = "AVR Boost";
                  Label.PowerStatus = temp;
                  data[17] = 2;
                  data[16] = 1;
               }
            } else if (var33[2] == 32) {
               if (bit5 == 0) {
                  bit5 = 1;
                  if (Input_Voltage == Output_Voltage) {
                     System.out.println("UPSMON : Bypass");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Bypass"));
                     bit3 = 0;
                     temp = "Bypass";
                     Label.PowerStatus = temp;
                     data[17] = 1;
                     data[16] = 0;
                  } else {
                     System.out.println("UPSMON : AVR Buck");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("AVR_Buck"));
                     bit3 = 1;
                     temp = "AVR Buck";
                     Label.PowerStatus = temp;
                     data[17] = 3;
                     data[16] = 1;
                  }
               }
            } else if (bit5 == 1) {
               bit5 = 0;
               System.out.println("UPSMON : Normal");
               var64 = UPSMON.RecordAPP;
               Record.set(new String("Normal"));
               data[17] = 0;
            }

            Q1 = Q1 + String.valueOf(bit5);
            if (var33[1] == -115 && var33[2] == 0) {
               if (bit4 == 0) {
                  bit4 = 1;
                  bit3 = 1;
                  System.out.println("UPSMON : UPS Failed");
                  var64 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_Failed"));
                  data[21] = 1;
               }
            } else if (var33[1] == -116 && var33[2] == 0) {
               if (bit4 == 0) {
                  bit4 = 1;
                  bit3 = 1;
                  System.out.println("UPSMON : UPS Failed");
                  var64 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_Failed"));
                  data[21] = 1;
               }
            } else if (bit4 == 1) {
               bit3 = 0;
               bit4 = 0;
               System.out.println("UPSMON : UPS Normal");
               var64 = UPSMON.RecordAPP;
               Record.set(new String("UPS_Normal"));
               data[21] = 0;
            }

            Q1 = Q1 + String.valueOf(bit4);
            Q1 = Q1 + String.valueOf(bit3);
            int var62;
            if (var33[2] == 64) {
               if (bit2 == 0) {
                  soundBO = true;
                  green_modeBO = true;
                  bit2 = 1;
                  data[23] = 1;
                  if (deeptestBO) {
                     System.out.println("UPSMON : Battery Deep Test");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Battery_Deep_Test"));
                     cal1 = Calendar.getInstance();
                     deeptestBO = true;
                  } else {
                     System.out.println("UPSMON : Battery Test");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Battery_Test"));
                     deeptestBO = false;
                  }

                  testflag = true;
                  ++Label.Bat_test_times;
                  temp = String.valueOf(Label.Bat_test_times);
                  fo = new FileOutputStream(Label.fileBatTestTimes, false);
                  fo.write(temp.getBytes(), 0, temp.length());
                  fo.close();
               }
            } else if (bit2 == 1) {
               bit2 = 0;
               data[23] = 0;
               if (deeptestBO) {
                  cal2 = Calendar.getInstance();
                  var63 = (int)((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000L);
                  if (var63 < 60) {
                     System.out.println("UPSMON : Battery Test Time (" + var63 + " secs)");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Battery_Test_Time_(" + var63 + "_secs)"));
                  } else {
                     var62 = var63 / 60;
                     var63 %= 60;
                     System.out.println("UPSMON : Battery Test Time (" + var62 + " mins " + var63 + " secs)");
                     var64 = UPSMON.RecordAPP;
                     Record.set(new String("Battery_Test_Time_(" + var62 + "_mins_" + var63 + "_secs)"));
                  }
               }

               System.out.println("UPSMON : Battery Normal");
               var64 = UPSMON.RecordAPP;
               Record.set(new String("Battery_Normal"));
               Label.Battestresult = "OK";
               temp2 = "Battery Normal";
               fo = new FileOutputStream(Label.fileBatResult, false);
               fo.write(temp2.getBytes(), 0, temp2.length());
               fo.close();
            }

            Q1 = Q1 + String.valueOf(bit2);
            data[9] = 0;
            data[10] = 0;
            Q1 = Q1 + String.valueOf(bit1) + bit0 + "\n";
            if (Q1.length() >= 44) {
               try {
                  var55 = Q1.getBytes();
                  var62 = var55.length;
                  if (var62 != 0) {
                     fo = new FileOutputStream(Label.fileget, false);
                     fo.write(var55, 0, var62);
                     fo.close();
                     Q1 = String.valueOf(Remaining_Time);
                     fo = new FileOutputStream(Label.fileRt, false);
                     fo.write(Q1.getBytes(), 0, Q1.length());
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

                     temp2 = temp + "," + Label.InputVolt + "," + Label.OutputVolt + "," + Label.Temperature + "," + Label.Load + "," + Label.Bat_level + "\n";
                     if (a <= 15) {
                        filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + cal3.get(1) + "_" + (cal3.get(2) + 1) + "_" + cal3.get(5) + ".csv";
                     } else if (cal3.get(11) == 0 && cal3.get(12) <= 3) {
                        filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + cal3.get(1) + "_" + (cal3.get(2) + 1) + "_" + cal3.get(5) + ".csv";
                     }

                     if (num % 2 == 0) {
                        fo = new FileOutputStream(filelog, true);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     }

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
               } catch (Exception var59) {
                  System.err.println("Exception : Class ConUSB3 : write() : " + var59);
               }
            }

            var47.setComplete(false);
            var48.setComplete(false);
            var49.setComplete(false);
            var50.setComplete(false);
            var51.setComplete(false);
            var52.setComplete(false);
            var53.setComplete(false);
            var54.setComplete(false);
            ++a;
            ++num;
            if (a >= 50 && a < 100) {
               delay = 5000;
            } else {
               delay = 10000;
            }
         }
      } catch (Exception var60) {
         System.out.println();
         System.out.println();
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
               } else if (temp2.equals("2")) {
                  a = 2;
               } else if (temp2.equals("0")) {
                  a = 3;
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

         if (f2.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));

            while((temp = br.readLine()) != null) {
               if (temp.equals("1")) {
                  a = 6;
               } else if (temp.equals("2")) {
                  a = 7;
               }

               temp2 = "";
               fo = new FileOutputStream(f2, false);
               fo.write(temp2.getBytes(), 0, temp2.length());
               fo.close();
            }

            br.close();
         }

         if (f3.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f3)));

            while((temp = br.readLine()) != null) {
               if (temp.equals("1")) {
                  a = 8;
               } else if (temp.equals("2")) {
                  a = 9;
               }

               temp2 = "";
               fo = new FileOutputStream(f3, false);
               fo.write(temp2.getBytes(), 0, temp2.length());
               fo.close();
            }

            br.close();
         }

         if (f5.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f5)));

            while((temp2 = br.readLine()) != null) {
               if (temp2.equals("2")) {
                  a = 10;
               } else if (temp2.equals("1")) {
                  a = 11;
               }

               temp2 = "";
               fo = new FileOutputStream(f5, false);
               fo.write(temp2.getBytes(), 0, temp2.length());
               fo.close();
            }

            br.close();
         }

         if (f_exit.exists()) {
            temp2 = "1";
            fo = new FileOutputStream(f_exit, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
            System.exit(0);
         }
      } catch (Exception var1) {
         System.err.println("Exception : Class ConUSB3 : read() : e : " + var1);
      }

   }

   static {
      f0 = new File(Label.fileConfigBatTest);
      f1 = new File(Label.fileConfigUpsDown);
      f2 = new File(Label.fileConfigOutlet1);
      f3 = new File(Label.fileConfigOutlet2);
      f4 = new File(Label.fileConfigUpsOff);
      f5 = new File(Label.fileConfigSilence);
      f_exit = new File(Label.fileConfigExit);
      filelog = "";
      Q1 = "";
      c1 = "";
      temp2 = null;
      br = null;
      fo = null;
      cal3 = null;
      pipe = null;
      aa = 0;
      bb = 0;
      data = new byte[]{80, 67, 77, 82, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 69, 78, 68};
   }
}
