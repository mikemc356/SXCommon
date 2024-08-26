package com.systemwerx.common.license;

import java.io.File;
import java.util.ArrayList;
import com.systemwerx.common.process.RunWindowsElevatedJVM;
import javafx.stage.Stage;

public class LicenseManager {
	LicenseManagerThread lmt = new LicenseManagerThread();
	LicenseManagerNotification notify = null;
	String licensePath = "license.dat";
	String launchJar = null;
	String productName = "****";
	String productID = "";
	static LicenseManager licenseManagerStatic;
	LicenseManagerUI licenseManagerUI;
	Stage stage;

	// Command line prodID, iLicensePath, iProductName

	public static void main(String args[])
	{
		LicenseManagerUI licenseManagerUI = new LicenseManagerUI();
		licenseManagerUI.initApp(args[0],args[2],args[1],null,true);
	}
	
    public void LicenseManager() {
		licenseManagerStatic = this;
	}

	public boolean applyLicenseKey()
	{
		long mod = -1;
		if (System.getProperty("user.dir").contains("Program Files") )
		//if ( true )
		{
		   RunWindowsElevatedJVM elevate = new RunWindowsElevatedJVM();
	  	   if ( elevate.isElevationRequired())
		   //if ( true )
		   {
	  		  ArrayList parms = new ArrayList();
	  		  parms.add(productID);
	  		  parms.add("\""+licensePath+"\"");
	  		  parms.add(productName);
		      File lic = new File(licensePath);
		      if ( lic.exists() ) mod = lic.lastModified();
		      elevate.setElevationProgramDirectory(System.getProperty("user.dir"));
		      elevate.setJarFile(launchJar);
		      elevate.setCurrentWorkingDirectory(System.getProperty("user.dir"));
		      elevate.runClass("com.systemwerx.common.license.LicenseManager",parms);
		      for ( int count = 0; count <10000; count++)
		      {
		    	  //System.out.println("Loop");
				  try
				  {
		    	     Thread.currentThread().sleep(1000);
				  }
				  catch ( Exception ex)
				  {
					  
				  }
				  
				 if ( mod == -1 && lic.exists() ) break;				 
		         if ( lic.exists() && (lic.lastModified() != mod ) )
		         {
					  try
					  {
			    	     Thread.currentThread().sleep(1500);
					  }
					  catch ( Exception ex)
					  {
						  
					  }
		    	   break;
		         }
		      }
		   }
		   else
		   {
			  lmt = new LicenseManagerThread();
			  //lmt.setNotify(Thread.currentThread());
			  lmt.setProductID(productID);
			  lmt.setLicensePath(licensePath);
			  lmt.setProductName(productName);
			  lmt.start();
			  try
			  {
				  synchronized(Thread.currentThread())
				  {
					  Thread.currentThread().wait();
				  }
			  }
			  catch ( Exception ex)
			  {
			  }
		   }
		}
		else
		{
			licenseManagerUI = new LicenseManagerUI();
			licenseManagerUI.initApp(productID,productName,licensePath,null,true);
			synchronized (this) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}

	
			/*lmt = new LicenseManagerThread();
			lmt.setNotify(Thread.currentThread());
			lmt.setProductID(productID);
		    lmt.setLicensePath(licensePath);
			lmt.setProductName(productName);
			lmt.start();
			try
			{
			  synchronized(Thread.currentThread())
			  {
				Thread.currentThread().wait();
			  }
			}
			catch ( Exception ex)
			{
			}*/
		}
		
		return true;
	}

	public boolean run()
	{
		if (System.getProperty("user.dir").contains("Program Files") )
		//if ( true )
		{
		   RunWindowsElevatedJVM elevate = new RunWindowsElevatedJVM();
	  	   if ( elevate.isElevationRequired())
		   //if ( true )
		   {
	  		  ArrayList parms = new ArrayList();
	  		  parms.add(productID);
	  		  parms.add(licensePath);
	  		  parms.add(productName);
		      File lic = new File(licensePath);
		      elevate.setElevationProgramDirectory(System.getProperty("user.dir"));
		      elevate.setJarFile(launchJar);
		      elevate.setCurrentWorkingDirectory(System.getProperty("user.dir"));
		      elevate.runClass("com.systemwerx.common.license.LicenseManager",parms);
		   }
		   else
		   {
			  lmt = new LicenseManagerThread();
			  //lmt.setNotify();
			  lmt.setProductID(productID);
			  lmt.setLicensePath(licensePath);
			  lmt.setProductName(productName);
			  lmt.start();
			  try
			  {
				  synchronized(Thread.currentThread())
				  {
					  //Thread.currentThread().wait();
				  }
			  }
			  catch ( Exception ex)
			  {
			  }
		   }
		}
		else
		{
			lmt = new LicenseManagerThread();
			//lmt.setNotify(Thread.currentThread());
			lmt.setProductID(productID);
		    lmt.setLicensePath(licensePath);
			lmt.setProductName(productName);
			lmt.start();
			try
			{
			  synchronized(Thread.currentThread())
			  {
				    //Thread.currentThread().wait();
			  }
			}
			catch ( Exception ex)
			{
			}
		}
		return true;
	}

	public boolean runInCurrentApplication()
	{
		this.stage = stage;
		LicenseManagerUI licenseManagerUI = new LicenseManagerUI();
  	    licenseManagerUI.initInCurrentApplication(productID,productName,licensePath,notify,true);
		return true;
	}

	//public boolean 
	public LicenseManagerNotification getNotify() {
		return notify;
	}

	public void setNotify(LicenseManagerNotification notify) {
		this.notify = notify;
	}

	public String getLicensePath() {
		return licensePath;
	}

	public void setLicensePath(String licensePath) {
		this.licensePath = licensePath;
	}

	public String getLaunchJar() {
		return launchJar;
	}

	public void setLaunchJar(String launchJar) {
		this.launchJar = launchJar;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String ID) {
		this.productID = ID;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public static LicenseManager getLicenseManagerStatic() {
		return licenseManagerStatic;
	}

	public static void setLicenseManagerStatic(LicenseManager licenseManagerStatic) {
		LicenseManager.licenseManagerStatic = licenseManagerStatic;
	}
}
