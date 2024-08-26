package com.systemwerx.common.process;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ProcessLauncher 
{
	String ProgramPath;
	String CWD;
	OutputStream OutputStrm = null;
	OutputStream OutputErrorStream = null;
	int Timeout = 1000;
	boolean returnOnProcessExit = false;
	Map<String, String> inputEnvironment;
	
    public  int run(String Name, List Parameters) throws Exception {
    	 
        //ProgramPath = "c:\\work\\projects\\sxcdcvt\\debug";
        //CWD = "c:\\work\\projects\\sxcdcvt";
        int rc = -1; 
        int Interval = Timeout / 10;
        ProcessBuilder builder;
        String Key = null;
        Map<String, String> environment; 
        Thread timer = Thread.currentThread();
        
       /* Iterator it = environment.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }*/ 
        
        String path = System.getenv("PATH");
        String osName= System.getProperty("os.name");
        
        if ( Parameters == null )
            builder = new ProcessBuilder(Name);
        else
        {
        	// Build Array for builder with program name
        	ArrayList TempArray = new ArrayList(Parameters);
        	TempArray.add(0, Name);
            builder = new ProcessBuilder(TempArray);
        }
        
        environment = builder.environment();
        
        Iterator it = environment.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
            Key = (String) pairs.getKey();
            
            if ( Key.equalsIgnoreCase("path"))
            {
                if ( osName.startsWith("Windows"))
                {
                    pairs.setValue(path+";"+ProgramPath+";");
                    break;
                }
                else
                {
                	pairs.setValue(path+":"+ProgramPath+":");
                	break;
                }
            }
        } 

        if ( inputEnvironment != null ) environment.putAll(inputEnvironment);
        
        if ( CWD != null ) builder.directory(new File(CWD));
        Process javap = builder.start();
        writeProcessOutput(javap);
        
        
        try
        {
           rc = javap.exitValue();
        }
        catch (IllegalThreadStateException ex1)
        {
        	System.out.println("IllegalThreadStateException");
        }
        
        // Not ended - wait if required 
        
        if (returnOnProcessExit)
        {
            try
            {
              rc = javap.waitFor();
            }
            catch (IllegalThreadStateException ex1)
            {
         	   //timer.sleep(Interval);
            	System.out.println("IllegalThreadStateException");
            }
         }
        
        if ( rc == -1 ) System.out.println("Task not ended ");
        //System.out.println("Task call ended "+rc);
        returnOnProcessExit = false;
        return rc;
    }
    
    public  int runWaitForExit(String Name, List Parameters) throws Exception {
    	returnOnProcessExit = true;
    	return run(Name,Parameters);
    }
    
    void writeProcessOutput(Process process) throws Exception{
    	InputStream is = process.getInputStream();
    	InputStream es = process.getErrorStream();
    	ByteBuffer Ibuf;
    	ByteBuffer Ebuf;
        int read = 0;
    	byte[] bytes = new byte[1024];
    	int InumRead, EnumRead;
    	String Istrout = "";
    	String Estrout = "";
    	char c;
    	
    	Ibuf = ByteBuffer.allocateDirect(1);
		Ebuf = ByteBuffer.allocateDirect(1);
    	
		ReadableByteChannel InpEchannel = Channels.newChannel(es);
		ReadableByteChannel Inpchannel = Channels.newChannel(is);
   
		try {
			while (true) {

				/* Check if we have any stdout data */
				
				//Ibuf.clear();
				//System.out.println("testing stream");
				
				try {
					if ( es.available() == 0 && is.available() == 0 )
					{
				      process.exitValue();
				      is.close();
				      es.close();
				      return;
					}
				    
				}
				catch (Exception ex)
				{
					
				}
				
				InumRead = is.available();

				if (InumRead > 0) {

					//Ibuf.rewind();

					while (InumRead > 0) {

						//System.out.println("Before read of stdout ");
						InumRead = Inpchannel.read(Ibuf);
						
						byte b = Ibuf.get(0);
						
						if ( OutputStrm != null )  OutputStrm.write(b);
						else
						{
							c = (char ) b;
							System.out.print(c);
						}
						//char c = (char) b;
						//Istrout = Istrout + c;
						Ibuf.clear();
						InumRead = is.available();
					}
				}

				/* Check if we have any stdout data */
		
				Ebuf.clear();
				// System.out.println("**** test Error input stream, result = "
				// + EnumRead);
				// System.out.println(EnumRead);
				EnumRead = es.available();
				if (EnumRead > 0) {

					Ebuf.rewind();
					int count = 0;

					while (EnumRead > 0) {
						EnumRead = InpEchannel.read(Ebuf);
						//System.out.println("**** test Error input stream, result = " + EnumRead);
						
						byte b = Ebuf.get(0);
						c = (char) b;
						
						if ( OutputErrorStream != null )  OutputErrorStream.write(b);
						else
						    System.err.print(c);
						
						Ebuf.clear();
						EnumRead = es.available();
					}
				}
			}
		}
		catch ( Exception ex)
		{
			System.out.println("Exception "+ex.getClass().getName()+" "+ex.getMessage());
			ex.printStackTrace();
		}
    	
    }

	public String getProgramPath() {
		return ProgramPath;
	}

	public void setProgramPath(String programPath) {
		ProgramPath = programPath;
	}

	public String getCWD() {
		return CWD;
	}

	public void setCWD(String cWD) {
		CWD = cWD;
	}

	public OutputStream getOutputStream() {
		return OutputStrm;
	}

	public void setOutputStream(OutputStream outputStream) {
		OutputStrm = outputStream;
	}

	public int getTimeout() {
		return Timeout;
	}

	public void setTimeout(int timeout) {
		Timeout = timeout;
	}

	public OutputStream getOutputErrorStream() {
		return OutputErrorStream;
	}

	public void setOutputErrorStream(OutputStream outputErrorStream) {
		OutputErrorStream = outputErrorStream;
	}

	public Map<String, String> getInputEnvironment() {
		return inputEnvironment;
	}

	public void setInputEnvironment(Map<String, String> inputEnvironment) {
		this.inputEnvironment = inputEnvironment;
	}
    
}
