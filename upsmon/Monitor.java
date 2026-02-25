package defpackage;

/* JADX INFO: loaded from: Monitor.class */
public class Monitor {
    public static void main(String[] strArr) {
        System.out.println("UPSMON : UPSMON Start");
        Record record = UPSMON.RecordAPP;
        Record.set(new String("UPSMON_Start"));
        new Monitor_App().start();
    }
}
