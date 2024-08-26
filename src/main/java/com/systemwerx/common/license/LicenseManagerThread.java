package com.systemwerx.common.license;

import java.lang.reflect.Method;

import javafx.application.Application;

public class LicenseManagerThread extends Thread {
	
	LicenseManagerUI licenseManagerUI;
	LicenseManagerNotification notify = null;
	String productID = "01";
	String licensePath = "license.dat";
	String productName = "****";
	
	public void run()
	{
		try
		{
			licenseManagerUI = new LicenseManagerUI();
			licenseManagerUI.initApp(productID,productName,licensePath,notify,true);
		}
		catch ( Exception ex)
		{
		}
	}

	public LicenseManagerNotification getNotify() {
		return notify;
	}

	public void setNotify(LicenseManagerNotification notify) {
		this.notify = notify;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getLicensePath() {
		return licensePath;
	}

	public void setLicensePath(String licensePath) {
		this.licensePath = licensePath;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
