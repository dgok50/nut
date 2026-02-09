/* Decompiler 604ms, total 1069ms, lines 735 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.StringTokenizer;

class ConCOM2Get extends Thread {
   static InputStream in;
   static boolean writeupstypeBO = true;
   static StringTokenizer tokens = null;
   static String[] text = new String[8];
   static String b7;
   static String b6;
   static String b5;
   static String b4;
   static String b3;
   static String b2;
   static String b1;
   static String b0 = null;
   static String inputV = "0";
   static String inputF = "0";
   static String outputV = "0";
   static String load = "0";
   static String batterycapacity = "0";
   static String temp2;
   static String temp3 = "";
   static String filelog = "";
   static String c1 = "";
   static String data1 = "";
   static boolean on_lineBO = false;
   static boolean off_lineBO = false;
   static boolean selftestflag = true;
   static boolean testflag = false;
   static boolean upsfailflag = true;
   static boolean batterylowflag = true;
   static boolean batfailBO = false;
   static boolean powerfailflag = true;
   static boolean powerrestoreflag = false;
   static boolean readBO = false;
   static boolean bypassflag = false;
   static boolean avrflag = false;
   static boolean battery_power = false;
   static boolean upsoffBO = false;
   static boolean battery_power_stop = false;
   static double batdata;
   static double batt = 0.0D;
   static int timeINT = 0;
   static FileOutputStream fo = null;
   static BufferedReader br = null;
   static Calendar cal1;
   static Calendar cal2;
   static Calendar cal3 = null;
   static int b = 0;
   static int aa = 0;
   static int bb = 0;
   static int cc = 0;
   static int bytesRead = 0;
   static int tempINT = 0;
   static byte[] data = new byte[]{80, 67, 77, 82, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 69, 78, 68};
   static byte[] buffer = new byte[64];

   public ConCOM2Get(InputStream var1) {
      in = var1;

      for(int var2 = 0; var2 < 8; ++var2) {
         text[var2] = "";
      }

   }

   public void run() {
      try {
         BufferedInputStream var1 = new BufferedInputStream(in);
         boolean var2 = false;
         StringBuffer var3 = new StringBuffer(47);

         do {
            bytesRead = var1.read(buffer);
            int var4;
            if (ConCOM2Set.IBO) {
               for(var4 = 0; var4 < bytesRead; ++var4) {
                  if (buffer[var4] == 35) {
                     temp2 = "";
                  }

                  temp2 = temp2 + (char)buffer[var4];
               }

               if (temp2.length() == 38) {
                  ConCOM2Set.IBO = false;
                  fo = new FileOutputStream(Label.fileCompany, false);
                  fo.write(temp2.getBytes(), 1, 15);
                  fo.close();
                  fo = new FileOutputStream(Label.fileModel, false);
                  fo.write(temp2.getBytes(), 17, 10);
                  Label.UpsModel = temp2.substring(17, 27);
                  fo.close();
                  fo = new FileOutputStream(Label.fileFirmware, false);
                  fo.write(temp2.getBytes(), 29, 9);
                  Label.UpsFirmware = temp2.substring(29, temp2.length());
                  fo.close();
               }
            } else if (ConCOM2Set.FBO) {
               for(var4 = 0; var4 < bytesRead; ++var4) {
                  if (buffer[var4] == 35) {
                     temp2 = "";
                  }

                  temp2 = temp2 + (char)buffer[var4];
               }

               if (temp2.length() > 20) {
                  temp3 = temp2.substring(1, 6);
                  fo = new FileOutputStream(Label.fileConfigOutputVolt, false);
                  fo.write(temp3.getBytes(), 0, temp3.length());
                  Label.ConfigOutputVoltage = Double.parseDouble(temp3);
                  fo.close();
                  temp3 = temp2.substring(11, 16);
                  fo = new FileOutputStream(Label.fileConfigBatVolt, false);
                  fo.write(temp3.getBytes(), 0, temp3.length());
                  fo.close();
                  temp3 = temp2.substring(17, 21);
                  fo = new FileOutputStream(Label.fileConfigFreq, false);
                  fo.write(temp3.getBytes(), 0, temp3.length());
                  fo.close();
                  ConCOM2Set.roopINT = 5;
                  ConCOM2Set.FBO = false;
               }
            } else if (ConCOM2Set.rtBO) {
               for(var4 = 0; var4 < bytesRead; ++var4) {
                  if (buffer[var4] == 35) {
                     temp2 = "";
                  }

                  temp2 = temp2 + (char)buffer[var4];
                  if (temp2.length() == 4) {
                     ConCOM2Set.rtBO = false;
                     if (temp2.indexOf("#") != -1) {
                        temp2 = temp2.substring(1, 4);
                        Label.Bat_backup_time = Integer.parseInt(temp2);
                        temp2 = String.valueOf(Label.Bat_backup_time);
                        data[9] = (byte)(Label.Bat_backup_time * 60 / 256);
                        data[10] = (byte)(Label.Bat_backup_time * 60 % 256);
                        fo = new FileOutputStream(Label.fileRt, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     }
                  }
               }
            } else if (ConCOM2Set.YopBO) {
               for(var4 = 0; var4 < bytesRead; ++var4) {
                  if (buffer[var4] == 42) {
                     temp2 = "";
                  }

                  temp2 = temp2 + (char)buffer[var4];
                  if (temp2.length() == 6) {
                     ConCOM2Set.YopBO = false;
                     if (temp2.indexOf("*") != -1) {
                        temp2 = temp2.substring(1, temp2.length());
                        tempINT = Integer.parseInt(temp2);
                        temp2 = String.valueOf(tempINT);
                        fo = new FileOutputStream(Label.fileOutputPower, false);
                        fo.write(temp2.getBytes(), 0, temp2.length());
                        fo.close();
                     }
                  }
               }
            } else {
               for(var4 = 0; var4 < bytesRead; ++var4) {
                  if (!this.checkinput(buffer[var4])) {
                     buffer[var4] = 48;
                  }
               }

               try {
                  var4 = 0;

                  int var5;
                  for(var5 = 0; var5 < bytesRead; ++var5) {
                     if (buffer[var5] == 40) {
                        var4 = var5;
                        var2 = false;
                        bytesRead -= var5;
                        break;
                     }

                     var2 = true;
                  }

                  try {
                     fo = new FileOutputStream(Label.fileget, var2);
                     fo.write(buffer, var4, bytesRead);
                     fo.close();
                  } catch (Exception var6) {
                     System.err.println("Exception : Class ConCOM2Get : e4 : " + var6);
                  }

                  for(var5 = 0; var5 < bytesRead; ++var5) {
                     var3.append((char)buffer[var4 + var5]);
                  }

                  this.caculate();
               } catch (Exception var7) {
                  System.err.println("Exception : Class ConCOM2Get : e3 :" + var7);
               }
            }
         } while(ConCOM2Set.roopINT >= 0);
      } catch (Exception var8) {
         System.err.println("Exception : Class ConCOM2Get : run() : e : " + var8);
      }

   }

   public boolean checkinput(byte var1) {
      boolean var2 = true;
      switch(var1) {
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 33:
      case 34:
      case 36:
      case 37:
      case 38:
      case 39:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45:
      case 47:
      default:
         var2 = false;
      case 13:
      case 32:
      case 35:
      case 40:
      case 46:
      case 48:
      case 49:
      case 50:
      case 51:
      case 52:
      case 53:
      case 54:
      case 55:
      case 56:
      case 57:
         return var2;
      }
   }

   public void caculate() {
      try {
         br = new BufferedReader(new InputStreamReader(new FileInputStream(Label.fileget)));
         String var1 = br.readLine();
         br.close();
         int var2;
         if (var1.length() < 46) {
            readBO = false;
         } else {
            tokens = new StringTokenizer(var1);
            var2 = 0;

            while(true) {
               if (tokens.hasMoreTokens()) {
                  String var3 = tokens.nextToken();
                  if (var3.indexOf("(") != -1) {
                     var2 = 0;
                  }

                  text[var2] = var3;
                  if (var2 == 7 && text[7].length() == 8) {
                     b7 = text[7].substring(0, 1);
                     b6 = text[7].substring(1, 2);
                     b5 = text[7].substring(2, 3);
                     b4 = text[7].substring(3, 4);
                     b3 = text[7].substring(4, 5);
                     b2 = text[7].substring(5, 6);
                     b1 = text[7].substring(6, 7);
                     b0 = text[7].substring(7, 8);
                  }

                  ++var2;
                  if (var2 < 8) {
                     continue;
                  }
               }

               readBO = true;
               break;
            }
         }

         if (readBO) {
            if (ConCOM2Set.roopINT <= 14) {
               if (b3.equals("0")) {
                  on_lineBO = true;
                  off_lineBO = false;
               } else if (b3.equals("1")) {
                  on_lineBO = false;
                  off_lineBO = true;
               }
            }

            Record var10000;
            if (b2.equals("1")) {
               if (selftestflag) {
                  if (ConCOM2Set.deeptestBO) {
                     System.out.println("UPSMON : Battery Deep Test");
                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("Battery_Deep_Test"));
                     cal1 = Calendar.getInstance();
                     ConCOM2Set.deeptestBO = true;
                  } else {
                     System.out.println("UPSMON : Battery Test");
                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("Battery_Test"));
                     ConCOM2Set.deeptestBO = false;
                  }

                  ++Label.Bat_test_times;
                  temp2 = String.valueOf(Label.Bat_test_times);
                  fo = new FileOutputStream(Label.fileBatTestTimes, false);
                  fo.write(temp2.getBytes(), 0, temp2.length());
                  fo.close();
                  ConCOM2Set.soundBO = true;
                  selftestflag = false;
                  testflag = true;
               }

               data[23] = 1;
            } else {
               if (!selftestflag) {
                  selftestflag = true;
                  if (ConCOM2Set.deeptestBO) {
                     cal2 = Calendar.getInstance();
                     var2 = (int)((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000L);
                     if (var2 < 60) {
                        System.out.println("UPSMON : Battery Test Time (" + var2 + " secs)");
                        var10000 = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Test_Time_(" + var2 + "_secs)"));
                     } else {
                        int var9 = var2 / 60;
                        var2 %= 60;
                        System.out.println("UPSMON : Battery Test Time (" + var9 + " mins " + var2 + " secs)");
                        var10000 = UPSMON.RecordAPP;
                        Record.set(new String("Battery_Test_Time_(" + var9 + "_mins_" + var2 + "_secs)"));
                     }
                  }

                  System.out.println("UPSMON : Battery Normal");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("Battery_Normal"));
                  Label.Battestresult = "OK";
                  temp2 = "Battery Normal";
                  fo = new FileOutputStream(Label.fileBatResult, false);
                  fo.write(temp2.getBytes(), 0, temp2.length());
                  fo.close();
               }

               data[23] = 0;
            }

            if (!b3.equals("2") && !b3.equals("3")) {
               if (batfailBO) {
                  batfailBO = false;
               }

               data[22] = 0;
            } else {
               if (data[22] == 0) {
                  System.out.println("UPSMON : Battery Failed");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("Battery_Failed"));
                  batfailBO = true;
                  Label.Battestresult = "Battery Failed";
                  temp2 = "Battery Failed";
                  fo = new FileOutputStream(Label.fileBatResult, false);
                  fo.write(temp2.getBytes(), 0, temp2.length());
                  fo.close();
               }

               data[22] = 1;
            }

            if (b4.equals("1")) {
               if (upsfailflag) {
                  System.out.println("UPSMON : UPS Failed");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_Failed"));
                  upsfailflag = false;
               }

               data[21] = 1;
            } else {
               if (!upsfailflag) {
                  upsfailflag = true;
                  System.out.println("UPSMON : Normal");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("Normal"));
               }

               data[21] = 0;
            }

            if (b6.equals("1")) {
               if (batterylowflag) {
                  System.out.println("UPSMON : UPS Battery Low");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_Battery_Low"));
                  batterylowflag = false;
                  Connect.lowbat = 1;
                  Label.PowerStatus = "Battery Low";
               }

               data[19] = 1;
            } else {
               batterylowflag = true;
               Connect.lowbat = 0;
               data[19] = 0;
            }

            if (b7.equals("1")) {
               if (b2.equals("0") && powerfailflag) {
                  powerfailflag = false;
                  powerrestoreflag = true;
                  battery_power = true;
                  System.out.println("UPSMON : Power Failure");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("Power_Failure"));
                  ConCOM2Set.soundBO = true;
                  ConCOM2Set.green_modeBO = true;
                  var2 = Integer.parseInt(UPSMON.shutdowntype);
                  Label.PowerStatus = "Battery Power";
                  ++Label.Bat_times;
                  temp2 = String.valueOf(Label.Bat_times);
                  fo = new FileOutputStream(Label.fileBatTimes, false);
                  fo.write(temp2.getBytes(), 0, temp2.length());
                  fo.close();
                  Label.ConfigIntervall = 2000;
                  if (var2 != 2) {
                     CountDOWN.usrcmd = true;
                     CountDOWN var10 = new CountDOWN();
                     var10.start();
                     System.out.println("UPSMON : Linux count down " + Label.osdelay + " seconds");
                  }
               }

               data[18] = 1;
            } else {
               powerfailflag = true;
               if (powerrestoreflag) {
                  powerrestoreflag = false;
                  battery_power_stop = true;
                  System.out.println("UPSMON : Power Restore");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("Power_Restore"));
                  CountDOWN.roopBO = false;
                  Label.PowerStatus = "On-Line";
               }

               data[18] = 0;
            }

            try {
               text[0] = text[0].substring(1, text[0].length());
               boolean var11 = text[0].startsWith("0");
               if (var11) {
                  inputV = text[0].substring(1, text[0].length());
               } else {
                  inputV = text[0];
               }

               aa = inputV.indexOf(46);
               if (aa != -1) {
                  inputV = inputV.substring(0, aa);
               }

               Label.InputVolt = Integer.parseInt(inputV);
               data[5] = (byte)(Label.InputVolt / 256);
               data[6] = (byte)(Label.InputVolt % 256);
               if (b % 2 == 1) {
                  temp2 = Label.InputVolt + " Voltage";
                  fo = new FileOutputStream(Label.fileInputVolt, false);
                  fo.write(temp2.getBytes(), 0, temp2.length());
                  fo.close();
               }

               if (Label.InputVoltMax < Label.InputVolt) {
                  Label.InputVoltMax = Label.InputVolt;
               }

               if (Label.InputVoltMini > Label.InputVolt) {
                  Label.InputVoltMini = Label.InputVolt;
               }
            } catch (Exception var7) {
               System.err.println("Exception : Class ConCOM2 : caculate : e2 : " + var7);
            }

            outputV = text[2];
            aa = outputV.indexOf(46);
            if (aa != -1) {
               outputV = outputV.substring(0, aa);
            }

            Label.OutputVolt = Integer.parseInt(outputV);
            data[7] = (byte)(Label.OutputVolt / 256);
            data[8] = (byte)(Label.OutputVolt % 256);
            double var12;
            if (b3.equals("0")) {
               if (b5.equals("1")) {
                  if (!bypassflag) {
                     bypassflag = true;
                     System.out.println("UPSMON : Bypass");
                     var10000 = UPSMON.RecordAPP;
                     Record.set(new String("Bypass"));
                     Label.PowerStatus = "Bypass";
                     data[17] = 1;
                  }
               } else if (bypassflag) {
                  bypassflag = false;
                  System.out.println("UPSMON : Normal");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("Normal"));
                  Label.PowerStatus = "On-Line";
                  data[17] = 0;
               }

               data[16] = 0;
            } else if (b3.equals("1")) {
               if (b5.equals("1")) {
                  if (!avrflag) {
                     avrflag = true;
                     var12 = Double.parseDouble(inputV);
                     double var4 = Double.parseDouble(outputV);
                     if (var12 > var4) {
                        System.out.println("UPSMON : AVR Buck");
                        var10000 = UPSMON.RecordAPP;
                        Record.set(new String("AVR_Buck"));
                        Label.PowerStatus = "AVR Buck";
                        data[17] = 3;
                     } else {
                        System.out.println("UPSMON : AVR Boost");
                        var10000 = UPSMON.RecordAPP;
                        Record.set(new String("AVR_Boost"));
                        Label.PowerStatus = "AVR Boost";
                        data[17] = 2;
                     }
                  }
               } else if (avrflag) {
                  avrflag = false;
                  System.out.println("UPSMON : Normal");
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("Normal"));
                  data[17] = 0;
               }

               data[16] = 1;
            }

            Label.Load = Integer.parseInt(text[3]);
            data[11] = (byte)Label.Load;
            inputF = text[4];
            aa = inputF.indexOf(46);
            if (aa != -1) {
               inputF = inputF.substring(0, aa);
            }

            Label.InputFreq = Integer.parseInt(inputF);
            data[13] = (byte)Label.InputFreq;
            if (b % 10 == 1) {
               temp2 = Label.InputFreq + " Hertz";
               fo = new FileOutputStream(Label.filefreq, false);
               fo.write(temp2.getBytes(), 0, temp2.length());
               fo.close();
            }

            if (text[5].indexOf(".") == -1) {
               Label.Bat_level = Integer.parseInt(text[5]);
            } else {
               Label.Bat_volt = Double.parseDouble(text[5]);
               temp2 = text[5];
               fo = new FileOutputStream(Label.fileBatVolt, false);
               fo.write(temp2.getBytes(), 0, temp2.length());
               fo.close();
            }

            data[12] = (byte)Label.Bat_level;
            temp2 = text[6];
            aa = temp2.indexOf(46);
            if (aa != -1) {
               temp2 = temp2.substring(0, aa);
            }

            Label.Temperature = Integer.parseInt(temp2);
            data[15] = (byte)Label.Temperature;
            if (b1.equals("1")) {
               var12 = Double.parseDouble(outputV);
               if (var12 <= 5.0D && !upsoffBO) {
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_OFF"));
                  System.out.println("UPSMON : UPS OFF");
                  upsoffBO = true;
               }
            } else {
               if (upsoffBO) {
                  var10000 = UPSMON.RecordAPP;
                  Record.set(new String("UPS_ON"));
                  System.out.println("UPSMON : UPS ON");
               }

               upsoffBO = false;
            }

            if (var1.length() >= 46) {
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
                     data1 = data1 + "0" + timeINT;
                  } else {
                     data1 = data1 + timeINT;
                  }

                  if (Label.Bat_level != 0) {
                     var1 = data1 + "," + inputV + "," + outputV + "," + Label.Temperature + "," + Label.Load + "," + Label.Bat_level + "\n";
                  } else {
                     var1 = data1 + "," + inputV + "," + outputV + "," + Label.Temperature + "," + Label.Load + "," + Label.Bat_volt + "\n";
                  }

                  if (b <= 3) {
                     filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + cal3.get(1) + "_" + (cal3.get(2) + 1) + "_" + cal3.get(5) + ".csv";
                  } else if (cal3.get(11) == 0 && cal3.get(12) <= 3) {
                     filelog = "EXT" + UPSMON.separaST + "LOG" + UPSMON.separaST + cal3.get(1) + "_" + (cal3.get(2) + 1) + "_" + cal3.get(5) + ".csv";
                  }

                  if (bb == 0) {
                     fo = new FileOutputStream(filelog, true);
                     fo.write(var1.getBytes(), 0, var1.length());
                     fo.close();
                     bb = 1;
                  } else {
                     bb = 0;
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
               } catch (Exception var6) {
                  System.err.println("Exception : Class caculate : ef : " + var6);
               }
            }

            ++b;
            if (b == 100) {
               b = 10;
            }
         }
      } catch (Exception var8) {
         System.err.println("Exception : Class ConCOM2 : caculate() : e1 : " + var8);
      }

   }
}
