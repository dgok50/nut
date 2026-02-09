/* Decompiler 201ms, total 629ms, lines 537 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

class ConCOM2Set extends Thread {
   public static int roopINT = 1;
   public static int a = 0;
   public static byte[] a1 = new byte[]{68, 81, 49, 13};
   public static byte[] a2 = new byte[]{81, 49, 13};
   public static byte[] F_command = new byte[]{70, 13};
   public static byte[] OI = new byte[]{79, 73, 13};
   public static byte[] Rt = new byte[]{82, 116, 13};
   public static byte[] I = new byte[]{73, 13};
   public static byte[] F = new byte[]{70, 13};
   public static byte[] Yop = new byte[]{89, 111, 112, 13};
   public static boolean a1BO = false;
   public static boolean a2BO = false;
   public static boolean soundBO = false;
   public static boolean green_modeBO = false;
   public static boolean rtBO = false;
   public static boolean YopBO = false;
   public static boolean OIBO = false;
   public static boolean Q1BO = false;
   public static boolean deeptestBO = false;
   public static boolean IBO = false;
   public static boolean FBO = false;
   public static BufferedReader br = null;
   public static File f0;
   public static File f1;
   public static File f2;
   public static File f3;
   public static File f4;
   public static File f5;
   public static File f_exit;
   public static File ff;
   public static String controloff;
   public static String temp2;
   public static FileOutputStream fo;
   OutputStream out;

   public ConCOM2Set(OutputStream var1) {
      this.out = var1;

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

         if (!f0.exists()) {
            temp2 = " ";
            fo = new FileOutputStream(f0, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }

         if (!f2.exists()) {
            temp2 = "0";
            fo = new FileOutputStream(f2, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }

         if (!f3.exists()) {
            temp2 = "0";
            fo = new FileOutputStream(f3, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }

         if (!f4.exists()) {
            temp2 = "0";
            fo = new FileOutputStream(f4, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
         }

         if (!f5.exists()) {
            temp2 = "0";
            fo = new FileOutputStream(f5, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
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
      } catch (Exception var3) {
         System.err.println("Exception : Class ConCOM2Set : ConCOM2Set : e0 : " + var3);
      }

   }

   public void run() {
      try {
         BufferedOutputStream var1 = new BufferedOutputStream(this.out);
         String var2 = null;
         Object var3 = null;

         while(roopINT >= 0) {
            read();
            if (roopINT % 60 == 0) {
               Connect.readfile();
               sleep(1000L);
            }

            if (roopINT <= 2) {
               IBO = true;
               var1.write(I);
               var1.flush();
               sleep(2000L);
            } else if (roopINT <= 4) {
               FBO = true;
               var1.write(F);
               var1.flush();
               sleep(2000L);
            } else if (roopINT % 10 == 0) {
               rtBO = true;
               var1.write(Rt);
               var1.flush();
               sleep(2000L);
            } else if (roopINT % 15 == 0) {
               YopBO = true;
               var1.write(Yop);
               var1.flush();
               sleep(2000L);
            } else if (roopINT % 2 == 0) {
               var1.write(a1);
               var1.flush();
               sleep((long)Label.ConfigIntervall);
            } else {
               var1.write(a2);
               var1.flush();
               sleep((long)Label.ConfigIntervall);
            }

            ++roopINT;
            if (roopINT == 1000) {
               roopINT = 20;
            }

            byte[] var4;
            if (a == 1) {
               var4 = new byte[]{84, 13};
               var1.write(var4);
               var1.flush();
               soundBO = true;
               deeptestBO = false;
               a = 0;
               sleep(1000L);
            } else if (a == 2) {
               var4 = new byte[]{84, 76, 13};
               var1.write(var4);
               var1.flush();
               deeptestBO = true;
               a = 0;
               sleep(1000L);
            } else if (a == 3) {
               var4 = new byte[]{67, 84, 13};
               var1.write(var4);
               var1.flush();
               a = 0;
               sleep(1000L);
            } else {
               String var5;
               if (a == 4) {
                  var4 = new byte[]{83, 48, 0, 13};
                  var5 = String.valueOf(Label.upsdelay);
                  if (Label.upsdelay < 10) {
                     var4[2] = (byte)var5.charAt(0);
                  } else if (Label.upsdelay >= 10) {
                     var4[1] = (byte)var5.charAt(0);
                     var4[2] = (byte)var5.charAt(1);
                  }

                  var1.write(var4);
                  var1.flush();
                  roopINT = -1;
               } else {
                  Record var10000;
                  if (a == 5) {
                     var4 = new byte[]{83, 48, 0, 13};
                     if (controloff.length() == 1) {
                        var4[2] = (byte)controloff.charAt(0);
                     } else if (controloff.length() == 2) {
                        var4[1] = (byte)controloff.charAt(0);
                        var4[2] = (byte)controloff.charAt(1);
                     }

                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("UPS_turn_off_after_" + controloff + "_minutes"));
                     System.out.println("UPSMON : UPS will turn off power after " + controloff + " minutes");
                     var1.write(var4);
                     var1.flush();
                     sleep(3000L);

                     try {
                        var5 = "EXT" + UPSMON.separaST + "Execute" + UPSMON.separaST + "DOWN";
                        System.out.println("UPSMON : Linux Shutdown !!");
                        var10000 = UPSMON.RecordAPP;
                        Record.set(new String("Linux_Shutdown"));
                        var10000 = UPSMON.RecordAPP;
                        Record.set2(new String("UPS_Shutdown"), controloff);
                        sleep(1000L);
                        Runtime.getRuntime().exec(var5);
                     } catch (Exception var14) {
                        System.err.println("Exception : Class ConCOM2Set : run() : e : " + var14);
                     }

                     try {
                        var2 = "0";
                        fo = new FileOutputStream(f4, false);
                        fo.write(var2.getBytes(), 0, var2.length());
                        fo.close();
                     } catch (Exception var13) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e9 : " + var13);
                     }

                     a = 0;
                  } else if (a == 6) {
                     var4 = new byte[]{79, 48, 49, 79, 78, 13};
                     var1.write(var4);
                     var1.flush();
                     System.out.println("UPSMON : Outlet Group1 ON");
                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("Outlet_Group1_ON"));

                     try {
                        temp2 = "0";
                        fo = new FileOutputStream(f2, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     } catch (Exception var12) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + var12);
                     }

                     a = 0;
                     sleep(1000L);
                  } else if (a == 7) {
                     var4 = new byte[]{79, 48, 49, 79, 70, 70, 13};
                     var1.write(var4);
                     var1.flush();
                     System.out.println("UPSMON : Outlet Group1 OFF");
                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("Outlet_Group1_OFF"));

                     try {
                        temp2 = "0";
                        fo = new FileOutputStream(f2, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     } catch (Exception var11) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + var11);
                     }

                     a = 0;
                     sleep(1000L);
                  } else if (a == 8) {
                     var4 = new byte[]{79, 48, 50, 79, 78, 13};
                     var1.write(var4);
                     var1.flush();
                     System.out.println("UPSMON : Outlet Group2 ON");
                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("Outlet_Group2_ON"));

                     try {
                        temp2 = "0";
                        fo = new FileOutputStream(f3, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     } catch (Exception var10) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + var10);
                     }

                     a = 0;
                     sleep(1000L);
                  } else if (a == 9) {
                     var4 = new byte[]{79, 48, 50, 79, 70, 70, 13};
                     var1.write(var4);
                     var1.flush();
                     System.out.println("UPSMON : Outlet Group2 OFF");
                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("Outlet_Group2_OFF"));

                     try {
                        temp2 = "0";
                        fo = new FileOutputStream(f3, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     } catch (Exception var9) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + var9);
                     }

                     a = 0;
                     sleep(1000L);
                  } else if (a == 10) {
                     System.out.println("UPSMON : Beeper OFF");
                     if (ConCOM2Get.b0.equals("1")) {
                        var4 = new byte[]{81, 13};
                        var1.write(var4);
                        var1.flush();
                     }

                     try {
                        temp2 = "0";
                        fo = new FileOutputStream(f5, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     } catch (Exception var8) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + var8);
                     }

                     a = 0;
                     sleep(1000L);
                  } else if (a == 11) {
                     System.out.println("UPSMON : Beeper ON");
                     if (ConCOM2Get.b0.equals("0")) {
                        var4 = new byte[]{81, 13};
                        var1.write(var4);
                        var1.flush();
                     }

                     try {
                        temp2 = "0";
                        fo = new FileOutputStream(f5, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     } catch (Exception var7) {
                        System.err.println("Exception : Class ConSNMPSet : run() : e6 : " + var7);
                     }

                     a = 0;
                     sleep(1000L);
                  }
               }
            }
         }
      } catch (Exception var15) {
         System.err.println("Exception : Class ConCOM2Set : run() : e1 :" + var15);
      }

      try {
         this.out.close();
      } catch (IOException var6) {
         System.err.println("Exception : Class ConCOM2Set : run() : e2 :" + var6);
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

         if (f5.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f5)));

            while((temp2 = br.readLine()) != null) {
               if (temp2.equals("2")) {
                  a = 10;
               } else if (temp2.equals("1")) {
                  a = 11;
               }
            }

            br.close();
            f5.delete();
         }

         if (f2.exists()) {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(f2)));

            while(true) {
               while((temp2 = br.readLine()) != null) {
                  if (temp2.equals("0")) {
                     br = new BufferedReader(new InputStreamReader(new FileInputStream(f3)));

                     while((temp2 = br.readLine()) != null) {
                        if (temp2.equals("1")) {
                           a = 8;
                        } else if (temp2.equals("2")) {
                           a = 9;
                        }
                     }
                  } else if (temp2.equals("1")) {
                     a = 6;
                  } else if (temp2.equals("2")) {
                     a = 7;
                  }
               }

               br.close();
               f2.delete();
               break;
            }
         }

         if (f_exit.exists()) {
            temp2 = "1";
            fo = new FileOutputStream(f5, false);
            fo.write(temp2.getBytes(), 0, temp2.length());
            fo.close();
            System.exit(0);
         }
      } catch (Exception var1) {
         System.err.println("Exception Class ConCOM2Set : read() : ea : " + var1);
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
      ff = null;
      controloff = "";
      temp2 = "";
      fo = null;
   }
}
