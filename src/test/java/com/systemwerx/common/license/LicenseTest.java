package com.systemwerx.common.license;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.Key;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class LicenseTest {

    String trialLicense;
    String trialLicenseExpired;
    String nonHostLicense;
    String thisHostLicense;
    String notThisHostLicense;
    String thisHostExpiredLicense;

    @Before
    public void setUp() throws Exception {
        LicenseGen licGen = new LicenseGen();
        nonHostLicense = licGen.generateLicense("Host","49999","0018","05");
        thisHostLicense = licGen.generateLicense(java.net.InetAddress.getLocalHost().getHostName(),"020C0","0018","05");
        thisHostExpiredLicense = licGen.generateLicense(java.net.InetAddress.getLocalHost().getHostName(),"011C0","0018","05");
        notThisHostLicense = licGen.generateLicense("AA","020C0","0018","05");
        trialLicense = licGen.generateLicense("Trial","830C0","0018","05");
        trialLicenseExpired = licGen.generateLicense("Trial","811C0","0018","05");
        System.out.println("Non host - "+nonHostLicense);
        System.out.println("Host - "+thisHostLicense);
        System.out.println("Trial - "+trialLicense);
    }

    @Test
    public void testTrialLicense() throws Exception {
        license lic = new license("05");    
        assertTrue(lic.verifyLicense(trialLicense.substring(0, 5), trialLicense.substring(5, 9), trialLicense.substring(9)));    
    }

    @Test
    public void testTrialLicenseExpired() throws Exception {
        license lic = new license("05");    
        assertFalse(lic.verifyLicense(trialLicenseExpired.substring(0, 5), trialLicenseExpired.substring(5, 9), trialLicenseExpired.substring(9)));    

    }

    @Test
    public void testNonHostLicense() throws Exception {
        license lic = new license("05");    
        assertTrue(lic.verifyLicense(nonHostLicense.substring(0, 5), nonHostLicense.substring(5, 9), nonHostLicense.substring(9)));    

    }

    @Test
    public void testThisHostLicense() throws Exception {
        license lic = new license("05");    
        assertTrue(lic.getExpireDateString(thisHostLicense.substring(0, 5)).equals("01/12/32"));
        assertTrue(lic.verifyLicense(thisHostLicense.substring(0, 5), thisHostLicense.substring(5, 9), thisHostLicense.substring(9)));    
    }

    @Test
    public void testNotThisHostLicense() throws Exception {
        license lic = new license("05");    
        assertFalse(lic.verifyLicense(notThisHostLicense.substring(0, 5), notThisHostLicense.substring(5, 9), notThisHostLicense.substring(9)));    
    }

    @Test
    public void testThisHostExpiredLicense() throws Exception {
        license lic = new license("05");    
        assertFalse(lic.verifyLicense(thisHostExpiredLicense.substring(0, 5), thisHostExpiredLicense.substring(5, 9), thisHostExpiredLicense.substring(9)));    
    }

    @AfterClass
    public static void cleanup() throws Exception {
    }

}