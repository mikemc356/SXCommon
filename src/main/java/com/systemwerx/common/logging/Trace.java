package com.systemwerx.common.logging;
import java.lang.*;
import javax.swing.UIManager;
import java.io.*;
import javax.swing.JOptionPane;




public class Trace
{
 public void traceMessage(String text)
 {
    java.io.StringWriter writer = new java.io.StringWriter();
    java.io.PrintWriter w = new java.io.PrintWriter(writer);
    Exception myexcept = new Exception(); 
    myexcept.printStackTrace(w);
    
    String stack = writer.toString();

    java.util.StringTokenizer toker = 
                         new java.util.StringTokenizer(stack);    
    
    // java.lang.Exception: StackTrace
    String line = toker.nextToken("\n"); 
    // at java.lang.Throwable.<init>(Throwable.java:89)
    line = toker.nextToken("\n"); 
    // at java.lang.Exception.<init>(Exception.java:53)
    line = toker.nextToken("\n"); 
    String line1 = new String(line);

    // Line is now the name of the method that called this one.

    // Remove the "... at"
    int index = line.indexOf(" ");
    line = line.substring(index + " ".length());

    // Remove the line info, JIT will mess it up anyway
    index = line.indexOf("(");
    line = line.substring(0, index);

    // find out the method name
    index = line.lastIndexOf(".");
    String method = line.substring(index + 1);

    // Class name
    String clazz = line.substring(0, index);
    
    // Get line number    
    index = line1.indexOf(":");
    int index1 = line1.indexOf(")");
    String Lin = line1.substring(index+1,index1);
    
    System.out.println((new java.util.Date().toString())+" "+clazz+"."+method+"():"+Lin+" - "+text);
 }
} 