package com.systemwerx.common.util;

import java.io.File;

public final class ProductPathUtil {

    public static String getLicensePath(String product) {

        String OS = System.getProperty("os.name").toLowerCase();
        String licensePath = System.getProperty("sxlicense");

        if (licensePath == null) {
            if (OS.indexOf("win") == -1) {
                licensePath = System.getProperty("user.home") + System.getProperty("file.separator") + "systemwerx"
                        + System.getProperty("file.separator") + product + System.getProperty("file.separator")
                        + "license.dat";
                return licensePath;
            } else {
                licensePath = System.getProperty("user.home") + System.getProperty("file.separator") + "systemwerx"
                        + System.getProperty("file.separator") + product + System.getProperty("file.separator")
                        + "license.dat";
                return licensePath;
            }

        } else {
            return licensePath;
        }
    }

    static public String getDataDirectory(String product) {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") == -1) {
            File f = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "systemwerx"
                    + System.getProperty("file.separator"));
            if (!f.exists())
                f.mkdir();
            f = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "systemwerx"
                    + System.getProperty("file.separator") + product + System.getProperty("file.separator"));
            if (!f.exists())
                f.mkdir();
            return f.getAbsolutePath();
        } else {
            File f = new File(System.getProperty("user.home") + System.getProperty("file.separator") + "systemwerx"
                    + System.getProperty("file.separator") + product
                    + System.getProperty("file.separator"));
            if (!f.exists())
                f.mkdir();

            return f.getAbsolutePath();
        }
    }
}
