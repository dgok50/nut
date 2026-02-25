package defpackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/* JADX INFO: loaded from: PROServer.class */
class PROServer extends Thread {
    static int a;
    static File f0 = new File(Label.fileConfigBatTest);
    static int b = 0;
    byte[] buffer = new byte[256];
    int receive_port = 0;
    String serverIPST = "";
    String s = "";

    PROServer() {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(2601);
            DatagramPacket datagramPacket = new DatagramPacket(this.buffer, this.buffer.length);
            while (true) {
                datagramSocket.receive(datagramPacket);
                if (datagramPacket.getLength() == 8) {
                    this.s = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    if (this.s.startsWith("PCM")) {
                        b = this.s.charAt(3);
                        a = this.s.charAt(4);
                        if (b == 71) {
                            if (a == 1) {
                                this.serverIPST = String.valueOf(datagramPacket.getAddress());
                                this.serverIPST = this.serverIPST.substring(1, this.serverIPST.length());
                                InetAddress byName = InetAddress.getByName(this.serverIPST);
                                this.receive_port = datagramPacket.getPort();
                                try {
                                    if (Connect.model == 3) {
                                        datagramSocket.send(new DatagramPacket(ConUSB3.data, ConUSB3.data.length, byName, this.receive_port));
                                    } else if (Connect.model == 4) {
                                        datagramSocket.send(new DatagramPacket(ConUSB4.data, ConUSB4.data.length, byName, this.receive_port));
                                    } else if (Connect.model == 6) {
                                        datagramSocket.send(new DatagramPacket(ConCOM1Get.data, ConCOM1Get.data.length, byName, this.receive_port));
                                    } else if (Connect.model == 7) {
                                        datagramSocket.send(new DatagramPacket(ConCOM2Get.data, ConCOM2Get.data.length, byName, this.receive_port));
                                    }
                                } catch (Exception e) {
                                    System.err.println("Exceptino : Class PROServer : run() : e : " + e);
                                }
                            }
                        } else if (b == 83) {
                            try {
                                FileOutputStream fileOutputStream = new FileOutputStream(f0, false);
                                fileOutputStream.write("1".getBytes(), 0, "1".length());
                                fileOutputStream.close();
                            } catch (IOException e2) {
                                System.err.println("Exception : Class PROServer : run() : e1 : " + e2);
                            }
                        }
                    }
                }
            }
        } catch (Exception e3) {
            System.err.println("Exception : Class PROServer : run() : " + e3);
        }
    }
}
