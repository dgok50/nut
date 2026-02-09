/* Decompiler 143ms, total 485ms, lines 338 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

class ConCOM1Set extends Thread {
   ConCOM1 comApp;
   OutputStream out;
   public static int roopINT = 0;
   public static int a = 0;
   public static boolean soundBO = false;
   public static boolean green_modeBO = false;
   public static FileOutputStream fo = null;
   public static BufferedReader br = null;
   public static File ff = null;
   public static File f0;
   public static File f1;
   public static File f4;
   public static File f5;
   public static File f_exit;
   public static String temp2;
   public static String controloff;

   public ConCOM1Set(OutputStream var1, ConCOM1 var2) {
      this.out = var1;
      this.comApp = var2;

      try {
         ff = new File(Label.fileConfigInterval);
         if (ff.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
            temp2 = br.readLine();
            Label.ConfigIntervall = Integer.parseInt(temp2) * 1000;
            br.close();
         } else {
            temp2 = "2";
            fo = new FileOutputStream(ff, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }

         ff = new File(Label.fileBatStart);
         if (ff.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
            Label.Batpowerstart = br.readLine();
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

         ff = new File(Label.fileBatStop);
         if (ff.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(ff)));
            Label.Batpowerend = br.readLine();
            br.close();
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

         if (!f0.exists()) {
            temp2 = " ";
            fo = new FileOutputStream(f0, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }

         if (!f4.exists()) {
            temp2 = "0";
            fo = new FileOutputStream(f4, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }
      } catch (Exception var4) {
         System.err.println("Exception : Class ConCOM1Set : ConCOM1Set()\t: e : " + var4);
      }

   }

   public void run() {
      try {
         BufferedOutputStream var1 = new BufferedOutputStream(this.out);
         byte var2 = 1;
         String var3 = null;

         for(Object var4 = null; roopINT >= 0; ++roopINT) {
            read();
            if (roopINT % 20 == 0) {
               Connect.readfile();
            }

            var1.write(var2);
            var1.flush();
            sleep((long)Label.ConfigIntervall);
            if (roopINT >= 10) {
               byte var5;
               if (a == 1) {
                  var5 = 3;
                  var1.write(var5);
                  var1.flush();
                  sleep(1000L);
                  a = 0;
               } else if (a == 2) {
                  byte[] var14 = new byte[]{-71, -68, (byte)Label.upsdelay, 0};
                  var1.write(var14);
                  var1.flush();
                  sleep(1000L);
                  roopINT = -1;
               } else if (a == 3) {
                  System.out.println("UPSMON : Beeper ON");
                  if (ConCOM1Get.Byte_10[3] == 1) {
                     var5 = 5;
                     var1.write(var5);
                     var1.flush();
                  }

                  try {
                     temp2 = "0";
                     fo = new FileOutputStream(f5, false);
                     fo.write(temp2.getBytes(), 0, temp2.length());
                     fo.close();
                  } catch (Exception var12) {
                     System.err.println("Exception : Class ConSNMPSet : run() : e7 : " + var12);
                  }

                  a = 0;
                  sleep(1000L);
               } else if (a == 4) {
                  System.out.println("UPSMON : Beeper OFF");
                  if (ConCOM1Get.Byte_10[3] == 0) {
                     var5 = 5;
                     var1.write(var5);
                     var1.flush();
                  }

                  try {
                     temp2 = "0";
                     fo = new FileOutputStream(f5, false);
                     fo.write(temp2.getBytes(), 0, temp2.length());
                     fo.close();
                  } catch (Exception var11) {
                     System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + var11);
                  }

                  a = 0;
                  sleep(1000L);
               } else if (a == 5) {
                  byte var15 = (byte)Integer.parseInt(controloff);
                  byte[] var6 = new byte[]{-71, -68, var15, 0};
                  Record var10000 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_turn_off_after_" + controloff + "_minutes"));
                  System.out.println("UPSMON : UPS will turn off power after " + controloff + " minutes");
                  var1.write(var6);
                  var1.flush();
                  sleep(3000L);

                  try {
                     String var7 = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                     System.out.println("UPSMON : Linux Shutdown !!");
                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("Linux_Shutdown"));
                     var10000 = UPSMON.RecordAPP;
                     Record.set2(new String("UPS_Shutdown"), controloff);
                     sleep(1000L);
                     Runtime.getRuntime().exec(var7);
                  } catch (Exception var10) {
                     System.err.println("Exception : Class ConCOM1Set : run() : e : " + var10);
                  }

                  try {
                     var3 = "0";
                     fo = new FileOutputStream(f4, false);
                     fo.write(var3.getBytes(), 0, var3.length());
                     fo.close();
                  } catch (Exception var9) {
                     System.err.println("Exception : Class ConSNMPSet : run() : e9 : " + var9);
                  }

                  roopINT = -1;
               }
            }

            if (roopINT == 5) {
               if (!ConCOM1.BO1200) {
                  roopINT = -1;
                  break;
               }

               Connect.model = 6;
               roopINT = 10;
            }

            if (roopINT == 100) {
               roopINT = 11;
            }
         }
      } catch (Exception var13) {
      }

      try {
         this.out.close();
      } catch (IOException var8) {
         System.err.println("Exception : Class ConCOM1 : Class ConCOM1Set : run() : e2 : " + var8);
      }

   }

   public static void read() {
      try {
         if (f0.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f0)));

            while(true) {
               if ((temp2 = br.readLine()) == null) {
                  br.close();
                  f0.delete();
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
                  a = 2;
               }

               temp2 = "";
               fo = new FileOutputStream(f1, false);
               fo.write(temp2.getBytes(), 0, temp2.length());
               fo.close();
            }

            br.close();
            f1.delete();
         }

         if (f4.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f4)));

            while((temp2 = br.readLine()) != null) {
               if (!temp2.equals("0")) {
                  controloff = temp2;
                  a = 5;
               }
            }

            br.close();
            f4.delete();
         }

         if (ConCOM1Get.bit7 == 1) {
            if (f5.exists()) {
               br = new BufferedReader(new InputStreamReader(new FileInputStream(f5)));

               while((temp2 = br.readLine()) != null) {
                  if (temp2.equals("2")) {
                     a = 4;
                  } else if (temp2.equals("1")) {
                     a = 3;
                  }
               }

               br.close();
            }

            f5.delete();
         }

         if (f_exit.exists()) {
            temp2 = "1";
            fo = new FileOutputStream(f5, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
            System.exit(0);
         }
      } catch (Exception var1) {
         System.err.println("Exception : Class ConCOM1 : read() : e : " + var1);
      }

   }

   static {
      f0 = new File(Label.fileConfigBatTest);
      f1 = new File(Label.fileConfigUpsDown);
      f4 = new File(Label.fileConfigUpsOff);
      f5 = new File(Label.fileConfigSilence);
      f_exit = new File(Label.fileConfigExit);
      temp2 = "";
      controloff = "";
   }
}
