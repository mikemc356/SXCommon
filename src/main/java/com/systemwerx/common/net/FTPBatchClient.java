package com.systemwerx.common.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.systemwerx.common.event.Log;

/*
 * Systemwerx FTP batch client
 * 
 * 
 */

public class FTPBatchClient
{
	String Message;
	String Server;
	int Port = 21;
	String User;
	String Password;
	String CmdFile;
	FTPClient ftp;
	int passive;
	boolean error;
	boolean prompt = true;
	boolean binary = false;
	FTPWriter writer = new FTPWriter(System.out);

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		FTPBatchClient fc = new FTPBatchClient();
		System.exit(fc.process(args));

	}

	public int process(String[] args)
	{
		try
		{
			for (String s : args)
			{
				System.out.println(s);
				if (s.startsWith("u:")) // Input script
				{
					User = s.substring(2);
				}

				if (s.startsWith("p:")) // Input script
				{
					Password = s.substring(2);
				}

				if (s.startsWith("s:")) // Input script
				{
					CmdFile = s.substring(2);
				}

				if (s.startsWith("h:")) // Input script
				{
					Server = s.substring(2);
				}
			}

			// End of parms - if we have a script then process it.

			if (!connect())
			{
				Log.error("Session terminated due to connect error.");
				return 12;
			}

			if (CmdFile != null)
			{
				String line = null;
				BufferedReader reader = new BufferedReader(new FileReader(
						CmdFile));
				while ((line = reader.readLine()) != null)
				{
                   command(line);
				}
			}
			return 0;
		} catch (Exception ex)
		{
			System.out.println();
			ex.printStackTrace();
			return 12;
		}
	}

	private boolean disConnect()
	{
		try
		{
			ftp.logout();
			ftp.disconnect();
			return true;
		} catch (Exception ex)
		{
			Message = ex.getMessage();
			System.out.println("Exception opening protocol: "
					+ ex.getClass().getName() + " " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}

	private boolean connect()
	{

		try
		{
			ftp = new FTPClient();
			ftp.addProtocolCommandListener(new PrintCommandListener(
					writer));
			System.setProperty("javax.net.debug", "all");
			int reply;
			String FTPCmd = null;
			ftp.setDefaultPort(Port);
			// System.out.println("FTP connect");
			ftp.connect(Server);
			// System.out.println("FTP connect complete");
			reply = ftp.getReplyCode();

			if (!FTPReply.isPositiveCompletion(reply))
			{
				ftp.disconnect();
				Log.fatal("FTP server refused connection.");
				Message = "FTP server refused connection.";
				return false;
			}

			writer.setSuppress(true);
			
			if (!ftp.login(User, Password))
			{
				Message = ftp.getReplyString();
				ftp.logout();
				ftp.disconnect();
				error = true;
				writer.setSuppress(false);
				return false;
			}
			
			writer.setSuppress(false);
			ftp.enterLocalPassiveMode();
			return true;
		} catch (IOException e)
		{
			if (ftp.isConnected())
			{
				try
				{
					ftp.disconnect();
				} catch (IOException f)
				{
					// do nothing
				}
			}
			Log.fatal("Could not connect to server.");
			e.printStackTrace();
			Message = e.getMessage();
			// ErrorMessage = "Could not connect to server.";
			// sessionBean.writeTransactionLog(ErrorMessage);
			return false;
		} catch (Exception ex)
		{
			Message = ex.getMessage();
			// sessionBean.writeTransactionLog("Exception opening protocol: "+
			// ex.getClass().getName() + " " + ex.getMessage());
			System.out.println("Exception opening protocol: "
					+ ex.getClass().getName() + " " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}

	public boolean command(String cmd)
	{

		try
		{
			String[] splitArray;
			String promptReply;
			// First lets get this into a string array
			try
			{
				splitArray = cmd.split("\\s+");
			} catch (PatternSyntaxException ex)
			{
				Message = "Parsing error : " + ex.getMessage();
				return false;
			}

			// Now get the command
			if (splitArray[0].equalsIgnoreCase("cd"))
			{
				if (splitArray.length == 1) // No directory specified
					return true;

				ftp.changeWorkingDirectory(splitArray[1]);
			}

			if (splitArray[0].equalsIgnoreCase("prompt"))
			{
				if (splitArray.length == 1) // No directory specified
					return true;

				if (splitArray[1].equalsIgnoreCase("on"))
					prompt = true;
				else if (splitArray[1].equalsIgnoreCase("off"))
					prompt = false;
				else
				{
					Log.error("Invalid prompt value - " + splitArray[1]);
				}
			}

			if (splitArray[0].equalsIgnoreCase("exit"))
			{
				if ( ftp != null ) ftp.disconnect();
			}
			
			if (splitArray[0].equalsIgnoreCase("quit"))
			{
				if ( ftp != null ) ftp.disconnect();
			}
			
			if (splitArray[0].equalsIgnoreCase("mput"))
			{
				if (splitArray.length == 1) // No directory specified
				{
					Message = "No path specified for mput";
					return false;
				}

				// Process wildcard
				FileFilter fileFilter = new WildcardFileFilter(splitArray[1]);

				// current directory
				File dir = new File(".");
				String[] strs = dir.list((FilenameFilter) fileFilter);

				for (int i = 0; i < strs.length; i++)
				{
					InputStream input;
					input = new FileInputStream(strs[i]);
					if ( !prompt)
					{
					   Log.info("Sending " + strs[i] + " to " + strs[i]);
					}
					else
					{
					  System.out.println("Sending " + strs[i] + " to " + strs[i]+" : Y/N:");
					  try {
				            InputStreamReader isr = new InputStreamReader(System.in);
				            BufferedReader br = new BufferedReader(isr);
				            promptReply = br.readLine();
				 
				        } catch (IOException e) {
				            e.printStackTrace();
				            return false;
				        }	
				        
				        if ( !promptReply.equalsIgnoreCase("y"))
				        {
				        	continue;
				        }
					}
					
					ftp.storeFile(strs[i], input);
					input.close();
				}
			}

			return true;
		} catch (Exception ex)
		{
			Message = ex.getMessage();
			// sessionBean.writeTransactionLog("Exception opening protocol: "+
			// ex.getClass().getName() + " " + ex.getMessage());
			System.out.println("Exception opening protocol: "
					+ ex.getClass().getName() + " " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}

	public String getMessage()
	{
		return Message;
	}

	public void setMessage(String message)
	{
		Message = message;
	}

	public String getServer()
	{
		return Server;
	}

	public void setServer(String server)
	{
		Server = server;
	}

	public int getPort()
	{
		return Port;
	}

	public void setPort(int port)
	{
		Port = port;
	}

	public String getUser()
	{
		return User;
	}

	public void setUser(String user)
	{
		User = user;
	}

	public String getPassword()
	{
		return Password;
	}

	public void setPassword(String password)
	{
		Password = password;
	}

	public boolean isError()
	{
		return error;
	}

	public void setError(boolean error)
	{
		this.error = error;
	}

	public boolean isPrompt()
	{
		return prompt;
	}

	public void setPrompt(boolean prompt)
	{
		this.prompt = prompt;
	}

	public boolean isBinary()
	{
		return binary;
	}

	public void setBinary(boolean binary)
	{
		this.binary = binary;
	}

}
