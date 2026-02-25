package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.StringTokenizer;

/* JADX INFO: loaded from: Log.class */
public class Log {
    static String separator = System.getProperty("file.separator");
    static String temp = "";
    static int a = 0;

    public static void main(String[] strArr) {
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        File file = new File("EXT" + separator + "LOG" + separator + (String.valueOf(calendar.get(1)) + "_" + String.valueOf(calendar.get(2) + 1) + "_" + String.valueOf(calendar.get(5))) + ".csv");
        File file2 = new File("EXT" + separator + "CONFIG" + separator + "UPS_USB_1200.txt");
        if (file.exists()) {
            try {
                System.out.println();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                while (true) {
                    String line = bufferedReader.readLine();
                    temp = line;
                    if (line == null) {
                        break;
                    }
                    if (a == 0) {
                        StringTokenizer stringTokenizer = new StringTokenizer(temp);
                        int i = 0;
                        while (stringTokenizer.hasMoreTokens()) {
                            String strNextToken = stringTokenizer.nextToken(",");
                            if (i == 5) {
                                if (file2.exists()) {
                                    System.out.println("  Time\t        I/V\tO/V\tFreq(H)\tLoad(%) Battery Capacity(%)");
                                } else if (strNextToken.indexOf(".") == -1) {
                                    System.out.println("  Time\t        I/V\tO/V\tTemp(C)\tLoad(%) Battery Capacity(%)");
                                } else {
                                    System.out.println("  Time\t        I/V\tO/V\tTemp(C)\tLoad(%) Battery Voltage");
                                }
                            }
                            i++;
                        }
                        a++;
                    }
                    temp = temp.replace(",", "\t");
                    System.out.println(temp);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            System.out.println();
        }
    }
}
