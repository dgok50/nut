package defpackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/* JADX INFO: loaded from: Record.class */
public class Record {
    static String enableST;
    static String smtpST;
    static String userST;
    static String sendermailST;
    static String receivemailST;
    static String passwdST;
    static String portST;
    static String sslST;
    static String eventST;
    static String timeST;
    static String temp = "";
    static BufferedReader br = null;
    static FileOutputStream fo = null;
    static File f = new File(Label.fileConfigMail);

    public static void set(String str) {
        eventST = str;
        Calendar calendar = Calendar.getInstance();
        write(str, String.valueOf(calendar.get(2) + 1), String.valueOf(calendar.getTime()).replace(' ', '_'));
    }

    public static void set2(String str, String str2) {
        eventST = str;
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(12) + Integer.parseInt(str2);
        calendar.set(calendar.get(1), calendar.get(2), calendar.get(5), calendar.get(11), i);
        write(str, String.valueOf(calendar.get(2) + 1), String.valueOf(calendar.getTime()).replace(' ', '_'));
        try {
            int i2 = calendar.get(11);
            if (i2 < 10) {
                temp = "0";
            }
            temp += i2 + ":";
            if (i < 10) {
                temp += "0";
            }
            temp += i + ":";
            int i3 = calendar.get(13);
            if (i3 < 10) {
                temp += "0";
            }
            temp += i3 + " " + calendar.get(1) + "/";
            int i4 = calendar.get(2) + 1;
            if (i4 < 10) {
                temp += "0";
            }
            temp += i4 + "/";
            int i5 = calendar.get(5);
            if (i5 < 10) {
                temp += "0";
            }
            temp += i5;
            fo = new FileOutputStream(Label.fileBatStop, false);
            fo.write(temp.getBytes(), 0, temp.length());
            fo.close();
        } catch (Exception e) {
            System.err.println("Exception : Class Record : set2() : " + e);
        }
    }

    public static void write(String str, String str2, String str3) {
        String strReplace;
        boolean z = false;
        if (str.equals("mailtest")) {
            z = true;
        } else {
            try {
                timeST = str3;
                byte[] bytes = (str3 + "\t" + str + "\n").getBytes();
                byte[] bytes2 = str.getBytes();
                fo = new FileOutputStream("EXT" + UPSMON.separaST + "EVENT" + UPSMON.separaST + str2 + ".txt", true);
                fo.write(bytes, 0, bytes.length);
                fo = new FileOutputStream("EXT" + UPSMON.separaST + "EVENT" + UPSMON.separaST + "Message.txt", false);
                fo.write(bytes2, 0, bytes2.length);
                fo.close();
            } catch (Exception e) {
                System.err.println("Exception Record: write(): eee" + e);
            }
        }
        try {
            if (f.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                int i = 0;
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    if (i == 0) {
                        enableST = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (i == 1) {
                        smtpST = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (i == 2) {
                        portST = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (i == 3) {
                        userST = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (i == 4) {
                        passwdST = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (i == 5) {
                        sslST = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (i == 6) {
                        sendermailST = line.substring(line.indexOf("=") + 1, line.length());
                    } else if (i == 7) {
                        receivemailST = line.substring(line.indexOf("=") + 1, line.length());
                    }
                    i++;
                }
                if (enableST.equals("true") && !eventST.startsWith("UPSMON")) {
                    try {
                        Properties properties = new Properties();
                        properties.put("mail.smtp.host", smtpST);
                        properties.put("mail.smtp.socketFactory.port", portST);
                        if (sslST.equals("true")) {
                        }
                        properties.put("mail.smtp.auth", "true");
                        properties.put("mail.smtp.port", portST);
                        properties.put("mail.smtp.starttls.enable", "true");
                        properties.put("mail.smtp.localhost", "linux");
                        MimeMessage mimeMessage = new MimeMessage(Session.getInstance(properties, new Authenticator() { // from class: Record.1
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(Record.userST, Record.passwdST);
                            }
                        }));
                        mimeMessage.setFrom(new InternetAddress(sendermailST));
                        mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receivemailST));
                        if (z) {
                            mimeMessage.setSubject("UPSMON : Email Test");
                            System.out.println("UPSMON : Email Test");
                            strReplace = "If you received this test mail, it means that your UPSMON mail settings are correct";
                        } else {
                            mimeMessage.setSubject("UPSMON Event Notification");
                            strReplace = ("Event : " + eventST + "\n\nTime  : " + timeST).replace('_', ' ');
                        }
                        mimeMessage.setContent(strReplace, "text/plain");
                        Transport.send(mimeMessage);
                    } catch (Exception e2) {
                    }
                }
            }
        } catch (Exception e3) {
            System.err.println("Exception : Class Record : write() : e : " + e3);
        }
    }
}
