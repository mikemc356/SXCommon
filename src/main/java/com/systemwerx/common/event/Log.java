package com.systemwerx.common.event;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
/**
 * 
 * Logging interface
 * 
 * 
 * @author Mike
 *
 */

public class Log 
{
   static Logger logger = Logger.getLogger("PassGen");
   
   public Log(String configFile)
   {
      PropertyConfigurator.configure(configFile);
   }
   
   public static void setConfig(String configFile)
   {
	  PropertyConfigurator.configure(configFile);
   }
   
   public static void debug(String text)
   {
      logger.debug(text);
   }
   
   public static void info(String Text)
   {
      logger.info(Text);
   }
   
   public static void trace(String text)
   {
      logger.trace(text);
   }
   
   public static void warn(String text)
   {
      logger.warn(text);
   }
   
   public static void error(String text)
   {
      logger.error(text);
   }
   
   public static void fatal(String text)
   {
      logger.fatal(text);
   }
}
