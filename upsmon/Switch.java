package defpackage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/* JADX INFO: compiled from: ConCOM1.java */
/* JADX INFO: loaded from: Switch.class */
class Switch extends Thread {
    ConCOM1 comApp;

    public Switch(ConCOM1 conCOM1) {
        this.comApp = conCOM1;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            sleep(5000L);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(Label.fileget)));
            String line = bufferedReader.readLine();
            bufferedReader.close();
            if (line == null) {
                ConCOM1Set.roopINT = -1;
                this.comApp.closeStream();
                ConCOM1 conCOM1 = this.comApp;
                ConCOM1.com.close();
                this.comApp.switcher();
            }
        } catch (InterruptedException e) {
            System.err.println("Exception : Class StartCOM1 : Class Switcher1 : run() : e1 :" + e);
        } catch (Exception e2) {
            System.err.println("Exception : Class StartCOM1 : Class Switcher1 : run() : e2 :" + e2);
        }
    }
}
