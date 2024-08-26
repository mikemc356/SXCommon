package com.systemwerx.common.license;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


public class LicenseManagerCmd
{
	static boolean traceActive = false;
	static com.systemwerx.common.logging.Trace tc;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
       	
        if ( args[0].equalsIgnoreCase("-apply"))
        {
            if ( args.length != 2)
            {
    			displayErrorMessage("License command is invalid is invalid - too many parameters");
    			return;
            }
            
        	if ( storeLicense(args[1]))
				displayInfoMessage("License apply successful");
        	else
    			displayErrorMessage("License apply failed");
        }
        else if ( args[0].equalsIgnoreCase("-displayhost"))
        {
        	try
        	{
        	  displayInfoMessage("Hostname : "+java.net.InetAddress.getLocalHost().getHostName());
        	}
        	catch ( Exception ex)
        	{
    		  displayErrorMessage("Exception occurred "+ex.getClass().getName()+" "+ex.getMessage());
    		  ex.printStackTrace();
        		
        	}
        }
        else
        {
       	  displayErrorMessage("Invalid command");
        }
	}

	static boolean storeLicense(String Key)
	{
		try
		{
			String licenseFile;
			if ((licenseFile = System.getProperty("sxlicense")) == null)
			{
				licenseFile = "license.dat";
			}
			else
			{
				if (traceActive)
					tc.traceMessage("License file selected : " + licenseFile);
			}
			license lic = new license("05");
			File out = new File(licenseFile);
			FileOutputStream fos = new FileOutputStream(licenseFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			if (Key.length() != 49)
			{
				displayErrorMessage("License is invalid - length is incorrect");
				return false;
			}

			if (lic.verifyLicense(Key.substring(0, 5), Key.substring(5, 9),Key.substring(9)))
			{
				displayInfoMessage("License is valid - expires "
						+ lic.getExpireDateString(Key.substring(0, 5)));
				oos.writeUTF(Key);
				oos.close();
				return true;
			}
			else
			{
				displayErrorMessage("License is not valid");
				return false;
			}
		}
		catch (Exception ex)
		{
			System.out.println("Exception detected" + ex.getMessage());
			System.out.println("Exception detected" + ex.getClass().getName());
			ex.printStackTrace();
		}
		return true;
	}

	static private void displayErrorMessage(String string)
	{
		System.out.println("ERROR " + string);
	}

	static private void displayInfoMessage(String string)
	{
		System.out.println(string);
	}

}
