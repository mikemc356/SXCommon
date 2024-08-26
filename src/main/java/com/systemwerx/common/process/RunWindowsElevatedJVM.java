package com.systemwerx.common.process;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * This utility runs an elevated JVM to allows access to restricted functions
 * 
 * 
 */

public class RunWindowsElevatedJVM {
	private static String OS = System.getProperty("os.name").toLowerCase();
	ProcessLauncher pl = new ProcessLauncher();
	String elevationProgramDirectory;
	String currentWorkingDirectory;
	String jarFile;
	Map<String, String> inputEnvironment;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	public boolean isElevationRequired()
	{
		if ( OS.indexOf("win") == -1 )
			return false;
		
		if ( OS.indexOf("windows 7") >= 0 )
			return true;
		
		if ( OS.indexOf("windows 8") >= 0 )
			return true;
		
		if ( OS.indexOf("windows 10") >= 0 )
			return true;
		
		return false;
	}

	public String getElevationProgramDirectory() {
		return elevationProgramDirectory;
	}

	public void setElevationProgramDirectory(String elevationProgramDirectory) {
		this.elevationProgramDirectory = elevationProgramDirectory;
	}

	public String getCurrentWorkingDirectory() {
		return currentWorkingDirectory;
	}

	public void setCurrentWorkingDirectory(String currentWorkingDirectory) {
		this.currentWorkingDirectory = currentWorkingDirectory;
	}

	public int runClass(String name, List iParms)
	{
		try
		{
			List<String> Parms = new ArrayList<String>();
			if ( currentWorkingDirectory != null )
			   pl.setCWD(currentWorkingDirectory);
			
			Parms.add("java.exe");
			if ( jarFile != null )
			{
			  Parms.add("-cp");
			  Parms.add(jarFile);
			}
			
			Parms.add(name);			
//			Parms.add("\""+currentWorkingDirectory+"\\license.dat\"");
			if (iParms != null ) Parms.addAll(iParms);
			
			if ( inputEnvironment != null ) pl.setInputEnvironment(inputEnvironment);
			pl.setOutputStream(System.out);
			int rc = pl.runWaitForExit(elevationProgramDirectory+"\\elevate.exe", Parms);

			return rc;
		}
		catch ( Exception ex)
		{
			System.out.println("Exception "+ex.getClass().getName()+" "+ex.getMessage());
			ex.printStackTrace();
			return -1;
		}
		
	}

	public String getJarFile() {
		return jarFile;
	}

	public void setJarFile(String jarFile) {
		this.jarFile = jarFile;
	}
	//boolean 
    //int rc = pl2.run("c:\\work\\projects\\sxcdcvt\\debug\\sxcdcvt.exe",null);

	public Map<String, String> getInputEnvironment() {
		return inputEnvironment;
	}

	public void setInputEnvironment(Map<String, String> inputEnvironment) {
		this.inputEnvironment = inputEnvironment;
	}
	
}
