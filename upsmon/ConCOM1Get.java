/* Decompiler 663ms, total 1039ms, lines 710 */
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

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
   public static int[] Byte_9 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
   public static int[] Byte_10 = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
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
   static byte[] data = new byte[]{80, 67, 77, 82, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 69, 78, 68};

   public ConCOM1Get(InputStream var1, ConCOM1 var2) {
      this.in = var1;
      this.comApp = var2;
   }

   public void run() {
      BufferedInputStream var1 = null;

      try {
         int[] var2 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
         var1 = new BufferedInputStream(this.in);
         int var3 = 0;
         int var4 = 0;
         int var5 = 0;
         byte[] var6 = new byte[64];
         boolean var7 = false;

         while(ConCOM1Set.roopINT >= 0) {
            int var22 = var1.read(var6);
            if (var22 == -1) {
               break;
            }

            try {
               for(int var8 = 0; var8 < var22; ++var8) {
                  int var9 = var6[var8];
                  if (var9 < 0) {
                     var9 += 256;
                  }

                  var2[var3] = var9;
                  if (var3 == 9) {
                     var4 = var9;
                  } else if (var3 == 10) {
                     var5 = var9;
                  }

                  ++var3;
                  if (var3 == 16) {
                     var3 = 0;
                  }
               }

               this.byte_9tobit(var4);
               this.byte_10tobit(var5);
               this.caculate(var2);
            } catch (Exception var19) {
               System.err.println("Exception : Class ConCOM1Get : run() : e1 :" + var19);
            }
         }
      } catch (Exception var20) {
      } finally {
         try {
            if (var1 != null) {
               var1.close();
            }
         } catch (Exception var18) {
            System.err.println("Exception : Class ConCOM1Get : run() : e3 :" + var18);
         }

      }

   }

   public void byte_10tobit(int var1) {
      Byte_10[7] = var1 / 128;
      int var2 = var1 % 128;
      Byte_10[6] = var2 / 64;
      int var3 = var2 % 64;
      Byte_10[5] = var3 / 32;
      int var4 = var3 % 32;
      Byte_10[4] = var4 / 16;
      int var5 = var4 % 16;
      Byte_10[3] = var5 / 8;
      int var6 = var5 % 8;
      Byte_10[2] = var6 / 4;
      int var7 = var6 % 4;
      Byte_10[1] = var7 / 2;
      Byte_10[0] = var7 % 2;
   }

   public void byte_9tobit(int var1) {
      Byte_9[7] = var1 / 128;
      int var2 = var1 % 128;
      Byte_9[6] = var2 / 64;
      int var3 = var2 % 64;
      Byte_9[5] = var3 / 32;
      int var4 = var3 % 32;
      Byte_9[4] = var4 / 16;
      int var5 = var4 % 16;
      Byte_9[3] = var5 / 8;
      int var6 = var5 % 8;
      Byte_9[2] = var6 / 4;
      int var7 = var6 % 4;
      Byte_9[1] = var7 / 2;
      Byte_9[0] = var7 % 2;
   }

   public void caculate(int[] var1) {
      if (b == 0) {
         try {
            temp = "POWERCOM";
            fo = new FileOutputStream(Label.fileCompany, false);
            fo.write(temp.getBytes(), 0, temp.length());
            fo.close();
         } catch (Exception var10) {
            System.err.println("Exception : Class ConCOM1 : caculate() : e : " + var10);
         }
      }

      temp = "(";
      if (var1[5] >= 240) {
         this.iUPS = 15;
      }

      Input_Voltage = this.cacuinvoltage(var1);
      Label.InputVolt = Input_Voltage;
      if (Input_Voltage == 0) {
         temp = temp + "000.0";
      } else if (Input_Voltage < 10) {
         temp = temp + "00" + Input_Voltage + ".0";
      } else if (Input_Voltage < 100) {
         temp = temp + "0" + Input_Voltage + ".0";
      } else {
         temp = temp + Input_Voltage + ".0";
      }

      data[5] = (byte)(Input_Voltage / 256);
      data[6] = (byte)(Input_Voltage % 256);
      if (Label.InputVoltMax < Label.InputVolt) {
         Label.InputVoltMax = Label.InputVolt;
      }

      if (Label.InputVoltMini > Label.InputVolt) {
         Label.InputVoltMini = Label.InputVolt;
      }

      temp = temp + " 000.0 ";
      Output_Voltage = this.cacuonvoltage(var1);
      Label.OutputVolt = Output_Voltage;
      if (Output_Voltage == 0) {
         temp = temp + "000.0";
      } else if (Output_Voltage < 10) {
         temp = temp + "00" + Output_Voltage + ".0";
      } else if (Output_Voltage < 100) {
         temp = temp + "0" + Output_Voltage + ".0";
      } else {
         temp = temp + Output_Voltage + ".0";
      }

      data[7] = (byte)(Output_Voltage / 256);
      data[8] = (byte)(Output_Voltage % 256);
      Load = this.cacuload(var1);
      Label.Load = Load;
      if (Load < 10) {
         temp = temp + " 00" + Load;
      } else {
         temp = temp + " 0" + Load;
      }

      data[11] = (byte)Load;
      Input_Frequency = this.ipfrequency(var1);
      if (Input_Frequency < 10) {
         temp = temp + " 0" + Input_Frequency + ".0";
      } else {
         temp = temp + " " + Input_Frequency + ".0";
      }

      data[13] = (byte)Input_Frequency;
      Battery_Capacity = this.battery(var1);
      Label.Bat_level = Battery_Capacity;
      if (Battery_Capacity >= 99) {
         temp = temp + " 0100";
      } else if (Battery_Capacity >= 10) {
         temp = temp + " 00" + Battery_Capacity;
      } else if (Battery_Capacity >= 3) {
         temp = temp + " 000" + Battery_Capacity;
      } else {
         temp = temp + " 0000";
      }

      data[12] = (byte)Battery_Capacity;
      Output_Frequency = this.opfrequency(var1);
      Label.InputFreq = Output_Frequency;
      if (Output_Frequency < 10) {
         temp = temp + " 0" + Output_Frequency + ".0";
      } else {
         temp = temp + " " + Output_Frequency + ".0";
      }

      data[14] = (byte)Output_Frequency;
      data[15] = -1;
      data[16] = 1;
      Record var10000;
      if (Byte_9[0] == 1) {
         if (Byte_10[2] != 1) {
            temp = temp + " 1";
            if (bit7 == 0) {
               bit7 = 1;
               battery_power = true;
               System.out.println("UPSMON : Power Failure");
               var10000 = UPSMON.RecordAPP;
               Record.set(new String("Power_Failure"));
               Label.PowerStatus = "Battery Power";
               ConCOM1Set.soundBO = true;
               ConCOM1Set.green_modeBO = true;

               try {
                  ++Label.Bat_times;
                  temp = String.valueOf(Label.Bat_times);
                  fo = new FileOutputStream(Label.fileBatTimes, false);
                  fo.write(temp.getBytes(), 0, temp.length());
                  fo.close();
               } catch (Exception var9) {
                  System.err.println("Exception : Class ConCOM1 : caculate() : ee : " + var9);
               }

               countdownthreadflag = true;
               CountDOWN.usrcmd = true;
               CountDOWN var2 = new CountDOWN();
               var2.start();
               System.out.println("UPSMON : Linux count down " + Label.osdelay + " seconds");
            }

            data[18] = 1;
         }
      } else {
         temp = temp + " 0";
         if (bit7 == 1) {
            bit7 = 0;
            battery_power_stop = true;
            System.out.println("UPSMON : Power Restore");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Power_Restore"));
            countdownthreadflag = false;
            CountDOWN.roopBO = false;
         }

         data[18] = 0;
         Label.PowerStatus = "Normal";
      }

      if (Byte_9[1] == 1) {
         temp = temp + "1";
         if (bit6 == 0) {
            bit6 = 1;
            System.out.println("UPSMON : Low Battery");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Low_Battery"));
            Label.PowerStatus = "Battery Low";
            Connect.lowbat = 1;
         }

         data[19] = 1;
      } else {
         temp = temp + "0";
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
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Overload"));
            Label.PowerStatus = "Overload";
         }

         data[20] = 1;
      } else {
         if (overload == 1) {
            overload = 0;
            System.out.println("UPSMON : Normal");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Normal"));
         }

         data[20] = 0;
      }

      if (Byte_9[3] == 1) {
         temp = temp + "1";
         if (bit5 == 0) {
            bit5 = 1;
            if (Input_Voltage > Output_Voltage) {
               System.out.println("UPSMON : Buck");
               var10000 = UPSMON.RecordAPP;
               Record.set(new String("Buck"));
               Label.PowerStatus = "AVR Buck";
               data[17] = 3;
            } else if (Output_Voltage > Input_Voltage) {
               System.out.println("UPSMON : Boost");
               var10000 = UPSMON.RecordAPP;
               Record.set(new String("Boost"));
               Label.PowerStatus = "AVR Boost";
               data[17] = 2;
            }
         }
      } else {
         temp = temp + "0";
         if (bit5 == 1) {
            bit5 = 0;
            System.out.println("UPSMON : Normal");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Normal"));
         }

         data[17] = 0;
      }

      if (Byte_10[0] == 1) {
         temp = temp + "1";
         if (bit4 == 0) {
            bit4 = 1;
            System.out.println("UPSMON : UPS Failed");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("UPS_Failed"));
         }

         data[21] = 1;
      } else {
         temp = temp + "0";
         if (bit4 == 1 || !upsfailflag) {
            bit4 = 0;
            batbadflag = true;
            System.out.println("UPSMON : Normal");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Normal"));
         }

         data[21] = 0;
      }

      if (Byte_10[1] == 1) {
         temp = temp + "3";
         if (batbadflag) {
            bit3 = 2;
            batbadflag = false;
            System.out.println("UPSMON : Battery Failed");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Battery_Failed"));

            try {
               Label.Battestresult = "Battery Failed";
               temp = "Battery Failed";
               fo = new FileOutputStream(Label.fileBatResult, false);
               fo.write(temp.getBytes(), 0, temp.length());
               fo.close();
            } catch (Exception var8) {
               System.err.println("Exception : Class ConCOM1Get : caculate() : e3 : " + var8);
            }
         }

         data[22] = 1;
      } else {
         temp = temp + "1";
         if (bit3 == 2 || !batbadflag) {
            bit3 = 0;
            batbadflag = true;
            System.out.println("UPSMON : Battery Normal");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Battery_Normal"));
         }

         data[22] = 0;
      }

      if (Byte_10[2] == 1) {
         temp = temp + "1";
         if (selftestflag) {
            selftestflag = false;

            try {
               ConCOM1Set.soundBO = true;
               System.out.println("UPSMON : UPS Battery Test");
               var10000 = UPSMON.RecordAPP;
               Record.set(new String("UPS_Battery_Test"));
               ++Label.Bat_test_times;
               temp = String.valueOf(Label.Bat_test_times);
               fo = new FileOutputStream(Label.fileBatTestTimes, false);
               fo.write(temp.getBytes(), 0, temp.length());
               testflag = true;
               fo.close();
            } catch (Exception var7) {
               System.err.println("Exception : Class ConCOM1Get : caculate() : e1 : " + var7);
            }
         }

         data[23] = 1;
      } else {
         temp = temp + "0";
         if (!selftestflag) {
            selftestflag = true;
            data[23] = 0;
            System.out.println("UPSMON : Battery Normal");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("Battery_Normal"));

            try {
               Label.Battestresult = "OK";
               temp = "Battery Normal";
               fo = new FileOutputStream(Label.fileBatResult, false);
               fo.write(temp.getBytes(), 0, temp.length());
               fo.close();
            } catch (Exception var6) {
               System.err.println("Exception : Class ConCOM1Get : caculate() : e2 : " + var6);
            }
         }
      }

      if (Byte_10[4] == 1) {
         if (!upsoffBO) {
            upsoffBO = true;
            bit1 = 1;
         }

         temp = temp + "0";
      } else {
         if (upsoffBO) {
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("UPS_OFF"));
            System.out.println("UPSMON : UPS OFF");
            var10000 = UPSMON.RecordAPP;
            Record.set(new String("UPS_ON"));
            System.out.println("UPSMON : UPS ON");
            temp = temp + "1";
         } else {
            temp = temp + "0";
         }

         upsoffBO = false;
         bit1 = 0;
      }

      ++b;
      if (b == 1000) {
         b = 10;
      }

      if (Byte_10[3] == 0) {
         temp = temp + "1";
      } else {
         temp = temp + "0";
      }

      if (temp.length() >= 44) {
         if (!ConCOM1.BO1200) {
            ConCOM1.BO1200 = true;
         }

         try {
            byte[] var11 = temp.getBytes();
            int var3 = var11.length;
            if (var3 != 0) {
               fo = new FileOutputStream(Label.fileget, false);
               fo.write(var11, 0, var3);
               fo.close();
            }
         } catch (Exception var5) {
            System.err.println("Exception : Class ConCOM1Get : caculate : e2 : " + var5);
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
               data1 = data1 + "0" + timeINT + ":";
            } else {
               data1 = data1 + timeINT + ":";
            }

            timeINT = cal3.get(13);
            if (timeINT <= 9) {
               data1 = data1 + "0" + timeINT + ",";
            } else {
               data1 = data1 + timeINT + ",";
            }

            data1 = data1 + Input_Voltage + "," + Output_Voltage + "," + Input_Frequency + "," + Load + "," + Battery_Capacity + "\n";
            if (b <= 3) {
               filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + cal3.get(1) + "_" + (cal3.get(2) + 1) + "_" + cal3.get(5) + ".csv";
            } else if (cal3.get(11) == 0 && cal3.get(12) <= 3) {
               filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + cal3.get(1) + "_" + (cal3.get(2) + 1) + "_" + cal3.get(5) + ".csv";
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
               data1 = data1 + "0" + timeINT + ":";
            } else {
               data1 = data1 + timeINT + ":";
            }

            timeINT = cal3.get(13);
            if (timeINT <= 9) {
               data1 = data1 + "0" + timeINT;
            } else {
               data1 = data1 + timeINT;
            }

            timeINT = cal3.get(2) + 1;
            if (timeINT <= 9) {
               data1 = data1 + " " + cal3.get(1) + "/0" + timeINT;
            } else {
               data1 = data1 + " " + cal3.get(1) + "/" + timeINT;
            }

            timeINT = cal3.get(5);
            if (timeINT <= 9) {
               data1 = data1 + "/0" + timeINT;
            } else {
               data1 = data1 + "/" + timeINT;
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
         } catch (Exception var4) {
            System.err.println("Exception : Class ConCOM1Get : caculate : e3 : " + var4);
         }
      }

   }

   public int battery(int[] var1) {
      if (this.iUPS == 15) {
         Batt = (double)var1[1];
      }

      if (this.BatLow == 1 && Batt > 29.0D || this.BatLow == 0 && Batt < 29.0D) {
         Batt = 29.0D;
      }

      if (Batt < 0.0D) {
         Batt = 0.0D;
      }

      if (Batt > 100.0D) {
         Batt = 100.0D;
      }

      Battery_Capacity = (int)Math.round(Batt);
      return Battery_Capacity;
   }

   public int opfrequency(int[] var1) {
      int var2 = 0;
      if (var1[6] != 0 && this.SDMode != 1) {
         var2 = 4807 / var1[6];
         if (this.iUPS == 15) {
            var2 = var1[6];
         }
      }

      if (var2 > 90) {
         var2 = 0;
      }

      var2 = Math.round((float)var2);
      String var3 = String.valueOf(var2);
      return var2;
   }

   public int cacuinvoltage(int[] var1) {
      if (this.iUPS == 15) {
         Involtage = (double)(var1[2] * 2);
      }

      if (Involtage < 25.0D) {
         Involtage = 0.0D;
      }

      Input_Voltage = (int)Math.round(Involtage);
      return Input_Voltage;
   }

   public int cacuonvoltage(int[] var1) {
      if (this.iUPS == 15) {
         Output_Voltage = var1[3] * 2;
      }

      Output_Voltage = Math.round((float)Output_Voltage);
      return Output_Voltage;
   }

   public int cacuload(int[] var1) {
      if (this.iUPS == 15) {
         Load = var1[0];
      }

      Load = Math.round((float)Load);
      return Load;
   }

   public int ipfrequency(int[] var1) {
      if (var1[4] != 0) {
         Input_Frequency = 4807 / var1[4];
      } else {
         Input_Frequency = 0;
      }

      if (this.iUPS == 15) {
         Input_Frequency = var1[4];
      }

      if (Input_Voltage <= 20) {
         Input_Frequency = 0;
      }

      Input_Frequency = Math.round((float)Input_Frequency);
      return Input_Frequency;
   }
}
