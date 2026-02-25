package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

/* JADX INFO: loaded from: Event.class */
public class Event {
    static String separator = System.getProperty("file.separator");
    static String temp = "";
    static int a = 0;

    public static void main(String[] strArr) {
        Calendar calendar = Calendar.getInstance();
        calendar.getTime();
        File file = new File("EXT" + separator + "EVENT" + separator + String.valueOf(calendar.get(2) + 1) + ".txt");
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
                        System.out.println("             Time\t\t    Event");
                        a++;
                    }
                    temp = temp.replace('_', ' ');
                    System.out.println(temp);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
            System.out.println();
        }
    }
}
