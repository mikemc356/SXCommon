package com.systemwerx.common.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class FTPWriter extends PrintWriter
{
	boolean suppress = false;
	
	public FTPWriter(File file) throws FileNotFoundException
	{
		super(file);
		// TODO Auto-generated constructor stub
	}

	public FTPWriter(PrintStream stream)
	{
		super(stream);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public PrintWriter append(char c)
	{
		if (!suppress) return super.append(c);
		else
			return null;
	}
	
	@Override
	public void print(String s)
	{
		if (!suppress) super.print(s);	
	}
	
	@Override
	public void print(char c)
	{
		if (!suppress) super.print(c);	
	}
	
	@Override
	public void print(char c[])
	{
		if (!suppress) super.print(c);	
	}
	
	public boolean isSuppress()
	{
		return suppress;
	}

	public void setSuppress(boolean suppress)
	{
		this.suppress = suppress;
	}
}
