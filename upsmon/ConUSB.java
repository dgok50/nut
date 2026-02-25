package defpackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;

/* JADX INFO: loaded from: ConUSB.class */
public class ConUSB {
    public static UsbConfiguration config = null;
    public static UsbInterface theInterface = null;
    public static Thread USBAPP = null;
    public static String temp = null;
    public static FileOutputStream fo = null;

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.usb.UsbException */
    public ConUSB() throws UsbException {
        try {
            conusb();
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.usb.UsbException */
    public void conusb() throws UsbException, InterruptedException, IOException {
        UsbDevice usbDeviceFindUPS = findUPS();
        if (usbDeviceFindUPS == null) {
            System.out.println("UPSMON : There is no any UPS USBDevice attached!");
            System.out.println("UPSMON : Exit");
            return;
        }
        config = usbDeviceFindUPS.getActiveUsbConfiguration();
        theInterface = config.getUsbInterface((byte) 0);
        theInterface.claim(new UsbInterfacePolicy() { // from class: ConUSB.1
            public boolean forceClaim(UsbInterface usbInterface) {
                return true;
            }
        });
        for (UsbEndpoint usbEndpoint : theInterface.getUsbEndpoints()) {
            if (usbEndpoint.getDirection() != 0) {
                if (Connect.model == 3) {
                    if (new File(Label.fileupsmodel).exists()) {
                        USBAPP = new ConUSB4(usbEndpoint, usbDeviceFindUPS, this);
                        USBAPP.setPriority(10);
                    } else {
                        USBAPP = new ConUSB3(usbEndpoint, usbDeviceFindUPS, this);
                        USBAPP.setPriority(10);
                    }
                }
                USBAPP.start();
            }
        }
    }

    private static UsbDevice findUPS() throws UsbException, InterruptedException, IOException {
        return listDevices(UsbHostManager.getUsbServices().getRootUsbHub());
    }

    private static UsbDevice listDevices(UsbHub usbHub) throws UsbException, UnsupportedEncodingException {
        UsbDevice usbDeviceListDevices;
        for (UsbHub usbHub2 : usbHub.getAttachedUsbDevices()) {
            UsbDeviceDescriptor usbDeviceDescriptor = usbHub2.getUsbDeviceDescriptor();
            if (usbDeviceDescriptor.idVendor() == 3487) {
                short sIdProduct = usbDeviceDescriptor.idProduct();
                if (sIdProduct == 162) {
                    try {
                        temp = "0xa2";
                        fo = new FileOutputStream(Label.fileUsbId, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        temp = "UPS-IMP/IMD";
                        fo = new FileOutputStream(Label.fileModel, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                    Connect.model = 1;
                    return usbHub2;
                }
                if (sIdProduct == 164) {
                    try {
                        temp = "0xa4";
                        fo = new FileOutputStream(Label.fileUsbId, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        temp = "UPS-WOW";
                        fo = new FileOutputStream(Label.fileModel, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                    } catch (Exception e2) {
                        System.err.println(e2);
                    }
                    Connect.model = 1;
                    return usbHub2;
                }
                if (sIdProduct == 166) {
                    try {
                        temp = "0xa6";
                        fo = new FileOutputStream(Label.fileUsbId, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        temp = "UPS-BNT";
                        fo = new FileOutputStream(Label.fileModel, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                    } catch (Exception e3) {
                        System.err.println(e3);
                    }
                    Connect.model = 1;
                    return usbHub2;
                }
                if (sIdProduct == 167) {
                    try {
                        temp = "0xa7";
                        fo = new FileOutputStream(Label.fileUsbId, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                        temp = "UPS-King";
                        fo = new FileOutputStream(Label.fileModel, false);
                        fo.write(temp.getBytes(), 0, temp.length());
                    } catch (Exception e4) {
                        System.err.println(e4);
                    }
                    Connect.model = 1;
                    return usbHub2;
                }
                if (sIdProduct == 163) {
                    Connect.model = 2;
                    return usbHub2;
                }
                if (sIdProduct == 4) {
                    Connect.model = 3;
                    return usbHub2;
                }
            } else if (usbHub2.isUsbHub() && (usbDeviceListDevices = listDevices(usbHub2)) != null) {
                return usbDeviceListDevices;
            }
        }
        return null;
    }
}
