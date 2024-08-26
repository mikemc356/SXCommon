package com.systemwerx.common.etl;

import java.io.*;
import java.*;
import java.lang.*;
import java.util.*;
import java.lang.reflect.*;


public class TranslateBean implements Serializable
{

   String ErrorMessage;
   String Filename;
   byte ConvTable[];
   int Convcount = 0;

   public TranslateBean()
   {
       ErrorMessage = "";
       ConvTable = new byte[265];
       return;
   }

    public static void main(String[] args)
    {

    }

   public boolean SetTranslateArray(String IArray) throws Exception
   {
	   if ( IArray.length() != 512 )
	   {
		   ErrorMessage = "SetTranslateArray : parameter size not 512";
		   return false;
	   }
	   
	   ConvTable = TransHexCharToBinaryData(IArray);
	   if ( Array.getLength(ConvTable) != 256 )
	   {
		   ErrorMessage = "SetTranslateArray : output array is invalid";
		   return false;
	   }
	   
	   return true;
   }
    
   public boolean LoadTable(String Filename)
   {
//       StringTokenizer st = new StringTokenizer("this is a test");
       Convcount = 0;
       int LineCount = 0;
       boolean Skip = false;
 

       String Token;

       try 
       {
       int bytes_read, loop = 1;
       int LinePos = 0;
       BufferedReader br = new BufferedReader( new FileReader(Filename));
       String inline = new String();
       
       while ( loop == 1 ) 
             {
               inline = br.readLine();
               StringTokenizer st = new StringTokenizer(inline);
               LinePos = 0;
               Skip = false;

               /* Now break line into tokens and insert into table */

               while (st.hasMoreTokens()) 
                     {
                       Token = st.nextToken();
                       if ( LinePos == 0 && Token.equals("#"))
                          {
                             Skip = true;
                             break;
                          }
                       ConvTable[Convcount] = TransHexCharToBinary(Token);
                       Convcount++;
                       LinePos++;
                       if ( LinePos == 16 ) break; // We have a full line lets get next 
                     }

               if ( LinePos < 15 && !Skip )
                  {
                     throw new TranslateBeanException("Invalid Translate table format");
                  }

               if ( !Skip ) LineCount++;
               if ( LineCount == 15 ) loop = 0;
               
             }
       
       return true;
       }
       catch ( Exception e)
       {
          Class c = e.getClass();
          String s = c.getName();
          System.out.println("Exception >>>"+s+"  "+e.getMessage());
          return false;
       }
   }

   public String TransToHex(byte Data )
   {
      int count;
      int WorkByte = 1, WorkByte1 = 1,inbyte;
      Integer TempInt;
      String outstring = new String("");

      for ( count = 0; count < 2;count++)
          {

             inbyte = Data;
             if ( count == 0 )
                {
                  inbyte = inbyte & 0xf0;
                  inbyte = inbyte >> 4;
                }
             else
                {
                  inbyte = inbyte & 0x0f;
                }

             switch (inbyte)
                    {
                       case 0x00:
                          outstring = outstring + "0";
                          break;

                       case 0x01:
                          outstring = outstring + "1";
                          break;

                       case 0x02:
                          outstring = outstring + "2";
                          break;

                       case 0x03:
                          outstring = outstring + "3";
                          break;

                       case 0x04:
                          outstring = outstring + "4";
                          break;

                       case 0x05:
                          outstring = outstring + "5";
                          break;

                       case 0x06:
                          outstring = outstring + "6";
                          break;

                       case 0x07:
                          outstring = outstring + "7";
                          break;

                       case 0x08:
                          outstring = outstring + "8";
                          break;

                       case 0x09:
                          outstring = outstring + "9";
                          break;

                       case 0x0A:
                          outstring = outstring + "A";
                          break;

                       case 0x0B:
                          outstring = outstring + "B";
                          break;

                       case 0x0C:
                          outstring = outstring + "C";
                          break;

                       case 0x0D:
                          outstring = outstring + "D";
                          break;

                       case 0x0E:
                          outstring = outstring + "E";
                          break;

                       case 0x0F:
                          outstring = outstring + "F";
                          break;
                    }

          }
   
      return outstring;
   }


//  Translate Binary data to a Hex string

   public String TransToHexData(byte Data[],int Length )
   {
    int count;
    String outstring = new String ();
    String tempstring;


    for ( count = 0; count < Length;count++)
	{
           tempstring = TransToHex(Data[count]);
           outstring = outstring + tempstring;
	}

    return outstring;
   }


// Translate Hex string to binary data

   public byte [] TransHexCharToBinaryData(String Data) throws TranslateBeanException
   {
    int count;
    byte outarray[] = new byte[Data.length()/2];
    int length = (Data.length()/2);
    String tempstring;

    if ( (Data.length()%2) != 0 )
         throw new TranslateBeanException("Invalid Parm length");


    for ( count = 0; count < length;count++)
	{
           tempstring = Data.substring(count*2,(count*2)+2); 
           outarray[count] = TransHexCharToBinary(tempstring);
	}

    return outarray;
   }

   public String GetErrorMessage()
   {
      return "";
   }

// Translate Hex string to one binary byte 

   public byte TransHexCharToBinary(String Data) throws TranslateBeanException
   {
      int count;
      int WorkByte = 1, WorkByte1 = 1,inbyte;
      byte DataBytes[] = Data.getBytes();
      byte OutByte;
      Integer TempInt;

      for ( count = 0; count < 2;count++)
          {

             if ( count == 0 )
                inbyte = DataBytes[0];
             else
                inbyte = DataBytes[1];

             switch (inbyte)
                    {
                       case '0':
                          WorkByte = (int) 0x00;
                          break;

                       case '1':
                          WorkByte = (int) 0x01;
                          break;

                       case '2':
                          WorkByte = (int) 0x02;
                          break;

                       case '3':
                          WorkByte = (int) 0x03;
                          break;

                       case '4':
                          WorkByte = (int) 0x04;
                          break;

                       case '5':
                          WorkByte = (int) 0x05;
                          break;

                       case '6':
                          WorkByte = (int) 0x06;
                          break;

                       case '7':
                          WorkByte = (int) 0x07;
                          break;

                       case '8':
                          WorkByte = (int) 0x08;
                          break;

                       case '9':
                          WorkByte = (int) 0x09;
                          break;

                       case 'A':
                       case 'a':
                          WorkByte = (int) 0x0A;
                          break;

                       case 'B':
                       case 'b':
                          WorkByte = (int) 0x0B;
                          break;

                       case 'C':
                       case 'c':
                          WorkByte = (int) 0x0C;
                          break;

                       case 'D':
                       case 'd':
                          WorkByte = (int) 0x0D;
                          break;

                       case 'E':
                       case 'e':
                          WorkByte = (int) 0x0E;
                          break;

                       case 'F':
                       case 'f':
                          WorkByte = (int) 0x0F;
                          break;
                          
                       default:
                          throw new TranslateBeanException("Invalid data in hex string");
                    }

             if ( count == 0 )
                {
                  WorkByte1 = WorkByte;
                  WorkByte1 = ( WorkByte1 << 4 );
                }
             else
                {
                  WorkByte1 = WorkByte1 | WorkByte;
                }
          }
   
      TempInt = new Integer(WorkByte1);
      OutByte =  TempInt.byteValue();
      return OutByte;
   }

public byte[] TransData(String Data)
{
	int count;
	int pos;
        int length = Data.length();
//	char Trdata[3];
        byte ansidata[];
        byte out[] = new byte[length];
        Byte val;
//        Byte val = new Byte((byte) 0);
        ansidata = Data.getBytes();
        
        
	for (count = 0;count < length;count++)
	    {
		  val = new Byte(ansidata[count]);
		  pos = val.intValue();
		  out[count] = ConvTable[pos];
	    }
    
    return out;    
}


}
