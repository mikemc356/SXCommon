package com.systemwerx.common.license;

import java.io.*;
import java.*;
import java.lang.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.reflect.*;
import java.util.*;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import com.systemwerx.common.etl.TranslateBean;

public class license implements Serializable {
   public String ErrorMessage = "";
   boolean traceActive = false;
   // String EncryptionKey = "1234567812345678";
   String EncryptionKey = "3580567872241455";
   String ProductID = "";

   public license(String productID) {
      if (System.getProperty("e3debug") != null) {
         traceActive = true;
         ;
      }

      if (System.getProperty("sxdebug") != null) {
         traceActive = true;
         ;
      }
      ProductID = productID;
   }

   public boolean verifyLicenseFile(String Key) {
      String licenseFile = "license.dat";
      ;
      try {
         if ((licenseFile = System.getProperty("sxlicense")) == null) {
            licenseFile = Key;
         } else {

         }
         FileInputStream fis = new FileInputStream(licenseFile);
         ObjectInputStream ois = new ObjectInputStream(fis);

         try {
            Key = ois.readUTF();
         }

         catch (java.io.FileNotFoundException ex) {
            return false;
         }

         if (verifyLicense(Key.substring(0, 5), Key.substring(5, 9), Key.substring(9))) {
            //System.out.println("License OK");
            return true;
         } else {
            System.out.println("License invalid");
            return false;
         }

      }

      catch (java.io.FileNotFoundException ex) {
         return false;
      }

      catch (java.io.EOFException ex) {
         // System.out.println("EOF detected"+ex.getMessage());
         return false;
      } catch (Exception ex) {
         System.out.println("Exception detected" + ex.getMessage());
         System.out.println("Exception detected" + ex.getClass().getName());
         ex.printStackTrace();
         return false;
      }
   }

   public boolean verifyLicense(String Expire, String Serial, String Lic) {
      String License, host = null;
      Calendar expireDate;
      Provider sunJCE;
      ErrorMessage = "";
      try {
         if (isTrial(Expire)) {
            host = "Trial";
         } 
         else if (isNonHostLicense(Expire)) {
            host = "Host";
         }
         else {
            host = java.net.InetAddress.getLocalHost().getHostName();
         }

         TranslateBean tb = new TranslateBean();
         String CompleteKey = new String();
         Cipher c = javax.crypto.Cipher.getInstance("DES");
         DESKeySpec Key = new DESKeySpec(tb.TransHexCharToBinaryData(EncryptionKey));
         SecretKeySpec DESKey = new SecretKeySpec(Key.getKey(), "DES");
         MessageDigest md = MessageDigest.getInstance("SHA-1");
         byte[] datastream;
         host = host.toUpperCase();
         CompleteKey = Expire + host + ProductID + Serial;

         // System.out.println("CompleteKey " + CompleteKey);
         datastream = CompleteKey.getBytes();
         c.init(Cipher.ENCRYPT_MODE, DESKey);
         datastream = c.doFinal(datastream);
         md.update(datastream);
         byte[] digest = md.digest();
         License = tb.TransToHexData(digest, Array.getLength(digest));
      } catch (Exception e) {
         Class c = e.getClass();
         String s = c.getName();
         System.out.println("Exception >>>" + s + "  " + e.getMessage());
         ErrorMessage = "licenseinvalid";
         e.printStackTrace();
         return false;
      }

      if (Lic.equals(License)) {
         /* check Expiry date */
         expireDate = getExpireCalendar(Expire);
         if (isAfterExpiry(expireDate)) {
            ErrorMessage = "licenseexpired";
            return false;
         }
         return true;
      }
      ErrorMessage = "licenseinvalid";
      return false;
   }

   boolean isTrial(String Expire) {
      String val = Expire.substring(0, 1);
      if (val.equals("8") || val.equals("9") || val.equals("A") || val.equals("a") || val.equals("B") || val.equals("b")
            || val.equals("C") || val.equals("c") || val.equals("D") || val.equals("d") || val.equals("E")
            || val.equals("e") || val.equals("F") || val.equals("f"))
         return true;
      return false;
   }

   boolean isNonHostLicense(String Expire) {
      String val = Expire.substring(0, 1);
      if (val.equals("4"))
         return true;
      return false;
   }

   public String getExpireDateString(String Expire) {
      try {
         byte[] data;
         int datai;
         if (Expire.equals("09999") || Expire.equals("49999")) {
            return new String("31/12/2099");
         }

         Integer dataint = Integer.decode("0x000" + Expire);
         datai = dataint.intValue();
         TranslateBean tb = new TranslateBean();
         datai = datai & 0x0000FFFF;

         int nyear = datai & 0x0000FF00;
         nyear = nyear >> 8;
         int nmonth = datai & 0x000000F0;
         nmonth = nmonth >> 4;
         int nweek = datai & 0x0000000F;
         nweek = nweek * 7;

         String syear = Integer.toString(nyear);
         if (syear.length() == 1)
            syear = "0" + syear;

         String smonth = Integer.toString(nmonth);
         if (smonth.length() == 1)
            smonth = "0" + smonth;

         String sweek = Integer.toString(nweek);
         if (sweek.length() == 1)
            sweek = "0" + sweek;
         if (sweek.equals("00"))
            sweek = "01";
         return new String(sweek + "/" + smonth + "/" + syear);
      } catch (Exception e) {
         Class c = e.getClass();
         String s = c.getName();
         System.out.println("Exception >>>" + s + "  " + e.getMessage());
         e.printStackTrace();
         return "";
      }
   }

   Calendar getExpireCalendar(String Expire) {
      Calendar cal;
      try {
         byte[] data;
         int datai;

         if (Expire.equals("09999") || Expire.equals("49999")) {
            cal = Calendar.getInstance();
            cal.clear();
            cal.set(Calendar.YEAR, 2099);
            cal.set(Calendar.MONTH, 12);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            return cal;
         }

         Integer dataint = Integer.decode("0x000" + Expire);
         datai = dataint.intValue();
         TranslateBean tb = new TranslateBean();
         datai = datai & 0x0000FFFF;

         int nyear = datai & 0x0000FF00;
         nyear = nyear >> 8;
         int nmonth = datai & 0x000000F0;
         nmonth = nmonth >> 4;
         int nweek = datai & 0x0000000F;
         nweek = nweek * 7;
         if (nweek == 0)
            nweek = 1;

         cal = Calendar.getInstance();
         cal.clear();
         cal.set(Calendar.YEAR, nyear + 2000);
         cal.set(Calendar.MONTH, nmonth - 1);
         cal.set(Calendar.DAY_OF_MONTH, nweek);

         return cal;
      } catch (Exception e) {
         Class c = e.getClass();
         String s = c.getName();
         System.out.println("Exception >>>" + s + "  " + e.getMessage());
         e.printStackTrace();
         return null;
      }
   }

   boolean isAfterExpiry(Calendar ICal) {
      Calendar current = Calendar.getInstance();
      if (ICal.after(current))
         return false;
      return true;
   }

   boolean isServer() {
      String name;
      name = System.getProperty("os.name");
      System.out.println(System.getProperty("os.name"));

      if (name.equals("Windows NT"))
         return false;
      if (name.equals("Windows XP"))
         return false;
      if (name.equals("Windows 2003"))
         return true;
      if (name.equals("WindowsNT"))
         return false;
      if (name.equals("Solaris"))
         return true;
      if (name.equals("HP-UX"))
         return true;
      if (name.equals("AIX"))
         return true;
      if (name.equals("Linux"))
         return true;
      return true;

   }

   /*
    * public static void main(String args[]) { license pw=new license("04");
    * 
    * String Result = null; Calendar licdate; try {
    * System.out.println("myServer: local host name is " +
    * java.net.InetAddress.getLocalHost( ). getHostName()); // Result =
    * pw.generateLicense("Trial","81293","0001","1234567812345678");
    * System.out.println("License is "+Result); if (
    * pw.verifyLicense("81130","0018","00E3201E6C7E44200B49C49037F84C77B6D1D2EA") )
    * { System.out.println("License is valid"); } if ( pw.isTrial("81293") ) {
    * System.out.println("License is a trial"); }
    * 
    * System.out.println("-----Lic 1 start----- "); licdate =
    * pw.getExpireCalendar("81293"); SimpleDateFormat df = new SimpleDateFormat();
    * df.applyPattern("dd/MM/yyyy");
    * System.out.println(df.format(licdate.getTime()));
    * System.out.println(pw.getExpireDateString("81293"));
    * System.out.println("Expiry 1 is "+licdate.toString());
    * System.out.println("Expiry 1 is "+licdate.get(Calendar.DAY_OF_MONTH)+"/"+
    * licdate.get(Calendar.MONTH)+"/"+licdate.get(Calendar.YEAR));
    * System.out.println("Expiry 1 is "+licdate.get(Calendar.DAY_OF_MONTH)+"/"+
    * licdate.get(Calendar.MONTH)+"/"+licdate.get(Calendar.YEAR)); if (
    * pw.isAfterExpiry(licdate)) { System.out.println("Expired"); } else
    * System.out.println("License OK");
    * 
    * licdate = pw.getExpireCalendar("80D93");
    * System.out.println("Expiry 2 is "+licdate.toString());
    * System.out.println(df.format(licdate.getTime())); if (
    * pw.isAfterExpiry(licdate)) { System.out.println("Expired"); } else
    * System.out.println("License OK");
    * 
    * System.out.println("License is "+Result);
    * System.out.println("Expire date is "+pw.getExpireDateString("80D93")); }
    * catch ( Exception e) { Class c = e.getClass(); String s = c.getName();
    * System.out.println("Exception >>>"+s+"  "+e.getMessage());
    * e.printStackTrace(); } }
    */
}
